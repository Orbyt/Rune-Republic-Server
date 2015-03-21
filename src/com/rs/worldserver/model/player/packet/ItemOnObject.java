package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.content.skill.Fletching;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.*;
import com.rs.worldserver.content.skill.*;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
/**
 * Item action
 * 
 * @author Jonas
 * 
 */
public class ItemOnObject implements Packet {

	@Override
	public void handlePacket(final Client client, int packetType, int packetSize) {
		
		client.getInStream().readSignedWordBigEndianA();
		int usedOnObjectID = client.getInStream().readUnsignedWordBigEndian();
		int usedOnY = client.getInStream().readSignedWordBigEndianA();
		int itemSlot = (client.getInStream().readSignedWordBigEndianA() - 128);
		int usedOnX = client.getInStream().readUnsignedWordBigEndianA();
		int itemID = client.getInStream().readUnsignedWord();
		client.println_debug("ItemAction: " + usedOnObjectID + ","+ usedOnX+ ","+usedOnY+ ","+itemID+ ","+itemSlot);
		if (client.withinInteractionDistance(usedOnX, usedOnY, client.getHeightLevel())){
			if (Cooking.isCookable(client, itemSlot, itemID, usedOnObjectID)) {
				return; // Cooking hook
			}	
		}
		if (usedOnObjectID==2782) usedOnObjectID=2783; // to handle Doric's anvils
		switch(usedOnObjectID) {
			case 3827:
				if (itemID == 954) {
					client.deletethatwall(usedOnX, usedOnY);
					client.makeGlobalObject(usedOnX, usedOnY, 3828, -2, 10);
				} else {
					client.getActionAssistant().sendMessage("You need a rope to go down here.");
				}
			break;
			case 3830:
				if (itemID == 954) {
					client.deletethatwall(usedOnX, usedOnY);
					client.makeGlobalObject(usedOnX, usedOnY, 3831, -2, 10);
				} else {
					client.getActionAssistant().sendMessage("You need a rope to go down here.");
				}
			break;
			case 4448:
				if(usedOnX == 2403 && usedOnY == 9496 ){
					if(itemID == 4045) {
						if(Server.getCastleWars().saraRocks2 == 1){
							Server.getCastleWars().updateRocks(2401,9494,3);
						}
					}
				} else if(usedOnX == 2408 && usedOnY == 9505 ){
					if(itemID == 4045) {
						if(Server.getCastleWars().saraRocks1 == 1){
							Server.getCastleWars().updateRocks(2409,9503,3);
						}
					}
				}
				else if(usedOnX == 2393 && usedOnY == 9500 ){
					if(itemID == 4045) {
						if(Server.getCastleWars().zamRocks2 == 1){
							Server.getCastleWars().updateRocks(2391,9501,3);
						}
					}
				}
				else if(usedOnX == 2399 && usedOnY == 9514 ){
					if(itemID == 4045) {
						if(Server.getCastleWars().zamRocks1 == 1){
							Server.getCastleWars().updateRocks(2400,9512,3);
						}
					}
				}
				break;
			case 11661:
			case 879:
			
				if (itemID == 229) {
					client.getActionAssistant().startAnimation(2552);
					client.setBusy(true);
					client.walked=false;
					EventManager.getSingleton().addEvent(client,"item on object", new Event() {
						public void stop() {
							client.getActionAssistant().startAnimation(2552);
							client.setBusy(false);
						}

						public void execute(EventContainer c) {
						int amountLeft = client.getActionAssistant().getItemAmount(229);
							if (amountLeft == 0 || client.walked) {
								c.stop();
								return;
							}
						if (client.getActionAssistant().getItemAmount(229) > 0) {
							client.getActionAssistant().startAnimation(883);	
							client.getActionAssistant().deleteItem(229, 1);
							client.getActionAssistant().addItem(227, 1);
							amountLeft--;
						} else {

							c.stop();
						}
						}
					}, 2500);
				}
				break;
// Warrior Guild Start
			//case 9094: //animator start
					/*if (itemID == 1117 || itemID == 1075 || itemID == 1155 && client.getActionAssistant().playerHasItem(1117) && client.getActionAssistant().playerHasItem(1075) && client.getActionAssistant().playerHasItem(1155)) {
						client.getActionAssistant().deleteItem(1155, 1);
						client.getActionAssistant().deleteItem(1075, 1);
						client.getActionAssistant().deleteItem(1117, 1);
						Server.getNpcManager().newNpc(3803, client.absX, client.absY-1, 0, 20,120,120,1);
						client.getActionAssistant().sendMessage("You place the armour on the animator...");
						client.getActionAssistant().sendMessage("...summoning a armoured figure.");
					}
					if (itemID == 1153 || itemID == 1067 || itemID == 1115 && client.getActionAssistant().playerHasItem(1153) && client.getActionAssistant().playerHasItem(1067) && client.getActionAssistant().playerHasItem(1115)) {
						client.getActionAssistant().deleteItem(1153, 1);
						client.getActionAssistant().deleteItem(1067, 1);
						client.getActionAssistant().deleteItem(1115, 1);
						Server.getNpcManager().newNpc(3804, client.absX, client.absY-1, 0, 20,120,120,1);
						client.getActionAssistant().sendMessage("You place the armour on the animator...");
						client.getActionAssistant().sendMessage("...summoning a armoured figure.");
					}
					if (itemID == 1157 || itemID == 1119 || itemID == 1125 && client.getActionAssistant().playerHasItem(1157) && client.getActionAssistant().playerHasItem(1119) && client.getActionAssistant().playerHasItem(1069)) {
						client.getActionAssistant().deleteItem(1157, 1);
						client.getActionAssistant().deleteItem(1119, 1);
						client.getActionAssistant().deleteItem(1069, 1);
						Server.getNpcManager().newNpc(3805, client.absX, client.absY-1, 0, 20,120,120,1);
						client.getActionAssistant().sendMessage("You place the armour on the animator...");
						client.getActionAssistant().sendMessage("...summoning a armoured figure.");
					}
					if (itemID == 1165 || itemID == 1077 || itemID == 1125 && client.getActionAssistant().playerHasItem(1165) && client.getActionAssistant().playerHasItem(1077) && client.getActionAssistant().playerHasItem(1125)) {
						client.getActionAssistant().deleteItem(1165, 1);
						client.getActionAssistant().deleteItem(1077, 1);
						client.getActionAssistant().deleteItem(1125, 1);
						Server.getNpcManager().newNpc(3806, client.absX, client.absY-1, 0, 20,120,120,1);
						client.getActionAssistant().sendMessage("You place the armour on the animator...");
						client.getActionAssistant().sendMessage("...summoning a armoured figure.");
					}
					if (itemID == 1121 || itemID == 1071 || itemID == 1165 && client.getActionAssistant().playerHasItem(1121) && client.getActionAssistant().playerHasItem(1071) && client.getActionAssistant().playerHasItem(1159)) {
						client.getActionAssistant().deleteItem(1121, 1);
						client.getActionAssistant().deleteItem(1071, 1);
						client.getActionAssistant().deleteItem(1159, 1);
						Server.getNpcManager().newNpc(3807, client.absX, client.absY-1, 0, 20,120,120,1);
						client.getActionAssistant().sendMessage("You place the armour on the animator...");
						client.getActionAssistant().sendMessage("...summoning a armoured figure.");
					}
					if (itemID == 1073 || itemID == 1123 || itemID == 1161 && client.getActionAssistant().playerHasItem(1161) && client.getActionAssistant().playerHasItem(1073) && client.getActionAssistant().playerHasItem(1123)) {
						client.getActionAssistant().deleteItem(1073, 1);
						client.getActionAssistant().deleteItem(1123, 1);
						client.getActionAssistant().deleteItem(1161, 1);
						Server.getNpcManager().newNpc(3808, client.absX, client.absY-1, 0, 20,120,120,1);
						client.getActionAssistant().sendMessage("You place the armour on the animator...");
						client.getActionAssistant().sendMessage("...summoning a armoured figure.");
					}*/
					/*if (itemID == 1163 || itemID == 1093 || itemID == 1079 || itemID == 1127) {
						if(client.getActionAssistant().playerHasItem(1079,1) && client.getActionAssistant().playerHasItem(1163,1) && client.getActionAssistant().playerHasItem(1127,1)){
							client.getActionAssistant().deleteItem(1163, 1);
							client.getActionAssistant().deleteItem(1079, 1);
							client.getActionAssistant().deleteItem(1127, 1);
							Server.getNpcManager().newNpc(3809, client.absX, client.absY-1,0, 20,120,120,0);
							client.getActionAssistant().sendMessage("You place the armour on the animator...");
							client.getActionAssistant().sendMessage("...summoning a armoured figure.");
						}
						if(client.getActionAssistant().playerHasItem(1093,1) && client.getActionAssistant().playerHasItem(1163,1) && client.getActionAssistant().playerHasItem(1127,1)){
							client.getActionAssistant().deleteItem(1163, 1);
							client.getActionAssistant().deleteItem(1093, 1);
							client.getActionAssistant().deleteItem(1127, 1);
							Server.getNpcManager().newNpc(3809, client.absX, client.absY-1,0, 20,120,120,0);
							client.getActionAssistant().sendMessage("You place the armour on the animator...");
							client.getActionAssistant().sendMessage("...summoning a armoured figure.");
						}						
					}

					//}
				break;*/
//Warrior Guild End			
			case 11666:
			case 2781:
				if (itemID == 2357) {
					client.getActionAssistant().openGoldInterface();
				}
			break;
		case 2783:
			if (!client.getActionAssistant().playerHasItem(2347, 1)){
				client.getActionAssistant().Send("You need a hammer to smith the bars.");
				break;
			}
			//int Type = CheckSmithing(ItemID, ItemSlot);
			//if (Type != -1) {
			//	client.getActionAssistant().OpenSmithingFrame(Type);
			//}/*			
				if (itemID == 2349) {//Anvil+bronze bar
					client.getActionAssistant().smithing = 0;
					client.getActionAssistant().smithinginterface();
				}
				if (itemID == 2351) {//Anvil+iron bar
					if (client.playerLevel[13] < 15)
						client.getActionAssistant().sendMessage("You need a smithing level of 15 to make Iron equipment.");
					else {
						client.getActionAssistant().smithinginterface();
						client.getActionAssistant().smithing = 1;
					}
				}
				if (itemID == 2353) {//Anvil+steel bar
					if (client.playerLevel[13] < 30)
						client.getActionAssistant().sendMessage("You need a smithing level of 30 to make Steel equipment.");
					else {
						client.getActionAssistant().smithing = 2;
						client.getActionAssistant().smithinginterface();
					}
				}
				if (itemID == 2359) {//Anvil+mithril bar
					if (client.playerLevel[13] < 50)
						client.getActionAssistant().sendMessage("You need a smithing level of 50 to make Mithril equipment.");
					else {
						client.getActionAssistant().smithing = 3;
						client.getActionAssistant().smithinginterface();
					}
				}
				if (itemID == 2361) {//Anvil+adamant bar
					if (client.playerLevel[13] < 70)
						client.getActionAssistant().sendMessage("You need a smithing level of 70 to make Adamant equipment.");
					else {
						client.getActionAssistant().smithing = 4;
						client.getActionAssistant().smithinginterface();
					}
				}
				if (itemID == 2363) {//Anvil+runite bar
					if (client.playerLevel[13] < 85)
						client.getActionAssistant().sendMessage("You need a smithing level of 85 to make Runite equipment.");
					else {
						client.getActionAssistant().smithing = 5;
						client.getActionAssistant().smithinginterface();
					}
				}						
			break;
			case 409:
				if (System.currentTimeMillis() - client.potionDelay < 2000)
				return;
				client.potionDelay = System.currentTimeMillis();
				if (itemID == 536 || itemID == 534 || itemID == 4812 || itemID == 4830 || itemID == 4832 || itemID == 4834) {
					client.getActionAssistant().deleteItem(itemID, itemSlot, 1);
					client.getActionAssistant().startAnimation(883);
					client.gfx(624);
					client.getActionAssistant().addSkillXP(800, 5);
					client.getActionAssistant().sendMessage("The Gods are pleased with your offering.");
					client.getActionAssistant().refreshSkill(5);
					client.getActionAssistant().requestUpdates();
					if(client.sounds == 1){
						client.getActionAssistant().frame174(1104,050,000);
					}
				}
				if (itemID == 532) {
					client.getActionAssistant().deleteItem(itemID, itemSlot, 1);
					client.getActionAssistant().startAnimation(883);
					client.gfx(624);
					client.getActionAssistant().addSkillXP(150, 5);
					client.getActionAssistant().sendMessage("The Gods are pleased with your offering.");
					client.getActionAssistant().refreshSkill(5);
					client.getActionAssistant().requestUpdates();
					if(client.sounds == 1){
						client.getActionAssistant().frame174(1104,050,000);
					}
				}
				if (itemID == 526 || itemID == 3133 || itemID == 3132 || itemID == 3131 || itemID == 3130 || itemID == 3129 || itemID == 3128 || itemID == 3827 || itemID == 3826) {
					client.getActionAssistant().deleteItem(itemID, itemSlot, 1);
					client.getActionAssistant().startAnimation(883);
					client.gfx(624);
					client.getActionAssistant().addSkillXP(100, 5);
					client.getActionAssistant().sendMessage("The Gods are pleased with your offering.");
					client.getActionAssistant().refreshSkill(5);
					client.getActionAssistant().requestUpdates();
					if(client.sounds == 1){
						client.getActionAssistant().frame174(1104,050,000);
					}
				}
				break;
			default:
				client.getActionAssistant().Send("Nothing interesting happens.");
			break;
		}
	}
}
