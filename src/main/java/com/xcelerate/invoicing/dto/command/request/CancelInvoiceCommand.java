package com.xcelerate.invoicing.dto.command.request;

import java.io.Serializable;

public class CancelInvoiceCommand implements Serializable {

    private Integer invoiceId;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
}
