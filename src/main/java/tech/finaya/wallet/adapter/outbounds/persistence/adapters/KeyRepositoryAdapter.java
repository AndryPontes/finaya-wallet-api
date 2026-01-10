package tech.finaya.wallet.adapter.outbounds.persistence.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.finaya.wallet.adapter.outbounds.persistence.jpa.KeyJpaRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.KeyRepository;

@Component
public class KeyRepositoryAdapter implements KeyRepository {
    
    @Autowired
    private KeyJpaRepository repository;

    @Override
    public boolean existsByValue(String value) {
        return repository.existsByValue(value);
    }

}
