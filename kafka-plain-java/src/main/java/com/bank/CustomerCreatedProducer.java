package com.bank;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class CustomerCreatedProducer {
    public static void main(String[] args) {

        Properties props = new Properties();

        // 1. Kafka broker
        props.put("bootstrap.servers", "localhost:9092");

        // 2. Serialization
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 3. Reliability
        props.put("acks", "all");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        String topic = "customer.created.v1";
        String key = "C123"; // customerId (partition key)

        String value = """
                {
                  "customerId": "C0123",
                  "name": "MohanM",
                  "email": "MohanM@gmail.com"
                }
                """;
        ProducerRecord<String, String> record =  new ProducerRecord<>(topic, key, value);
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println(
                        "Message sent to topic=" + metadata.topic()
                                + " partition=" + metadata.partition()
                                + " offset=" + metadata.offset()
                );
            } else {
                exception.printStackTrace();
            }
        });


        producer.flush();   // 🔴 ensures message is sent
        producer.close();



    }
}
