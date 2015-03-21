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
public class ffa implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.inDuelArena()) {
			return;
		}
	
		if (c.inWild()) {
			c.getActionAssistant().Send("You cannot use this command in the wilderness.");
			return;
		}	
		if (Server.FFA) {
		int teleroom = Misc.random(2);
		//c.getActionAssistant().startTeleport(3297, 9824, 0, "modern");
			c.getActionAssistant().Send("@red@WARNING, ONCE YOU ENTER THE FFA AREA YOU CANNOT LEAVE");
			c.getActionAssistant().Send("@red@WARNING, ONCE THE FFA BEGINS YOU CANNOT RETURN FOR VOID/FIRE CAPES");
				if (teleroom == 0) {
					c.getActionAssistant().startTeleport(3285, 9836, 0, "modern");
					c.getActionAssistant().Send("You have entered waiting room alpha. Enter the portal when the event begins");
				}
				else if (teleroom == 1) {
				//c.getActionAssistant().startTeleport(3314, 9807, 0, "modern");
				c.getActionAssistant().startTeleport(3285, 9836, 0, "modern");
				c.getActionAssistant().Send("You have entered waiting room beta. Enter the portal when the event begins");
				}
				else if (teleroom == 2) {
				c.getActionAssistant().startTeleport(3278, 9806, 0, "modern");
				c.getActionAssistant().Send("You have entered waiting room gamma. Enter the portal when the event begins");
				}
		}
			else {
				c.getActionAssistant().Send("No FFA is currently in progress!");
				}
		}

}
