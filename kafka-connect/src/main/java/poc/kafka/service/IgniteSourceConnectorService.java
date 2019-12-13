package poc.kafka.service;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Key;
import poc.kafka.domain.KeySchema;
import poc.kafka.domain.Person;
import poc.kafka.domain.Schema2;
import poc.kafka.domain.Value;
import poc.kafka.domain.ValueSchema;
import poc.kafka.properties.KafkaProperties;

@Service
@Slf4j
public class IgniteSourceConnectorService {

	@Autowired
	private KafkaProperties kp;
	private AtomicInteger count = new AtomicInteger(0);

	private void produce() {
		log.debug("produce service");

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);
		int records = Integer.valueOf(kp.getMetaData().get("records"));

		Producer<Key, Value> producer = producer();

		for (int i = 0; i < records; i++) {
			Schema2[] fields = { new Schema2("int32", false, "cityId"), new Schema2("string", false, "name") };

			producer.send(new ProducerRecord<Key, Value>(topic, new Key(new KeySchema("int32", false), i),
					new Value(new ValueSchema("struct", false, fields, "Person"), new Person(i, "p" + i))));

			count.getAndIncrement();
		}

		producer.close();
	}

	private Producer<Key, Value> producer() {
		log.debug("producer service");

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	private void timer() {
		log.debug("timer service");

		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

		timer.scheduleAtFixedRate(() -> {
			log.debug("count: " + count.get());
		}, 10, 10, TimeUnit.SECONDS);
	}

	public void main() {
		log.debug("main service");

		timer();
		produce();
	}
}
