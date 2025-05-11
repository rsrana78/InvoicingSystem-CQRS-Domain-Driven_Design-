package com.xcelerate.invoicing.controller;

import com.xcelerate.invoicing.dto.query.response.InvoiceQueryDto;
import com.xcelerate.invoicing.dto.query.response.RevenueDTO;
import com.xcelerate.invoicing.dto.query.response.TransactionQueryDto;
import com.xcelerate.invoicing.service.InvoiceQueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceQueryController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceQueryController.class);
    private final InvoiceQueryHandler invoiceQueryHandler;

    public InvoiceQueryController(InvoiceQueryHandler invoiceQueryHandler) {
        this.invoiceQueryHandler = invoiceQueryHandler;
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceQueryDto> getInvoiceById(@PathVariable Integer invoiceId) {
        logger.info("Getting invoice by ID:{}",invoiceId);
        return ResponseEntity.ok(invoiceQueryHandler.getInvoiceById(invoiceId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InvoiceQueryDto>> getInvoiceByCustomerId(@PathVariable String customerId) {
        logger.info("Getting invoice by customer ID:{}",customerId);
        return ResponseEntity.ok(invoiceQueryHandler.getInvoiceByCustomerId(customerId));
    }


    @GetMapping("/outstanding")
    public ResponseEntity<List<InvoiceQueryDto>> getOutstandingInvoices() {
        logger.info("Getting all outstanding invoices");
        return ResponseEntity.ok(invoiceQueryHandler.getOutstandingInvoices());
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueDTO> getTotalRevenue() {
        logger.info("Getting total revenue");
        return ResponseEntity.ok(invoiceQueryHandler.getRevenue());
    }

    @GetMapping("/transactions/{invoiceId}")
    public ResponseEntity<List<TransactionQueryDto>> getTransactionsByInvoiceId(@PathVariable Integer invoiceId) {
        logger.info("Getting invoice transactions by ID:{}",invoiceId);
        return ResponseEntity.ok(invoiceQueryHandler.getInvoiceTransactions(invoiceId));
    }
}
