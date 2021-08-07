package com.mq.poc.util;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomErrorHandler implements ErrorHandler {

	@Override
	public void handleError(Throwable t) {
		log.info("Error occured in listener +++++++++++++++++++++++++++++++++++++ : {}", t);
	}

}
