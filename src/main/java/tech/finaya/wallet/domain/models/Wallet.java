package tech.finaya.wallet.domain.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import tech.finaya.wallet.domain.exceptions.AmountIsInvalidException;
import tech.finaya.wallet.domain.exceptions.InsufficientBalanceException;
import tech.finaya.wallet.domain.models.enums.KeyType;

@Entity
@Table(name = "wallets")
public class Wallet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_id", nullable = false, updatable = false)
    private UUID walletId = UUID.randomUUID();

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    @Column(name = "balance", nullable = false, precision = 20, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "locked_balance", nullable = false, precision = 20, scale = 2)
    private BigDecimal lockedBalance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Key> keys = new HashSet<>();

    @OneToMany(mappedBy = "fromWallet", orphanRemoval = true)
    private Set<Transaction> fromWalletTransactions = new HashSet<>();

    @OneToMany(mappedBy = "toWallet", orphanRemoval = true)
    private Set<Transaction> toWalletTransactions = new HashSet<>();
    
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BalanceHistory> balanceHistory = new HashSet<>();

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected Wallet() {}

    public Wallet(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Set<Key> getKeys() {
        return keys;
    }

    public Set<Transaction> getFromWalletTransactions() {
        return fromWalletTransactions;
    }

    public Set<Transaction> getToWalletTransactions() {
        return toWalletTransactions;
    }

    public void addFromWalletTransactions(Transaction transaction) {
        this.fromWalletTransactions.add(transaction);
    }

    public void addToWalletTransactions(Transaction transaction) {
        this.toWalletTransactions.add(transaction);
    }

    public void addBalanceHistory(BalanceHistory balanceHistory) {
        this.balanceHistory.add(balanceHistory);
    }

    public void addKey(Key key) {
        key.setWallet(this);
        this.keys.add(key);
    }

    public boolean isTypeKeyExist(KeyType type) {
        return this.keys.stream().anyMatch(key -> key.getType() == type);
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);

        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        validateAmountForWithdraw(amount);

        this.balance = this.balance.subtract(amount);
    }

    public void lockBalance(BigDecimal amount) {
        validateAmount(amount);
        validateAmountForWithdraw(amount);

        this.lockedBalance = this.lockedBalance.add(amount);
        this.balance = this.balance.subtract(amount);
    }

    public void confirmLockedBalance(BigDecimal amount) {
        validateAmount(amount);
        validateLockedAmountForWithdraw(amount);

        this.lockedBalance = this.lockedBalance.subtract(amount);
    }

    public void rejectLockedBalance(BigDecimal amount) {
        validateAmount(amount);
        validateLockedAmountForWithdraw(amount);

        this.lockedBalance = this.lockedBalance.subtract(amount);
        this.balance = this.balance.add(amount);
    }
    
    private void validateAmountForWithdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                String.format("Insufficient balance: balance [%s], withdrawal [%s]", this.balance, amount)
            );
        }
    }

    private void validateLockedAmountForWithdraw(BigDecimal amount) {
        if (this.lockedBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                String.format("Insufficient locked balance: locked balance [%s], withdrawal [%s]", this.lockedBalance, amount)
            );
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new AmountIsInvalidException(String.format("Amount [%s] cannot be null", amount));
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountIsInvalidException(String.format("Amount [%s] must be greater than zero", amount));
        }
    }

}
