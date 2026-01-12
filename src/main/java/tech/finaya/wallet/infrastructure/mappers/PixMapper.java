package tech.finaya.wallet.infrastructure.mappers;

import java.math.BigDecimal;

import tech.finaya.wallet.adapter.inbounds.dto.responses.PixResponse;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;

public class PixMapper {

    public static PixResponse toResponse(
        String endToEndId,
        String fromPixKey,
        String toPixKey,
        BigDecimal amount,
        TransactionStatus status
    ) {
        return new PixResponse(endToEndId, fromPixKey, toPixKey, amount, status);
    }

}
