package poc.kafka.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Key;
import poc.kafka.domain.Key1;
import poc.kafka.domain.Key2;
import poc.kafka.domain.KeySchema;
import poc.kafka.domain.KeySchemaless;
import poc.kafka.domain.Person;
import poc.kafka.domain.Person1;
import poc.kafka.domain.Person1Key;
import poc.kafka.domain.Person2;
import poc.kafka.domain.Person2Key;
import poc.kafka.domain.PersonBinary;
import poc.kafka.domain.Schema2;
import poc.kafka.domain.Value;
import poc.kafka.domain.Value1;
import poc.kafka.domain.Value2;
import poc.kafka.domain.ValueSchema;
import poc.kafka.domain.ValueSchemaless;
import poc.kafka.properties.KafkaProperties;

@Service
@Slf4j
public class IgniteSourceConnectorService {

	@Autowired
	private KafkaProperties kp;
	private AtomicInteger count = new AtomicInteger(0);

	private void produce2WithDates() {
		log.debug("produceWithDates service");

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);
		int records = Integer.valueOf(kp.getMetaData().get("records"));

		Producer<Key2, Value2> producer = producer2WithDates();

//		Schema2[] keyFields = { new Schema2("int32", false, "id"), new Schema2("int32", false, "cityId") };
//		Schema2[] fields = { new Schema2("int32", false, "id"), new Schema2("int32", false, "cityId"),
//				new Schema2("string", false, "name"),
//				new Schema2("int32", false, "aDate", "org.apache.kafka.connect.data.Date"),
//				new Schema2("int64", false, "aTimestamp", "org.apache.kafka.connect.data.Timestamp") };

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		log.debug("date: " + calendar.getTime());

//		for (int i = 0; i < records; i++) {
//			producer.send(new ProducerRecord<Key2, Value2>(topic,
//					new Key2(new ValueSchema("struct", false, keyFields, "Person2Key"), new Person2Key(i, i)),
//					new Value2(new ValueSchema("struct", false, fields, "Person2"), new Person2(i, i, "p" + i,
//							org.apache.kafka.connect.data.Date.fromLogical(SchemaBuilder.int32()
//									.name("org.apache.kafka.connect.data.Date").schema(), calendar.getTime()),
//							Timestamp.fromLogical(
//									SchemaBuilder.int64().name("org.apache.kafka.connect.data.Timestamp").schema(),
//									new Date())))));
//
//			count.getAndIncrement();
//		}

		Schema2[] keyFields = { new Schema2("int32", false, "id", "org.apache.kafka.connect.data.Date"),
				new Schema2("int64", false, "cityId", "org.apache.kafka.connect.data.Timestamp") };
		Schema2[] fields = { new Schema2("int32", false, "id", "org.apache.kafka.connect.data.Date"),
				new Schema2("int64", false, "cityId", "org.apache.kafka.connect.data.Timestamp") };

		int d = org.apache.kafka.connect.data.Date.fromLogical(
				SchemaBuilder.int32().name("org.apache.kafka.connect.data.Date").schema(), calendar.getTime());
		long t = Timestamp.fromLogical(SchemaBuilder.int64().name("org.apache.kafka.connect.data.Timestamp").schema(),
				new Date());

		for (int i = 0; i < records; i++) {
			producer.send(new ProducerRecord<Key2, Value2>(topic,
					new Key2(new ValueSchema("struct", false, keyFields, "Person2Key"), new Person2Key(d, t)),
					new Value2(new ValueSchema("struct", false, fields, "Person2"), new Person2(d, t))));

			count.getAndIncrement();
		}

		producer.close();
	}

	private Producer<Key2, Value2> producer2WithDates() {
		log.debug("producerWithDates service");

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	private void produce1() {
		log.debug("produce1 service");

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);
		int records = Integer.valueOf(kp.getMetaData().get("records"));

		Producer<Key1, Value1> producer = producer1();

		for (int i = 0; i < records; i++) {
			Schema2[] keyFields = { new Schema2("int32", false, "id"), new Schema2("int32", false, "cityId") };
			Schema2[] fields = { new Schema2("int32", false, "id"), new Schema2("int32", false, "cityId"),
					new Schema2("string", false, "name") };

			producer.send(new ProducerRecord<Key1, Value1>(topic,
					new Key1(new ValueSchema("struct", false, keyFields, "Person1Key"), new Person1Key(i, i)),
					new Value1(new ValueSchema("struct", false, fields, "Person1"), new Person1(i, i, "p" + i))));

//			producer.send(new ProducerRecord<Key1, Value1>(topic, new Key1(new Person1Key(i, i)),
//					new Value1(new Person1(i, i, "p" + i))));

			count.getAndIncrement();
		}

		producer.close();
	}

	private Producer<Key1, Value1> producer1() {
		log.debug("producer1 service");

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	private void produceSchemaless2() {
		log.debug("produceSchemaless2 service");

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);
		int records = Integer.valueOf(kp.getMetaData().get("records"));

		Producer<Integer, PersonBinary> producer = producerSchemaless2();

		for (int i = 0; i < records; i++) {
			producer.send(new ProducerRecord<Integer, PersonBinary>(topic, i, new PersonBinary(i, "p" + i)));

			count.getAndIncrement();
		}

		producer.close();
	}

	private Producer<Integer, PersonBinary> producerSchemaless2() {
		log.debug("producerSchemaless2 service");

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

	private void produceSchemaless1() {
		log.debug("produceSchemaless1 service");

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);
		int records = Integer.valueOf(kp.getMetaData().get("records"));

		Producer<Integer, Person> producer = producerSchemaless1();

		for (int i = 0; i < records; i++) {
			producer.send(new ProducerRecord<Integer, Person>(topic, i, new Person(i, "p" + i)));

			count.getAndIncrement();
		}

		producer.close();
	}

	private Producer<Integer, Person> producerSchemaless1() {
		log.debug("producerSchemaless1 service");

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
	}

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

	private void produceSchemaless() {
		log.debug("produceSchemaless service");

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);
		int records = Integer.valueOf(kp.getMetaData().get("records"));

		Producer<KeySchemaless, ValueSchemaless> producer = producerSchemaless();

		for (int i = 0; i < records; i++) {
			producer.send(new ProducerRecord<KeySchemaless, ValueSchemaless>(topic, new KeySchemaless(i),
					new ValueSchemaless(new Person(i, "p" + i))));

			count.getAndIncrement();
		}

		producer.close();
	}

	private Producer<KeySchemaless, ValueSchemaless> producerSchemaless() {
		log.debug("producerSchemaless service");

		Properties kafkaProps = new Properties();

		kp.getKafkaProducer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaProducer<>(kafkaProps);
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
		// produce();
		// produceSchemaless();
		// produceSchemaless1();
		// produceSchemaless2();
		// produce1();
		// produce1();
		produce2WithDates();
	}
}
