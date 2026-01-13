package tech.finaya.wallet.domain.models;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        optional = false,
        orphanRemoval = true
    )
    private Wallet wallet;

    @Column(name = "name", nullable = false, unique = false, length = 50)
    private String name;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    protected User() {}

    public User(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
        this.wallet = new Wallet(this);
    }

    public Long getId() {
        return id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
