package tech.finaya.wallet.adapter.inbounds.dto.responses;

public record UserResponse(
    Long id,
    String name,
    String cpf,
    Boolean isActive,
    WalletResponse wallet
) {}
