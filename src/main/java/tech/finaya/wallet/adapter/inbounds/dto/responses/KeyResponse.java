package tech.finaya.wallet.adapter.inbounds.dto.responses;

import tech.finaya.wallet.domain.models.enums.KeyType;

public record KeyResponse(
    Long id,
    String value,
    KeyType type
) {}
