package poc.kafka.domain;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Order implements Externalizable {
	private long orderId;
	private long customerId;
	private long employeeID;
	private Date orderDate;
	private long shipperID;

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(orderId);
		out.writeLong(customerId);
		out.writeLong(employeeID);
		out.writeObject(orderDate);
		out.writeLong(shipperID);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		orderId = in.readLong();
		customerId = in.readLong();
		employeeID = in.readLong();
		orderDate = (Date) in.readObject();
		shipperID = in.readLong();
	}
}
