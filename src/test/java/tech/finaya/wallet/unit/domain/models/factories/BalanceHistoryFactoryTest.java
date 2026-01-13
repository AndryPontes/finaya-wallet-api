package tech.finaya.wallet.unit.domain.models.factories;

import org.junit.jupiter.api.Test;

import tech.finaya.wallet.domain.models.BalanceHistory;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.factories.BalanceHistoryFactory;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BalanceHistoryFactoryTest {

    @Test
    void build_shouldCreateBalanceHistoryWithCorrectValues() {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(600));
        
        Transaction transaction = new Transaction();

        BalanceHistory balanceHistory = BalanceHistoryFactory.build(
            wallet,
            transaction,
            BigDecimal.valueOf(100),
            BigDecimal.valueOf(500)
        );

        assertThat(balanceHistory).isNotNull();
        assertThat(balanceHistory.getWallet()).isEqualTo(wallet);
        assertThat(balanceHistory.getTransaction()).isEqualTo(transaction);
        assertThat(balanceHistory.getAmount()).isEqualByComparingTo("100");
        assertThat(balanceHistory.getBalanceBefore()).isEqualByComparingTo("500");
        assertThat(balanceHistory.getBalanceAfter()).isEqualByComparingTo("600");
    }
}
