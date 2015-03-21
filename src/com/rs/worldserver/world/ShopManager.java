package com.rs.worldserver.world;

import com.rs.worldserver.Config;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rs.worldserver.model.Shop;
import com.rs.worldserver.model.ShopItem;

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
 * Manages shops
 * 
 * @author Graham
 * 
 */
public class ShopManager {

	private Map<Integer, Shop> shops;

	public static final int RESTOCK_DELAY = 60000;

	public ShopManager() throws IOException {
		this.shops = new HashMap<Integer, Shop>();
		loadShops("config/shops/shops.cfg");
	}
	/**
	 * Loads spawns from a file.
	 * 
	 * @param name
	 * @throws IOException
	 */
	private void loadShops(String name) throws IOException {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(name));
			while (true) {
				String line = file.readLine();
				if (line == null)
					break;
				int spot = line.indexOf('=');
				if (spot > -1) {
					String values = line.substring(spot + 1);
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.trim();
					String[] valuesArray = values.split("\t");
					int id = Integer.valueOf(valuesArray[0]);
					String sname = valuesArray[1];
					String type = valuesArray[2];
					int currency = Integer.valueOf(valuesArray[3]);
					Shop.Type t = Shop.Type.GENERAL;
					if (type.equals("SPECIALIST"))
						t = Shop.Type.SPECIALIST;
					Shop s = new Shop(id, sname, t, currency);
					int ptr = 4;
					while (true) {
						if (ptr >= valuesArray.length) {
							break;
						}
						int itemId = Integer.valueOf(valuesArray[ptr++]);
						int itemAmt = Integer.valueOf(valuesArray[ptr++]);
						s.addItem(new ShopItem(itemId, itemAmt, itemAmt));
					}
					shops.put(id, s);
				}
			}
			System.out.println("Loaded " + shops.size() + " shops.");
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Gets the shops.
	 * 
	 * @return
	 */
	public Map<Integer, Shop> getShops() {
		return shops;
	}

	/**
	 * Processes shops.
	 */
	public void process() {
		for (Map.Entry<Integer, Shop> entry : shops.entrySet()) {
			entry.getValue().process();
		}
	}

}
