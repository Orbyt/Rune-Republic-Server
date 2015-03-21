package com.rs.worldserver.model.npc;

import java.util.ArrayList;
import java.util.List;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.Client;

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
 * An NPC type
 * 
 * @author Graham
 * 
 */
public class NPCDefinition {

	/**
	 * The type id.
	 */
	public int type;

	/**
	 * Combat level.
	 */
	public int combat;

	/**
	 * Health.
	 */
	private int health;

	/**
	 * Name.
	 */
	public String name;

	/**
	 * Respawn timer.
	 */
	private int respawn;

	/**
	 * Drops.
	 */
	private List<NPCDrop> drops;

	/**
	 * Construct the NPC definition
	 * 
	 * @param type
	 * @param combat
	 * @param health
	 * @param name
	 * @param respawn
	 */
	public NPCDefinition(int type, int combat, int health, String name,
			int respawn) {
		this.type = type;
		this.combat = combat;
		this.health = health;
		this.name = name;
		this.drops = new ArrayList<NPCDrop>();
		this.respawn = respawn;
	}

	/**
	 * Add a drop.
	 * 
	 * @param drop
	 */
	public void addDrop(NPCDrop drop) {
		drops.add(drop);
	}

	/**
	 * Gets the npc type
	 * 
	 * @return
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Gets the npc combat
	 * 
	 * @return
	 */
	public int getNPCCombat() {
		return this.combat;
	}

	/**
	 * Gets the npc health
	 * 
	 * @return
	 */
	public int getHealth() {
		return this.health;
	}

	/**
	 * Gets the npc name
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the npc drops
	 * 
	 * @return
	 */
	public List<NPCDrop> getDrops() {
		return this.drops;
	}

	/**
	 * Gets the respawn time.
	 * 
	 * @return
	 */
	public int getRespawn() {
		return this.respawn;
	}

}
