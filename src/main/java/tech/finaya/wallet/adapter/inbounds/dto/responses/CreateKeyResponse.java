package tech.finaya.wallet.adapter.inbounds.dto.responses;

public record CreateKeyResponse(
    String type,
    String value,
    String walletId
) {}
