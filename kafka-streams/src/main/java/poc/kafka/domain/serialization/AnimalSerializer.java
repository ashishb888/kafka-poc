package poc.kafka.domain.serialization;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

import poc.kafka.domain.Animal;

@SuppressWarnings("unused")
public class AnimalSerializer implements Serializer<Animal> {

//	@Override
//	public byte[] serialize(String topic, Animal data) {
//		byte[] bArr = null;
//
//		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
//			data.writeExternal(out);
//			bArr = bos.toByteArray();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return bArr;
//	}

	@Override
	public byte[] serialize(String topic, Animal data) {
		return SerializationUtils.serialize(data);
	}

}
