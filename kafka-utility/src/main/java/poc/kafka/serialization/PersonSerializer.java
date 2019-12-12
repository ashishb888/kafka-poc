package poc.kafka.serialization;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.util.SerializationUtils;

import poc.kafka.domain.Person;

public class PersonSerializer implements Serializer<Person> {

	@Override
	public byte[] serialize(String topic, Person data) {
		if (data == null)
			return null;

		try {
			return SerializationUtils.serialize(data);
		} catch (Exception e) {
			throw new SerializationException("Error while serializing object", e);
		}

	}

}
