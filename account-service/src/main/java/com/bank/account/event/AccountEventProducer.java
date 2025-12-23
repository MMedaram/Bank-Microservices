package com.bank.account.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountEventProducer {

    private final KafkaTemplate<String, AccountCreatedEvent> kafkaTemplate;

    public AccountEventProducer(KafkaTemplate<String, AccountCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAccountCreated(AccountCreatedEvent event) {

        kafkaTemplate.send("account.created.v1",
                event.getCustomerNumber(), // partition key
                event
        );

        log.info("Account created event sent successfully with ID: {}", event);

    }
}
