package com.xcelerate.invoicing.dto.command.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class AddChargeCommand implements Serializable {

    private Integer invoiceId;

    @NotNull(message = "Charge amount can not be null")
    @DecimalMin(value = "0.01", message = "Charge amount must be more than 0")
    @DecimalMax(value = "999999999", message = "Charge amount cannot be more than 999999999")
    @Digits(integer = 9, fraction = 2, message = "Charge amount must have up to 9 digits and 2 decimal places")
    private BigDecimal chargeAmount;

    @NotEmpty(message = "Please enter description")
    private String description;

    public BigDecimal getChargeAmount() {
        return chargeAmount!=null?chargeAmount.setScale(2, RoundingMode.HALF_UP):chargeAmount;
    }
}