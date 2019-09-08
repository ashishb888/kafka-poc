package poc.kafka.domain.serialization;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import org.apache.kafka.common.serialization.Deserializer;

import poc.kafka.domain.Employee;

public class EmployeeDeserializer implements Deserializer<Employee> {

	@Override
	public Employee deserialize(String topic, byte[] data) {
		Employee employee = null;

		try (ByteArrayInputStream bis = new ByteArrayInputStream(data); ObjectInput in = new ObjectInputStream(bis)) {
			employee = new Employee();
			employee.readExternal(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return employee;
	}

}
