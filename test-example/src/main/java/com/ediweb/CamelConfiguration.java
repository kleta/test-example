package com.ediweb;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfiguration extends SingleRouteCamelConfiguration implements InitializingBean {

	@Bean
	public RouteBuilder route() {
		return new RouteBuilder() {
			public void configure() {
				from("jms:queue:autorize").to("jsonrpc2:СБИС.Аутентифицировать")
						.setHeader(Exchange.CONTENT_TYPE, constant("application/json-rpc; charset=utf-8"))
						.to("log:Авторизация?level=INFO").to("https4://online.sbis.ru/auth/service/")
						.to("log:Ответ?level=INFO");

				from("jms:queue:findbyinn")
						// .process(new FindByInnRequestProcessor())
						.to("jsonrpc2://Контрагент.СписокОбщийИСПП")
						.setHeader(Exchange.CONTENT_TYPE, constant("application/json-rpc; charset=utf-8"))
						.to("log:Поиск по ИНН?level=INFO");

			}
		};
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

}
