package com.bank.notificationservice.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_in_log")
@Data
public class EventInLog {

    @Id
    private String eventId;

    private String eventType;
    private String sourceService;
    private String topicName;

    private LocalDateTime receivedAt;
    private LocalDateTime processedAt;


}

