package tech.finaya.wallet.adapter.outbounds.persistence.repositories;

import tech.finaya.wallet.domain.models.Wallet;

public interface WalletRepository {

    Wallet create(Wallet wallet);

}
