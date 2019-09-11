package poc.kafka.domain.serialization;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import poc.kafka.domain.Animal;

public class AnimalSerde implements Serde<Animal> {

	@Override
	public Serializer<Animal> serializer() {
		return new AnimalSerializer();
	}

	@Override
	public Deserializer<Animal> deserializer() {
		return new AnimalDeserializer();
	}

}
