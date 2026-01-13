package tech.finaya.wallet.adapter.inbounds.dto.requests;

import java.math.BigDecimal;

public record PixOutRequest(String fromPixKey, String toPixKey, BigDecimal amount) {}
