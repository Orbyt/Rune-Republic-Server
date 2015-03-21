package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;

public class back implements Command {

	@Override
	public void execute(Client c, String command) {
		if((c.stuckX != 500 ) && (!c.inWild()) && (c.absX < 501)) {
				c.getActionAssistant().startTeleport(c.stuckX, c.stuckY, c.stuckHeight, "modern");
				c.getActionAssistant().Send("@red@You have returned to your last position");
				c.stuckX = 500;
		} else {

		}			
	
	}
}