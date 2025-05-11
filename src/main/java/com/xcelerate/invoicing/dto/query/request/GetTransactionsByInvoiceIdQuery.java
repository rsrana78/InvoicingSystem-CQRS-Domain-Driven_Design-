package com.xcelerate.invoicing.dto.query.request;

import java.io.Serializable;

public class GetTransactionsByInvoiceIdQuery implements Serializable {

    private Integer invoiceId;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
}
