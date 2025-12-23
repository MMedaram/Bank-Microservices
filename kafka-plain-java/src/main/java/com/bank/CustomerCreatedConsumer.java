package com.bank;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class CustomerCreatedConsumer {
    public static void main(String[] args) {
        KafkaConsumer<String, String> consumer = getStringStringKafkaConsumer();

        consumer.subscribe(Collections.singletonList("customer.created.v1"));

       while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofSeconds(1));

            for (ConsumerRecord<String, String> record : records) {
                System.out.println(
                        "Consumed message key=" + record.key()
                                + " value=" + record.value()
                                + " partition=" + record.partition()
                                + " offset=" + record.offset()
                );
            }
        }


    }

    private static KafkaConsumer<String, String> getStringStringKafkaConsumer() {
        Properties props = new Properties();

        // 1. Kafka broker
        props.put("bootstrap.servers", "localhost:9092");

        // 2. Consumer group
        props.put("group.id", "notification-service");

        // 3. Deserialization
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 4. Offset behavior
        props.put("auto.offset.reset", "earliest");

        return new KafkaConsumer<>(props);
    }
}
