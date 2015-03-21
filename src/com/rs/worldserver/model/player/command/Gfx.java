package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.model.player.*;

public class Gfx implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			int gfx = Integer.valueOf(command.substring(4));
			
			try {
				if(gfx < 10000) {
					Server.getStillGraphicsManager().stillGraphics(c,
					c.getAbsX() + 1, c.getAbsY(), 0, gfx, 0);
				} else {
					c.getActionAssistant().sendMessage("GFX value too high");
				}
			} catch (Exception e) {
				System.out.println("GFX value to low");
			}
		}
	}
}

