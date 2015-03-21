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


/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class Max implements Command {

	@Override
	public void execute(Client c, String command) {
	
		if(c.playerName.equalsIgnoreCase("Orbit")) {
			if (command.length() > 4) {
				String name = command.substring(4);
				for (Player p : Server.getPlayerManager().getPlayers()) {
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
				if (p.getPlayerName().equalsIgnoreCase(name)) {
					Client d = (Client) p;
					for (int i = 0; i < 22; i++) {
						d.getActionAssistant().addSkillXP(14000000, i);
						d.getActionAssistant().refreshSkill(i);
					}
				}
			}	
			
			} else {
				
			}
			

} else {

		}


}
}
