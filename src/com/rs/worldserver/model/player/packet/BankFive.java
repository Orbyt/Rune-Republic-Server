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
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.util.BanProcessor;
import java.util.Hashtable;
import java.io.*;

/**
 * Bank five items
 * 
 * @author Graham
 * 
 */
public class BankFive implements Packet {

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		int interfaceID = client.getInStream().readUnsignedWordA();
		int removeID = client.getInStream().readSignedWordBigEndian();
		int removeSlot = client.getInStream().readSignedWordBigEndian();
		int temp = client.getActionAssistant().getItemSlot(removeID);
		Hashtable craftMappings = new Hashtable(21);
		craftMappings.put(1763,1635);craftMappings.put(1765,1637);craftMappings.put(1767,1639);
		craftMappings.put(1769,1641);craftMappings.put(1771,1643);craftMappings.put(1773,1645);
		craftMappings.put(6447,6575);craftMappings.put(1782,1654);craftMappings.put(1784,1656);
		craftMappings.put(1786,1658);craftMappings.put(1788,1660);craftMappings.put(1790,1662);
		craftMappings.put(1536,1664);craftMappings.put(6449,6577);craftMappings.put(1545,1673);
		craftMappings.put(1547,1675);craftMappings.put(1549,1677);craftMappings.put(1551,1679);
		craftMappings.put(1553,1681);craftMappings.put(1555,1683);craftMappings.put(6451,6579);
		
		client.println_debug(interfaceID + " Bank 5 items: " + removeID
				+ " slot: " + removeSlot);
				if(interfaceID == 4233){
					for(int k = 0; k < 7; k++){
						if(client.getActionAssistant().rings[k] == removeID){
							client.getActionAssistant().goldCraft("ring", 5, k);
						}
					}
				}
				if(interfaceID == 4239){
					for(int k = 0; k < 7; k++){
						if(client.getActionAssistant().necks[k] == removeID){
							client.getActionAssistant().goldCraft("neck", 5, k);
						}
					}
				}
				if(interfaceID == 4245){
					for(int k = 0; k < 7; k++){
						if(client.getActionAssistant().ammys[k] == removeID){
							client.getActionAssistant().goldCraft("ammy", 5, k);
						}
					}
				}
					if(interfaceID == 15445){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(5, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15452){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(5, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15459){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(5, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15466){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(5, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15473){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(5, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15481){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(5, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if (interfaceID==57220) { //begin checking for Make 5 smithing
						client.getActionAssistant().multiSmith(removeSlot,1119,5);
					}
					else if (interfaceID==57476) {
						client.getActionAssistant().multiSmith(removeSlot,1120,5);
					}
					else if (interfaceID==57732) {
						client.getActionAssistant().multiSmith(removeSlot,1121,5);
					}
					else if (interfaceID==57988) {
						client.getActionAssistant().multiSmith(removeSlot,1122,5);
					}
					else if (interfaceID==58244) {
						client.getActionAssistant().multiSmith(removeSlot,1123,5);
					}
					if(interfaceID == 2448){ //begin checking for Make 5 gold jewlery
						removeID=(Integer)craftMappings.get(removeID);
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().rings[k] == removeID){
								client.getActionAssistant().removeAllWindows();
								client.getActionAssistant().multiGoldCraft("ring", 5, k);
							}
						}
					}
					if(interfaceID == 3984){
						removeID=(Integer)craftMappings.get(removeID);
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().necks[k] == removeID){
								client.getActionAssistant().removeAllWindows();
								client.getActionAssistant().multiGoldCraft("neck", 5, k);
							}
						}
					}
					if(interfaceID == 5520){
						removeID=(Integer)craftMappings.get(removeID);
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().ammys[k] == removeID){
								client.getActionAssistant().removeAllWindows();
								client.getActionAssistant().multiGoldCraft("ammy", 5, k);
							}
						}
					}
                if (interfaceID >= 1119 && interfaceID <= 1123) {
                    client.getActionAssistant().smithamount = 4;
                    client.getActionAssistant().smith(removeSlot, interfaceID);
                    /*final int WHATEVER = removeSlot;
                    final int WHATTTT = interfaceID;
						EventManager.getSingleton().addEvent(client, new Event() {
							@Override
							public void execute(EventContainer c) {	
								if (client.getActionAssistant().smithamount == 0 || !client.getActionAssistant().smith(WHATEVER, WHATTTT))
									c.stop();
								client.getActionAssistant().smithamount--;
							}
							@Override
							public void stop() {
								
							}

						}, 2500);	*/					
                }
		if (interfaceID == 18579) {
		
			client.getContainerAssistant().bankItem(removeID, removeSlot, 5);
		} else if (interfaceID == 34453) {
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
			client.getContainerAssistant().fromBank(removeID, removeSlot, 5);
		} else if (interfaceID == 28558) {
	
		
			client.getContainerAssistant().sellItem(removeID, removeSlot, 1);
		} else if (interfaceID == 48271) {
			client.getContainerAssistant().buyItem(removeID, removeSlot, 1);
		} else if(interfaceID == 31372) {
			if(client.duelStatus <= 0) { 
			if (temp == -1) {
		client.getActionAssistant().sendMessage("AN ERROR HAS OCCURED 3");
		return;
		}
		
				client.getTradeAssistant().tradeItem(removeID, client.getActionAssistant().getItemSlot(removeID), 5);
			} else {
				if(!client.secondDuelScreen){
					client.getActionAssistant().stakeItem(removeID, client.getActionAssistant().getItemSlot(removeID), 5);
				}
			}
			
		} else if(interfaceID == 55181) {
			if(!client.secondTradeWindow) {
				client.getTradeAssistant().fromTrade(client.getTradeAssistant().getOffer()[removeSlot]-1, removeSlot, 5);
			}
		} else if (interfaceID == 36250) {
			if(!client.secondDuelScreen){
				client.getActionAssistant().fromDuel(removeID, removeSlot, 5);
			}
		}
	}

}
