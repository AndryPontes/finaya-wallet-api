package tech.finaya.wallet.adapter.inbounds.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.finaya.wallet.adapter.inbounds.controllers.api.PixIntraAPI;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixIntraRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixIntraResponse;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.usecases.MakePix;
import tech.finaya.wallet.infrastructure.mappers.PixIntraMapper;

@RestController
@RequestMapping("/api/intra/pix")
public class PixIntraController implements PixIntraAPI {
    
    @Autowired
    private Logger log;

    @Autowired
    public MakePix makePixIntra;

    @PostMapping
    public ResponseEntity<PixIntraResponse> send(
        @RequestBody PixIntraRequest request,
        @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey
    ) {
        log.info(
            "Making a PIX intra transaction from key [{}] to key [{}] of the value [{}] with idempotency key [{}]...",
            request.fromPixKey(),
            request.toPixKey(),
            request.amount(),
            idempotencyKey
        );

        Transaction transaction = makePixIntra.execute(idempotencyKey, request);

        log.info(
            "PIX intra transaction made from key [{}] to key [{}] of the value [{}] with idempotency key [{}]...",
            request.fromPixKey(),
            request.toPixKey(),
            request.amount(),
            idempotencyKey
        );

        return ResponseEntity.ok(
            PixIntraMapper.toResponse(
                transaction.getEndToEndId(), 
                request.fromPixKey(),
                request.toPixKey(),
                transaction.getAmount(),
                transaction.getStatus()
            )
        );
    }

}
