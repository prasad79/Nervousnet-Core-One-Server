/*******************************************************************************
 *     SwarmPulse - A service for collective visualization and sharing of mobile 
 *     sensor data, text messages and more.
 *
 *     Copyright (C) 2015 ETH Zürich, COSS
 *
 *     This file is part of SwarmPulse.
 *
 *     SwarmPulse is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SwarmPulse is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SwarmPulse. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * 	*******************************************************************************/
package ch.ethz.coss.nervousnet.core.sql;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import ch.ethz.coss.nervousnet.core.utils.Log;

public final class SqlConnection {

	// Server properties
	private String username;
	private String password;
	private String hostname;
	private int port;
	private String database;

	private DataSource source;

//	public Connection getConnection() {
//		try {
//			return source.getConnection();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			Log.getInstance().append(Log.FLAG_ERROR, "Can't get a connection from the data source");
//			return null;
//		}
//	}
	
	public  Connection getConnection()  {
		try {
	    URI dbUri = new URI(System.getenv("DATABASE_URL"));

	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
	    System.out.println(dbUrl);
	    return DriverManager.getConnection(dbUrl, username, password);
		} catch (java.net.URISyntaxException e2) {
			e2.printStackTrace();
			Log.getInstance().append(Log.FLAG_ERROR, " URISyntaxException: Can't get a connection from the data source");
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_ERROR, "Can't get a connection from the data source");
			return null;
		}
	}

	/**
	 * Connection will be set up this way: [jdbc:mysql://" + hostname + ":
	 * " + port + "/" + database]
	 * 
	 * @param hostname
	 * @param username
	 * @param password
	 * @param port
	 * @param database
	 */
	public SqlConnection(String hostname, String username, String password, int port, String database) {
		this.username = username;
		this.password = password;
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		print();
		source = setup();

	}

	private DataSource setup() {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_ERROR, "Error loading the SQL driver");
			return null;
		}

		ConnectionFactory cf = null;
		try {
			cf = new DriverManagerConnectionFactory("jdbc:mysql://" + hostname + ":" + port + "/" + database, username,
					password);
			// System.out.println("CF - " + cf.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, null);

		ObjectPool<PoolableConnection> connPool = new GenericObjectPool<PoolableConnection>(pcf);

		pcf.setPool(connPool);
		PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<PoolableConnection>(connPool);
		// System.out.println("DataSource -- " + dataSource);
		return dataSource;
	}

	private void print() {
		System.out.println("***********SqlConnection************");
		System.out.println("hostname - " + hostname);
		System.out.println("username - " + username);
		System.out.println("password - **********" );
		System.out.println("database - " + database);
		System.out.println("************************************");
	}

}