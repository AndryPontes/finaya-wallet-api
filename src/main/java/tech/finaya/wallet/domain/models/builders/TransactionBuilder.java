package tech.finaya.wallet.domain.models.builders;

import java.math.BigDecimal;
import java.util.UUID;

import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;
import tech.finaya.wallet.domain.models.enums.TransactionType;

public class TransactionBuilder {

    private Wallet fromWallet;
    private Wallet toWallet;
    private BigDecimal amount;
    private String idempotencyKey;
    private String endToEndId;
    private TransactionType type;
    private TransactionStatus status = TransactionStatus.PENDING;

    public TransactionBuilder() {}

    public TransactionBuilder fromWallet(Wallet fromWallet) {
        this.fromWallet = fromWallet;
        return this;
    }

    public TransactionBuilder toWallet(Wallet toWallet) {
        this.toWallet = toWallet;
        return this;
    }

    public TransactionBuilder amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder idempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
        return this;
    }

    public TransactionBuilder endToEndId(String endToEndId) {
        this.endToEndId = endToEndId;
        return this;
    }

    public TransactionBuilder status(TransactionStatus status) {
        this.status = status;
        return this;
    }

    public TransactionBuilder type(TransactionType type) {
        this.type = type;
        return this;
    }

    public Transaction build() {
        Transaction transaction = new Transaction();

        if (type == TransactionType.DEPOSIT && toWallet == null) {
            throw new IllegalArgumentException("toWallet cannot be null");
        } else if (type == TransactionType.WITHDRAW && fromWallet == null) {
            throw new IllegalArgumentException("fromWallet cannot be null");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount must be greater than zero");
        }

        transaction.setFromWallet(fromWallet);
        transaction.setToWallet(toWallet);
        transaction.setAmount(amount);
        transaction.setIdempotencyKey(idempotencyKey);
        transaction.setEndToEndId(endToEndId != null ? endToEndId : UUID.randomUUID().toString());
        transaction.setStatus(status);
        transaction.setType(type);

        return transaction;
    }
}
