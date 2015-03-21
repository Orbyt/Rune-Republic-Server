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
import com.rs.worldserver.model.player.*;
/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class Sw implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.modDay || c.modDays || c.playerName.equalsIgnoreCase("Orbit")) {
				if(c.mage >= 1) {
					c.getActionAssistant().sendMessage("You switch to modern magic.");
					c.actionAssistant.setSidebarInterface(6, 1151);
					c.mage = 0;
				} else {
					c.getActionAssistant().sendMessage("You switch to ancient magic.");
					c.actionAssistant.setSidebarInterface(6, 12855);
					c.mage = 1;
				}	
		} else {
			c.getActionAssistant().sendMessage("You do not have the correct rights to use this.");
		}
	}

}
