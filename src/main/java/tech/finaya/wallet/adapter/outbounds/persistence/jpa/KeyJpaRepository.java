package tech.finaya.wallet.adapter.outbounds.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.finaya.wallet.domain.models.Key;

@Repository
public interface KeyJpaRepository extends JpaRepository<Key, Long> {

    boolean existsByValue(String value);

}
