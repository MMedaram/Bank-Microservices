package com.bank.customer.validation;

import com.bank.customer.entity.CustomerDto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class CustomerValidator {

    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static List<String> validate(CustomerDto dto) {

        List<String> errors = new ArrayList<>();

        if (dto.getName() == null || dto.getName().isBlank()) {
            errors.add("Name is required");
        }

        if (dto.getEmail() == null || !EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            errors.add("Invalid email");
        }

        if (dto.getPhone() == null || !PHONE_PATTERN.matcher(dto.getPhone()).matches()) {
            errors.add("Phone must be 10 digits");
        }

        if (dto.getBranchCode() == null || dto.getBranchCode().isBlank()) {
            errors.add("Branch code is required");
        }

        return errors;
    }
}
