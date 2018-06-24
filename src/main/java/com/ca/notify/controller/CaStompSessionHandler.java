package com.ca.notify.controller;

import java.util.concurrent.CountDownLatch;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

/**
 * Stomp session handler.
 * @deprecated
 *
 */
public class CaStompSessionHandler extends StompSessionHandlerAdapter {
	
	private StompSession session;
	private CountDownLatch latch;
	
	public CaStompSessionHandler(){
		
	}
	
	public CaStompSessionHandler(CountDownLatch latch) {
		this.latch = latch;
	}
	
	@Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		this.session = session;
        session.send("/app/public", "Message");
        latch.countDown();
    }
	
	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		System.out.println(exception);
	}

	@Override
	public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
		System.out.println(ex);
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		System.out.println(payload);
	}
}
