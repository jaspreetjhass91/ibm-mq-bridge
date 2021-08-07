package com.mq.poc.rest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mq.poc.rabbit.exception.BridgeException;
import com.mq.poc.util.AppConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/bridge")
public class RabbitMessageLoader {

	private static final String HELLO_MESSAGE = "hello message";
	private static final String IBM_MQ_ROUTE_A = "ibm.mq.route.A";
	private static final String EXCHANGE_A = "exchangeA";

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@GetMapping("healthCheck")
	public String healthCheck() {
		log.info("inside healthCheck");
		return AppConstant.OK;
	}

	@GetMapping("publishMessage")
	public String sendMessage() {
		try {
			rabbitTemplate.convertAndSend(EXCHANGE_A, IBM_MQ_ROUTE_A, HELLO_MESSAGE);
		} catch (Exception exception) {
			throw new BridgeException("Error while publishing message to rabbit queue");
		}
		return "sent";
	}
}
