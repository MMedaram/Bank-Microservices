package com.bank.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String accountNumber;
    private String customerNumber;
    private String branchCode;
    private Double balance;
    private String accountType;

}