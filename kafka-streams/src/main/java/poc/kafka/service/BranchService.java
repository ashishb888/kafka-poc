package poc.kafka.service;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Animal;
import poc.kafka.domain.Cat;
import poc.kafka.domain.Dog;
import poc.kafka.domain.serialization.AnimalDeserializer;
import poc.kafka.domain.serialization.AnimalSerde;
import poc.kafka.domain.serialization.AnimalSerializer;
import poc.kafka.properties.KafkaProperties;

/**
 * @author ashishb888
 */

@Service
@Slf4j
@SuppressWarnings("unused")
public class BranchService {

	@Autowired
	private KafkaProperties kp;

	private void branches() {
		log.info("branches service");

		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "branch-service");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
				kp.getKafkaStreams().get(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG));
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, AnimalSerde.class);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		final StreamsBuilder builder = new StreamsBuilder();

		KStream<Integer, Animal> source = builder.stream("gs3",
				Consumed.with(Serdes.Integer(), Serdes.serdeFrom(new AnimalSerializer(), new AnimalDeserializer())));

		@SuppressWarnings("unchecked")
		KStream<Integer, Animal>[] branches = source.branch((key, value) -> value instanceof Cat,
				(key, value) -> value instanceof Dog, (key, value) -> true);
		// branches[0].print();
		branches[0].to("cat");
		branches[1].to("dog");

		final Topology topology = builder.build();

		final KafkaStreams streams = new KafkaStreams(topology, props);
		final CountDownLatch latch = new CountDownLatch(1);

		log.info("topology: " + topology.describe());

		// attach shutdown handler to catch control-c
		Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
			@Override
			public void run() {
				streams.close();
				latch.countDown();
			}
		});

		try {
			streams.start();
			latch.await();
		} catch (Throwable e) {
			// System.exit(1);
			log.error("", e);
		}

		// System.exit(0);
	}

	public void main() {
		log.info("main service");

		branches();
	}
}
