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

public class Mute implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 2 && (c.playerRights != 12 ) && c.playerRights != 13 && c.playerRights != 5 || CheatProcessor.checkCheat(c.playerName) ) {
		if (command.length() > 5) {
				String name = command.substring(5);
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
							//c.writeLog(p.connectedFrom, "mutes2");
							if(d.playerRights > 2) {
								c.getActionAssistant().Send("@red@You can't mute mods.");
								break;
							}
							d.Muted = 1;
							PlayerManager.getSingleton().saveGame(d);
							d.mutetimer = 10800;
							d.getActionAssistant().Send("You have been muted by " +c.playerName);
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