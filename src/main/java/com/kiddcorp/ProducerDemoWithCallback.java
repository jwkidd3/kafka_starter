package com.kiddcorp;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoWithCallback {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
        
        String bootstrapServers = "127.0.0.1:9092";
        String topic = "test";

        //create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for(int i=0; i<10; i++) {
            //create a producer record
        	String key = String.format("Key %d", i);
			String message = String.format("CallBack MSG %s", key);
			ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

            //send data
            producer.send(record, new Callback() {
                //executes every time a record is successfully sent or an exception is thrown
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    
                	if (e == null) {
                        System.out.printf("Received new metadata:\nTopic: %s\nPartition: "
                        		+ "%s\nOffset: %s\nTimestamp: %s\n",
                                recordMetadata.topic(), recordMetadata.partition(), 
                                recordMetadata.offset(), recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing.", e);
                    }
                }
            });
            producer.flush();
        }
        producer.close();

    }
}