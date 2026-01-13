package tech.finaya.wallet.unit.infrastructure.mappers;

import org.junit.jupiter.api.Test;
import tech.finaya.wallet.adapter.inbounds.dto.responses.DepositResponse;
import tech.finaya.wallet.infrastructure.mappers.MakeDepositMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MakeDepositMapperTest {

    @Test
    void toResponse_shouldReturnDepositResponse_withCorrectValues() {
        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(500.00);

        DepositResponse response = MakeDepositMapper.toResponse(walletId, amount);

        assertThat(response).isNotNull();
        assertThat(response.walletId()).isEqualTo(walletId);
        assertThat(response.amount()).isEqualTo(amount);
    }
}
