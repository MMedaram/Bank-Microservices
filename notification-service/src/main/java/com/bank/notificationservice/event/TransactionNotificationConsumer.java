package com.bank.notificationservice.event;

import com.bank.notificationservice.dto.EventInLog;
import com.bank.notificationservice.repository.EventInLogRepository;
import com.bank.notificationservice.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class TransactionNotificationConsumer {

    private final EventInLogRepository eventInLogRepository;
    private final EmailService emailService;

    public TransactionNotificationConsumer(EventInLogRepository eventInLogRepository, EmailService emailService) {
        this.eventInLogRepository = eventInLogRepository;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "account.transaction.v1",
            groupId = "notification-service",
    containerFactory = "accountTransactionKafkaListenerContainerFactory" )
    public void handleTransaction(AccountTransactionCompletedEvent event) {

        log.info("EVENT CLASS = {}", event.getClass());


        // 1️⃣ Idempotency check
        if (eventInLogRepository.existsById(event.getEventId())) {
            return; // duplicate → ignore
        }

        // 2️⃣ Business logic
        emailService.sendTransactionNotificationEmail(event);

        // 3️⃣ Insert IN log (AFTER success)
        EventInLog inLog = new EventInLog();
        inLog.setEventId(event.getEventId());
        inLog.setEventType("TRANSACTION_COMPLETED");
        inLog.setSourceService("account-service");
        inLog.setTopicName("account.transaction.v1");
        inLog.setReceivedAt(LocalDateTime.now());
        inLog.setProcessedAt(LocalDateTime.now());

        eventInLogRepository.save(inLog);
    }



}