package com.bank.notificationservice.client;

import com.bank.notificationservice.dto.Account;
import com.bank.notificationservice.dto.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "account-service")
public interface AccountClient {

    @GetMapping("/api/transactions/{accountNumber}/{fromDate}/{toDate}")
    List<Transaction> getTransactionByAccountNumberAndDateRange(@PathVariable String accountNumber, @PathVariable String fromDate, @PathVariable String toDate);

    @GetMapping("/api/accounts/customer/{customerNumber}")
    List<Account> getAllAccountsByCustomerNumber(@PathVariable String customerNumber);

}
