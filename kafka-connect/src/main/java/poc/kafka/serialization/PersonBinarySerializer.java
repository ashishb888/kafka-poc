package poc.kafka.serialization;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import poc.kafka.domain.PersonBinary;

public class PersonBinarySerializer implements Serializer<PersonBinary> {

	@Override
	public byte[] serialize(String topic, PersonBinary data) {

		if (data == null)
			return null;

		try {
			return SerializationUtils.serialize(data);
		} catch (Exception e) {
			throw new SerializationException("Error while serializing object to JSON", e);
		}
	}
}
