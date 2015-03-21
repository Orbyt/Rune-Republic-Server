package com.rs.worldserver.util.log;

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

import java.io.PrintStream;
import java.text.DecimalFormat;

import com.rs.worldserver.Server;

/**
 * Logging class.
 * 
 * @author Graham
 * 
 */
public class Log extends PrintStream {

	private final DecimalFormat format = new DecimalFormat("00");
	private final long startTime = System.currentTimeMillis();

	public Log(PrintStream out) {
		super(out);
	}

	public void print(String s) {
		String f = prefix() + s;
		super.print(f);
		if (Server.getGui() != null) {
			Server.getGui().log(f);
		}
	}

	private String prefix() {
		return "[" + timeSince(startTime) + "]: ";
	}

	public final String timeSince(long time) {
		int seconds = (int) ((System.currentTimeMillis() - time) / 1000);
		int minutes = (int) (seconds / 60);
		int hours = (int) (minutes / 60);
		int days = (int) (hours / 24);
		String dayStr = "";
		if (days > 0)
			dayStr = days + " days, ";
		String s = null;
		synchronized (format) {
			s = dayStr + format.format(hours % 24) + ":"
					+ format.format(minutes % 60) + ":"
					+ format.format(seconds % 60);
		}
		return s;
	}

}
