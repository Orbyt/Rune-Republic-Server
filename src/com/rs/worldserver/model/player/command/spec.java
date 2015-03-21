package com.rs.worldserver.model.player.command;

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
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.*;

/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class spec implements Command {

	@Override
	public void execute(Client client, String command) {
	double factor = 1;
							if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 19669) {
								 factor = 0.9;
							}
					if(client.duelRule[10] && client.duelStatus >= 2) 
						{ //markerduel
							client.getActionAssistant().sendMessage("Special attacks have been disabled during this duel!");
							return;
						}	
					if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 15486 && (client.specialAmount >= ((int)(100)*factor))) {
						client.getActionAssistant().startAnimation(12804);
						client.gfx0(2319);
						client.SOLspec = System.currentTimeMillis();
						client.specOn = false;
						client.specialAmount -= 100;
						client.actionAssistant.sendMessage("You feel an aura of protection surrounding you");
						return;
					}
					else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 15486 && (client.specialAmount < ((int)(100)*factor))) {
						client.actionAssistant.sendMessage("You do not have enough special power "+client.specialAmount+"%");
						client.specOn = false;
						return;
					}
	/*	if (c.inDuelArena()) {
			c.getActionAssistant().Send("Don't Cheat Asshole");
			return;
		}
		//if(c.playerName.equalsIgnoreCase("Orbit")) {
		if ((c.playerRights > 4) && !(c.inWild())) {
			c.specialAmount = 100;
		}*/
	}

}
