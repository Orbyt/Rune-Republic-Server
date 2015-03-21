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

public class tj implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 3) {
			if(c.respawnTimer > 3) {
				return;
			}
			if (c.inDuelArena()) {
				c.getActionAssistant().Send("no cheating");
				return;
			}
			if (c.inCWar()) {
				c.getActionAssistant().Send("no cheating");
				return;
			}
			if (c.inCombat) {
			c.getActionAssistant().sendMessage("You can not use this in combat!");
				return;
			}
			if (c.inWild() && c.playerRights <= 4) {
				c.getActionAssistant().sendMessage("You can not use this in the wild!");
				return;
			}
			try{
					c.fishing = false;
					c.walked = true;
					c.teleportToX = 2100;
					c.teleportToY = 4427;
					if(c.inWild()){
					String prefix = "[@red@Server Notice@bla@]";
String mess = "" + c.playerName + " has teleported from the wilderness.";
				
				PlayerManager.getSingleton().sendGlobalMessage(
					prefix + ":@bla@ "
							+ mess);
						}
				} catch(Exception e) { 
					c.getActionAssistant().sendMessage("Wrong Syntax! Use as ::tj"); 
				}
		} else {
			c.getActionAssistant().sendMessage("You do not have the rights to use this command.");
		}
	}
}