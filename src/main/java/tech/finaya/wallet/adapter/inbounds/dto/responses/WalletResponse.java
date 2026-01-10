package tech.finaya.wallet.adapter.inbounds.dto.responses;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record WalletResponse(
    Long id,
    UUID walletId,
    BigDecimal balance,
    Set<KeyResponse> keys,
    List<TransactionResponse> transactions
) {}
