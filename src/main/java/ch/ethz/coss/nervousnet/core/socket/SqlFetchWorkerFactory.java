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
package ch.ethz.coss.nervousnet.core.socket;

import java.sql.Connection;

import ch.ethz.coss.nervousnet.core.PulseTimeMachineRequest;
import ch.ethz.coss.nervousnet.core.PulseWebSocketServer;
import ch.ethz.coss.nervousnet.core.sql.SqlConnection;
import ch.ethz.coss.nervousnet.core.sql.SqlRequestWorker;
import ch.ethz.coss.nervousnet.core.sql.SqlSetup;

public class SqlFetchWorkerFactory {

	SqlConnection sqlco;
	SqlSetup sqlse;
	PulseTimeMachineRequest ptmRequest;

	public SqlFetchWorkerFactory(SqlConnection sqlco, SqlSetup sqlse) {
		this.sqlco = sqlco;
		this.sqlse = sqlse;
	}

	public SqlFetchWorker createWorker(PulseWebSocketServer pSocketServer, PulseTimeMachineRequest ptmRequest) {
		SqlRequestWorker srWorker = new SqlRequestWorker(pSocketServer, sqlco.getConnection(), sqlse, ptmRequest);
		return srWorker;
	}
}
