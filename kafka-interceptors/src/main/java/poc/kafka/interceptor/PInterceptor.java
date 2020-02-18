package poc.kafka.interceptor;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.domain.Customer;

@Slf4j
public class PInterceptor implements ProducerInterceptor<Long, Customer> {

	@Override
	public void configure(Map<String, ?> configs) {

	}

	@Override
	public ProducerRecord<Long, Customer> onSend(ProducerRecord<Long, Customer> record) {
		log.debug("record#value: " + record.value());
		record.value().setCity("Updated from Interceptor");
		return record;
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		log.debug("metadata: " + metadata);

		if (exception != null)
			log.error(exception.getMessage(), exception);
	}

	@Override
	public void close() {

	}

}
