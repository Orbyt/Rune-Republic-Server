package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.content.skill.Fletching;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.*;

public class MagicOnItems implements Packet {

	public int castOnSlot, castOnItem, e3, castSpell;

	@Override
	public void handlePacket(Client c, int packetType, int packetSize) {
			castOnSlot = c.getInStream().readSignedWord();
			castOnItem = c.getInStream().readSignedWordA();
			e3 = c.getInStream().readSignedWord();
			castSpell = c.getInStream().readSignedWordA();
			c.println_debug("Magic Spell: " + castSpell + ","+ castOnItem);
			int alchvaluez = (int) Math.floor(Server.getItemManager().getItemDefinition(castOnItem).getShopValue());
			if (castOnItem == 4828 || castOnItem == 4827 || castOnItem == 7774 || castOnItem == 7775 || castOnItem == 1464) {
			alchvaluez = 1;
			}
			if (castOnItem >= 11694 && castOnItem <= 7896) {
			alchvaluez = 50000000;
			}
			/*if (Misc.random(200) == 1) {Server.getItemManager().getItemDefinition(castOnItem).getShopValue()

				c.triggerRandom();
				return;
			}*/
			if(c.blackMarks  > 0) {
				c.getActionAssistant().sendMessage("This action is disabled while jailed");
				return;
			}
			if ((c.playerItems[castOnSlot] - 1) != castOnItem) {
				c.getActionAssistant().sendMessage("You don't have that item!");
				return;
            		}
			if (!c.getActionAssistant().playerHasItem(castOnItem,1)) {
				c.getActionAssistant().sendMessage("You don't have that item!");
				return;
			}
			
	if(castSpell == 1173) {		
		/*	if(c.playerLevel[6] < 43) {
				c.getActionAssistant().sendMessage("You need the magic level of 43 or higher to cast this spell.");
				break;
			}
			if(!c.getActionAssistant().playerHasItemAmount(554, 5) && !c.getActionAssistant().playerHasItemAmount(561, 1)) {
				c.getActionAssistant().sendMessage("You do not have enough runes to cast this spell.");
				break;
			}
			switch (castOnItem) {
				case 436:
					if(!c.getActionAssistant().playerHasItemAmount(436, 1)) {
						c.getActionAssistant().sendMessage("You haven't got the correct ores to superheat this item.");
						break;
					}
					c.getActionAssistant().deleteItem(castOnItem, c.getActionAssistant().getItemSlot(castOnItem), 1);
					c.getActionAssistant().deleteItem(438, c.getActionAssistant().getItemSlot(438), 1);
					c.getActionAssistant().addItem(2349, 1);
					c.getActionAssistant().addSkillXP(53, 6);
					c.getActionAssistant().startAnimation(725);
					c.gfx100(148);
					break;

				case 438:
					if(!c.getActionAssistant().playerHasItemAmount(436, 1)) {
						c.getActionAssistant().sendMessage("You haven't got the correct ores to superheat this item.");
						break;
					}
					c.getActionAssistant().deleteItem(castOnItem, c.getActionAssistant().getItemSlot(castOnItem), 1);
					c.getActionAssistant().deleteItem(436, c.getActionAssistant().getItemSlot(436), 1);
					p.addItem(2349, 1);
					p.addSkillXP(53, 6);
					p.startAnimation(725);
					c.gfx100(148);
					break;

				case 440:
					if(!c.getActionAssistant().playerHasItemAmount(436, 1)) {
						c.getActionAssistant().sendMessage("You haven't got the correct ores to superheat this item.");
						break;
					}				
					c.getActionAssistant().deleteItem(castOnItem, c.getActionAssistant().getItemSlot(castOnItem), 1);
					c.getActionAssistant().addItem(2351, 1);
					c.getActionAssistant().addSkillXP(60, 6);
					c.getActionAssistant().startAnimation(725);
					c.gfx100(148);
					break;

				case 442:
					c.getActionAssistant().deleteItem(castOnItem, c.getActionAssistant().getItemSlot(castOnItem), 1);
					c.getActionAssistant().addItem(2355, 1);
					c.getActionAssistant().addSkillXP(70, 6);
					c.getActionAssistant().startAnimation(725);
					c.gfx100(148);
					break;

				case 447:
					c.getActionAssistant().deleteItem(castOnItem, c.getActionAssistant().getItemSlot(castOnItem), 1);
					c.getActionAssistant().addItem(2359, 1);
					c.getActionAssistant().addSkillXP(75, 6);
					c.getActionAssistant().startAnimation(725);
					c.gfx100(148);
					break;

				case 449:
					c.getActionAssistant().deleteItem(castOnItem, c.getActionAssistant().getItemSlot(castOnItem), 1);
					c.getActionAssistant().addItem(2361, 1);
					c.getActionAssistant().addSkillXP(80, 6);
					c.getActionAssistant().startAnimation(725);
					c.gfx100(148);
					break;

				case 451:
					c.getActionAssistant().deleteItem(castOnItem, c.getActionAssistant().getItemSlot(castOnItem), 1);
					c.getActionAssistant().addItem(2363, 1);
					c.getActionAssistant().addSkillXP(100, 6);
					c.getActionAssistant().startAnimation(725);
					c.gfx100(148);
					break;

				default: p.sendMessage("You can only cast this spell on ores."); break;
			}*/
		}	
	if(castSpell == 1155) {//enchant Saphs 7
		if(castOnItem == 1637 || castOnItem == 1656 || castOnItem == 1694) {
			if(c.playerLevel[6] >= 7) {
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==false) || (c.getActionAssistant().playerHasItemAmount(555, 1)==false)) {
				c.getActionAssistant().sendMessage("You don't have enough runes to cast this spell.");
				}
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==true) && (c.getActionAssistant().playerHasItemAmount(555, 1)==true)) {
					c.getActionAssistant().deleteItem(castOnItem, castOnSlot, 1);
					if (castOnItem == 1694) {
						c.getActionAssistant().addItem(1727, 1);
						c.getActionAssistant().startAnimation(712);
						c.gfx100(238);
						c.getActionAssistant().frame174(206, 000, 000);}
					if (castOnItem == 1637) {
						c.getActionAssistant().addItem(2550, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(114);
						c.getActionAssistant().frame174(207, 000, 000);}
					if (castOnItem == 1656)	{
						c.getActionAssistant().addItem(3853, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(114);
						c.getActionAssistant().frame174(207, 000, 000);}
					c.getActionAssistant().addSkillXP(1, 6);
					//c.getActionAssistant().setSidebarMage();
					c.getActionAssistant().deleteItem(564, c.getActionAssistant().getItemSlot(564), 1);
					c.getActionAssistant().deleteItem(555, c.getActionAssistant().getItemSlot(555), 1);
				}
			}
		}
	}
	if(castSpell == 1165) {//enchant Emeralds 27
		if(castOnItem == 1639 || castOnItem == 1658 || castOnItem == 1696) {
			if(c.playerLevel[6] >= 27) {
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==false) || (c.getActionAssistant().playerHasItemAmount(556, 3)==false)) {
					c.getActionAssistant().sendMessage("You don't have enough runes to cast this spell.");
				}
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==true) && (c.getActionAssistant().playerHasItemAmount(556, 3)==true)) {
					c.getActionAssistant().deleteItem(castOnItem, castOnSlot, 1);
					if (castOnItem == 1639) {
						c.getActionAssistant().addItem(2552, 1);
						c.getActionAssistant().startAnimation(712);
						c.gfx100(238);
						c.getActionAssistant().frame174(206, 000, 000);}
					if (castOnItem == 1658) {
						c.getActionAssistant().addItem(1658, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(114);
						c.getActionAssistant().frame174(207, 000, 000);}
					if (castOnItem == 1696)	{
						c.getActionAssistant().addItem(1729, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(114);
						c.getActionAssistant().frame174(207, 000, 000);}
					c.getActionAssistant().addSkillXP(1, 6);
					//c.getActionAssistant().setSidebarMage();
					c.getActionAssistant().deleteItem(564, c.getActionAssistant().getItemSlot(564), 1);
					c.getActionAssistant().deleteItem(556, c.getActionAssistant().getItemSlot(556), 3);
				}
			}
		}	
	}	
	if(castSpell == 1176) {//enchant Ruby 49
		if(castOnItem == 1641 || castOnItem == 1660 || castOnItem == 1698) {
			if(c.playerLevel[6] >= 49) {
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==false) || (c.getActionAssistant().playerHasItemAmount(554, 5)==false)) {
					c.getActionAssistant().sendMessage("You don't have enough runes to cast this spell.");
				}
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==true) && (c.getActionAssistant().playerHasItemAmount(554, 5)==true)) {
					c.getActionAssistant().deleteItem(castOnItem, castOnSlot, 1);
					if (castOnItem == 1641) {
						c.getActionAssistant().addItem(2568, 1);
						c.getActionAssistant().startAnimation(712);
						c.gfx100(238);
						c.getActionAssistant().frame174(206, 000, 000);}
					if (castOnItem == 1660) {
						c.getActionAssistant().addItem(1660, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(115);
						c.getActionAssistant().frame174(207, 000, 000);}
					if (castOnItem == 1698)	{
						c.getActionAssistant().addItem(1725, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(115);
						c.getActionAssistant().frame174(207, 000, 000);}
					c.getActionAssistant().addSkillXP(1, 6);
					//c.getActionAssistant().setSidebarMage();
					c.getActionAssistant().deleteItem(564, c.getActionAssistant().getItemSlot(564), 1);
					c.getActionAssistant().deleteItem(554, c.getActionAssistant().getItemSlot(554), 5);
				}
			}
		}		
	}	
	if(castSpell == 1180) {//enchant diamonds 57
		if(castOnItem == 1643 || castOnItem == 1662 || castOnItem == 1700) {
			if(c.playerLevel[6] >= 57) {
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==false) || (c.getActionAssistant().playerHasItemAmount(557, 10)==false)) {
					c.getActionAssistant().sendMessage("You don't have enough runes to cast this spell.");
				}
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==true) && (c.getActionAssistant().playerHasItemAmount(557, 10)==true)) {
					c.getActionAssistant().deleteItem(castOnItem, castOnSlot, 1);
					if (castOnItem == 1643) {
						c.getActionAssistant().addItem(2570, 1);
						c.getActionAssistant().startAnimation(712);
						c.gfx100(238);
						c.getActionAssistant().frame174(206, 000, 000);}
					if (castOnItem == 1662) {
						c.getActionAssistant().addItem(1662, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(116);
						c.getActionAssistant().frame174(207, 000, 000);}
					if (castOnItem == 1700)	{
						c.getActionAssistant().addItem(1731, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(116);
						c.getActionAssistant().frame174(207, 000, 000);}
					c.getActionAssistant().addSkillXP(1, 6);
					//c.getActionAssistant().setSidebarMage();
					c.getActionAssistant().deleteItem(564, c.getActionAssistant().getItemSlot(564), 1);
					c.getActionAssistant().deleteItem(554, c.getActionAssistant().getItemSlot(557), 10);
				}
			}
		}		
	}	
	if(castSpell == 1187) {//enchant dragonstones 68
		if(castOnItem == 1645 || castOnItem == 1664 || castOnItem == 1702) {
			if(c.playerLevel[6] >= 68) {
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==false) || (c.getActionAssistant().playerHasItemAmount(555, 15)==false) || (c.getActionAssistant().playerHasItemAmount(557, 15)==false)) {
					c.getActionAssistant().sendMessage("You don't have enough runes to cast this spell.");
				}
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==true) && (c.getActionAssistant().playerHasItemAmount(555, 15)==true) && (c.getActionAssistant().playerHasItemAmount(557, 15)==true)) {
					c.getActionAssistant().deleteItem(castOnItem, castOnSlot, 1);
					if (castOnItem == 1645) {
						c.getActionAssistant().addItem(2572, 1);
						c.getActionAssistant().startAnimation(712);
						c.gfx100(238);
						c.getActionAssistant().frame174(213, 000, 000);}
					if (castOnItem == 1664) {
						c.getActionAssistant().addItem(1664, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(116);
						c.getActionAssistant().frame174(214, 000, 000);}
					if (castOnItem == 1702)	{
						c.getActionAssistant().addItem(1712, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(116);
						c.getActionAssistant().frame174(215, 000, 000);}
					c.getActionAssistant().addSkillXP(1, 6);
					//c.getActionAssistant().setSidebarMage();
					c.getActionAssistant().deleteItem(564, c.getActionAssistant().getItemSlot(564), 1);
					c.getActionAssistant().deleteItem(554, c.getActionAssistant().getItemSlot(557), 15);
					c.getActionAssistant().deleteItem(555, c.getActionAssistant().getItemSlot(555), 15);
				}
			}
		}	
	}	
	if(castSpell == 6003) {//enchant Onyx 87
		if(castOnItem == 6575 || castOnItem == 6577 || castOnItem == 6581) {
			if(c.playerLevel[6] >= 87) {
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==false) || (c.getActionAssistant().playerHasItemAmount(554, 20)==false) || (c.getActionAssistant().playerHasItemAmount(557, 20)==false)) {
					c.getActionAssistant().sendMessage("You don't have enough runes to cast this spell.");
				}
				if((c.getActionAssistant().playerHasItemAmount(564, 1)==true) && (c.getActionAssistant().playerHasItemAmount(554, 20)==true) && (c.getActionAssistant().playerHasItemAmount(557, 20)==true)) {
					c.getActionAssistant().deleteItem(castOnItem, castOnSlot, 1);
					if (castOnItem == 6575) {
						c.getActionAssistant().addItem(6583, 1);
						c.getActionAssistant().startAnimation(712);
						c.gfx100(238);
						c.getActionAssistant().frame174(213, 000, 000);}
					if (castOnItem == 6577) {
						c.getActionAssistant().addItem(6577, 1);
						c.getActionAssistant().startAnimation(721);
						c.gfx100(452);
						c.getActionAssistant().frame174(214, 000, 000);}
						
					if (castOnItem == 6581)	{
						c.getActionAssistant().addItem(6585, 1);
						c.getActionAssistant().startAnimation(720);
						c.gfx100(452);
						c.getActionAssistant().frame174(215, 000, 000);}
					c.getActionAssistant().addSkillXP(1, 6);
					c.getActionAssistant().startAnimation(720);
					//c.getActionAssistant().setSidebarMage();
					c.getActionAssistant().deleteItem(564, c.getActionAssistant().getItemSlot(564), 1);
					c.getActionAssistant().deleteItem(554, c.getActionAssistant().getItemSlot(557), 20);
					c.getActionAssistant().deleteItem(555, c.getActionAssistant().getItemSlot(554), 20);
				}
			}
		}	
	}	
	if(castSpell == 1162) //High Alch  with staffs and Fire runes
	{
		if(c.playerLevel[6] >= 21)
		{
		if((c.getActionAssistant().playerHasItem(561, 1)==false) 
			|| (c.getActionAssistant().playerHasItem(554, 3)==false) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 1387 
			|| (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1387) && (c.getActionAssistant().playerHasItem(561, 1)==false))
		{
			c.getActionAssistant().sendMessage("You do not have enough runes to cast this spell.");
		}
		if((c.getActionAssistant().playerHasItem(561, 1)==true) && (c.getActionAssistant().playerHasItem(554, 3)==true) 
			|| (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1387) && (c.getActionAssistant().playerHasItem(561, 1)==true))
		{
		if(castOnItem == 995)
		{
			c.getActionAssistant().sendMessage("You can't cast high alchemy on gold.");
		} else {
		if(castOnItem == 1)
		{
			c.getActionAssistant().sendMessage("You cant convert this item.");
		} else if (System.currentTimeMillis() - c.lastAction > c.actionInterval){
			c.actionInterval = 3000;
			c.lastAction = System.currentTimeMillis();
			c.getActionAssistant().startAnimation(713);
			c.getCombat().resetAttack();
			c.gfx(113);
			if(c.sounds == 1) {
				c.getActionAssistant().frame174(223, 030, 000);
			}
			c.getActionAssistant().addSkillXP(1600, 6);
			c.getActionAssistant().refreshSkill(6);
			c.getActionAssistant().requestUpdates();
			alchvaluez = (alchvaluez / 3);
			c.getActionAssistant().deleteItem(castOnItem, castOnSlot, 1);
			c.getActionAssistant().addItem(995, alchvaluez);
			c.getActionAssistant().sendFrame106(6);
			c.getActionAssistant().deleteItem(561, c.getActionAssistant().getItemSlot(561), 1);//Remove nature rune
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 1387){
			c.getActionAssistant().deleteItem(554, c.getActionAssistant().getItemSlot(554), 3); //Remove fire rune
		}
		}
	}
	}
}
	else if(c.playerLevel[6] <= 20)
	{
		c.getActionAssistant().sendMessage("You need a magic level of 21 to cast this spell.");
	}
}	
	if(castSpell == 1178) //High Alch  with staffs and Fire runes
	{
		if(c.playerLevel[6] >= 55)
		{
		if((c.getActionAssistant().playerHasItem(561, 1)==false) 
			|| (c.getActionAssistant().playerHasItem(554, 5)==false) && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 1387 
			|| (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1387) && (c.getActionAssistant().playerHasItem(561, 1)==false))
		{
			c.getActionAssistant().sendMessage("You do not have enough runes to cast this spell.");
		}
		if((c.getActionAssistant().playerHasItem(561, 1)==true) && (c.getActionAssistant().playerHasItem(554, 5)==true) 
			|| (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1387) && (c.getActionAssistant().playerHasItem(561, 1)==true))
		{
		if(castOnItem == 995)
		{
			c.getActionAssistant().sendMessage("You can't cast high alchemy on gold.");
		} else {
		if(castOnItem == 1)
		{
			c.getActionAssistant().sendMessage("You cant convert this item.");
		} else if (System.currentTimeMillis() - c.lastAction > c.actionInterval){
			c.actionInterval = 3000;
			c.lastAction = System.currentTimeMillis();
			c.getActionAssistant().startAnimation(713);
			c.getCombat().resetAttack();
			c.gfx(113);
			if(c.sounds == 1) {
				c.getActionAssistant().frame174(223, 030, 000);
			}
			c.getActionAssistant().addSkillXP(1600, 6);
			c.getActionAssistant().refreshSkill(6);
			c.getActionAssistant().requestUpdates();
			alchvaluez = (alchvaluez / 3);
			c.getActionAssistant().deleteItem(castOnItem, castOnSlot, 1);
			c.getActionAssistant().addItem(995, alchvaluez);
			c.getActionAssistant().sendFrame106(6);
			c.getActionAssistant().deleteItem(561, c.getActionAssistant().getItemSlot(561), 1);//Remove nature rune
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != 1387){
			c.getActionAssistant().deleteItem(554, c.getActionAssistant().getItemSlot(554), 5); //Remove fire rune
		}
		}
	}
	}
}
	else if(c.playerLevel[6] <= 54)
	{
		c.getActionAssistant().sendMessage("You need a magic level of 55 to cast this spell.");
	}
}

	}

}
