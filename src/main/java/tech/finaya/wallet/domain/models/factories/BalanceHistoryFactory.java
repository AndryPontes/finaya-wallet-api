package tech.finaya.wallet.domain.models.factories;

import java.math.BigDecimal;

import tech.finaya.wallet.domain.models.BalanceHistory;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.builders.BalanceHistoryBuilder;

public final class BalanceHistoryFactory {

    public static BalanceHistory build(
        Wallet wallet,
        Transaction transaction,
        BigDecimal amount,
        BigDecimal balanceBefore
    ) {
        return new BalanceHistoryBuilder()
            .wallet(wallet)
            .transaction(transaction)
            .amount(amount)
            .balanceBefore(balanceBefore)
            .balanceAfter(wallet.getBalance())
            .build();
    }

}
