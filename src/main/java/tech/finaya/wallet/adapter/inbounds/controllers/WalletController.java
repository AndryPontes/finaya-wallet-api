package tech.finaya.wallet.adapter.inbounds.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.finaya.wallet.adapter.inbounds.controllers.api.WalletAPI;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.inbounds.dto.requests.DepositRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateKeyResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.DepositResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.GetBalanceResponse;
import tech.finaya.wallet.domain.models.Key;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.usecases.CreateKey;
import tech.finaya.wallet.domain.usecases.GetBalance;
import tech.finaya.wallet.domain.usecases.MakeDeposit;
import tech.finaya.wallet.infrastructure.mappers.CreateKeyMapper;
import tech.finaya.wallet.infrastructure.mappers.GetBalanceMapper;
import tech.finaya.wallet.infrastructure.mappers.MakeDepositMapper;

@RestController
@RequestMapping("/api/wallets")
public class WalletController implements WalletAPI {
    
    @Autowired
    private Logger log;

    @Autowired
    public CreateKey createKey;

    @Autowired
    public GetBalance getBalance;

    @Autowired
    public MakeDeposit makeDeposit;

    @PostMapping("/{wallet_id}/key")
    public ResponseEntity<CreateKeyResponse> createKey(
        @PathVariable("wallet_id") UUID walletId, 
        @RequestBody CreateKeyRequest request
    ) {
        log.info("Creating a key with type [{}] and value [{}] in wallet [{}]...", request.type(), request.value(), walletId);

        Key key = createKey.execute(walletId, request);

        log.info("Key created with type [{}] and value [{}] in wallet [{}]...", key.getType(), key.getValue(), walletId);

        return ResponseEntity.ok(CreateKeyMapper.toResponse(key, walletId));
    }

    @GetMapping("/{wallet_id}/balance")
    public ResponseEntity<GetBalanceResponse> getBalance(
        @PathVariable("wallet_id") UUID walletId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime at
    ) {
        log.info("Getting balance in wallet [{}]...", walletId);

        BigDecimal balance = (at == null)
            ? getBalance.execute(walletId)
            : getBalance.executeAt(walletId, at);

        log.info("Balance [{}] checked in wallet [{}]...", balance, walletId);

        return ResponseEntity.ok(GetBalanceMapper.toResponse(walletId, balance, at));
    }

    @PostMapping("/{wallet_id}/deposit")    
    public ResponseEntity<DepositResponse> deposit(
        @PathVariable("wallet_id") UUID walletId, 
        @RequestBody DepositRequest request
    ) {
        log.info("Depositing funds [{}] into wallet [{}]...", request.amount(), walletId);

        Wallet wallet = makeDeposit.execute(walletId, request);

        log.info("Balance [{}] deposited in wallet [{}]...", wallet.getBalance(), wallet.getWalletId());

        return ResponseEntity.ok(MakeDepositMapper.toResponse(wallet));
    }

}
