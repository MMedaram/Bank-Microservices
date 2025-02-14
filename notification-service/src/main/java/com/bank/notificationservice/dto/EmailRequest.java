package com.bank.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String to;
    private String amount;
    private String availableBalance;
    private String customerName;
    private String accountNumber;
    private String transactionType;
}