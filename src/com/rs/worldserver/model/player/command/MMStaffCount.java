package com.rs.worldserver.model.player.command;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;

//import com.rs.worldserver.model.player.ActionAssistant;

public class MMStaffCount implements Command {

    @Override
    public void execute(Client c, String command) {
        c.getActionAssistant().sendMessage("SQL");

    }
}
