package tech.finaya.wallet.adapter.outbounds.persistence.adapters;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.finaya.wallet.adapter.outbounds.persistence.jpa.WalletJpaRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.WalletRepository;
import tech.finaya.wallet.domain.models.Wallet;

@Component
public class WalletRepositoryAdapter implements WalletRepository {
    
    @Autowired
    private WalletJpaRepository repository;

    @Override
    public Optional<Wallet> findByWalletId(UUID walletId) {
        return repository.findByWalletId(walletId);
    }

    @Override
    public Wallet save(Wallet wallet) {
        return repository.saveAndFlush(wallet);
    }

}
