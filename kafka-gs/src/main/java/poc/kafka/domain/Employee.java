package poc.kafka.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee implements Externalizable {

	private String name;
	private int age;

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.age = in.readInt();
		this.name = (String) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(age);
		out.writeObject(name);
	}

}
