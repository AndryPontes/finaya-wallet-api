package tech.finaya.wallet.adapter.inbounds.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetBalanceResponse(
    UUID walletId,
    BigDecimal balance,
    LocalDateTime at
) {}
