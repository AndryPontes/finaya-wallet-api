package tech.finaya.wallet.adapter.inbounds.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import tech.finaya.wallet.adapter.inbounds.dto.requests.PixIntraRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.PixIntraResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "PIX intra", description = "Operations for PIX intra")
public interface PixIntraAPI {

    @Operation(
        summary = "Make PIX intra",
        description = "Make a PIX intra to another key."
    )
    public ResponseEntity<PixIntraResponse> send(
        @RequestBody PixIntraRequest request,
        @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey
    );

}
