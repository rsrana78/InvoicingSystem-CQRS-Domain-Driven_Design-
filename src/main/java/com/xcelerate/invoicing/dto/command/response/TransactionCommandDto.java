package com.xcelerate.invoicing.dto.command.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xcelerate.invoicing.enums.TransactionType;
import com.xcelerate.invoicing.util.CurrencyFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCommandDto implements Serializable {
    private TransactionType type;
    private String description;
    private BigDecimal amount;
    private String amountString;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:MM:SS")

    private LocalDateTime timestamp;

    public String getAmountString() {
        return CurrencyFormatUtil.getCurrencyString(this.amount);
    }
}
