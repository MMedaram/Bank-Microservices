package com.bank.customer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic customerCreatedTopic() {
        return TopicBuilder.name("customer.created.v1")
                .partitions(3)
                .replicas(1)
                .build();
    }

}

