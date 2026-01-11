package tech.finaya.wallet.adapter.outbounds.persistence.repositories;

import java.util.Optional;

import tech.finaya.wallet.domain.models.Transaction;

public interface TransactionRepository {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

}
