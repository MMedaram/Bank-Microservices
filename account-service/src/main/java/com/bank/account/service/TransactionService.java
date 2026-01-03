package com.bank.account.service;

import com.bank.account.client.CustomerClient;
import com.bank.account.client.NotificationClient;
import com.bank.account.entity.Account;
import com.bank.account.entity.CustomerDto;
import com.bank.account.entity.EmailRequestDto;
import com.bank.account.entity.EventOutLog;
import com.bank.account.entity.Transaction;
import com.bank.account.entity.TransactionType;
import com.bank.account.event.AccountTransactionCompletedEvent;
import com.bank.account.exception.AccountNotFoundException;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.EventOutLogRepository;
import com.bank.account.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private EventOutLogRepository eventOutLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
        accountRepository.save(account);

        //EmailRequestDto requestDto = prepareTransactionEmailRequest(account,transaction);
        //notificationClient.sendEmail(requestDto);

        Transaction savedTransaction = transactionRepository.save(transaction);

        // 🔹 Build event
        AccountTransactionCompletedEvent event = new AccountTransactionCompletedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setTransactionId(savedTransaction.getId().toString());
        event.setTransactionType(transactionType.name());
        event.setSourceAccountNumber(accountNumber);
        event.setAmount(amount);
        event.setSourceBalanceAfter(account.getBalance());
        event.setTransactionDate(savedTransaction.getTransactionDate());
        event.setDescription(description);

        // 🔹 Insert OUT log (same DB transaction)
        EventOutLog outLog = new EventOutLog();
        outLog.setEventId(event.getEventId());
        outLog.setEventType("TRANSACTION_COMPLETED");
        outLog.setAggregateType("ACCOUNT");
        outLog.setAggregateId(accountNumber);
        outLog.setTopicName("account.transaction.v1");
        outLog.setPayload(event);

        outLog.setStatus("NEW");
        outLog.setRetryCount(0);
        outLog.setCreatedAt(LocalDateTime.now());

        eventOutLogRepository.save(outLog);

        return savedTransaction;
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

        Transaction saved = transactionRepository.save(transaction);

        // 🔹 Build event
        AccountTransactionCompletedEvent event = new AccountTransactionCompletedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setTransactionId(saved.getId().toString());
        event.setTransactionType("TRANSFER");
        event.setSourceAccountNumber(sourceAccountNumber);
        event.setDestinationAccountNumber(destinationAccountNumber);
        event.setAmount(amount);
        event.setSourceBalanceAfter(sourceAccount.getBalance());
        event.setDestinationBalanceAfter(destinationAccount.getBalance());
        event.setTransactionDate(saved.getTransactionDate());
        event.setDescription(saved.getDescription());

        // 🔹 OUT log
        EventOutLog outLog = new EventOutLog();
        outLog.setEventId(event.getEventId());
        outLog.setEventType("TRANSACTION_COMPLETED");
        outLog.setAggregateType("TRANSACTION");
        outLog.setAggregateId(saved.getId().toString());
        outLog.setTopicName("account.transaction.v1");
        outLog.setPayload(event);
        outLog.setStatus("NEW");
        outLog.setRetryCount(0);
        outLog.setCreatedAt(LocalDateTime.now());

        eventOutLogRepository.save(outLog);

        return saved;
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