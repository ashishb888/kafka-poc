package poc.kafka.service;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import poc.kafka.domain.Employee;
import poc.kafka.domain.serialization.EmployeeDeserializer;
import poc.kafka.domain.serialization.EmployeeSerializer;

@Service
@Log
public class ExternalizableService {

	private void serialization() {
		log.info("serialization service");

		Employee emp = new Employee("Ashish", 21);
		log.info("emp: " + emp);

		byte[] empBytes = new EmployeeSerializer().serialize("", emp);
		log.info("empBytes: " + empBytes);

		Employee emp1 = new EmployeeDeserializer().deserialize("", empBytes);
		log.info("emp1: " + emp1);
	}

	public void main() {
		log.info("main service");

		serialization();
	}
}
