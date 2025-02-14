package com.bank.account.repository;

import com.bank.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("SELECT a.accountNumber FROM Account a WHERE a.branchCode = :branchCode ORDER BY a.accountNumber DESC")
    String findTopByBranchCodeOrderByAccountNumberDesc(String branchCode);

    @Query("SELECT a FROM Account a WHERE a.customerNumber = :customerNumber")
    List<Account> findAllByCustomerNumber(String customerNumber);
}

