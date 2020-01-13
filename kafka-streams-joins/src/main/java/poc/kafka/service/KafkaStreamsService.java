package poc.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ashishb888
 */

@Service
@Slf4j
public class KafkaStreamsService {
	@Autowired
	private InnerJoinService ijs;

	public void main() {
		log.debug("main service");

		ijs.main();
	}
}
