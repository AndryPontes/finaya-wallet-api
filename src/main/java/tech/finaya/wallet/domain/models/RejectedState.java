package tech.finaya.wallet.domain.models;

public class RejectedState implements TransactionState {

    @Override
    public void confirm(Transaction transaction) throws IllegalArgumentException {
        throw new IllegalStateException("A transação já foi rejeitada...");
    }

    @Override
    public void reject(Transaction transaction) throws IllegalArgumentException {
        throw new IllegalStateException("A transação já foi rejeitada...");
    }

}
