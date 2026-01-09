package tech.finaya.wallet.adapter.inbounds.dto.responses;

public record CreateUserResponse(
    String name,
    Boolean isActive
) {}
