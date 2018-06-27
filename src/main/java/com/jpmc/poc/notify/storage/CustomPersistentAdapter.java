package com.jpmc.poc.notify.storage;

import org.apache.activemq.store.jdbc.JDBCPersistenceAdapter;
import org.apache.log4j.Logger;

/**
 * 
 * Persistence adapter to enable derby in memory store in the broker.
 */
public class CustomPersistentAdapter {
	
	private static final Logger logger = Logger.getLogger(CustomPersistentAdapter.class);
	
	private static volatile JDBCPersistenceAdapter jdbcPersistentAdapter;

	private CustomPersistentAdapter() {
	}

	public static JDBCPersistenceAdapter getInstance() {
		if (jdbcPersistentAdapter == null) {
			synchronized (CustomPersistentAdapter.class) {
				if (jdbcPersistentAdapter == null) {
					jdbcPersistentAdapter = new JDBCPersistenceAdapter();
				}
			}
		}
		return jdbcPersistentAdapter;

	}
}
