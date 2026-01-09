package tech.finaya.wallet.adapter.outbounds.persistence.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tech.finaya.wallet.domain.models.User;

@Repository
public interface UserJpaRepository extends CrudRepository<User, Long> {}
