package com.ediweb.camel.routes;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ediweb.camel.transformers.TransformToOutputFormat;

@Component
public class Route extends RouteBuilder {

	@Autowired
	private TransformToOutputFormat outputFormatTransformer;

	@Override
	public void configure() throws Exception {
		from("file:{{inputDir}}?ext=xml&delete=true").to("log:Найден документ?level=INFO").process(outputFormatTransformer).to("file:{{outputDir}}");
	}

}
