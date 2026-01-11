package tech.finaya.wallet.domain.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    
    public InsufficientBalanceException(String msg) {
        super(msg);
    }

}
