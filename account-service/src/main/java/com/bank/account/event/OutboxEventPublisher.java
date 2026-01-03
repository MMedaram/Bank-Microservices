package com.bank.account.event;

import com.bank.account.entity.EventOutLog;
import com.bank.account.repository.EventOutLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OutboxEventPublisher {

    private final EventOutLogRepository outLogRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OutboxEventPublisher(EventOutLogRepository outLogRepository,
                                KafkaTemplate<String, Object> kafkaTemplate,
                                ObjectMapper objectMapper) {
        this.outLogRepository = outLogRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void publishPendingEvents() {

        List<EventOutLog> events = outLogRepository.findTop10ByStatusOrderByCreatedAtAsc("NEW");

        for (EventOutLog outLog : events) {
            try {
                Object payload = outLog.getPayload();

                kafkaTemplate.send(
                        outLog.getTopicName(),
                        outLog.getAggregateId(),
                        payload
                );


                outLog.setStatus("PUBLISHED");
                outLog.setPublishedAt(LocalDateTime.now());

            } catch (Exception ex) {
                outLog.setRetryCount(outLog.getRetryCount() + 1);
                // status stays NEW for retry
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void publishOutbox() {
        publishPendingEvents();
    }
}
