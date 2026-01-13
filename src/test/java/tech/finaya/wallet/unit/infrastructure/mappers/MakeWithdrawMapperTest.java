package tech.finaya.wallet.unit.infrastructure.mappers;

import org.junit.jupiter.api.Test;
import tech.finaya.wallet.adapter.inbounds.dto.responses.WithdrawResponse;
import tech.finaya.wallet.infrastructure.mappers.MakeWithdrawMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MakeWithdrawMapperTest {

    @Test
    void toResponse_shouldReturnWithdrawResponse_withCorrectValues() {
        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(300.00);

        WithdrawResponse response = MakeWithdrawMapper.toResponse(walletId, amount);

        assertThat(response).isNotNull();
        assertThat(response.walletId()).isEqualTo(walletId);
        assertThat(response.amount()).isEqualTo(amount);
    }
}
