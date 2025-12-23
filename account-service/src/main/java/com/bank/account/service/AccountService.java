package com.bank.account.service;

import com.bank.account.client.CustomerClient;
import com.bank.account.entity.Account;
import com.bank.account.entity.AccountDto;
import com.bank.account.entity.AccountType;
import com.bank.account.entity.CustomerDto;
import com.bank.account.event.AccountCreatedEvent;
import com.bank.account.event.AccountEventProducer;
import com.bank.account.exception.AccountNotFoundException;
import com.bank.account.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountEventProducer accountEventProducer;

    @Autowired
    private CustomerClient customerClient;  // Link to Customer service

    public String generateAccountNumber(String branchCode, String customerNumber) {
        // Get the latest account number for the branch (for simplicity, query only the last account)
        String lastAccountNumber = accountRepository.findTopByBranchCodeOrderByAccountNumberDesc(branchCode).getAccountNumber();

        if (null != lastAccountNumber) {
            // Extract the number part from last account number, assuming it's in the format customerNumber + "xxxx"
            String numberPart = lastAccountNumber.substring(customerNumber.length());
            int incrementedNumber = Integer.parseInt(numberPart) + 1;

            // Format it back to have leading zeros (e.g., customerNumber + "0001")
            return customerNumber + String.format("%04d", incrementedNumber);
        } else {
            // If no account exists for this branch, start with the first account number
            return customerNumber + "0001"; // Default to first account number
        }
    }

    public Account createAccount(AccountDto accountDto) {
        String customerNumber = accountDto.getCustomerNumber();
        log.info("Creating account for customer : {}", customerNumber);

        CustomerDto customerDto = customerClient.getCustomerByCustomerNumber(customerNumber);
        if (customerDto == null) {
            log.error("Customer not found with customerNumber : {}", customerNumber);
            throw new EntityNotFoundException("Customer not found with customerNumber: " + customerNumber);
        }

        String accountNumber = generateAccountNumber(accountDto.getBranchCode(), accountDto.getCustomerNumber());

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(accountDto.getBalance());
        account.setBranchCode(accountDto.getBranchCode());
        account.setCustomerNumber(accountDto.getCustomerNumber());
        account.setAccountType(AccountType.valueOf(accountDto.getAccountType()));

        Account saved = accountRepository.save(account);

        AccountCreatedEvent event = new AccountCreatedEvent();
        event.setAccountNumber(saved.getAccountNumber());
        event.setCustomerNumber(saved.getCustomerNumber());
        event.setCustomerMail(customerDto.getEmail());
        event.setCustomerName(customerDto.getName());
        event.setAccountType(saved.getAccountType().name());
        event.setBranchCode(saved.getBranchCode());
        event.setBalance(saved.getBalance());

        accountEventProducer.publishAccountCreated(event);

        log.info("Account created successfully with ID: {}", saved.getId());
        return saved;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setAccountNumber(updatedAccount.getAccountNumber());
                    account.setBalance(updatedAccount.getBalance());
                    account.setAccountType(updatedAccount.getAccountType());
                    return accountRepository.save(account);
                }).orElseThrow(() -> new RuntimeException("Account not found with ID: " + id));
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    /**
     * Get the current balance of an account
     *
     * @param accountNumber Account Number
     * @return Current balance
     */
    public double getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(Account::getBalance)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    public List<Account> getAllAccountsByCustomerNumber(String customerNumber) {
        return accountRepository.findAllByCustomerNumber(customerNumber);
    }
}