package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.player.*;

public class Tele implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			c.getActionAssistant().Send("Can't be used in duel arena");
			return;
		}
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			String[] args = command.split(" ");
			try{
					c.fishing = false;
					c.walked = true;
					c.teleportToX = Integer.parseInt(args[1]);
					c.teleportToY = Integer.parseInt(args[2]);
					c.heightLevel = Integer.parseInt(args[3]);
				} catch(Exception e) { 
					c.fishing = false;
					c.walked = true;
					c.teleportToX = Integer.parseInt(args[1]);
					c.teleportToY = Integer.parseInt(args[2]);
					c.heightLevel = 0;
				}
			return;
		}	
		if (c.playerRights > 4 && !c.inWild()) {
			String[] args = command.split(" ");
			try{
					c.fishing = false;
					c.walked = true;
					c.teleportToX = Integer.parseInt(args[1]);
					c.teleportToY = Integer.parseInt(args[2]);
					c.heightLevel = Integer.parseInt(args[3]);
				} catch(Exception e) { 
					c.fishing = false;
					c.walked = true;
					c.teleportToX = Integer.parseInt(args[1]);
					c.teleportToY = Integer.parseInt(args[2]);
					c.heightLevel = 0;
				}
		} else {
		
		}
	}
}