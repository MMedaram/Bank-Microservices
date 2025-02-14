package com.bank.account.controller;

import com.bank.account.entity.Account;
import com.bank.account.entity.AccountDto;
import com.bank.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account API", description = "Endpoints for managing accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDto account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @GetMapping
    @Operation(summary = "Get all accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/customer/{customerNumber}")
    @Operation(summary = "Get all accounts by customer")
    public ResponseEntity<List<Account>> getAllAccountsByCustomerNumber(@PathVariable String customerNumber) {
        return ResponseEntity.ok(accountService.getAllAccountsByCustomerNumber(customerNumber));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account details")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody Account account) {
        return ResponseEntity.ok(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get the balance for an account
     *
     * @param accountNumber Account number for balance enquiry
     * @return Current balance
     */
    @Operation(summary = "Get balance for an account",
            description = "This endpoint allows you to check the current balance of a specific account.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Balance retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(type = "number", format = "double"))),
                    @ApiResponse(responseCode = "404",
                            description = "Account not found",
                            content = @Content)
            })
    @GetMapping("/balance/{accountNumber}")
    public double getBalance(@PathVariable String accountNumber) {
        return accountService.getBalance(accountNumber);
    }

}