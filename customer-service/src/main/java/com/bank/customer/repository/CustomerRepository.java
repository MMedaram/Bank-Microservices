package com.bank.customer.repository;

import com.bank.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerNumber(String customerNumber);

    @Query("SELECT c.customerNumber FROM Customer c WHERE c.branchCode = :branchCode ORDER BY c.customerNumber DESC")
    String findLastCustomerNumberByBranchCode(@Param("branchCode") String branchCode);
}

