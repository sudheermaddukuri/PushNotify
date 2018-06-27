package com.jpmc.poc.notify.controller.service.impl;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.jpmc.poc.notify.storage.CreateDerbyStore;

public class RecoveryDatabaseServiceImpl {
	private static final Logger logger = Logger.getLogger(RecoveryDatabaseServiceImpl.class);
	public static void run(){
		final CountDownLatch dbLatch = new CountDownLatch(1);
		Thread dbThread = new Thread(new CreateDerbyStore(dbLatch));
		dbThread.start();
		try {
			dbLatch.await();
		} catch (InterruptedException e) {
			logger.fatal(e.getCause());
		}
	}
}
