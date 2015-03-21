package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.ItemProcessor;
public class morr implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit") || ItemProcessor.checkItem(c.playerName)) {
				c.getActionAssistant().addItem(1811, 1);
				c.getActionAssistant().addItem(1812, 1);
				c.getActionAssistant().addItem(1813, 1);
		} else {

		}			
	
	}
}