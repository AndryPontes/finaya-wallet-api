package tech.finaya.wallet.adapter.outbounds.persistence.repositories;

import tech.finaya.wallet.domain.models.webhook.PixWebhookEvent;

public interface PixWebhookEventRepository {

    boolean existsByEventId(String eventId);

    PixWebhookEvent save(PixWebhookEvent pixWebhookEvent);

}
