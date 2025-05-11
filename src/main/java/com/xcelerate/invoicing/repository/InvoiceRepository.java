package com.xcelerate.invoicing.repository;

import com.xcelerate.invoicing.domain.Invoice;
import com.xcelerate.invoicing.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    List<Invoice> findAllByCustomerId(String customerId);
    List<Invoice> findAllByStatusIs(InvoiceStatus status);

    @Query("SELECT SUM(i.totalAmount), SUM(i.balance) FROM Invoice i")
    Object[] getRevenue();


}
