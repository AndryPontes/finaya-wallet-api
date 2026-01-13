package tech.finaya.wallet.unit.domain.models.builders;

import org.junit.jupiter.api.Test;

import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.builders.TransactionBuilder;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;
import tech.finaya.wallet.domain.models.enums.TransactionType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionBuilderTest {

    @Test
    void build_shouldCreateDepositTransactionSuccessfully() {
        Wallet toWallet = new Wallet();

        Transaction transaction = new TransactionBuilder()
            .type(TransactionType.DEPOSIT)
            .toWallet(toWallet)
            .amount(BigDecimal.valueOf(200))
            .idempotencyKey("idemKey")
            .build();

        assertThat(transaction).isNotNull();
        assertThat(transaction.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transaction.getToWallet()).isEqualTo(toWallet);
        assertThat(transaction.getAmount()).isEqualByComparingTo("200");
        assertThat(transaction.getIdempotencyKey()).isEqualTo("idemKey");
        assertThat(transaction.getEndToEndId()).isNotNull();
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    void build_shouldCreateWithdrawTransactionSuccessfully() {
        Wallet fromWallet = new Wallet();

        Transaction transaction = new TransactionBuilder()
            .type(TransactionType.WITHDRAW)
            .fromWallet(fromWallet)
            .amount(BigDecimal.valueOf(150))
            .build();

        assertThat(transaction).isNotNull();
        assertThat(transaction.getType()).isEqualTo(TransactionType.WITHDRAW);
        assertThat(transaction.getFromWallet()).isEqualTo(fromWallet);
        assertThat(transaction.getAmount()).isEqualByComparingTo("150");
    }

    @Test
    void build_shouldThrowException_whenDepositWithoutToWallet() {
        TransactionBuilder builder = new TransactionBuilder()
            .type(TransactionType.DEPOSIT)
            .amount(BigDecimal.valueOf(100));

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void build_shouldThrowException_whenWithdrawWithoutFromWallet() {
        TransactionBuilder builder = new TransactionBuilder()
            .type(TransactionType.WITHDRAW)
            .amount(BigDecimal.valueOf(100));

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void build_shouldThrowException_whenAmountIsZeroOrNegative() {
        TransactionBuilder builder = new TransactionBuilder()
            .type(TransactionType.DEPOSIT)
            .toWallet(new Wallet())
            .amount(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, builder::build);

        builder.amount(BigDecimal.valueOf(-50));
        assertThrows(IllegalArgumentException.class, builder::build);
    }
}
