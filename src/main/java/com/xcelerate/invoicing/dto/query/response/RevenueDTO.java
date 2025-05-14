package com.xcelerate.invoicing.dto.query.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueDTO implements Serializable {

    private BigDecimal totalInvoice;
    private BigDecimal totalReceived;
    private BigDecimal outstanding;

    public RevenueDTO(BigDecimal totalInvoice, BigDecimal totalReceived) {
        this.totalInvoice = totalInvoice;
        this.totalReceived = totalReceived;
        this.outstanding = totalInvoice.subtract(totalReceived);
    }
}
