package com.mq.poc.config;

import javax.jms.JMSException;

import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(value = { IbmMqProperties.class })
public class IbmQueueConfig {

	@Autowired
	private IbmMqProperties ibmMqProperties;

	@Scope("prototype")
	@Bean
	public javax.jms.ConnectionFactory mqConnectionFactory() throws JMSException {
		final MQConnectionFactory mqConnectionFactory = new MQConnectionFactory();
		mqConnectionFactory.setConnectionNameList(ibmMqProperties.getConnectionProperties().getIbmMQConnectionString());
		mqConnectionFactory.setChannel(ibmMqProperties.getConnectionProperties().getChannel());
		mqConnectionFactory.setQueueManager(ibmMqProperties.getConnectionProperties().getQueueManager());
		mqConnectionFactory.put(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
		mqConnectionFactory.put(WMQConstants.USERID, ibmMqProperties.getConnectionProperties().getUser());
		mqConnectionFactory.put(WMQConstants.PASSWORD, ibmMqProperties.getConnectionProperties().getPassword());
		mqConnectionFactory.put(WMQConstants.WMQ_CONNECTION_MODE, 1);
		return mqConnectionFactory;
	}

	@Primary
	@Bean("jmsPoolConnectionFactoryA")
	public javax.jms.ConnectionFactory jmsPoolConnectionFactoryA(final javax.jms.ConnectionFactory mqConnectionFactory)
			throws JMSException {
		log.warn("mqconnectionfactory is {}", mqConnectionFactory);
		final JmsPoolConnectionFactory jmsPoolConnectionFactory = new JmsPoolConnectionFactory();
		jmsPoolConnectionFactory.setConnectionFactory(mqConnectionFactory);
		jmsPoolConnectionFactory.setBlockIfSessionPoolIsFull(ibmMqProperties.getQueueA().getBlockIfFull());
		// jmsPoolConnectionFactory.setBlockIfSessionPoolIsFullTimeout(cloudpropA.getBlockIfFullTimeout());
		// jmsPoolConnectionFactory.setConnectionCheckInterval(cloudpropA.getTimeBetweenExpirationCheck());
		jmsPoolConnectionFactory.setConnectionIdleTimeout(ibmMqProperties.getQueueA().getIdleTimeout());
		jmsPoolConnectionFactory.setMaxConnections(ibmMqProperties.getQueueA().getMaxConnections());
		jmsPoolConnectionFactory.setMaxSessionsPerConnection(ibmMqProperties.getQueueA().getMaxSessionsPerConnection());
		return jmsPoolConnectionFactory;
	}

	@Bean("jmsPoolConnectionFactoryB")
	public javax.jms.ConnectionFactory jmsPoolConnectionFactoryB(
			final javax.jms.ConnectionFactory mqConnectionFactory) throws JMSException {
		log.warn("mqconnectionfactory is {}", mqConnectionFactory);
		final JmsPoolConnectionFactory jmsPoolConnectionFactory = new JmsPoolConnectionFactory();
		jmsPoolConnectionFactory.setConnectionFactory(mqConnectionFactory);
		jmsPoolConnectionFactory.setBlockIfSessionPoolIsFull(ibmMqProperties.getQueueB().getBlockIfFull());
		// jmsPoolConnectionFactory.setBlockIfSessionPoolIsFullTimeout(cloudpropB.getBlockIfFullTimeout());
		// jmsPoolConnectionFactory.setConnectionCheckInterval(cloudpropB.getTimeBetweenExpirationCheck());
		jmsPoolConnectionFactory.setConnectionIdleTimeout(ibmMqProperties.getQueueB().getIdleTimeout());
		jmsPoolConnectionFactory.setMaxConnections(ibmMqProperties.getQueueB().getMaxConnections());
		jmsPoolConnectionFactory.setMaxSessionsPerConnection(ibmMqProperties.getQueueB().getMaxSessionsPerConnection());
		return jmsPoolConnectionFactory;
	}

	@Bean("jmsPoolConnectionFactoryC")
	public javax.jms.ConnectionFactory jmsPoolConnectionFactoryC(
			final javax.jms.ConnectionFactory mqConnectionFactory) throws JMSException {
		log.warn("mqconnectionfactory is {}", mqConnectionFactory);
		final JmsPoolConnectionFactory jmsPoolConnectionFactory = new JmsPoolConnectionFactory();
		jmsPoolConnectionFactory.setConnectionFactory(mqConnectionFactory);
		jmsPoolConnectionFactory.setBlockIfSessionPoolIsFull(ibmMqProperties.getQueueC().getBlockIfFull());
		// jmsPoolConnectionFactory.setBlockIfSessionPoolIsFullTimeout(cloudpropC.getBlockIfFullTimeout());
		// jmsPoolConnectionFactory.setConnectionCheckInterval(cloudpropC.getTimeBetweenExpirationCheck());
		jmsPoolConnectionFactory.setConnectionIdleTimeout(ibmMqProperties.getQueueC().getIdleTimeout());
		jmsPoolConnectionFactory.setMaxConnections(ibmMqProperties.getQueueC().getMaxConnections());
		jmsPoolConnectionFactory.setMaxSessionsPerConnection(ibmMqProperties.getQueueC().getMaxSessionsPerConnection());
		return jmsPoolConnectionFactory;
	}

	@Bean("defaultJmsListenerContainerFactoryA")
	public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactoryA(
			@Qualifier("jmsPoolConnectionFactoryA") final javax.jms.ConnectionFactory jmsPoolConnectionFactoryA,
			final DefaultJmsListenerContainerFactoryConfigurer configurer,
			final RabbitTransactionManager transactionManager) {
		log.warn("jmsPoolconnectionfactory is {}", jmsPoolConnectionFactoryA);
		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setMessageConverter(jacksonJmsMessageConverter());
		// factory.setErrorHandler(customErrorHandler);
		factory.setPubSubDomain(Boolean.FALSE);
		factory.setSessionTransacted(true);
		// factory.setSessionAcknowledgeMode(AcknowledgeMode.CLIENT.getMode());
		if (ibmMqProperties.getQueueA().getExponentialBackoff()) {
			final ExponentialBackOff exponentialBackOff = new ExponentialBackOff(ibmMqProperties.getQueueA().getReconnectInterval(),
					ibmMqProperties.getQueueA().getMultiplier());
			exponentialBackOff.setMaxInterval(ibmMqProperties.getQueueA().getMaxInterval());
			factory.setBackOff(exponentialBackOff);
		} else {
			final FixedBackOff fixedBackOff = new FixedBackOff();
			fixedBackOff.setInterval(ibmMqProperties.getQueueA().getReconnectInterval());
			// fixedBackOff.setMaxAttempts(3);
			factory.setBackOff(fixedBackOff);
		}
		factory.setTransactionManager(transactionManager);
		configurer.configure(factory, jmsPoolConnectionFactoryA);
		return factory;
	}

	@Bean("defaultJmsListenerContainerFactoryB")
	public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactoryB(
			@Qualifier("jmsPoolConnectionFactoryB") final javax.jms.ConnectionFactory jmsPoolConnectionFactoryB,
			final DefaultJmsListenerContainerFactoryConfigurer configurer,
			final RabbitTransactionManager transactionManager) {
		log.warn("jmsPoolconnectionfactory is {}", jmsPoolConnectionFactoryB);
		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setMessageConverter(jacksonJmsMessageConverter());
		// factory.setErrorHandler(customErrorHandler);
		factory.setPubSubDomain(Boolean.FALSE);
		factory.setSessionTransacted(true);
		// factory.setSessionAcknowledgeMode(AcknowledgeMode.CLIENT.getMode());
		if (ibmMqProperties.getQueueB().getExponentialBackoff()) {
			final ExponentialBackOff exponentialBackOff = new ExponentialBackOff(ibmMqProperties.getQueueB().getReconnectInterval(),
					ibmMqProperties.getQueueB().getMultiplier());
			exponentialBackOff.setMaxInterval(ibmMqProperties.getQueueB().getMaxInterval());
			factory.setBackOff(exponentialBackOff);
		} else {
			final FixedBackOff fixedBackOff = new FixedBackOff();
			fixedBackOff.setInterval(ibmMqProperties.getQueueB().getReconnectInterval());
			// fixedBackOff.setMaxAttempts(3);
			factory.setBackOff(fixedBackOff);
		}
		factory.setTransactionManager(transactionManager);
		configurer.configure(factory, jmsPoolConnectionFactoryB);
		return factory;
	}

	@Bean("defaultJmsListenerContainerFactoryC")
	public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactoryC(
			@Qualifier("jmsPoolConnectionFactoryC") final javax.jms.ConnectionFactory jmsPoolConnectionFactoryC,
			final DefaultJmsListenerContainerFactoryConfigurer configurer,
			final RabbitTransactionManager transactionManager) {
		log.warn("jmsPoolconnectionfactory is {}", jmsPoolConnectionFactoryC);
		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setMessageConverter(jacksonJmsMessageConverter());
		// factory.setErrorHandler(customErrorHandler);
		factory.setPubSubDomain(Boolean.FALSE);
		factory.setSessionTransacted(true);
		// factory.setSessionAcknowledgeMode(AcknowledgeMode.CLIENT.getMode());
		if (ibmMqProperties.getQueueC().getExponentialBackoff()) {
			final ExponentialBackOff exponentialBackOff = new ExponentialBackOff(ibmMqProperties.getQueueC().getReconnectInterval(),
					ibmMqProperties.getQueueC().getMultiplier());
			exponentialBackOff.setMaxInterval(ibmMqProperties.getQueueC().getMaxInterval());
			factory.setBackOff(exponentialBackOff);
		} else {
			final FixedBackOff fixedBackOff = new FixedBackOff();
			fixedBackOff.setInterval(ibmMqProperties.getQueueC().getReconnectInterval());
			// fixedBackOff.setMaxAttempts(3);
			factory.setBackOff(fixedBackOff);
		}
		factory.setTransactionManager(transactionManager);
		configurer.configure(factory, jmsPoolConnectionFactoryC);
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplate(final javax.jms.ConnectionFactory mqConnectionFactory) throws JMSException {
		final JmsTemplate jmsTemplate = new JmsTemplate(mqConnectionFactory);
		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
		return jmsTemplate;
	}

	@Bean
	public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
		final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

}
