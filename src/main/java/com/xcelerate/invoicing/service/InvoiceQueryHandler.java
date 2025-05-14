package com.xcelerate.invoicing.service;

import com.xcelerate.invoicing.domain.InvoiceDomain;
import com.xcelerate.invoicing.domain.TransactionDomain;
import com.xcelerate.invoicing.dto.query.response.InvoiceQueryDto;
import com.xcelerate.invoicing.dto.query.response.RevenueDTO;
import com.xcelerate.invoicing.dto.query.response.TransactionQueryDto;
import com.xcelerate.invoicing.entity.InvoiceEntity;
import com.xcelerate.invoicing.entity.TransactionEntity;
import com.xcelerate.invoicing.enums.InvoiceStatus;
import com.xcelerate.invoicing.mapper.InvoiceMapper;
import com.xcelerate.invoicing.mapper.TransactionMapper;
import com.xcelerate.invoicing.repository.InvoiceRepository;
import com.xcelerate.invoicing.repository.InvoiceTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class InvoiceQueryHandler {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceQueryHandler.class);

    private final InvoiceRepository invoiceRepository;
    private final InvoiceTransactionRepository invoiceTransactionRepository;
    private final InvoiceMapper invoiceMapper;
    private final TransactionMapper transactionMapper;

    public InvoiceQueryHandler(InvoiceRepository invoiceRepository,
                               InvoiceTransactionRepository invoiceTransactionRepository,
                               InvoiceMapper invoiceMapper,
                               TransactionMapper transactionMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceTransactionRepository = invoiceTransactionRepository;
        this.invoiceMapper = invoiceMapper;
        this.transactionMapper = transactionMapper;
    }

    public InvoiceQueryDto getInvoiceById(Integer invoiceId) {
        logger.info("Getting invoice data for ID:{}",invoiceId);
        InvoiceEntity entity = invoiceRepository.findById(invoiceId).orElseThrow(() -> new NoSuchElementException("Invoice not found"));
        InvoiceDomain domain = invoiceMapper.map(entity);
        return invoiceMapper.domainToQueryDto(domain);
    }

    public List<InvoiceQueryDto> getInvoiceByCustomerId(String customerId) {
        logger.info("Getting invoices data for customer:{}",customerId);
        List<InvoiceEntity> entityList = invoiceRepository.findAllByCustomerId(customerId);
        List<InvoiceDomain> domainList = entityList.stream().map(e->invoiceMapper.map(e)).collect(Collectors.toList());
        return invoiceMapper.domainToQueryDtoList(domainList);
    }

    public List<InvoiceQueryDto> getOutstandingInvoices() {
        logger.info("Getting all outstanding invoices");
        List<InvoiceEntity> entityList = invoiceRepository.findAllByStatusIs(InvoiceStatus.ISSUED);
        List<InvoiceDomain> domainList = entityList.stream().map(e->invoiceMapper.map(e)).collect(Collectors.toList());
        return invoiceMapper.domainToQueryDtoList(domainList);
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
        List<TransactionEntity> transactionEntityList = invoiceTransactionRepository.findAllByInvoice_id(invoiceId);
        List<TransactionDomain> domainList = transactionEntityList.stream().map(e->transactionMapper.map(e)).collect(Collectors.toList());
        return transactionMapper.domainToQueryDtoList(domainList);
    }
}
