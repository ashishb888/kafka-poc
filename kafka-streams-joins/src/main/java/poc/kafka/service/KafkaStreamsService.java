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
	@Autowired
	private ProducerService ps;
	@Autowired
	private LeftJoinService ljs;
	@Autowired
	private RightJoinService rjs;

	public void main() {
		log.debug("main service");

		ps.main();
		// ijs.main();
		// ljs.main();
		rjs.main();
	}
}
