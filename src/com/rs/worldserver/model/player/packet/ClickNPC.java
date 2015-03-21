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

import com.rs.worldserver.Server;
import com.rs.worldserver.content.skill.Fishing;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCCombat;
import com.rs.worldserver.model.npc.NPCAction;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.*;
import com.rs.worldserver.Config;


/**
 * Click NPC packet.
 * 
 * @author Graham
 * 
 */
public class ClickNPC implements Packet {

	public static final int FIRST_CLICK = 155, SECOND_CLICK = 17,
							THIRD_CLICK = 21;

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
			
		if (packetType == FIRST_CLICK) {
			int slot = client.getInStream().readSignedWordBigEndian();
			NPC npc = Server.getNpcManager().getNPC(slot);
			Misc.println_debug("NPC " + slot + " click.");
			if (npc != null) {
				Misc.println_debug("First click NPC: type: "
						+ npc.getDefinition().getType());
				if (!client.withinInteractionDistance(npc.getAbsX(), npc
						.getAbsY(), npc.getHeightLevel())) {
					client.getActionAssistant().sendMessage(
							"You are too far away to do that.");
					return;
				} else {
					//client.getActionAssistant().turnTo(npc.getAbsX(),npc.getAbsY());
					client.faceUpdate(slot);
					npc.faceTo(client.playerId);
				}
				if(!client.npcClick) return;
				//client.getActionAssistant().sendMessage("NPC " + npc.getDefinition().getType() + " click.");
				switch(npc.getDefinition().getType()) {
				//case 2291:
					//client.getActionAssistant().npc(2291, "Greetings! Are you ready", "to head to bandits training?", "", "", 2291);
					
				//break;
				
				case 0:
				Server.getDegradeManager().repair(client);
				break;
				case 1552: // Santa
					if(client.actionAssistant.playerHasItem(19864,1)){
						client.getActionAssistant().deleteItem(19864, client.getActionAssistant().getItemSlot(19864), 1);
						client.getActionAssistant().addItem(client.getClueAssistant().randomReward7(), 1);
					}
				break;
				case 804:
					client.getActionAssistant().tanningInterface();
				break;
				case 8725:
				int ticketValue = 0;
				int totalTickets = 0;
					for (int z = 0; z < client.playerItems.length; z++) {
					ticketValue = client.getActionAssistant().getStatueValue(client.playerItems[z]-1);
						if (ticketValue != 0) {  
							totalTickets += ticketValue;
							client.getActionAssistant().deleteItem(client.playerItems[z]-1,client.getActionAssistant().getItemSlot(client.playerItems[z]-1), 1);
						}
					}
					if (totalTickets > 0) {
					client.getActionAssistant().addItem(621,totalTickets);
					}
					client.getActionAssistant().sendMessage("I have traded your statues into tickets");
					
				break;
				case 248:
				if(client.getActionAssistant().freeSlots() < 10)	{			
					client.getActionAssistant().sendMessage("You must have atleast 10 free inventory spaces");
					break;
				}
	int potType = 0;
	int doses = 0;
	int arrayid = 500;
	int vials = 0;

	for (int z = 0; z < client.playerItems.length; z++) {

		for (int i = 0; i < client.getActionAssistant().Potions.length; i++) {
			for ( int n = 0; n < 4; n++) {
			
				if (n == 0) {continue;}

				if (client.playerItems[z]-1 == client.getActionAssistant().Potions[i][n]) {  
					if (potType == 0) {
						potType = client.getActionAssistant().Potions[i][0];
						arrayid = i;
					}
					else {
						if (client.getActionAssistant().Potions[i][0] != potType) { continue; }
					}
					if (n == 1) {
						doses += 3*client.playerItemsN[z];
					}
					else if (n == 2) {
						doses += 2*client.playerItemsN[z];
					}
					else if (n == 3) {
						doses += client.playerItemsN[z];
					}
					vials += client.playerItemsN[z];
					client.getActionAssistant().deleteItem(client.getActionAssistant().Potions[i][n],client.getActionAssistant().getItemSlot(client.getActionAssistant().Potions[i][n]), 2000000000);
				}

			}
		}
	}
	if (potType != 0 && arrayid != 500) {
		int remainder = doses % 4;
		int addDose = (doses - remainder)/4;
		vials = vials -addDose - remainder;
		if (addDose != 0) {
			client.getActionAssistant().addItem(potType,addDose);
		}
			
			if (remainder == 1) {
				client.getActionAssistant().addItem( client.getActionAssistant().Potions[arrayid][3],1);
			}
			else if (remainder == 2) {
				client.getActionAssistant().addItem( client.getActionAssistant().Potions[arrayid][2],1);
			}
			else if (remainder == 3) {
				client.getActionAssistant().addItem( client.getActionAssistant().Potions[arrayid][1],1);
			}
			if (vials > 0) {
		client.getActionAssistant().addItem(230,vials);
		}
	}
	client.getActionAssistant().sendMessage("I have combined all of your potions");
				break;
				case 2258:
					npc.faceTo(client.playerId);
					npc.startAnimation(1818,1818);
					npc.gfx100(343);
					client.getActionAssistant().startTeleport(3050, 4840, 0, "modern");
						client.isSkulled = true;
						client.skullTimer = Config.SKULL_TIMER;
						if(Config.SKULL_HEAD_ICON) {
							client.headIcon = 0;
						}
						client.getActionAssistant().requestUpdates();					
				break;
				case 198:
					client.getActionAssistant().npc(588, "Greetings! And welcome to the Warrior's", "Guild, to start off, head downstairs and", "use the animator. It will require sets", "of armour to work.", 198);
				break;	
				case 3801:
					client.getActionAssistant().showInterface(7200);
					break;
	
				}
				
				if (Fishing.FishingFirstClick.clickObject(client, npc
						.getDefinition().getType(), npc.getAbsX(), npc
						.getAbsY())) {
				} else {

					NPCAction action = Server.getNpcManager()
							.getFirstClickNPCAction(
									npc.getDefinition().getType());
					if (action != null) {
						parseAction(client, npc, action);
					}
				}
			} else {
				Misc.println_debug("NPC " + slot + " non-existant.");
			}
		} else if (packetType == SECOND_CLICK) {
			int slot = client.getInStream().readUnsignedWordBigEndianA();
			NPC npc = Server.getNpcManager().getNPC(slot);
			Misc.println_debug("NPC " + slot + " click.");
			if (npc != null) {
				Misc.println_debug("Second click NPC: type: "
						+ npc.getDefinition().getType());
				if (!client.withinInteractionDistance(npc.getAbsX(), npc
						.getAbsY(), npc.getHeightLevel())) {
					client.getActionAssistant().sendMessage(
							"You are too far away to do that.");
					return;
				} else {
					//client.getActionAssistant().turnTo(npc.getAbsX(),npc.getAbsY());
					client.faceUpdate(slot);
					npc.faceTo(client.playerId);
				}
			switch(npc.getDefinition().getType()) {
			case 9:
			case 10:
			case 32:
				Thieving.pickPocket(client, "Guard", "and recieve some ingredients.", 40, 46, client.getActionAssistant().random2nds(), 1,npc.getDefinition().getType());
			break;
			case 1:
			case 2:
				Thieving.pickPocket(client, "Man", "and recieve some ingredients.", 1, 10, client.getActionAssistant().random2nds(), 1,npc.getDefinition().getType());
				break;
			case 23:
				Thieving.pickPocket(client, "Knight", "and recieve some ingredients.", 55, 85, client.getActionAssistant().random2nds(), 1,npc.getDefinition().getType());
				break;
			case 21:
				Thieving.pickPocket(client, "Hero", "and recieve some ingredients.", 80, 275, client.getActionAssistant().random2nds(), 1,npc.getDefinition().getType());
				break;
			case 66:
				Thieving.pickPocket(client, "Gnome", "and recieve some ingredients.", 75, 200, client.getActionAssistant().random2nds(), 1,npc.getDefinition().getType());
			break;

	
			}
				if (Fishing.FishingSecondClick.clickObject(client, npc
						.getDefinition().getType(), npc.getAbsX(), npc
						.getAbsY())) {
				} else {
					NPCAction action = Server.getNpcManager()
							.getSecondClickNPCAction(
									npc.getDefinition().getType());
					if (action != null) {
						parseAction(client, npc, action);
					}
				}
				
			} else {
				Misc.println_debug("NPC " + slot + " non-existant.");
			}
		} else if (packetType == THIRD_CLICK) {
			int slot = client.getInStream().readSignedWord();
			NPC npc = Server.getNpcManager().getNPC(slot);
			Misc.println_debug("NPC " + slot + " click.");
			if (npc != null) {
				Misc.println_debug("Third click NPC: type: "
						+ npc.getDefinition().getType());
				if (!client.withinInteractionDistance(npc.getAbsX(), npc
						.getAbsY(), npc.getHeightLevel())) {
					client.getActionAssistant().sendMessage(
							"You are too far away to do that.");
					return;
				} else {
					//client.getActionAssistant().turnTo(npc.getAbsX(),npc.getAbsY());
					client.faceUpdate(slot);
					npc.faceTo(client.playerId);
				}
				switch(npc.getDefinition().getType()) {
				case 553:
					npc.startAnimation(1818,1818);
					client.gfx100(343);
					client.getActionAssistant().startTeleport(2911,4832, 0, "modern");	
				break;
				case 2259:
					npc.startAnimation(1818,1818);
					client.gfx100(343);
					client.getActionAssistant().startTeleport(3018, 3958, 1, "modern");
				break;
				default:
					client.getActionAssistant().Send("Nothing interesting happens.");
				break;	
				}
				NPCAction action = Server.getNpcManager()
						.getThirdClickNPCAction(npc.getDefinition().getType());
				if (action != null) {
					parseAction(client, npc, action);
				}
			} else {
				Misc.println_debug("NPC " + slot + " non-existant.");
			}
		}
	}

	public void parseAction(Client client, NPC npc, NPCAction action) {
		try {
			client.getCombat().curepoison();
			if (action.getType() == NPCAction.Type.TALK) {
				Server.getDialogueManager().openDialogue(client,
						action.getTalkDialogueId(),
						npc.getDefinition().getType());
			} else if (action.getType() == NPCAction.Type.OPEN_BANK) {
				client.getActionAssistant().openUpBank();
			} else if (action.getType() == NPCAction.Type.OPEN_SHOP) {
				client.getActionAssistant().openUpShop(action.getShopId());
			}
		} catch (Exception e) {

		}
	}

}
