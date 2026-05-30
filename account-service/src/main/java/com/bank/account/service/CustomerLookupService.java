package com.bank.account.service;

import com.bank.account.client.CustomerClient;
import com.bank.account.entity.CustomerDto;
import com.bank.account.exception.CustomerServiceUnavailableException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerLookupService {

    private static final String CUSTOMER_SERVICE = "customerService";

    private final CustomerClient customerClient;

    public CustomerLookupService(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    @Retry(name = CUSTOMER_SERVICE)
    @CircuitBreaker(name = CUSTOMER_SERVICE, fallbackMethod = "customerLookupFallback")
    public CustomerDto getCustomerByCustomerNumber(String customerNumber) {
        try {
            return customerClient.getCustomerByCustomerNumber(customerNumber);
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException("Customer not found with customerNumber: " + customerNumber);
        }
    }

    public void customerLookupFallback(String customerNumber, Throwable ex) {
        log.warn("Customer service lookup failed for customerNumber={}: {}", customerNumber, ex.toString());
        throw new CustomerServiceUnavailableException(
                "Customer service is currently unavailable. Please try again later.",
                ex
        );
    }
}
