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

/**
 * A type of item.
 * 
 * @author Graham
 * 
 */
public class ItemDefinition {

	private int id;
	public String name;
	private double shopValue;
	private double lowAlch;
	private double highAlch;
	private int[] bonuses;
	private String description;

	public ItemDefinition(int id, String name, String description,
			double shopValue, double lowAlch, double highAlch, int[] bonuses) {
		this.id = id;
		this.name = name;
		this.shopValue = shopValue;
		this.lowAlch = lowAlch;
		this.highAlch = highAlch;
		this.bonuses = bonuses;
		this.description = description;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public double getShopValue() {
		return this.shopValue;
	}

	public double getLowAlch() {
		return this.lowAlch;
	}

	public double getHighAlch() {
		return this.highAlch;
	}

	public int[] getBonuses() {
		return this.bonuses;
	}

	public int getBonus(int i) {
		return this.bonuses[i];
	}

	public String getDescription() {
		return this.description;
	}

}
