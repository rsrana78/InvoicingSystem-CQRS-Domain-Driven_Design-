package com.xcelerate.invoicing.domain;

import com.xcelerate.invoicing.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDomain{

    private Integer id;
    private TransactionType type;
    private String description;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public TransactionDomain(TransactionType type, BigDecimal amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
}
