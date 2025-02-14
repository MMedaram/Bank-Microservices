package com.bank.account.client;

import com.bank.account.entity.EmailRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications/email")
    public String sendEmail(@RequestBody EmailRequestDto request);
}
