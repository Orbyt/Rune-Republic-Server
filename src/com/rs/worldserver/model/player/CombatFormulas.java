package com.rs.worldserver.model.player;

import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.ItemDefinition;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.util.*;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import java.io.*;
import java.util.*;

public class CombatFormulas {

	private Client c;
	public CombatFormulas(Client c) {
		this.c = c;
	}
	
	public CombatFormulas(){
	
	}	
	public void diagonalAttackCorrection(Client c, int attackingPlayerId){
		Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[attackingPlayerId];
		if(c.getCombatFormulas().diagonal(c.getAbsX(),c.getAbsY(),targetPlayer.getAbsX(),targetPlayer.getAbsY()) && !c.follower){
			if (c.freezeTimer <= 0){
				int r = Misc.random(3);
				switch (r) {
					case 0:
						c.getActionAssistant().playerWalk(c.absX, c.absY - 1, "Diag Attack combatform");
						break;
					case 1:
						c.getActionAssistant().playerWalk(c.absX, c.absY + 1, "Diag Attack combatform");
						break;
					case 2:
						c.getActionAssistant().playerWalk(c.absX + 1, c.absY, "Diag Attack combatform");
						break;
					case 3:
						c.getActionAssistant().playerWalk(c.absX - 1, c.absY, "Diag Attack combatform");
						break;
						
				}
				//c.getActionAssistant().Send("Here");
				c.faceUpdate(attackingPlayerId+32768);
			} else {
				c.faceUpdate(attackingPlayerId+32768);
				return;
			}				
		}
	}
	
	public void sameSpotAttackCorrection(Client c, int attackingPlayerId){
		Client targetPlayer = (Client) PlayerManager.getSingleton().getPlayers()[attackingPlayerId];
		if (targetPlayer == null)
			return;	
		if (c.absX == targetPlayer.absX && c.absY == targetPlayer.absY){
			if (c.freezeTimer <= 0){
				c.setCanWalk(false);
				c.faceUpdate(targetPlayer.playerId+32768);
				int r = Misc.random(3);
				switch (r) {
					case 0:
						c.getActionAssistant().playerWalk(c.absX, c.absY - 1,"Same Spot combatform");
						break;
					case 1:
						c.getActionAssistant().playerWalk(c.absX, c.absY + 1,"Same Spot combatform");
						break;
					case 2:
						c.getActionAssistant().playerWalk(c.absX + 1, c.absY,"Same Spot combatform");
						break;
					case 3:
						c.getActionAssistant().playerWalk(c.absX - 1, c.absY,"Same Spot combatform");
						break;
				}
				c.setCanWalk(true);
			} else {
				return;
			}
		}
	}
	
	
	public boolean diagonal(int x, int y, int x1, int y1) {
		int xDial = Math.abs(x - x1);
		int yDial = Math.abs(y - y1);
		return xDial == 1 && yDial == 1;
	}	
	public boolean voidMelee(){
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 11676 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 8839 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 8840 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 8842){
			return true;
		}
		return false;
	}
	public boolean eliteVoidMelee(){
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 11676 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 19785 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 19786 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 8842){
			return true;
		}
		return false;
	}
	public boolean ava(){
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] == 10499) 
		{
			return true;
		}
		return false;
	}
	public int GanodermicReductionMage(int damage) {
		double damage2 = damage;
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 22482) {
			damage2 = damage*0.965;
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 22490) {
			damage2 = damage*0.83;
		}
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 22486 ) {
			damage2 = damage*0.94;
		}
		return (int)damage2;
	}
	public int GanodermicReductionMelee(int damage) {
		double damage2 = damage;
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 22482) {
			damage2 = damage*0.98;
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 22490) {
			damage2 = damage*0.925;
		}
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 22486 ) {
			damage2 = damage*0.975;
		}
		return (int)damage2;
	}
	public boolean voidRange(){
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 11675 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 8839 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 8840 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 8842){
			return true;
		}
		return false;
	}
	public boolean eliteVoidRange(){
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 11675 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 19785 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 19786 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 8842){
			return true;
		}
		return false;
	}
	
	public boolean voidMage(){
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 11674 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 8839 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 8840 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 8842){
			return true;
		}
		return false;
	}
	public boolean eliteVoidMage(){
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 11674 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 19785 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 19786 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 8842){
			return true;
		}
		return false;
	}
	public boolean fullAkrisaeEquipped(){
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 21736 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 21752 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 21760 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21744){
			return true;
		}
		return false;			
	}
	public boolean polyPore(){
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 22494){
			return true;
		}
		return false;			
	}
	public boolean armaStaff(){
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 21777){
			return true;
		}
		return false;			
	}
	
	public boolean fullGuthanEquipped(){
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 4724 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 4728 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 4730 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4726){
			return true;
		}
		return false;			
	}
	public boolean fullVeracEquipped(){
		if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 4753 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 4757 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 4759 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4755){
			return true;
		}
		return false;			
	}
	public boolean fullDharokEquipped(){
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] == 4716 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] == 4720 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] == 4722 && c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4718){
			return true;
		}
		return false;
	}	
	public void sendWeapon(){
		int Weapon = c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
		String WeaponName = null;
		if (Weapon == -1){
			WeaponName = "Unarmed";
		} else {
			ItemDefinition def = Server.getItemManager().getItemDefinition(
				Weapon);
			WeaponName = def.getName();
		}
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replace("Iron", "");
		WeaponName2 = WeaponName2.replace("Steel", "");
		WeaponName2 = WeaponName2.replace("Black", "");
		WeaponName2 = WeaponName2.replace("Mithril", "");
		WeaponName2 = WeaponName2.replace("Adamant", "");
		WeaponName2 = WeaponName2.replace("Rune", "");
		WeaponName2 = WeaponName2.replace("Granite", "");
		WeaponName2 = WeaponName2.replace("Dragon", "");
		WeaponName2 = WeaponName2.replace("Crystal", "");
		WeaponName2 = WeaponName2.replace("White", "");
		WeaponName2 = WeaponName2.trim();
		if (WeaponName.equals("Unarmed")){
			c.getActionAssistant().setSidebarInterface(0, 5855); //punch, kick, block
			c.getActionAssistant().sendFrame126(WeaponName, 5857);
		} else if (WeaponName.endsWith("whip")){
			c.getActionAssistant().setSidebarInterface(0, 12290); //flick, lash, deflect
			c.getActionAssistant().sendFrame246(12291, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 12293);
			c.barId = 12325;
		} else if (WeaponName.endsWith("Whip")){
			c.getActionAssistant().setSidebarInterface(0, 12290); //flick, lash, deflect
			c.getActionAssistant().sendFrame246(12291, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 12293);
			c.barId = 12325;
		} else if (WeaponName.endsWith("bow") || WeaponName.endsWith("10")|| WeaponName.endsWith("full") || WeaponName.startsWith("seercull") ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] ==15241 ){
			c.getActionAssistant().setSidebarInterface(0, 1764); //accurate, rapid, longrange
			c.getActionAssistant().sendFrame246(1765, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 1767);
			c.barId = 7551;
		} else if (WeaponName.startsWith("Staff") || WeaponName.endsWith("staff") || WeaponName.endsWith("wand") || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] ==13869 ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] ==18355  || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] ==18371){
			c.getActionAssistant().setSidebarInterface(0, 328); //spike, impale, smash, block
			c.getActionAssistant().sendFrame246(329, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 331);
		} else if (WeaponName2.startsWith("dart") || WeaponName2.startsWith("knife") || WeaponName2.startsWith("javelin") || WeaponName.equalsIgnoreCase("toktz-xil-ul")){
			c.getActionAssistant().setSidebarInterface(0, 4446); //accurate, rapid, longrange
			c.getActionAssistant().sendFrame246(4447, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 4449);
		} else if (WeaponName2.startsWith("dagger") || WeaponName2.startsWith("claws")){
			c.getActionAssistant().setSidebarInterface(0, 2276); //stab, lunge, slash, block
			c.getActionAssistant().sendFrame246(2277, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 2279);
			c.barId = 7576;
		} else if (WeaponName2.startsWith("pickaxe")){
			c.getActionAssistant().setSidebarInterface(0, 5570); //spike, impale, smash, block
			c.getActionAssistant().sendFrame246(5571, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 5573);
		} else if (WeaponName2.startsWith("axe") || WeaponName2.startsWith("battleaxe")){
			c.getActionAssistant().setSidebarInterface(0, 1698); //chop, hack, smash, block
			c.getActionAssistant().sendFrame246(1699, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 1701);
			c.barId = 7501;
		} else if (WeaponName2.startsWith("halberd")){
			c.getActionAssistant().setSidebarInterface(0, 8460); //jab, swipe, fend
			c.getActionAssistant().sendFrame246(8461, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 8463);
			c.barId = 8495;
		} else if (WeaponName2.startsWith("Scythe")){
			c.getActionAssistant().setSidebarInterface(0, 8460); //jab, swipe, fend
			c.getActionAssistant().sendFrame246(8461, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 8463);
		}else if ((WeaponName2.startsWith("spear")) || (WeaponName2.startsWith("Spear"))){
			c.getActionAssistant().setSidebarInterface(0, 4679); //lunge, swipe, pound, block
			c.getActionAssistant().sendFrame246(4680, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 4682);
			c.barId = 7501;
		} else {
			c.getActionAssistant().setSidebarInterface(0, 2423); //chop, slash, lunge, block
			c.getActionAssistant().sendFrame246(2424, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 2426);
			c.barId = 7601;
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5680 ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1231 ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1215 ||c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5698 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1305 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7158  || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4587 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11694){
			c.getActionAssistant().setSidebarInterface(0, 2276); //stab, lunge, slash, block
			c.getActionAssistant().sendFrame246(2277, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 2279);
			c.barId = 7576;
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4153){
			c.getActionAssistant().setSidebarInterface(0, 1698); //chop, hack, smash, block
			c.getActionAssistant().sendFrame246(1699, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 1701);
			c.barId = 7501;
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1249 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11716){ //spear
			c.getActionAssistant().setSidebarInterface(0, 1698); //chop, hack, smash, block
			c.getActionAssistant().sendFrame246(1699, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 1701);
			c.barId = 7501;
		}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1263){ //spear
			c.getActionAssistant().setSidebarInterface(0, 1698); //chop, hack, smash, block
			c.getActionAssistant().sendFrame246(1699, 200, Weapon);
			c.getActionAssistant().sendFrame126(WeaponName, 1701);
			c.barId = 7501;
		}
	}	

}