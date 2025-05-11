package com.xcelerate.invoicing.dto.query.response;

import com.xcelerate.invoicing.enums.InvoiceStatus;
import com.xcelerate.invoicing.util.CurrencyFormatUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceQueryDto implements Serializable {

    private Integer invoiceId;
    private String customerId;
    private InvoiceStatus status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private BigDecimal balance;
    private String totalAmountString;
    private String balanceString;
    private List<TransactionQueryDto> transactions;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmountString() {
        return CurrencyFormatUtil.getCurrencyString(this.totalAmount);
    }

    public String getBalanceString() {
        return CurrencyFormatUtil.getCurrencyString(this.balance);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<TransactionQueryDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionQueryDto> transactions) {
        this.transactions = transactions;
    }
}
