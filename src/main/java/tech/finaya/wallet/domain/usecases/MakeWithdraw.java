package tech.finaya.wallet.domain.usecases;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.finaya.wallet.adapter.inbounds.dto.requests.WithdrawRequest;
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
public class MakeWithdraw {
    
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
    public Wallet execute(UUID walletId, String idempotencyKey, WithdrawRequest request) {
        Wallet wallet = walletRepository
            .findByWalletId(walletId)
            .orElseThrow(() -> new WalletDoesntExistException(walletId));

        if (transactionRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
            return wallet;
        }

        BigDecimal balanceBefore = wallet.getBalance();

        wallet.withdraw(request.amount());

        Transaction transaction = createTransaction(idempotencyKey, request, wallet);
        createBalanceHistory(request, wallet, balanceBefore, transaction);

        transactionRepository.save(transaction);

        return walletRepository.save(wallet);
    }

    private void createBalanceHistory(WithdrawRequest request, Wallet wallet, BigDecimal balanceBefore,
            Transaction transaction) {
        BalanceHistory balanceHistory = BalanceHistoryFactory.build(wallet, transaction, request.amount(), balanceBefore);
        wallet.addBalanceHistory(balanceHistory);
    }

    private Transaction createTransaction(String idempotencyKey, WithdrawRequest request, Wallet wallet) {
        Transaction transaction = TransactionFactory.build(TransactionType.WITHDRAW, request.amount(), idempotencyKey, wallet, null);
        transaction.confirm();
        wallet.addFromWalletTransactions(transaction);
        
        return transaction;
    }

}
