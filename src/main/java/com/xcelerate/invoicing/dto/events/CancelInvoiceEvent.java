package com.xcelerate.invoicing.dto.events;

import com.xcelerate.invoicing.enums.EventTypes;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CancelInvoiceEvent extends AbstractEventDTO implements Serializable {

    private final Integer invoiceId;

    public CancelInvoiceEvent(Integer invoiceId) {
        this.eventId = String.valueOf(invoiceId);
        this.eventType = EventTypes.CancelInvoiceCommand.name();
        this.eventTimeStamp = LocalDateTime.now();
        this.invoiceId = invoiceId;
    }

    public Integer getId() {
        return invoiceId;
    }
}
