package poc.kafka.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee2 implements Serializable {

	private static final long serialVersionUID = -2464237063624173405L;

	private String name;
	private int age;

}
