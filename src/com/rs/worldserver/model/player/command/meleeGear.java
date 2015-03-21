package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.ItemProcessor;

public class meleeGear implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
		   c.getActionAssistant().addItem(2450, 1);//zam brew
				c.getActionAssistant().addItem(3024, 1);//super rest
				c.getActionAssistant().addItem(3024, 1);//super rest
				c.getActionAssistant().addItem(3024, 1);//super rest
				c.getActionAssistant().addItem(19784, 1);//korasi
				c.getActionAssistant().addItem(6685, 1);//sara brew
				c.getActionAssistant().addItem(6685, 1);//sara brew
				c.getActionAssistant().addItem(6685, 1);//sara brew
				c.getActionAssistant().addItem(9075, 1000);//astral
				c.getActionAssistant().addItem(560, 1000);//death
				c.getActionAssistant().addItem(557, 1000);//earths
				c.getActionAssistant().addItem(20139, 1);//torva body
				c.getActionAssistant().addItem(13655, 1);//d kite
				c.getActionAssistant().addItem(7901, 1);//lime
				c.getActionAssistant().addItem(15220, 1);//zerker ring (i)
				c.getActionAssistant().addItem(20143, 1);//torva legs
				c.getActionAssistant().addItem(19748, 1);//ardy cloak
				c.getActionAssistant().addItem(11732, 1);//dragon boots
				c.getActionAssistant().addItem(7462, 1);//Barrows Gloves
				c.getActionAssistant().addItem(6585, 1);//Fury
				c.getActionAssistant().addItem(20135, 1);//torva Helm
		} else {
		}				
	}
}
