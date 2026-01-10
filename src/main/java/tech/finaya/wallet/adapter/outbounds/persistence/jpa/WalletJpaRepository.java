package tech.finaya.wallet.adapter.outbounds.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.finaya.wallet.domain.models.Wallet;

@Repository
public interface WalletJpaRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByWalletId(UUID walletId);

}
