package tech.finaya.wallet.infrastructure.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tech.finaya.wallet.adapter.inbounds.dto.responses.KeyResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.TransactionResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.UserResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.WalletResponse;
import tech.finaya.wallet.domain.models.Key;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.domain.models.Wallet;

public class FindAllUsersMapper {

    public static UserResponse toResponse(User user) {
        if (user == null) return null;

        Wallet wallet = user.getWallet();

        WalletResponse walletResponse = null;

        if (wallet != null) {
            Set<KeyResponse> keys = wallet.getKeys().stream()
                .map(FindAllUsersMapper::toKeyResponse)
                .collect(Collectors.toSet());

            List<TransactionResponse> transactions = wallet.getTransactions().stream()
                .map(FindAllUsersMapper::toTransactionResponse)
                .toList();

            walletResponse = new WalletResponse(
                wallet.getId(),
                wallet.getWalletId(),
                wallet.getBalance(),
                keys,
                transactions
            );
        }

        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getCpf(),
            user.isActive(),
            walletResponse
        );
    }

    public static List<UserResponse> toListResponse(List<User> users) {
        return users.stream()
            .map(FindAllUsersMapper::toResponse)
            .collect(Collectors.toList());
    }

    private static KeyResponse toKeyResponse(Key key) {
        return new KeyResponse(
            key.getId(),
            key.getValue(),
            key.getType()
        );
    }

    private static TransactionResponse toTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
            transaction.getId(),
            transaction.getEndToEndId(),
            transaction.getAmount(),
            transaction.getCreatedAt(),
            transaction.getStatus()
        );
    }
}