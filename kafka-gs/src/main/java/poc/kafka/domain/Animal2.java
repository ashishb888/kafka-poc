package poc.kafka.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Log
public class Animal2 implements Serializable {
	private static final long serialVersionUID = 2429297752036622512L;

	public String name;

	public void whoAmI() {
		log.info("I am an Animal");
	}
}
