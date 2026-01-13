package tech.finaya.wallet.unit.infrastructure.mappers;

import org.junit.jupiter.api.Test;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixOutResponse;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;
import tech.finaya.wallet.infrastructure.mappers.PixOutMapper;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PixOutMapperTest {

    @Test
    void toResponse_shouldReturnPixOutResponse_withCorrectValues() {
        String endToEndId = "E123";
        String fromPixKey = "admin@finaya.com";
        String toPixKey = "user@finaya.com";
        BigDecimal amount = BigDecimal.valueOf(450.00);
        TransactionStatus status = TransactionStatus.CONFIRMED;

        PixOutResponse response = PixOutMapper.toResponse(
            endToEndId,
            fromPixKey,
            toPixKey,
            amount,
            status
        );

        assertThat(response).isNotNull();
        assertThat(response.endToEndId()).isEqualTo(endToEndId);
        assertThat(response.fromPixKey()).isEqualTo(fromPixKey);
        assertThat(response.toPixKey()).isEqualTo(toPixKey);
        assertThat(response.amount()).isEqualTo(amount);
        assertThat(response.status()).isEqualTo(status);
    }
}
