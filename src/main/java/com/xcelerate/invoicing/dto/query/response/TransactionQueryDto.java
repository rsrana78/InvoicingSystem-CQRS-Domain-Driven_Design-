package com.xcelerate.invoicing.dto.query.response;

import com.xcelerate.invoicing.enums.TransactionType;
import com.xcelerate.invoicing.util.CurrencyFormatUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionQueryDto implements Serializable {
    private TransactionType type;
    private String description;
    private BigDecimal amount;
    private String amountString;
    private LocalDateTime timestamp;

    public String getAmountString() {
        return CurrencyFormatUtil.getCurrencyString(this.amount);
    }
}
