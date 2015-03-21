package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.content.skill.Fletching;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.*;
import com.rs.worldserver.content.skill.*;
/**
 * Item action
 * 
 * @author Jonas
 * 
 */
public class ItemAction implements Packet {
	public final int ITEM_ON_ITEM = 53, ITEM_ON_PLAYER = 14, ITEM_ON_OBJECT = 192, ITEM_ON_NPC = 57, RUB_ITEM = 75;
	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
	
	switch(packetType) {
		case RUB_ITEM:
			int itemId = c.getInStream().readSignedWordA();
			int item2ID = c.getInStream().readSignedWordBigEndian();
			int item2ID3 = c.getInStream().readSignedWordA();
			int item2ID4 = c.getInStream().readUnsignedWord();
			if (!c.getActionAssistant().playerHasItem(item2ID3,1)) { return; }
			c.println_debug("ItemAction: " + itemId + ","+ item2ID+ ","+ item2ID3+ ","+ item2ID4);
			switch (item2ID3) {
			case 1712:
				c.getActionAssistant().clearQuestInterface();
				c.getActionAssistant().sendFrame126("Edgeville", 2494);
				c.getActionAssistant().sendFrame126("Karamja", 2495);
				c.getActionAssistant().sendFrame126("Draynor Village", 2496);
				c.getActionAssistant().sendFrame126("Al kharid", 2497);
				c.getActionAssistant().sendFrame126("Nowhere", 2498);
				c.getActionAssistant().sendQuestSomething(8143);
				c.getActionAssistant().sendFrame164(2492);
				c.flushOutStream();
			//break;
			case 3853:
				c.getActionAssistant().clearQuestInterface();
				c.getActionAssistant().sendFrame126("Fight Pits", 2494);
				c.getActionAssistant().sendFrame126("Pest Control", 2495);
				c.getActionAssistant().sendFrame126("", 2496);
				c.getActionAssistant().sendFrame126("", 2497);
				c.getActionAssistant().sendFrame126("Nowhere", 2498);
				c.getActionAssistant().sendQuestSomething(8143);
				c.getActionAssistant().sendFrame164(2492);
				c.flushOutStream();			
			break;
			case 3852:
				c.getActionAssistant().clearQuestInterface();
				c.getActionAssistant().sendFrame126("Fight Pits", 2494);
				c.getActionAssistant().sendFrame126("Pest Control", 2495);
				c.getActionAssistant().sendFrame126("", 2496);
				c.getActionAssistant().sendFrame126("", 2497);
				c.getActionAssistant().sendFrame126("Nowhere", 2498);
				c.getActionAssistant().sendQuestSomething(8143);
				c.getActionAssistant().sendFrame164(2492);
				c.flushOutStream();			
			break;
			case 3855:
				c.getActionAssistant().clearQuestInterface();
				c.getActionAssistant().sendFrame126("Fight Pits", 2494);
				c.getActionAssistant().sendFrame126("Pest Control", 2495);
				c.getActionAssistant().sendFrame126("", 2496);
				c.getActionAssistant().sendFrame126("", 2497);
				c.getActionAssistant().sendFrame126("Nowhere", 2498);
				c.getActionAssistant().sendQuestSomething(8143);
				c.getActionAssistant().sendFrame164(2492);
				c.flushOutStream();			
			break;
			}
			
		break;
		case ITEM_ON_ITEM:		
			int usedWithSlot = c.getInStream().readUnsignedWord();
			int itemUsedSlot = c.getInStream().readUnsignedWordA();
			if (usedWithSlot >28 || usedWithSlot < 0 || itemUsedSlot > 28 || itemUsedSlot < 0) {return;}
			int usedWith = c.playerItems[usedWithSlot] - 1;
			int itemUsed = c.playerItems[itemUsedSlot] - 1;
			if (Fletching.isFletchable(c, usedWithSlot, itemUsedSlot)) {
			return; }// Cooking hook
			c.println_debug("ItemAction: " + usedWith + ","+ itemUsed);
			switch (itemUsed) {
			case 590:
			if (usedWith == 1511){
				Firemaking.fire(c, 1, 1511, 100);
			}
			if (usedWith == 1521){
				Firemaking.fire(c, 15, 1521, 150);
			}	
			if (usedWith == 1519){
				Firemaking.fire(c, 30, 1519, 200);
			}
			if (usedWith == 1517){
				Firemaking.fire(c, 45, 1517, 300);
			}
			if (usedWith == 1515){
				Firemaking.fire(c, 60, 1515, 400);
			}	
			if (usedWith == 1513){
				Firemaking.fire(c, 75, 1513, 500);
			}				
			break;
		case 1511:	
			if (usedWith == 590){
				Firemaking.fire(c, 1, 1511, 100);
			}			
		break;
		case 1521:
			if (usedWith == 590){
				Firemaking.fire(c, 15, 1521, 150);
			}
		break;
		case 1519:
			if (usedWith == 590){
				Firemaking.fire(c, 30, 1519, 200);
			}
		break;
		case 1517:
			if (usedWith == 590){
				Firemaking.fire(c, 45, 1517, 300);
			}
		break;
		case 1515:
			if (usedWith == 590){
				Firemaking.fire(c, 60, 1515, 400);
			}
		break;
		case 1513:
			if (usedWith == 590){
				Firemaking.fire(c, 75, 1513, 500);
			}
		break;
		case 1755:
		if (usedWith == 1623){
			c.getActionAssistant().Craft(100, 1623, 1727, 1);
		}			
		if (usedWith == 1621){
			c.getActionAssistant().Craft(150, 1621, 1729, 35);
		}		
		if (usedWith == 1619){
			c.getActionAssistant().Craft(200, 1619, 1725, 60);
		}
		if (usedWith == 1617){
			c.getActionAssistant().Craft(300, 1617, 1731, 70);
		}
		if (usedWith == 1631){
			c.getActionAssistant().Craft(500, 1631, 1712, 80);
		}	
		if (usedWith == 6571){
			c.getActionAssistant().Craft(1000, 6571, 6585, 99);
		}
		break;
		case 1623:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(100, 1623, 1727, 1);
		}	
		break;
		case 1621:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(150, 1621, 1729, 35);
		}	
		break;
		case 1619:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(200, 1619, 1725, 60);
		}	
		break;
		case 1617:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(300, 1617, 1731, 70);
		}	
		break;
		case 1631:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(500, 1631, 1712, 80);
		}	
		break;				
		case 6571:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(1000, 6571, 6585, 99);
		}	
		break;
		case 227:
			if (usedWith == 249){
				c.getActionAssistant().startAnimation(363);
				c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
				c.getActionAssistant().deleteItem(249, c.getActionAssistant().getItemSlot(249), 1);
				c.getActionAssistant().addItem(2428, 1);
				c.getActionAssistant().addSkillXP(100, 15);
				c.getActionAssistant().refreshSkill(15);
			}
			if (usedWith == 251){
				if (c.playerLevel[15] >= 20) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(251, c.getActionAssistant().getItemSlot(251), 1);
					c.getActionAssistant().addItem(2446, 1);
					c.getActionAssistant().addSkillXP(200, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 20 or higher Herblore to make this.");
				}
			}
			if (usedWith == 269){
				if (c.playerLevel[15] >= 99) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(269, c.getActionAssistant().getItemSlot(269), 1);
					c.getActionAssistant().addItem(2450, 1);
					c.getActionAssistant().addSkillXP(1500, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 99 Herblore to make this.");
				}
			}
			if (usedWith == 253){
				if (c.playerLevel[15] >= 30) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(253, c.getActionAssistant().getItemSlot(253), 1);
					c.getActionAssistant().addItem(113, 1);
					c.getActionAssistant().addSkillXP(300, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 30 or higher Herblore to make this.");
				}
			}
			if (usedWith == 257){
				if (c.playerLevel[15] >= 40) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(257, c.getActionAssistant().getItemSlot(257), 1);
					c.getActionAssistant().addItem(2434, 1);
					c.getActionAssistant().addSkillXP(400, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 40 or higher Herblore to make this.");
				}
			}			
			if (usedWith == 259){
				if (c.playerLevel[15] >= 50) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(259, c.getActionAssistant().getItemSlot(259), 1);
					c.getActionAssistant().addItem(2436, 1);
					c.getActionAssistant().addSkillXP(500, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 50 or higher Herblore to make this.");
				}
			}	
			if (usedWith == 263){
				if (c.playerLevel[15] >= 55) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(263, c.getActionAssistant().getItemSlot(263), 1);
					c.getActionAssistant().addItem(2440, 1);
					c.getActionAssistant().addSkillXP(600, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 55 or higher Herblore to make this.");
				}
			}
			if (usedWith == 3000){
				if (c.playerLevel[15] >= 60) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(3000, c.getActionAssistant().getItemSlot(3000), 1);
					c.getActionAssistant().addItem(3024, 1);
					c.getActionAssistant().addSkillXP(700, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 60 or higher Herblore to make this.");
				}
			}
			if (usedWith == 265){
				if (c.playerLevel[15] >= 65) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(265, c.getActionAssistant().getItemSlot(265), 1);
					c.getActionAssistant().addItem(2442, 1);
					c.getActionAssistant().addSkillXP(800, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 65 or higher Herblore to make this.");
				}
			}
			if (usedWith == 267){
				if (c.playerLevel[15] >= 70) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(267, c.getActionAssistant().getItemSlot(267), 1);
					c.getActionAssistant().addItem(2444, 1);
					c.getActionAssistant().addSkillXP(900, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 70 or higher Herblore to make this.");
				}
			}
			if (usedWith == 2481){
				if (c.playerLevel[15] >= 80) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(2481, c.getActionAssistant().getItemSlot(2481), 1);
					c.getActionAssistant().addItem(3040, 1);
					c.getActionAssistant().addSkillXP(1000, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 80 or higher Herblore to make this.");
				}
			}
			break;
		case 269:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 99) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(269, c.getActionAssistant().getItemSlot(269), 1);
					c.getActionAssistant().addItem(2450, 1);
					c.getActionAssistant().addSkillXP(1500, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 99 Herblore to make this.");
				}
			}
			break;
		case 249:
			if (usedWith == 227){
				c.getActionAssistant().startAnimation(363);
				c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
				c.getActionAssistant().deleteItem(249, c.getActionAssistant().getItemSlot(249), 1);
				c.getActionAssistant().addItem(2428, 1);
				c.getActionAssistant().addSkillXP(100, 15);
				c.getActionAssistant().refreshSkill(15);
			}
		break;
		case 251:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 20) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(251, c.getActionAssistant().getItemSlot(251), 1);
					c.getActionAssistant().addItem(2446, 1);
					c.getActionAssistant().addSkillXP(200, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 20 or higher Herblore to make this.");
				}
			}
		break;
		case 253:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 30) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(253, c.getActionAssistant().getItemSlot(253), 1);
					c.getActionAssistant().addItem(113, 1);
					c.getActionAssistant().addSkillXP(300, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 30 or higher Herblore to make this.");
				}
			}
		break;
		case 257:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 40) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(257, c.getActionAssistant().getItemSlot(257), 1);
					c.getActionAssistant().addItem(2434, 1);
					c.getActionAssistant().addSkillXP(400, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 40 or higher Herblore to make this.");
				}
			}			
		break;
		case 259:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 50) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(259, c.getActionAssistant().getItemSlot(259), 1);
					c.getActionAssistant().addItem(2436, 1);
					c.getActionAssistant().addSkillXP(500, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 50 or higher Herblore to make this.");
				}
			}	
		break;
		case 263:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 55) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(263, c.getActionAssistant().getItemSlot(263), 1);
					c.getActionAssistant().addItem(2440, 1);
					c.getActionAssistant().addSkillXP(600, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 55 or higher Herblore to make this.");
				}
			}
		break;
		case 3000:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 60) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(3000, c.getActionAssistant().getItemSlot(3000), 1);
					c.getActionAssistant().addItem(3024, 1);
					c.getActionAssistant().addSkillXP(700, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 60 or higher Herblore to make this.");
				}
			}
		break;
		case 265:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 65) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(265, c.getActionAssistant().getItemSlot(265), 1);
					c.getActionAssistant().addItem(2442, 1);
					c.getActionAssistant().addSkillXP(800, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 65 or higher Herblore to make this.");
				}
			}
		break;
		case 267:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 70) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(267, c.getActionAssistant().getItemSlot(267), 1);
					c.getActionAssistant().addItem(2444, 1);
					c.getActionAssistant().addSkillXP(900, 15);
					c.getActionAssistant().refreshSkill(15);
				} else {
					c.getActionAssistant().Send("You need 70 or higher Herblore to make this.");
				}
			}
		break;
		case 2481:
			if (usedWith == 227){
				if (c.playerLevel[15] >= 80) {
					c.getActionAssistant().startAnimation(363);
					c.getActionAssistant().deleteItem(227, c.getActionAssistant().getItemSlot(227), 1);
					c.getActionAssistant().deleteItem(2481, c.getActionAssistant().getItemSlot(2481), 1);
					c.getActionAssistant().addItem(3040, 1);
					c.getActionAssistant().addSkillXP(1000, 15);
					c.getActionAssistant().refreshSkill(15);
				
				} else {
					c.getActionAssistant().Send("You need 80 or higher Herblore to make this.");
				}
			}		
		break;
		
		
		
		default:
			c.getActionAssistant().Send("Nothing interesting happens.");
		break;			
			
			
			
		}
		
		case ITEM_ON_OBJECT:
		
		c.getInStream().readSignedWordBigEndianA();
		int usedOnObjectID = c.getInStream().readUnsignedWordBigEndian();
		int usedOnY = c.getInStream().readSignedWordBigEndianA();
		int itemSlot = (c.getInStream().readSignedWordBigEndianA() - 128);
		int usedOnX = c.getInStream().readUnsignedWordBigEndianA();
		int itemID = c.getInStream().readUnsignedWord();

		switch(usedOnObjectID) {
		case 13197:
			if (System.currentTimeMillis() - c.potionDelay < 2000)
			return;
			c.potionDelay = System.currentTimeMillis();
			if (itemID == 536 || itemID == 534 || itemID == 4812 || itemID == 4830 || itemID == 4832 || itemID == 4834) {
				c.getActionAssistant().startAnimation(883);
				c.getActionAssistant().addSkillXP(3000, 5);
				c.getActionAssistant().deleteItem(itemID, itemSlot, 1);
				c.getActionAssistant().sendMessage("The Gods are pleased with your offering.");
				c.getActionAssistant().refreshSkill(5);
				c.getActionAssistant().requestUpdates();
				if(c.sounds == 1){
				c.getActionAssistant().frame174(1104,050,000);
				}
			}
			if (itemID == 532) {
				c.getActionAssistant().startAnimation(883);
				c.getActionAssistant().addSkillXP(1000, 5);
				c.getActionAssistant().deleteItem(itemID, itemSlot, 1);
				c.getActionAssistant().sendMessage("The Gods are pleased with your offering.");
				c.getActionAssistant().refreshSkill(5);
				c.getActionAssistant().requestUpdates();
				if(c.sounds == 1){
				c.getActionAssistant().frame174(1104,050,000);
				}
			}
			if (itemID == 526 || itemID == 3133 || itemID == 3132 || itemID == 3131 || itemID == 3130 || itemID == 3129 || itemID == 3128 || itemID == 3827 || itemID == 3826) {
				c.getActionAssistant().startAnimation(883);
				c.getActionAssistant().addSkillXP(700, 5);
				c.getActionAssistant().deleteItem(itemID, itemSlot, 1);
				c.getActionAssistant().sendMessage("The Gods are pleased with your offering.");
				c.getActionAssistant().refreshSkill(5);
				c.getActionAssistant().requestUpdates();
				if(c.sounds == 1){
				c.getActionAssistant().frame174(1104,050,000);
				}
			}
			break;
		default:
			//c.getActionAssistant().Send("Nothing interesting happens ert.");
		break;	
		}
		
		case ITEM_ON_NPC:
		
			int a = c.getInStream().readSignedWordBigEndianA();
			int b = c.getInStream().readSignedWordBigEndianA();
			int c1 = c.getInStream().readSignedWordBigEndian();
			int d = c.getInStream().readSignedWordBigEndianA();
			c.getActionAssistant().Send("Nothing interesting happens.");
			break;	

		case ITEM_ON_PLAYER:
		
		    int getSlotA;
			int getSlotB;
			int getSlotC;

			getSlotA = c.getInStream().readSignedWordBigEndianA();
			getSlotB = c.getInStream().readSignedWordBigEndian();
			getSlotC = c.getInStream().readUnsignedWordA();
			
			int crackerSlot = c.getInStream().readSignedWordBigEndian();
			int usedOn = (int) (Misc.hexToInt(c.getInStream().buffer, 3, 1) / 1000);

			int random = (int)(2 * Math.random()) + 1;
			int crackerId = c.playerItems[crackerSlot];

			crackerId -= 1;
			Client c2 = (Client) PlayerManager.getSingleton().getPlayers()[usedOn];
			if ((crackerId == 962) && c.getActionAssistant().playerHasItem(962,1)) {
				c.getActionAssistant().sendMessage("You pull a Christmas cracker...");
				c2.getActionAssistant().sendMessage("Someone pulls a Christmas Cracker on you.");
				c.getActionAssistant().deleteItem(crackerId, crackerSlot, 1);
			if (random == 1) {
				c.getActionAssistant().addItem(c.getActionAssistant().randomHat(), 1);
				c.getActionAssistant().sendMessage("Yay! I got a Party Hat!");
				c2.getActionAssistant().sendMessage("Aww.. The other person got the party hat..");
			} else {
				c.getActionAssistant().sendMessage("Aww.. The other person got the party hat..");
				c2.getActionAssistant().addItem(c.getActionAssistant().randomHat(), 1);
				c2.getActionAssistant().sendMessage("Yay! I got the party hat!");
				}
			}
		
		}
	}
}
