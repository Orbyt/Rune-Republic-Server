package com.rs.worldserver.hash;

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
 * Holds global server data.
 * 
 * @author Graham
 * 
 */
public class HashData {

	private HashData() {
	}

	private static HashData singleton = null;

	public static HashData getSingleton() {
		if (singleton == null) {
			singleton = new HashData();
		}
		return singleton;
	}

	private static HashData tempHashData = null;

	public HashData getTempHashData() {
		tempHashData = new HashData();
		return tempHashData;
	}

	public void commit() {
		singleton = tempHashData;
		tempHashData = null;
	}

	public String serverName;
	public String[] welcomeMessage;

}
