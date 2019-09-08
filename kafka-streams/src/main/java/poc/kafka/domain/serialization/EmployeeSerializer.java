package poc.kafka.domain.serialization;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.kafka.common.serialization.Serializer;

import poc.kafka.domain.Employee;

public class EmployeeSerializer implements Serializer<Employee> {

	@Override
	public byte[] serialize(String topic, Employee data) {
		byte[] bArr = null;

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
			data.writeExternal(out);
			bArr = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bArr;
	}

}
