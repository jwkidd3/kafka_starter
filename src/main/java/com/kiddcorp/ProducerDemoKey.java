package com.kiddcorp;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoKey {

	public static void main(String[] args) throws ExecutionException, InterruptedException {

		Logger logger = LoggerFactory.getLogger(ProducerDemoKey.class);

		String bootstrapServers = "127.0.0.1:9092";
		String topic = "test";

		// create producer properties
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		// create producer
		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

		for (int i = 0; i < 10; i++) {
			// create a producer record
			String key = String.format("Key %d", i);
			String message = String.format("Message %s", key);
			ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

			// send data
			producer.send(record, (recordMetadata, e) -> {
				System.out.println(recordMetadata.partition());
				if (e == null) {
					logger.info("Received new metadata:\nTopic: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
							recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
							recordMetadata.timestamp());
				} else {
					logger.error("Error while producing.", e);
				}
			}).get(); // synchronous //DON'T DO it in production
			producer.flush();
		}
		producer.close();

	}
}
