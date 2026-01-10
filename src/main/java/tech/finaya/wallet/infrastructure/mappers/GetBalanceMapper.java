package tech.finaya.wallet.infrastructure.mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import tech.finaya.wallet.adapter.inbounds.dto.responses.GetBalanceResponse;

public class GetBalanceMapper {
    
    public static GetBalanceResponse toResponse(UUID walletId, BigDecimal balance, LocalDateTime at) {
        return new GetBalanceResponse(walletId, balance, at);
    }

}
