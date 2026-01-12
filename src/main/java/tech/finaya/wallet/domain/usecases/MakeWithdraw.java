package tech.finaya.wallet.domain.usecases;

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
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;

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

        Transaction transaction = wallet.withdraw(idempotencyKey, request.amount());        

        transactionRepository.save(transaction);

        return walletRepository.save(wallet);
    }

}
