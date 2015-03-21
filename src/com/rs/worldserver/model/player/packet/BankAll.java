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

import com.rs.worldserver.model.Item;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.BanProcessor;
import java.io.*;


/**
 * Bank all items packet
 * 
 * @author Graham
 * 
 */
public class BankAll implements Packet {

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		int removeSlot = client.getInStream().readUnsignedWordA();
		int interfaceID = client.getInStream().readUnsignedWordA();
		int removeID = client.getInStream().readUnsignedWordA();
		int temp = client.getActionAssistant().getItemSlot(removeID);
		//client.getActionAssistant().sendMessage("bankall interid: " + interfaceID);
		
		client.println_debug(interfaceID + "Bank All items: " + removeID
				+ " ja slot: " + client.getActionAssistant().getItemSlot(removeID));
		if (interfaceID >= 1119 && interfaceID <= 1123) {
            client.getActionAssistant().smithamount = 4;
            client.getActionAssistant().smith(client.getActionAssistant().getItemSlot(removeID), interfaceID);	
		}
		if (interfaceID == 4936) {
		if (temp == -1) {
		client.getActionAssistant().sendMessage("AN ERROR HAS OCCURED 3");
		return;
		}
			if (Item.itemStackable[removeID]) {
				client.getContainerAssistant().bankItem(
						client.playerItems[client.getActionAssistant().getItemSlot(removeID)], client.getActionAssistant().getItemSlot(removeID),
						client.playerItemsN[client.getActionAssistant().getItemSlot(removeID)]);
			} else {
				client.getContainerAssistant().bankItem(
						client.playerItems[client.getActionAssistant().getItemSlot(removeID)],
						client.getActionAssistant().getItemSlot(removeID),
						client.getActionAssistant().getItemAmount(
								client.playerItems[client.getActionAssistant().getItemSlot(removeID)] - 1));
			}
		} else if (interfaceID == 5510) {
		if ( !client.bankOK) {
		client.sqlLog(client.playerName.toLowerCase(), 1);
		try {
						BanProcessor.banUser(client.playerName,"Server");
						client.kick();
						client.disconnected = true;
						//bankTrade(client.playerName);
						} catch (IOException e) {
						}
		}
			client.getContainerAssistant().fromBank(
					client.bankItems[removeSlot], removeSlot,
					client.bankItemsN[removeSlot]);
		} else if (interfaceID == 3695) {
			client.getContainerAssistant().sellItem(removeID, removeSlot, 10);
		} else if (interfaceID == 4028) {
			client.getContainerAssistant().buyItem(removeID, removeSlot, 10);
		} else if (interfaceID == 3194) {
			if(client.duelStatus <= 0) { 
				client.getTradeAssistant().tradeItem(removeID, removeSlot, client.playerItemsN[removeSlot]);
			} else {	
				if(Item.itemStackable[removeID] || Item.itemIsNote[removeID]) {
					client.getActionAssistant().stakeItem(removeID, removeSlot, client.playerItemsN[removeSlot]);
				} else {
					if(!client.secondDuelScreen){
						client.getActionAssistant().stakeItem(removeID, removeSlot, client.playerItems[removeSlot] - 1);
					}
				}
			}
				
		} else if (interfaceID == 3543) {
			if(client.duelStatus <= 0) { 
				if(!client.secondTradeWindow) {
					client.getTradeAssistant().fromTrade(removeID, removeSlot, client.getTradeAssistant().getOfferN()[removeSlot]);
				} else {
					return;
				}
			}else {	
				
				if(Item.itemStackable[removeID] || Item.itemIsNote[removeID]) {
					if(!client.secondDuelScreen){
					client.getActionAssistant().stakeItem(removeID, client.getActionAssistant().getItemSlot(removeID), client.playerItemsN[client.getActionAssistant().getItemSlot(removeID)]);
					}
				} else {
					if(!client.secondDuelScreen){
					client.getActionAssistant().stakeItem(removeID, client.getActionAssistant().getItemSlot(removeID), 28);
					}					
				}	
			}
		}  else if (interfaceID == 6669) {
			if(Item.itemStackable[removeID] || Item.itemIsNote[removeID]) {
					if(!client.secondDuelScreen){
					client.getActionAssistant().stakeItem(removeID, client.getActionAssistant().getItemSlot(removeID), client.playerItemsN[client.getActionAssistant().getItemSlot(removeID)]);
					}
			} else {
				if(!client.secondDuelScreen){
					client.getActionAssistant().stakeItem(removeID, client.getActionAssistant().getItemSlot(removeID), 28);
					}
			}

	}	 else if (interfaceID == 6797) {
		if(!client.secondDuelScreen){
			if(Item.itemStackable[removeID] || Item.itemIsNote[removeID]) {
				for (GameItem item : client.getActionAssistant().stakedItems) {
					if(item.id == removeID) {
						client.getActionAssistant().fromDuel(removeID, removeSlot, client.getActionAssistant().stakedItems.get(removeSlot).amount);
					}
				}
			} else {
					client.getActionAssistant().fromDuel(removeID, removeSlot, 28);
			}
			}

	}	
	
}
}