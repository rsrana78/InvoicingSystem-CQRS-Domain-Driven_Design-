package com.xcelerate.invoicing.repository;

import com.xcelerate.invoicing.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Integer> {
}
