package tech.finaya.wallet.domain.models;

import tech.finaya.wallet.domain.models.enums.TransactionStatus;

public class PendingState implements TransactionState {

    @Override
    public void confirm(Transaction transaction) throws IllegalArgumentException {
        transaction.setStatus(TransactionStatus.CONFIRMED);
        transaction.setState(new ConfirmedState());
    }

    @Override
    public void reject(Transaction transaction) throws IllegalArgumentException {
        transaction.setStatus(TransactionStatus.REJECTED);
        transaction.setState(new RejectedState());
    }
    
}
