package com.kiddcorp;
import java.time.LocalDateTime;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Example of a simple producer
 */
public class BasicProducer {

    public static void main(String[] args) {

        String bootstrapServers = "127.0.0.1:9092";
        String topic = "test";

        //create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //create a producer record
        String message = String.format("Hello world - %tN", LocalDateTime.now());
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);

        //send data
        producer.send(record);
        producer.flush();
        producer.close();
    }
}
