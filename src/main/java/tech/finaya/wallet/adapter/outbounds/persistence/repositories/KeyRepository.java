package tech.finaya.wallet.adapter.outbounds.persistence.repositories;

public interface KeyRepository {

    boolean existsByValue(String value);

}
