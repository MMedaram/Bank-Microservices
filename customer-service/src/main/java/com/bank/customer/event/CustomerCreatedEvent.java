package com.bank.customer.event;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CustomerCreatedEvent {
    private String customerNumber;
    private String name;
    private String email;
    private String phone;
    private String branchCode;
}
