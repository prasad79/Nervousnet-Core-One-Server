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
 *  *******************************************************************************/
package ch.ethz.coss.nervousnet.core.sql;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class PulseElementConfiguration {

	private Long elementID;
	private String elementName;

	public PulseElementConfiguration() {

	}

	@XmlElementWrapper(name = "attributes")
	@XmlElement(name = "attribute")
	public List<PulseElementAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<PulseElementAttribute> attributes) {
		this.attributes = attributes;

	}

	private List<PulseElementAttribute> attributes = new ArrayList<PulseElementAttribute>();

	@XmlElement(name = "sensorName")
	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		// System.out.println("PulseElementConfiguration ElementName = "
		// + elementName);
		this.elementName = elementName;
	}

	@XmlElement(name = "sensorID")
	public Long getElementID() {
		return elementID;
	}

	public void setElementID(Long elementID) {
		this.elementID = elementID;
	}

}
