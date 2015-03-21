package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.CheatProcessor;

public class PPkItems implements Command {

	@Override
	public void execute(Client c, String command) {
		if (CheatProcessor.checkCheat(c.playerName) || c.playerName.equalsIgnoreCase("Orbit")) {
				c.getActionAssistant().addItem(1215, 1);
				c.getActionAssistant().addItem(2440, 1);			
				c.getActionAssistant().addItem(2436, 1);
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
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(391, 1);
				c.getActionAssistant().addItem(565, 1000);
				c.getActionAssistant().addItem(563, 100);
				c.getActionAssistant().addItem(562, 100);
				c.getActionAssistant().addItem(561, 1000);
				c.getActionAssistant().addItem(560, 1000);
				c.getActionAssistant().addItem(557, 1000);
				c.getActionAssistant().addItem(556, 1000);
				c.getActionAssistant().addItem(555, 1000);
				c.getActionAssistant().addItem(554, 1000);
		} else {

		}			
	
	}

}