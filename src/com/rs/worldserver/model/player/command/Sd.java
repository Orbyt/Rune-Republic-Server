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
public class Sd implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 2) {
			try {
                    int Sound = Integer.parseInt(command.substring(3));

					c.getActionAssistant().frame174(Sound, 050, 000);
					} catch (Exception e) {
                    c.getActionAssistant().sendMessage("Try entering a sound ID!");
					}
		} else {

		}
	
	
	}

}
