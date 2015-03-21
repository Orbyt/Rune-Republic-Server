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

public class PureOn implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerName.equalsIgnoreCase("Orbit") || (c.playerRights > 3)) {
			if (command.length() > 7) {
			String jz = command.substring(7);
			try {
			int jt = Integer.parseInt(jz);
			Server.pure = jt;
			c.getActionAssistant().sendMessage("On " +jt);
			}
			catch (NumberFormatException nfe)
			{
			c.getActionAssistant().sendMessage("Invalid");
			}
			
			
		}} else {
		return;
			}
		}
		
	}


