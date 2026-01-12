package tech.finaya.wallet.adapter.inbounds.dto.requests;

import java.math.BigDecimal;

public record PixRequest(String fromPixKey, String toPixKey, BigDecimal amount) {}
