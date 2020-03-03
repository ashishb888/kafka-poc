package poc.kafka.domain.serialization;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import poc.kafka.domain.Order;

public class OrderDeserializer implements Deserializer<Order> {

	@Override
	public Order deserialize(String topic, byte[] data) {
		return SerializationUtils.deserialize(data);
	}
}
