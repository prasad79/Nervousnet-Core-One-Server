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

import org.java_websocket.WebSocket;

public class PulseTimeMachineRequest {
	public static long ID_COUNTER = 0;
	public long requestID = 0;
	public int readingType = 0;
	public long startTime = 0;
	public long endTime = 0;
	public WebSocket webSocket = null;
	public boolean isNull;

	public PulseTimeMachineRequest(boolean isNull) {
		this.isNull = isNull;
	}

	public PulseTimeMachineRequest(long requestID, int readingType, long startTime, long endTime, WebSocket webSocket) {
		this.requestID = ID_COUNTER++;
		this.readingType = readingType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.webSocket = webSocket;
	}

	public PulseTimeMachineRequest(String request, WebSocket webSocket) {
		requestID++;
		// System.out.println("String request = "+request);
		String[] tokens = request.split(",");

		this.readingType = Integer.parseInt(tokens[1]);
		this.startTime = Long.parseLong(tokens[2]);
		this.endTime = Long.parseLong(tokens[3]);
		this.webSocket = webSocket;

		print();
	}

	public void print() {
		// System.out.println("/*************PulseTimeMachineRequest*******************/");
		// System.out.println("Request ID - "+requestID);
		// System.out.println("ReadingType - "+readingType);
		// System.out.println("StartTime - "+startTime);
		// System.out.println("EndTime - "+endTime);
		// System.out.println("/*******************************************************/");
	}

}