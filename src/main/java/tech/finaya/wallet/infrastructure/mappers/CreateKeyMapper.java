package tech.finaya.wallet.infrastructure.mappers;

import java.util.UUID;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateKeyResponse;
import tech.finaya.wallet.domain.models.Key;
import tech.finaya.wallet.domain.models.enums.KeyType;
import tech.finaya.wallet.infrastructure.exceptions.KeyTypeInvalidException;

public class CreateKeyMapper {
    
    // nao utilizei o converter do spring para nao expor a entidade key no controller (requestbody)
    public static Key toEntity(CreateKeyRequest request) {
        try {
            return new Key(KeyType.valueOf(request.type().toUpperCase()), request.value());
        } catch (IllegalArgumentException ex) {
            throw new KeyTypeInvalidException(request.type());
        }
    }

    public static CreateKeyResponse toResponse(Key key, UUID walletId) {
        return new CreateKeyResponse(key.getType().name(), key.getValue(), walletId.toString());
    }

}
