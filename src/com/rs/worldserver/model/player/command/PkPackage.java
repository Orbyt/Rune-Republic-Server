package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.SkillConstants;
//Don't use, code checks for wrong command length.
public class PkPackage implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			if (command.length() > 5) {
					String name = command.substring(5);
					for (Player p : Server.getPlayerManager().getPlayers()) {
						if (p == null)
							continue;
						if (!p.isActive || p.disconnected)
							continue;
						if (p.getPlayerName().equalsIgnoreCase(name)) {
							Client d = (Client) p;
							d.getActionAssistant().addItem(392,1000);
							
						}
					}
			}		
	
		}
	}
}