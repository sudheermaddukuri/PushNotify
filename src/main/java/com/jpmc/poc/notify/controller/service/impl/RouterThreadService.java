package com.jpmc.poc.notify.controller.service.impl;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.jpmc.poc.notify.cache.CacheInstance;
import com.jpmc.poc.notify.connectors.SockJsConnector;
import com.jpmc.poc.notify.domain.Notify;

public class RouterThreadService implements Runnable{
	
	private static final Logger logger = Logger.getLogger(RouterThreadService.class);
	
	private Message message;
	
	public RouterThreadService(final Message message) {
		this.message = message;
	}
	
	public void run() {
		logger.info(Thread.currentThread().getName() + "activated for routing" + this.message);
		SockJsConnector connector = null;
		TextMessage textMessage = (TextMessage) this.message;
		try {
			connector = new SockJsConnector();
			connector.sendMessageWithinVm(textMessage.getText());
			Notify messageToCache = (new Gson().fromJson(textMessage.getText(), Notify.class));
			CopyOnWriteArrayList<Notify> args = CacheInstance.getInstance().get(messageToCache.getId());
			//TODO It might have flushed to disk check for that
			if(args == null || args.size() == 0){
				CopyOnWriteArrayList<Notify> msgs = new CopyOnWriteArrayList<Notify>();
				msgs.add(messageToCache);
				CacheInstance.getInstance().putIfAbsent(messageToCache.getId(), msgs);
			}else{
				args.add(messageToCache);
				CacheInstance.getInstance().replace(messageToCache.getId(), args);
			}
		} catch (JMSException e) {
			logger.fatal(e.getCause());
		}finally{

			//TODO
		}
		
	}

}
