package com.rs.worldserver.model.object;

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
 * Represents an in-game object.
 * 
 * @author Graham
 * 
 */
public class GameObject {

	private ObjectDefinition definition;

	public static enum Face {
		NORTH, EAST, SOUTH, WEST,
	};

	private Face face = Face.NORTH;

	private int absX, absY, heightLevel;
	private int type;

	public GameObject(int absX, int absY, int heightLevel, Face face, int type,
			ObjectDefinition definition) {
		this.absX = absX;
		this.absY = absY;
		this.heightLevel = heightLevel;
		this.face = face;
		this.definition = definition;
		this.type = type;
	}

	public ObjectDefinition getDefinition() {
		return this.definition;
	}

	public Face getFace() {
		return this.face;
	}

	public int getFaceID() {
		if (face == Face.SOUTH)
			return -3;
		if (face == Face.EAST)
			return -2;
		if (face == Face.NORTH)
			return -1;
		return 0;// west
	}

	public int getAbsX() {
		return absX;
	}

	public int getAbsY() {
		return absY;
	}

	public int getHeightLevel() {
		return heightLevel;
	}

	public int getType() {
		return type;
	}

}
