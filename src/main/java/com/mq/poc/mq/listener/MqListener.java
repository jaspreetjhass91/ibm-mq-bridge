package com.mq.poc.mq.listener;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mq.poc.domain.CustomTextMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MqListener {

	@Autowired
	private ObjectMapper objectMapper;

	protected void receiveMessage(final Message message)
			throws JsonMappingException, JsonProcessingException, JMSException {
		log.info("enter into receiveMessage method with parameters : {} ", message);
		final CustomTextMessage customTextMessage = objectMapper.readValue(message.getBody(String.class),
				CustomTextMessage.class);
		publish(customTextMessage);
		log.info("exit from receiveMessage method");
	}

	abstract void publish(CustomTextMessage customTextMessage);

}
