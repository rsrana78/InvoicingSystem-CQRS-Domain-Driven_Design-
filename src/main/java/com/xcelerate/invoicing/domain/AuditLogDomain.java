package com.xcelerate.invoicing.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditLogDomain {

    private Integer id;
    private Integer invoiceId;
    private String action;
    private String json;
    private LocalDateTime timestamp;

    public AuditLogDomain(Integer invoiceId, String action, String json) {
        this.invoiceId = invoiceId;
        this.action = action;
        this.json = json;
        this.timestamp = LocalDateTime.now();
    }
}
