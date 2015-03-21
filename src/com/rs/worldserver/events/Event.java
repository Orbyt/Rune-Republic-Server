package com.rs.worldserver.events;

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
 * A simple interface for an event.
 * 
 * @author Graham
 * 
 */
public interface Event {

	/**
	 * Called when the event is executed.
	 * 
	 * @param container
	 *            The event container, so the event can dynamically change the
	 *            tick time etc.
	 */
	public void execute(EventContainer container);

	/**
	 * Called when an event is stopped.
	 */
	public void stop();

}
