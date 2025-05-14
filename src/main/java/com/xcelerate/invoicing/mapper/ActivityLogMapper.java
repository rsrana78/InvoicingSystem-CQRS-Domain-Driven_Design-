package com.xcelerate.invoicing.mapper;

import com.xcelerate.invoicing.domain.AuditLogDomain;
import com.xcelerate.invoicing.entity.AuditLogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityLogMapper {
    AuditLogEntity domainToEntity(AuditLogDomain domain);
}
