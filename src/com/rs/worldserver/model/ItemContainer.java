package com.rs.worldserver.model;

import java.util.HashMap;
import java.util.Map;

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
 * An item container
 * 
 * @author Graham
 * 
 */
public class ItemContainer {

	/**
	 * A map of slot ID to item data.
	 */
	protected Map<Integer, Item> items;

	/**
	 * The number of slots in the container.
	 */
	private int containerSize;

	/**
	 * Initialises the container, reserving memory for a set size.
	 * 
	 * @param size
	 *            The size of the container.
	 */
	public ItemContainer(int size) {
		items = new HashMap<Integer, Item>(size);
		containerSize = size;
	}

	/**
	 * Adds an item to the container.
	 * 
	 * @param item
	 *            The item to add.
	 */
	public void addItem(Item item) {
		int slot = getFreeSlot();
		if (slot != -1) {
			items.put(slot, item);
		} else {
			// TODO inform player that their inventory is full
		}
	}

	public int getFreeSlot() {
		for (int i = 0; i < containerSize; i++) {
			if (!items.containsKey(i))
				return i;
		}
		return -1;
	}

	/**
	 * Removes the item in the specified slot.
	 * 
	 * @param slot
	 *            The slot.
	 */
	public void removeItem(int slot) {
		items.remove(slot);
	}

	/**
	 * Get item by slot.
	 * 
	 * @param slot
	 * @return
	 */
	public Item getItemBySlot(int slot) {
		return items.get(slot);
	}

	/**
	 * Gets the next slot for the specified item.
	 * 
	 * @return The item slot, or -1 if it does not exist.
	 */
	public int getItemSlot(int id) {
		for (Map.Entry<Integer, Item> entry : items.entrySet()) {
			if (entry.getValue().getId() == id) {
				return entry.getKey();
			}
		}
		return -1;
	}

	/**
	 * Gets the nubmer of an item.
	 * 
	 * @param id
	 * @return
	 */
	public int numberOf(int id) {
		int ct = 0;
		for (Map.Entry<Integer, Item> entry : items.entrySet()) {
			if (entry.getValue().getId() == id) {
				ct += entry.getValue().getAmount();
			}
		}
		return ct;
	}

	/**
	 * Gets the size of this container.
	 * 
	 * @return The size of this container.
	 */
	public int getContainerSize() {
		return containerSize;
	}

	public Map<Integer, Item> getMap() {
		return this.items;
	}

}
