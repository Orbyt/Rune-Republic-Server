package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.ItemProcessor;

public class mageGear implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {	
				c.getActionAssistant().addItem(13899, 1);// VLS
				c.getActionAssistant().addItem(565, 1200);// BLods
				c.getActionAssistant().addItem(20151, 1);// Pernix  Top
				c.getActionAssistant().addItem(20155, 1);// Pernix Skirt
				c.getActionAssistant().addItem(2450, 1);// Zammy Brew
				c.getActionAssistant().addItem(560, 2400);// death
				c.getActionAssistant().addItem(9185, 1);// Rune cbow				
				c.getActionAssistant().addItem(20147, 1);// Pernix helm	
				c.getActionAssistant().addItem(3024, 1);//super rest
				c.getActionAssistant().addItem(555, 3600);//waters
				c.getActionAssistant().addItem(6685, 1);// sara brew
				c.getActionAssistant().addItem(6685, 1);//sara brew	
				c.getActionAssistant().addItem(10828, 1);// Nei
				c.getActionAssistant().addItem(19748, 1);//ardy cloak
				c.getActionAssistant().addItem(13738, 1);// arcane
				c.getActionAssistant().addItem(6585, 1);//fury
				c.getActionAssistant().addItem(7871, 500);//drag bolts
				c.getActionAssistant().addItem(15486, 1);//staff of light			
				c.getActionAssistant().addItem(7901, 1);//Lime
				c.getActionAssistant().addItem(20167, 1);//virtis botom
				c.getActionAssistant().addItem(20163, 1);//virus top
				c.getActionAssistant().addItem(20159, 1);//virus helm
				c.getActionAssistant().addItem(7462, 1);//B gloves
				c.getActionAssistant().addItem(6920, 1);//inf boots
				c.getActionAssistant().addItem(2550, 1);//recoil
							
		} else {
		}				
	}
}
