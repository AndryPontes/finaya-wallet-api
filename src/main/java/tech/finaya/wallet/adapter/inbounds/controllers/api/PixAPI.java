package tech.finaya.wallet.adapter.inbounds.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import tech.finaya.wallet.adapter.inbounds.dto.requests.PixRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "PIX", description = "Operations for PIX")
public interface PixAPI {

    @Operation(
        summary = "Make PIX",
        description = "Make a PIX to another key."
    )
    public ResponseEntity<PixResponse> makePix(
        @RequestBody PixRequest request,
        @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey
    );

}
