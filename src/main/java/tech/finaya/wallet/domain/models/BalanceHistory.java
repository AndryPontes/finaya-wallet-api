package tech.finaya.wallet.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "balance_histories")
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(name="amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal amount;

    @Column(name = "balance_after", nullable = false, precision = 20, scale = 2)
    private BigDecimal balanceAfter;

    @Column(name = "balance_before", nullable = false, precision = 20, scale = 2)
    private BigDecimal balanceBefore;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public BalanceHistory() {}

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

}