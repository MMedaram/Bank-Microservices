package com.bank.customer.controller;

import com.bank.customer.entity.UploadResponse;
import com.bank.customer.service.CustomerFileService;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/customers/files")
public class CustomerFileController {

    @Autowired
    private CustomerFileService customerFileService;

    @PostMapping( value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<UploadResponse> uploadCustomers( @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        UploadResponse response = customerFileService.uploadCustomers(file);

          return ResponseEntity.ok(response);
    }
}