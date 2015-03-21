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
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.Config;
/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class StarterPack implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.starter == 0){
			c.getActionAssistant().clearQuestInterface();
			c.getActionAssistant().sendFrame126("Combat Kit", 2494);
			c.getActionAssistant().sendFrame126("ol", 2495);
			c.getActionAssistant().sendFrame126("Skilling Kit", 2496);
			c.getActionAssistant().sendFrame126("", 2497);
			c.getActionAssistant().sendFrame126("", 2498);
			c.getActionAssistant().sendQuestSomething(8143);
			c.getActionAssistant().sendFrame164(2492);
			c.flushOutStream();
		}
	}

}