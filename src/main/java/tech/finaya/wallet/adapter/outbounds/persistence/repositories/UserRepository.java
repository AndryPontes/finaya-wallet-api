package tech.finaya.wallet.adapter.outbounds.persistence.repositories;

import java.util.List;

import tech.finaya.wallet.domain.models.User;

public interface UserRepository {

    User create(User user);
    
    List<User> FindAll();

}
