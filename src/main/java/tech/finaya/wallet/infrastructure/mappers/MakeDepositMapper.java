package tech.finaya.wallet.infrastructure.mappers;

import tech.finaya.wallet.adapter.inbounds.dto.responses.DepositResponse;
import tech.finaya.wallet.domain.models.Wallet;

public class MakeDepositMapper {

    public static DepositResponse toResponse(Wallet wallet) {
        return new DepositResponse(wallet.getWalletId(), wallet.getBalance());
    }

}
