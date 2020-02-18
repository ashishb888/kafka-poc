package poc.kafka.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Customer;
import poc.kafka.properties.KafkaProperties;

/**
 * @author ashishb888
 */

@Service
@Slf4j
public class ConsumerService {

	@Autowired
	private KafkaProperties kp;
	AtomicBoolean kafkaConsumerRunning = new AtomicBoolean(true);

	private void consume() {
		log.debug("consume service");

		try {
			String topic = kp.getMetaData().get("topic");
			Consumer<Long, Customer> consumer = customerConsumer();

			consumer.subscribe(Arrays.asList(topic));

			while (kafkaConsumerRunning.get()) {
				ConsumerRecords<Long, Customer> records = consumer.poll(Duration.ofMillis(10));

				records.forEach(r -> {
					log.debug("r#value: " + r.value());
				});
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private Consumer<Long, Customer> customerConsumer() {
		log.debug("customerConsumer service");

		Properties configs = new Properties();
		kp.getKafkaConsumer().forEach((k, v) -> {
			configs.put(k, v);
		});

		configs.put("value.deserializer", "poc.kafka.domain.serialization.CustomerDeserializer");

		return new KafkaConsumer<>(configs);
	}

	public void main() {
		log.debug("main service");

		consume();
	}
}
