package com.bank.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "customer Number is required")
    private String customerNumber;  // Linked to the customer

    @NotBlank(message = "branch Code is required")
    private String branchCode; // Linked to Branch

    @NotNull(message = "Balance is required")
    @Min(value = 100, message = "Balance cannot be negative")
    private Double balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

}