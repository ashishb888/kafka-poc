package poc.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ashishb888
 */

@Service
@Slf4j
@SuppressWarnings({ "unused" })
public class KafkaService {

	@Autowired
	private FileSourceConnectorService fscs;
	@Autowired
	private IgniteSourceConnectorService iscs;

	public void main() {
		log.debug("main service");

		// fscs.main();
		iscs.main();
	}
}
