package com.mq.poc;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableRabbit
@EnableSwagger2
@SpringBootApplication
public class MqBridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqBridgeApplication.class, args);
	}

}