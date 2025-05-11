package com.xcelerate.invoicing.service;

import com.xcelerate.invoicing.domain.AuditLog;
import com.xcelerate.invoicing.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityLoggingService {

    private final AuditLogRepository auditLogRepository;

    public ActivityLoggingService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logActivity(Integer invoiceId, String action, String json){
        AuditLog auditLog = new AuditLog(invoiceId, action, json);
        auditLogRepository.saveAndFlush(auditLog);
    }
}
