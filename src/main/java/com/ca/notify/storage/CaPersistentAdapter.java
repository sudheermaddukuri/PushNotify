package com.ca.notify.storage;

import org.apache.activemq.store.jdbc.JDBCPersistenceAdapter;
import org.apache.log4j.Logger;

/**
 * 
 * Persistence adapter to enable derby in memory store in the broker.
 */
public class CaPersistentAdapter {
	
	private static final Logger logger = Logger.getLogger(CaPersistentAdapter.class);
	
	private static volatile JDBCPersistenceAdapter jdbcPersistentAdapter;

	private CaPersistentAdapter() {
	}

	public static JDBCPersistenceAdapter getInstance() {
		if (jdbcPersistentAdapter == null) {
			synchronized (CaPersistentAdapter.class) {
				if (jdbcPersistentAdapter == null) {
					jdbcPersistentAdapter = new JDBCPersistenceAdapter();
				}
			}
		}
		return jdbcPersistentAdapter;

	}
}
