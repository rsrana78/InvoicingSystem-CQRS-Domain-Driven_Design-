package com.xcelerate.invoicing.dto.command.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeInvoiceCommand implements Serializable {

    private Integer invoiceId;
}
