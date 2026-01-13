package tech.finaya.wallet.adapter.inbounds.dto.requests;

import java.math.BigDecimal;

public record PixIntraRequest(String fromPixKey, String toPixKey, BigDecimal amount) {}
