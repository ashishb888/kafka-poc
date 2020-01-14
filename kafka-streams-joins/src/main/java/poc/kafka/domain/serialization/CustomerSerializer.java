package poc.kafka.domain.serialization;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.util.SerializationUtils;

import poc.kafka.domain.Customer;

public class CustomerSerializer implements Serializer<Customer> {

	@Override
	public byte[] serialize(String topic, Customer data) {
		return SerializationUtils.serialize(data);
	}
}
