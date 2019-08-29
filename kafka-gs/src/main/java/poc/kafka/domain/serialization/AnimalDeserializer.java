package poc.kafka.domain.serialization;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import poc.kafka.domain.Animal;

public class AnimalDeserializer implements Deserializer<Animal> {

//	@Override
//	public Animal deserialize(String topic, byte[] data) {
//		Animal animal = null;
//
//		try (ByteArrayInputStream bis = new ByteArrayInputStream(data); ObjectInput in = new ObjectInputStream(bis)) {
//			animal = new Animal();
//			animal.readExternal(in);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return animal;
//	}

	@Override
	public Animal deserialize(String topic, byte[] data) {
		return SerializationUtils.deserialize(data);
	}

}
