package tech.finaya.wallet.unit.domain.models.factories;

import org.junit.jupiter.api.Test;

import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.enums.TransactionType;
import tech.finaya.wallet.domain.models.factories.TransactionFactory;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionFactoryTest {

    @Test
    void build_shouldCreateDepositTransactionSuccessfully() {
        Wallet toWallet = new Wallet();

        Transaction transaction = TransactionFactory.build(
            TransactionType.DEPOSIT,
            BigDecimal.valueOf(200),
            "idemKey",
            null,
            toWallet
        );

        assertThat(transaction).isNotNull();
        assertThat(transaction.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transaction.getToWallet()).isEqualTo(toWallet);
        assertThat(transaction.getAmount()).isEqualByComparingTo("200");
        assertThat(transaction.getIdempotencyKey()).isEqualTo("idemKey");
    }

    @Test
    void build_shouldCreateWithdrawTransactionSuccessfully() {
        Wallet fromWallet = new Wallet();

        Transaction transaction = TransactionFactory.build(
            TransactionType.WITHDRAW,
            BigDecimal.valueOf(150),
            "idemKey",
            fromWallet,
            null
        );

        assertThat(transaction).isNotNull();
        assertThat(transaction.getType()).isEqualTo(TransactionType.WITHDRAW);
        assertThat(transaction.getFromWallet()).isEqualTo(fromWallet);
        assertThat(transaction.getAmount()).isEqualByComparingTo("150");
        assertThat(transaction.getIdempotencyKey()).isEqualTo("idemKey");
    }

    @Test
    void build_shouldThrowException_whenAmountIsZeroOrNegative() {
        Wallet toWallet = new Wallet();

        assertThrows(IllegalArgumentException.class, () -> TransactionFactory.build(
            TransactionType.DEPOSIT,
            BigDecimal.ZERO,
            "idemKey",
            null,
            toWallet
        ));

        assertThrows(IllegalArgumentException.class, () -> TransactionFactory.build(
            TransactionType.DEPOSIT,
            BigDecimal.valueOf(-10),
            "idemKey",
            null,
            toWallet
        ));
    }

    @Test
    void build_shouldThrowException_whenDepositWithoutToWallet() {
        assertThrows(IllegalArgumentException.class, () -> TransactionFactory.build(
            TransactionType.DEPOSIT,
            BigDecimal.valueOf(100),
            "idemKey",
            null,
            null
        ));
    }

    @Test
    void build_shouldThrowException_whenWithdrawWithoutFromWallet() {
        assertThrows(IllegalArgumentException.class, () -> TransactionFactory.build(
            TransactionType.WITHDRAW,
            BigDecimal.valueOf(100),
            "idemKey",
            null,
            null
        ));
    }
}
