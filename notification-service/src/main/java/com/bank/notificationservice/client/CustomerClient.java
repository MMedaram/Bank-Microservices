package com.bank.notificationservice.client;

import com.bank.notificationservice.dto.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/api/customers")
    List<Customer> getAllCustomers();

}