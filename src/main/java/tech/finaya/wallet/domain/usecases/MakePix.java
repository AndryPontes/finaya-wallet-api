package tech.finaya.wallet.domain.usecases;

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
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;

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

        Optional<Transaction> transactionSaved = transactionRepository.findByIdempotencyKey(idempotencyKey);

        if (transactionSaved.isPresent()) {
            return transactionSaved.get();
        }

        Transaction transaction = fromWallet.makePixTo(idempotencyKey, toWallet, request.amount());

        transactionRepository.save(transaction);

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        return transaction;
    }

}
