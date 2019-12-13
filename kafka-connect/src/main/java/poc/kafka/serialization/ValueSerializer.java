package poc.kafka.serialization;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import poc.kafka.domain.Value;

public class ValueSerializer implements Serializer<Value> {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public byte[] serialize(String topic, Value data) {

		if (data == null)
			return null;

		try {
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SerializationException("Error while serializing object to JSON", e);
		}
	}
}
