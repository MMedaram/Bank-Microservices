package com.bank.account.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @NotBlank(message = "customer Number is required")
    private String customerNumber;  // Linked to the customer

    @NotBlank(message = "branch Code is required")
    private String branchCode; // Linked to Branch

    @NotNull(message = "Balance is required")
    @Min(value = 100, message = "Balance cannot be negative")
    private Double balance;

    @NotNull(message = "Account Type is required")
    @Pattern(regexp = "SAVINGS|CURRENT|FIXED_DEPOSIT", message = "Invalid account type")
    private String accountType;

}
