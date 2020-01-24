package poc.kafka.domain.serialization;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import poc.kafka.domain.Order;

public class OrderDeserializer implements Deserializer<Order> {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Order deserialize(String topic, byte[] data) {
		if (data == null)
			return null;

		try {
			return objectMapper.readValue(data, Order.class);
		} catch (IOException e) {
			throw new SerializationException("Error while deserializing object from JSON", e);
		}
	}
}
