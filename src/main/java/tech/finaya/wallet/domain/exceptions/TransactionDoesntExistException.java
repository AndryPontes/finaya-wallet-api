package tech.finaya.wallet.domain.exceptions;

public class TransactionDoesntExistException extends RuntimeException {
    
    public TransactionDoesntExistException(String endToEndId) {
        super(String.format("Transaction with endToEndId [%s] doesnt exist", endToEndId));
    }

}
