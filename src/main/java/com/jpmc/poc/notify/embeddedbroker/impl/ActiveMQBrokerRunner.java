package com.jpmc.poc.notify.embeddedbroker.impl;

import java.util.concurrent.CountDownLatch;

import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

import com.jpmc.poc.notify.broker.BrokerInstance;

/**
 * 
 * ActiveMQ broker runner. The runner waits for the broker to start before
 * passing on the control to the calling thread.
 */
public class ActiveMQBrokerRunner implements Runnable {

	private static final Logger logger = Logger.getLogger(ActiveMQBrokerRunner.class);
		
	private final CountDownLatch latch;
	private BrokerService broker = null;

	public ActiveMQBrokerRunner(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run() {
		try {
			broker = BrokerInstance.getInstance();
			broker.start();
			while (true) {
				Thread.sleep(3000);
				if (broker.isStarted()) {
					latch.countDown();
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getCause());
		} finally {
			// TODO
		}
	}

}
