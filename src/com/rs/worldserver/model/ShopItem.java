package com.rs.worldserver.model;

import com.rs.worldserver.world.ShopManager;

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
 * Represents an item being sold in a shop
 * 
 * @author Graham
 * 
 */
public class ShopItem extends Item {

	/**
	 * The normal amount of items that are in the shop (so restocking/player
	 * stock going down can work).
	 */
	private int normalAmount;

	/**
	 * The last 'automatic' stock change.
	 */
	private long lastAutomaticStockChange;

	/**
	 * Is this item always stocked?
	 */
	private boolean isAlwaysStocked;

	/**
	 * Creates this shop item. Shop items created this way are automatically
	 * stocked.
	 * 
	 * @param id
	 *            The id.
	 * @param amount
	 *            The initial amount.
	 * @param normalAmount
	 *            The normal amount.
	 */
	public ShopItem(int id, int amount, int normalAmount) {
		super(id, amount);
		this.isAlwaysStocked = true;
		this.normalAmount = normalAmount;
		this.lastAutomaticStockChange = System.currentTimeMillis();
	}

	/**
	 * Creates this shop item. Shop items created this way are NOT automatically
	 * stocked.
	 * 
	 * @param id
	 *            The id.
	 * @param amount
	 *            The initial amount.
	 * @param normalAmount
	 *            The normal amount.
	 */
	public ShopItem(int id, int amount) {
		super(id, amount);
		this.isAlwaysStocked = false;
		this.lastAutomaticStockChange = System.currentTimeMillis();
	}

	/**
	 * Gets the normal amount.
	 * 
	 * @return
	 */
	public int getNormalAmount() {
		return this.normalAmount;
	}

	/**
	 * Gets the last automatic stock chage.
	 * 
	 * @return
	 */
	public long getLastAutomaticStockChange() {
		return this.lastAutomaticStockChange;
	}

	/**
	 * Updates the stock.
	 * 
	 * @return true if we should be removed, false if not.
	 */
	public boolean updateStock() {
		if (this.lastAutomaticStockChange + ShopManager.RESTOCK_DELAY < System
				.currentTimeMillis()) {
			this.lastAutomaticStockChange = System.currentTimeMillis();
			if (this.isAlwaysStocked) {
				if (this.getAmount() > this.normalAmount) {
					this.setAmount(this.getAmount() - 1);
				} else if (this.getAmount() < this.normalAmount) {
					this.setAmount(this.getAmount() + 1);
				}
				return false;
			} else {
				this.setAmount(this.getAmount() - 1);
				if (this.getAmount() <= 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Is this item always automatically stocked?
	 * 
	 * @return
	 */
	public boolean isAlwaysStocked() {
		return this.isAlwaysStocked;
	}

	public void setLastAutomaticStockChange(long currentTimeMillis) {
		this.lastAutomaticStockChange = currentTimeMillis;
	}

}
