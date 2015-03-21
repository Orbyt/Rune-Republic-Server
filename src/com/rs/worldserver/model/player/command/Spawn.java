package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.world.*;

import java.util.ArrayList;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
public class Spawn implements Command {
//DONT USE, poorly written, rewrite, command.substring is incorrect
	
	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit")) {
			int newNPC = Integer.parseInt(command.substring(6));
			int id = Server.getNpcManager().spawnNPC(newNPC, c.getAbsX(), c.getAbsY(), c.getHeightLevel(), 0);
			System.out.println("ID: "+id);
			c.getActionAssistant().sendMessage("spawning newNPC" + newNPC);
			Server.getNpcManager().getNPC(id).petOwner = c.playerId;
			Server.getNpcManager().getNPC(id).isPet = true;
			c.petId = id;
		}
	}

}
