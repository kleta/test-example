package com.ediweb.camel.routes;

import javax.annotation.PostConstruct;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ediweb.camel.transformers.MessageAggregation;
import com.ediweb.camel.transformers.TransformToOutputFormat;

@Component
public class Route extends RouteBuilder {

	@Autowired
	private TransformToOutputFormat outputFormatTransformer;
	
	@Autowired
	private MessageAggregation agregator;

	@PostConstruct
	public void init() {
		System.out.println();
	}

	@Override
	public void configure() throws Exception {
		from("file:{{inputDir}}").to("log:Найден документ?level=INFO").multicast(agregator).to("direct:original", "direct:transform").end();

		from("direct:original").to("log:original?level=INFO");
		from("direct:transform").to("log:Поступило сообщение для преобразования?level=INFO").doTry()
				.process(outputFormatTransformer).doCatch(Throwable.class).to("log:Ошибка преобразования?level=ERROR");
	}

}
