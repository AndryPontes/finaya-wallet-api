package tech.finaya.wallet.domain.usecases;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.finaya.wallet.adapter.inbounds.dto.requests.PixOutRequest;
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
public class MakePixOut {
    
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
    public Transaction execute(String idempotencyKey, PixOutRequest request) {
        Wallet fromWallet = walletRepository
            .findByKey(request.fromPixKey())
            .orElseThrow(() -> new PixKeyDoesntExistException(request.fromPixKey()));

        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(idempotencyKey);

        if (existingTransaction.isPresent()) {
            return existingTransaction.get();
        }

        BigDecimal fromWalletBalanceBefore = fromWallet.getBalance();

        fromWallet.lockBalance(request.amount());

        // TODO: integra com BACEN

        Transaction transaction = createTransaction(idempotencyKey, request, fromWallet);
        createBalanceHistory(request, fromWallet, fromWalletBalanceBefore, transaction);

        Transaction transactionSaved = transactionRepository.save(transaction);

        walletRepository.save(fromWallet);

        return transactionSaved;
    }

    private void createBalanceHistory(
        PixOutRequest request, 
        Wallet fromWallet, 
        BigDecimal fromWalletBalanceBefore, 
        Transaction transaction
    ) {
        BalanceHistory fromWalletBalanceHistory = BalanceHistoryFactory.build(fromWallet, transaction, request.amount(), fromWalletBalanceBefore);
        fromWallet.addBalanceHistory(fromWalletBalanceHistory);
    }

    private Transaction createTransaction(String idempotencyKey, PixOutRequest request, Wallet fromWallet) {
        Transaction transaction = TransactionFactory.build(TransactionType.PIX, request.amount(), idempotencyKey, fromWallet, null);
        fromWallet.addFromWalletTransactions(transaction);

        return transaction;
    }

}
