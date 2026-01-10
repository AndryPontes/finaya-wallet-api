package tech.finaya.wallet.adapter.outbounds.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.finaya.wallet.domain.models.User;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"wallet", "wallet.keys", "wallet.fromWalletTransactions", "wallet.toWalletTransactions"})
    List<User> findAll();

}
