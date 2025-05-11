package com.xcelerate.invoicing.dto.query.response;

import java.io.Serializable;
import java.math.BigDecimal;

public class RevenueDTO implements Serializable {

    private BigDecimal totalInvoice;
    private BigDecimal totalReceived;
    private BigDecimal outstanding;

    public RevenueDTO(BigDecimal totalInvoice, BigDecimal totalReceived) {
        this.totalInvoice = totalInvoice;
        this.totalReceived = totalReceived;
        this.outstanding = totalInvoice.subtract(totalReceived);
    }

    public BigDecimal getTotalInvoice() {
        return totalInvoice;
    }

    public void setTotalInvoice(BigDecimal totalInvoice) {
        this.totalInvoice = totalInvoice;
    }

    public BigDecimal getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(BigDecimal totalReceived) {
        this.totalReceived = totalReceived;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }
}
