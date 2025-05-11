package com.xcelerate.invoicing.dto.query.response;

import com.xcelerate.invoicing.enums.TransactionType;
import com.xcelerate.invoicing.util.CurrencyFormatUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionQueryDto implements Serializable {
    private TransactionType type;
    private String description;
    private BigDecimal amount;
    private String amountString;
    private LocalDateTime timestamp;

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountString() {
        return CurrencyFormatUtil.getCurrencyString(this.amount);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
