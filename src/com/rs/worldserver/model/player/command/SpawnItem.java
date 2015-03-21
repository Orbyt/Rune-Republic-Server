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
 * Spawn Item command
 * 
 * @author Orbit
 * 
 */
public class SpawnItem implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit")) {
			String idAndAmount[] = command.split(" ");
			if (Integer.valueOf(idAndAmount[1]) == null) {
				return;
			}
			int itemName = Integer.valueOf(idAndAmount[1]);
			int itemAmount = 1;
			if (idAndAmount.length > 2) {
			if (Integer.valueOf(idAndAmount[2]) != null) {
			itemAmount = Integer.valueOf(idAndAmount[2]);
				}
			}
			c.getActionAssistant().addItem(itemName, itemAmount);
		} else {
			c.getActionAssistant().sendMessage("Wrong syntax. Syntax is (::command <itemname> <itemnumber>)");
		}
	} 
}