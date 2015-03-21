package com.rs.worldserver.model.player.packet;

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

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.Config;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.world.ItemManager;
import com.rs.worldserver.model.player.DoorManager;
import com.rs.worldserver.Server;

/**
 * Loading complete packet.
 * 
 * @author Graham
 * 
 */
public class LoadingComplete implements Packet {

	public static final int GAME_LOAD = 121, AREA_LOAD = 210;

	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
	if(packetType == GAME_LOAD) {
	 if(!c.npcClick){ 
           c.getCombat().resetAttack(); 
	  } 
			if(c.skullTimer > 0) {
				c.isSkulled = true;
				c.skullIcon = 0;
			}
		c.deletethatdoor(2543, 10143);	
		c.deletethatwall(2543, 10143);
		
		c.deletethatdoor(2965, 3381);	
		c.deletethatwall(2965, 3381);
		
		//c.doZoning();
		Server.doorManager.loadDoors(c);
		//Server.getItemManager().update(c);
		//Server.getItemManager().reloadItems(c);
		
	}
	if(packetType == AREA_LOAD) {
	 if(!c.npcClick){ 
           c.getCombat().resetAttack(); 
	  } 
		c.doZoning();
		Server.doorManager.loadDoors(c);
			if(c.skullTimer > 0) {
				c.isSkulled = true;
				c.skullIcon = 0;
			}
	}
} 

}
