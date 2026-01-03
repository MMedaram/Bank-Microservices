package com.bank.notificationservice.repository;

import com.bank.notificationservice.dto.EventInLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInLogRepository extends JpaRepository<EventInLog, String> {
}
