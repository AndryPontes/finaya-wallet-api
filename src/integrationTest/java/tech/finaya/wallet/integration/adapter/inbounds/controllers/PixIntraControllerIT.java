package tech.finaya.wallet.integration.adapter.inbounds.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.DepositRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixIntraRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixIntraResponse;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.integration.BaseIT;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PixIntraControllerIT extends BaseIT {

    private UUID fromWalletId;
    private UUID toWalletId;
    private String fromKeyValue;
    private String toKeyValue;

    @BeforeEach
    void setup() {
        User userFrom = createUser.execute(new CreateUserRequest("admin", "11111111111"));
        User userTo = createUser.execute(new CreateUserRequest("user", "22222222222"));

        fromWalletId = userFrom.getWallet().getWalletId();
        toWalletId = userTo.getWallet().getWalletId();

        fromKeyValue = createKey.execute(fromWalletId, new CreateKeyRequest("PHONE", "021999999999")).getValue();
        toKeyValue = createKey.execute(toWalletId, new CreateKeyRequest("EMAIL", "admin@finaya.com")).getValue();

        makeDeposit.execute(fromWalletId, UUID.randomUUID().toString(), new DepositRequest(BigDecimal.valueOf(1000.0)));
    }

    @Test
    void sendPixIntra_shouldTransferFundsBetweenWallets() {
        PixIntraRequest request = new PixIntraRequest(fromKeyValue, toKeyValue, BigDecimal.valueOf(250));

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
        assertThat(response.fromPixKey()).isEqualTo(fromKeyValue);
        assertThat(response.toPixKey()).isEqualTo(toKeyValue);
        assertThat(response.amount()).isEqualTo(BigDecimal.valueOf(250));

        BigDecimal fromBalance = getBalance.execute(fromWalletId);
        BigDecimal toBalance = getBalance.execute(toWalletId);

        assertThat(fromBalance).isEqualByComparingTo(BigDecimal.valueOf(750.0));
        assertThat(toBalance).isEqualByComparingTo(BigDecimal.valueOf(250.0));
    }

    @Test
    void sendPixIntra_shouldNotAllowOverdraft() {
        PixIntraRequest request = new PixIntraRequest(fromKeyValue, toKeyValue, BigDecimal.valueOf(5000));

        webTestClient.post()
            .uri("/api/intra/pix")
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isBadRequest();
    }
}
