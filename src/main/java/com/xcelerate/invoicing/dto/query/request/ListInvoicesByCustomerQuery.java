package com.xcelerate.invoicing.dto.query.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ListInvoicesByCustomerQuery implements Serializable {

    private String customerId;

}
