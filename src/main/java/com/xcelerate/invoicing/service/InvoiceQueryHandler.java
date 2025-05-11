package com.xcelerate.invoicing.service;

import com.xcelerate.invoicing.domain.Invoice;
import com.xcelerate.invoicing.domain.Transaction;
import com.xcelerate.invoicing.dto.query.response.InvoiceQueryDto;
import com.xcelerate.invoicing.dto.query.response.RevenueDTO;
import com.xcelerate.invoicing.dto.query.response.TransactionQueryDto;
import com.xcelerate.invoicing.enums.InvoiceStatus;
import com.xcelerate.invoicing.mapper.InvoiceQueryMapper;
import com.xcelerate.invoicing.mapper.TransactionQueryMapper;
import com.xcelerate.invoicing.repository.InvoiceRepository;
import com.xcelerate.invoicing.repository.InvoiceTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InvoiceQueryHandler {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceQueryHandler.class);

    private final InvoiceRepository invoiceRepository;
    private final InvoiceTransactionRepository invoiceTransactionRepository;
    private final InvoiceQueryMapper invoiceQueryMapper;
    private final TransactionQueryMapper transactionQueryMapper;

    public InvoiceQueryHandler(InvoiceRepository invoiceRepository,
                               InvoiceTransactionRepository invoiceTransactionRepository,
                               InvoiceQueryMapper invoiceQueryMapper,
                               TransactionQueryMapper transactionQueryMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceTransactionRepository = invoiceTransactionRepository;
        this.invoiceQueryMapper = invoiceQueryMapper;
        this.transactionQueryMapper = transactionQueryMapper;
    }

    public InvoiceQueryDto getInvoiceById(Integer invoiceId) {
        logger.info("Getting invoice data for ID:{}",invoiceId);
        Invoice entity = invoiceRepository.findById(invoiceId).orElseThrow(() -> new NoSuchElementException("Invoice not found"));
        return invoiceQueryMapper.toDto(entity);
    }

    public List<InvoiceQueryDto> getInvoiceByCustomerId(String customerId) {
        logger.info("Getting invoices data for customer:{}",customerId);
        List<Invoice> customerInvoiceList = invoiceRepository.findAllByCustomerId(customerId);
        return invoiceQueryMapper.toDto(customerInvoiceList);
    }

    public List<InvoiceQueryDto> getOutstandingInvoices() {
        logger.info("Getting all outstanding invoices");
        List<Invoice> customerInvoiceList = invoiceRepository.findAllByStatusIs(InvoiceStatus.ISSUED);
        return invoiceQueryMapper.toDto(customerInvoiceList);
    }

    public RevenueDTO getRevenue() {
        logger.info("Getting revenue");
        Object[] revenue = invoiceRepository.getRevenue();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalBalance = BigDecimal.ZERO;
        if(revenue != null && revenue.length>0){
            Object[] innerArray = (Object[]) revenue[0];
            totalAmount = (BigDecimal) innerArray[0];
            totalBalance = (BigDecimal) innerArray[1];
        }
        return new RevenueDTO(totalAmount, totalBalance);
    }

    public List<TransactionQueryDto> getInvoiceTransactions(Integer invoiceId){
        logger.info("Getting invoice transactions data for ID:{}",invoiceId);
        List<Transaction> invoiceTransactionList = invoiceTransactionRepository.findAllByInvoice_invoiceId(invoiceId);
        return transactionQueryMapper.toDto(invoiceTransactionList);
    }
}
