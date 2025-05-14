package com.xcelerate.invoicing.service;

import com.xcelerate.invoicing.domain.InvoiceDomain;
import com.xcelerate.invoicing.dto.command.request.AddChargeCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyCreditMemoCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyPaymentCommand;
import com.xcelerate.invoicing.dto.command.request.CreateInvoiceCommand;
import com.xcelerate.invoicing.dto.command.response.InvoiceCommandDto;
import com.xcelerate.invoicing.dto.events.*;
import com.xcelerate.invoicing.entity.InvoiceEntity;
import com.xcelerate.invoicing.entity.TransactionEntity;
import com.xcelerate.invoicing.enums.TransactionType;
import com.xcelerate.invoicing.events.publisher.CustomEventPublisher;
import com.xcelerate.invoicing.mapper.InvoiceMapper;
import com.xcelerate.invoicing.repository.InvoiceRepository;
import com.xcelerate.invoicing.util.LoggingUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InvoiceCommandHandler {

    private final static Logger logger = LoggerFactory.getLogger(InvoiceCommandHandler.class);


    private final InvoiceRepository invoiceRepository;
    private final ActivityLoggingService activityLoggingService;
    private final InvoiceMapper invoiceMapper;
    private final CustomEventPublisher eventPublisher;

    public InvoiceCommandHandler(InvoiceRepository invoiceRepository,
                                 ActivityLoggingService activityLoggingService,
                                 InvoiceMapper invoiceMapper,
                                 CustomEventPublisher eventPublisher) {
        this.invoiceRepository = invoiceRepository;
        this.activityLoggingService = activityLoggingService;
        this.invoiceMapper = invoiceMapper;
        this.eventPublisher = eventPublisher;
    }

    public InvoiceCommandDto createInvoice(CreateInvoiceCommand createInvoiceCommand) {
        logger.info("Inside createInvoice method of service layer.");
        InvoiceDomain invoiceDomain = new InvoiceDomain(createInvoiceCommand.getCustomerId(), createInvoiceCommand.getDueDate(), createInvoiceCommand.getTotalAmount());
        if(createInvoiceCommand.getAddChargeCommand() != null){
            invoiceDomain.addCharge(createInvoiceCommand.getAddChargeCommand().getChargeAmount(),
                    createInvoiceCommand.getAddChargeCommand().getDescription());
        }
        InvoiceEntity invoiceEntity = createInvoice(invoiceDomain);
        invoiceDomain.setId(invoiceEntity.getId());
        logger.info("Invoice created successfully");
        activityLoggingService.logActivity(invoiceDomain.getId(), "CREATED", LoggingUtil.getString(createInvoiceCommand));
        eventPublisher.publishEvent(new CreateInvoiceEvent(invoiceDomain.getId(), createInvoiceCommand));
        return invoiceMapper.domainToCommandDTO(invoiceDomain);
    }

    public InvoiceCommandDto addCharge(AddChargeCommand addChargeCommand) {
        logger.info("Inside addCharge method of service layer. Adding charge for invoice {}",addChargeCommand.getInvoiceId());
        InvoiceEntity invoiceEntity = invoiceRepository.findById(addChargeCommand.getInvoiceId()).orElseThrow();
        InvoiceDomain invoiceDomain = invoiceMapper.map(invoiceEntity);
        invoiceDomain.addCharge(addChargeCommand.getChargeAmount(), addChargeCommand.getDescription());
        updateInvoice(invoiceEntity, invoiceDomain);
        logger.info("Charges added successfully for invoice {}", addChargeCommand.getInvoiceId());
        activityLoggingService.logActivity(invoiceDomain.getId(), TransactionType.CHARGE.name(), LoggingUtil.getString(addChargeCommand));
        eventPublisher.publishEvent(new AddChargeEvent(invoiceDomain.getId(), addChargeCommand));
        return invoiceMapper.domainToCommandDTO(invoiceDomain);
    }

    public InvoiceCommandDto applyPayment(ApplyPaymentCommand applyPaymentCommand) {
        logger.info("Inside applyPayment method of service layer.");
        InvoiceEntity invoiceEntity = invoiceRepository.findById(applyPaymentCommand.getInvoiceId()).orElseThrow();
        InvoiceDomain invoiceDomain = invoiceMapper.map(invoiceEntity);
        invoiceDomain.applyPayment(applyPaymentCommand.getPaymentAmount(), applyPaymentCommand.getDescription());
        updateInvoice(invoiceEntity, invoiceDomain);
        logger.info("Payment applied successfully for invoice {}", applyPaymentCommand.getInvoiceId());
        activityLoggingService.logActivity(invoiceDomain.getId(), TransactionType.PAYMENT.name(), LoggingUtil.getString(applyPaymentCommand));
        eventPublisher.publishEvent(new ApplyPaymentEvent(invoiceDomain.getId(), applyPaymentCommand));
        return invoiceMapper.domainToCommandDTO(invoiceDomain);
    }

    public InvoiceCommandDto applyCreditMemo(ApplyCreditMemoCommand creditMemoCommand) {
        logger.info("Inside addCreditMemo method  of service layer.");
        InvoiceEntity invoiceEntity = invoiceRepository.findById(creditMemoCommand.getInvoiceId()).orElseThrow();
        InvoiceDomain invoiceDomain = invoiceMapper.map(invoiceEntity);
        invoiceDomain.applyCreditMemo(creditMemoCommand.getCreditAmount(), creditMemoCommand.getReason());
        updateInvoice(invoiceEntity, invoiceDomain);
        logger.info("Credit memo applied successfully for invoice {}", creditMemoCommand.getInvoiceId());
        activityLoggingService.logActivity(invoiceDomain.getId(), TransactionType.CREDIT_MEMO.name(), LoggingUtil.getString(creditMemoCommand));
        eventPublisher.publishEvent(new ApplyCreditMemoEvent(invoiceDomain.getId(), creditMemoCommand));
        return invoiceMapper.domainToCommandDTO(invoiceDomain);
    }

    public InvoiceCommandDto finalizeInvoice(Integer invoiceId) {
        logger.info("Inside finalizeInvoice method of service layer");
        InvoiceEntity invoiceEntity = invoiceRepository.findById(invoiceId).orElseThrow();
        InvoiceDomain invoiceDomain = invoiceMapper.map(invoiceEntity);
        invoiceDomain.finalizeInvoice();
        updateInvoice(invoiceEntity, invoiceDomain);
        logger.info("Invoice {} finalized successfully", invoiceId);
        activityLoggingService.logActivity(invoiceDomain.getId(), "FINALIZE", String.valueOf(invoiceId));
        eventPublisher.publishEvent(new FinalizeInvoiceEvent(invoiceDomain.getId()));
        return invoiceMapper.domainToCommandDTO(invoiceDomain);
    }

    public InvoiceCommandDto cancelInvoice(Integer invoiceId) {
        logger.info("Inside cancelInvoice method of service layer.");
        InvoiceEntity invoiceEntity = invoiceRepository.findById(invoiceId).orElseThrow();
        InvoiceDomain invoiceDomain = invoiceMapper.map(invoiceEntity);
        invoiceDomain.cancel();
        invoiceRepository.saveAndFlush(invoiceEntity);
        logger.info("Invoice {} cancelled successfully", invoiceId);
        activityLoggingService.logActivity(invoiceDomain.getId(), "CANCEL", String.valueOf(invoiceId));
        eventPublisher.publishEvent(new FinalizeInvoiceEvent(invoiceDomain.getId()));
        return invoiceMapper.domainToCommandDTO(invoiceDomain);
    }

    private InvoiceEntity createInvoice(InvoiceDomain invoiceDomain){
        InvoiceEntity invoiceEntity = new InvoiceEntity(invoiceDomain.getCustomerId(), invoiceDomain.getDueDate(), invoiceDomain.getTotalAmount());
        if(!CollectionUtils.isEmpty(invoiceDomain.getTransactions())){
            List<TransactionEntity> transactionEntityList = new ArrayList<>();
            InvoiceEntity finalInvoiceEntity = invoiceEntity;
            invoiceDomain.getTransactions().forEach(t->{
                TransactionEntity transactionEntity = new TransactionEntity(finalInvoiceEntity, t.getType(), t.getAmount(), t.getDescription());
                transactionEntityList.add(transactionEntity);
            });
            invoiceEntity.setTransactions(transactionEntityList);
        }
        invoiceEntity = invoiceRepository.saveAndFlush(invoiceEntity);
        return invoiceEntity;
    }

    private void updateInvoice(InvoiceEntity invoiceEntity, InvoiceDomain invoiceDomain){
        invoiceEntity.setStatus(invoiceDomain.getStatus());
        invoiceEntity.setDueDate(invoiceDomain.getDueDate());
        invoiceEntity.setBalance(invoiceDomain.getBalance());
        invoiceEntity.setCustomerId(invoiceDomain.getCustomerId());
        invoiceEntity.setTotalAmount(invoiceDomain.getTotalAmount());
        invoiceEntity.setCreatedAt(invoiceDomain.getCreatedAt());
        if(!CollectionUtils.isEmpty(invoiceDomain.getTransactions())){
            InvoiceEntity finalInvoiceEntity = invoiceEntity;
            invoiceDomain.getTransactions().forEach(t->{
                if(t.getId() == null){
                    TransactionEntity transactionEntity = new TransactionEntity(finalInvoiceEntity, t.getType(), t.getAmount(), t.getDescription());
                    invoiceEntity.getTransactions().add(transactionEntity);
                }
            });
        }
        invoiceRepository.saveAndFlush(invoiceEntity);
    }
}
