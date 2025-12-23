package com.bank.customer.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class CustomerEventProducer {

    private final KafkaTemplate<String, CustomerCreatedEvent> kafkaTemplate;

    public CustomerEventProducer(KafkaTemplate<String, CustomerCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCustomerCreated(CustomerCreatedEvent event) {
        kafkaTemplate.send(
                "customer.created.v1",
                event.getCustomerNumber(),
                event
        );

        System.out.println("===================CustomerCreatedEvent==========="+event);
    }
}
