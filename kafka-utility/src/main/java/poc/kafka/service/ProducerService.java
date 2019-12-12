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
import poc.kafka.domain.KCString;
import poc.kafka.domain.Person;
import poc.kafka.domain.Schema;
import poc.kafka.properties.KafkaProperties;

@Service
@Slf4j
public class ProducerService {

	@Autowired
	private KafkaProperties kp;
	private AtomicInteger count = new AtomicInteger(0);

	private void produce() {
		log.debug("produce service");

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);
		int records = Integer.valueOf(kp.getMetaData().get("records"));

		Producer<Integer, Person> producer = producer();

		for (int i = 0; i < records; i++) {
			producer.send(new ProducerRecord<Integer, Person>(topic, i, new Person(i, "p" + i)));

			count.getAndIncrement();
		}

		producer.close();
	}

	private void produceString() {
		log.debug("produceString service");

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);
		int records = Integer.valueOf(kp.getMetaData().get("records"));

		Producer<Integer, String> producer = stringProducer();

		for (int i = 0; i < records; i++) {
			producer.send(new ProducerRecord<Integer, String>(topic, i, "p" + i));

			count.getAndIncrement();
		}

		producer.close();
	}

	private void produceKCString() {
		log.debug("produceKCString service");

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);
		int records = Integer.valueOf(kp.getMetaData().get("records"));

		Producer<Integer, KCString> producer = stringKCProducer();

		for (int i = 0; i < records; i++) {
			producer.send(new ProducerRecord<Integer, KCString>(topic, i,
					new KCString(new Schema("string", false), "p" + i)));

			count.getAndIncrement();
		}

		producer.close();
	}

	private Producer<Integer, KCString> stringKCProducer() {
		log.debug("stingProducer service");

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

	private Producer<Integer, Person> producer() {
		log.debug("producer service");

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	private Producer<Integer, String> stringProducer() {
		log.debug("stingProducer service");

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	public void main() {
		log.debug("main service");

		timer();
		// produce();
		// produceString();
		produceKCString();
	}
}
