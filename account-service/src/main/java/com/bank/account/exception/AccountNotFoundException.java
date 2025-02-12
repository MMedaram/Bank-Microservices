package com.bank.account.exception;


public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountNumber) {
        super("Account with account number " + accountNumber + " not found");
    }
}
