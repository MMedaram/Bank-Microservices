package com.bank.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String customerNumber;
    private String name;
    private String email;
    private String phone;
    private String branchCode;
}
