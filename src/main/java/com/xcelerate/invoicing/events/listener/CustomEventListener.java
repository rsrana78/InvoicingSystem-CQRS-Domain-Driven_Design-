package com.xcelerate.invoicing.events.listener;

import com.xcelerate.invoicing.dto.events.*;
import com.xcelerate.invoicing.util.LoggingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomEventListener {

    private static final Logger logger = LoggerFactory.getLogger(CustomEventListener.class);

    @EventListener
    public void handleInvoiceEvent(Object event) {
        if(event instanceof CreateInvoiceEvent){
            CreateInvoiceEvent invoiceEvent = (CreateInvoiceEvent) event;
            logger.info("{} Event Received with data {}",invoiceEvent.getEventType(), LoggingUtil.getString(invoiceEvent));
        } else if(event instanceof AddChargeEvent){
            AddChargeEvent invoiceEvent = (AddChargeEvent) event;
            logger.info("{} Event Received with data {}",invoiceEvent.getEventType(), LoggingUtil.getString(invoiceEvent));
        } else if(event instanceof ApplyPaymentEvent){
            ApplyPaymentEvent invoiceEvent = (ApplyPaymentEvent) event;
            logger.info("{} Event Received with data {}",invoiceEvent.getEventType(), LoggingUtil.getString(invoiceEvent));
        } else if(event instanceof ApplyCreditMemoEvent){
            ApplyCreditMemoEvent invoiceEvent = (ApplyCreditMemoEvent) event;
            logger.info("{} Event Received with data {}",invoiceEvent.getEventType(), LoggingUtil.getString(invoiceEvent));
        } else if(event instanceof CancelInvoiceEvent){
            CancelInvoiceEvent invoiceEvent = (CancelInvoiceEvent) event;
            logger.info("{} Event Received with data {}",invoiceEvent.getEventType(), LoggingUtil.getString(invoiceEvent));
        } else if(event instanceof FinalizeInvoiceEvent){
            FinalizeInvoiceEvent invoiceEvent = (FinalizeInvoiceEvent) event;
            logger.info("{} Event Received with data {}",invoiceEvent.getEventType(), LoggingUtil.getString(invoiceEvent));
        }
    }


}

