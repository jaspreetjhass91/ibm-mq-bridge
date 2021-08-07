package com.mq.poc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "rabbit.mq")
public class RabbitMqProperties {

	private CommonRabbitPoolConfig queueA;
	private CommonRabbitPoolConfig queueB;
	private CommonRabbitPoolConfig queueC;
	private RabbitMqConnectionProperties connectionProperties;
	
	@Data
	static class CommonRabbitPoolConfig{
		private String autoStartup;
		private String concurrency;
	}
	
	@Data
	static class RabbitMqConnectionProperties{
		private String addresses;
		private String virtualHost;
		private String user;
		private String password;
	}

}
