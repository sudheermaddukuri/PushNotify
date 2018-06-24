package com.ca.notify.controller.service.impl;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import com.ca.notify.appconfig.ConfigurationBean;
import com.ca.notify.util.StringConstants;

@Configuration
@EnableJms
public class MessageBusListenerConfiguration {
	private static final Logger logger = Logger.getLogger(MessageBusListenerConfiguration.class);

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
    	logger.info("Creating jms connection factory bean");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency(ConfigurationBean.config.get(StringConstants.CONCURR_ROUTER));
        return factory;
    }
    
    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
    	logger.info("Creating activemq connection bean");
    	ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://camessagebus");
        return connectionFactory;
    }
}
