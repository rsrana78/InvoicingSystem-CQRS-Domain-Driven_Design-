package com.xcelerate.invoicing.domain;

import com.xcelerate.invoicing.enums.InvoiceStatus;
import com.xcelerate.invoicing.enums.TransactionType;
import com.xcelerate.invoicing.exception.InvalidActionException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvoiceDomain {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceDomain.class);

    private Integer id;
    private String customerId;
    private InvoiceStatus status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private BigDecimal balance;
    private final List<TransactionDomain> transactions = new ArrayList<>();

    public InvoiceDomain(String customerId, LocalDate dueDate, BigDecimal totalAmount) {
        this.customerId = customerId;
        this.status = InvoiceStatus.DRAFT;
        this.dueDate = dueDate;
        this.createdAt = LocalDateTime.now();
        this.totalAmount = totalAmount;
        this.balance = totalAmount;
    }

    public void addCharge(BigDecimal amount, String description) {
        logger.info("Adding charge to invoice");
        if (this.status != InvoiceStatus.DRAFT) {
            logger.error("Invoice status is not {}, can not add charge in status {}", InvoiceStatus.DRAFT, this.status);
            throw new InvalidActionException("Charges can only be added to DRAFT invoices");
        }
        TransactionDomain charge = new TransactionDomain(TransactionType.CHARGE, amount, description);
        this.transactions.add(charge);
        this.totalAmount = totalAmount.add(amount);
        this.balance = this.balance.add(amount);
        logger.info("Charge added successfully");
    }

    public void applyPayment(BigDecimal amount, String description) {
        logger.info("Adding payment to invoice");
        if (this.status != InvoiceStatus.ISSUED) {
            logger.error("Payment can not be added to invoice in current status {}", this.status);
            throw new InvalidActionException("Payments can only be applied to ISSUED invoices");
        }
        if(amount.compareTo(this.balance)>0){
            logger.error("Payment amount can not exceed the remaining balance");
            throw new InvalidActionException("Payment amount can not exceed the remaining balance");
        }
        TransactionDomain payment = new TransactionDomain(TransactionType.PAYMENT, amount, description);
        this.transactions.add(payment);
        this.balance = this.balance.subtract(amount);
        if (this.balance.compareTo(BigDecimal.ZERO) <= 0) {
            this.status = InvoiceStatus.PAID;
        }
        logger.info("Payment added successfully");
    }

    public void applyCreditMemo(BigDecimal amount, String description) {
        logger.info("Applying credit memo to invoice");
        if (this.status != InvoiceStatus.ISSUED) {
            logger.error("Credit memo can not be applied in current status {}", this.status);
            throw new InvalidActionException("Credit memos can only be applied to ISSUED invoices");
        }
        if(amount.compareTo(this.balance)>0){
            logger.error("Credit memo amount can not exceed the remaining balance");
            throw new InvalidActionException("Credit memo amount can not exceed the remaining balance");
        }
        TransactionDomain credit = new TransactionDomain(TransactionType.CREDIT_MEMO, amount, description);
        this.transactions.add(credit);
        this.balance = this.balance.subtract(amount);
        if (this.balance.compareTo(BigDecimal.ZERO) <= 0) {
            this.status = InvoiceStatus.PAID;
        }
        logger.info("Credit memo applied successfully");
    }

    public void finalizeInvoice() {
        logger.info("Finalizing the invoice....");
        if (this.status != InvoiceStatus.DRAFT) {
            logger.error("Invoice in status {} can not be finalized", this.status);
            throw new InvalidActionException("Only DRAFT invoices can be finalized");
        }
        this.status = InvoiceStatus.ISSUED;
        logger.info("Invoice finalized successfully");
    }

    public void cancel() {
        logger.info("Cancelling the invoice");
        if (this.status == InvoiceStatus.PAID) {
            logger.error("Current status ({}) is not {}", this.status, InvoiceStatus.PAID);
            throw new InvalidActionException("Paid invoices cannot be cancelled");
        }
        this.status = InvoiceStatus.CANCELLED;
        logger.info("Invoice cancelled successfully");
    }
}
