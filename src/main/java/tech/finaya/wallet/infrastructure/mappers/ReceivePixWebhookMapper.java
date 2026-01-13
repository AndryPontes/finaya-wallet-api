package tech.finaya.wallet.infrastructure.mappers;

import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventRequest;
import tech.finaya.wallet.domain.models.enums.PixWebhookEventType;
import tech.finaya.wallet.domain.models.webhook.PixWebhookEvent;
import tech.finaya.wallet.infrastructure.exceptions.EventTypeIsInvalidException;

public class ReceivePixWebhookMapper {

    public static PixWebhookEvent toEntity(PixWebhookEventRequest request) {
        try {
            return new PixWebhookEvent(
                request.eventId(), 
                request.endToEndId(), 
                PixWebhookEventType.valueOf(request.eventType().name().toUpperCase()), 
                request.occurredAt()
            );
        } catch (IllegalArgumentException ex) {
            throw new EventTypeIsInvalidException(request.eventType().name());
        }
    }

}
