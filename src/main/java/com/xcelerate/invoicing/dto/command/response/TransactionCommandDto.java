package com.xcelerate.invoicing.dto.command.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xcelerate.invoicing.enums.TransactionType;
import com.xcelerate.invoicing.util.CurrencyFormatUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionCommandDto implements Serializable {
    private TransactionType type;
    private String description;
    private BigDecimal amount;
    private String amountString;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:MM:SS")

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAmountString() {
        return CurrencyFormatUtil.getCurrencyString(this.amount);
    }
}
