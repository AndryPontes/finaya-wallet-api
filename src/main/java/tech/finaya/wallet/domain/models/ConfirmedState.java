package tech.finaya.wallet.domain.models;

public class ConfirmedState implements TransactionState {

    @Override
    public void confirm(Transaction transaction) throws IllegalArgumentException {
        throw new IllegalStateException("A transação já foi confirmada...");
    }

    @Override
    public void reject(Transaction transaction) throws IllegalArgumentException {
        throw new IllegalStateException("A transação já foi confirmada...");
    }
    
}
