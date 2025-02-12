package com.bank.account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private Double amount;
}
