package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.ItemProcessor;
public class voi implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit") || ItemProcessor.checkItem(c.playerName) ) {
				c.getActionAssistant().addItem(1797, 1);
				c.getActionAssistant().addItem(1798, 1);			
				c.getActionAssistant().addItem(1799, 1);
				c.getActionAssistant().addItem(1800, 1);
				c.getActionAssistant().addItem(1818, 1);
				c.getActionAssistant().addItem(1801, 1);
		} else {

		}			
	
	}
}