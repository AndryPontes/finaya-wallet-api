package tech.finaya.wallet.domain.usecases;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.finaya.wallet.adapter.outbounds.persistence.repositories.BalanceHistoryRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.WalletRepository;
import tech.finaya.wallet.domain.exceptions.WalletDoesntExistException;
import tech.finaya.wallet.domain.models.Wallet;

@Service
public class GetBalance {
    
    @Autowired
    public WalletRepository walletRepository;

    @Autowired
    public BalanceHistoryRepository balanceHistoryRepository;

    public BigDecimal execute(UUID walletId) {
        Wallet wallet = walletRepository
            .findByWalletId(walletId)
            .orElseThrow(() -> new WalletDoesntExistException(walletId));

        return wallet.getBalance();
    }

    public BigDecimal executeAt(UUID walletId, LocalDateTime at) {
        return balanceHistoryRepository
            .findLastBalanceBefore(walletId, at)
            .orElse(BigDecimal.ZERO);
    }

}
