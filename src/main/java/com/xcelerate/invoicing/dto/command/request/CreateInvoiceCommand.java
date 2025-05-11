package com.xcelerate.invoicing.dto.command.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class CreateInvoiceCommand implements Serializable {

    @NotEmpty(message = "Please enter customer ID")
    private String customerId;

    @NotNull(message = "Invoice amount can not be null")
    @DecimalMin(value = "0.01", message = "Amount must be more than 0")
    @DecimalMax(value = "999999999", message = "Amount cannot be more than 999999999")
    @Digits(integer = 9, fraction = 2, message = "Amount must have up to 9 digits and 2 decimal places")
    private BigDecimal totalAmount;


    @NotNull(message = "Invoice due date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private AddChargeCommand addChargeCommand;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount!=null?totalAmount.setScale(2, RoundingMode.HALF_UP):totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public AddChargeCommand getAddChargeCommand() {
        return addChargeCommand;
    }

    public void setAddChargeCommand(AddChargeCommand addChargeCommand) {
        this.addChargeCommand = addChargeCommand;
    }
}