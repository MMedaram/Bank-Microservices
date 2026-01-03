package com.bank.notificationservice.event;

import com.bank.notificationservice.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class NotificationConsumer {

    @Autowired
    private EmailService emailService;


    @KafkaListener(
            topics = "customer.created.v1",
            containerFactory = "customerCreatedKafkaListenerContainerFactory"
            )
    public void onCustomerCreated(CustomerCreatedEvent event) {
        System.out.println("📧 Sending CustomerCreatedEvent email to: " + event.getEmail());
        emailService.sendCustomerOnBordEmail(event.getEmail());
    }


    @KafkaListener(
            topics = "account.created.v1",
            containerFactory = "accountCreatedKafkaListenerContainerFactory"
    )
    public void handleAccountCreated(AccountCreatedEvent event) {

        Map<String, Object> templateModel = Map.of(
                "customerName", event.getCustomerName(),
                "customerNumber", event.getCustomerNumber(),
                "accountNumber", event.getAccountNumber(),
                "accountType",event.getAccountType(),
                "branchName",event.getBranchCode(),
                "balance",event.getBalance()
        );

        emailService.sendAccountCreationEmail(event);

        log.info( "📧 Account created: " + event.getAccountNumber() +
                        " for customer " + event.getCustomerNumber()
        );

    }

}
