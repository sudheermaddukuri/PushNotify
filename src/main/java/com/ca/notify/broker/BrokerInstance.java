package com.ca.notify.broker;

import java.io.IOException;

import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

import com.ca.notify.storage.CaPersistentAdapter;

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
					brokerInstance.setBrokerName("camessagebus");
					brokerInstance.setPersistent(true);
					brokerInstance.setDataDirectory("canotificationdata");
					try {
						brokerInstance.setPersistenceAdapter(CaPersistentAdapter.getInstance());
					} catch (IOException e) {
						logger.fatal(e.getCause());
					}
				}
			}
		}
		return brokerInstance;
	}
}
