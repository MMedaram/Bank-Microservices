package com.bank.customer.entity;

import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public static Customer toEntity(CustomerDto dto, String customerNumber) {
        Customer customer = new Customer();
        customer.setCustomerNumber(customerNumber);
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setBranchCode(dto.getBranchCode());
        return customer;
    }
}
