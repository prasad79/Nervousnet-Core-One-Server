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

public class PulseConstants {

	public static String PULSE_LIGHT_LABEL = "Light";
	public static String PULSE_NOISE_LABEL = "Noise";
	public static String PULSE_ACCEL_LABEL = "Accel";
	public static String PULSE_BATTERY_LABEL = "Battery";
	public static String PULSE_GYRO_LABEL = "Gyro";
	public static String PULSE_TEMP_LABEL = "Temperature";
	public static String PULSE_PROX_LABEL = "Proximity";
	public static String PULSE_TEXT_LABEL = "Message";
	

	public static String getLabel(int readingType) {

		switch (readingType) {
		case 1:
			return PULSE_ACCEL_LABEL;
		case 2:
			return PULSE_BATTERY_LABEL;
		case 3:
			return PULSE_LIGHT_LABEL;
		case 4:
			return PULSE_GYRO_LABEL;
		case 5:
			return PULSE_NOISE_LABEL;
		case 6:
			return PULSE_PROX_LABEL;
		case 7:
			return PULSE_TEMP_LABEL;
		case 8:
		default:
			return PULSE_TEXT_LABEL;

		}
	}

}
