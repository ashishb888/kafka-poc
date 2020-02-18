package poc.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.service.KafkaService;

/**
 * @author ashishb888
 */

@SpringBootApplication
@Slf4j
public class KafkaStreamsApp implements CommandLineRunner {
	@Autowired
	private KafkaService kss;

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsApp.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.debug("run service");

		kss.main();
	}

}
