package tech.finaya.wallet.adapter.outbounds.persistence.adapters;

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
    public Wallet create(Wallet wallet) {
        return repository.save(wallet);
    }

}
