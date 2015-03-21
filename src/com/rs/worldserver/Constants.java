package com.rs.worldserver;

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
 * Global constants
 * 
 * @author Graham
 * 
 */
public class Constants {	/**
	 * Spawn point X coordinate.
	 */
	public static final int SPAWN_X = 3217;

	/**
	 * Spawn point Y coordinate.
	 */
	public static final int SPAWN_Y = 3448;

	/**
	 * Spawn point Z coordinate.
	 */
	public static final int SPAWN_Z = 0;

	/**
	 * Maximum connections per individual IP address.
	 */
	public static final int MAX_CONNECTIONS_PER_IP = 4;

	/**
	 * The cycle time.
	 */
	public static final int CYCLE_TIME = 500;
	public static byte[] Orbit = {-1,-1,-1,-1,-1
	};
	
	public static byte[] James = {-1,-1,-1,-1
	};
	/**
	 * Packet sizes.
	 */
	public static final int PACKET_SIZES[] = { 
			0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 0, // 50
			0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, -1, -1, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
			0, 0, 6, 6, 0, 0, 0 // 250d
	};

	/**
	 * Buffer size.
	 */
		public static final int INITIAL_BUFFER_SIZE = 2048;
	//public static final int INITIAL_BUFFER_SIZE = 2048;

	/**
	 * Port.
	 */
	public static final int PORT = 43594; // 43594=default

	// Remark: the player structures are just a temporary solution for now
	// Later we will avoid looping through all the players for each player
	// by making use of a hash table maybe based on map regions (8x8 granularity
	// should be ok)
	public static final int MAXIMUM_PLAYERS = 825;

	/**
	 * Experience rate
	 */
	public static final int EXP_RATE = 43;

	// walking related stuff - walking queue etc...
	public static final int WALKING_QUEUE_SIZE = 50;

	/**
	 * Ingame timeout in seconds.
	 */
	public static final int INGAME_TIMEOUT = 300; 

	public static final int MAX_ITEM_AMOUNT = 2000000000;

	public static final String[] BONUS_NAME = { "Stab", "Slash", "Crush",
			"Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range",
			"Strength", "Prayer" };

}
