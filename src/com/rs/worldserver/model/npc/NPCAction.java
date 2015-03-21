package com.rs.worldserver.model.npc;

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

import java.util.HashMap;
import java.util.Map;

/**
 * NPC actions
 * 
 * @author Graham
 * 
 */
public class NPCAction {

	/**
	 * The type of action.
	 * 
	 * @author Graham
	 * 
	 */
	public enum Type {
		OPEN_BANK, // open player bank
		OPEN_SHOP, // open a shop
		TALK, // start a dialogue
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
	public NPCAction(Type type) {
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
	public void setTalkDialogueId(int nextMessage) throws Exception {
		if (type != Type.TALK) {
			throw new Exception("Extra data invalid for current type");
		}
		extraData.put("talkDialogueId", nextMessage);
	}

	/**
	 * Gets the next message id, if the type is Type.NEXT.
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getTalkDialogueId() throws Exception {
		if (type != Type.TALK) {
			throw new Exception("Extra data invalid for current type");
		}
		return (Integer) extraData.get("talkDialogueId");
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
