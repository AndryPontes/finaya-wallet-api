package tech.finaya.wallet.adapter.outbounds.persistence.adapters;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.finaya.wallet.adapter.outbounds.persistence.jpa.UserJpaRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.UserRepository;
import tech.finaya.wallet.domain.models.User;

@Component
public class UserRepositoryAdapter implements UserRepository {
    
    @Autowired
    private UserJpaRepository repository;

    @Override
    public User create(User user) {
        return repository.saveAndFlush(user);
    }

    @Override
    public List<User> FindAll() {
        return repository.findAll();
    }

}
