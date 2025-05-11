package com.xcelerate.invoicing.service;

import com.xcelerate.invoicing.domain.Invoice;
import com.xcelerate.invoicing.dto.command.request.AddChargeCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyCreditMemoCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyPaymentCommand;
import com.xcelerate.invoicing.dto.command.request.CreateInvoiceCommand;
import com.xcelerate.invoicing.dto.command.response.InvoiceCommandDto;
import com.xcelerate.invoicing.dto.events.*;
import com.xcelerate.invoicing.enums.TransactionType;
import com.xcelerate.invoicing.events.publisher.CustomEventPublisher;
import com.xcelerate.invoicing.mapper.InvoiceCommandMapper;
import com.xcelerate.invoicing.repository.InvoiceRepository;
import com.xcelerate.invoicing.util.LoggingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InvoiceCommandHandler {

    private final static Logger logger = LoggerFactory.getLogger(InvoiceCommandHandler.class);


    private final InvoiceRepository invoiceRepository;
    private final ActivityLoggingService activityLoggingService;
    private final InvoiceCommandMapper invoiceCommandMapper;
    private final CustomEventPublisher eventPublisher;

    public InvoiceCommandHandler(InvoiceRepository invoiceRepository,
                                 ActivityLoggingService activityLoggingService,
                                 InvoiceCommandMapper invoiceCommandMapper,
                                 CustomEventPublisher eventPublisher) {
        this.invoiceRepository = invoiceRepository;
        this.activityLoggingService = activityLoggingService;
        this.invoiceCommandMapper = invoiceCommandMapper;
        this.eventPublisher = eventPublisher;
    }

    public InvoiceCommandDto createInvoice(CreateInvoiceCommand createInvoiceCommand) {
        logger.info("Inside createInvoice method of service layer.");
        Invoice invoice = new Invoice(createInvoiceCommand.getCustomerId(), createInvoiceCommand.getDueDate(), createInvoiceCommand.getTotalAmount());
        if(createInvoiceCommand.getAddChargeCommand() != null){
            invoice.addCharge(createInvoiceCommand.getAddChargeCommand().getChargeAmount(),
                    createInvoiceCommand.getAddChargeCommand().getDescription());
        }
        invoice = invoiceRepository.saveAndFlush(invoice);
        logger.info("Invoice created successfully");
        activityLoggingService.logActivity(invoice.getInvoiceId(), "CREATED", LoggingUtil.getString(createInvoiceCommand));
        eventPublisher.publishEvent(new CreateInvoiceEvent(invoice.getInvoiceId(), createInvoiceCommand));
        return invoiceCommandMapper.toDto(invoice);
    }

    public InvoiceCommandDto addCharge(AddChargeCommand addChargeCommand) {
        logger.info("Inside addCharge method of service layer. Adding charge for invoice {}",addChargeCommand.getInvoiceId());
        Invoice invoice = invoiceRepository.findById(addChargeCommand.getInvoiceId()).orElseThrow();
        invoice.addCharge(addChargeCommand.getChargeAmount(), addChargeCommand.getDescription());
        invoice = invoiceRepository.saveAndFlush(invoice);
        logger.info("Charges added successfully for invoice {}", addChargeCommand.getInvoiceId());
        activityLoggingService.logActivity(invoice.getInvoiceId(), TransactionType.CHARGE.name(), LoggingUtil.getString(addChargeCommand));
        eventPublisher.publishEvent(new AddChargeEvent(invoice.getInvoiceId(), addChargeCommand));
        return invoiceCommandMapper.toDto(invoice);
    }

    public InvoiceCommandDto applyPayment(ApplyPaymentCommand applyPaymentCommand) {
        logger.info("Inside applyPayment method of service layer.");
        Invoice invoice = invoiceRepository.findById(applyPaymentCommand.getInvoiceId()).orElseThrow();
        invoice.applyPayment(applyPaymentCommand.getPaymentAmount(), applyPaymentCommand.getDescription());
        invoice = invoiceRepository.saveAndFlush(invoice);
        logger.info("Payment applied successfully for invoice {}", applyPaymentCommand.getInvoiceId());
        activityLoggingService.logActivity(invoice.getInvoiceId(), TransactionType.PAYMENT.name(), LoggingUtil.getString(applyPaymentCommand));
        eventPublisher.publishEvent(new ApplyPaymentEvent(invoice.getInvoiceId(), applyPaymentCommand));
        return invoiceCommandMapper.toDto(invoice);
    }

    public InvoiceCommandDto applyCreditMemo(ApplyCreditMemoCommand creditMemoCommand) {
        logger.info("Inside addCreditMemo method  of service layer.");
        Invoice invoice = invoiceRepository.findById(creditMemoCommand.getInvoiceId()).orElseThrow();
        invoice.applyCreditMemo(creditMemoCommand.getCreditAmount(), creditMemoCommand.getReason());
        invoice = invoiceRepository.saveAndFlush(invoice);
        logger.info("Credit memo applied successfully for invoice {}", creditMemoCommand.getInvoiceId());
        activityLoggingService.logActivity(invoice.getInvoiceId(), TransactionType.CREDIT_MEMO.name(), LoggingUtil.getString(creditMemoCommand));
        eventPublisher.publishEvent(new ApplyCreditMemoEvent(invoice.getInvoiceId(), creditMemoCommand));
        return invoiceCommandMapper.toDto(invoice);
    }

    public InvoiceCommandDto finalizeInvoice(Integer invoiceId) {
        logger.info("Inside finalizeInvoice method of service layer");
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();
        invoice.finalizeInvoice();
        invoice = invoiceRepository.saveAndFlush(invoice);
        logger.info("Invoice {} finalized successfully", invoiceId);
        activityLoggingService.logActivity(invoice.getInvoiceId(), "FINALIZE", String.valueOf(invoiceId));
        eventPublisher.publishEvent(new FinalizeInvoiceEvent(invoice.getInvoiceId()));
        return invoiceCommandMapper.toDto(invoice);
    }

    public InvoiceCommandDto cancelInvoice(Integer invoiceId) {
        logger.info("Inside cancelInvoice method of service layer.");
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();
        invoice.cancel();
        invoice = invoiceRepository.saveAndFlush(invoice);
        logger.info("Invoice {} cancelled successfully", invoiceId);
        activityLoggingService.logActivity(invoice.getInvoiceId(), "CANCEL", String.valueOf(invoiceId));
        eventPublisher.publishEvent(new FinalizeInvoiceEvent(invoice.getInvoiceId()));
        return invoiceCommandMapper.toDto(invoice);
    }
}
