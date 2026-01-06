package com.branch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncUseCase {

    @Async
    public void sendEmail(String email) {
        System.out.println("Thread: " + Thread.currentThread().getName());
        // simulate delay
        try {
            Thread.sleep(50000);
            log.info("Testing Async feature");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Email sent to " + email);
    }
}
