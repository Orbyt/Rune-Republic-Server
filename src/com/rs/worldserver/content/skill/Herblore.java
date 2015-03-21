package com.rs.worldserver.content.skill;

import com.rs.worldserver.Server;
import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

/**
 * Herblore skill handler
 * 
 * @author Chachi
 * 
 */
public class Herblore implements Skill {

	private static final int[] HERBS = { 249,251,253,255,257,2998,259,263,3000,265,2481,267,6016,269,2398};
	private static final int[] HLEVEL = {1,5,12,22,30,34,45,55,63,66,69,72,73,78,82};
	private static final int[] UNFINISHED = { 91,93,95,97,99,3002,101,105,3004,107,2483,109,5936,111,5939};
	private static final int[] INGREDIENTS = { 221,235,225,223,239,2152,231,221,225,241,223,239,241,245,223,3138,6693,247,6018};
	private static final int[] LEVEL =         {1,  5,  12, 22, 30, 34,  38, 45,48,  55, 60, 63, 66, 69, 72, 73,  78,  81,  82};
	private static final int[] EXP =          {25, 37, 50,  62, 75, 80, 87,100,106, 125,137,142,150,157,162,165,  175,180,190};
	
	private static final int[] VIAL = {229};
	
	private static final int[] WFVIAL = {227};
	

	public static int getIntArray(int[] array1, int[] array2, int herb) {
		int a = 0;
		for (int object : array1) {
			if (object == herb) {
				return array2[a];
			}
			a++;
		}
		return -1;
	}

	public static String getStringArray(int[] array1, String[] array2, int herb) {
		int a = 0;
		for (int object : array1) {
			if (object == herb) {
				return array2[a];
			}
			a++;
		}
		return "";
	}

	public static int contains(int[] array, int value) {
		for (int i : array) {
			if (i == value)
				return value;
		}
		return 0;
	}	
	
	public static boolean isMakable(Client client, int usedItem, int usedWithItem) {
		if (usedItem == contains(HERBS, usedItem) && usedWithItem == contains(WFVIAL, usedWithItem)) {
			return startMixingUnf(client,usedItem,usedWithItem);
		} 
		if (usedWithItem == contains(HERBS, usedWithItem) && usedItem == contains(WFVIAL, usedWithItem)) {
			return startMixingUnf(client,usedWithItem,usedItem);
		}	
		if (usedItem == contains(UNFINISHED, usedItem) && usedWithItem == contains(INGREDIENTS, usedWithItem)) {
			return sMixing(client,usedWithItem,usedItem);	}		
		if (usedWithItem == contains(UNFINISHED, usedWithItem) && usedItem == contains(INGREDIENTS, usedItem)) {
			return sMixing(client,usedWithItem,usedItem);		
		} else {
			return false;
		}
	}
			
	public static boolean startMixingUnf(final Client client,final int usedItem,final int usedWithItem) {
		if (client.checkBusy()) {
			return true;
		}
		if (client.playerLevel[15] >= getIntArray(HERBS, HLEVEL, usedItem)) {		
			int unfPot = getIntArray(HERBS, UNFINISHED, usedItem);
			client.getActionAssistant().startAnimation(363);
			client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
			client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
			client.getActionAssistant().addItem(unfPot, 1);
			
			
		} else {
			client.getActionAssistant().sendMessage("You need a herblore level of "
				+ getIntArray(HERBS, HLEVEL, usedItem)
				+ " to make that potion.");
		}
		return true;
	}
	public static boolean sMixing(final Client client,final int usedItem,final int usedWithItem) {
		if (client.checkBusy()) {
			return true;
		}
		if((usedItem == 221 && usedWithItem == 91) || (usedItem == 91 && usedWithItem == 221)){	
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(221, client.getActionAssistant().getItemSlot(221), 1);
				client.getActionAssistant().deleteItem(91, client.getActionAssistant().getItemSlot(91), 1);
				client.getActionAssistant().addItem(2428, 1);
				client.getActionAssistant().addSkillXP(25,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			
		}
		if((usedItem == 235 && usedWithItem == 93) ||(usedItem == 93 && usedWithItem == 235)){	
				if (client.playerLevel[15] >= 5) {
					client.getActionAssistant().startAnimation(363);
					client.getActionAssistant().deleteItem(235, client.getActionAssistant().getItemSlot(235), 1);
					client.getActionAssistant().deleteItem(93, client.getActionAssistant().getItemSlot(93), 1);
					client.getActionAssistant().addItem(2446, 1);
					client.getActionAssistant().addSkillXP(37,SkillConstants.HERBLORE);
					client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
				} else {
					client.getActionAssistant().sendMessage("You need a herblore level of 5 to make that potion.");				
					return true;
				}
			
		}		
		if((usedItem == 225 && usedWithItem == 95) ||(usedItem == 95 && usedWithItem == 225)){
				if (client.playerLevel[15] >= 12) {
					client.getActionAssistant().startAnimation(363);
					client.getActionAssistant().deleteItem(225, client.getActionAssistant().getItemSlot(225), 1);
					client.getActionAssistant().deleteItem(95, client.getActionAssistant().getItemSlot(95), 1);
					client.getActionAssistant().addItem(113, 1);
					client.getActionAssistant().addSkillXP(50,SkillConstants.HERBLORE);
					client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
				} else {
					client.getActionAssistant().sendMessage("You need a herblore level of 12 to make that potion.");				
					return false;
				}
			
		}	
		if((usedItem == 223 && usedWithItem == 97) ||(usedItem == 97 && usedWithItem == 223)){
				if (client.playerLevel[15] >= 22) {
					client.getActionAssistant().startAnimation(363);
					client.getActionAssistant().deleteItem(223, client.getActionAssistant().getItemSlot(223), 1);
					client.getActionAssistant().deleteItem(97, client.getActionAssistant().getItemSlot(97), 1);
					client.getActionAssistant().addItem(2430, 1);
					client.getActionAssistant().addSkillXP(62,SkillConstants.HERBLORE);
					client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
				} else {
					client.getActionAssistant().sendMessage("You need a herblore level of 22 to make that potion.");				
					return false;
				}
			
		}		
		if((usedItem == 239 && usedWithItem == 99) || (usedItem == 99 && usedWithItem == 239)){
			if (client.playerLevel[15] >= 30) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(2432, 1);
				client.getActionAssistant().addSkillXP(75,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 30 to make that potion.");				
				return false;
			}
		}		
		if((usedItem == 2998 && usedWithItem == 3002) || (usedItem == 3002 && usedWithItem == 2998)){
			if (client.playerLevel[15] >= 34) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(3032, 1);
				client.getActionAssistant().addSkillXP(80,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 34 to make that potion.");				
				return false;
			}
		}	
		if((usedItem == 99 && usedWithItem == 231) || (usedItem == 231 && usedWithItem == 99)){
			if (client.playerLevel[15] >= 38) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(2434, 1);
				client.getActionAssistant().addSkillXP(87,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 38 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 101 && usedWithItem == 221) || (usedItem == 221 && usedWithItem == 101)){
			if (client.playerLevel[15] >= 45) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(2436, 1);
				client.getActionAssistant().addSkillXP(100,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 45 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 101 && usedWithItem == 235) || (usedItem == 235 && usedWithItem == 101)){
			if (client.playerLevel[15] >= 48) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(2448, 1);
				client.getActionAssistant().addSkillXP(106,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 48 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 105 && usedWithItem == 225) || (usedItem == 225 && usedWithItem == 105)){
			if (client.playerLevel[15] >= 55) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(2440, 1);
				client.getActionAssistant().addSkillXP(125,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 55 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 105 && usedWithItem == 241) || (usedItem == 241 && usedWithItem == 105)){
			if (client.playerLevel[15] >= 60) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(187, 1);
				client.getActionAssistant().addSkillXP(137,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 60 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 3004 && usedWithItem == 223) || (usedItem == 223 && usedWithItem == 3004)){
			if (client.playerLevel[15] >= 63) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(3024, 1);
				client.getActionAssistant().addSkillXP(142,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 63 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 107 && usedWithItem == 239) || (usedItem == 239 && usedWithItem == 107)){
			if (client.playerLevel[15] >= 66) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(2442, 1);
				client.getActionAssistant().addSkillXP(150,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 66 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 2483 && usedWithItem == 241) || (usedItem == 241 && usedWithItem == 2483)){
			if (client.playerLevel[15] >= 69) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(2452, 1);
				client.getActionAssistant().addSkillXP(157,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 69 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 109 && usedWithItem == 245) || (usedItem == 245 && usedWithItem == 109)){
			if (client.playerLevel[15] >= 72) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(2444, 1);
				client.getActionAssistant().addSkillXP(162,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 72 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 5936 && usedWithItem == 223) || (usedItem == 223 && usedWithItem == 5936)){
			if (client.playerLevel[15] >= 73) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(5937, 1);
				client.getActionAssistant().addSkillXP(165,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 73 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 111 && usedWithItem == 247) || (usedItem == 247 && usedWithItem == 111)){
			if (client.playerLevel[15] >= 78) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(2450, 1);
				client.getActionAssistant().addSkillXP(175,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 78 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 3002 && usedWithItem == 6693) || (usedItem == 6693 && usedWithItem == 3002)){
			if (client.playerLevel[15] >= 81) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(6685, 1);
				client.getActionAssistant().addSkillXP(180,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 81 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 5939 && usedWithItem == 6018) || (usedItem == 6018 && usedWithItem == 5939)){
			if (client.playerLevel[15] >= 82) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(5940, 1);
				client.getActionAssistant().addSkillXP(190,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 82 to make that potion.");				
				return false;
			}
		}
		if((usedItem == 2483 && usedWithItem == 3138) || (usedItem == 3138 && usedWithItem == 2483)){
			if (client.playerLevel[15] >= 76) {
				client.getActionAssistant().startAnimation(363);
				client.getActionAssistant().deleteItem(usedItem, client.getActionAssistant().getItemSlot(usedItem), 1);
				client.getActionAssistant().deleteItem(usedWithItem, client.getActionAssistant().getItemSlot(usedWithItem), 1);
				client.getActionAssistant().addItem(3042, 1);
				client.getActionAssistant().addSkillXP(150,SkillConstants.HERBLORE);
				client.getActionAssistant().refreshSkill(SkillConstants.HERBLORE);
			} else {
				client.getActionAssistant().sendMessage("You need a herblore level of 76 to make that potion.");				
				return false;
			}
		}
		
		return true;
	}		

	
}