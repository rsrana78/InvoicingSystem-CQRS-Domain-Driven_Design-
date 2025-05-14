package com.xcelerate.invoicing.repository;

import com.xcelerate.invoicing.entity.InvoiceEntity;
import com.xcelerate.invoicing.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

    List<InvoiceEntity> findAllByCustomerId(String customerId);
    List<InvoiceEntity> findAllByStatusIs(InvoiceStatus status);

    @Query("SELECT SUM(i.totalAmount), SUM(i.balance) FROM InvoiceEntity i")
    Object[] getRevenue();


}
