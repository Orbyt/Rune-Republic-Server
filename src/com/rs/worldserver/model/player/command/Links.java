package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;

public class Links implements Command {

	@Override
	public void execute(Client c, String command) {
		c.getActionAssistant().sendMessage("@red@Important Rune Republic Help Links!");
		c.getActionAssistant().sendMessage("@red@Forums@blk@: +www.runerepublic.com#url#");
//		c.getActionAssistant().sendMessage("@red@Guides@blk@: +www.runerepublic.com#url#");
//		c.getActionAssistant().sendMessage("@red@Vote4cash@blk@: +#url#");
//		c.getActionAssistant().sendMessage("@red@Appeals@blk@: +#url#");
//		c.getActionAssistant().sendMessage("@red@Bug Report@blk@: +#url#");
//		c.getActionAssistant().sendMessage("@red@Password Support@blk@: +#url#");
	}
}