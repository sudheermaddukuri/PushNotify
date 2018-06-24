package com.ca.notify.connectors;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.ca.notify.controller.CaStompSessionHandler;

/**
 * 
 * Stomp connector
 * @deprecated
 */
public class StompConnector {
	
	private ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	private CountDownLatch latch = new CountDownLatch(1);
	
	public void sendStompMessage(String message){
		taskScheduler.afterPropertiesSet();
		WebSocketClient webSocketClient = new StandardWebSocketClient();
		WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new StringMessageConverter());
		stompClient.setTaskScheduler(taskScheduler);
		String url = "ws://localhost:61614/";
		stompClient.setDefaultHeartbeat(new long[] {0, 0});
		CaStompSessionHandler sessionHandler = new CaStompSessionHandler(latch);
		try {
			stompClient.connect(url, sessionHandler).get(3, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			//TODO Log
		} catch (ExecutionException e1) {
			//TODO Log
		} catch (TimeoutException e1) {
			//TODO Log
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			//TODO Log
		}
	}
	
//	public static void main(String args[]){
//		StompConnector conn = new StompConnector();
//		conn.sendStompMessage("arg");
//	}
}
