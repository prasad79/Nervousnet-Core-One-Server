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
 * 	Author:
 * 	Prasad Pulikal - prasad.pulikal@gess.ethz.ch  - Initial design and implementation
 *******************************************************************************/
package ch.ethz.coss.nervousnet.core;

import java.io.IOException;

import org.java_websocket.WebSocketImpl;

import ch.ethz.coss.nervousnet.core.socket.PulseConcurrentServer;
import ch.ethz.coss.nervousnet.core.socket.PulseRequestHandlingServer;
import ch.ethz.coss.nervousnet.core.socket.SqlFetchWorkerFactory;
import ch.ethz.coss.nervousnet.core.sql.PulseElementConfiguration;
import ch.ethz.coss.nervousnet.core.sql.SqlConnection;
import ch.ethz.coss.nervousnet.core.sql.SqlSetup;
import ch.ethz.coss.nervousnet.core.sql.SqlUploadWorkerFactory;
import ch.ethz.coss.nervousnet.core.utils.Log;

public class PulseServer {

	public static void main(String[] args) throws InterruptedException, IOException {
		WebSocketImpl.DEBUG = false;

		// Load configuration from custom path or current directory
		if (args.length > 0) {
			System.out.println("Config file name = "+args[0]);
			config = Configuration.getInstance(args[0]);
		} else {
			config = Configuration.getInstance();
		}

		// Set up logging
		Log log = Log.getInstance(config.getLogDisplayVerbosity(), config.getLogWriteVerbosity(), config.getLogPath());
		log.append(Log.FLAG_INFO, "Reading configuration file done");

		log.append(Log.FLAG_INFO, "Reading configuration file done");
		// Set up SQL connection
		SqlConnection sqlco = new SqlConnection(config.getSqlHostname(), config.getSqlUsername(),
				config.getSqlPassword(), config.getSqlPort(), config.getSqlDatabase());
		log.append(Log.FLAG_INFO, "Establishing connection to SQL database done");

		// Set up SQL tables
		SqlSetup sqlse = new SqlSetup(sqlco.getConnection(), config);
		sqlse.setupTables();

		// Create factory which creates workers for uploading to the SQL
		// database
		SqlUploadWorkerFactory factory = new SqlUploadWorkerFactory(sqlco, sqlse);

		SqlFetchWorkerFactory sqlFfactory = new SqlFetchWorkerFactory(sqlco, sqlse);
		PulseRequestHandlingServer prhServer = new PulseRequestHandlingServer(config.getServerThreads() + 5,
				sqlFfactory);
		Thread reqServerThread = new Thread(prhServer);
		reqServerThread.start();

		PulseWebSocketServer pWebSocketServer = new PulseWebSocketServer(8446, prhServer);
		pWebSocketServer.start();

		// Start server
		PulseConcurrentServer server = new PulseConcurrentServer(Integer.parseInt(System.getenv("PORT")), pWebSocketServer, 5, factory);
		Thread serverThread = new Thread(server);
		serverThread.start();
		log.append(Log.FLAG_INFO, "PulseConcurrentServer Started");

		Thread sqlFetchThread = new Thread(prhServer);
		sqlFetchThread.start();

		boolean running = true;

		while (running) {
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				log.append(Log.FLAG_WARNING, "Server execution interrupted");
				running = false;
			}
		}

		// Stop server
		server.stop();
		prhServer.stop();
		log.append(Log.FLAG_INFO, "Server terminated");

	}

	public static int smartphonesPort = Integer.parseInt(System.getenv("PORT"));// 8445;
	public static Configuration config;
}