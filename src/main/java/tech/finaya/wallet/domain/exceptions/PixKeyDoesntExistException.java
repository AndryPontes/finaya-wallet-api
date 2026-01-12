package tech.finaya.wallet.domain.exceptions;

public class PixKeyDoesntExistException extends RuntimeException {
    
    public PixKeyDoesntExistException(String pixKey) {
        super(String.format("Pix key [%s] doesnt exist", pixKey));
    }

}
