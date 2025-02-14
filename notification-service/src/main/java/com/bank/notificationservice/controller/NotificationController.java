package com.bank.notificationservice.controller;

import com.bank.notificationservice.dto.EmailRequest;
import com.bank.notificationservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")

public class NotificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {

        Map<String, Object> templateModel = Map.of(
                "customerName", request.getCustomerName(),
                "amount", request.getAmount(),
                "availableBalance", request.getAvailableBalance(),
                "accountNumber",request.getAccountNumber(),
                "transactionType",request.getTransactionType()
        );

        emailService.sendEmail(request.getTo(),templateModel);

        return ResponseEntity.ok("Email sent successfully to " + request.getTo());
    }
}
