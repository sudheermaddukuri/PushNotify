package com.ca.notify.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

/**
 * 
 * Create the message database if not already created. This database is a
 * secondary storage to check if there are any message which are not delivered
 * and is delivered once a client logs in.
 *
 */
public class CreateDerbyStore implements Runnable {

	private static final Logger logger = Logger.getLogger(CreateDerbyStore.class);

	private final CountDownLatch latch;

	private static String framework = "embedded";
	private static String protocol = "jdbc:derby:";
	private static Connection conn = null;
	private static ArrayList<Statement> statements = new ArrayList<Statement>();
	private static Statement s;

	public CreateDerbyStore(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run() {
		try {
			Properties props = new Properties();
			String dbName = "capushnotif.recovery";
			conn = DriverManager.getConnection(protocol + dbName + ";create=true", props);
			conn.setAutoCommit(false);
			s = conn.createStatement();
			statements.add(s);
			s.execute("create table message_state(id varchar(40), content varchar(256), category varchar(40), delivery varchar(10),timestamp varchar(40)");
			conn.commit();
		} catch (Exception e) {
			logger.error(e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.fatal(e1.getMessage());
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.fatal(e.getMessage());
				}
			}
			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					logger.fatal(e.getMessage());
				}
			}
			latch.countDown();
		}
	}
}
