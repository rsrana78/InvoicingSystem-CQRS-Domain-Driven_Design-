package com.xcelerate.invoicing.dto.command.request;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ApplyPaymentCommand implements Serializable {

    private Integer invoiceId;
    @NotNull(message = "Payment amount can not be null")
    @DecimalMin(value = "0.01", message = "Payment amount must be more than 0")
    @DecimalMax(value = "999999999", message = "Payment amount cannot be more than 999999999")
    @Digits(integer = 9, fraction = 2, message = "Payment amount must have up to 9 digits and 2 decimal places")
    private BigDecimal paymentAmount;

    @NotEmpty(message = "Please enter payment description")
    private String description;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount!=null?paymentAmount.setScale(2, RoundingMode.HALF_UP):paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}