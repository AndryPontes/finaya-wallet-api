package tech.finaya.wallet.domain.models;

public interface TransactionState {
    
    void confirm(Transaction transaction) throws IllegalArgumentException;
    void reject(Transaction transaction) throws IllegalArgumentException;

}
