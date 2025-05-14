package com.xcelerate.invoicing.dto.events;

import com.xcelerate.invoicing.dto.command.request.AddChargeCommand;
import com.xcelerate.invoicing.enums.EventTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AddChargeEvent extends AbstractEventDTO implements Serializable {

    private final Integer invoiceId;
    private final BigDecimal chargeAmount;
    private final String description;

    public AddChargeEvent(Integer invoiceId, AddChargeCommand addChargeCommand) {
        this.eventId = String.valueOf(invoiceId);
        this.eventType = EventTypes.AddChargeCommand.name();
        this.eventTimeStamp = LocalDateTime.now();
        this.invoiceId = invoiceId;
        this.chargeAmount = addChargeCommand.getChargeAmount();
        this.description = addChargeCommand.getDescription();
    }

    public Integer getId() {
        return invoiceId;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public String getDescription() {
        return description;
    }
}