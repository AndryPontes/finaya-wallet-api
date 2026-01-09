package tech.finaya.wallet.domain.models;

import java.time.LocalDate;

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

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    protected User() {}

    public User(String name) {
        this.name = name;
        this.wallet = new Wallet(this);
    }
    
    public String getName() {
        return name;
    }

    public Boolean isActive() {
        return isActive;
    }

}
