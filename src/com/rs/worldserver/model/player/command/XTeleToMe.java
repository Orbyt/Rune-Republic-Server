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


public class XTeleToMe implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			if (command.length() > 10) {
				String name = command.substring(10);
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected || p.jailTimer > 0)
						continue;
					
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						Client d = (Client) p;	
						if(d.duelStatus > 4){
						c.getActionAssistant().Send("@red@This player is in a duel!");
						return;
					}
						d.fishing = false;
						d.walked = true;
						c.getActionAssistant().Send("@red@You have teleported "+p.playerName+" to you.");
						d.cancelTasks();
						d.getActionAssistant().startTeleport3(c.absX, c.absY, c.heightLevel, "modern");
						d.getActionAssistant().Send("@red@You have been teleported to "+ c.playerName);
						c.updateRequired = true; 
						c.appearanceUpdateRequired = true;
						d.updateRequired = true;
						d.appearanceUpdateRequired = true;
						
						
					}
				}
				} else {
					c.getActionAssistant().sendMessage("Syntax is ::xteletome <name>.");
				}
				return;
			} 
			if ((c.playerRights > 2 || BankProcessor.checkMM(c.playerName)) && (c.playerRights != 5) && (!c.inWild() || c.inFFA())) {
			if (command.length() > 10) {
				String name = command.substring(10);
				if (c.inDuelArena()) {
					c.getActionAssistant().Send("@red@You can't teleport him to you here.");
					return;
				}
				if (c.inDi()) { //|| c.inDie()) {
					c.getActionAssistant().Send("@red@You can't teleport him to you here.");
					return;
				}				
				for (Player p : Server.getPlayerManager().getPlayers()) {
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected || p.jailTimer > 0)
						continue;
					if (p.getPlayerName().equalsIgnoreCase(name)) {
						if(c.inWild() && c.wildLevel >= Config.NO_TELEPORT_WILD_LEVEL) {
							c.getActionAssistant().sendMessage("You can't teleport someone on or above level "+Config.NO_TELEPORT_WILD_LEVEL+" in the wilderness.");
							break;
						}
						

						
						Client d = (Client) p;
						if ((d.playerRights > 9) && (d.playerRights != 12)) {
							c.getActionAssistant().Send("@red@You can't teleport him to you.");
							break;
						}
						if (p.playerRights == 0 && c.inDi()) {
						c.getActionAssistant().Send("@red@You can't teleport him to you.");
							break;
							}
						if (p.duelStatus > 1) {
							c.getActionAssistant().Send("@red@You can't teleport stakers to you.");
							break;
							}
						if(d.inWild() || d.teleoff == 1){
							c.getActionAssistant().Send("@red@You can't teleport to someone in the wild.");
							break;							
						}						
						d.fishing = false;
						d.walked = true;
						c.getActionAssistant().Send("@red@You have teleported "+p.playerName+" to you.");
						d.cancelTasks();
						d.getActionAssistant().startTeleport3(c.absX, c.absY, c.heightLevel, "modern");
						d.getActionAssistant().Send("@red@You have been teleported to "+ c.playerName);
						c.updateRequired = true; 
						c.appearanceUpdateRequired = true;
						d.updateRequired = true;
						d.appearanceUpdateRequired = true;
						
						
					}
				}
				} else {
					c.getActionAssistant().sendMessage("Syntax is ::xteletome <name>.");
				}			
		}
	}
}
