package tech.finaya.wallet.adapter.outbounds.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tech.finaya.wallet.domain.models.BalanceHistory;

@Repository
public interface BalanceHistoryJpaRepository extends JpaRepository<BalanceHistory, Long> {

    @Query(
        "SELECT b.balanceAfter FROM BalanceHistory b " +
        "WHERE b.wallet.walletId = :walletId AND b.createdAt <= :at " +
        "ORDER BY b.createdAt DESC"
    )
    List<BigDecimal> findAllBalanceAfter(
        @Param("walletId") UUID walletId,
        @Param("at") LocalDateTime at,
        Pageable pageable
    );

}
