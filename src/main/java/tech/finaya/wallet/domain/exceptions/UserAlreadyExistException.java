package tech.finaya.wallet.domain.exceptions;

public class UserAlreadyExistException extends RuntimeException {
    
    public UserAlreadyExistException(String cpf) {
        super(String.format("User with cpf [%s] already exist", cpf));
    }

}
