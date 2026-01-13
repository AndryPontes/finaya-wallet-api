package tech.finaya.wallet.infrastructure.mappers;

import java.math.BigDecimal;

import tech.finaya.wallet.adapter.inbounds.dto.responses.PixOutResponse;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;

public class PixOutMapper {

    public static PixOutResponse toResponse(
        String endToEndId,
        String fromPixKey,
        String toPixKey,
        BigDecimal amount,
        TransactionStatus status
    ) {
        return new PixOutResponse(endToEndId, fromPixKey, toPixKey, amount, status);
    }

}
