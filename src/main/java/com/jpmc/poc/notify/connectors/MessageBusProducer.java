package com.jpmc.poc.notify.connectors;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

/**
 * Message producer to the camessagebus
 * 
 *
 */
public class MessageBusProducer {
	
	private static final Logger logger = Logger.getLogger(MessageBusProducer.class);
	
	Connection connection = null;
	Session session = null;

	public void sendMessage(String msg, String dst) {
		try {

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://camessagebus");
			connection = connectionFactory.createConnection();
			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destination = session.createQueue(dst);

			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			TextMessage message = session.createTextMessage(msg);

			producer.send(message);

		} catch (JMSException e) {
			logger.error(e.getCause());
		} finally {

			try {
				if (session != null)
					session.close();
				if (connection != null)
					connection.close();
			} catch (JMSException e) {
				logger.error(e.getCause());
			}

		}
	}
}
