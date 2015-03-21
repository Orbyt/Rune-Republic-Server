package com.rs.worldserver.model.player.command;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;

public class Inter implements Command {

	@Override
	public void execute(Client c, String command) {
	if(c.playerName.equalsIgnoreCase("Orbit")) {
		if (command.length() > 6) {
			int id = Integer.valueOf(command.substring(6));
				c.getActionAssistant().showInterface(id);
				c.getActionAssistant().sendMessage("Testing Interface: ["+id+"].");
			} else {
				c.getActionAssistant().sendMessage("You do not have enough rights to do this.");
			}
		} else {
			
		}
	}
}