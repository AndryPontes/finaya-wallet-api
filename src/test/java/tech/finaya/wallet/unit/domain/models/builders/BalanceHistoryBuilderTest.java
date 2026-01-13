package tech.finaya.wallet.unit.domain.models.builders;

import org.junit.jupiter.api.Test;

import tech.finaya.wallet.domain.models.BalanceHistory;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.builders.BalanceHistoryBuilder;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BalanceHistoryBuilderTest {

    @Test
    void build_shouldCreateBalanceHistoryWithCorrectValues() {
        Wallet wallet = new Wallet();
        Transaction transaction = new Transaction();

        BalanceHistory balanceHistory = new BalanceHistoryBuilder()
            .wallet(wallet)
            .transaction(transaction)
            .amount(BigDecimal.valueOf(100))
            .balanceBefore(BigDecimal.valueOf(500))
            .balanceAfter(BigDecimal.valueOf(600))
            .build();

        assertThat(balanceHistory).isNotNull();
        assertThat(balanceHistory.getWallet()).isEqualTo(wallet);
        assertThat(balanceHistory.getTransaction()).isEqualTo(transaction);
        assertThat(balanceHistory.getAmount()).isEqualByComparingTo("100");
        assertThat(balanceHistory.getBalanceBefore()).isEqualByComparingTo("500");
        assertThat(balanceHistory.getBalanceAfter()).isEqualByComparingTo("600");
    }

    @Test
    void build_shouldThrowException_whenAmountIsNull() {
        BalanceHistoryBuilder builder = new BalanceHistoryBuilder()
            .wallet(new Wallet())
            .transaction(new Transaction())
            .balanceBefore(BigDecimal.ZERO)
            .balanceAfter(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void build_shouldThrowException_whenAmountIsZeroOrNegative() {
        BalanceHistoryBuilder builder = new BalanceHistoryBuilder()
            .wallet(new Wallet())
            .transaction(new Transaction())
            .amount(BigDecimal.ZERO)
            .balanceBefore(BigDecimal.ZERO)
            .balanceAfter(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, builder::build);

        builder.amount(BigDecimal.valueOf(-10));
        assertThrows(IllegalArgumentException.class, builder::build);
    }
}
