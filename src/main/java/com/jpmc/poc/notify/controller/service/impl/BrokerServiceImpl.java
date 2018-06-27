package com.jpmc.poc.notify.controller.service.impl;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.jpmc.poc.notify.embeddedbroker.impl.ActiveMQBrokerRunner;

/**
 * 
 * Broker Service to start the configured embedded broker. Dynamic configuration of the broker is not supported now.
 */
public class BrokerServiceImpl{
	
	private static final Logger logger = Logger.getLogger(RecoveryDatabaseServiceImpl.class);

	public static void run(){
			runActiveMQBroker();	
	}
	
	public static void runActiveMQBroker(){
		final CountDownLatch brokerLatch = new CountDownLatch(1);
		Thread brokerThread = new Thread(new ActiveMQBrokerRunner(brokerLatch));
		brokerThread.start();
		try {
			brokerLatch.await();
		} catch (InterruptedException e) {	
			// TODO Log
		}
		
		logger.info("Simulated sleep");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			logger.info(e.getCause());
		}
		
		logger.info("Initiating datastore creation");
		RecoveryDatabaseServiceImpl.run();
		logger.info("Data store creation done");
	}
	
	private static void runRabbitMQBroker(){
 
	}
	
}
