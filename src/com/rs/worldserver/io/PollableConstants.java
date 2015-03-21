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

/**
 * Class representing a connection to a MySQL database
 * 
 * @author thiefmn6092
 */

public interface PollableConstants {
	public static long IO_STATUS_NOT_STARTED = 0;
	public static long IO_STATUS_IN_PROGRESS = 1;
	public static long IO_STATUS_FAILED = 2;
	public static long IO_STATUS_FINISHED = 3;
}