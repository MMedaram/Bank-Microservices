package com.bank.account.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto {

        private String to;
        private String amount;
        private String availableBalance;
        private String customerName;
        private String accountNumber;
        private String transactionType;

}
