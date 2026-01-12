package tech.finaya.wallet.domain.usecases;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.finaya.wallet.adapter.inbounds.dto.requests.PixRequest;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.TransactionRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.WalletRepository;
import tech.finaya.wallet.domain.exceptions.PixKeyDoesntExistException;
import tech.finaya.wallet.domain.models.BalanceHistory;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.enums.TransactionType;
import tech.finaya.wallet.domain.models.factories.BalanceHistoryFactory;
import tech.finaya.wallet.domain.models.factories.TransactionFactory;

@Service
public class MakePix {
    
    @Autowired
    public WalletRepository walletRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    @Retryable(
        retryFor = {OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    @Transactional
    public Transaction execute(String idempotencyKey, PixRequest request) {
        Wallet fromWallet = walletRepository
            .findByKey(request.fromPixKey())
            .orElseThrow(() -> new PixKeyDoesntExistException(request.fromPixKey()));

        Wallet toWallet = walletRepository
            .findByKey(request.toPixKey())
            .orElseThrow(() -> new PixKeyDoesntExistException(request.toPixKey()));

        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(idempotencyKey);

        if (existingTransaction.isPresent()) {
            return existingTransaction.get();
        }

        BigDecimal fromWalletBalanceBefore = fromWallet.getBalance();
        BigDecimal toWalletBalanceBefore = toWallet.getBalance();

        fromWallet.withdraw(request.amount());
        toWallet.deposit(request.amount());

        Transaction transaction = createTransaction(idempotencyKey, request, fromWallet, toWallet);
        createBalanceHistory(request, fromWallet, toWallet, fromWalletBalanceBefore, toWalletBalanceBefore, transaction);

        Transaction transactionSaved = transactionRepository.save(transaction);

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        return transactionSaved;
    }

    private void createBalanceHistory(
        PixRequest request, 
        Wallet fromWallet, 
        Wallet toWallet,
        BigDecimal fromWalletBalanceBefore, 
        BigDecimal toWalletBalanceBefore, 
        Transaction transaction
    ) {
        BalanceHistory fromWalletBalanceHistory = BalanceHistoryFactory.build(fromWallet, transaction, request.amount(), fromWalletBalanceBefore);
        fromWallet.addBalanceHistory(fromWalletBalanceHistory);

        BalanceHistory toWalletBalanceHistory = BalanceHistoryFactory.build(toWallet, transaction, request.amount(), toWalletBalanceBefore);
        toWallet.addBalanceHistory(toWalletBalanceHistory);
    }

    private Transaction createTransaction(String idempotencyKey, PixRequest request, Wallet fromWallet, Wallet toWallet) {
        Transaction transaction = TransactionFactory.build(TransactionType.PIX, request.amount(), idempotencyKey, fromWallet, toWallet);
        transaction.confirm();
        fromWallet.addFromWalletTransactions(transaction);
        toWallet.addToWalletTransactions(transaction);

        return transaction;
    }

}
