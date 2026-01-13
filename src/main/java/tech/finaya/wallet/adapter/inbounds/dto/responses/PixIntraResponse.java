package tech.finaya.wallet.adapter.inbounds.dto.responses;

import java.math.BigDecimal;

import tech.finaya.wallet.domain.models.enums.TransactionStatus;

public record PixIntraResponse(
    String endToEndId,
    String fromPixKey,
    String toPixKey,
    BigDecimal amount,
    TransactionStatus status
) {}
