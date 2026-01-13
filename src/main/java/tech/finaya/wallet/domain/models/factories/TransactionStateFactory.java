package tech.finaya.wallet.domain.models.factories;

import tech.finaya.wallet.domain.models.ConfirmedState;
import tech.finaya.wallet.domain.models.PendingState;
import tech.finaya.wallet.domain.models.RejectedState;
import tech.finaya.wallet.domain.models.TransactionState;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;

public final class TransactionStateFactory {

    public static TransactionState build(TransactionStatus status) {
        switch (status) {
            case PENDING:
                return new PendingState();
            case CONFIRMED:
                return new ConfirmedState();
            case REJECTED:
                return new RejectedState();
            default:
                throw new IllegalArgumentException(String.format("Invalid state: %s", status));
        }
    }
}
