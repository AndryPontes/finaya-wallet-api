package tech.finaya.wallet.adapter.outbounds.persistence.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.finaya.wallet.adapter.outbounds.persistence.jpa.PixWebhookEventJpaRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.PixWebhookEventRepository;
import tech.finaya.wallet.domain.models.webhook.PixWebhookEvent;

@Component
public class PixWebhookEventRepositoryAdapter implements PixWebhookEventRepository {
    
    @Autowired
    private PixWebhookEventJpaRepository repository;

    @Override
    public boolean existsByEventId(String eventId) {
        return repository.existsByEventId(eventId);
    }

    @Override
    public PixWebhookEvent save(PixWebhookEvent pixWebhookEvent) {
        return repository.saveAndFlush(pixWebhookEvent);
    }

}
