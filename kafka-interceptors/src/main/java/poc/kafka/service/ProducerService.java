package poc.kafka.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
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
public class ProducerService {

	@Autowired
	private KafkaProperties kp;

	private void produceCustomers() {
		log.debug("produceCustomers service");

		try {
			Producer<Long, Customer> producer = customerProducer();
			// long records = Long.valueOf(kp.getMetaData().get("records"));
			// String topic = kp.getMetaData().get("topic");
			long records = 10L;
			String topic = "ki-1";
			List<String> cities = Arrays.asList("Kamothe", "Kharghar", "Vashi", "Sanpada", "Nerul");
			List<String> countries = Arrays.asList("India", "USA", "UK", "Japan");

			for (long i = 0; i < records; i++) {
				Collections.shuffle(cities);
				Collections.shuffle(countries);

				producer.send(new ProducerRecord<Long, Customer>(topic, i,
						new Customer(i, i, "c" + i, cities.get(0), countries.get(0))));

				// Thread.sleep(10000);
			}

			producer.close();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private Producer<Long, Customer> customerProducer() {
		log.debug("customerProducer service");

		Properties configs = new Properties();
		kp.getKafkaProducer().forEach((k, v) -> {
			configs.put(k, v);
		});

		configs.put("value.serializer", "poc.kafka.domain.serialization.CustomerSerializer");

		return new KafkaProducer<>(configs);
	}

	public void main() {
		log.debug("main service");

		produceCustomers();
	}
}
