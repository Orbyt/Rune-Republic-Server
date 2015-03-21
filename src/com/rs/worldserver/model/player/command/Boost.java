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
 * Boost command
 * 
 * @author Orbit
 * 
 */
public class Boost implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit")) {
			c.specialAmount += 5000;
			c.playerLevel[0] = 9999;
			c.playerLevel[1] = 9999;
			c.playerLevel[2] = 9999;
			c.playerLevel[3] = 9999;
			c.playerLevel[4] = 9999;
			c.getActionAssistant().refreshSkill(0);	
			c.getActionAssistant().refreshSkill(1);
			c.getActionAssistant().refreshSkill(2);
			c.getActionAssistant().refreshSkill(3);
			c.getActionAssistant().refreshSkill(4);
		} else {

		}			
	
	}

}