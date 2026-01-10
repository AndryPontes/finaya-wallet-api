package tech.finaya.wallet.infrastructure.exceptions;

public class KeyTypeInvalidException extends RuntimeException {
    
    public KeyTypeInvalidException(String type) {
        super(String.format("Type [%s] invalid", type));
    }

}
