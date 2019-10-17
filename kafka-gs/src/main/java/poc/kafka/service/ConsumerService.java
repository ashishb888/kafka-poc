package poc.kafka.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Animal;
import poc.kafka.domain.Cat;
import poc.kafka.domain.Dog;
import poc.kafka.properties.KafkaProperties;

@Service
@Slf4j
@SuppressWarnings({ "unused" })
public class ConsumerService {

	@Autowired
	private KafkaProperties kp;
	private AtomicInteger count = new AtomicInteger(0);

	private void sendDiffTypes() {
		log.info("sendDiffTypes service");

		Producer<Integer, Animal> producer = animalProducer();
		String topic = "kp4";

		IntStream.iterate(0, i -> i + 1).limit(1000000).forEach(i -> {
			int partitions = 10;
			int partition = new Random().ints(0, partitions).findFirst().getAsInt();

			if (i % 2 == 0)
				producer.send(new ProducerRecord<Integer, Animal>(topic, partition, i, new Dog(i)));
			else
				producer.send(new ProducerRecord<Integer, Animal>(topic, partition, i, new Cat(i)));
		});

		producer.close();
	}

	private void consumeDiffTypes() {
		log.info("consumeDiffTypes service");
		
		int partitions = 10;
		
		ExecutorService es = Executors.newFixedThreadPool(partitions);
		AtomicInteger ai = new AtomicInteger(0);
		String topic = "kp4";

		for (int i = 0; i < partitions; i++) {
			es.submit(() -> {
				Consumer<Integer, Animal> consumer = animalConsumer();

				int partition = ai.getAndIncrement();

				log.debug("partition: " + partition);

				TopicPartition tp = new TopicPartition(topic, partition);
				consumer.assign(Arrays.asList(tp));
				consumer.seekToBeginning(Arrays.asList(tp));
				// consumer.assignment();

				while (true) {
					ConsumerRecords<Integer, Animal> records = consumer.poll(Duration.ofMillis(10));

					for (ConsumerRecord<Integer, Animal> consumerRecord : records) {
						count.getAndIncrement();
					}

					Thread.sleep(1000);
				}
			});
		}

		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

		timer.scheduleAtFixedRate(() -> {
			log.debug("count: " + count.get());
		}, 10, 10, TimeUnit.SECONDS);
	}

	private Producer<Integer, Animal> animalProducer() {

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	private Consumer<Integer, Animal> animalConsumer() {

		Properties kafkaProps = new Properties();

		kp.getKafkaConsumer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaConsumer<>(kafkaProps);
	}

	private void produceConsume() {
		log.debug("produceConsume service");

		// sendDiffTypes();
		consumeDiffTypes();
	}

	public void main() {
		log.debug("main service");

		produceConsume();
	}
}
