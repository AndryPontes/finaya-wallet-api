package tech.finaya.wallet.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import tech.finaya.wallet.domain.models.enums.TransactionStatus;
import tech.finaya.wallet.domain.models.factories.TransactionStateFactory;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "end_to_end_id", nullable = false, unique = true)
    private String endToEndId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Transient
    private TransactionState state = new PendingState();

    // Toda vez que o objeto for carregado ou persistido no banco esse metodo sera disparado atualizando o state
    @PostLoad
    @PostPersist
    private void initState() {
        this.state = TransactionStateFactory.build(this.status);
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    protected void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public void confirm() throws IllegalArgumentException {
        state.confirm(this);
    }

    public void reject() throws IllegalArgumentException {
        state.reject(this);
    }

}
