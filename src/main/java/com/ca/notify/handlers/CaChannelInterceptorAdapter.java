package com.ca.notify.handlers;

import org.apache.log4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class CaChannelInterceptorAdapter extends ChannelInterceptorAdapter{
	
	private static final Logger logger = Logger.getLogger(CaChannelInterceptorAdapter.class);
	
	@Override
	  public Message<?> preSend(Message<?> message, MessageChannel channel) {
	    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
	    StompCommand command = accessor.getCommand();
	    logger.info(command);
	    logger.info(accessor.getDestination());
	    return message;
	  }
}
