package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
public class PkItems implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
				c.getActionAssistant().addItem(5698, 1);
				c.getActionAssistant().addItem(2450, 1);			
				c.getActionAssistant().addItem(4720, 1);
				c.getActionAssistant().addItem(4722, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(555, 1000);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(560, 1000);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(565, 1000);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
		} else {

		}			
	
	}

}
