package tech.finaya.wallet.infrastructure.mappers;

import java.math.BigDecimal;
import java.util.UUID;

import tech.finaya.wallet.adapter.inbounds.dto.responses.WithdrawResponse;

public class MakeWithdrawMapper {

    public static WithdrawResponse toResponse(UUID walletId, BigDecimal amount) {
        return new WithdrawResponse(walletId, amount);
    }

}
