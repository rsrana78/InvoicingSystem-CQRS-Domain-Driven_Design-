package com.xcelerate.invoicing.dto.events;

import com.xcelerate.invoicing.dto.command.request.ApplyCreditMemoCommand;
import com.xcelerate.invoicing.enums.EventTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ApplyCreditMemoEvent extends AbstractEventDTO implements Serializable {

    private final Integer invoiceId;
    private final BigDecimal creditAmount;
    private final String reason;

    public ApplyCreditMemoEvent(Integer invoiceId, ApplyCreditMemoCommand applyCreditMemoCommand) {
        this.eventId = String.valueOf(invoiceId);
        this.eventType = EventTypes.ApplyCreditMemoCommand.name();
        this.eventTimeStamp = LocalDateTime.now();
        this.invoiceId = invoiceId;
        this.creditAmount = applyCreditMemoCommand.getCreditAmount();
        this.reason = applyCreditMemoCommand.getReason();
    }

    public Integer getId() {
        return invoiceId;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public String getReason() {
        return reason;
    }
}