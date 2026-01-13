package tech.finaya.wallet.domain.models.webhook;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tech.finaya.wallet.domain.models.enums.PixWebhookEventType;

@Entity
@Table(name = "pix_webhook_events")
public class PixWebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, updatable = false, unique = true)
    private String eventId;

    @Column(name = "end_to_end_id", nullable = false, updatable = false)
    private String endToEndId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private PixWebhookEventType eventType;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt = LocalDateTime.now();

    protected PixWebhookEvent() {}

    public PixWebhookEvent(
        String eventId,
        String endToEndId,
        PixWebhookEventType eventType,
        LocalDateTime occurredAt
    ) {
        this.eventId = eventId;
        this.endToEndId = endToEndId;
        this.eventType = eventType;
        this.occurredAt = occurredAt;
    }

    public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public PixWebhookEventType getEventType() {
        return eventType;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    
}
