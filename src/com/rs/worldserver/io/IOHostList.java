package com.rs.worldserver.io;

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

import java.util.Hashtable;
import java.util.Map;

/**
 * A map of hosts and the number of connections from that host. This helps to
 * stop SYIpkpker.
 * 
 * YOU MUST SYNCHRONIZE THIS YOURSELF.
 * 
 * @author Graham
 * 
 */
public class IOHostList {

	public static Map<String, Integer> socketList = new Hashtable<String, Integer>();

	public static void add(String remoteAddress) {
		Integer ct = socketList.get(remoteAddress);
		if (ct == null) {
			ct = 1;
		} else {
			ct++;
		}
		socketList.put(remoteAddress, ct);
	}

	public static void remove(String remoteAddress) {
		if (socketList.containsKey(remoteAddress)) {
			int ct = socketList.get(remoteAddress);
			ct--;
			if (ct == 0) {
				socketList.remove(remoteAddress);
			} else {
				socketList.put(remoteAddress, ct);
			}
		}
	}

	public static boolean has(String remoteAddress, int checkCount) {
		Integer count = socketList.get(remoteAddress);
		if (count == null)
			return false;
		if (count >= checkCount /* checkCount */) {
			return true;
		}
		return false;
	}

}