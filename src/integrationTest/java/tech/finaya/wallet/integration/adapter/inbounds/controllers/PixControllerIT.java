package tech.finaya.wallet.integration.adapter.inbounds.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.DepositRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixOutRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventTypeRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixOutResponse;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.integration.BaseIT;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PixControllerIT extends BaseIT {

    private UUID fromWalletId;
    private String fromKeyValue;
    private Transaction transaction;

    @BeforeEach
    void setup() {
        User userFrom = createUser.execute(new CreateUserRequest("admin", "33333333333"));
        fromWalletId = userFrom.getWallet().getWalletId();
        fromKeyValue = createKey.execute(fromWalletId, new CreateKeyRequest("PHONE", "021999999999")).getValue();
        makeDeposit.execute(fromWalletId, UUID.randomUUID().toString(), new DepositRequest(BigDecimal.valueOf(600.0)));
        transaction = makePixOut.execute(UUID.randomUUID().toString(), new PixOutRequest(fromKeyValue, "user@finaya.com", BigDecimal.valueOf(100.0)));
    }

    @Test
    void sendPixOut_shouldTransferFundsAndUpdateBalance() {
        PixOutRequest request = new PixOutRequest(fromKeyValue, "user@finaya.com", BigDecimal.valueOf(300));
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
        assertThat(response.fromPixKey()).isEqualTo(fromKeyValue);
        assertThat(response.toPixKey()).isEqualTo("user@finaya.com");
        assertThat(response.amount()).isEqualTo(BigDecimal.valueOf(300));

        BigDecimal fromBalance = getBalance.execute(fromWalletId);
        assertThat(fromBalance).isEqualByComparingTo(BigDecimal.valueOf(200));
    }

    @Test
    void receiveWebhook_shouldProcessEventSuccessfully() {
        PixWebhookEventRequest webhookRequest = new PixWebhookEventRequest(
            transaction.getEndToEndId(),
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
        PixOutRequest request = new PixOutRequest(fromKeyValue, "user@finaya.com", BigDecimal.valueOf(10000));

        webTestClient.post()
            .uri("/api/pix")
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest();
    }
}
