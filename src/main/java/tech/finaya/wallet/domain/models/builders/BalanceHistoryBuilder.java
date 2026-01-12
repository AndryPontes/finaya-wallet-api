package tech.finaya.wallet.domain.models.builders;

import java.math.BigDecimal;

import tech.finaya.wallet.domain.models.BalanceHistory;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;

public class BalanceHistoryBuilder {

    private Wallet wallet;
    private Transaction transaction;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;

    public BalanceHistoryBuilder() {}

    public BalanceHistoryBuilder wallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public BalanceHistoryBuilder transaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }

    public BalanceHistoryBuilder amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public BalanceHistoryBuilder balanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
        return this;
    }

    public BalanceHistoryBuilder balanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
        return this;
    }

    public BalanceHistory build() {
        BalanceHistory balanceHistory = new BalanceHistory();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount must be greater than zero");
        }

        balanceHistory.setWallet(wallet);
        balanceHistory.setTransaction(transaction);
        balanceHistory.setAmount(amount);
        balanceHistory.setBalanceBefore(balanceBefore);
        balanceHistory.setBalanceAfter(balanceAfter);
        
        return balanceHistory;
    }
    
}
