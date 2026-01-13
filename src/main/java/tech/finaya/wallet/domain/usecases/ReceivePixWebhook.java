package tech.finaya.wallet.domain.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventTypeRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventRequest;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.PixWebhookEventRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.TransactionRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.WalletRepository;
import tech.finaya.wallet.domain.exceptions.TransactionDoesntExistException;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.infrastructure.mappers.ReceivePixWebhookMapper;

@Service
public class ReceivePixWebhook {

    @Autowired
    public WalletRepository walletRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    @Autowired
    private PixWebhookEventRepository pixWebhookEventRepository;

    @Retryable(
        retryFor = {OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    @Transactional
    public Transaction execute(PixWebhookEventRequest request) {
        Transaction transaction = transactionRepository
            .findByEndToEndId(request.endToEndId())
            .orElseThrow(() -> new TransactionDoesntExistException(request.endToEndId()));

        if (pixWebhookEventRepository.existsByEventId(request.eventId())) {
            return transaction;
        }

        switch (request.eventType()) {
            case PixWebhookEventTypeRequest.CONFIRMED -> {
                transaction.getFromWallet().confirmLockedBalance(transaction.getAmount());
                transaction.confirm();
            }
            case PixWebhookEventTypeRequest.REJECTED -> {
                transaction.getFromWallet().rejectLockedBalance(transaction.getAmount());
                transaction.reject();
            }
        }

        transactionRepository.save(transaction);

        walletRepository.save(transaction.getFromWallet());
        
        pixWebhookEventRepository.save(ReceivePixWebhookMapper.toEntity(request));

        return transaction;
    }

}
