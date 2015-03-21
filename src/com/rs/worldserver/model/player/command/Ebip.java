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
import com.rs.worldserver.util.CheatProcessor;


/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class Ebip implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			c.getActionAssistant().Send("Don't Cheat Asshole");
			return;
		}
		if (c.getPlayerName().equalsIgnoreCase("Orbit")) {
			if(c.easyipban == 0 ) {
				c.easyipban = 1;
				c.getActionAssistant().sendMessage("Easy IP Ban Activated");
				//c.getActionAssistant().showOption(4, 0,"Trade With", 2);
				//c.getActionAssistant().showOption(5, 0,"Follow", 3);
				c.getActionAssistant().showOption(4, 0,"IP Mute", 4);	
				//c.getActionAssistant().showOption(5, 0,"Jail", 5);	
				c.getActionAssistant().showOption(5, 0,"IP Ban", 5);
			} else {
				c.easyipban = 0;
				c.getActionAssistant().sendMessage("Easy IP Ban Deactivated");
				c.getActionAssistant().showOption(4, 0,"Trade With", 3);
				c.getActionAssistant().showOption(5, 0,"Follow", 4);
			}
		}
	}
}

