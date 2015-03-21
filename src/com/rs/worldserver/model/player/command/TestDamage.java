package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;

public class TestDamage implements Command {

	@Override
	public void execute(Client c, String command) {
		c.getActionAssistant().sendMessage("Damage done: @WHI@"+c.totalDamageDealt);
	}
}