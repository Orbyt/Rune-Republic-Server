package com.rs.worldserver.model.player;

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
import com.rs.worldserver.model.DialogueAction;
import com.rs.worldserver.model.DialogueMessage;

/**
 * Dialogue assistant class
 * 
 * @author Graham
 * 
 */
public class DialogueAssistant {

	private Client client;

	private DialogueMessage currentDialogue = null;
	private int npcId;

	public DialogueAssistant(Client client) {
		this.client = client;
	}

	public boolean isDialogueOpen() {
		return this.currentDialogue != null;
	}

	public DialogueMessage getCurrentDialogue() {
		return this.currentDialogue;
	}

	public void setCurrentDialogue(DialogueMessage currentDialogue, int npcId) {
		this.currentDialogue = currentDialogue;
		this.npcId = npcId;
	}

	public int getNpcID() {
		return this.npcId;
	}

	public void doAction(DialogueAction action) {
		try {		
			if (action.getType() == DialogueAction.Type.NEXT) {
				Server.getDialogueManager().openDialogue(client,
						action.getNextMessage(), npcId);
			} else if (action.getType() == DialogueAction.Type.OPEN_BANK) {
				client.getActionAssistant().openUpBank();
			} else if (action.getType() == DialogueAction.Type.OPEN_SHOP) {
				client.getActionAssistant().openUpShop(action.getShopId());
			} else if (action.getType() == DialogueAction.Type.TEL2) {
				if(npcId == 2291){//3180 3043
					client.teleportToX = 3350;
					client.teleportToY = 2998;
				}				
				Server.getDialogueManager().cancelDialogue(this.client);
			} else if (action.getType() == DialogueAction.Type.TELE) {
				if(npcId == 2291){//3180 3043
					client.teleportToX = 3180;
					client.teleportToY = 3043;
				}
				if(npcId == 2617){//3180 3043
					client.teleportToX = 2413;
					client.teleportToY = 5117;
				}				
				if(npcId == 2437){//3180 3043
					client.teleportToX = 2550;
					client.teleportToY = 3758;
				}
				if(npcId == 510){//3180 3043
					client.teleportToX = 2834;
					client.teleportToY = 2954;
				}			
				if(npcId == 511){//3180 3043
					client.teleportToX = 2779;
					client.teleportToY = 3213;
				}	
				if(npcId == 381){//3180 3043
					client.teleportToX = 2772;
					client.teleportToY = 3234;
				}				
				Server.getDialogueManager().cancelDialogue(this.client);
			} else if (action.getType() == DialogueAction.Type.EXCHANGE) {
				if(npcId == 747){//3180 3043
					if(client.getActionAssistant().playerHasItemAmount(1540,1) && client.getActionAssistant().playerHasItemAmount(11286,1) && client.getActionAssistant().playerHasItemAmount(995,1200000)){
						client.getActionAssistant().deleteItem(1540, 1);
						client.getActionAssistant().deleteItem(995, 1200000);
						client.getActionAssistant().deleteItem(11286, 1);
						client.getActionAssistant().addItem(11283,1);
						Server.getDialogueManager().cancelDialogue(this.client);
					} else {

					}
					Server.getDialogueManager().openDialogue(client,
						82, npcId);
				}
				if (npcId == 2283) {
					if (!client.getActionAssistant().playerHasItem(757)) {
						client.getActionAssistant().addItem(757, 1);
						Server.getDialogueManager().cancelDialogue(this.client);
					} else {
						Server.getDialogueManager().openDialogue(client, 107, npcId);
					}
					
					
					
					
				}
			} else if (action.getType() == DialogueAction.Type.SLAYER) {
				if(npcId == 8274){
					if(client.slayerAmount <= 0){
						client.getActionAssistant().giveTask();
						client.getActionAssistant().sendFrame200(4888, 591);
						client.getActionAssistant().sendQuest(
								Server.getNpcManager().getNPCDefinition(npcId).getName(),
								4889);
						client.getActionAssistant().sendQuest("Your task is to kill "+client.slayerAmount+" "+Server.getNpcManager().getNPCDefinition(client.slayerTask).getName()+".", 4890);
						client.getActionAssistant().sendQuest(" ", 4891);
						client.getActionAssistant().sendFrame75(npcId, 4888);
						client.getActionAssistant().sendFrame164(4887);
						client.flushOutStream();
					} else {
						client.getActionAssistant().sendFrame200(4888, 591);
						client.getActionAssistant().sendQuest(
								Server.getNpcManager().getNPCDefinition(npcId).getName(),
								4889);
						client.getActionAssistant().sendQuest("Your task is to kill "+client.slayerAmount+" "+Server.getNpcManager().getNPCDefinition(client.slayerTask).getName()+".", 4890);
						client.getActionAssistant().sendQuest("You must finish this task first, before you can get a new one.", 4891);
						client.getActionAssistant().sendFrame75(npcId, 4888);
						client.getActionAssistant().sendFrame164(4887);
						client.flushOutStream();
					}
				}
				
				if(npcId == 1597){

				}	
				if(npcId == 1598){

				}	
				if(npcId == 1599){

				}		
				//Server.getDialogueManager().cancelDialogue(this.client);				
			
			}else if (action.getType() == DialogueAction.Type.BLOODLUSTP) {
					if(false){
						int coinSlot = client.getActionAssistant().getItemSlot(995);
					//String coinName = Server.getItemManager().getItemDefinition(s.getCurrency()).getName();
						if (coinSlot == -1 || client.playerItemsN[coinSlot] < 200000000) {
							client.getActionAssistant().sendFrame200(4888, 591);
							client.getActionAssistant().sendQuest(
									Server.getNpcManager().getNPCDefinition(npcId).getName(),
									4889);
							client.getActionAssistant().sendQuest("You do not have enough money for this team!", 4890);
							client.getActionAssistant().sendQuest("It's 200,000,00 gold pieces.", 4891);
							client.getActionAssistant().sendFrame75(npcId, 4888);
							client.getActionAssistant().sendFrame164(4887);
							client.flushOutStream();
							return;
						}
						Server.getDialogueManager().cancelDialogue(this.client);	
						client.bloodlust = true;
						client.getOutStream().createFrame(193);
						client.flushOutStream();
						return;
					}
					client.getActionAssistant().sendFrame200(4888, 591);
					client.getActionAssistant().sendQuest(
								Server.getNpcManager().getNPCDefinition(npcId).getName(),
								4889);
						client.getActionAssistant().sendQuest("You are already on a bloodlust team!", 4890);
						client.getActionAssistant().sendQuest("Leave your team to start one!", 4891);
						client.getActionAssistant().sendFrame75(npcId, 4888);
						client.getActionAssistant().sendFrame164(4887);
						client.flushOutStream();
			
			}else if (action.getType() == DialogueAction.Type.BLOODLUSTT) {
					if(false){
						Server.getDialogueManager().cancelDialogue(this.client);	
						client.bloodlustp = true;
						client.getOutStream().createFrame(193);
						client.flushOutStream();
						return;
					}
					client.getActionAssistant().sendFrame200(4888, 591);
					client.getActionAssistant().sendQuest(
								Server.getNpcManager().getNPCDefinition(npcId).getName(),
								4889);
						client.getActionAssistant().sendQuest("You are already on a bloodlust team!", 4890);
						client.getActionAssistant().sendQuest("Leave your team to start one!", 4891);
						client.getActionAssistant().sendFrame75(npcId, 4888);
						client.getActionAssistant().sendFrame164(4887);
						client.flushOutStream();
			
			}
			else if (action.getType() == DialogueAction.Type.DICE) {
				if (npcId == 1408) {
					Server.getDialogueManager().cancelDialogue(this.client);				
					client.getOutStream().createFrame(27);
					int jt = client.getInStream().readDWord();
					client.flushOutStream();		
					client.isDicing = true;
				}
				}
			
			else {
				Server.getDialogueManager().cancelDialogue(this.client);
			}
		} catch (Exception e) {
			client.getActionAssistant().removeAllWindows();
			e.printStackTrace();
		}
	}

}
