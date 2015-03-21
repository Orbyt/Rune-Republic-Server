package com.rs.worldserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Player;

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
 * Represents a single shop.
 * 
 * @author Graham
 * 
 */
public class Shop extends ItemContainer {

	public static enum Type {
		GENERAL, SPECIALIST
	}

	private int id;
	private String name;
	private Type type;
	private int currency;

	private static final int MAX_SHOP_ITEMS = 100;

	/**
	 * Create a shop.
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param currency
	 */
	public Shop(int id, String name, Type type, int currency) {
		super(MAX_SHOP_ITEMS);
		this.id = id;
		this.name = name;
		this.type = type;
		this.currency = currency;
	}

	/**
	 * Gets this shops id.
	 * 
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets this shops name.
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets this shops type.
	 * 
	 * @return
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Updates stock etc.
	 */
	public void process() {
		List<Integer> remove = new ArrayList<Integer>();
		for (Map.Entry<Integer, Item> entry : items.entrySet()) {
			if (entry.getValue() instanceof ShopItem) {
				ShopItem si = (ShopItem) entry.getValue();
				if (si.updateStock()) {
					remove.add(entry.getKey());
				}
			}
		}
		for (int slot : remove) {
			this.removeItem(slot);
		}
		updated();
	}

	/**
	 * Gets the current shop currency.
	 * 
	 * @return
	 */
	public int getCurrency() {
		return this.currency;
	}

	/**
	 * Is an item sellable?
	 * 
	 * @param id
	 * @return
	 */
	public boolean isItemSellable(int id) {
		if (type == Type.GENERAL)
			return true;
		for (Map.Entry<Integer, Item> entry : items.entrySet()) {
			if (entry.getValue() instanceof ShopItem) {
				ShopItem si = (ShopItem) entry.getValue();
				if (si.getId() == id)
					return true;
			}
		}
		return false;
	}

	/**
	 * Gets the normal number of an item.
	 * 
	 * @param id
	 * @return
	 */
	public int normalNumberOf(int id) {
		int ct = 0;
		for (Map.Entry<Integer, Item> entry : items.entrySet()) {
			if (!(entry.getValue() instanceof ShopItem))
				continue;
			ShopItem si = (ShopItem) entry.getValue();
			if (si.getId() == id) {
				ct += si.getNormalAmount();
			}
		}
		return ct;
	}

	/**
	 * Gets an item buy value.
	 * 
	 * @param removeID
	 * @return
	 */
	public int getItemBuyValue(int id) {
		double shopValue = Server.getItemManager().getItemDefinition(id)
				.getShopValue();
		double overstock = numberOf(id) - normalNumberOf(id);
		double totalPrice = shopValue * 1;//1.26875;
		/*if (overstock > 0) {
			totalPrice -= ((shopValue / 100) * (1.26875 * overstock));
		} else if (overstock < 0) {
			totalPrice += ((shopValue / 100) * (1.26875 * overstock));
		}*/
		if (type == Type.GENERAL) {
			totalPrice *= 1;//1.25;
			totalPrice *= 1;//0.4;
		} else {
			totalPrice *= 1;//0.6;
		}
		/*double shopValue = Server.getItemManager().getItemDefinition(id)
				.getShopValue();
		double overstock = numberOf(id) - normalNumberOf(id);
		double totalPrice = shopValue * 1.26875;
		if (overstock > 0) {
			totalPrice -= ((shopValue / 100) * (1.26875 * overstock));
		} else if (overstock < 0) {
			totalPrice += ((shopValue / 100) * (1.26875 * overstock));
		}
		if (type == Type.GENERAL) {
			totalPrice *= 1.25;
			totalPrice *= 0.4;
		} else {
			totalPrice *= 0.6;

		}*/
		return (int) Math.ceil(totalPrice);
	}

	/**
	 * Gets an item buy value.
	 * 
	 * @param removeID
	 * @return
	 */
	public int getItemSellValue(int id) {
		double shopValue = Server.getItemManager().getItemDefinition(id)
				.getShopValue();
		double overstock = numberOf(id) - normalNumberOf(id);
		double totalPrice = shopValue * 1;//.26875;
		/*if (overstock > 0) {
			totalPrice -= ((shopValue / 100) * (1.26875 * overstock));
		} else if (overstock < 0) {
			totalPrice += ((shopValue / 100) * (1.26875 * overstock));
		}*/
		if (type == Type.GENERAL) {
			totalPrice *= 1;//.25;
		} else {
			totalPrice *= 1;//0.6;
		}/*
		double shopValue = Server.getItemManager().getItemDefinition(id)
				.getShopValue();
		double overstock = numberOf(id) - normalNumberOf(id);
		double totalPrice = shopValue * 1.26875;
		if (overstock > 0) {
			totalPrice += ((shopValue / 100) * (1.26875 * overstock));
		} else if (overstock < 0) {
			totalPrice -= ((shopValue / 100) * (1.26875 * overstock));
		}
		if (type == Type.GENERAL) {
			totalPrice *= 1.25;
		} else {
			totalPrice *= 0.6;
		}*/
		return (int) Math.floor(totalPrice);
	}

	/**
	 * We've updated.
	 */
	public void updated() {
		for (Player p : Server.getPlayerManager().getPlayers()) {
			if (p == null)
				continue;
			if (p.isActive && !p.disconnected) {
				Client c = (Client) p;
				if (c.getExtraData().containsKey("shop")) {
					if (((Integer) c.getExtraData().get("shop")) == id) {
						c.getActionAssistant().resetShop(this);
					}
				}
			}
		}
	}

}
