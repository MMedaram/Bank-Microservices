package com.bank.notificationservice.config;

import com.bank.notificationservice.event.AccountCreatedEvent;
import com.bank.notificationservice.event.CustomerCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, CustomerCreatedEvent> customerCreatedConsumerFactory() {

        JsonDeserializer<CustomerCreatedEvent> deserializer =  new JsonDeserializer<>(CustomerCreatedEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerCreatedEvent> customerCreatedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, CustomerCreatedEvent> factory =  new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(customerCreatedConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, AccountCreatedEvent> accountCreatedConsumerFactory() {

        JsonDeserializer<AccountCreatedEvent> deserializer = new JsonDeserializer<>(AccountCreatedEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountCreatedEvent> accountCreatedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, AccountCreatedEvent> factory =  new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountCreatedConsumerFactory());
        return factory;
    }

    // common configs
    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}
