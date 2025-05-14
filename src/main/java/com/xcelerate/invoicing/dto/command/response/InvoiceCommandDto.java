package com.xcelerate.invoicing.dto.command.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class InvoiceCommandDto implements Serializable {

    private Integer id;
    private String customerId;
    private InvoiceStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:MM:SS")

    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private BigDecimal balance;

    private String totalAmountString;
    private String balanceString;
    private List<TransactionCommandDto> transactions;

    public String getBalanceString() {
        return CurrencyFormatUtil.getCurrencyString(this.balance);
    }
}
