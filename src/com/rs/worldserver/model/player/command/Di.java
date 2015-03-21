package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.Config;
/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class Di implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			return;
		}
		if (c.inWild()) {
			c.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}		
		if (System.currentTimeMillis() - c.teleDelay < 4000) {
			return;
		}
		if(c.playerRights > 0 || c.modDay || c.modDays) {
			c.getActionAssistant().startTeleport(2337, 9799, 0, "modern");
			c.teleDelay = System.currentTimeMillis();
		} else {
			c.getActionAssistant().Send("You need to be a donator to a use this command.");
		}
	}

}