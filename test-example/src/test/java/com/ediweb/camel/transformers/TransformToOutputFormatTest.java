package com.ediweb.camel.transformers;

import static org.junit.Assert.fail;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest
@EnableAutoConfiguration
@RunWith(CamelSpringJUnit4ClassRunner.class)
public class TransformToOutputFormatTest {
	@Autowired
	private CamelContext camelContext;

	@Autowired
	private TransformToOutputFormat myProcessor;

	@EndpointInject(uri = "mock:result")
	private MockEndpoint resultEndpoint;

	@EndpointInject(uri = "mock:error")
	private MockEndpoint errorEndpoint;
	
	@Produce(uri = "direct:start")
	private ProducerTemplate template;
	
	@Produce(uri = "direct:error")
	private ProducerTemplate errorTemplate;

	@Before
	public void setUp() throws Exception {
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:start").process(myProcessor).to("mock:result");
				from("direct:error").doTry().process(myProcessor).doCatch(Throwable.class)
			    .to("mock:error");
			}
		});
		camelContext.start();
	}

	@Test
	public void testProcess() {
		 resultEndpoint.expectedMessageCount(1);
		 File testFile=new File("src/test/resources/ORIGINAL_ORDERS.xml");
		 GenericFile<File> f=new GenericFile<>();
		 f.setFile(testFile);
		 template.sendBody(f);
		 resultEndpoint.expectedMessageCount(1);
		 testFile=new File("src/test/resources/pom.xml");
		 f=new GenericFile<>();
		 f.setFile(testFile);
		 errorTemplate.sendBody(f);
		 errorEndpoint.expectedMessageCount(1);
	}

}
