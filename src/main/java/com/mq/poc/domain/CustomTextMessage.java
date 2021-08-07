package com.mq.poc.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomTextMessage implements Serializable {

	private static final long serialVersionUID = -4391478316834394400L;

	private long id;
	private String message;

}
