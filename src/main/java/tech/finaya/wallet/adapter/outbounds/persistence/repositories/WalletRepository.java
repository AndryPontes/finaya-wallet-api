package tech.finaya.wallet.adapter.outbounds.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import tech.finaya.wallet.domain.models.Wallet;

public interface WalletRepository {

    Optional<Wallet> findByWalletId(UUID walletId);

    Optional<Wallet> findByKey(String key);
    
    Wallet save(Wallet wallet);

}
