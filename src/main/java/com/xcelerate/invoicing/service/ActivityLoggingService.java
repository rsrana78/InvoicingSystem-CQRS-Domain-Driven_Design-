package com.xcelerate.invoicing.service;

import com.xcelerate.invoicing.domain.AuditLogDomain;
import com.xcelerate.invoicing.mapper.ActivityLogMapper;
import com.xcelerate.invoicing.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityLoggingService {

    private final AuditLogRepository auditLogRepository;
    private final ActivityLogMapper activityLogMapper;

    public ActivityLoggingService(AuditLogRepository auditLogRepository,
                                  ActivityLogMapper activityLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.activityLogMapper = activityLogMapper;
    }

    public void logActivity(Integer invoiceId, String action, String json){
        AuditLogDomain auditLog = new AuditLogDomain(invoiceId, action, json);
        auditLogRepository.saveAndFlush(activityLogMapper.domainToEntity(auditLog));
    }
}
