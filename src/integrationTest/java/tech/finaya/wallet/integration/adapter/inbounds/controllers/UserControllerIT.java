package tech.finaya.wallet.integration.adapter.inbounds.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateUserResponse;
import tech.finaya.wallet.integration.BaseIT;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerIT extends BaseIT {

    @Test
    void createUser_shouldReturnCreatedUserResponse() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Admin", "12345678900");

        CreateUserResponse response = webTestClient.post()
            .uri("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString(request))
            .exchange()
            .expectStatus().isOk()
            .expectBody(CreateUserResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Admin");
        assertThat(response.cpf()).isEqualTo("12345678900");
        assertThat(response.isActive()).isTrue();
        assertThat(response.walletId()).isNotNull();
    }

}
