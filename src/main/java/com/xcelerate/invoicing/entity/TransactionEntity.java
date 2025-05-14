package com.xcelerate.invoicing.entity;

import com.xcelerate.invoicing.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class TransactionEntity extends BaseEntity<Integer> implements Serializable{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType type;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "create_ts")
    private LocalDateTime timestamp;

    public TransactionEntity(InvoiceEntity invoice, TransactionType type, BigDecimal amount, String description) {
        this.invoice = invoice;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
}
