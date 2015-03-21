package com.rs.worldserver.model;

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

import com.rs.worldserver.model.player.Client;

public class FloorItem extends Item {

	private long droppedAt;
	private Client droppedBy;
	public int x;
	public int y;
	private int height;
	private boolean isSpawn;
	private boolean canTake;

	public Client getDroppedBy() {
		return droppedBy;
	}

	public void setPos(int x, int y, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
	}

	public long getDroppedAt() {
		return droppedAt;
	}

	public void resetOwner() {
		droppedBy = null;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public FloorItem(int id, int amount, Client player, int x, int y, int height) {
		super(id, amount);
		this.droppedAt = System.currentTimeMillis();
		this.droppedBy = player;
		this.x = x;
		this.y = y;
		this.height = height;
		this.isSpawn = false;
		this.canTake = true;
	}

	public void setSpawn(boolean val) {
		isSpawn = val;
	}

	public boolean isSpawn() {
		return isSpawn;
	}
	
	public boolean canPickup() {
		return canTake;
	}
	
	public void setTaken() {
		this.canTake=false;
	}

}
