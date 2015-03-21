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
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.Item;
import com.rs.worldserver.model.player.CastleWars;
/**
 * Drop item packet
 * 
 * @author Graham
 * 
 */
public class DropItem implements Packet {

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		int droppedItem = client.getInStream().readUnsignedWordA();
		client.getInStream().readUnsignedByte();
		client.getInStream().readUnsignedByte();
		int slot = client.getInStream().readUnsignedWordA();
		if (slot > 28 || slot < 0) { return; }
		client.println_debug("dropItem: " + droppedItem + " Slot: " + slot);
		if (client.inDuelArena()){
			client.getActionAssistant().sendMessage("This action is disabled while in duel arena.");
			return;		
		}		
		if(client.newFag > 0){
			client.getActionAssistant().sendMessage("You are still under new player protection this action can not be performed!" +(int)(client.newFag/2)+ " seconds remaining");
			return;		
		}		
		if(client.blackMarks  > 0) {
			client.getActionAssistant().sendMessage("This action is disabled while jailed");
			return;
		}
		if(PlayerManager.updateAnnounced || PlayerManager.updateRunning) {
			client.getActionAssistant().sendMessage("This action is disabled during updates.");
			return;
		}
		// if (Item.itemIsNote[(droppedItem)] == true) {
			// client.getActionAssistant().Send("You can not drop this item anymore to prevent abuse.");
			// return;
		// }
		
		if(droppedItem == 4037 ||droppedItem == 4039){
				client.getActionAssistant().deleteItem(client.playerItems[slot] - 1, slot,
				client.playerItemsN[slot]);
				Server.getCastleWars().dropFlag(client,droppedItem);
				return;
		}
   
		
		if(droppedItem == 13660 || droppedItem == 13661 || droppedItem == 13662 || 
		droppedItem == 13663 || droppedItem == 13664 || droppedItem == 13665 || 
		droppedItem == 13666 || droppedItem == 13667 || droppedItem == 13668 || 
		droppedItem == 13669 || droppedItem == 13670 || droppedItem == 13671 || 
		droppedItem == 2518 || droppedItem == 2520 || droppedItem == 2522 || 
		droppedItem == 2524 || droppedItem == 2526 || droppedItem == 6570  || droppedItem == 7806 || droppedItem == 7807 || droppedItem == 7808||
		droppedItem == 10551 || droppedItem == 7454 || droppedItem == 7455  || droppedItem == 7456 || droppedItem == 7457 || droppedItem == 7458||
		droppedItem == 7459 || droppedItem == 7460 || droppedItem == 7461  || droppedItem == 7462 || droppedItem == 7463 ||
		droppedItem == 11663 || droppedItem == 8839 || droppedItem == 7923  || droppedItem == 8840 || droppedItem == 11664 ||
		droppedItem == 7940 || droppedItem == 11665 || droppedItem == 7942  || droppedItem == 6656 || droppedItem == 8842 || droppedItem == 18353 ||
		droppedItem == 18351 || 
		droppedItem == 1038 ||
		droppedItem == 1039 ||
		droppedItem == 1040 ||
		droppedItem == 1041 ||
		droppedItem == 1042 ||
		droppedItem == 1043 ||
		droppedItem == 1044 ||
		droppedItem == 1045 || 
		droppedItem == 1046 ||
		droppedItem == 1047 ||
		droppedItem == 1048 || 
		droppedItem == 1049 ||
		droppedItem == 1050 ||
		droppedItem == 1051 ||
		droppedItem == 1052 ||
		droppedItem == 1053 ||
		droppedItem == 1054 ||
		droppedItem == 1055 ||
		droppedItem == 1056 ||
		droppedItem == 1057 ||
		droppedItem == 1058	||
		droppedItem == 13901 ||
		droppedItem == 13889 ||
		droppedItem == 13895 ||
		droppedItem == 13907 ||
		droppedItem == 13863 ||
		droppedItem == 13869 ||
		droppedItem == 13866 ||
		droppedItem == 13860 ||
		droppedItem == 13872 ||
		droppedItem == 13875 ||
		droppedItem == 13878 ||
		droppedItem == 13886 ||
		droppedItem == 13892 ||
		droppedItem == 13898 ||
		droppedItem == 13904 ||
		droppedItem == 7837 ||
		droppedItem == 7774 ||
		droppedItem == 1464 || droppedItem == 20772 || droppedItem == 20771 || droppedItem == 20773 || droppedItem == 21407
		
		) { 
			client.getActionAssistant().Send("You can not drop this item.");
			return;
		}
		if(Server.getFightPits().isInFightPitsWait(client) || Server.getFightPits().isInFightPitsGame(client)) {
					client.getActionAssistant().sendMessage("You can not drop items in fightpits.");
					return;
				}	
		if (client.inWild() == true){
			if(droppedItem == 229 || droppedItem == 391) {
				client.getActionAssistant().dropItem(droppedItem, slot);
				//PlayerManager.getSingleton().saveGame(client);
				return;
			} else {
				client.getActionAssistant().Send("You can not drop that in the wild.");
			}
			
		} else {
			client.getActionAssistant().dropItem(droppedItem, slot);
			//PlayerManager.getSingleton().saveGame(client);
		}
		
	}

}
