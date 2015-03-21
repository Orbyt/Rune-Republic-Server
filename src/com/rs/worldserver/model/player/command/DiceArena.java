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
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.Misc;
public class DiceArena implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			return;
		}
	
		if (c.inWild()) {
			c.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}	
		if (Server.DICE) {
			c.getActionAssistant().Send("@red@WARNING, DON'T GET TOO ADDICTED! GL!");
			c.getActionAssistant().startTeleport(2037, 4535, 0, "modern");
		} else {
			c.getActionAssistant().Send("No Dice Event is currently in progress!");
		}
	}

}
