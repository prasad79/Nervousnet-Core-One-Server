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
 * *******************************************************************************/
package ch.ethz.coss.nervousnet.core.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import ch.ethz.coss.nervousnet.core.sql.SqlSetup;

public class TypeAdapter extends XmlAdapter<String, Integer> {

	@Override
	public String marshal(Integer v) throws Exception {
		switch (v) {
		case SqlSetup.TYPE_BOOL:
			return "BOOL";
		case SqlSetup.TYPE_INT32:
			return "INT32";
		case SqlSetup.TYPE_INT64:
			return "INT64";
		case SqlSetup.TYPE_FLOAT:
			return "FLOAT";
		case SqlSetup.TYPE_DOUBLE:
			return "DOUBLE";
		case SqlSetup.TYPE_STRING:
			return "STRING";
		case SqlSetup.TYPE_LOCATION:
			return "GEOGRAPHY";
		default:
			return "ERROR!";
		}
	}

	@Override
	public Integer unmarshal(String v) throws Exception {
		String type = v.toUpperCase();
		if (type.equals("BOOL")) {
			return SqlSetup.TYPE_BOOL;
		} else if (type.equals("INT32")) {
			return SqlSetup.TYPE_INT32;
		} else if (type.equals("INT64")) {
			return SqlSetup.TYPE_INT64;
		} else if (type.equals("FLOAT")) {
			return SqlSetup.TYPE_FLOAT;
		} else if (type.equals("DOUBLE")) {
			return SqlSetup.TYPE_DOUBLE;
		} else if (type.equals("STRING")) {
			return SqlSetup.TYPE_STRING;
		} else if (type.equals("GEOGRAPHY")) {
			return SqlSetup.TYPE_LOCATION;
		} else {
			return -1;
		}
	}
}
