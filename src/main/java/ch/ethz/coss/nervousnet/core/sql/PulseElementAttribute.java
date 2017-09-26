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
package ch.ethz.coss.nervousnet.core.sql;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.ethz.coss.nervousnet.core.xml.TypeAdapter;

class PulseElementAttribute {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		// System.out.println("PulseElementAttribute name -- " + name);
		this.name = name;
	}

	@XmlJavaTypeAdapter(type = int.class, value = TypeAdapter.class)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public PulseElementAttribute() {
	}

	private String name;

	private int type;
}
