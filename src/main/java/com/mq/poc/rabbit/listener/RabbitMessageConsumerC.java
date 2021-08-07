package com.mq.poc.rabbit.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMessageConsumerC {

	@RabbitListener(queues = "queueC", containerFactory = "simpleRabbitListenerContainerFactoryC", concurrency = "${rabbit.mq.queue-c.concurrency}", autoStartup = "${rabbit.mq.queue-c.auto-startup}")
	public void onReceive(Message message) {
		byte[] body = message.getBody();
		log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Message Received by Rabbit listener C is : {}",
				new String(body));
	}

}
