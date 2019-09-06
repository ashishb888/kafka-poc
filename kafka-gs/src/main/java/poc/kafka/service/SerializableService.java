package poc.kafka.service;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import poc.kafka.domain.Employee2;
import poc.kafka.domain.serialization.Employee2Deserializer;
import poc.kafka.domain.serialization.Employee2Serializer;

@Service
@Log
@SuppressWarnings("resource")
public class SerializableService {

	private void serialization() {
		log.info("serialization service");

		Employee2 emp = new Employee2("Ashish", 21);
		log.info("emp: " + emp);

		byte[] empBytes = new Employee2Serializer().serialize("", emp);
		log.info("empBytes: " + empBytes);

		Employee2 emp1 = new Employee2Deserializer().deserialize("", empBytes);
		log.info("emp1: " + emp1);
	}

	public void main() {
		log.info("main service");

		serialization();
	}
}
