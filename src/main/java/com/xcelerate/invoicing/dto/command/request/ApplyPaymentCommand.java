package com.xcelerate.invoicing.dto.command.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyPaymentCommand implements Serializable {

    private Integer invoiceId;
    @NotNull(message = "Payment amount can not be null")
    @DecimalMin(value = "0.01", message = "Payment amount must be more than 0")
    @DecimalMax(value = "999999999", message = "Payment amount cannot be more than 999999999")
    @Digits(integer = 9, fraction = 2, message = "Payment amount must have up to 9 digits and 2 decimal places")
    private BigDecimal paymentAmount;

    @NotEmpty(message = "Please enter payment description")
    private String description;

    public BigDecimal getPaymentAmount() {
        return paymentAmount!=null?paymentAmount.setScale(2, RoundingMode.HALF_UP):paymentAmount;
    }
}