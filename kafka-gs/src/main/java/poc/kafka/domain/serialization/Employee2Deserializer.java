package poc.kafka.domain.serialization;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import poc.kafka.domain.Employee2;

public class Employee2Deserializer implements Deserializer<Employee2> {

	@Override
	public Employee2 deserialize(String topic, byte[] data) {
		return SerializationUtils.deserialize(data);
	}

}
