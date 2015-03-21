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

public class lootshare implements Command {

	@Override
	public void execute(Client c, String command) {
		if (!c.Lootshare) {
			c.Lootshare = true;
			c.getActionAssistant().sendMessage("@gre@You enabled lootshare");
		} else {
		c.Lootshare = false;
		c.getActionAssistant().sendMessage("@red@You have disabled lootshare");
			}
		}
		
	}


