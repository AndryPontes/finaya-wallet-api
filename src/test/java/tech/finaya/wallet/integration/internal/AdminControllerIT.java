package tech.finaya.wallet.integration.internal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.UserResponse;
import tech.finaya.wallet.domain.usecases.CreateUser;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CreateUser createUser;

    @Test
    void findAllUsers_shouldReturnAllUsers() {
        createUser.execute(new CreateUserRequest("Admin", "11111111111"));
        createUser.execute(new CreateUserRequest("User", "22222222222"));

        List<UserResponse> response = webTestClient.get()
            .uri("/internal/users")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UserResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.size()).isGreaterThanOrEqualTo(2);
        assertThat(response.stream().anyMatch(u -> u.name().equals("Admin"))).isTrue();
        assertThat(response.stream().anyMatch(u -> u.name().equals("User"))).isTrue();
    }
}
