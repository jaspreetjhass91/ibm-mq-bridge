/**
 * 
 */
package com.mq.poc.mq.listener;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mq.poc.domain.CustomTextMessage;
import com.mq.poc.util.AppConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jaspreet.singh
 *
 */
@Slf4j
@Component
public class IbmMessageConsumerC extends MqListener {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Transactional(transactionManager = AppConstant.TRANSACTION_MANAGER)
	@JmsListener(destination = AppConstant.QUEUEC, containerFactory = AppConstant.CONNECTION_FACTORY_C, concurrency = "${ibm.mq.queue-a.concurrency}")
	public void receiveMessage(Message message) throws JMSException, JsonMappingException, JsonProcessingException {
		super.receiveMessage(message);
	}

	@Override
	void publish(CustomTextMessage customTextMessage) {
		rabbitTemplate.convertAndSend(AppConstant.EXCHANGE_C, AppConstant.ROUTE_KEY_C, customTextMessage);
		log.info("message : {} sent successfully to queue C : {} ", customTextMessage);
	}
}
