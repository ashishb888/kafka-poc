package poc.kafka.interceptor;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Customer;

@Slf4j
public class CInterceptor implements ConsumerInterceptor<Long, Customer> {
	@Override
	public void configure(Map<String, ?> configs) {

	}

	@Override
	public ConsumerRecords<Long, Customer> onConsume(ConsumerRecords<Long, Customer> records) {
		records.forEach(r -> {
			log.debug("r#value: " + r.value());
			r.value().setCity("Updated from consumer interceptor");
		});

		return records;
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
		log.debug("offsets: " + offsets);
	}

	@Override
	public void close() {

	}

}
