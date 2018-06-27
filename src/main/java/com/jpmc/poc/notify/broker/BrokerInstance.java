package com.jpmc.poc.notify.broker;

import java.io.IOException;

import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

import com.jpmc.poc.notify.storage.CustomPersistentAdapter;

/**
 * 
 * Return the instance of the global broker.
 *
 */
public class BrokerInstance {
	private static final Logger logger = Logger.getLogger(BrokerInstance.class);
	private static volatile BrokerService brokerInstance;

	private BrokerInstance() {
	}

	public static BrokerService getInstance() {
		if (brokerInstance == null) {
			synchronized (BrokerInstance.class) {
				if (brokerInstance == null) {
					brokerInstance = new BrokerService();
					brokerInstance.setBrokerName("pnsmessagebus");
					brokerInstance.setPersistent(true);
					brokerInstance.setDataDirectory("pnsnotificationdata");
					try {
						brokerInstance.setPersistenceAdapter(CustomPersistentAdapter.getInstance());
					} catch (IOException e) {
						logger.fatal(e.getCause());
					}
				}
			}
		}
		return brokerInstance;
	}
}
