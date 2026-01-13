package tech.finaya.wallet.unit.infrastructure.mappers;

import org.junit.jupiter.api.Test;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateUserResponse;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.infrastructure.mappers.CreateUserMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserMapperTest {

    @Test
    void toEntity_shouldReturnUser_withCorrectValues() {
        CreateUserRequest request = new CreateUserRequest("Finaya", "12345678901");

        User user = CreateUserMapper.toEntity(request);

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Finaya");
        assertThat(user.getCpf()).isEqualTo("12345678901");
        assertThat(user.getWallet()).isNotNull();
        assertThat(user.isActive()).isTrue();
    }

    @Test
    void toResponse_shouldReturnCreateUserResponse_withCorrectValues() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId);
        User user = new User("Finaya", "98765432100");
        user.setWallet(wallet);
        user.setActive(true);

        CreateUserResponse response = CreateUserMapper.toResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Finaya");
        assertThat(response.cpf()).isEqualTo("98765432100");
        assertThat(response.isActive()).isTrue();
        assertThat(response.walletId()).isEqualTo(walletId);
    }
}
