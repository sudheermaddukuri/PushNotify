package com.ca.notify.message.store.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ca.notify.Application;
import com.ca.notify.domain.Notify;

@Component
public class StorageWriter {
	private static final Logger logger = Logger.getLogger(Application.class);
	
	private static String framework = "embedded";
	private static String protocol = "jdbc:derby:";
	private static Connection conn = null;
	private static Statement s;
	
	public static void storeMessageBackup(Notify nf) {

		PreparedStatement msgInsert = null;

		try {
			//TODO Transaction
			logger.info("Start storing message to ensure delivery");
			Properties props = new Properties();
			String dbName = "capushnotif.recovery";
			conn = DriverManager.getConnection(protocol + dbName + ";create=false", props);
			conn.setAutoCommit(false);
			msgInsert = conn.prepareStatement("insert into message_state values (?, ?, ?, ?)");
			msgInsert.setString(1, nf.getArg());
			msgInsert.setString(2, nf.getId());
			msgInsert.setString(3, nf.getCategory().name());
			msgInsert.setString(4, nf.getTimestamp());
			msgInsert.executeUpdate();
			logger.info("Message stored as a backup");
			conn.commit();
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.fatal(e1.getMessage());
			}
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.fatal(e.getMessage());
				}
			if (s != null)
				try {
					msgInsert.close();
				} catch (SQLException e) {
					logger.fatal(e.getMessage());
				}
		}

	}
}
