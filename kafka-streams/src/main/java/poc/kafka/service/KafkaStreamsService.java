package poc.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ashishb888
 */

@Service
@Slf4j
@SuppressWarnings("unused")
public class KafkaStreamsService {

	@Autowired
	private PipeService ps;
	@Autowired
	private LineSplitService lss;
	@Autowired
	private WordCountService wcs;

	public void main() {
		log.info("main service");

		// ps.main();
		// lss.main();
		wcs.main();
	}
}
