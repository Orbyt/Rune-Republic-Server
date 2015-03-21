package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;

public class Tile2 implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			int absx = c.absX;
			int absy = c.absY;
			c.appendToAutoTile(absx, absy);
		} else {

		}			
	
	}

}
