package poc.kafka.domain.serialization;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

import poc.kafka.domain.Employee2;

public class Employee2Serializer implements Serializer<Employee2> {

	@Override
	public byte[] serialize(String topic, Employee2 data) {
		return SerializationUtils.serialize(data);
	}

}
