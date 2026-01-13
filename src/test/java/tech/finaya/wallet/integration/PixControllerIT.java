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
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixOutRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventTypeRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixOutResponse;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.domain.usecases.CreateUser;
import tech.finaya.wallet.domain.usecases.GetBalance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PixControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CreateUser createUser;

    @Autowired
    private GetBalance getBalance;

    private UUID fromWalletId;

    @BeforeEach
    void setup() {
        User userFrom = createUser.execute(new CreateUserRequest("admin", "33333333333"));

        fromWalletId = userFrom.getWallet().getWalletId();

        webTestClient.post()
            .uri("/api/wallets/{walletId}/deposit", fromWalletId)
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new DepositRequest(BigDecimal.valueOf(1000)))
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void sendPixOut_shouldTransferFundsAndUpdateBalance() {
        PixOutRequest request = new PixOutRequest("admin@finaya.com", "user@finaya.com", BigDecimal.valueOf(300));
        String idempotencyKey = UUID.randomUUID().toString();

        PixOutResponse response = webTestClient.post()
            .uri("/api/pix")
            .header("Idempotency-Key", idempotencyKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody(PixOutResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.fromPixKey()).isEqualTo("admin@finaya.com");
        assertThat(response.toPixKey()).isEqualTo("user@finaya.com");
        assertThat(response.amount()).isEqualTo(BigDecimal.valueOf(300));

        BigDecimal fromBalance = getBalance.execute(fromWalletId);
        assertThat(fromBalance).isEqualTo(BigDecimal.valueOf(700));
    }

    @Test
    void receiveWebhook_shouldProcessEventSuccessfully() {
        String endToEndId = UUID.randomUUID().toString();

        PixWebhookEventRequest webhookRequest = new PixWebhookEventRequest(
            endToEndId,
            UUID.randomUUID().toString(),
            PixWebhookEventTypeRequest.CONFIRMED,
            LocalDateTime.now()
        );

        webTestClient.post()
            .uri("/api/pix/webhook")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(webhookRequest)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void sendPixOut_shouldFailOnInsufficientBalance() {
        PixOutRequest request = new PixOutRequest("admin@finaya.com", "user@finaya.com", BigDecimal.valueOf(5000));

        webTestClient.post()
            .uri("/api/pix")
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().is5xxServerError();
    }
}
