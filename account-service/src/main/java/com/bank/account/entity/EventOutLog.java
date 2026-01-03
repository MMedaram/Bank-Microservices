package com.bank.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_out_log")
@Data
public class EventOutLog {

    @Id
    private String eventId;

    private String eventType;
    private String aggregateType;
    private String aggregateId;
    private String topicName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Object payload;

    private String status;
    private Integer retryCount;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
}