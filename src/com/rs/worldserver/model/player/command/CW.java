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

public class CW implements Command {

	@Override
	public void execute(Client c, String command) {
		
			try {
				if (Config.CastleWars) {
					c.fishing = false;
					c.walked = true;
					c.stopMovement();
					c.getActionAssistant().startTeleport(2439, 3085, 0, "modern");
				} else {
					c.getActionAssistant().sendMessage("You can't use that on this world!"); 
				}
			} catch(Exception e) { 
				c.getActionAssistant().sendMessage("Wrong Syntax!"); 
				}
			return;
		}	
}