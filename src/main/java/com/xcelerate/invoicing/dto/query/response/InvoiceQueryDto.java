package com.xcelerate.invoicing.dto.query.response;

import com.xcelerate.invoicing.enums.InvoiceStatus;
import com.xcelerate.invoicing.util.CurrencyFormatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceQueryDto implements Serializable {

    private Integer id;
    private String customerId;
    private InvoiceStatus status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private BigDecimal balance;
    private String totalAmountString;
    private String balanceString;
    private List<TransactionQueryDto> transactions;

    public String getTotalAmountString() {
        return CurrencyFormatUtil.getCurrencyString(this.totalAmount);
    }

    public String getBalanceString() {
        return CurrencyFormatUtil.getCurrencyString(this.balance);
    }

}
