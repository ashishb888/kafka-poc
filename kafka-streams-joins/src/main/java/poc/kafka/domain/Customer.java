package poc.kafka.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Customer implements Externalizable {
	private long customerId;
	private long orderId;
	private String customerName;
	private String city;
	private String country;

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(customerId);
		out.writeLong(orderId);
		out.writeObject(customerName);
		out.writeObject(city);
		out.writeObject(country);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		customerId = in.readLong();
		orderId = in.readLong();
		customerName = (String) in.readObject();
		city = (String) in.readObject();
		country = (String) in.readObject();
	}
}
