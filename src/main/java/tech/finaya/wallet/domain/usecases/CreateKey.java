package tech.finaya.wallet.domain.usecases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateKeyRequest;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.KeyRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.WalletRepository;
import tech.finaya.wallet.domain.exceptions.WalletDoesntExistException;
import tech.finaya.wallet.domain.exceptions.WalletKeyAlreadyExistException;
import tech.finaya.wallet.domain.exceptions.WalletTypeKeyAlreadyExistException;
import tech.finaya.wallet.domain.models.Key;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.infrastructure.mappers.CreateKeyMapper;

@Service
public class CreateKey {
    
    @Autowired
    public WalletRepository walletRepository;

    @Autowired
    public KeyRepository keyRepository;

    @Transactional
    public Key execute(UUID walletId, CreateKeyRequest request) {
        Key key = CreateKeyMapper.toEntity(request);

        Wallet wallet = walletRepository
            .findByWalletId(walletId)
            .orElseThrow(() -> new WalletDoesntExistException(walletId));

        if (wallet.isTypeKeyExist(key.getType())) {
            throw new WalletTypeKeyAlreadyExistException(key.getType().name());
        }

        if (keyRepository.existsByValue(key.getValue())) {
            throw new WalletKeyAlreadyExistException(key.getValue());
        }

        wallet.addKey(key);

        walletRepository.save(wallet);

        return key;
    }

}
