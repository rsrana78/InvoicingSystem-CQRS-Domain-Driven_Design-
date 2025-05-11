package com.xcelerate.invoicing.repository;

import com.xcelerate.invoicing.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
}
