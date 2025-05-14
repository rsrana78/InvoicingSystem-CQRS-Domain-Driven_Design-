package com.xcelerate.invoicing.entity;

import com.xcelerate.invoicing.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "invoices")
public class InvoiceEntity extends BaseEntity<Integer> implements Serializable {

    @Column(name = "customer_id")
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_status")
    private InvoiceStatus status;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;

    @OneToMany(mappedBy = "invoice",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionEntity> transactions = new ArrayList<>();

    public InvoiceEntity(String customerId, LocalDate dueDate, BigDecimal totalAmount) {
        this.customerId = customerId;
        this.status = InvoiceStatus.DRAFT;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.totalAmount = totalAmount;
        this.balance = totalAmount;
    }
}
