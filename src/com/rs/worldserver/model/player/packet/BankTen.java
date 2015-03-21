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

import com.rs.worldserver.util.BanProcessor;
import java.io.*;
/**
 * Bank ten items
 * 
 * @author Graham
 * 
 */
public class BankTen implements Packet {

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		int interfaceID = client.getInStream().readUnsignedWordA();
		int removeID = client.getInStream().readUnsignedWordA();
		int removeSlot = client.getInStream().readUnsignedWordA();
		int temp = client.getActionAssistant().getItemSlot(removeID);

		client.println_debug(interfaceID + "Bank 10 items: " + removeID
				+ " ja slot: " + client.getActionAssistant().getItemSlot(removeID));
				if(interfaceID == 39046 && removeID == 1712){
					client.necklace = 1;
					client.getActionAssistant().clearQuestInterface();
					client.getActionAssistant().sendFrame126("Relleka", 2494);
					client.getActionAssistant().sendFrame126("Karamja", 2495);
					client.getActionAssistant().sendFrame126("Draynor Village", 2496);
					client.getActionAssistant().sendFrame126("Al kharid", 2497);
					client.getActionAssistant().sendFrame126("Nowhere", 2498);
					client.getActionAssistant().sendQuestSomething(8143);
					client.getActionAssistant().sendFrame164(2492);
					client.flushOutStream();					
				}
				if(interfaceID == 4233){
					for(int k = 0; k < 7; k++){
						if(client.getActionAssistant().rings[k] == removeID){
							client.getActionAssistant().goldCraft("ring", 10, k);
						}
					}
				}
				if(interfaceID == 4239){
					for(int k = 0; k < 7; k++){
						if(client.getActionAssistant().necks[k] == removeID){
							client.getActionAssistant().goldCraft("neck", 10, k);
						}
					}
				}
				if(interfaceID == 4245){
					for(int k = 0; k < 7; k++){
						if(client.getActionAssistant().ammys[k] == removeID){
							client.getActionAssistant().goldCraft("ammy", 10, k);
						}
					}
				}
					if(interfaceID == 15445){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(10, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15452){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(10, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15459){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(10, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15466){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(10, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15473){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(10, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if(interfaceID == 15481){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().sItem[k] == removeID){
								client.getActionAssistant().smeltSilver(10, k);
								client.getActionAssistant().removeAllWindows();
							}
						}
					}
					if (interfaceID==24452) { //begin checking for Make 10 smithing
						client.getActionAssistant().multiSmith(removeSlot,1119,10);
					}
					else if (interfaceID==24708) {
						client.getActionAssistant().multiSmith(removeSlot,1120,10);
					}
					else if (interfaceID==24964) {
						client.getActionAssistant().multiSmith(removeSlot,1121,10);
					}
					else if (interfaceID==25220) {
						client.getActionAssistant().multiSmith(removeSlot,1122,10);
					}
					else if (interfaceID==25476) {
						client.getActionAssistant().multiSmith(removeSlot,1123,10);
					}
					if(interfaceID == 35216){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().rings[k] == removeID){
								client.getActionAssistant().removeAllWindows();
								client.getActionAssistant().multiGoldCraft("ring", 10, k);
							}
						}
					}
					if(interfaceID == 36752){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().necks[k] == removeID){
								client.getActionAssistant().removeAllWindows();
								client.getActionAssistant().multiGoldCraft("neck", 10, k);
							}
						}
					}
					if(interfaceID == 38288){
						for(int k = 0; k < 7; k++){
							if(client.getActionAssistant().ammys[k] == removeID){
								client.getActionAssistant().removeAllWindows();
								client.getActionAssistant().multiGoldCraft("ammy", 10, k);
							}
						}
					}
		if (interfaceID >= 1119 && interfaceID <= 1123) {
            client.getActionAssistant().smithamount = 4;
            client.getActionAssistant().smith(client.getActionAssistant().getItemSlot(removeID), interfaceID);	
		}
		if (interfaceID == 51347) {
		if (temp == -1) {
		client.getActionAssistant().sendMessage("AN ERROR HAS OCCURED 8");
		return;
		}
			client.getContainerAssistant().bankItem(removeID, client.getActionAssistant().getItemSlot(removeID), 10);
		} else if (interfaceID == 1685) {
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
			client.getContainerAssistant().fromBank(removeID, removeSlot, 10);
		} else if (interfaceID == 15503) {
			client.getContainerAssistant().buyItem(removeID, removeSlot, 5);
		} else if (interfaceID == 61326) {
			client.getContainerAssistant().sellItem(removeID, removeSlot, 5);
		} else if(interfaceID == 64140) {
			if(client.duelStatus <= 0) {
			if (temp == -1) {
		client.getActionAssistant().sendMessage("AN ERROR HAS OCCURED 10");
		return;
		}
				client.getTradeAssistant().tradeItem(removeID, client.getActionAssistant().getItemSlot(removeID), 10);
			} else {
				if(!client.secondDuelScreen){
				if (temp == -1) {
		client.getActionAssistant().sendMessage("AN ERROR HAS OCCURED 11");
		return;
		}
				client.getActionAssistant().stakeItem(removeID, client.getActionAssistant().getItemSlot(removeID), 10);
				}
			}
		} else if(interfaceID == 22413) {
			if(!client.secondTradeWindow) {
					//client.getTradeAssistant().fromTrade(removeID, removeSlot, 10);	
					client.getTradeAssistant().fromTrade(client.getTradeAssistant().getOffer()[removeSlot]-1, removeSlot, 10);	

			}
		} else if (interfaceID == 3482) {
			if(!client.secondDuelScreen){
				client.getActionAssistant().fromDuel(removeID, removeSlot, 10);
			}
		}
	}

}
