package com.jpmc.poc.notify;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

import com.jpmc.poc.notify.controller.service.impl.BrokerServiceImpl;

/**
 * Startup class to boot the notification service. It does a sequential launch
 * of the broker and the the service. The embedded broker is an activemq broker
 * and is starts with the stomp protocol by default.
 * 
 */

@SpringBootApplication
public class Application implements EmbeddedServletContainerCustomizer{
	
	private static final Logger logger = Logger.getLogger(Application.class);
	
	@Value("#{'${contextPath:\"\"}'}")
	private String contextPath;
	
	public static void main(String[] args) {
		logger.info("Starting push-notification server");
		BrokerServiceImpl.run();
		
		//TODO register shutdown hook
		//SpringApplication app = new SpringApplication(Application.class);
		//app.setRegisterShutdownHook(true);
		//TODO clean up resources on shutdown hook
		
		System.setProperty("server.servlet.context-path", "/notification");
		SpringApplication.run(Application.class, args);
		logger.info("push-notification app started");
		
		logger.info("notification service up and running");
	}

	public void customize(ConfigurableEmbeddedServletContainer container) {
		// TODO Auto-generated method stub
		container.setContextPath(contextPath);
	}
}
