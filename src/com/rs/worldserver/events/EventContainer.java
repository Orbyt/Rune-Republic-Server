package com.rs.worldserver.events;
import com.rs.worldserver.Server;
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
 * Holds extra data for an event (for example the tick time etc).
 * 
 * @author Graham
 * 
 */
public class EventContainer {

	/**
	 * The tick time in milliseconds.
	 */
	private int tick;
	// event label
	private String Label;
	/**
	 * The actual event.
	 */
	private Event event;

	/**
	 * A flag which specifies if the event is running;
	 */
	private boolean isRunning;

	/**
	 * When this event was last run.
	 */
	private long lastRun;

	/**
	 * The owner.
	 */
	private Object owner;

	/**
	 * The event container.
	 * 
	 * @param owner
	 * @param evt
	 * @param tick
	 */
	protected EventContainer(Object owner, Event evt, int tick,String label) {
		this.owner = owner;
		this.tick = tick;
		this.event = evt;
		this.Label = label;
		this.isRunning = true;
		this.lastRun = System.currentTimeMillis();
		Server.EventCount++; // ERIC
	}

	/**
	 * Set tick.
	 * 
	 * @param tick
	 */
	public void setTick(int tick) {
		this.tick = tick;
	}
	public void setOwner(Object s){
		this.owner = s;
	}

	/**
	 * Gets the owner of the object.
	 * 
	 * @return
	 */
	public Object getOwner() {
		return this.owner;
	}
	public String getLabel(){
		return this.Label;
	}
	/**
	 * Stops this event.
	 */
	public void stop() {
		if(this.isRunning) Server.EventCount--; // ERIC
		this.isRunning = false;
		this.event.stop();
	}

	/**
	 * Returns the is running flag.
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * Returns the tick time.
	 * 
	 * @return
	 */
	public int getTick() {
		return this.tick;
	}

	/**
	 * Executes the event!
	 */
	public void execute() {
		this.lastRun = System.currentTimeMillis();
		this.event.execute(this);
	}

	/**
	 * Gets the last run time.
	 * 
	 * @return
	 */
	public long getLastRun() {
		return this.lastRun;
	}

}
