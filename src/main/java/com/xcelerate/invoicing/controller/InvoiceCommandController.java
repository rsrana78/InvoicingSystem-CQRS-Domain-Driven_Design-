package com.xcelerate.invoicing.controller;

import com.xcelerate.invoicing.dto.command.request.AddChargeCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyCreditMemoCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyPaymentCommand;
import com.xcelerate.invoicing.dto.command.request.CreateInvoiceCommand;
import com.xcelerate.invoicing.dto.command.response.InvoiceCommandDto;
import com.xcelerate.invoicing.service.InvoiceCommandHandler;
import com.xcelerate.invoicing.util.LoggingUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/invoices")
public class InvoiceCommandController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceCommandController.class);

    private final InvoiceCommandHandler invoiceCommandHandler;


    public InvoiceCommandController(InvoiceCommandHandler invoiceCommandHandler) {
        this.invoiceCommandHandler = invoiceCommandHandler;
    }

    @PostMapping
    public ResponseEntity<InvoiceCommandDto> createInvoice(@Valid @RequestBody CreateInvoiceCommand request) {
        logger.info("Creating invoice with data:{}", LoggingUtil.getString(request));
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        return ResponseEntity.ok(invoice);
    }

    @PostMapping("/{invoiceId}/charges")
    public ResponseEntity<InvoiceCommandDto> addCharge(@PathVariable Integer invoiceId, @Valid @RequestBody AddChargeCommand chargeRequest) {
        chargeRequest.setInvoiceId(invoiceId);
        logger.info("Adding charges to invoice {}. Request data:{}",invoiceId, LoggingUtil.getString(chargeRequest));
        InvoiceCommandDto updatedInvoice = invoiceCommandHandler.addCharge(chargeRequest);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PostMapping("/{invoiceId}/payments")
    public ResponseEntity<InvoiceCommandDto> applyPayment(@PathVariable Integer invoiceId, @Valid @RequestBody ApplyPaymentCommand paymentRequest) {
        paymentRequest.setInvoiceId(invoiceId);
        logger.info("Applying payment to invoice {}. Request data:{}",invoiceId, LoggingUtil.getString(paymentRequest));
        InvoiceCommandDto updatedInvoice = invoiceCommandHandler.applyPayment(paymentRequest);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PostMapping("/{invoiceId}/credit-memo")
    public ResponseEntity<InvoiceCommandDto> creditMemo(@PathVariable Integer invoiceId, @Valid @RequestBody ApplyCreditMemoCommand creditMemoRequest) {
        logger.info("Adding credit memo to invoice {}. Request data:{}", invoiceId, LoggingUtil.getString(creditMemoRequest));
        creditMemoRequest.setInvoiceId(invoiceId);
        InvoiceCommandDto updatedInvoice = invoiceCommandHandler.applyCreditMemo(creditMemoRequest);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PostMapping("/{invoiceId}/finalize")
    public ResponseEntity<InvoiceCommandDto> finalizeInvoice(@PathVariable Integer invoiceId) {
        logger.info("Finalizing the invoice {}", invoiceId);
        InvoiceCommandDto finalized = invoiceCommandHandler.finalizeInvoice(invoiceId);
        return ResponseEntity.ok(finalized);
    }

    @PostMapping("/{invoiceId}/cancel")
    public ResponseEntity<InvoiceCommandDto> cancelInvoice(@PathVariable Integer invoiceId) {
        logger.info("Cancelling invoice with id:{}", invoiceId);
        InvoiceCommandDto finalized = invoiceCommandHandler.cancelInvoice(invoiceId);
        return ResponseEntity.ok(finalized);
    }
}
