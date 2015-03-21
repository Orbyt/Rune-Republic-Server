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

public class unMute implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 2 && (c.playerRights != 12) && c.playerRights != 13 && c.playerRights != 5 || CheatProcessor.checkCheat(c.playerName) ) {
		if (command.length() > 7) {
				String name = command.substring(7);
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						try {
							Client d = (Client) p;
							d.Muted = 0;
							PlayerManager.getSingleton().saveGame(d);
							d.getActionAssistant().Send("You have been unmuted.");
							c.getActionAssistant().Send("You have unmuted "+d.playerName+"");
						} catch(Exception e){
							e.printStackTrace();
						}
						
						
					}
				}
		}
	}
}
}