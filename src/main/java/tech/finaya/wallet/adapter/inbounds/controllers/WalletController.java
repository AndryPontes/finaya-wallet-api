package tech.finaya.wallet.adapter.inbounds.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.finaya.wallet.adapter.inbounds.controllers.api.WalletAPI;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateKeyResponse;
import tech.finaya.wallet.domain.models.Key;
import tech.finaya.wallet.domain.usecases.CreateKey;
import tech.finaya.wallet.infrastructure.mappers.CreateKeyMapper;

@RestController
@RequestMapping("/api/wallets")
public class WalletController implements WalletAPI {
    
    @Autowired
    private Logger log;

    @Autowired
    public CreateKey createKey;

    @PostMapping("/{wallet_id}/key")
    public ResponseEntity<CreateKeyResponse> createKey(@PathVariable("wallet_id") UUID walletId, @RequestBody CreateKeyRequest request) {
        log.info("Creating a key with type [{}] and value [{}] in wallet [{}]...", request.type(), request.value(), walletId);

        Key key = createKey.execute(walletId, CreateKeyMapper.toEntity(request));

        log.info("Key created with type [{}] and value [{}] in wallet [{}]...", key.getType(), key.getValue(), walletId);

        return ResponseEntity.ok(CreateKeyMapper.toResponse(key, walletId));
    }

}
