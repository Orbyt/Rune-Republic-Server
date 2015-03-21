package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class Pray implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.inWild()) {
			c.getActionAssistant().Send("This can not be done in the wild!");
			return;
		}
				c.playerLevel[5] = 1;
				c.playerXP[5] = 0;
				c.updateRequired = true;
				c.getActionAssistant().sendFrame126(""+c.playerLevel[5]+"", 4012);
				c.appearanceUpdateRequired = true;
				c.getActionAssistant().Send("Prayer Lowered To 1!");
				c.getActionAssistant().refreshSkill(5);
				c.getActionAssistant().requestUpdates();
		}
	}