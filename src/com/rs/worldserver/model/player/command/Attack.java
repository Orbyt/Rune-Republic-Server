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
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class Attack implements Command {

	@Override
	public void execute(Client c, String command) {
	if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] > -1 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] > -1 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > -1 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] > -1 || 
				c.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] > -1 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] > -1 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_FEET] > -1) {
				c.getActionAssistant().Send("You can not be wearing any items when doing this command!");
				return;
			}
		if(c.inWild()) {
			c.getActionAssistant().Send("This can not be done in the wild!");
			return;
		}
		if(c.inCombat) {
			c.getActionAssistant().Send("This can not be done in combat!");
			return;
		}
		if(c.isFighting) {
			c.getActionAssistant().Send("This can not be done in combat!");
			return;
		}
		String cmd[] = command.split(" ");
		if (Integer.valueOf(cmd[1]) == null) {
			c.getActionAssistant().Send("Use as ::resetattack level ie: (::resetattack 45)");
			return;
		}	
			
		if (c.getLevelForXP(c.playerXP[0]) >= Integer.valueOf(cmd[1])) {
			c.playerXP[0] = c.getActionAssistant().getXPForLevel(Integer.valueOf(cmd[1])) +1;
			c.playerLevel[0] = Integer.valueOf(cmd[1]);
			c.updateRequired = true;
			c.getActionAssistant().sendFrame126(""+c.playerLevel[1]+"", 4012);
			c.appearanceUpdateRequired = true;
			c.getActionAssistant().Send("Attack Lowered!");
			c.getActionAssistant().refreshSkill(0);
			c.getActionAssistant().requestUpdates();
			return;
		}
		else {
		c.getActionAssistant().Send("Your level is not high enough to do that");
		
		}		
			
		}
	}


