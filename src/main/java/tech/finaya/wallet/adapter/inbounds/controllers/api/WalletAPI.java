package tech.finaya.wallet.adapter.inbounds.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.DepositRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.WithdrawRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateKeyResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.DepositResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.GetBalanceResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.WithdrawResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
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

    @Operation(
        summary = "Check current wallet balance",
        description = "Check current balance and balance by date."
    )
    public ResponseEntity<GetBalanceResponse> getBalance(
        @PathVariable("wallet_id") UUID walletId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime at
    );

    @Operation(
        summary = "Deposit credit",
        description = "Deposit credit in wallet."
    )
    public ResponseEntity<DepositResponse> deposit(
        @PathVariable("wallet_id") UUID walletId,
        @RequestBody DepositRequest request,
        @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    );

    @Operation(
        summary = "Withdraw credit",
        description = "Withdraw credit in wallet."
    )
    public ResponseEntity<WithdrawResponse> withdraw(
        @PathVariable("wallet_id") UUID walletId,
        @RequestBody WithdrawRequest request,
        @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    );

}
