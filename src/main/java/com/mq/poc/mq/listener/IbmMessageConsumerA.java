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
public class IbmMessageConsumerA extends MqListener {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Transactional(transactionManager = AppConstant.TRANSACTION_MANAGER)
	@JmsListener(destination = AppConstant.QUEUEA, containerFactory = AppConstant.CONNECTION_FACTORY_A, concurrency = "${ibm.mq.queue-a.concurrency}")
	public void receiveMessage(Message message) throws JMSException, JsonMappingException, JsonProcessingException {
		super.receiveMessage(message);
	}

	@Override
	void publish(CustomTextMessage customTextMessage) {
		rabbitTemplate.convertAndSend(AppConstant.EXCHANGE_A, AppConstant.ROUTE_KEY_A, customTextMessage);
		log.info("message : {} sent successfully to queue A : {} ", customTextMessage);
	}
}
