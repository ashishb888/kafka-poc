package poc.kafka.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Log
public class Animal implements Externalizable {
	public String name;

	public void whoAmI() {
		log.info("I am an Animal");
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		name = (String) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(name);
	}
}
