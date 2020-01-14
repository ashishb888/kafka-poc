package poc.kafka.domain.serialization;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.util.SerializationUtils;

import poc.kafka.domain.Order;

public class OrderSerializer implements Serializer<Order> {

	@Override
	public byte[] serialize(String topic, Order data) {
		return SerializationUtils.serialize(data);
	}
}
