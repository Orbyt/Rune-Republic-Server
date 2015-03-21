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
import com.rs.worldserver.util.CheatProcessor;


/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class CWReset implements Command {

	@Override
	public void execute(Client c, String command) {
	
		if(Server.getCastleWars().isInCw(c)){	
			if(c.playerEquipment[3] == 4037 || c.playerEquipment[3] ==  4039 || c.inCombat){
				c.getActionAssistant().sendMessage("@red@You can not use the unstuck command!");
				return;
			}
			if(Server.getCastleWars().isSaraTeam(c)) {
				c.getActionAssistant().movePlayer(2426, 3076,1); //sara
				c.teleDelay = 5000;
				c.getActionAssistant().sendMessage("@red@ You have been unstuck!");
			} else {
				c.getActionAssistant().movePlayer(2372,3131,1);
				c.teleDelay = 5000;
				c.getActionAssistant().sendMessage("@red@ You have been unstuck!");
			}
		} else {
			c.getActionAssistant().sendMessage("@red@ You are not in castlewars!");
		}
		
	}

}
