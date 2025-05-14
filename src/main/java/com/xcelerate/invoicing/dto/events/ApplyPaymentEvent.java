package com.xcelerate.invoicing.dto.events;

import com.xcelerate.invoicing.dto.command.request.ApplyPaymentCommand;
import com.xcelerate.invoicing.enums.EventTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ApplyPaymentEvent extends AbstractEventDTO implements Serializable {

    private final Integer invoiceId;
    private final BigDecimal paymentAmount;
    private final String description;

    public ApplyPaymentEvent(Integer invoiceId, ApplyPaymentCommand applyPaymentCommand) {
        this.eventId = String.valueOf(invoiceId);
        this.eventType = EventTypes.ApplyPaymentCommand.name();
        this.eventTimeStamp = LocalDateTime.now();
        this.invoiceId = invoiceId;
        this.paymentAmount = applyPaymentCommand.getPaymentAmount();
        this.description = applyPaymentCommand.getDescription();
    }

    public Integer getId() {
        return invoiceId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public String getDescription() {
        return description;
    }
}