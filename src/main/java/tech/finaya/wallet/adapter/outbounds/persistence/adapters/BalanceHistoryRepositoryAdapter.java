package tech.finaya.wallet.adapter.outbounds.persistence.adapters;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.finaya.wallet.adapter.outbounds.persistence.jpa.BalanceHistoryJpaRepository;
import tech.finaya.wallet.adapter.outbounds.persistence.repositories.BalanceHistoryRepository;

@Component
public class BalanceHistoryRepositoryAdapter implements BalanceHistoryRepository {
    
    @Autowired
    private BalanceHistoryJpaRepository repository;

    @Override
    public Optional<BigDecimal> findLastBalanceBefore(UUID walletId, LocalDateTime at) {
        return repository.findLastBalanceBefore(walletId, at);
    }

}
