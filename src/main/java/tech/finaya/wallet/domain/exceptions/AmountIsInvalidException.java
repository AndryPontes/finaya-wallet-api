package tech.finaya.wallet.domain.exceptions;

public class AmountIsInvalidException extends RuntimeException {
    
    public AmountIsInvalidException(String msg) {
        super(msg);
    }

}
