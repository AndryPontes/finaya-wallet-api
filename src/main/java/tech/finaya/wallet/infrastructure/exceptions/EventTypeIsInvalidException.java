package tech.finaya.wallet.infrastructure.exceptions;

public class EventTypeIsInvalidException extends RuntimeException {
    
    public EventTypeIsInvalidException(String type) {
        super(String.format("Event type [%s] invalid", type));
    }

}
