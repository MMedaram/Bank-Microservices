package com.bank.customer.service;

import com.bank.customer.clinet.BranchClient;
import com.bank.customer.entity.Customer;
import com.bank.customer.entity.CustomerDto;
import com.bank.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BranchClient branchClient;

    public Customer createCustomer(CustomerDto customerDto) {
        // Validate branchCode by calling Branch Service
        if (!branchClient.isBranchExists(customerDto.getBranchCode())) {
            throw new RuntimeException("Invalid branch code: " + customerDto.getBranchCode());
        }

        // Generate the next customer number
        String nextCustomerNumber = generateNextCustomerNumber(customerDto.getBranchCode());

        Customer customer = new Customer();
        customer.setCustomerNumber(nextCustomerNumber);
        customer.setName(customerDto.getName());
        customer.setBranchCode(customerDto.getBranchCode());
        customer.setPhone(customerDto.getPhone());
        customer.setEmail(customerDto.getEmail());

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerByCustomerNumber(String customerNumber) {
        return customerRepository.findByCustomerNumber(customerNumber);
    }

    public Customer updateCustomer(Long id, CustomerDto updatedCustomer) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setName(updatedCustomer.getName());
                    customer.setEmail(updatedCustomer.getEmail());
                    customer.setPhone(updatedCustomer.getPhone());
                    customer.setBranchCode(updatedCustomer.getBranchCode());

                    return customerRepository.save(customer);
                }).orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public String generateNextCustomerNumber(String branchCode) {

        // Fetch the last customer number for the given branchCode
        String lastCustomerNumber = customerRepository.findLastCustomerNumberByBranchCode(branchCode);

        int nextNumber = 1; // Default starting value if no customer exists yet

        if (lastCustomerNumber != null && lastCustomerNumber.startsWith(branchCode)) {
            // Extract the number part and increment it
            String lastNumberPart = lastCustomerNumber.substring(branchCode.length());
            nextNumber = Integer.parseInt(lastNumberPart) + 1;
        }

        // Format the next number to 4 digits (e.g., 0001, 0002, etc.)
        DecimalFormat format = new DecimalFormat("0000");
        return branchCode + format.format(nextNumber);
    }


}
