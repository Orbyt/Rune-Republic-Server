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
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.Config;
import com.rs.worldserver.util.Misc;

/**
 * Tele to home command
 * 
 * Location differs upon players faction
 * 
 * @author Orbit
 * 
 */
public class Home implements Command {

	@Override
	public void execute(Client client, String command) {
		if (System.currentTimeMillis() - client.teleDelay < 3000) {
			return;
		}
		if(Server.getCastleWars().isInCwWait(client)){
			return;
		}
         client.getActionAssistant().startTeleport(Config.HOME_X, Config.HOME_Y, 0, "modern");
         client.teleDelay = System.currentTimeMillis();
        }
        
		
	}


