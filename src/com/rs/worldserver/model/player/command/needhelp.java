package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.HelpManager;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.ItemProcessor;
public class needhelp implements Command {

	@Override
	public void execute(Client c, String command) {
	if(System.currentTimeMillis() - c.lastUsed  > 300000){
		if (command.length() > 9) {
			String reason = command.substring(9);
			HelpManager.addPlayer(c.playerName);	
			c.getActionAssistant().sendMessage("@blu@Someone will be with you shortly.");
			c.lastUsed = System.currentTimeMillis();
			for (Player p : Server.getPlayerManager().getPlayers()) {
				if(p != null) {
				Client person = (Client)p;
					if( person.playerRights > 3){
						if(person.helpOn){
							person.getActionAssistant().sendMessage("@blu@ "+c.playerName+ " needs help with " + reason);
						}
					}
				}
			}
		} else{
		c.getActionAssistant().sendMessage("@blu@You need a reason! Type ::needhelp (Your reason), keep it short!");
		}
	} else {
		c.getActionAssistant().sendMessage("@blu@You sent a help ticket already, please wait a bit before using this again!");
	}
	}
}