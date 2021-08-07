package com.mq.poc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "ibm.mq")
public class IbmMqProperties {

	private CommonPoolConfig queueA;
	private CommonPoolConfig queueB;
	private CommonPoolConfig queueC;
	private IBMMqConnectionProperties connectionProperties;
	
	@Data
	static class CommonPoolConfig{
		private boolean enabled;
		private String concurrency;
		private Integer maxConnections;
		private Integer idleTimeout;
		private Integer maxSessionsPerConnection;
		private Boolean blockIfFull;
		private Long blockIfFullTimeout;
		private Long reconnectInterval;
		private Long maxInterval;
		private Float multiplier;
		private Boolean exponentialBackoff;
	}
	
	@Data
	static class IBMMqConnectionProperties{
		private String ibmMQConnectionString;
		private String queueManager;
		private String channel;
		private String user;
		private String password;
	}
	
	
}