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
package ch.ethz.coss.nervousnet.core.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import ch.ethz.coss.nervousnet.core.PulseTimeMachineRequest;
import ch.ethz.coss.nervousnet.core.PulseWebSocketServer;
import ch.ethz.coss.nervousnet.core.socket.SqlFetchWorker;
import ch.ethz.coss.nervousnet.core.utils.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class SqlRequestWorker extends SqlFetchWorker {

	SqlSetup sqlse;
	PulseTimeMachineRequest ptmRequest;

	public SqlRequestWorker(PulseWebSocketServer ps, Connection connection, SqlSetup sqlse,
			PulseTimeMachineRequest ptmRequest) {
		super(ps, connection);
		this.sqlse = sqlse;
		this.ptmRequest = ptmRequest;
	}

	@Override
	public void run() {
		try {

			JsonObject feature = null;
			JsonArray features = null;
			JsonObject featureCollection = null;

			try {

				/***** SQL get ********/
				// Fetch data
				PreparedStatement datastmt = sqlse.getSensorValuesFetchStatement(connection, ptmRequest.readingType,
						ptmRequest.startTime, ptmRequest.endTime);
				ResultSet rs = datastmt.executeQuery();
				featureCollection = new JsonObject();
				features = new JsonArray();
				// System.out.println("SQL query result size =
				// "+rs.getFetchSize());
				long currentTimeMillis = System.currentTimeMillis();
				while (rs.next()) {
					long volatility = rs.getLong("Volatility");
					long recordTime = rs.getLong("RecordTime");

//					System.out.println("Volatility = " + volatility);
//					System.out.println("currentTimeMillis = " + currentTimeMillis);
//					System.out.println("left time = " + (currentTimeMillis - (recordTime + (volatility * 1000))));
					if(volatility != -1)
					if (volatility == 0 || currentTimeMillis > (recordTime + (volatility * 1000) )) {
//						System.out.println("Continue");
						continue;
					}
					
					

					String lat = rs.getString("lat");
					String lon = rs.getString("lon");

					feature = new JsonObject();
					feature.addProperty("type", "Feature");
					JsonObject point = new JsonObject();
					point.addProperty("type", "Point");
					JsonArray coord = new JsonArray();
					coord.add(new JsonPrimitive(lat));
					coord.add(new JsonPrimitive(lon));
					point.add("coordinates", coord);
					feature.add("geometry", point);

					JsonObject properties = new JsonObject();

					properties.addProperty("volatility", volatility);
					if (ptmRequest.readingType == 0) {
						String luxVal = rs.getString("Light");
						// System.out.println("Reading instance of light");
						properties.addProperty("readingType", "" + 0);
						properties.addProperty("level", luxVal);
					} else if (ptmRequest.readingType == 1) {
						String soundVal = rs.getString("Decibel");
						properties.addProperty("readingType", "" + 1);
						properties.addProperty("level", soundVal);
					} else if (ptmRequest.readingType == 2) {
						String message = rs.getString("Message");
						message = message.trim();
						properties.addProperty("readingType", "" + 2);
						
						
						if(message.length() <= 0){
							message = "***Empty Message***";
							continue;
						}
						
						properties.addProperty("message", message);
						
					} else {
						// System.out.println("Reading instance not known");
					}

					feature.add("properties", properties);
					features.add(feature);

					// if((features.getAsJsonArray()).size() >= 60000){
					// featureCollection.add("features", features);
					// pSocketServer.sendToSocket(ptmRequest.webSocket,
					// ptmRequest.requestID, featureCollection.toString(),
					// false);
					// featureCollection = new JsonObject();
					// featureCollection = new JsonObject();
					// features = new JsonArray();
					// try {
					// Thread.sleep(10);
					// } catch (Exception e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// break;
					// }
				}

				featureCollection.add("features", features);
				// System.out.println("Feature collection +
				// "+featureCollection.toString());
				pSocketServer.sendToSocket(ptmRequest.webSocket, ptmRequest.requestID, featureCollection.toString(),
						true);

				/*************/

			} catch (JsonParseException e) {
				System.out.println("can't save json object: " + e.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().append(Log.FLAG_WARNING, "Generic error");
		} finally {
			cleanup();
		}
	}

	@Override
	protected void cleanup() {
		super.cleanup();

	}
}
