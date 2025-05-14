package com.xcelerate.invoicing.dto.command.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class ApplyCreditMemoCommand implements Serializable {

    private Integer invoiceId;

    @NotNull(message = "Credit amount can not be null")
    @DecimalMin(value = "0.01", message = "Credit amount must be more than 0")
    @DecimalMax(value = "999999999", message = "Credit amount cannot be more than 999999999")
    @Digits(integer = 9, fraction = 2, message = "Credit amount must have up to 9 digits and 2 decimal places")
    private BigDecimal creditAmount;

    @NotEmpty(message = "Please enter reason for credit memo")
    private String reason;

    public BigDecimal getCreditAmount() {
        return creditAmount!=null?creditAmount.setScale(2, RoundingMode.HALF_UP):creditAmount;
    }
}