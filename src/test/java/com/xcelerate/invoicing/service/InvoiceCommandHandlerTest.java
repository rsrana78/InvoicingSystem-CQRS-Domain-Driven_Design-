package com.xcelerate.invoicing.service;

import com.xcelerate.invoicing.dto.command.request.AddChargeCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyCreditMemoCommand;
import com.xcelerate.invoicing.dto.command.request.ApplyPaymentCommand;
import com.xcelerate.invoicing.dto.command.request.CreateInvoiceCommand;
import com.xcelerate.invoicing.dto.command.response.InvoiceCommandDto;
import com.xcelerate.invoicing.dto.command.response.TransactionCommandDto;
import com.xcelerate.invoicing.enums.InvoiceStatus;
import com.xcelerate.invoicing.enums.TransactionType;
import com.xcelerate.invoicing.exception.InvalidActionException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class InvoiceCommandHandlerTest {

    @Autowired
    private InvoiceCommandHandler invoiceCommandHandler;

    @Test
    void testCreateInvoice() {
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        //Execution
        InvoiceCommandDto response = invoiceCommandHandler.createInvoice(request);
        //Verification
        assertEquals(request.getCustomerId(), response.getCustomerId());
        assertEquals(InvoiceStatus.DRAFT, response.getStatus());
        assertEquals(request.getDueDate(), response.getDueDate());
        assertEquals(request.getTotalAmount(), response.getTotalAmount());
        assertEquals(request.getTotalAmount(), response.getBalance());
    }

    @Test
    void testCreateInvoiceWithChargeAmount() {
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        AddChargeCommand chargeRequest = new AddChargeCommand();
        chargeRequest.setChargeAmount(BigDecimal.valueOf(50));
        chargeRequest.setDescription("Printing charges");
        request.setAddChargeCommand(chargeRequest);
        //Execution
        InvoiceCommandDto response = invoiceCommandHandler.createInvoice(request);
        //Verification
        assertEquals(request.getCustomerId(), response.getCustomerId());
        assertEquals(InvoiceStatus.DRAFT, response.getStatus());
        assertEquals(request.getDueDate(), response.getDueDate());
        assertEquals(request.getTotalAmount().add(chargeRequest.getChargeAmount()), response.getTotalAmount());
        assertEquals(request.getTotalAmount().add(chargeRequest.getChargeAmount()), response.getBalance());
        assertEquals(1, response.getTransactions().size());
        assertEquals(chargeRequest.getChargeAmount(), response.getTransactions().get(0).getAmount());
        assertEquals(chargeRequest.getDescription(), response.getTransactions().get(0).getDescription());
    }

    @Test
    void testAddCharge() {
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        AddChargeCommand chargeRequest = new AddChargeCommand();
        chargeRequest.setInvoiceId(invoice.getId());
        chargeRequest.setChargeAmount(BigDecimal.valueOf(50));
        chargeRequest.setDescription("Printing charges");
        //Execution
        InvoiceCommandDto response = invoiceCommandHandler.addCharge(chargeRequest);
        //Verification
        assertEquals(request.getCustomerId(), response.getCustomerId());
        assertEquals(InvoiceStatus.DRAFT, response.getStatus());
        assertEquals(request.getDueDate(), response.getDueDate());
        assertEquals(request.getTotalAmount().add(chargeRequest.getChargeAmount()), response.getTotalAmount());
        assertEquals(request.getTotalAmount().add(chargeRequest.getChargeAmount()), response.getBalance());
        assertEquals(1, response.getTransactions().size());
        assertEquals(chargeRequest.getChargeAmount(), response.getTransactions().get(0).getAmount());
        assertEquals(chargeRequest.getDescription(), response.getTransactions().get(0).getDescription());
    }

    @Test
    void testApplyPayment() {
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        ApplyPaymentCommand paymentRequest = new ApplyPaymentCommand();
        paymentRequest.setInvoiceId(invoice.getId());
        paymentRequest.setPaymentAmount(BigDecimal.valueOf(100));
        paymentRequest.setDescription("Online Payment");
        AddChargeCommand chargeRequest = new AddChargeCommand();
        chargeRequest.setInvoiceId(invoice.getId());
        chargeRequest.setChargeAmount(BigDecimal.valueOf(50));
        chargeRequest.setDescription("Printing charges");
        invoiceCommandHandler.addCharge(chargeRequest);
        invoiceCommandHandler.finalizeInvoice(invoice.getId());
        //Execution
        InvoiceCommandDto response = invoiceCommandHandler.applyPayment(paymentRequest);
        //Verification
        assertEquals(request.getCustomerId(), response.getCustomerId());
        assertEquals(InvoiceStatus.ISSUED, response.getStatus());
        assertEquals(request.getDueDate(), response.getDueDate());
        assertEquals(request.getTotalAmount().add(chargeRequest.getChargeAmount()), response.getTotalAmount());
        BigDecimal balance = request.getTotalAmount().add(chargeRequest.getChargeAmount()).subtract(paymentRequest.getPaymentAmount());
        assertEquals(balance, response.getBalance());
        assertEquals(2, response.getTransactions().size());//Charge and payment
        TransactionCommandDto payment = response.getTransactions().stream().filter(t->t.getType().equals(TransactionType.PAYMENT)).findFirst().get();
        assertEquals(paymentRequest.getPaymentAmount(), payment.getAmount());
        assertEquals(paymentRequest.getDescription(), payment.getDescription());
    }

    @Test
    void testApplyCreditMemo() {
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        AddChargeCommand chargeRequest = new AddChargeCommand();
        chargeRequest.setInvoiceId(invoice.getId());
        chargeRequest.setChargeAmount(BigDecimal.valueOf(50));
        chargeRequest.setDescription("Printing charges");
        ApplyPaymentCommand paymentRequest = new ApplyPaymentCommand();
        paymentRequest.setInvoiceId(invoice.getId());
        paymentRequest.setPaymentAmount(BigDecimal.valueOf(500));
        paymentRequest.setDescription("Online Payment");
        invoiceCommandHandler.addCharge(chargeRequest);
        invoiceCommandHandler.finalizeInvoice(invoice.getId());
        invoiceCommandHandler.applyPayment(paymentRequest);
        ApplyCreditMemoCommand creditMemoRequest = new ApplyCreditMemoCommand();
        creditMemoRequest.setInvoiceId(invoice.getId());
        creditMemoRequest.setCreditAmount(BigDecimal.valueOf(50));
        creditMemoRequest.setReason("Test memo");
        //Execution
        InvoiceCommandDto response = invoiceCommandHandler.applyCreditMemo(creditMemoRequest);
        //Verification
        assertEquals(request.getCustomerId(), response.getCustomerId());
        assertEquals(InvoiceStatus.PAID, response.getStatus());
        assertEquals(request.getDueDate(), response.getDueDate());
        assertEquals(request.getTotalAmount().add(chargeRequest.getChargeAmount()), response.getTotalAmount());
        assertEquals(0, response.getBalance().compareTo(BigDecimal.valueOf(0.0)));
        assertEquals(3, response.getTransactions().size());//Charge, payment, and Memo

        TransactionCommandDto charge = response.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CHARGE)).findFirst().get();
        TransactionCommandDto payment = response.getTransactions().stream().filter(t->t.getType().equals(TransactionType.PAYMENT)).findFirst().get();
        TransactionCommandDto memo = response.getTransactions().stream().filter(t->t.getType().equals(TransactionType.CREDIT_MEMO)).findFirst().get();
        //Verify charge
        assertEquals(chargeRequest.getChargeAmount(), charge.getAmount());
        assertEquals(chargeRequest.getDescription(), charge.getDescription());
        //Verify payment
        assertEquals(paymentRequest.getPaymentAmount(), payment.getAmount());
        assertEquals(paymentRequest.getDescription(), payment.getDescription());
        //Verify credit memo
        assertEquals(creditMemoRequest.getCreditAmount(), memo.getAmount());
        assertEquals(creditMemoRequest.getReason(), memo.getDescription());
    }

    @Test
    public void testCancelInvoice(){
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        //Execution
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        InvoiceCommandDto response = invoiceCommandHandler.cancelInvoice(invoice.getId());
        //Verification
        assertEquals(request.getCustomerId(), response.getCustomerId());
        assertEquals(InvoiceStatus.CANCELLED, response.getStatus());
        assertEquals(request.getDueDate(), response.getDueDate());
        assertEquals(request.getTotalAmount(), response.getTotalAmount());
        assertEquals(request.getTotalAmount(), response.getBalance());
    }

    @Test
    public void testCancelAfterPaid(){
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        AddChargeCommand chargeRequest = new AddChargeCommand();
        chargeRequest.setInvoiceId(invoice.getId());
        chargeRequest.setChargeAmount(BigDecimal.valueOf(50));
        chargeRequest.setDescription("Printing charges");
        invoiceCommandHandler.addCharge(chargeRequest);
        invoiceCommandHandler.finalizeInvoice(invoice.getId());
        ApplyPaymentCommand paymentRequest = new ApplyPaymentCommand();
        paymentRequest.setInvoiceId(invoice.getId());
        paymentRequest.setPaymentAmount(BigDecimal.valueOf(550));
        paymentRequest.setDescription("Full Payment");
        invoiceCommandHandler.applyPayment(paymentRequest);
        //Execution
        Exception exception = assertThrows(InvalidActionException.class, () -> invoiceCommandHandler.cancelInvoice(invoice.getId()));
        //Verification
        assertEquals("Paid invoices cannot be cancelled", exception.getMessage());
    }

    @Test
    void testAddChargeAfterFinalize() {
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        invoiceCommandHandler.finalizeInvoice(invoice.getId());
        AddChargeCommand chargeRequest = new AddChargeCommand();
        chargeRequest.setInvoiceId(invoice.getId());
        chargeRequest.setChargeAmount(BigDecimal.valueOf(50));
        chargeRequest.setDescription("Test charges");
        //Execution
        Exception exception = assertThrows(InvalidActionException.class, () -> invoiceCommandHandler.addCharge(chargeRequest));
        //Verification
        assertEquals("Charges can only be added to DRAFT invoices", exception.getMessage());
    }

    @Test
    public void testOverPayment(){
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        AddChargeCommand chargeRequest = new AddChargeCommand();
        chargeRequest.setInvoiceId(invoice.getId());
        chargeRequest.setChargeAmount(BigDecimal.valueOf(50));
        chargeRequest.setDescription("Printing charges");
        invoiceCommandHandler.addCharge(chargeRequest);
        invoiceCommandHandler.finalizeInvoice(invoice.getId());
        ApplyPaymentCommand paymentRequest = new ApplyPaymentCommand();
        paymentRequest.setInvoiceId(invoice.getId());
        paymentRequest.setPaymentAmount(BigDecimal.valueOf(600));//Balance=550
        paymentRequest.setDescription("Full Payment");
        //Execution
        Exception exception = assertThrows(InvalidActionException.class, () -> invoiceCommandHandler.applyPayment(paymentRequest));
        //Verification
        assertEquals("Payment amount can not exceed the remaining balance", exception.getMessage());
    }

    @Test
    public void testInvalidCreditMemo(){
        //Setup
        CreateInvoiceCommand request = getRequestPayload();
        InvoiceCommandDto invoice = invoiceCommandHandler.createInvoice(request);
        AddChargeCommand chargeRequest = new AddChargeCommand();
        chargeRequest.setInvoiceId(invoice.getId());
        chargeRequest.setChargeAmount(BigDecimal.valueOf(50));
        chargeRequest.setDescription("Printing charges");
        invoiceCommandHandler.addCharge(chargeRequest);
        invoiceCommandHandler.finalizeInvoice(invoice.getId());
        ApplyPaymentCommand paymentRequest = new ApplyPaymentCommand();
        paymentRequest.setInvoiceId(invoice.getId());
        paymentRequest.setPaymentAmount(BigDecimal.valueOf(500));
        paymentRequest.setDescription("Full Payment");
        invoiceCommandHandler.applyPayment(paymentRequest);
        ApplyCreditMemoCommand creditMemoRequest = new ApplyCreditMemoCommand();
        creditMemoRequest.setInvoiceId(invoice.getId());
        creditMemoRequest.setCreditAmount(BigDecimal.valueOf(100));//Balance=50
        creditMemoRequest.setReason("Test memo");
        //Execution
        Exception exception = assertThrows(InvalidActionException.class, () -> invoiceCommandHandler.applyCreditMemo(creditMemoRequest));
        //Verification
        assertEquals("Credit memo amount can not exceed the remaining balance", exception.getMessage());
    }


    private CreateInvoiceCommand getRequestPayload(){
        CreateInvoiceCommand createInvoiceCommand = new CreateInvoiceCommand();
        createInvoiceCommand.setCustomerId("C-123");
        createInvoiceCommand.setTotalAmount(BigDecimal.valueOf(500));
        createInvoiceCommand.setDueDate(LocalDate.of(2025,6,25));
        return createInvoiceCommand;
    }

}
