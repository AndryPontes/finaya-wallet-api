package tech.finaya.wallet.unit.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tech.finaya.wallet.domain.models.ConfirmedState;
import tech.finaya.wallet.domain.models.PendingState;
import tech.finaya.wallet.domain.models.RejectedState;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionStateTest {

    private Transaction transaction;
    private PendingState pendingState;

    @BeforeEach
    void setup() {
        transaction = new Transaction("e2e", "idemKey", BigDecimal.valueOf(10), TransactionStatus.PENDING);
        pendingState = new PendingState();
        transaction.setState(pendingState);
    }

    @Test
    void pendingState_confirm_shouldSetStatusConfirmedAndStateConfirmedState() {
        pendingState.confirm(transaction);

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.CONFIRMED);
        assertThat(transaction.getState()).isInstanceOf(ConfirmedState.class);
    }

    @Test
    void pendingState_reject_shouldSetStatusRejectedAndStateRejectedState() {
        pendingState.reject(transaction);

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.REJECTED);
        assertThat(transaction.getState()).isInstanceOf(RejectedState.class);
    }

    @Test
    void confirmedState_confirm_shouldRemainConfirmed() {
        ConfirmedState confirmedState = new ConfirmedState();
        transaction.setState(confirmedState);
        transaction.setStatus(TransactionStatus.CONFIRMED);

        confirmedState.confirm(transaction);

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.CONFIRMED);
        assertThat(transaction.getState()).isInstanceOf(ConfirmedState.class);
    }

    @Test
    void confirmedState_reject_shouldRemainConfirmed() {
        ConfirmedState confirmedState = new ConfirmedState();
        transaction.setState(confirmedState);
        transaction.setStatus(TransactionStatus.CONFIRMED);

        confirmedState.reject(transaction);

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.CONFIRMED);
        assertThat(transaction.getState()).isInstanceOf(ConfirmedState.class);
    }

    @Test
    void rejectedState_confirm_shouldRemainRejected() {
        RejectedState rejectedState = new RejectedState();
        transaction.setState(rejectedState);
        transaction.setStatus(TransactionStatus.REJECTED);

        rejectedState.confirm(transaction);

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.REJECTED);
        assertThat(transaction.getState()).isInstanceOf(RejectedState.class);
    }

    @Test
    void rejectedState_reject_shouldRemainRejected() {
        RejectedState rejectedState = new RejectedState();
        transaction.setState(rejectedState);
        transaction.setStatus(TransactionStatus.REJECTED);

        rejectedState.reject(transaction);

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.REJECTED);
        assertThat(transaction.getState()).isInstanceOf(RejectedState.class);
    }
}
