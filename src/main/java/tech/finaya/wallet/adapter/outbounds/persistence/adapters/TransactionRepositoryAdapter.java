package tech.finaya.wallet.adapter.outbounds.persistence.adapters;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.finaya.wallet.adapter.outbounds.persistence.jpa.TransactionJpaRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.TransactionRepository;
import tech.finaya.wallet.domain.models.Transaction;

@Component
public class TransactionRepositoryAdapter implements TransactionRepository {
    
    @Autowired
    private TransactionJpaRepository repository;

    @Override
    public Optional<Transaction> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey);
    }

    @Override
    public Optional<Transaction> findByEndToEndId(String endToEndId) {
        return repository.findByEndToEndId(endToEndId);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

}
