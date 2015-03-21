package com.rs.worldserver.model.player.command;

import java.io.IOException;
import java.io.*;
import java.util.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.*;
import com.rs.worldserver.world.PlayerManager;
public class SafeOff implements Command {

	@Override
	public void execute(Client c, String command) {
		if(c.playerName.equalsIgnoreCase("Orbit") || (c.playerRights > 3)) {
			Server.safe = false;
			c.getActionAssistant().sendMessage("Off");
				String prefix = "";
		String mess = "" + c.playerName + " has DISABLED the Safe FFA rule ";
				prefix = "[@red@Server Notice@bla@] ";
					PlayerManager.getSingleton().sendGlobalMessage(
					prefix
							+ mess);
		} else {
		return;
			}
		}
		
	}


