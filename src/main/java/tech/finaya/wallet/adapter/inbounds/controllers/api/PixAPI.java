package tech.finaya.wallet.adapter.inbounds.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixOutRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.PixWebhookEventRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixOutResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "PIX", description = "Operations for PIX")
public interface PixAPI {

    @Operation(
        summary = "Make PIX Out",
        description = "Make a PIX out to another key."
    )
    public ResponseEntity<PixOutResponse> send(
        @RequestBody PixOutRequest request,
        @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey
    );

    @Operation(
        summary = "PIX Webhook",
        description = "Receives Pix webhook."
    )
    public ResponseEntity<Void> receiveWebhook(
        @RequestBody PixWebhookEventRequest request
    );

}
