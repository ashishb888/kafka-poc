package poc.kafka.domain;

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
public class Customer {
	private long customerId;
	private long orderId;
	private String customerName;
	private String city;
	private String country;
}
