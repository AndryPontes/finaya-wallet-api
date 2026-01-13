package tech.finaya.wallet.domain.models;

public class RejectedState implements TransactionState {

    @Override
    public void confirm(Transaction transaction) throws IllegalArgumentException {
        // ignorando por ser idempotente
        // throw new IllegalStateException("The transaction has already been rejected.");
    }

    @Override
    public void reject(Transaction transaction) throws IllegalArgumentException {
        // ignorando por ser idempotente
        // throw new IllegalStateException("The transaction has already been rejected.");
    }

}
