package com.rs.worldserver.model.player.command;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;

public class Claim implements Command {

    public void execute(Client c, String command) {
        c.getActionAssistant().sendMessage("This feature is not yet implemented");
    }
}
