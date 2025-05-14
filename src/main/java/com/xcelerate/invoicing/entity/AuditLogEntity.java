package com.xcelerate.invoicing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "audit_logs")
public class AuditLogEntity extends BaseEntity<Integer> implements Serializable {

    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "action")
    private String action;

    @Column(name = "request_json", length = 5000)
    private String json;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public AuditLogEntity(Integer invoiceId, String action, String json) {
        this.invoiceId = invoiceId;
        this.action = action;
        this.json = json;
        this.timestamp = LocalDateTime.now();
    }
}
