package com.rs.worldserver.hash;

import java.io.FileInputStream;
import java.util.Properties;

//Shard Revolutions Generic MMORPG Server
//Copyright (C) 2008  Graham Edgecombe

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

/**
 * Handles rehashing of server hashdata.
 * 
 * @author Graham
 * 
 */
public class RehashManager {

	public static void rehash() throws Exception {
		// load from file in future lol
		Properties parser = new Properties();
		parser.load(new FileInputStream("config/server.ini"));
		HashData temp = HashData.getSingleton().getTempHashData();
		temp.serverName = parser
				.getProperty("server_name", "Shard Revolutions");
		int lines = Integer.parseInt(parser.getProperty("motd_lines", "0"));
		temp.welcomeMessage = new String[lines];
		for (int i = 0; i < lines; i++) {
			temp.welcomeMessage[i] = parser.getProperty("motd_line" + i, "");
		}
		HashData.getSingleton().commit();
	}

}
