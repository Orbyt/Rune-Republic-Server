package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;

public class Lottery implements Command {

    @Override
    public void execute(Client c, String command) {
        c.getActionAssistant().sendMessage("SQL");
    }
}
