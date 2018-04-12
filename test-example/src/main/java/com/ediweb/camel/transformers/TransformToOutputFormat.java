package com.ediweb.camel.transformers;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class TransformToOutputFormat implements Processor{
	
	

	@Override
	public void process(Exchange message) throws Exception {
		Message inMessage = message.getIn();
		Object body = inMessage.getBody();
		message.setOut(inMessage);
	}

}
