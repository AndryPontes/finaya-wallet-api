package tech.finaya.wallet.integration.adapter.inbounds.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.DepositRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.WithdrawRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateKeyResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.DepositResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.GetBalanceResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.WithdrawResponse;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.integration.BaseIT;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class WalletControllerIT extends BaseIT {

    private UUID walletId;

    @BeforeEach
    void setup() {
        User user = createUser.execute(new CreateUserRequest("admin", "33333333333"));
        this.walletId = user.getWallet().getWalletId();
    }

    @Test
    void getBalance_shouldReturnInitialBalance() {
        GetBalanceResponse response = webTestClient.get()
            .uri("/api/wallets/{walletId}/balance", walletId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(GetBalanceResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.balance()).isEqualByComparingTo(BigDecimal.valueOf(0));
    }

    @Test
    void deposit_shouldIncreaseWalletBalance() {
        DepositRequest request = new DepositRequest(BigDecimal.valueOf(100.00));

        DepositResponse response = webTestClient.post()
            .uri("/api/wallets/{walletId}/deposit", walletId)
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody(DepositResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));

        GetBalanceResponse balance = webTestClient.get()
            .uri("/api/wallets/{walletId}/balance", walletId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(GetBalanceResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(balance.balance()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void withdraw_shouldDecreaseWalletBalance() {
        webTestClient.post()
            .uri("/api/wallets/{walletId}/deposit", walletId)
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new DepositRequest(BigDecimal.valueOf(200.00)))
            .exchange()
            .expectStatus().isOk();

        WithdrawRequest request = new WithdrawRequest(BigDecimal.valueOf(50.00));

        WithdrawResponse response = webTestClient.post()
            .uri("/api/wallets/{walletId}/withdraw", walletId)
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody(WithdrawResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(response.amount()).isEqualByComparingTo(BigDecimal.valueOf(50));

        GetBalanceResponse balance = webTestClient.get()
            .uri("/api/wallets/{walletId}/balance", walletId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(GetBalanceResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(balance.balance()).isEqualByComparingTo(BigDecimal.valueOf(150));
    }

    @Test
    void createKey_shouldAddKeyToWallet() {
        CreateKeyRequest request = new CreateKeyRequest("EMAIL", "admin@finaya.com");

        CreateKeyResponse response = webTestClient.post()
            .uri("/api/wallets/{walletId}/key", walletId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk()
            .expectBody(CreateKeyResponse.class)
            .returnResult()
            .getResponseBody();

        assertThat(response.walletId()).isEqualTo(walletId.toString());
        assertThat(response.value()).isEqualTo("admin@finaya.com");
    }
}
