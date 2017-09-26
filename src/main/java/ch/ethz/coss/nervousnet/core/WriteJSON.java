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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class WriteJSON {

	public static void sendJSON(Socket socket, Object o, double[] o2, int type) {

		try {
			Scanner in = new Scanner(socket.getInputStream());

			while (!in.nextLine().isEmpty())
				;

			PrintWriter out = new PrintWriter(socket.getOutputStream());

			String message = new Gson().toJson(o);
			String message2 = new Gson().toJson(o2[0]);
			String message3 = new Gson().toJson(o2[1]);
			String message4 = new Gson().toJson(type);
			String bothJson = "[" + message + "," + message2 + "," + message3 + "," + message4 + "]";
			out.println("HTTP/1.0 200 OK");
			out.println("Content-Type: text/json");
			out.printf("Content-Length: %d%n", bothJson.length());
			out.println("Access-Control-Allow-Origin: *");
			out.println();
			out.println(bothJson);
			out.flush();

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendJSON(Socket socket, Object o) {

		try {
			Scanner in = new Scanner(socket.getInputStream());

			while (!in.nextLine().isEmpty())
				;

			PrintWriter out = new PrintWriter(socket.getOutputStream());

			String message = new Gson().toJson(o);

			out.println("HTTP/1.0 200 OK");
			out.println("Content-Type: text/json");
			out.printf("Content-Length: %d%n", message.length());
			out.println("Access-Control-Allow-Origin: *");
			out.println();
			out.println(message);
			out.flush();

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public static void sendGeoJSON(Socket socket, Object o) {
//
//		try {
//			Scanner in = new Scanner(socket.getInputStream());
//
//			while (!in.nextLine().isEmpty())
//				;
//
//			PrintWriter out = new PrintWriter(socket.getOutputStream());
//
//
//			JsonObject feature = new JsonObject();
//			try {
//				feature.addProperty("type", "Feature");
//				// JsonArray featureList = new JsonArray();
//				// iterate through your list
//				// for (ListElement obj : list) {
//				// {"geometry": {"type": "Point", "coordinates": [-94.149,
//				// 36.33]}
//				JsonObject point = new JsonObject();
//				point.addProperty("type", "Point");
//				// construct a JSONArray from a string; can also use an array or
//				// list
//				JsonArray coord = new JsonArray();
//				coord.add(new JsonPrimitive(reading.location.latnLong[0]));
//				coord.add(new JsonPrimitive(reading.location.latnLong[1]));
//				point.add("coordinates", coord);
//				feature.add("geometry", point);
//				JsonObject properties = new JsonObject();
//				if (reading.type == 0) {
//					// System.out.println("Reading instance of light");
//					properties.addProperty("readingType", "" + 0);
//					properties.addProperty("lightLevel", "" + ((LightReading) reading).lightVal);
//				} else if (reading.type == 1) {
//					properties.addProperty("readingType", "" + 1);
//					properties.addProperty("noiseLevel", "" + ((NoiseReading) reading).soundVal);
//				} else if (reading.type == 2) {
//					properties.addProperty("readingType", "" + 2);
//					properties.addProperty("message", "" + ((TextVisual) reading).textMsg);
//				} else {
//					// System.out.println("Reading instance not known");
//				}
//
//				feature.add("properties", properties);
//
//				// }
//			} catch (JsonParseException e) {
//				// System.out.println("can't save json object: " +
//				// e.toString());
//			}
//			// output the result
//			// System.out.println("featureCollection=" + feature.toString());
//
//			String message = feature.toString();
//
//			out.println("HTTP/1.0 200 OK");
//			out.println("Content-Type: text/json");
//			out.printf("Content-Length: %d%n", message.length());
//			out.println("Access-Control-Allow-Origin: *");
//			out.println();
//			out.println(message);
//			out.flush();
//
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
