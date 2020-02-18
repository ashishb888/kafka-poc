package poc.kafka.domain.serialization;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import poc.kafka.domain.Customer;

public class CustomerDeserializer implements Deserializer<Customer> {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Customer deserialize(String topic, byte[] data) {
		if (data == null)
			return null;

		try {
			return objectMapper.readValue(data, Customer.class);
		} catch (IOException e) {
			throw new SerializationException("Error while deserializing object from JSON", e);
		}
	}
}
