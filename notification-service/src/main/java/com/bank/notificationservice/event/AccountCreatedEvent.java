package com.bank.notificationservice.event;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AccountCreatedEvent {
    private String accountNumber;
    private String customerNumber;
    private String customerName;
    private String customerMail;
    private String accountType;
    private String branchCode;
    private Double balance;
}
