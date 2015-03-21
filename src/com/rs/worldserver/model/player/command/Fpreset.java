package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.FightPits;

public class Fpreset implements Command {

	@Override
	public void execute(Client c, String command) {
		//if(c.playerName.equalsIgnoreCase("Orbit")) {
		if(c.playerRights >= 4) {
			//for(int i = 0; i < 1000; i++)
				//if(Server.getFightPits().totalPlayers == 0 && Server.getFightPits().started) {
					Server.getFightPits().endGame();
					//Server.getFightPits().updateWaitInterfaces(c);
					//Server.getFightPits().process();
					//Server.getFightPits().announceTime();
					Server.getFightPits().startGame();
					PlayerManager.getSingleton().sendGlobalMessage("[@red@SERVER-MESSAGE@bla@]: FightPits Reset By "+ c.playerName);
		} else {
		c.getActionAssistant().sendMessage("You do not have high enough rights to use this command.");
		}
	}
}	