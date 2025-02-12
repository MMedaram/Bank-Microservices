package com.bank.account.client;

import com.bank.account.config.FeignConfig;
import com.bank.account.entity.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "customer-service")
public interface CustomerClient {

    @GetMapping("/api/customers/{customerNumber}")
    CustomerDto getCustomerByCustomerNumber(@PathVariable String customerNumber); //The path must match exactly what is defined in CustomerController

}