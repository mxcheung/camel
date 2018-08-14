package com.maxcheung.camelsimple.route.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxcheung.camelsimple.service.RouteServiceImpl;

public class KafkaConsumerProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerProcessor.class);

	private static final String APPLICATIONID = "applicationId";

	public void process(Exchange exchange) throws Exception {
		String messageKey = "";
		Message message = exchange.getIn();
		Integer partitionId = (Integer) message
				.getHeader(KafkaConstants.PARTITION);
		String topicName = (String) message
				.getHeader(KafkaConstants.TOPIC);
		if (message.getHeader(KafkaConstants.KEY) != null)
			messageKey = (String) message
					.getHeader(KafkaConstants.KEY);
		Object data = message.getBody();

		Object headers = message.getHeader(KafkaConstants.HEADERS);
		LOG.info("topicName :: "
				+ topicName + " partitionId :: "
				+ partitionId + " messageKey :: "
				+ messageKey + " message :: "
				+ data + "\n");
	}

}