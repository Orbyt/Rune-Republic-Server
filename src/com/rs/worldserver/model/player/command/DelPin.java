package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.util.CheatProcessor;
import com.rs.worldserver.util.BankProcessor;


public class DelPin implements Command {
	public String temppin = null;
	@Override
	public void execute(Client c, String command) {
	if(c.playerRights > 3) {
	String name = command.substring(7);
			for (Player p : Server.getPlayerManager().getPlayers()) {
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
				if (p.getPlayerName().equalsIgnoreCase(name)) {
					    Client d = (Client) p;
						d.getActionAssistant().sendMessage("Your bank pin has been reset by" + c.playerName);
						d.bankPinSet = 0;
						d.bankpin = "";
						c.getActionAssistant().sendMessage("@red@You reset "+p.playerName +" bank pin");		
				}					
			}
		}
	}

}