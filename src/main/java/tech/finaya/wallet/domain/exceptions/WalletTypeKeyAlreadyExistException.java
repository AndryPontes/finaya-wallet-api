package tech.finaya.wallet.domain.exceptions;

public class WalletTypeKeyAlreadyExistException extends RuntimeException {
    
    public WalletTypeKeyAlreadyExistException(String type) {
        super(String.format("Wallet type key [%s] already exist", type));
    }

}
