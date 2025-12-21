package com.bank.customer.service;

import com.bank.customer.entity.Customer;
import com.bank.customer.entity.CustomerDto;
import com.bank.customer.entity.CustomerMapper;
import com.bank.customer.entity.UploadResponse;
import com.bank.customer.repository.CustomerRepository;
import com.bank.customer.validation.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CustomerFileService {

    @Autowired
    private CustomerRepository customerRepository;

    private static final int CHUNK_SIZE = 50;

    public UploadResponse uploadCustomers(MultipartFile file) {

        int total = 0;
        int success = 0;

        List<String> errors = new ArrayList<>();
        List<Customer> batch = new ArrayList<>();

        Map<String, Integer> branchCounters = new HashMap<>();

        // 🔹 DB existing data (ONE TIME FETCH)
        Set<String> existingEmails = new HashSet<>(customerRepository.findAllEmails());
        Set<String> existingPhones = new HashSet<>(customerRepository.findAllPhones());

        // 🔹 In-file duplicate tracking
        Set<String> fileEmails = new HashSet<>();
        Set<String> filePhones = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                total++;
                String[] columns = line.split(",");

                try {
                    CustomerDto dto = mapToDto(columns);

                    // 1️⃣ Field validation
                    List<String> validationErrors = CustomerValidator.validate(dto);
                    if (!validationErrors.isEmpty()) {
                        errors.add("Row " + total + ": " + String.join(", ", validationErrors));
                        continue;
                    }

                    // 2️⃣ Duplicate check within file
                    if (!fileEmails.add(dto.getEmail())) {
                        errors.add("Row " + total + ": Duplicate email in file");
                        continue;
                    }

                    if (!filePhones.add(dto.getPhone())) {
                        errors.add("Row " + total + ": Duplicate phone in file");
                        continue;
                    }

                    // 3️⃣ Duplicate check against DB
                    if (existingEmails.contains(dto.getEmail())) {
                        errors.add("Row " + total + ": Email already exists in database");
                        continue;
                    }

                    if (existingPhones.contains(dto.getPhone())) {
                        errors.add("Row " + total + ": Phone already exists in database");
                        continue;
                    }

                    // 4️⃣ Customer number generation (branch-safe)
                    Integer counter = branchCounters.get(dto.getBranchCode());

                    if (counter == null) {
                        String last = customerRepository
                                .findLastCustomerNumberByBranchCode(dto.getBranchCode());

                        counter = (last == null)
                                ? 0
                                : Integer.parseInt(last.substring(dto.getBranchCode().length()));
                    }

                    counter++;
                    branchCounters.put(dto.getBranchCode(), counter);

                    String customerNumber =
                            buildCustomerNumber(dto.getBranchCode(), counter);

                    Customer customer =
                            CustomerMapper.toEntity(dto, customerNumber);

                    batch.add(customer);

                    // 5️⃣ Chunk save
                    if (batch.size() == CHUNK_SIZE) {
                        saveBatch(batch, existingEmails, existingPhones);
                        success += batch.size();
                        batch.clear();
                    }

                } catch (Exception ex) {
                    errors.add("Row " + total + ": " + ex.getMessage());
                }
            }

            // save remaining
            if (!batch.isEmpty()) {
                saveBatch(batch, existingEmails, existingPhones);
                success += batch.size();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process file", e);
        }

        return new UploadResponse(total, success, errors.size(), errors);
    }

    // 🔹 Safe batch save
    private void saveBatch(List<Customer> batch,
                           Set<String> existingEmails,
                           Set<String> existingPhones) {

        customerRepository.saveAll(batch);

        // update in-memory DB sets
        for (Customer c : batch) {
            existingEmails.add(c.getEmail());
            existingPhones.add(c.getPhone());
        }
    }

    private CustomerDto mapToDto(String[] columns) {
        if (columns.length < 4) {
            throw new IllegalArgumentException("Invalid column count");
        }

        return new CustomerDto(
                columns[0].trim(),
                columns[1].trim(),
                columns[2].trim(),
                columns[3].trim()
        );
    }

    private String buildCustomerNumber(String branchCode, int counter) {
        return branchCode + String.format("%04d", counter);
    }
}
