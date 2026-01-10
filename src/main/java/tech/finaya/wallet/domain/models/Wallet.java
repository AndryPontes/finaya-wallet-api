package tech.finaya.wallet.domain.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Key> keys = new HashSet<>();

    @OneToMany(mappedBy = "fromWallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> fromWalletTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "toWallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> toWalletTransactions = new ArrayList<>();

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

    public List<Transaction> getFromWalletTransactions() {
        return fromWalletTransactions;
    }

    public List<Transaction> getToWalletTransactions() {
        return toWalletTransactions;
    }

    public void addKey(Key key) {
        key.setWallet(this);
        this.keys.add(key);
    }

    public boolean isTypeKeyExist(KeyType type) {
        return this.keys.stream().anyMatch(key -> key.getType() == type);
    }

}
