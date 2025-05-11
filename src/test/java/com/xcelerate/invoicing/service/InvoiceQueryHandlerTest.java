package com.xcelerate.invoicing.service;

import com.xcelerate.invoicing.dto.command.request.AddChargeCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyCreditMemoCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyPaymentCommand;
import com.xcelerate.invoicing.dto.command.request.CreateInvoiceCommand;
import com.xcelerate.invoicing.dto.command.response.InvoiceCommandDto;
import com.xcelerate.invoicing.dto.command.response.TransactionCommandDto;
import com.xcelerate.invoicing.dto.query.response.InvoiceQueryDto;
import com.xcelerate.invoicing.dto.query.response.TransactionQueryDto;
import com.xcelerate.invoicing.enums.TransactionType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class InvoiceQueryHandlerTest {

    @Autowired
    private InvoiceCommandHandler invoiceCommandHandler;

    @Autowired
    private InvoiceQueryHandler invoiceQueryHandler;

    @Test
    void testGetInvoiceById() {
        //Setup
        InvoiceCommandDto command = addInvoice();
        TransactionCommandDto chargeCommand = command.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CHARGE)).findFirst().get();
        TransactionCommandDto paymentCommand = command.getTransactions().stream().filter(t->t.getType().equals(TransactionType.PAYMENT)).findFirst().get();
        TransactionCommandDto memoCommand = command.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CREDIT_MEMO)).findFirst().get();
        //Execution
        InvoiceQueryDto query = invoiceQueryHandler.getInvoiceById(command.getInvoiceId());
        //Verification
        assertEquals(command.getTotalAmount(), query.getTotalAmount());
        assertEquals(command.getCustomerId(), query.getCustomerId());
        assertEquals(command.getBalance(), query.getBalance());
        assertEquals(command.getStatus(), query.getStatus());
        assertEquals(command.getDueDate(), query.getDueDate());
        assertEquals(command.getCreatedAt(), query.getCreatedAt());
        assertEquals(command.getInvoiceId(), query.getInvoiceId());
        assertEquals(command.getTransactions().size(), query.getTransactions().size());//Charge, payment, and Memo

        TransactionQueryDto chargeQuery = query.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CHARGE)).findFirst().get();
        TransactionQueryDto paymentQuery = query.getTransactions().stream().filter(t->t.getType().equals(TransactionType.PAYMENT)).findFirst().get();
        TransactionQueryDto memoQuery = query.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CREDIT_MEMO)).findFirst().get();
        //Verify charge
        assertEquals(chargeCommand.getAmount(), chargeQuery.getAmount());
        assertEquals(chargeCommand.getDescription(), chargeQuery.getDescription());
        //Verify payment
        assertEquals(paymentCommand.getAmount(), paymentQuery.getAmount());
        assertEquals(paymentCommand.getDescription(), paymentQuery.getDescription());
        //Verify credit memo
        assertEquals(memoCommand.getAmount(), memoQuery.getAmount());
        assertEquals(memoCommand.getDescription(), memoQuery.getDescription());
    }

    @Test
    void testGetInvoiceByCustomerId() {
        //Setup
        InvoiceCommandDto command = addInvoice();
        TransactionCommandDto chargeCommand = command.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CHARGE)).findFirst().get();
        TransactionCommandDto paymentCommand = command.getTransactions().stream().filter(t->t.getType().equals(TransactionType.PAYMENT)).findFirst().get();
        TransactionCommandDto memoCommand = command.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CREDIT_MEMO)).findFirst().get();
        //Execution
        List<InvoiceQueryDto> queryList = invoiceQueryHandler.getInvoiceByCustomerId(command.getCustomerId());
        //Verification
        InvoiceQueryDto query = queryList.stream().filter(i->i.getInvoiceId().equals(command.getInvoiceId())).findFirst().get();
        assertEquals(command.getTotalAmount(), query.getTotalAmount());
        assertEquals(command.getCustomerId(), query.getCustomerId());
        assertEquals(command.getBalance(), query.getBalance());
        assertEquals(command.getStatus(), query.getStatus());
        assertEquals(command.getDueDate(), query.getDueDate());
        assertEquals(command.getCreatedAt(), query.getCreatedAt());
        assertEquals(command.getInvoiceId(), query.getInvoiceId());
        assertEquals(command.getTransactions().size(), query.getTransactions().size());//Charge, payment, and Memo

        TransactionQueryDto chargeQuery = query.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CHARGE)).findFirst().get();
        TransactionQueryDto paymentQuery = query.getTransactions().stream().filter(t->t.getType().equals(TransactionType.PAYMENT)).findFirst().get();
        TransactionQueryDto memoQuery = query.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CREDIT_MEMO)).findFirst().get();
        //Verify charge
        assertEquals(chargeCommand.getAmount(), chargeQuery.getAmount());
        assertEquals(chargeCommand.getDescription(), chargeQuery.getDescription());
        //Verify payment
        assertEquals(paymentCommand.getAmount(), paymentQuery.getAmount());
        assertEquals(paymentCommand.getDescription(), paymentQuery.getDescription());
        //Verify credit memo
        assertEquals(memoCommand.getAmount(), memoQuery.getAmount());
        assertEquals(memoCommand.getDescription(), memoQuery.getDescription());
    }

    @Test
    void testOutstandingInvoice() {
        //Setup
        InvoiceCommandDto command = addInvoice();
        //Execution
        List<InvoiceQueryDto> queryList = invoiceQueryHandler.getOutstandingInvoices();
        //Verification
        InvoiceQueryDto query = queryList.stream().filter(i->i.getInvoiceId().equals(command.getInvoiceId())).findFirst().get();
        assertEquals(command.getTotalAmount(), query.getTotalAmount());
        assertEquals(command.getCustomerId(), query.getCustomerId());
        assertEquals(command.getBalance(), query.getBalance());
        assertEquals(command.getStatus(), query.getStatus());
        assertEquals(command.getDueDate(), query.getDueDate());
        assertEquals(command.getCreatedAt(), query.getCreatedAt());
        assertEquals(command.getInvoiceId(), query.getInvoiceId());
        assertEquals(command.getTransactions().size(), query.getTransactions().size());//Charge, payment, and Memo
    }

    @Test
    void testInvoiceTransactions() {
        //Setup
        InvoiceCommandDto command = addInvoice();
        TransactionCommandDto chargeCommand = command.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CHARGE)).findFirst().get();
        TransactionCommandDto paymentCommand = command.getTransactions().stream().filter(t->t.getType().equals(TransactionType.PAYMENT)).findFirst().get();
        TransactionCommandDto memoCommand = command.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CREDIT_MEMO)).findFirst().get();
        //Execution
        List<TransactionQueryDto> queryList = invoiceQueryHandler.getInvoiceTransactions(command.getInvoiceId());
        //Verification
        TransactionQueryDto chargeQuery = queryList.stream().filter(t->t.getType().equals(TransactionType.CHARGE)).findFirst().get();
        TransactionQueryDto paymentQuery = queryList.stream().filter(t->t.getType().equals(TransactionType.PAYMENT)).findFirst().get();
        TransactionQueryDto memoQuery = queryList.stream().filter(t->t.getType().equals(TransactionType.CREDIT_MEMO)).findFirst().get();
        //Verify charge
        assertEquals(chargeCommand.getAmount(), chargeQuery.getAmount());
        assertEquals(chargeCommand.getDescription(), chargeQuery.getDescription());
        //Verify payment
        assertEquals(paymentCommand.getAmount(), paymentQuery.getAmount());
        assertEquals(paymentCommand.getDescription(), paymentQuery.getDescription());
        //Verify credit memo
        assertEquals(memoCommand.getAmount(), memoQuery.getAmount());
        assertEquals(memoCommand.getDescription(), memoQuery.getDescription());
    }

    private InvoiceCommandDto addInvoice(){
        //Invoice
        CreateInvoiceCommand request = getRequestPayload();
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        //Charge
        AddChargeCommand chargeRequest = new AddChargeCommand();
        chargeRequest.setInvoiceId(invoice.getInvoiceId());
        chargeRequest.setChargeAmount(BigDecimal.valueOf(50));
        chargeRequest.setDescription("Charge Description");
        //Payment
        ApplyPaymentCommand paymentRequest = new ApplyPaymentCommand();
        paymentRequest.setInvoiceId(invoice.getInvoiceId());
        paymentRequest.setPaymentAmount(BigDecimal.valueOf(100));
        paymentRequest.setDescription("Payment Description");
        //Credit Memo
        ApplyCreditMemoCommand creditMemoRequest = new ApplyCreditMemoCommand();
        creditMemoRequest.setInvoiceId(invoice.getInvoiceId());
        creditMemoRequest.setCreditAmount(BigDecimal.valueOf(50));
        creditMemoRequest.setReason("Memo Description");
        //
        invoiceCommandHandler.addCharge(chargeRequest);
        invoiceCommandHandler.finalizeInvoice(invoice.getInvoiceId());
        invoiceCommandHandler.applyPayment(paymentRequest);
        InvoiceCommandDto response = invoiceCommandHandler.applyCreditMemo(creditMemoRequest);
        return response;
    }

    private CreateInvoiceCommand getRequestPayload(){
        CreateInvoiceCommand createInvoiceCommand = new CreateInvoiceCommand();
        createInvoiceCommand.setCustomerId("C-124");
        createInvoiceCommand.setTotalAmount(BigDecimal.valueOf(500));
        createInvoiceCommand.setDueDate(LocalDate.of(2025,6,25));
        return createInvoiceCommand;
    }

}
