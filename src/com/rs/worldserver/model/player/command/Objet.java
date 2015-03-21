
package com.rs.worldserver.model.player.command;
import com.rs.worldserver.Config;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.world.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
public class Objet implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit")) {
			//c.specialAmount = 10000;
			int newNPC = Integer.parseInt(command.substring(6));
			c.getActionAssistant().object(newNPC, c.getX(), c.getY(), 0, 10);
			c.getActionAssistant().requestUpdates();	
		}
	}

}
