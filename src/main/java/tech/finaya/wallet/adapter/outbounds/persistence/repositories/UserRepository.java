package tech.finaya.wallet.adapter.outbounds.persistence.repositories;

import java.util.List;

import tech.finaya.wallet.domain.models.User;

public interface UserRepository {

    User save(User user);
    
    List<User> FindAll();

    boolean existsByCpf(String cpf);

}
