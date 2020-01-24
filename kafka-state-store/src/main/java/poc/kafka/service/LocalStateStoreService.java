package poc.kafka.service;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Customer;
import poc.kafka.domain.serialization.CustomerDeserializer;
import poc.kafka.domain.serialization.CustomerSerializer;
import poc.kafka.properties.KafkaProperties;
import poc.kafka.service.constants.Constants;

/**
 * @author ashishb888
 */

@Service
@Slf4j
public class LocalStateStoreService {

	@Autowired
	private KafkaProperties kp;

	private void groupedByCity() throws Exception {
		log.debug("groupedByCity service");

		final StreamsBuilder builder = new StreamsBuilder();
		// String topic = kp.getMetaData().get("topic");
		final Serde<Customer> customerSerde = Serdes.serdeFrom(new CustomerSerializer(), new CustomerDeserializer());
		final Serde<Long> longSerde = Serdes.Long();
		final Serde<String> stringSerde = Serdes.String();
		String storeName = "GroupedByCity";
		String topic = Constants.CUSTOMER_TOPIC;

		KStream<Long, Customer> source = builder.stream(topic, Consumed.with(longSerde, customerSerde));
		KGroupedStream<String, Customer> groupedByCity = source.selectKey((k, v) -> v.getCity())
				.groupByKey(Grouped.with(stringSerde, customerSerde));
		groupedByCity.count(Materialized.as(storeName));

		final Topology topology = builder.build();
		final KafkaStreams streams = new KafkaStreams(topology, configs());
		final CountDownLatch latch = new CountDownLatch(1);

		log.info("topology: " + topology.describe());

		new Thread(() -> {

			while (!streams.state().equals(KafkaStreams.State.RUNNING)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

			ReadOnlyKeyValueStore<String, Customer> keyValueStore = streams.store(storeName,
					QueryableStoreTypes.keyValueStore());

			keyValueStore.all().forEachRemaining(r -> {
				log.debug("k: " + r.key + ", v: " + r.value);
			});

		}, "state-store").start();

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
		log.debug("configs service");

		Properties configs = new Properties();

		kp.getKafkaStreams().forEach((k, v) -> {
			configs.put(k, v);
		});

		return configs;
	}

	private void start() throws Exception {
		log.debug("start service");

		groupedByCity();
	}

	public void main() {
		log.debug("main service");

		try {
			start();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
