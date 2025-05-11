package com.xcelerate.invoicing.dto.query.request;

import java.io.Serializable;

public class ListInvoicesByCustomerQuery implements Serializable {

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
