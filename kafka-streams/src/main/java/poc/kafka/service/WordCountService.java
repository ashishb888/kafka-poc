package poc.kafka.service;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

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
import poc.kafka.properties.KafkaProperties;

/**
 * @author ashishb888
 */

@Service
@Slf4j
@SuppressWarnings("unused")
public class WordCountService {

	@Autowired
	private KafkaProperties kp;

	private void wordsCount() {
		log.info("wordsCount service");

		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-linesplit");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
				kp.getKafkaStreams().get(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG));
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		final StreamsBuilder builder = new StreamsBuilder();

		KStream<String, String> source = builder.stream("streams-plaintext-input",
				Consumed.with(Serdes.String(), Serdes.String()));

		source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+")))
				.groupBy((key, value) -> value).count(Materialized.as("counts-store")).toStream()
				.to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
//				.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store")).toStream()
//				.to("streams-wordcount-output", Produced.<String, Long>with(Serdes.String(), Serdes.Long()));

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

		wordsCount();
	}
}
