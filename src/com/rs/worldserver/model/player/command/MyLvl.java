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
import com.rs.worldserver.util.CheatProcessor;

/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class MyLvl implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 4) {

			String[] args = command.split(" ");
			try {
			if (Integer.parseInt(args[2]) > 0 && Integer.parseInt(args[2]) < 100 && !c.inDuelArena()){
				c.playerLevel[Integer.parseInt(args[1])] = 1;
				c.playerXP[Integer.parseInt(args[1])] = 0;
				c.playerLevel[Integer.parseInt(args[1])] = Integer.parseInt(args[2]);
				c.playerXP[Integer.parseInt(args[1])] = c.getActionAssistant().getXPForLevel(Integer.parseInt(args[2]));
				c.updateRequired = true;
				c.getActionAssistant().sendFrame126(""+c.playerLevel[Integer.parseInt(args[1])]+"", 4012);
				c.appearanceUpdateRequired = true;
				c.getActionAssistant().refreshSkill(Integer.parseInt(args[1]));
				c.getActionAssistant().requestUpdates();
			}
			} catch (NumberFormatException nfe){
				c.getActionAssistant().sendMessage("Invalid");
			}
		}
	}
}