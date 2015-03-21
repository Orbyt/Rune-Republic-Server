package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.world.*;

public class NpcReset implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit")) {
			for(int i = 0; i < 1000; i++)
				if(Server.getNpcManager().getNPC(i) != null) {
					Server.getNpcManager().getNPC(i).isDead = true;
				}
		PlayerManager.getSingleton().sendGlobalMessage("[@red@SERVER-MESSAGE@bla@]: NPC's Reset By "+ c.playerName);
		}
	}
}	
	
