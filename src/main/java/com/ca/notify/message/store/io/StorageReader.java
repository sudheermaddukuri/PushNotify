package com.ca.notify.message.store.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

public class StorageReader {
	private static final Logger logger = Logger.getLogger(StorageReader.class);

	private Connection conn;
	private Statement s;
	private ResultSet rs;

	public void selectUndelivered() {
		logger.info("Undelivered messages");
		try {
			Properties props = new Properties();
			String dbName = "capushnotif.recovery";
			conn = DriverManager.getConnection("jdbc:derby:" + dbName + ";create=false", props);
			conn.setAutoCommit(false);
			s = conn.createStatement();
			rs = s.executeQuery("select content,id,delivery from message_state");
			conn.commit();
			while (rs.next())
				logger.info(rs.getString("content"));
			
		} catch (SQLException e) {

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
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.fatal(e.getMessage());
			}
		}
	}
}
