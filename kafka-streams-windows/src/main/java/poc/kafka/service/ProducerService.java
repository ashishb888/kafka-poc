package poc.kafka.service;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Customer;
import poc.kafka.domain.Order;
import poc.kafka.properties.KafkaProperties;
import poc.kafka.service.constants.Constants;

/**
 * @author ashishb888
 */

@Service
@Slf4j
public class ProducerService {

	@Autowired
	private KafkaProperties kp;

	private void produceOrders() {
		log.debug("produceOrders service");

		try {
			Producer<Long, Order> producer = orderProducer();
			long records = Long.valueOf(kp.getMetaData().get("records"));
			// String topic = kp.getMetaData().get("topic");
			String topic = Constants.ORDER_TOPIC;

			for (long i = 0; i < records; i++) {
				long customerId = new Random().longs(0, 10).boxed().findFirst().get();

				producer.send(new ProducerRecord<Long, Order>(topic, i,
						new Order(i, customerId, i, new Date(System.currentTimeMillis()), i)));

				Thread.sleep(10000);
			}

			producer.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private Producer<Long, Order> orderProducer() {
		log.debug("orderProducer service");

		Properties configs = new Properties();
		kp.getKafkaProducer().forEach((k, v) -> {
			configs.put(k, v);
		});

		configs.put("value.serializer", "poc.kafka.domain.serialization.OrderSerializer");

		return new KafkaProducer<>(configs);
	}

	private void produceCustomers() {
		log.debug("produceCustomers service");

		try {
			Producer<Long, Customer> producer = customerProducer();
			// long records = Long.valueOf(kp.getMetaData().get("records"));
			// String topic = kp.getMetaData().get("topic");
			long records = 10L;
			String topic = Constants.CUSTOMER_TOPIC;
			List<String> cities = Arrays.asList("Kamothe", "Kharghar", "Vashi", "Sanpada", "Nerul");
			List<String> countries = Arrays.asList("India", "USA", "UK", "Japan");

			while (true) {
				Collections.shuffle(cities);
				Collections.shuffle(countries);

				long i = new Random().longs(0, records).findFirst().getAsLong();

				producer.send(new ProducerRecord<Long, Customer>(topic, i,
						new Customer(i, i, "c" + i, cities.get(0), countries.get(0))));

				long millis = new Random().longs(1000, 5000).findFirst().getAsLong();
				Thread.sleep(millis);
			}
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

		configs.put("value.serializer", "poc.kafka.domain.serialization.CustomerJsonSerializer");

		return new KafkaProducer<>(configs);
	}

	public void main() {
		log.debug("main service");

		new Thread(() -> {
			produceCustomers();
		}, "customer-producer").start();
	}
}
