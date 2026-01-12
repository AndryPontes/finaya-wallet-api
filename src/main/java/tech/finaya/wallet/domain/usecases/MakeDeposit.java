package tech.finaya.wallet.domain.usecases;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.finaya.wallet.adapter.inbounds.dto.requests.DepositRequest;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.TransactionRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.WalletRepository;
import tech.finaya.wallet.domain.exceptions.WalletDoesntExistException;
import tech.finaya.wallet.domain.models.BalanceHistory;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.enums.TransactionType;
import tech.finaya.wallet.domain.models.factories.BalanceHistoryFactory;
import tech.finaya.wallet.domain.models.factories.TransactionFactory;

@Service
public class MakeDeposit {
    
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
    public Wallet execute(UUID walletId, String idempotencyKey, DepositRequest request) {
        Wallet wallet = walletRepository
            .findByWalletId(walletId)
            .orElseThrow(() -> new WalletDoesntExistException(walletId));

        if (transactionRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
            return wallet;
        }

        BigDecimal balanceBefore = wallet.getBalance();

        wallet.deposit(request.amount());

        Transaction transaction = createTransaction(idempotencyKey, request, wallet);
        createBalanceHistory(request, wallet, balanceBefore, transaction);

        transactionRepository.save(transaction);

        return walletRepository.save(wallet);
    }

    private void createBalanceHistory(DepositRequest request, Wallet wallet, BigDecimal balanceBefore, Transaction transaction) {
        BalanceHistory balanceHistory = BalanceHistoryFactory.build(wallet, transaction, request.amount(), balanceBefore);
        wallet.addBalanceHistory(balanceHistory);
    }

    private Transaction createTransaction(String idempotencyKey, DepositRequest request, Wallet wallet) {
        Transaction transaction = TransactionFactory.build(TransactionType.DEPOSIT, request.amount(), idempotencyKey, null, wallet);
        transaction.confirm();
        wallet.addToWalletTransactions(transaction);

        return transaction;
    }

}
