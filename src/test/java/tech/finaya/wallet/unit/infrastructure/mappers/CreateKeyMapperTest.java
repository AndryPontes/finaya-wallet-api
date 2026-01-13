package tech.finaya.wallet.unit.infrastructure.mappers;

import org.junit.jupiter.api.Test;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateKeyResponse;
import tech.finaya.wallet.domain.models.Key;
import tech.finaya.wallet.domain.models.enums.KeyType;
import tech.finaya.wallet.infrastructure.exceptions.KeyTypeIsInvalidException;
import tech.finaya.wallet.infrastructure.mappers.CreateKeyMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateKeyMapperTest {

    @Test
    void toEntity_shouldReturnKey_whenTypeIsValid() {
        CreateKeyRequest request = new CreateKeyRequest("EMAIL", "admin@finaya.com");

        Key key = CreateKeyMapper.toEntity(request);

        assertThat(key).isNotNull();
        assertThat(key.getType()).isEqualTo(KeyType.EMAIL);
        assertThat(key.getValue()).isEqualTo("admin@finaya.com");
    }

    @Test
    void toEntity_shouldThrowException_whenTypeIsInvalid() {
        CreateKeyRequest request = new CreateKeyRequest("INVALID_TYPE", "admin@finaya.com");

        assertThrows(KeyTypeIsInvalidException.class, () -> CreateKeyMapper.toEntity(request));
    }

    @Test
    void toResponse_shouldReturnCreateKeyResponse_withCorrectValues() {
        Key key = new Key(KeyType.PHONE, "+5521999999999");
        UUID walletId = UUID.randomUUID();

        CreateKeyResponse response = CreateKeyMapper.toResponse(key, walletId);

        assertThat(response).isNotNull();
        assertThat(response.type()).isEqualTo("PHONE");
        assertThat(response.value()).isEqualTo("+5521999999999");
        assertThat(response.walletId()).isEqualTo(walletId.toString());
    }
}
