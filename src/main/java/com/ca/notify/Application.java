package com.ca.notify;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import com.ca.notify.controller.service.impl.BrokerServiceImpl;

/**
 * Startup class to boot the notification service. It does a sequential launch
 * of the broker and the the service. The embedded broker is an activemq broker
 * and is starts with the stomp protocol by default.
 * 
 */

@SpringBootApplication
public class Application extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer{
	
	private static final Logger logger = Logger.getLogger(Application.class);
	
	@Value("#{'${contextPath:\"\"}'}")
	private String contextPath;
	
	public static void main(String[] args) {
		logger.info("Starting ca-push-notification server");
		BrokerServiceImpl.run();
		
		//TODO register shutdown hook
		//SpringApplication app = new SpringApplication(Application.class);
		//app.setRegisterShutdownHook(true);
		//TODO clean up resources on shutdown hook
		SpringApplication.run(Application.class, args);
		logger.info("ca-push-notification app started");
		
		logger.info("Ca notification service up and running");
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		logger.info("Starting ca-push-notification server");
		BrokerServiceImpl.run();
		
		return application.sources(Application.class);
    }
	
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setContextPath(contextPath);
		
	}
}
