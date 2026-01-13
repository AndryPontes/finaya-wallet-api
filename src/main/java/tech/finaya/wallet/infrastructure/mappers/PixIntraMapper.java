package tech.finaya.wallet.infrastructure.mappers;

import java.math.BigDecimal;

import tech.finaya.wallet.adapter.inbounds.dto.responses.PixIntraResponse;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;

public class PixIntraMapper {

    public static PixIntraResponse toResponse(
        String endToEndId,
        String fromPixKey,
        String toPixKey,
        BigDecimal amount,
        TransactionStatus status
    ) {
        return new PixIntraResponse(endToEndId, fromPixKey, toPixKey, amount, status);
    }

}
