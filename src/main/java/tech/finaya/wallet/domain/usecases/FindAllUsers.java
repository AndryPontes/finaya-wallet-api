package tech.finaya.wallet.domain.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.finaya.wallet.adapter.outbounds.persistence.repositories.UserRepository;
import tech.finaya.wallet.domain.models.User;

@Service
public class FindAllUsers {
    
    @Autowired
    public UserRepository repository;

    @Transactional
    public List<User> execute() {
        return repository.FindAll();
    }

}
