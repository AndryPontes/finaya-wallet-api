package tech.finaya.wallet.adapter.inbounds.dto.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawResponse(
    UUID walletId,
    BigDecimal amount
) {}
