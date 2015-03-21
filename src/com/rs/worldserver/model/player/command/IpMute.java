package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.CheatProcessor;
import com.rs.worldserver.util.BanProcessor;


public class IpMute implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			if (command.length() > 7) {
					String name = command.substring(7);
					for (Player p : Server.getPlayerManager().getPlayers()) {
						if (p == null)
							continue;
						if (!p.isActive || p.disconnected)
							continue;
						if (p.getPlayerName().equalsIgnoreCase(name)) {
							if(p.playerRights > 2) {
								c.getActionAssistant().sendMessage("You can not mute - "+p.playerName);
								return;
							}
							try {
								Client d = (Client) p;
								c.writeLog(p.playerName.toLowerCase(), "mutes2");
								BanProcessor.muteUser(Integer.toString(p.playerMac).toLowerCase(), c.playerName.toLowerCase());
								d.Muted = 2;
								PlayerManager.getSingleton().saveGame(d);
								d.getActionAssistant().Send("You have been muted.");
								c.getActionAssistant().Send("You have muted "+d.playerName+"");
								
							} catch(Exception e){
								e.printStackTrace();
							}
							
							
						}
					}
			}
	}
}
}