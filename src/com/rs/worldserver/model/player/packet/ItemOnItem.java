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
public class ItemOnItem implements Packet {

	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
		int usedWithSlot = c.getInStream().readUnsignedWord();
		int itemUsedSlot = c.getInStream().readUnsignedWordA();
		int usedWith = c.playerItems[usedWithSlot] - 1;
		int itemUsed = c.playerItems[itemUsedSlot] - 1;
		c.println_debug("ItemAction: " + usedWith + ","+ itemUsed);
		//c.getActionAssistant().sendMessage("1: "+usedWith+ " 2: " + itemUsed);
		if (Fletching.isFletchable(c, usedWithSlot, itemUsedSlot)) {
			return; // Cooking hook
		}
		if (Herblore.isMakable(c, itemUsed, usedWith)) {
			return; 
		}
		if (itemUsed == 145 && usedWith == 261 || itemUsed == 261 && usedWith == 145) {
			if (c.playerLevel[15] >= 90) {
		if (c.getActionAssistant().playerHasItem(995,5000000) && c.getActionAssistant().playerHasItem(261, 1) && c.getActionAssistant().playerHasItem(145,1)) {
		c.getActionAssistant().startAnimation(363);
				c.getActionAssistant().deleteItem(145, c.getActionAssistant().getItemSlot(145), 1);
				c.getActionAssistant().deleteItem(995, c.getActionAssistant().getItemSlot(995), 5000000);
				c.getActionAssistant().deleteItem(261, c.getActionAssistant().getItemSlot(261), 1);
				c.getActionAssistant().addItem(15309,1);
				c.getActionAssistant().addSkillXP(200,SkillConstants.HERBLORE);
				c.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
				}
				else {
				c.getActionAssistant().Send("You need 5m coins to make this");
				}
			}
			else {
				c.getActionAssistant().Send("You need a herblore level of 90 to make this");
				}
		}
		if (itemUsed == 157 && usedWith == 267 || itemUsed == 267 && usedWith == 157) {
			if (c.playerLevel[15] >= 90) {
				if (c.getActionAssistant().playerHasItem(995,5000000) && c.getActionAssistant().playerHasItem(157, 1) && c.getActionAssistant().playerHasItem(267,1)) {
				c.getActionAssistant().startAnimation(363);
				c.getActionAssistant().deleteItem(157, c.getActionAssistant().getItemSlot(157), 1);
				c.getActionAssistant().deleteItem(995, c.getActionAssistant().getItemSlot(995), 5000000);
				c.getActionAssistant().deleteItem(267, c.getActionAssistant().getItemSlot(267), 1);
				c.getActionAssistant().addItem(15313,1);
				c.getActionAssistant().addSkillXP(200,SkillConstants.HERBLORE);
				c.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
				}
				else {
				c.getActionAssistant().Send("You need 5m coins to make this");
				}
			}
			else {
				c.getActionAssistant().Send("You need a herblore level of 90 to make this");
				}
		}
		if (itemUsed == 2481 && usedWith == 163 || itemUsed == 163 && usedWith == 2481) {
			if (c.playerLevel[15] >= 90) {
				if (c.getActionAssistant().playerHasItem(995,5000000) && c.getActionAssistant().playerHasItem(163, 1) && c.getActionAssistant().playerHasItem(2481,1)) {
				c.getActionAssistant().startAnimation(363);
				c.getActionAssistant().deleteItem(163, c.getActionAssistant().getItemSlot(163), 1);
				c.getActionAssistant().deleteItem(995, c.getActionAssistant().getItemSlot(995), 5000000);
				c.getActionAssistant().deleteItem(2481, c.getActionAssistant().getItemSlot(2481), 1);
				c.getActionAssistant().addItem(15317,1);
				c.getActionAssistant().addSkillXP(200,SkillConstants.HERBLORE);
				c.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
				}
				else {
				c.getActionAssistant().Send("You need 5m coins to make this");
				}
			}
			else {
				c.getActionAssistant().Send("You need a herblore level of 90 to make this");
				}
		}
		if (itemUsed == 3042 && usedWith == 2998 || itemUsed == 2998 && usedWith == 3042) {
			if (c.playerLevel[15] >= 90) {
				if (c.getActionAssistant().playerHasItem(995,5000000) && c.getActionAssistant().playerHasItem(2998, 1) && c.getActionAssistant().playerHasItem(3042,1)) {
				c.getActionAssistant().startAnimation(363);
				c.getActionAssistant().deleteItem(2998, c.getActionAssistant().getItemSlot(2998), 1);
				c.getActionAssistant().deleteItem(995, c.getActionAssistant().getItemSlot(995), 5000000);
				c.getActionAssistant().deleteItem(3042, c.getActionAssistant().getItemSlot(3042), 1);
				c.getActionAssistant().addItem(15321,1);
				c.getActionAssistant().addSkillXP(200,SkillConstants.HERBLORE);
				c.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
				}
				else {
				c.getActionAssistant().Send("You need 5m coins to make this");
				}
			}
			else {
				c.getActionAssistant().Send("You need a herblore level of 90 to make this");
				}
		}
		if (itemUsed == 245 && usedWith == 169 || itemUsed == 169 && usedWith == 245) {
			if (c.playerLevel[15] >= 90) {
				if (c.getActionAssistant().playerHasItem(995,5000000) && c.getActionAssistant().playerHasItem(169, 1) && c.getActionAssistant().playerHasItem(245,1)) {
				c.getActionAssistant().startAnimation(363);
				c.getActionAssistant().deleteItem(169, c.getActionAssistant().getItemSlot(169), 1);
				c.getActionAssistant().deleteItem(995, c.getActionAssistant().getItemSlot(995), 5000000);
				c.getActionAssistant().deleteItem(245, c.getActionAssistant().getItemSlot(245), 1);
				c.getActionAssistant().addItem(15325,1);
				c.getActionAssistant().addSkillXP(200,SkillConstants.HERBLORE);
				c.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
				}
				else {
				c.getActionAssistant().Send("You need 5m coins to make this");
				}
			}
			else {
				c.getActionAssistant().Send("You need a herblore level of 90 to make this");
				}
		}
			if (itemUsed == 269 && (usedWith == 15325 || usedWith == 15309 || usedWith == 15313 || usedWith == 15317 || usedWith == 15321)) {
			if (c.playerLevel[15] >= 99) {
				if (c.getActionAssistant().playerHasItem(995,25000000) && c.getActionAssistant().playerHasItem(15309, 1) && c.getActionAssistant().playerHasItem(15313,1)&& c.getActionAssistant().playerHasItem(15317,1)
				&& c.getActionAssistant().playerHasItem(15325,1)&& c.getActionAssistant().playerHasItem(15321,1)) {
				c.getActionAssistant().startAnimation(363);
				c.getActionAssistant().deleteItem(15325, c.getActionAssistant().getItemSlot(15325), 1);
				c.getActionAssistant().deleteItem(15321, c.getActionAssistant().getItemSlot(15321), 1);
				c.getActionAssistant().deleteItem(15309, c.getActionAssistant().getItemSlot(15309), 1);
				c.getActionAssistant().deleteItem(15313, c.getActionAssistant().getItemSlot(15313), 1);
				c.getActionAssistant().deleteItem(15317, c.getActionAssistant().getItemSlot(15317), 1);
				c.getActionAssistant().deleteItem(995, c.getActionAssistant().getItemSlot(995), 25000000);
				c.getActionAssistant().addItem(15333,1);
				c.getActionAssistant().addSkillXP(300,SkillConstants.HERBLORE);
				c.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
				c.achievementProgress[0] += 1;
				if (c.achievementProgress[0] == 1000) {
					c.getNRAchievements().checkSkilling(c,8);
				}
				}
				else {
				c.getActionAssistant().Send("You do not have the required ingredients to make this");
				}
			}
			else {
				c.getActionAssistant().Send("You need a herblore level of 99 to make this");
				}
		}
		if(itemUsed == 13734 && usedWith == 9954 || itemUsed == 9954 && usedWith == 13734) {		
			if(c.playerLevel[5] >= 85){//blessed
				c.getActionAssistant().deleteItem(13734, c.getActionAssistant().getItemSlot(13734), 1);
				c.getActionAssistant().deleteItem(9954, c.getActionAssistant().getItemSlot(9954), 1);
				c.getActionAssistant().addItem(13736, 1);
			} else {
				c.getActionAssistant().Send("You need 85 prayer to make this.");
			}
		}
		
		if(itemUsed == 11690 && usedWith == 9956 || itemUsed == 9956 && usedWith == 11690) {
			if(c.playerLevel[13] >= 70) {
					c.getActionAssistant().deleteItem(11690, c.getActionAssistant().getItemSlot(11690), 1);
					c.getActionAssistant().deleteItem(9956, c.getActionAssistant().getItemSlot(9956), 1);
					c.getActionAssistant().addItem(11696,1);	
			} else {
				c.getActionAssistant().Send("You need 70 smithing to make this.");
			}
		}
		
		if(itemUsed == 11690 && usedWith == 9957 || itemUsed == 9957 && usedWith == 11690) {
			if(c.playerLevel[13] >= 70) {
				c.getActionAssistant().deleteItem(11690, c.getActionAssistant().getItemSlot(11690), 1);
				c.getActionAssistant().deleteItem(9957, c.getActionAssistant().getItemSlot(9957), 1);
				c.getActionAssistant().addItem(11694,1);	
			} else {
				c.getActionAssistant().Send("You need 70 smithing to make this.");
			}
		}

		
		if(itemUsed == 11690 && usedWith == 9958 || itemUsed == 9958 && usedWith == 11690) {
			if(c.playerLevel[13] >= 70) {
					c.getActionAssistant().deleteItem(11690, c.getActionAssistant().getItemSlot(11690), 1);
					c.getActionAssistant().deleteItem(9958, c.getActionAssistant().getItemSlot(9958), 1);
					c.getActionAssistant().addItem(11698,1);	
			} else {
				c.getActionAssistant().Send("You need 70 smithing to make this.");
			}
		}
		
		if(itemUsed == 11690 && usedWith == 9959 || itemUsed == 9959 && usedWith == 11690) {
			if(c.playerLevel[13] >= 70) {
					c.getActionAssistant().deleteItem(11690, c.getActionAssistant().getItemSlot(11690), 1);
					c.getActionAssistant().deleteItem(9959, c.getActionAssistant().getItemSlot(9959), 1);
					c.getActionAssistant().addItem(11700,1);	
			} else {
				c.getActionAssistant().Send("You need 70 smithing to make this.");
			}
		}
		
		
		if(itemUsed == 11710 && usedWith == 11712 || itemUsed == 11714 && usedWith == 11712 || itemUsed == 11712 && usedWith == 11710 || itemUsed == 11714 && usedWith == 11710 || itemUsed == 11710 && usedWith == 11714 || itemUsed == 11712 && usedWith == 11714) {
			if(c.playerLevel[13] >= 70) {
				if(c.actionAssistant.playerHasItem(11710,1) && c.actionAssistant.playerHasItem(11712,1) && c.actionAssistant.playerHasItem(11714,1)){
					c.getActionAssistant().deleteItem(11710, c.getActionAssistant().getItemSlot(11710), 1);
					c.getActionAssistant().deleteItem(11712, c.getActionAssistant().getItemSlot(11712), 1);
					c.getActionAssistant().deleteItem(11714, c.getActionAssistant().getItemSlot(11714), 1);
					c.getActionAssistant().addItem(11690,1);				
					c.getActionAssistant().Send("You combined all 3 shards to make a godsword blade!");
				} else {
					c.getActionAssistant().Send("You need all 3 shards on you to create a godsword!");
					}
				} else {
				c.getActionAssistant().Send("You need 70 smithing to make this.");
				}
		}
		
		if(itemUsed == 13752 && usedWith == 13736 || itemUsed == 13736 && usedWith == 13752) {		
			if(c.playerLevel[5] >= 90 && c.playerLevel[13] >= 90){//spectral
				c.getActionAssistant().deleteItem(13752, c.getActionAssistant().getItemSlot(13752), 1);
				c.getActionAssistant().deleteItem(13736, c.getActionAssistant().getItemSlot(13736), 1);
				c.getActionAssistant().addItem(13744, 1);
			} else {
				c.getActionAssistant().Send("You need 90 prayer & 90 smithing to make this.");
			}
			}
				   
		if(itemUsed == 13750 && usedWith == 13736 || itemUsed == 13736 && usedWith == 13750) {		
			if(c.playerLevel[5] >= 90 && c.playerLevel[13] >= 90){//Elysian
				c.getActionAssistant().deleteItem(13750, c.getActionAssistant().getItemSlot(13750), 1);
				c.getActionAssistant().deleteItem(13736, c.getActionAssistant().getItemSlot(13736), 1);
				c.getActionAssistant().addItem(13742, 1);
			} else {
				c.getActionAssistant().Send("You need 90 prayer & 90 smithing to make this.");
			}
		}		   
		if(itemUsed == 13748 && usedWith == 13736 || itemUsed == 13736 && usedWith == 13748) {		
			if(c.playerLevel[5] >= 90 && c.playerLevel[13] >= 90){//Divine
				c.getActionAssistant().deleteItem(13748, c.getActionAssistant().getItemSlot(13748), 1);
				c.getActionAssistant().deleteItem(13736, c.getActionAssistant().getItemSlot(13736), 1);
				c.getActionAssistant().addItem(13740, 1);
			} else {
				c.getActionAssistant().Send("You need 90 prayer & 90 smithing to make this.");
			}
		}			   
		if(itemUsed == 13746 && usedWith == 13736 || itemUsed == 13736 && usedWith == 13746) {		
			if(c.playerLevel[5] >= 90 && c.playerLevel[13] >= 90){//Arcane
				c.getActionAssistant().deleteItem(13746, c.getActionAssistant().getItemSlot(13746), 1);
				c.getActionAssistant().deleteItem(13736, c.getActionAssistant().getItemSlot(13736), 1);
				c.getActionAssistant().addItem(13738, 1);
			} else {
				c.getActionAssistant().Send("You need 90 prayer & 90 smithing to make this.");
			}
		}		
		if(itemUsed == 1785 && usedWith == 1775 || itemUsed == 1775 && usedWith == 1785) {
			c.getActionAssistant().startAnimation(884);
			final Client d = (Client) c;
			if (d.checkBusy()) {
				return;
			}
			d.setBusy(true);			

			EventManager.getSingleton().addEvent(d,"itemonitem", new Event() {

			@Override
			public void execute(EventContainer cont) {
				
				cont.stop();
			}

			@Override
			public void stop() {
				d.getActionAssistant().deleteItem(1775, d.getActionAssistant().getItemSlot(1775), 1);
				d.getActionAssistant().addItem(229, 1);	
				d.setBusy(false);
			}

			}, 1400);
			
			
			return;		
		}
		
		if(itemUsed == 1759 && usedWith == 1673 || itemUsed == 1673 && usedWith == 1759) {
			c.getActionAssistant().deleteItem(1673, c.getActionAssistant().getItemSlot(1673), 1);
			c.getActionAssistant().deleteItem(1759, c.getActionAssistant().getItemSlot(1759), 1);
			c.getActionAssistant().addItem(1692, 1);	
			return;		
		}
		if(itemUsed == 1759 && usedWith == 1675 || itemUsed == 1675 && usedWith == 1759) {
			c.getActionAssistant().deleteItem(1675, c.getActionAssistant().getItemSlot(1675), 1);
			c.getActionAssistant().deleteItem(1759, c.getActionAssistant().getItemSlot(1759), 1);
			c.getActionAssistant().addItem(1694, 1);	
			return;		
		}	
		if(itemUsed == 1759 && usedWith == 1677 || itemUsed == 1677 && usedWith == 1759) {
			c.getActionAssistant().deleteItem(1677, c.getActionAssistant().getItemSlot(1677), 1);
			c.getActionAssistant().deleteItem(1759, c.getActionAssistant().getItemSlot(1759), 1);
			c.getActionAssistant().addItem(1696, 1);	
			return;		
		
		
		}		
		if(itemUsed == 1759 && usedWith == 1679 || itemUsed == 1679 && usedWith == 1759) {
			c.getActionAssistant().deleteItem(1679, c.getActionAssistant().getItemSlot(1679), 1);
			c.getActionAssistant().deleteItem(1759, c.getActionAssistant().getItemSlot(1759), 1);
			c.getActionAssistant().addItem(1698, 1);	
			return;		
		}	
		if(itemUsed == 1759 && usedWith == 1681 || itemUsed == 1681 && usedWith == 1759) {
			c.getActionAssistant().deleteItem(1681, c.getActionAssistant().getItemSlot(1681), 1);
			c.getActionAssistant().deleteItem(1759, c.getActionAssistant().getItemSlot(1759), 1);
			c.getActionAssistant().addItem(1700, 1);	
			return;		
		}	
		if(itemUsed == 1759 && usedWith == 1683 || itemUsed == 1683 && usedWith == 1759) {
			c.getActionAssistant().deleteItem(1683, c.getActionAssistant().getItemSlot(1683), 1);
			c.getActionAssistant().deleteItem(1759, c.getActionAssistant().getItemSlot(1759), 1);
			c.getActionAssistant().addItem(1702, 1);	
			return;		
		}		
		if(itemUsed == 1759 && usedWith == 6579 || itemUsed == 6579 && usedWith == 1759) {
			c.getActionAssistant().deleteItem(6579, c.getActionAssistant().getItemSlot(6579), 1);
			c.getActionAssistant().deleteItem(1759, c.getActionAssistant().getItemSlot(1759), 1);
			c.getActionAssistant().addItem(6581, 1);	
			return;		
		}		
		if(itemUsed == 233 && usedWith == 237 || itemUsed == 237 &&usedWith == 233) {
			c.getActionAssistant().startAnimation(364);
			c.getActionAssistant().deleteItem(237, c.getActionAssistant().getItemSlot(237), 1);
			c.getActionAssistant().addItem(235, 1);	
			return;
		}		
		if((itemUsed == 1741 && usedWith == 1733)){
			c.getActionAssistant().showInterface(2311);
			return;
		} else if((itemUsed == 1733 && usedWith == 1741)){
			c.getActionAssistant().showInterface(2311);
			return;
		}	
		/*if((itemUsed == 1743 && usedWith == 1733)){
			c.getActionAssistant().showInterface(2311);
			return;
		} else if((itemUsed == 1733 && usedWith == 1743)){
			c.getActionAssistant().showInterface(2311);
			return;
		}*/			
		if(itemUsed == 1733 && usedWith == 1745 || itemUsed == 1745 &&usedWith == 1733) {
			c.dhide = true;
			c.getActionAssistant().GreenDragonhideCrafting();
			return;
			}
		if(itemUsed == 1733 && usedWith == 2505 || itemUsed == 2505 &&usedWith == 1733) {
			c.dhide = true;
			c.getActionAssistant().BlueDragonhideCrafting();
			return;
			}
		if(itemUsed == 1733 && usedWith == 2507 || itemUsed == 2507 &&usedWith == 1733) {
			c.dhide = true;
			c.getActionAssistant().RedDragonhideCrafting();
			return;
			}
		if(itemUsed == 1733 && usedWith == 2509 || itemUsed == 2509 &&usedWith == 1733) {
			c.dhide = true;
			c.getActionAssistant().BlackDragonhideCrafting();
			return;
			}		
		switch (itemUsed) {
		//Dhide Crafting

		//Dhide Crafting END
		case 15309:
		case 15310:
		case 15311:
		case 15313:
		case 15314:
		case 15315:
		case 15317:
		case 15318:
		case 15319:
		case 15321:
		case 15322:
		case 15323:
		case 15325:
		case 15326:
		case 15327:
			case 115:
			case 117:
			case 119:
			case 121:
			case 123:
			case 125:
			case 127:
			case 129:
			case 131:
			case 133:
			case 135:
			case 137:
			case 139:
			case 141:
			case 143:
			case 145:
			case 147:
			case 149:
			case 151:
			case 153:
			case 155:
			case 157:
			case 159:
			case 161:
			case 163:
			case 165:
			case 167:
			case 169:
			case 171:
			case 173:
			case 175:
			case 177:
			case 179:
			case 181:
			case 183:
			case 185:
			case 189:
			case 191:
			case 193:
			case 2452:
			case 2454:
			case 2456:
			case 2458:
			case 3008:
			case 3010:
			case 3012:
			case 3014:
			case 3016:
			case 3018:
			case 3020:
			case 3022:
			case 3026:
			case 3028:
			case 3030:
			case 3032:
			case 3034:
			case 3036:
			case 3038:
			case 3042:
			case 3044:
			case 3046:
			case 6685:
			case 6687:
			case 6689:
			case 6691:
			case 113:
			case 2428:
			case 2430:
			case 2432:
			case 2434:
			case 2436:
			case 2438:
			case 2440:
			case 2442:
			case 2444:
			case 2446:
			case 2448:
			case 2450:
			case 3024:
			case 3040:
			case 5943:
				Pots.combine(c, itemUsed, usedWith);
			break;	
			case 2366:
				if (usedWith == 2368){
					if(c.playerLevel[13] >= 60){
						c.getActionAssistant().deleteItem(2366, c.getActionAssistant().getItemSlot(2366), 1);
						c.getActionAssistant().deleteItem(2368, c.getActionAssistant().getItemSlot(2368), 1);
						c.getActionAssistant().addItem(1187, 1);
					} else {
						c.getActionAssistant().Send("You need 60 smithing to make this.");
					}
				}
				break;	
			case 2368:
				if (usedWith == 2366){
					if(c.playerLevel[13] >= 60){
						c.getActionAssistant().deleteItem(2366, c.getActionAssistant().getItemSlot(2366), 1);
						c.getActionAssistant().deleteItem(2368, c.getActionAssistant().getItemSlot(2368), 1);
						c.getActionAssistant().addItem(1187, 1);
					} else {
						c.getActionAssistant().Send("You need 60 smithing to make this.");
					}
				}
				break;					
			case 11286:
				if (usedWith == 1540){
					if(c.playerLevel[13] >= 90){
						c.getActionAssistant().deleteItem(1540, c.getActionAssistant().getItemSlot(1540), 1);
						c.getActionAssistant().deleteItem(11286, c.getActionAssistant().getItemSlot(11286), 1);
						c.getActionAssistant().addItem(11283, 1);
					} else {
						c.getActionAssistant().Send("You need 90 smithing to make this.");
					}
				}
			break;
			case 1540:
				if (usedWith == 11286){
					if(c.playerLevel[13] >= 90){
						c.getActionAssistant().deleteItem(1540, c.getActionAssistant().getItemSlot(1540), 1);
						c.getActionAssistant().deleteItem(11286, c.getActionAssistant().getItemSlot(11286), 1);
						c.getActionAssistant().addItem(11283, 1);
					} else {
						c.getActionAssistant().Send("You need 90 smithing to make this.");
					}
				}
			break;			
			case 590:
			if (usedWith == 1511){
				Firemaking.fire(c, 1, 1511, 100);
			}
			if (usedWith == 2511){
				Firemaking.fire(c, 1, 2511, 100);
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
			if (usedWith == 6333){
				Firemaking.fire(c, 35, 6333, 105);
			}
			if (usedWith == 6332){
				Firemaking.fire(c, 50, 6332, 158);
			}			
			break;
		case 6332:	
			if (usedWith == 590){
				Firemaking.fire(c, 50, 6332, 158);
			}			
		break;			
		case 6333:	
			if (usedWith == 590){
				Firemaking.fire(c, 35, 6333, 105);
			}			
		break;			
		case 1511:	
			if (usedWith == 590){
				Firemaking.fire(c, 1, 1511, 100);
			}			
		break;
		case 2511:	
			if (usedWith == 590){
				Firemaking.fire(c, 1, 2511, 100);
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
			c.getActionAssistant().Craft(50, 1623, 1607, 20);
		}			
		if (usedWith == 1621){
			c.getActionAssistant().Craft(68, 1621, 1605, 27);
		}		
		if (usedWith == 1619){
			c.getActionAssistant().Craft(85, 1619, 1603, 34);
		}
		if (usedWith == 1617){
			c.getActionAssistant().Craft(108, 1617, 1601, 43);
		}
		if (usedWith == 1631){
			c.getActionAssistant().Craft(138, 1631, 1615, 55);
		}	
		if (usedWith == 6571){
			c.getActionAssistant().Craft(168, 6571, 6573, 67);
		}
		break;
		case 1623:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(50, 1623, 1607, 20);
		}	
		break;
		case 1621:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(68, 1621, 1605, 27);
		}	
		break;
		case 1619:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(85, 1619, 1603, 34);
		}	
		break;
		case 1617:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(108, 1617, 1601, 43);
		}	
		break;
		case 1631:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(138, 1631, 1615, 55);
		}	
		break;				
		case 6571:		
		if (usedWith == 1755){
			c.getActionAssistant().Craft(168, 6571, 6573, 67);
		}	
		break;
	
		
		default:
		//	c.getActionAssistant().Send("Nothing interesting happens.");
		break;			
			
			
			
		}
	}
}
	
	
	
