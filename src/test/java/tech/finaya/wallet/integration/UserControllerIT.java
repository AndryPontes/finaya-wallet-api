package tech.finaya.wallet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateUserResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

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
