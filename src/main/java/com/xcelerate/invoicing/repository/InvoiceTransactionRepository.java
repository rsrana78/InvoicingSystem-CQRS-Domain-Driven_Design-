package com.xcelerate.invoicing.repository;

import com.xcelerate.invoicing.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceTransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByInvoice_invoiceId(Integer invoiceId);

}
