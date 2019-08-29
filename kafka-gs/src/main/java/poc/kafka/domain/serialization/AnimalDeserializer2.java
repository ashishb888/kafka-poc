package poc.kafka.domain.serialization;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import poc.kafka.domain.Animal;

public class AnimalDeserializer2 implements Deserializer<Animal> {

	@Override
	public Animal deserialize(String topic, byte[] data) {
		return SerializationUtils.deserialize(data);
	}

}
