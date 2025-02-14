package com.bank.account.repository;

import com.bank.account.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountNumber(String  accountNumber);

    @Query("SELECT t.transactionType, t.transactionDate, t.amount, t.accountNumber FROM Transaction t WHERE t.accountNumber = :accountNumber AND t.transactionDate >= :fromDate AND t.transactionDate < :toDate")
    List<Transaction> getTransactionByAccountNumberAndDateRange(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate);
}
