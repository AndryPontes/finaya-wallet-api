package tech.finaya.wallet.adapter.inbounds.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateKeyResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "Wallet", description = "Operations for wallets")
public interface WalletAPI {

    @Operation(
        summary = "Create a key inside a wallet",
        description = "Create a key by type with value in wallet by *wallet_id*."
    )
    ResponseEntity<CreateKeyResponse> createKey(
        @PathVariable("wallet_id") UUID walletId, 
        @RequestBody CreateKeyRequest request
    );
}
