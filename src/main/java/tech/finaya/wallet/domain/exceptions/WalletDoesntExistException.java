package tech.finaya.wallet.domain.exceptions;

import java.util.UUID;

public class WalletDoesntExistException extends RuntimeException {
    
    public WalletDoesntExistException(UUID walletId) {
        super(String.format("Wallet [%s] doesnt exist", walletId));
    }

}
