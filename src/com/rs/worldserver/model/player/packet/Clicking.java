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
import com.rs.worldserver.world.PlayerManager;


/**
 * Clicking some stuff in game
 * 
 * @author Graham
 * 
 */
public class Clicking implements Packet {

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		@SuppressWarnings("unused")
		int interfaceID = client.getInStream().readUnsignedWordA();
		
		Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
		if(o != null) {
			if(client.duelStatus >= 1 && client.duelStatus <= 4) {
				client.getActionAssistant().declineDuel();
				o.getActionAssistant().declineDuel();
				client.inCombat = false;
				o.inCombat = false;
				o.ClickCount = 6;
				client.ClickCount = 6;
			}
		}
		/* if(client.duelStatus == 6) {
			client.getActionAssistant().claimStakedItems();	
		} */
		//client.appearanceFix();
		if(client.banking) {
			client.inCombat = false;
			client.banking = false;
			client.bankOK = false;
			client.getActionAssistant().lockMiniMap(false);
			client.logoutDelay = System.currentTimeMillis();
		}		
		client.cancelTasks();
		//PlayerManager.getSingleton().saveGame(client);

	}

}
