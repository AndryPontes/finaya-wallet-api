package tech.finaya.wallet.adapter.outbounds.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.finaya.wallet.domain.models.Transaction;

@Repository
public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

}
