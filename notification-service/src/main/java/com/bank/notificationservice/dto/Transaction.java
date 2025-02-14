package com.bank.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private String accountNumber;
    private String customerName;
    private String transactionType;
    private double transactionAmount;
    private Instant transactionDate;


}
