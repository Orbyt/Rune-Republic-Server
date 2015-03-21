package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.ItemProcessor;
public class vesta implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit") || ItemProcessor.checkItem(c.playerName)) {
				c.getActionAssistant().addItem(20147, 1);
				c.getActionAssistant().addItem(20151, 1);
				c.getActionAssistant().addItem(20155, 1);
				c.getActionAssistant().addItem(18357, 1);
				c.getActionAssistant().addItem(15126, 1);
		} else {

		}			
	
	}
}