package com.bank.account.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountEventProducer {

    private final KafkaTemplate<String, AccountCreatedEvent> kafkaTemplate;
    private OutboxEventPublisher outboxEventPublisher;

    public AccountEventProducer(KafkaTemplate<String, AccountCreatedEvent> kafkaTemplate, OutboxEventPublisher outboxEventPublisher) {
        this.kafkaTemplate = kafkaTemplate;
        this.outboxEventPublisher = outboxEventPublisher;
    }

    public void publishAccountCreated(AccountCreatedEvent event) {

        kafkaTemplate.send("account.created.v1",
                event.getCustomerNumber(), // partition key
                event
        );

        log.info("Account created event sent successfully with ID: {}", event);

    }


    @Scheduled(fixedDelay = 5000)
    public void publishOutbox() {
        outboxEventPublisher.publishPendingEvents();
    }
}
