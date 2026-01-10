package tech.finaya.wallet.domain.usecases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.finaya.wallet.adapter.inbounds.dto.requests.DepositRequest;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.WalletRepository;
import tech.finaya.wallet.domain.exceptions.WalletDoesntExistException;
import tech.finaya.wallet.domain.models.Wallet;

@Service
public class MakeDeposit {
    
    @Autowired
    public WalletRepository walletRepository;

    @Transactional
    public Wallet execute(UUID walletId, DepositRequest request) {
        Wallet wallet = walletRepository
            .findByWalletId(walletId)
            .orElseThrow(() -> new WalletDoesntExistException(walletId));

        wallet.deposit(request.amount());

        return walletRepository.save(wallet);
    }

}
