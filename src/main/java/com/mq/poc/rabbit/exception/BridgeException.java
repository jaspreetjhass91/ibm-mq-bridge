package com.mq.poc.rabbit.exception;

public class BridgeException extends RuntimeException {

	private static final long serialVersionUID = -3000610762701601637L;

	public BridgeException(final String cause) {
		super(cause);
	}

}
