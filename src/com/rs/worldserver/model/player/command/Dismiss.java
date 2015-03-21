package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.world.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
public class Dismiss implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.petSpawner || c.playerRights > 3) {
			NPC npcPet = Server.getNpcManager().getNPC(c.petId);
            if(npcPet != null) {
				if (npcPet.petOwner == c.playerId) {
					Server.getNpcManager().deleteNPC(c.petId);
				}
			}
			
		}
	}
}
