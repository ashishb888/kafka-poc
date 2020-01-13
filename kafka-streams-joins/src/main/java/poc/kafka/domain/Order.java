package poc.kafka.domain;

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
public class Order {
	private long orderId;
	private long customerId;
	private long employeeID;
	private Date orderDate;
	private long shipperID;
}
