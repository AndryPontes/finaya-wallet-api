package tech.finaya.wallet.adapter.inbounds.dto.requests;

import java.math.BigDecimal;

public record WithdrawRequest(BigDecimal amount) {}
