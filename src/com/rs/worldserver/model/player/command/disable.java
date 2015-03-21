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

public class disable implements Command {

	@Override
	public void execute(Client c, String command) {
		if (!c.Disable) {
			c.Disable = true;
			c.getActionAssistant().sendMessage("You enabled privacy mode");
		} else {
			c.Disable = false;
			c.getActionAssistant().sendMessage("You have disabled privacy mode");
		}
	}		
}


