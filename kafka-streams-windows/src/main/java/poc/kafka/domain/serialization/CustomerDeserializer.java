package poc.kafka.domain.serialization;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import poc.kafka.domain.Customer;

public class CustomerDeserializer implements Deserializer<Customer> {

	@Override
	public Customer deserialize(String topic, byte[] data) {
		return SerializationUtils.deserialize(data);
	}
}
