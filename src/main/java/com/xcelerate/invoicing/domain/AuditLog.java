package com.xcelerate.invoicing.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog implements Serializable {

    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;

    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "action")
    private String action;

    @Column(name = "request_json", length = 5000)
    private String json;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public AuditLog(){

    }

    public AuditLog(Integer invoiceId, String action, String json) {
        this.invoiceId = invoiceId;
        this.action = action;
        this.json = json;
        this.timestamp = LocalDateTime.now();
    }

    public Integer getLogId() {
        return logId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
