package com.rs.worldserver.model.player;

import com.rs.worldserver.*;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.*;
import com.rs.worldserver.model.player.RegionManager.Region.Tile;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.model.object.GameObject.Face;
public class Webs {

	public static void slashWeb(final Client player, final int x, final int y, final int h, final int type) {
		final GameObject web = Server.getObjectManager().getObjectAt(x, y, h);
		if(x == 3093 && y == 3957){
			if((player.getX() != 3092 && player.getY() != 3957) || (player.getX() != 3094 && player.getY() != 3957))
				return;
		}
		if(x == 3095 && y == 3957){
			if((player.getX() != 3095 && player.getY() != 3957) || (player.getX() != 3094 && player.getY() != 3957))
				return;
		}
		if (player.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] != -1) {
			boolean usingBow = false;
			boolean usingOtherRangeWeapons =  false;
			for (int bowId : player.BOWS) {
				if(player.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == bowId) {
					 usingBow = true;
				}
			}				
			for (int otherRangeId : player.OTHER_RANGE_WEAPONS) {
				if(player.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}		
			if(usingBow || 	usingOtherRangeWeapons){
				player.getActionAssistant().sendMessage("You need a melee weapon to slash the web.");
				return;	
			}
	
			if (Server.getObjectManager().getObjectAt(x, y,h) != null) {
				player.getActionAssistant().turnTo(x, y);
				player.getActionAssistant().startAnimation(player.getCombatEmotes().getWepAnim());
					if (Misc.random(1) == 1) {
						WebEvent.startCut(player,x,y,h);
						Server.getObjectManager().update(player);
					} else {
						player.getActionAssistant().sendMessage("You fail to slash the webs.");
					}	
			}
		}
	}
}