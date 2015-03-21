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
 * Represents an action to do after a dialogue message.
 * 
 * @author Graham
 * 
 */
public class DialogueAction {

	/**
	 * The type of action.
	 * 
	 * @author Graham
	 * 
	 */
	public enum Type {
		CLOSE, // end the dialogue
		OPEN_BANK, // open player bank
		OPEN_SHOP, // open a shop
		NEXT, // next message
		TELE,
		TEL2,
		DICE,
		BLOODLUSTT,
		BLOODLUSTP,
		EXCHANGE,
		SLAYER,
	}

	/**
	 * Type of action.
	 */
	private Type type;

	/**
	 * Extra data for this action.
	 */
	private Map<String, Object> extraData;

	/**
	 * Creates the dialogue action object with the specified type.
	 * 
	 * @param type
	 */
	public DialogueAction(Type type) {
		this.type = type;
		this.extraData = new HashMap<String, Object>();
	}

	/**
	 * Sets the shop id, if the type is Type.OPEN_SHOP
	 * 
	 * @param shop
	 * @throws Exception
	 */
	public void setShopId(int shop) throws Exception {
		if (type != Type.OPEN_SHOP) {
			throw new Exception("Extra data invalid for current type");
		}
		extraData.put("shopId", shop);
	}

	/**
	 * Gets the shop id, if the type is Type.OPEN_SHOP.
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getShopId() throws Exception {
		if (type != Type.OPEN_SHOP) {
			throw new Exception("Extra data invalid for current type.");
		}
		return (Integer) extraData.get("shopId");
	}

	/**
	 * Sets the next message id, if the type is Type.NEXT.
	 * 
	 * @param nextMessage
	 * @throws Exception
	 */
	public void setNextMessage(int nextMessage) throws Exception {
		if (type != Type.NEXT) {
			throw new Exception("Extra data invalid for current type");
		}
		extraData.put("nextMessage", nextMessage);
	}

	/**
	 * Gets the next message id, if the type is Type.NEXT.
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getNextMessage() throws Exception {
		if (type != Type.NEXT) {
			throw new Exception("Extra data invalid for current type");
		}
		return (Integer) extraData.get("nextMessage");
	}

	/**
	 * Gets the type.
	 * 
	 * @return
	 */
	public Type getType() {
		return this.type;
	}

}
