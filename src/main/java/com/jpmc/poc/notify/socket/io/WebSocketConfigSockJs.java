package com.jpmc.poc.notify.socket.io;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.jpmc.poc.notify.appconfig.ConfigurationBean;
import com.jpmc.poc.notify.handlers.HttpHandshakeInterceptor;
import com.jpmc.poc.notify.util.StringConstants;

@Configuration
@EnableWebSocketMessageBroker
@DependsOn("config")
public class WebSocketConfigSockJs extends AbstractWebSocketMessageBrokerConfigurer {
	private static final Logger logger = Logger.getLogger(WebSocketConfigSockJs.class);

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		logger.info("Enabling message broker for spring");
		config.enableSimpleBroker("/topic");
		logger.info("Enabling application prefix for the destinations");
		config.setApplicationDestinationPrefixes("/app");
	}

	public void registerStompEndpoints(StompEndpointRegistry registry) {
		logger.info("Registering stomp endpoints for allowed origins:" + ConfigurationBean.config.get(StringConstants.ORIGIN));
		registry.addEndpoint("/pns").setAllowedOrigins(ConfigurationBean.config.get(StringConstants.ORIGIN)).withSockJS();
	}
}
