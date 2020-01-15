package poc.kafka.service;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Customer;
import poc.kafka.domain.CustomerOrder;
import poc.kafka.domain.Order;
import poc.kafka.domain.serialization.CustomerDeserializer;
import poc.kafka.domain.serialization.CustomerSerializer;
import poc.kafka.domain.serialization.OrderDeserializer;
import poc.kafka.domain.serialization.OrderSerializer;
import poc.kafka.properties.KafkaProperties;
import poc.kafka.service.constants.Constants;

/**
 * @author ashishb888
 */

@Service
@Slf4j
public class InnerJoinService {

	@Autowired
	private KafkaProperties kp;

	private void streamStreamJoin() {
		log.debug("streamStreamJoin service");

		final StreamsBuilder builder = new StreamsBuilder();
		// String topic = kp.getMetaData().get("topic");
		final String customerTopic = Constants.CUSTOMER_TOPIC;
		final String orderTopic = Constants.ORDER_TOPIC;

		Serde<Customer> customerSerde = Serdes.serdeFrom(new CustomerSerializer(), new CustomerDeserializer());
		Serde<Order> orderSerde = Serdes.serdeFrom(new OrderSerializer(), new OrderDeserializer());
		Serde<Long> longSerde = Serdes.Long();

		KStream<Long, Customer> customerStream = builder.stream(customerTopic, Consumed.with(longSerde, customerSerde));
		KStream<Long, Order> orderStream = builder.stream(orderTopic, Consumed.with(longSerde, orderSerde))
				.selectKey((k, v) -> v.getCustomerId());

		KStream<Long, CustomerOrder> joinedStream = orderStream.join(customerStream,
				(order, customer) -> new CustomerOrder(customer, order), JoinWindows.of(Duration.ofSeconds(120)),
				Joined.with(longSerde, orderSerde, customerSerde));

		joinedStream.peek((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
		});

		final Topology topology = builder.build();
		final KafkaStreams streams = new KafkaStreams(topology, configs());
		final CountDownLatch latch = new CountDownLatch(1);

		log.info("topology: " + topology.describe());

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			streams.close();
			latch.countDown();
		}, "streams-shutdown-hook"));

		try {
			streams.start();
			latch.await();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}

	}

	private Properties configs() {
		Properties configs = new Properties();

		kp.getKafkaStreams().forEach((k, v) -> {
			configs.put(k, v);
		});

		return configs;
	}

	private void joins() {
		streamStreamJoin();
	}

	private void start() {
		log.debug("start service");

		joins();
	}

	public void main() {
		log.debug("main service");

		start();
	}
}
