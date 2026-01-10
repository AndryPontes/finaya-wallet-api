package tech.finaya.wallet.adapter.inbounds.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import tech.finaya.wallet.domain.models.enums.TransactionStatus;

public record TransactionResponse(
    Long id,    
    String endToEndId,
    BigDecimal amount,
    LocalDateTime createdAt,
    TransactionStatus status
) {}
