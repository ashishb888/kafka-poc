package poc.kafka.service;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import poc.kafka.properties.KafkaProperties;

/**
 * @author ashishb888
 */

@Service
@Slf4j
public class KafkaService {

	@Autowired
	private KafkaProperties kp;

	private void createTopic() {
		log.debug("createTopic service");

		AdminClient adminClient = adminClient();

		NewTopic topic = new NewTopic("ac-test", 10, (short) 1);
		CreateTopicsOptions topicsOptions = new CreateTopicsOptions();
		CreateTopicsResult topicsResult = adminClient.createTopics(Collections.singleton(topic), topicsOptions);
		try {
			topicsResult.all().get();
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getMessage(), e);
		}
	}

	private AdminClient adminClient() {
		log.debug("adminClient service");

		Properties kafkaProps = new Properties();

		kp.getKafkaConsumer().forEach((k, v) -> {
			log.debug("k: " + k + ", v: " + v);
			kafkaProps.put(k, v);
		});

		return AdminClient.create(kafkaProps);
	}

	private void start() {
		log.debug("start service");

		createTopic();
	}

	public void main() {
		log.debug("main service");

		start();
	}
}
