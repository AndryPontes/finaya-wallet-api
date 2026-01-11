package tech.finaya.wallet.adapter.outbounds.persistence.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface BalanceHistoryRepository {

    Optional<BigDecimal> findLastBalanceAfter(UUID walletId, LocalDateTime LocalDateTime);

}
