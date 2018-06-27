package com.jpmc.poc.notify.controller.service.impl;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {
	
	private static final Logger logger = Logger.getLogger(MessageReceiver.class);
	private static final String QUEUE = "public";
	
	@JmsListener(destination = QUEUE)
    public void receiveMessage(final Message message) throws JMSException {
		logger.info("Worker identified for routing" + Thread.currentThread().getName());
		Thread thread = new Thread(new RouterThreadService(message));
		thread.start();
    }

}
