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
package ch.ethz.coss.nervousnet.core.socket;

import java.io.IOException;
import java.net.Socket;

import ch.ethz.coss.nervousnet.core.PulseWebSocketServer;
import ch.ethz.coss.nervousnet.core.utils.Log;

public abstract class ConcurrentSocketWorker implements Runnable {
	@Override
	public abstract void run();

	protected Socket socket;
	protected PulseWebSocketServer pSocketServer;

	protected ConcurrentSocketWorker(Socket socket, PulseWebSocketServer pSocketServer) {
		this.socket = socket;
		this.pSocketServer = pSocketServer;
	}

	protected void cleanup() {
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {

			Log.getInstance().append(Log.FLAG_ERROR, " Error in Socket connection.");
		}

		// try {
		// pSocketServer.stop();
		// } catch (IOException | InterruptedException e) {
		//
		// Log.getInstance().append(Log.FLAG_ERROR,
		// " Error in stopping Pulse WebSocketServer.");
		// }
	}
}
