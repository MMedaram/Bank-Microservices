package com.bank.account.service;

import com.bank.account.client.CustomerClient;
import com.bank.account.client.NotificationClient;
import com.bank.account.entity.Account;
import com.bank.account.entity.CustomerDto;
import com.bank.account.entity.EmailRequestDto;
import com.bank.account.entity.Transaction;
import com.bank.account.entity.TransactionType;
import com.bank.account.exception.AccountNotFoundException;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.bank.account.entity.TransactionType.TRANSFER;


@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private CustomerClient customerClient;

    @Transactional
    public Transaction createTransaction(String accountNumber, Double amount, TransactionType transactionType, String description) {
        // Find account by account number
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType(String.valueOf(transactionType));
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(description);

        // Update account balance based on the transaction type (credit, debit)
        if (transactionType == TransactionType.CREDIT) {
            account.setBalance(account.getBalance() + amount);
        } else if (transactionType == TransactionType.DEBIT) {
            if (account.getBalance() < amount) {
                throw new RuntimeException("Insufficient funds");
            }
            account.setBalance(account.getBalance() - amount);
        }
        EmailRequestDto requestDto = prepareTransactionEmailRequest(account,transaction);
        accountRepository.save(account);
        Transaction result = transactionRepository.save(transaction);
        notificationClient.sendEmail(requestDto);

        return result;
    }

    @Transactional
    public Transaction transferFunds(String sourceAccountNumber, String destinationAccountNumber, Double amount) {
        // Transfer logic
        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException(sourceAccountNumber));

        Account destinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException(destinationAccountNumber));

        if (sourceAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds in source account");
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Record the transaction
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(sourceAccountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionType(String.valueOf(TRANSFER));
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription("Transfer from Account " + sourceAccountNumber + " to Account " + destinationAccountNumber);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionByAccountNumber(String accountNumber) {

       return transactionRepository.findByAccountNumber(accountNumber);
    }


    public List<Transaction> getTransactionByAccountNumberAndDateRange(String accountNumber, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(23,59,59);

        return transactionRepository.getTransactionByAccountNumberAndDateRange(accountNumber,fromDateTime,toDateTime);
    }


    private EmailRequestDto prepareTransactionEmailRequest(Account account, Transaction transaction){

        EmailRequestDto emailRequestDto = new EmailRequestDto();

        emailRequestDto.setAccountNumber(account.getAccountNumber());
        emailRequestDto.setTransactionType(transaction.getTransactionType());
        emailRequestDto.setAvailableBalance(String.valueOf(account.getBalance()));
        emailRequestDto.setAmount(String.valueOf(transaction.getAmount()));

        CustomerDto dto = customerClient.getCustomerByCustomerNumber(account.getCustomerNumber());

        emailRequestDto.setTo(dto.getEmail());
        emailRequestDto.setCustomerName(dto.getName());
        return emailRequestDto;
    }
}