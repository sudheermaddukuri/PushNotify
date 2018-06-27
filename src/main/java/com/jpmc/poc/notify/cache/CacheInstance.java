package com.jpmc.poc.notify.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.jpmc.poc.notify.domain.Notify;

/**
 * 
 * Returns an instance of the global cache.
 */

public class CacheInstance {
	private static final Logger logger = Logger.getLogger(CacheInstance.class);
	private static volatile ConcurrentHashMap<String, CopyOnWriteArrayList<Notify>> cacheInstance;

	private CacheInstance() {
	}

	public static ConcurrentHashMap<String, CopyOnWriteArrayList<Notify>> getInstance() {
		if (cacheInstance == null) {
			synchronized (CacheInstance.class) {
				if (cacheInstance == null) {
					cacheInstance = new ConcurrentHashMap<String, CopyOnWriteArrayList<Notify>>(100,0.9f,2);
					}
				}
			}
		return cacheInstance;
	}
}
