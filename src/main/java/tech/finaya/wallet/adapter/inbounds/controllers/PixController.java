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
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixResponse;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.usecases.MakePix;
import tech.finaya.wallet.infrastructure.mappers.PixMapper;

@RestController
@RequestMapping("/api/pix")
public class PixController implements PixAPI {
    
    @Autowired
    private Logger log;

    @Autowired
    public MakePix makePix;

    @PostMapping
    public ResponseEntity<PixResponse> makePix(
        @RequestBody PixRequest request,
        @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey
    ) {
        log.info(
            "Making a PIX transaction from key [{}] to key [{}] of the value [{}] with idempotency key [{}]...",
            request.fromPixKey(),
            request.toPixKey(),
            request.amount(),
            idempotencyKey
        );

        Transaction transaction = makePix.execute(idempotencyKey, request);

        log.info(
            "PIX transaction made from key [{}] to key [{}] of the value [{}] with idempotency key [{}]...",
            request.fromPixKey(),
            request.toPixKey(),
            request.amount(),
            idempotencyKey
        );

        return ResponseEntity.ok(
            PixMapper.toResponse(
                transaction.getEndToEndId(), 
                request.fromPixKey(),
                request.toPixKey(),
                transaction.getAmount(),
                transaction.getStatus()
            )
        );
    }

}
