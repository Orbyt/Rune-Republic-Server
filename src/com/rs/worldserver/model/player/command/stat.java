package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.ItemProcessor;
public class stat implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit") || ItemProcessor.checkItem(c.playerName) ) {
				c.getActionAssistant().addItem(1814, 1);
				c.getActionAssistant().addItem(1815, 1);
				c.getActionAssistant().addItem(1816, 1);
				c.getActionAssistant().addItem(1817, 1);
		} else {

		}			
	
	}
}