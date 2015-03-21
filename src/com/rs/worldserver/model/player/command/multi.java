package com.rs.worldserver.model.player.command;

import java.io.IOException;
import java.io.*;
import java.util.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.*;

public class multi implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit") || (c.playerRights > 3)) {
			String prefix = "[@red@Server Notice@bla@]";
			if (Server.FFAMULTI) {
			Server.FFAMULTI = false;
			c.getActionAssistant().sendMessage("Multi FFA disabled");
		String mess = "" + c.playerName + " has DISABLED multi mode for FFA arena (message sent to this world only)";
				
				PlayerManager.getSingleton().sendGlobalMessage(
					prefix + ":@bla@ "
							+ mess);
			}
			else {
			Server.FFAMULTI = true;
			c.getActionAssistant().sendMessage("Multi FFA enabled");
				String mess = "" + c.playerName + " has ENABLED multi mode for FFA arena (message sent to this world only)";
				
				
				PlayerManager.getSingleton().sendGlobalMessage(
					prefix + ":@bla@ "
							+ mess);
			}
		} else {
		return;
			}
		}
		
	}


