package tech.finaya.wallet.adapter.inbounds.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.finaya.wallet.adapter.inbounds.controllers.api.PixAPI;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixOutRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixOutResponse;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.usecases.MakePixOut;
import tech.finaya.wallet.domain.usecases.ReceivePixWebhook;
import tech.finaya.wallet.infrastructure.mappers.PixOutMapper;

@RestController
@RequestMapping("/api/pix")
public class PixController implements PixAPI {
    
    @Autowired
    private Logger log;

    @Autowired
    public MakePixOut makePixOut;

    @Autowired
    public ReceivePixWebhook receivePixWebhook;

    @PostMapping
    public ResponseEntity<PixOutResponse> send(
        @RequestBody PixOutRequest request,
        @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey
    ) {
        log.info(
            "Making a PIX out transaction from key [{}] to key [{}] of the value [{}] with idempotency key [{}]...",
            request.fromPixKey(),
            request.toPixKey(),
            request.amount(),
            idempotencyKey
        );

        Transaction transaction = makePixOut.execute(idempotencyKey, request);

        log.info(
            "PIX out transaction made from key [{}] to key [{}] of the value [{}] with idempotency key [{}]...",
            request.fromPixKey(),
            request.toPixKey(),
            request.amount(),
            idempotencyKey
        );

        return ResponseEntity.ok(
            PixOutMapper.toResponse(
                transaction.getEndToEndId(), 
                request.fromPixKey(),
                request.toPixKey(),
                transaction.getAmount(),
                transaction.getStatus()
            )
        );
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveWebhook(
        @RequestBody PixWebhookEventRequest request
    ) {
        log.info(
            "Received PIX webhook event [{}] for transaction [{}] of type [{}] at [{}]...",
            request.eventId(),
            request.endToEndId(),
            request.eventType(),
            request.occurredAt()
        );

        receivePixWebhook.execute(request);

        log.info(
            "Processed PIX webhook event [{}] for transaction [{}] of type [{}]...",
            request.eventId(),
            request.endToEndId(),
            request.eventType().name()
        );

        return ResponseEntity.ok().build();
    }

}
