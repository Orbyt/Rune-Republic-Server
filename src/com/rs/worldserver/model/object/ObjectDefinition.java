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
 * Object definition
 * 
 * @author Graham
 * 
 */
public class ObjectDefinition {

	private int type;
	private String name;
	private String description;
	private int xsize;
	private int ysize;

	public ObjectDefinition(int type, String name, String description,
			int xsize, int ysize) {
		this.type = type;
		this.name = name;
		this.description = description;
		this.xsize = xsize;
		this.ysize = ysize;
	}

	public int getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public int getXSize() {
		return this.xsize;
	}

	public int getYSize() {
		return this.ysize;
	}

}
