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
 * 	 *******************************************************************************/
package ch.ethz.coss.nervousnet.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ch.ethz.coss.nervousnet.core.sql.PulseElementConfiguration;
import ch.ethz.coss.nervousnet.core.utils.FileOperations;
import ch.ethz.coss.nervousnet.core.utils.Log;

@XmlRootElement(name = "config")
public class Configuration {

	private static Configuration config;

	private String sqlUsername;
	private String sqlPassword;
	private String sqlHostname;
	private int sqlPort;
	private String sqlDatabase;

	private int logWriteVerbosity;
	private int logDisplayVerbosity;

	private String configPath;
	private String logPath;

	private int serverPortApps;
	private int serverPortClient;
	private int serverThreads;

	private List<PulseElementConfiguration> sensors = new ArrayList<PulseElementConfiguration>();

	public static Configuration getConfig() {
		return config;
	}

	public static void setConfig(Configuration config) {
		Configuration.config = config;
	}

	@XmlElementWrapper(name = "sqlsensors")
	@XmlElement(name = "sensor")
	public List<PulseElementConfiguration> getSensors() {
		// System.out.println("Sensors size - " + sensors.size());
		return sensors;
	}

	public void setSensors(List<PulseElementConfiguration> sensors) {

		// System.out.println("Set Sensors called - " + sensors.size());
		this.sensors = sensors;
	}

	public int getServerThreads() {
		return serverThreads;
	}

	public void setServerThreads(int serverThreads) {
		this.serverThreads = serverThreads;
	}

	public int getServerPortForApp() {
		return serverPortApps;
	}

	public void setServerPortForApp(int serverPort) {
		this.serverPortApps = serverPort;
	}

	public int getServerPortForClient() {
		return serverPortClient;
	}

	public void setServerPortForClient(int serverPort) {
		this.serverPortClient = serverPort;
	}

	public String getSqlHostname() {
		return sqlHostname;
	}

	public void setSqlHostname(String sqlHostname) {
		this.sqlHostname = sqlHostname;
	}

	public int getSqlPort() {
		return sqlPort;
	}

	public void setSqlPort(int sqlPort) {
		this.sqlPort = sqlPort;
	}

	public String getSqlDatabase() {
//		return sqlDatabase;
		return System.getenv("JDBC_DATABASE_URL");
	}

	public void setSqlDatabase(String sqlDatabase) {
		this.sqlDatabase = sqlDatabase;
	}

	public String getSqlUsername() {
		return sqlUsername;
	}

	public void setSqlUsername(String sqlUsername) {
		this.sqlUsername = sqlUsername;
	}

	public String getSqlPassword() {
		return sqlPassword;
	}

	public void setSqlPassword(String sqlPassword) {
		this.sqlPassword = sqlPassword;
	}

	public int getLogWriteVerbosity() {
		return logWriteVerbosity;
	}

	public void setLogWriteVerbosity(int logWriteVerbosity) {
		this.logWriteVerbosity = logWriteVerbosity;
	}

	public int getLogDisplayVerbosity() {
		return logDisplayVerbosity;
	}

	public void setLogDisplayVerbosity(int logDisplayVerbosity) {
		this.logDisplayVerbosity = logDisplayVerbosity;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public static synchronized Configuration getInstance(String path) {
		if (config == null) {
			config = new Configuration(path);
			// Load configuration from file
			unmarshal();
		}
		return config;
	}

	public static synchronized Configuration getInstance() {
		if (config == null) {
			config = new Configuration("nervousnet_core2_config.xml");
			// Load configuration from file
			unmarshal();
		}
		return config;
	}

	/**
	 * No-arg default constructor for unmarshal
	 */
	private Configuration() {
	}

	/**
	 * Default constructor if configuration file is not found
	 * 
	 * @param path
	 */
	private Configuration(String path) {
		// Write default configuration here
		this.configPath = path;
		// Logging
		this.logDisplayVerbosity = Log.FLAG_ERROR | Log.FLAG_WARNING;
		this.logWriteVerbosity = Log.FLAG_ERROR | Log.FLAG_WARNING;
		this.logPath = "log.txt";
		// SQL
		this.sqlHostname = "";
		this.sqlUsername = "";
		this.sqlPassword = "";
		this.sqlPort = 3306;
		this.sqlDatabase = "";
		// Networking
		this.serverPortApps = Integer.parseInt(System.getenv("PORT"));//8445;
		this.serverPortClient = 8446;
		this.serverThreads = 5;
		// Sensors
	}

	public static synchronized void marshal() {
		try {
			JAXBContext context = JAXBContext.newInstance(Configuration.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			m.marshal(config, new File(config.getConfigPath()));
		} catch (JAXBException jbe) {
			Log.getInstance().append(Log.FLAG_WARNING, "Couldn't write the configuration file");
		}
	}

	public static synchronized void unmarshal() {
		try {
			JAXBContext context = JAXBContext.newInstance(Configuration.class);
			Unmarshaller um = context.createUnmarshaller();
			// System.out.println("Config path --"
			// + Configuration.config.getConfigPath());

			InputStream in = Configuration.class.getClassLoader()
					.getResourceAsStream(Configuration.config.getConfigPath());
			// FileReader fReader = new FileReader(
			// Configuration.config.getConfigPath());
			//
			// if(fReader == null)
			// //System.out.println("fReader is null");

			Configuration config = (Configuration) um.unmarshal(in);
			Configuration.config = config;
			return;
		} catch (JAXBException jbe) {
			jbe.printStackTrace();
			Log.getInstance().append(Log.FLAG_ERROR, "Error parsing the configuration file");
		} catch (Exception ioe) {
			ioe.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING, "Couldn't read the configuration file");
		}
		// Error reading the configuration, write current configuration after
		// backing up
		try {
			FileOperations.copyFile(new File(Configuration.config.getConfigPath()),
					new File(Configuration.config.getConfigPath() + ".back"));
		} catch (IOException e) {
		}
		// marshal();
	}
}
