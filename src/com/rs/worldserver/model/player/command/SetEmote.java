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

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.Config;

/**
 * Set emote command
 * 
 * @author Ventrillo
 * DONT USE, command.substring(6) is probably wrong, should be command.substring(9)
 */
public class SetEmote implements Command {

	@Override
	public void execute(Client c, String command) {
	if((c.playerName.equalsIgnoreCase("Orbit") || Config.WORLD_NUMBER ==5)) {
		if (command.length() > 6) {
			int emote = Integer.valueOf(command.substring(6));
				c.getActionAssistant().startAnimation(emote);
				c.getActionAssistant().sendMessage(
						"id: "+emote);
			} else {
				c.getActionAssistant().sendMessage(
						"You do not have enough rights to do this.");
			}
		} else {
			
		}
	}
}
