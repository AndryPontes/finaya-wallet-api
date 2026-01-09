package tech.finaya.wallet.domain.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.finaya.wallet.adapter.outbounds.persistence.repositories.UserRepository;
import tech.finaya.wallet.domain.models.User;

@Service
public class CreateUser {
    
    @Autowired
    public UserRepository repository;

    public User execute(User user) {
        return repository.create(user);
    }

}
