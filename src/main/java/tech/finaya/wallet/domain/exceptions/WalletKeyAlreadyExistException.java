package tech.finaya.wallet.domain.exceptions;

public class WalletKeyAlreadyExistException extends RuntimeException {
    
    public WalletKeyAlreadyExistException(String value) {
        super(String.format("Wallet key [%s] already exist", value));
    }

}
