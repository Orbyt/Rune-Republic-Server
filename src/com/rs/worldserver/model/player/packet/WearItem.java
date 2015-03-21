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
import com.rs.worldserver.model.player.*;
/**
 * Wear items packet
 * 
 * @author Graham
 * 
 */
public class WearItem implements Packet {

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		int wearID = client.getInStream().readUnsignedWord();
		int wearSlot = client.getInStream().readUnsignedWordA();
		@SuppressWarnings("unused")
		int interfaceID = client.getInStream().readUnsignedWordA();
		client.println_debug("WearItem: " + wearID + " slot: " + wearSlot);
		switch(wearID){
			case 4079:
				client.getActionAssistant().startAnimation(1458);
			break;
			case 10102: // rock
			if(client.duelRule[6]){
				client.getActionAssistant().sendMessage("Food has been disabled in this duel!");
				return;
			}
		//	client.getActionAssistant().eatFood(23, 1600, itemId, itemSlot);
			if(client.playerLevel[3] + 10 >= 110) {
				client.playerLevel[3] = 110;
			} 
			else if (client.playerLevel[3] + 10 >= (client.getLevelForXP(client.playerXP[3]) +10)) {
				client.playerLevel[3] = client.getLevelForXP(client.playerXP[3]) + 10;
			}
			break;
		default:
		client.getActionAssistant().wear(wearID, wearSlot);
		client.getCombatManager().calculateBonus();
		client.getSpecials().specialBar();
		//client.specOn = false;
		client.getActionAssistant().requestUpdates();
		}
	}

}
