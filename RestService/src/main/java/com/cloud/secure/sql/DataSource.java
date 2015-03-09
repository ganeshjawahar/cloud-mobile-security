package com.cloud.secure.sql;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.cloud.secure.AppGlobals;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSource {

	private static DataSource datasource;
	private ComboPooledDataSource cpds;

	private DataSource() throws IOException, SQLException,
			PropertyVetoException {
		cpds = new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver"); // loads the jdbc driver
		cpds.setJdbcUrl(AppGlobals.RDS_IP);
		cpds.setUser("root");
		cpds.setPassword("");
		
		cpds.setMinPoolSize(5); 
		cpds.setCheckoutTimeout(1000000);
		cpds.setMaxPoolSize(10);
		cpds.setIdleConnectionTestPeriod(300);
		cpds.setMaxStatements(100);
		cpds.setMaxStatementsPerConnection(12);
		cpds.setMaxIdleTime(30);
	}

	public static DataSource getInstance() throws IOException, SQLException,
			PropertyVetoException {
		if (datasource == null) {
			datasource = new DataSource();
			return datasource;
		} else {
			return datasource;
		}
	}

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		return this.cpds.getConnection();
	}

}