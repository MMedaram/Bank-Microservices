package com.bank.account.repository;

import com.bank.account.entity.EventOutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventOutLogRepository extends JpaRepository<EventOutLog, String> {

    List<EventOutLog> findTop10ByStatusOrderByCreatedAtAsc(String status);

}
