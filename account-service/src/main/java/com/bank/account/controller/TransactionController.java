package com.bank.account.controller;

import com.bank.account.entity.Transaction;
import com.bank.account.entity.TransactionRequest;
import com.bank.account.entity.TransferRequest;
import com.bank.account.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction API", description = "Endpoints for managing Transactions like Credit , Debit,Transfer")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Create a transaction (credit, debit)
     *
     * @param request TransactionRequest
     * @return Transaction
     */
    @Operation(summary = "Create a transaction",
            description = "This endpoint allows you to create a new transaction (credit/debit) for an account.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transaction details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Transaction created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid request body",
                            content = @Content)
            })
    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionRequest request) {
        return transactionService.createTransaction(request.getAccountNumber(), request.getAmount(), request.getTransactionType(), request.getDescription());
    }

    /**
     * Transfer funds between two accounts
     *
     * @param request TransferRequest
     * @return Transaction
     */
    @Operation(summary = "Transfer funds",
            description = "This endpoint allows you to transfer funds between two accounts.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Transfer request details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransferRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Funds transferred successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid request body",
                            content = @Content),
                    @ApiResponse(responseCode = "404",
                            description = "Account not found",
                            content = @Content)
            })
    @PostMapping("/transfer")
    public Transaction transferFunds(@RequestBody TransferRequest request) {
        return transactionService.transferFunds(request.getSourceAccountNumber(), request.getDestinationAccountNumber(), request.getAmount());
    }

    /**
     * Get transaction by account number
     *
     * @param accountNumber Account number
     * @return Transactions
     */
    @Operation(summary = "Get transactions by account number",
            description = "This endpoint allows you to retrieve the transaction details for a specific account.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Transaction found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Transaction.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Transaction not found",
                            content = @Content)
            })
    @GetMapping("/{accountNumber}")
    public List<Transaction> getTransactionByAccountNumber(@PathVariable String accountNumber) {
        return transactionService.getTransactionByAccountNumber(accountNumber);
    }


    /**
     * Get transactions list by account number, fromDate, toDate
     *
     * @param accountNumber , fromDate , toDate
     * @return Transactions
     */
    @Operation(summary = "Get transactions by account number and date range")
    @GetMapping("/{accountNumber}/{fromDate}/{toDate}")
    public List<Transaction> getTransactionByAccountNumberAndDateRange(@PathVariable String accountNumber,
                                                                       @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                       @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        return transactionService.getTransactionByAccountNumberAndDateRange(accountNumber,fromDate,toDate);
    }

}
