package poc.kafka.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

import lombok.extern.java.Log;
import poc.kafka.domain.Animal;
import poc.kafka.domain.Cat;
import poc.kafka.domain.Dog;
import poc.kafka.domain.Employee;
import poc.kafka.domain.serialization.EmployeeDeserializer;
import poc.kafka.domain.serialization.EmployeeSerializer;
import poc.kafka.properties.KafkaProperties;

@Service
@Log
@SuppressWarnings({ "unused", "resource" })
public class ExternalizableService {

	@Autowired
	private KafkaProperties kp;

	private void sendDiffTypes() {
		log.info("sendDiffTypes service");

		Producer<Integer, Animal> producer = animalProducer();

		IntStream.iterate(0, i -> i + 1).limit(10).forEach(i -> {
			if (i % 2 == 0)
				producer.send(new ProducerRecord<Integer, Animal>("gs4", i, new Dog(i)));
			else
				producer.send(new ProducerRecord<Integer, Animal>("gs4", i, new Cat(i)));
		});
	}

	private void consumeDiffTypes() {
		log.info("consumeDiffTypes service");

		Consumer<Integer, Animal> consumer = animalConsumer();
		TopicPartition tp = new TopicPartition("gs3", 0);
		consumer.assign(Arrays.asList(tp));

		while (true) {
			ConsumerRecords<Integer, Animal> records = consumer.poll(Duration.ofMillis(10));

			records.forEach(r -> {
				log.info("r: " + r);
			});
		}
	}

	private void produce() {
		log.info("produce service");

		Producer<Integer, Employee> producer = producer();

		IntStream.iterate(0, i -> i + 1).limit(10).forEach(i -> {
			producer.send(new ProducerRecord<Integer, Employee>("gs1", i, new Employee(String.valueOf(i), i)));
		});
	}

	private Producer<Integer, Animal> animalProducer() {

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			// log.info("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	private Consumer<Integer, Animal> animalConsumer() {

		Properties kafkaProps = new Properties();

		kp.getKafkaConsumer().forEach((k, v) -> {
			// log.info("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaConsumer<>(kafkaProps);
	}

	private Producer<Integer, Employee> producer() {

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			// log.info("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	private void consume() {
		log.info("consume service");

		Consumer<Integer, Employee> consumer = consumer();
		TopicPartition tp = new TopicPartition("gs1", 0);

		consumer.assign(Arrays.asList(tp));
		consumer.seekToBeginning(Arrays.asList(tp));

		while (true) {
			ConsumerRecords<Integer, Employee> records = consumer.poll(Duration.ofMillis(10));

			for (ConsumerRecord<Integer, Employee> record : records) {
				log.info("record: " + record);
			}
		}
	}

	private Consumer<Integer, Employee> consumer() {

		Properties kafkaProps = new Properties();

		kp.getKafkaConsumer().forEach((k, v) -> {
			// log.info("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaConsumer<>(kafkaProps);
	}

	private void produceToMultiPartitions() {
		log.info("produceToMultiPartitions service");

		Producer<Integer, Employee> producer = producer();

		IntStream.iterate(0, i -> i + 1).limit(10).forEach(i -> {
			producer.send(new ProducerRecord<Integer, Employee>("gs2", i, i, new Employee(String.valueOf(i), i)));
		});
	}

	private void consumeByGroup() {
		log.info("consumeByGroup service");

		final int partitions = 10;
		ExecutorService es = Executors.newFixedThreadPool(partitions);

		for (int i = 0; i < partitions; i++) {
			int localI = i;

			es.submit(() -> {

				Consumer<Integer, Employee> consumer = consumer();
				TopicPartition tp = new TopicPartition("gs2", localI);

				consumer.assign(Arrays.asList(tp));
				consumer.seekToBeginning(Arrays.asList(tp));

				while (true) {
					ConsumerRecords<Integer, Employee> records = consumer.poll(Duration.ofMillis(10));

					for (ConsumerRecord<Integer, Employee> record : records) {
						log.info("record: " + record);
					}
				}
			});
		}
	}

	private void produceConsume() {
		log.info("produceConsume service");

		sendDiffTypes();
		consumeDiffTypes();
		// produceToMultiPartitions();
		// consumeByGroup();
		// produce();
		// consume();
	}

	private void serialization() {
		log.info("serialization service");

		Employee emp = new Employee("Ashish", 21);
		log.info("emp: " + emp);

		byte[] empBytes = new EmployeeSerializer().serialize("", emp);
		log.info("empBytes: " + empBytes);

		Employee emp1 = new EmployeeDeserializer().deserialize("", empBytes);
		log.info("emp1: " + emp1);
	}

	public void main() {
		log.info("main service");

		// serialization();
		produceConsume();
	}
}
