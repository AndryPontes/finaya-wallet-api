package tech.finaya.wallet.unit.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.finaya.wallet.domain.exceptions.AmountIsInvalidException;
import tech.finaya.wallet.domain.exceptions.InsufficientBalanceException;
import tech.finaya.wallet.domain.models.Key;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.enums.KeyType;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void setup() {
        wallet = new Wallet();
    }

    @Test
    void deposit_shouldIncreaseBalance() {
        wallet.deposit(BigDecimal.valueOf(100));
        assertThat(wallet.getBalance()).isEqualByComparingTo("100");
    }

    @Test
    void deposit_shouldThrowWhenAmountIsInvalid() {
        assertThrows(AmountIsInvalidException.class, () -> wallet.deposit(BigDecimal.ZERO));
        assertThrows(AmountIsInvalidException.class, () -> wallet.deposit(BigDecimal.valueOf(-10)));
        assertThrows(AmountIsInvalidException.class, () -> wallet.deposit(null));
    }

    @Test
    void withdraw_shouldDecreaseBalance() {
        wallet.deposit(BigDecimal.valueOf(200));
        wallet.withdraw(BigDecimal.valueOf(50));
        assertThat(wallet.getBalance()).isEqualByComparingTo("150");
    }

    @Test
    void withdraw_shouldThrowWhenInsufficientBalance() {
        wallet.deposit(BigDecimal.valueOf(50));
        assertThrows(InsufficientBalanceException.class, () -> wallet.withdraw(BigDecimal.valueOf(100)));
    }

    @Test
    void withdraw_shouldThrowWhenAmountIsInvalid() {
        assertThrows(AmountIsInvalidException.class, () -> wallet.withdraw(BigDecimal.ZERO));
        assertThrows(AmountIsInvalidException.class, () -> wallet.withdraw(BigDecimal.valueOf(-5)));
        assertThrows(AmountIsInvalidException.class, () -> wallet.withdraw(null));
    }

    @Test
    void lockBalance_shouldDecreaseBalanceAndIncreaseLockedBalance() {
        wallet.deposit(BigDecimal.valueOf(200));
        wallet.lockBalance(BigDecimal.valueOf(50));

        assertThat(wallet.getBalance()).isEqualByComparingTo("150");
        assertThat(wallet.getLockedBalance()).isEqualByComparingTo("50");
    }

    @Test
    void lockBalance_shouldThrowWhenInsufficientBalance() {
        wallet.deposit(BigDecimal.valueOf(50));
        assertThrows(InsufficientBalanceException.class, () -> wallet.lockBalance(BigDecimal.valueOf(100)));
    }

    @Test
    void confirmLockedBalance_shouldDecreaseLockedBalance() {
        wallet.deposit(BigDecimal.valueOf(200));
        wallet.lockBalance(BigDecimal.valueOf(50));
        wallet.confirmLockedBalance(BigDecimal.valueOf(50));

        assertThat(wallet.getLockedBalance()).isEqualByComparingTo("0");
        assertThat(wallet.getBalance()).isEqualByComparingTo("150");
    }

    @Test
    void rejectLockedBalance_shouldDecreaseLockedBalanceAndReturnToBalance() {
        wallet.deposit(BigDecimal.valueOf(200));
        wallet.lockBalance(BigDecimal.valueOf(50));
        wallet.rejectLockedBalance(BigDecimal.valueOf(50));

        assertThat(wallet.getLockedBalance()).isEqualByComparingTo("0");
        assertThat(wallet.getBalance()).isEqualByComparingTo("200");
    }

    @Test
    void addKey_shouldSetWalletAndAddToKeys() {
        Key key = new Key(KeyType.EMAIL, "admin@finaya.com");
        wallet.addKey(key);

        assertThat(wallet.getKeys()).contains(key);
        assertThat(key.getWallet()).isEqualTo(wallet);
    }

    @Test
    void isTypeKeyExist_shouldReturnTrueIfKeyTypeExists() {
        Key key = new Key(KeyType.EMAIL, "admin@finaya.com");
        wallet.addKey(key);

        assertThat(wallet.isTypeKeyExist(KeyType.EMAIL)).isTrue();
        assertThat(wallet.isTypeKeyExist(KeyType.PHONE)).isFalse();
    }
}
