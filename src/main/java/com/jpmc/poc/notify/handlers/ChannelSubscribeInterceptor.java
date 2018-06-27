package com.jpmc.poc.notify.handlers;

import java.util.Set;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;

/**
 * 
 * Class to intercept the subscribe event and search for messages which are undelivered for a destination and re-dispatch them.
 */
@Component
public class ChannelSubscribeInterceptor implements SmartApplicationListener {

	public void onApplicationEvent(ApplicationEvent arg0) {
		DefaultSimpUserRegistry reg = new DefaultSimpUserRegistry();
		reg.onApplicationEvent(arg0);
	}

	
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	//@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public boolean supportsSourceType(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
