package poc.kafka.service;

import java.nio.ByteBuffer;
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
import poc.kafka.properties.KafkaProperties;

@Service
@Slf4j
@SuppressWarnings({ "unused" })
public class ConsumerService {

	@Autowired
	private KafkaProperties kp;
	private AtomicInteger count = new AtomicInteger(0);

	private void consume() {
		log.debug("consume service");

		String[] partitions = kp.getMetaData().get("partitions").split("\\,");
		log.debug("partitions: " + Arrays.toString(partitions));
		int nPartitions = partitions.length;
		log.debug("nPartitions: " + nPartitions);

		ExecutorService es = Executors.newFixedThreadPool(nPartitions);
		AtomicInteger ai = new AtomicInteger(0);

		String topic = kp.getMetaData().get("topic");
		log.debug("topic: " + topic);

		for (int i = 0; i < nPartitions; i++) {
			es.submit(() -> {
				Consumer<Short, ByteBuffer> consumer = consumer();

				int partition = Integer.valueOf(partitions[ai.getAndIncrement()]);

				log.debug("partition: " + partition);

				TopicPartition tp = new TopicPartition(topic, partition);
				consumer.assign(Arrays.asList(tp));
				consumer.seekToBeginning(Arrays.asList(tp));
				// consumer.assignment();

				while (true) {
					ConsumerRecords<Short, ByteBuffer> records = consumer.poll(Duration.ofMillis(10));

					for (ConsumerRecord<Short, ByteBuffer> record : records) {
						ByteBuffer bb = record.value();

						// 1
						int astSeqNmbr = (bb.getInt(40));

						// 2
						String astActySymblName = (bytesToString(bb, 44, 10));

						// 3
						String astActySeries = (bytesToString(bb, 54, 2));
						// 4
						short astMktNmbr = (bb.getShort(56));
						// 5
						short astActyTypeNmbr = (bb.getShort(58));

						// 15
						short aeaMachineNmbr = (bb.getShort(434));

						// 16
						short aeagroupId = (bb.getShort(436));

						// 17
						int astGroupSeqNmbr = (bb.getInt(438));

						log.debug("astSeqNmbr: " + astSeqNmbr + ", astActySymblName: " + astActySymblName
								+ ", astActySeries: " + astActySeries + ", astMktNmbr: " + astMktNmbr
								+ ", astActyTypeNmbr: " + astActyTypeNmbr + ", aeaMachineNmbr: " + aeaMachineNmbr
								+ ", aeagroupId: " + aeagroupId + ", astGroupSeqNmbr: " + astGroupSeqNmbr);

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

	public static String bytesToString(ByteBuffer byteBuffer, int offset, int length) {
		byte[] bArr = new byte[length];
		byteBuffer.position(offset);
		byteBuffer.get(bArr, 0, length);

		return new String(bArr);
	}

	private Consumer<Short, ByteBuffer> consumer() {
		log.debug("consumer service");

		Properties kafkaProps = new Properties();

		kp.getKafkaConsumer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return new KafkaConsumer<>(kafkaProps);
	}

	public void main() {
		log.debug("main service");

		consume();
	}
}
