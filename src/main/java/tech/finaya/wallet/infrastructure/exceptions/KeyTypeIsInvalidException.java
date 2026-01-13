package tech.finaya.wallet.infrastructure.exceptions;

public class KeyTypeIsInvalidException extends RuntimeException {
    
    public KeyTypeIsInvalidException(String type) {
        super(String.format("Key type [%s] invalid", type));
    }

}
