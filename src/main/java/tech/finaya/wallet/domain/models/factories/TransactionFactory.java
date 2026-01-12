package tech.finaya.wallet.domain.models.factories;

import java.math.BigDecimal;

import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.builders.TransactionBuilder;
import tech.finaya.wallet.domain.models.enums.TransactionType;

public final class TransactionFactory {

    public static Transaction build(
            TransactionType type,
            BigDecimal amount,
            String idempotencyKey,
            Wallet fromWallet,
            Wallet toWallet
    ) {
        return new TransactionBuilder()
            .type(type)
            .amount(amount)
            .idempotencyKey(idempotencyKey)
            .fromWallet(fromWallet)
            .toWallet(toWallet)
            .build();
    }

}
