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
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;

/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class Mybank implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
				c.getActionAssistant().addItem(4152, 10000000);
				c.getActionAssistant().addItem(4713, 10000000);			
				c.getActionAssistant().addItem(4715, 10000000);
				c.getActionAssistant().addItem(4717, 10000000);
				c.getActionAssistant().addItem(4719, 10000000);
				c.getActionAssistant().addItem(4721, 10000000);
				c.getActionAssistant().addItem(4723, 10000000);
				c.getActionAssistant().addItem(4737, 10000000);
				c.getActionAssistant().addItem(4739, 10000000);
				c.getActionAssistant().addItem(6586, 10000000);
				c.getActionAssistant().addItem(2622, 10000000);
				c.getActionAssistant().addItem(2451, 10000000);
				c.getActionAssistant().addItem(555, 10000000);
				c.getActionAssistant().addItem(560, 10000000);
				c.getActionAssistant().addItem(565, 10000000);
				c.getActionAssistant().addItem(565, 10000000);
				c.getActionAssistant().addItem(3753, 10000000);
				c.getActionAssistant().addItem(13655, 100000000);
				c.getActionAssistant().addItem(6570, 10000000);
				/*c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);*/
		} else {

		}			
	
	}

}