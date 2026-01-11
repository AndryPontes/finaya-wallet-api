package tech.finaya.wallet.infrastructure.mappers;

import java.math.BigDecimal;
import java.util.UUID;

import tech.finaya.wallet.adapter.inbounds.dto.responses.DepositResponse;

public class MakeDepositMapper {

    public static DepositResponse toResponse(UUID walletId, BigDecimal amount) {
        return new DepositResponse(walletId, amount);
    }

}
