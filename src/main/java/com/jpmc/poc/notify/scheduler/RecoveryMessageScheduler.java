package com.jpmc.poc.notify.scheduler;

import java.util.TimerTask;

//import com.jpmc.poc.notify.message.store.io.StorageReader;

/**
 * 
 * This component is used to recover messages. The scheduler runs as a cron job
 * to find out all the messages that are not ack'd changes the timestamp and put
 * it back to the message bus.
 *
 */

public class RecoveryMessageScheduler extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void run() {
//		StorageReader msgs = new StorageReader();
//		msgs.selectUndelivered();
//		
//	}
	
}
