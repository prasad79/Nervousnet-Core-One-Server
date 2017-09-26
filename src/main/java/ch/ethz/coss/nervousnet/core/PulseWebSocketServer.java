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

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Hashtable;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import ch.ethz.coss.nervousnet.core.socket.PulseRequestHandlingServer;

public class PulseWebSocketServer extends WebSocketServer {

	// private ArrayList<WebSocket> timeMachineConnectionsList = new
	// ArrayList<WebSocket>();

	private PulseRequestHandlingServer prhServer;

	public PulseWebSocketServer(int port, PulseRequestHandlingServer prhServer) throws UnknownHostException {
		super(new InetSocketAddress(port));
		this.prhServer = prhServer;
		this.prhServer.setPulseServer(this);

	}

	public PulseWebSocketServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("/**************New Connection**********************/");
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress()
				+ " has joined in to received the pulse pushes!");
		System.out.println("Total Connections = " + connections().size());
		System.out.println("/************************************/");

	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("/**************Connection Disconnected**********************/");
		// System.out.println(conn + " has disconnected");
		// System.out.println("Total Connections = " + connections().size());
		// System.out.println("/************************************/");

		if (prhServer.hTimeMachineConnectionList.containsKey(conn))
			prhServer.hTimeMachineConnectionList.remove(conn);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("/**************New Messages**********************/");
		// System.out.println(conn + ": " + message);
		// System.out.println("WebSocket Connection : " + conn.toString());
		// System.out.println("/************************************/");

		if (message.contains("type=")) {
			int type = Integer.parseInt(message.substring(message.indexOf("=") + 1, message.indexOf("=") + 2));

			// System.out.println("Type = "+type);

			switch (type) {
			case 0:
				prhServer.hTimeMachineConnectionList.remove(conn);
				// System.out.println("Switched conn to RealTime.");
				// System.out.println("hTimeMachineConnectionList size =
				// "+prhServer.hTimeMachineConnectionList.size());

				break;
			case 1:
				// System.out.println("Switched conn to Time Machine.");
				// System.out.println("hTimeMachineConnectionList size =
				// "+prhServer.hTimeMachineConnectionList.size());

				String request = message.substring(message.indexOf("=") + 1);
				// System.out.println("Request -- "+request);
				if (request.length() > 1) {
					PulseTimeMachineRequest pulseTimeMachineRequest = new PulseTimeMachineRequest(request, conn);
					prhServer.addToRequestList(pulseTimeMachineRequest);
					prhServer.hTimeMachineConnectionList.put(conn, pulseTimeMachineRequest);
					Thread reqServerThread = new Thread(prhServer);
					reqServerThread.start();

				} else if (request.length() == 1) {
					prhServer.hTimeMachineConnectionList.put(conn, new PulseTimeMachineRequest(true));

				}

				break;

			}

		}
	}

	@Override
	public void onFragment(WebSocket conn, Framedata fragment) {
		// System.out.println("received fragment: " + fragment);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a
			// specific websocket
		}
	}

	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToAll(String text) {
		// System.out.println("sendToAll Text - " + text);
		// System.out.println("prhServer.hTimeMachineConnectionList size
		// "+prhServer.hTimeMachineConnectionList.size());

		Collection<WebSocket> con = connections();
		synchronized (con) {
			for (WebSocket c : con) {
				if (!prhServer.hTimeMachineConnectionList.containsKey(c)) {
					c.send(text);
					// //System.out.println("sent Text - " + text);
				} else {
					System.out.println("Not sending message to this connection because it is in time-machine state");
				}

			}

		}
	}

	/**
	 * Sends data to specific Websocket connection. Used for Time-machine
	 * implementation
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToSocket(WebSocket conn, long requestID, String text, boolean isComplete) {
		// System.out.println("inside sendToSocket " );
		// System.out.println("prhServer.hTimeMachineConnectionList size
		// "+prhServer.hTimeMachineConnectionList.size());
		if (prhServer.hTimeMachineConnectionList.containsKey(conn)) {

			PulseTimeMachineRequest ptmRequest = prhServer.hTimeMachineConnectionList.get(conn);
			if (ptmRequest.requestID == requestID && !ptmRequest.isNull) {
				conn.send(text);
			}

			if (isComplete) {
				prhServer.hTimeMachineConnectionList.put(conn, new PulseTimeMachineRequest(true));
			}

		}

		if (prhServer.hTimeMachineConnectionList.size() == 0)
			PulseTimeMachineRequest.ID_COUNTER = 0;

	}

}