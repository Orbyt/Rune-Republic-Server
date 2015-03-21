package com.rs.worldserver.model.player.command;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.Config;
import java.sql.*;

import com.rs.worldserver.util.BankProcessor;


public class mm implements Command {

	@Override
	public void execute(Client client, String command) {
	if (client.playerRights > 1 || BankProcessor.checkMM(client.playerName)) {
	if (!Config.MainServer) {
	client.getActionAssistant().sendMessage("@red@You can't use that command on this world");
	return;
	}
			if (client.isYellMuted() || client.isMuted()) {
				client.getActionAssistant().sendMessage("You are muted.");
				return;
			}
		command = command.substring(3);
		String[] args = command.split(",");
		if(args.length > 0){
			int type = 0; // 0 is nrgp
			if(args[0].equalsIgnoreCase("both")){
				type = 1;
			}
				String prefix = "";
				String mess = "" + client.playerName + " is available for Middlemanning on world: " +Config.WORLD_NUMBER;
				prefix = "[@red@Server Notice@bla@] ";
		
			
		

			try {
				Connection con = DriverManager.getConnection("jdbc:mysql://"+Config.SQLHost+"/vanquish","root","testing");
				String query = "INSERT INTO servermessage(playername,message,prefix,world) values ('" + "" + "','" + mess.replaceAll("'","~") + "','" + prefix + "',";
				String tempquery = "";
				for (int i=1; i <= Config.MAX_WORLDS; i++) {
					
						tempquery = query + Integer.toString(i) + ");";
						Statement statement = con.createStatement();
						statement.executeUpdate(tempquery);
						statement.close();
				}
				con.close();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			}
		} else {
		}
	}

}
