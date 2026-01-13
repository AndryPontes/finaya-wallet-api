package tech.finaya.wallet.integration.adapter.inbounds.controllers.internal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.UserResponse;
import tech.finaya.wallet.integration.BaseIT;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdminControllerIT extends BaseIT {

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
