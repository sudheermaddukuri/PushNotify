package com.jpmc.poc.notify.storage;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;

public class Cp {
	private BasicDataSource getDataSource(){
		  BasicDataSource ds=new BasicDataSource();
		  ds.setDriverClassName(EmbeddedDriver.class.getName());
		  ds.setUrl("jdbc:derby:target/testdb;create=true");
		  ds.setUsername("james");
		  ds.setPassword("james");
		  return ds;
	}
	
	
}
