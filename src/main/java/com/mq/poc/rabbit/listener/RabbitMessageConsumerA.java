package com.mq.poc.rabbit.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMessageConsumerA {

	@RabbitListener(queues = "queueA", containerFactory = "simpleRabbitListenerContainerFactoryA", concurrency = "${rabbit.mq.queue-a.concurrency}", autoStartup = "${rabbit.mq.queue-a.auto-startup}")
	public void onReceive(Message message) {
		byte[] body = message.getBody();
		log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Message Received by Rabbit listener A is : {}",
				new String(body));
	}

}
