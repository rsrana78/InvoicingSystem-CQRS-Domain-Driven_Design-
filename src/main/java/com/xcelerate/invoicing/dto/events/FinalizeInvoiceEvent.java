package com.xcelerate.invoicing.dto.events;

import com.xcelerate.invoicing.enums.EventTypes;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FinalizeInvoiceEvent extends AbstractEventDTO implements Serializable {

    private final Integer invoiceId;

    public FinalizeInvoiceEvent(Integer invoiceId) {
        this.eventId = String.valueOf(invoiceId);
        this.eventType = EventTypes.AddChargeCommand.name();
        this.eventTimeStamp = LocalDateTime.now();
        this.invoiceId = invoiceId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }
}
