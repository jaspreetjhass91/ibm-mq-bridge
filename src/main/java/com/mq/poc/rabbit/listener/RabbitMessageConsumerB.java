package com.mq.poc.rabbit.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMessageConsumerB {

	@RabbitListener(queues = "queueB", containerFactory = "simpleRabbitListenerContainerFactoryB", concurrency = "${rabbit.mq.queue-b.concurrency}", autoStartup = "${rabbit.mq.queue-b.auto-startup}")
	public void onReceive(Message message) {
		byte[] body = message.getBody();
		log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Message Received by Rabbit listener B is : {}",
				new String(body));
	}

}
