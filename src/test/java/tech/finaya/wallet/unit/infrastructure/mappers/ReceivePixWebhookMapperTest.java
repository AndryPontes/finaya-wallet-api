package tech.finaya.wallet.unit.infrastructure.mappers;

import org.junit.jupiter.api.Test;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventTypeRequest;
import tech.finaya.wallet.domain.models.enums.PixWebhookEventType;
import tech.finaya.wallet.domain.models.webhook.PixWebhookEvent;
import tech.finaya.wallet.infrastructure.mappers.ReceivePixWebhookMapper;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class ReceivePixWebhookMapperTest {

    @Test
    void toEntity_shouldReturnPixWebhookEvent_whenEventTypeIsValid() {
        String eventId = "event_id";
        String endToEndId = "E123";
        LocalDateTime occurredAt = LocalDateTime.now();

        PixWebhookEventRequest request = new PixWebhookEventRequest(
            endToEndId,
            eventId,
            PixWebhookEventTypeRequest.CONFIRMED,
            occurredAt
        );

        PixWebhookEvent event = ReceivePixWebhookMapper.toEntity(request);

        assertThat(event).isNotNull();
        assertThat(event.getEventId()).isEqualTo(eventId);
        assertThat(event.getEndToEndId()).isEqualTo(endToEndId);
        assertThat(event.getEventType()).isEqualTo(PixWebhookEventType.CONFIRMED);
        assertThat(event.getOccurredAt()).isEqualTo(occurredAt);
    }

}
