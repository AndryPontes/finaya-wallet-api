package tech.finaya.wallet.adapter.inbounds.dto.requests;

import java.time.LocalDateTime;

public record PixWebhookEventRequest(String endToEndId, String eventId, PixWebhookEventTypeRequest eventType, LocalDateTime occurredAt) {}
