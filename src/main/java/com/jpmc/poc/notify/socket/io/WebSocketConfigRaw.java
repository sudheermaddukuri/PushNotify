package com.jpmc.poc.notify.socket.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.jpmc.poc.notify.handlers.RawWebSocketHandler;

@Deprecated
@Configuration
@EnableWebSocket
public class WebSocketConfigRaw implements WebSocketConfigurer{
	
	@Autowired
	protected RawWebSocketHandler webSocketHandler;

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketHandler, "/pnr").setAllowedOrigins("*");
		
	}

}
