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

/**
 * My position command
 * 
 * @author Graham
 * 
 */
public class MyPosition implements Command {

	@Override
	public void execute(Client client, String command) {
		client.getActionAssistant().sendMessage(
				"You are at " + client.getAbsX() + ", " + client.getAbsY()
						+ " (height: " + client.getHeightLevel() + ")");
	}

}
