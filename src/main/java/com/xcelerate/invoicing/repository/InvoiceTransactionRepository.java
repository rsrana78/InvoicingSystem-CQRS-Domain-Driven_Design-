package com.xcelerate.invoicing.repository;

import com.xcelerate.invoicing.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceTransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    List<TransactionEntity> findAllByInvoice_id(Integer invoiceId);

}
