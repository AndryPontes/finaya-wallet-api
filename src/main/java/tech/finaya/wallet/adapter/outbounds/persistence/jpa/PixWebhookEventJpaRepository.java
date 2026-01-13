package tech.finaya.wallet.adapter.outbounds.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.finaya.wallet.domain.models.webhook.PixWebhookEvent;

@Repository
public interface PixWebhookEventJpaRepository extends JpaRepository<PixWebhookEvent, Long> {

    boolean existsByEventId(String eventId);

}
