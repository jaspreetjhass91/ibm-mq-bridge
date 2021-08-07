package com.mq.poc.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(value = { RabbitMqProperties.class })
public class RabbitConfig {

	@Autowired
	private RabbitMqProperties rabbitMqProperties;

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setAddresses(rabbitMqProperties.getConnectionProperties().getAddresses());
		cachingConnectionFactory.setVirtualHost(rabbitMqProperties.getConnectionProperties().getVirtualHost());
		cachingConnectionFactory.setUsername(rabbitMqProperties.getConnectionProperties().getUser());
		cachingConnectionFactory.setPassword(rabbitMqProperties.getConnectionProperties().getPassword());
		return cachingConnectionFactory;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactoryA(
			final CachingConnectionFactory cachingConnectionFactory) {
		final SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		simpleRabbitListenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
		return simpleRabbitListenerContainerFactory;
	}
	
	@Bean
	public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactoryB(
			final CachingConnectionFactory cachingConnectionFactory) {
		final SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		simpleRabbitListenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
		return simpleRabbitListenerContainerFactory;
	}
	
	@Bean
	public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactoryC(
			final CachingConnectionFactory cachingConnectionFactory) {
		final SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		simpleRabbitListenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
		return simpleRabbitListenerContainerFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final CachingConnectionFactory cachingConnectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
		rabbitTemplate.setChannelTransacted(true);
		rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
		// jackson2JsonMessageConverter.setClassMapper(classMapper());
		return jackson2JsonMessageConverter;
	}

	@Bean("transactionManager")
	public RabbitTransactionManager transactionManager(final ConnectionFactory connectionFactory) {
		RabbitTransactionManager rabbitTransactionManager = new RabbitTransactionManager(connectionFactory);
		return rabbitTransactionManager;
	}

}
