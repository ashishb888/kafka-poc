package poc.kafka.domain.serialization;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

import poc.kafka.domain.Animal;

public class AnimalSerializer2 implements Serializer<Animal> {

	@Override
	public byte[] serialize(String topic, Animal data) {
		return SerializationUtils.serialize(data);
	}

}
