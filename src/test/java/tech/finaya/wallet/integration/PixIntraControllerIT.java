package tech.finaya.wallet.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.DepositRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixIntraRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixIntraResponse;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.domain.usecases.CreateUser;
import tech.finaya.wallet.domain.usecases.GetBalance;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PixIntraControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CreateUser createUser;

    @Autowired
    private GetBalance getBalance;

    private UUID fromWalletId;
    private UUID toWalletId;

    @BeforeEach
    void setup() {
        User userFrom = createUser.execute(new CreateUserRequest("admin", "11111111111"));
        User userTo = createUser.execute(new CreateUserRequest("user", "22222222222"));

        fromWalletId = userFrom.getWallet().getWalletId();
        toWalletId = userTo.getWallet().getWalletId();

        webTestClient.post()
            .uri("/api/wallets/{walletId}/deposit", fromWalletId)
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new DepositRequest(BigDecimal.valueOf(1000)))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void sendPixIntra_shouldTransferFundsBetweenWallets() {
        PixIntraRequest request = new PixIntraRequest("admin@finaya.com", "user@finaya.com", BigDecimal.valueOf(250));

        String idempotencyKey = UUID.randomUUID().toString();

        PixIntraResponse response = webTestClient.post()
            .uri("/api/intra/pix")
            .header("Idempotency-Key", idempotencyKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody(PixIntraResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.fromPixKey()).isEqualTo("admin@finaya.com");
        assertThat(response.toPixKey()).isEqualTo("user@finaya.com");
        assertThat(response.amount()).isEqualTo(BigDecimal.valueOf(250));

        BigDecimal fromBalance = getBalance.execute(fromWalletId);
        BigDecimal toBalance = getBalance.execute(toWalletId);

        assertThat(fromBalance).isEqualTo(BigDecimal.valueOf(750));
        assertThat(toBalance).isEqualTo(BigDecimal.valueOf(250));
    }

    @Test
    void sendPixIntra_shouldNotAllowOverdraft() {
        PixIntraRequest request = new PixIntraRequest("admin@finaya.com", "user@finaya.com", BigDecimal.valueOf(5000));

        webTestClient.post()
            .uri("/api/intra/pix")
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().is5xxServerError();
    }
}
