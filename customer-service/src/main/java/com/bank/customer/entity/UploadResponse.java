package com.bank.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UploadResponse {
    private int totalRecords;
    private int successCount;
    private int failureCount;
    private List<String> errors;
}