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
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class Lvl implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			String[] args = command.split(" ");
			if (args.length == 4) {
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(args[1])) {
						Client d = (Client) p;
						if (Integer.parseInt(args[3]) >= 999999) {
							c.getActionAssistant().sendMessage("More then 99998 will crash the server.");
							return;
						}
						if(p.duelStatus > 4){
							c.getActionAssistant().Send("@red@This player is in a duel!");
							return;
						}
						//if (c.playerName.equalsIgnoreCase(args[1]) != (c.playerName.equalsIgnoreCase("Orbit")) {
						//	c.getActionAssistant().sendMessage("This can't be used to give stats to other players");
						//	return;
						//	}

						d.playerLevel[Integer.parseInt(args[2])] = 1;
						d.playerXP[Integer.parseInt(args[2])] = 0;
						d.playerLevel[Integer.parseInt(args[2])] = Integer.parseInt(args[3]);
						d.playerXP[Integer.parseInt(args[2])] = d.getActionAssistant().getXPForLevel(Integer.parseInt(args[3]));
						d.updateRequired = true;
						d.getActionAssistant().sendFrame126(""+d.playerLevel[Integer.parseInt(args[2])]+"", 4012);
						d.appearanceUpdateRequired = true;
						d.getActionAssistant().refreshSkill(Integer.parseInt(args[2]));
						d.getActionAssistant().requestUpdates();
						
					}
				}
			}
		}
	}
}
