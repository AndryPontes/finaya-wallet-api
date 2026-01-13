package tech.finaya.wallet.unit.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tech.finaya.wallet.domain.models.ConfirmedState;
import tech.finaya.wallet.domain.models.PendingState;
import tech.finaya.wallet.domain.models.RejectedState;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;
import tech.finaya.wallet.domain.models.enums.TransactionType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    void setup() {
        transaction = new Transaction(
            "e2e",
            "idemKey",
            BigDecimal.valueOf(100),
            TransactionStatus.PENDING
        );
        transaction.setType(TransactionType.DEPOSIT);
        transaction.initState();
    }

    @Test
    void initState_shouldSetPendingStateInitially() {
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
        assertThat(transaction.getState()).isInstanceOf(PendingState.class);
    }

    @Test
    void confirm_shouldChangeStateToConfirmed() {
        transaction.confirm();
        assertThat(transaction.getState()).isInstanceOf(ConfirmedState.class);
    }

    @Test
    void reject_shouldChangeStateToRejected() {
        transaction.reject();
        assertThat(transaction.getState()).isInstanceOf(RejectedState.class);
    }

    @Test
    void setStatus_shouldUpdateStatusWithoutChangingState() {
        transaction.setStatus(TransactionStatus.CONFIRMED);
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.CONFIRMED);
        assertThat(transaction.getState()).isInstanceOf(PendingState.class);
    }

    @Test
    void initState_shouldUpdateStateBasedOnStatus() {
        transaction.setStatus(TransactionStatus.CONFIRMED);
        transaction.initState();
        assertThat(transaction.getState()).isInstanceOf(ConfirmedState.class);

        transaction.setStatus(TransactionStatus.REJECTED);
        transaction.initState();
        assertThat(transaction.getState()).isInstanceOf(RejectedState.class);
    }

    @Test
    void confirm_shouldntChangeStateToConfirmed() {
        Transaction transaction = new Transaction(
            "e2e",
            "idemKey",
            BigDecimal.valueOf(50),
            TransactionStatus.REJECTED
        );
        transaction.initState();
        transaction.confirm();
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.REJECTED);
    }

    @Test
    void confirm_shouldntChangeStateToRejected() {
        Transaction transaction = new Transaction(
            "e2e",
            "idemKey",
            BigDecimal.valueOf(50),
            TransactionStatus.CONFIRMED
        );
        transaction.initState();
        transaction.reject();
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.CONFIRMED);
    }
}
