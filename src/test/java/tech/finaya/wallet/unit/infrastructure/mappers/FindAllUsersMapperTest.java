package tech.finaya.wallet.unit.infrastructure.mappers;

import org.junit.jupiter.api.Test;
import tech.finaya.wallet.adapter.inbounds.dto.responses.UserResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.WalletResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.KeyResponse;
import tech.finaya.wallet.adapter.inbounds.dto.responses.TransactionResponse;
import tech.finaya.wallet.domain.models.Key;
import tech.finaya.wallet.domain.models.Transaction;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.domain.models.Wallet;
import tech.finaya.wallet.domain.models.enums.KeyType;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;
import tech.finaya.wallet.infrastructure.mappers.FindAllUsersMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FindAllUsersMapperTest {

    @Test
    void toResponse_shouldReturnUserResponse_withWallet() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId);
        Key key = new Key(KeyType.EMAIL, "admin@finaya.com");
        wallet.addKey(key);

        Transaction tx1 = new Transaction("e2e1", "idemKey1", BigDecimal.valueOf(100), TransactionStatus.CONFIRMED);
        Transaction tx2 = new Transaction("e2e2", "idemKey2", BigDecimal.valueOf(200), TransactionStatus.PENDING);
        wallet.addFromWalletTransactions(tx1);
        wallet.addToWalletTransactions(tx2);

        User user = new User("Admin", "12345678901");
        user.setWallet(wallet);
        user.setActive(true);

        UserResponse response = FindAllUsersMapper.toResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Admin");
        assertThat(response.cpf()).isEqualTo("12345678901");
        assertThat(response.isActive()).isTrue();

        WalletResponse walletResponse = response.wallet();
        assertThat(walletResponse).isNotNull();
        assertThat(walletResponse.walletId()).isEqualTo(walletId);
        assertThat(walletResponse.balance()).isEqualTo(wallet.getBalance());

        Set<KeyResponse> keyResponses = walletResponse.keys();
        assertThat(keyResponses).hasSize(1);
        assertThat(keyResponses.iterator().next().value()).isEqualTo("admin@finaya.com");

        List<TransactionResponse> fromTxs = walletResponse.fromWalletTransactions();
        assertThat(fromTxs).hasSize(1);
        assertThat(fromTxs.get(0).amount()).isEqualTo(BigDecimal.valueOf(100));

        List<TransactionResponse> toTxs = walletResponse.toWalletTransactions();
        assertThat(toTxs).hasSize(1);
        assertThat(toTxs.get(0).amount()).isEqualTo(BigDecimal.valueOf(200));
    }

    @Test
    void toResponse_shouldReturnUserResponse_withoutWallet() {
        User user = new User("Admin", "98765432100");
        user.setActive(false);

        UserResponse response = FindAllUsersMapper.toResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Admin");
        assertThat(response.cpf()).isEqualTo("98765432100");
        assertThat(response.isActive()).isFalse();
        assertThat(response.wallet()).isNotNull();
    }

    @Test
    void toResponse_shouldReturnNull_whenUserIsNull() {
        UserResponse response = FindAllUsersMapper.toResponse(null);
        assertThat(response).isNull();
    }

    @Test
    void toListResponse_shouldMapListOfUsers() {
        User user1 = new User("Admin", "12345678901");
        User user2 = new User("User", "98765432100");
        user1.setActive(true);
        user2.setActive(false);

        List<UserResponse> responses = FindAllUsersMapper.toListResponse(List.of(user1, user2));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).name()).isEqualTo("Admin");
        assertThat(responses.get(1).name()).isEqualTo("User");
    }

    @Test
    void toListResponse_shouldReturnEmptyList_whenInputIsEmpty() {
        List<UserResponse> responses = FindAllUsersMapper.toListResponse(List.of());
        assertThat(responses).isEmpty();
    }
}
