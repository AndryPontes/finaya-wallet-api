package tech.finaya.wallet.unit.infrastructure.mappers;

import org.junit.jupiter.api.Test;
import tech.finaya.wallet.adapter.inbounds.dto.responses.GetBalanceResponse;
import tech.finaya.wallet.infrastructure.mappers.GetBalanceMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetBalanceMapperTest {

    @Test
    void toResponse_shouldReturnGetBalanceResponse_withCorrectValues() {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(1500.75);
        LocalDateTime at = LocalDateTime.now();

        GetBalanceResponse response = GetBalanceMapper.toResponse(walletId, balance, at);

        assertThat(response).isNotNull();
        assertThat(response.walletId()).isEqualTo(walletId);
        assertThat(response.balance()).isEqualTo(balance);
        assertThat(response.at()).isEqualTo(at);
    }
}
