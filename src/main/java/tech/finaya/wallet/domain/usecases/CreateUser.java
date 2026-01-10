package tech.finaya.wallet.domain.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.UserRepository;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.infrastructure.mappers.CreateUserMapper;

@Service
public class CreateUser {
    
    @Autowired
    public UserRepository repository;

    @Transactional
    public User execute(CreateUserRequest request) {
        User user = CreateUserMapper.toEntity(request);
        
        return repository.create(user);
    }

}
