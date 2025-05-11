package com.xcelerate.invoicing.dto.events;

import com.xcelerate.invoicing.dto.command.request.CreateInvoiceCommand;
import com.xcelerate.invoicing.enums.EventTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateInvoiceEvent extends AbstractEventDTO implements Serializable {

    private final String customerId;
    private final BigDecimal totalAmount;
    private final LocalDate dueDate;

    public CreateInvoiceEvent(Integer invoiceId, CreateInvoiceCommand createInvoiceCommand) {
        this.eventId = String.valueOf(invoiceId);
        this.eventType = EventTypes.CreateInvoiceCommand.name();
        this.eventTimeStamp = LocalDateTime.now();
        this.customerId = createInvoiceCommand.getCustomerId();
        this.totalAmount = createInvoiceCommand.getTotalAmount();
        this.dueDate = createInvoiceCommand.getDueDate();
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}