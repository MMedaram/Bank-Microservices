package com.bank.account.event;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class AccountTransactionCompletedEvent {
    private String eventId;              // UUID
    private String transactionId;

    private String transactionType;      // CREDIT / DEBIT / TRANSFER

    private String sourceAccountNumber;
    private String destinationAccountNumber; // null for credit/debit

    private Double amount;
    private Double sourceBalanceAfter;
    private Double destinationBalanceAfter;  // null for credit/debit

    private LocalDateTime transactionDate;
    private String description;
}
