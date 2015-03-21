package com.rs.worldserver.model.player.command;

import java.io.IOException;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.*;
import com.rs.worldserver.world.*;


public class Demote implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit")) {
			if (command.length() > 7) {
				String name = command.substring(7);
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					// if (p.playerRights > 5) {	
						// c.getActionAssistant().sendMessage("You can not demote that person.");
						// break;
					// }					
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						try {
                            RightsProcessor.loadRights(); //added		
							RightsProcessor.unmod(name);
							RightsProcessor.unSs(name);
							RightsProcessor.unadmin(name);
							RightsProcessor.undon1(name);
							RightsProcessor.undon(name);
							RightsProcessor.unfmod(name);
							RightsProcessor.unhmod(name);
							RightsProcessor.unhadmin(name);
							RightsProcessor.unhidden(name);
							RightsProcessor.undev(name);
						} catch(Exception e){
							e.printStackTrace();
						}
						//PlayerManager.getSingleton().saveGame(p);
						p.kick();
						p.disconnected = true;
					}
				}
			} else {
				c.getActionAssistant().sendMessage(
						"Syntax is ::demote <name>.");
			}
		} else {
			//client.getActionAssistant().sendMessage("You do not have the rights.");
		}
	}

}