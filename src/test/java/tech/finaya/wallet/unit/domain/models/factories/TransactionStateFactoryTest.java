package tech.finaya.wallet.unit.domain.models.factories;

import org.junit.jupiter.api.Test;
import tech.finaya.wallet.domain.models.*;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;
import tech.finaya.wallet.domain.models.factories.TransactionStateFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionStateFactoryTest {

    @Test
    void build_shouldReturnPendingState_whenStatusIsPending() {
        TransactionState state = TransactionStateFactory.build(TransactionStatus.PENDING);
        assertThat(state).isInstanceOf(PendingState.class);
    }

    @Test
    void build_shouldReturnConfirmedState_whenStatusIsConfirmed() {
        TransactionState state = TransactionStateFactory.build(TransactionStatus.CONFIRMED);
        assertThat(state).isInstanceOf(ConfirmedState.class);
    }

    @Test
    void build_shouldReturnRejectedState_whenStatusIsRejected() {
        TransactionState state = TransactionStateFactory.build(TransactionStatus.REJECTED);
        assertThat(state).isInstanceOf(RejectedState.class);
    }

    @Test
    void build_shouldThrowException_whenStatusIsNullOrInvalid() {
        assertThrows(IllegalArgumentException.class, () -> TransactionStateFactory.build(null));
    }
}
