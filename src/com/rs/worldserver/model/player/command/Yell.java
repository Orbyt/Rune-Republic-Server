package com.rs.worldserver.model.player.command;

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Config;
import java.sql.*;
import com.rs.worldserver.util.BankProcessor;

// SQL might be for multi-world yell messages, need to edit
public class Yell implements Command {
	public static Connection con = null;
	public static Statement statement;

//	public static void createConnection() {
//		try {
//			Class.forName("com.mysql.jdbc.Driver").newInstance();
//			con = DriverManager.getConnection("jdbc:mysql://"+Config.SQLHost+"/vanquish","root","testing");
//			statement = con.createStatement();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	@Override
	public void execute(Client client, String command) {
	if (client.playerRights > 1) {
		if (command.length() > 5) {
			if (client.isYellMuted() || client.isMuted()) {
				client.getActionAssistant().sendMessage("You are muted.");
				return;
			}
			if (command.length() >75 || command.substring(5).toLowerCase().contains(":stop:")) {
			client.getActionAssistant().sendMessage("Unable to process.");
			return;
			}
			String prefix = "";
			if (client.playerRights == 0)
				prefix = "[@gre@MEMBER@bla@]: ";		
			if (client.playerRights == 1)
				prefix = "[@gre@$$$@bla@] ";
			if (client.playerRights == 2)
				prefix = "[@red@$$$@bla@] ";
			if (client.playerRights == 3)
				prefix = "[@blu@SUPPORT@bla@] ";
			if (client.playerRights == 13)
				prefix = "[@or2@EVENT SUPPORT@bla@] ";
			if (client.playerRights == 4)
				prefix = "[@mag@MOD@bla@] ";
			if (client.playerRights == 5)
				prefix = "[@or2@MARKET@bla@] ";
			if (client.playerRights == 6)
				prefix = "[@cya@HEAD-MOD@bla@] ";
			if (client.playerRights == 7)
				prefix = "[@yel@ADMIN@bla@] ";
			if (client.playerRights == 8)
				prefix = "[@yel@ADMIN@bla@] ";
			if (client.playerRights == 9)
				prefix = "[@red@God <3@bla@] ";	
			if (client.playerRights == 10)
				prefix = "[@red@Advisor@bla@] ";	
			if (client.playerRights == 11)
				prefix = "[@red@Legend@bla@] ";
			if (client.playerName.equalsIgnoreCase("Orbit"))
				prefix = "[@red@Developer@bla@] ";
		
			
			if (command.substring(5).contains("#url#")) {
				return;
				}
			
			PlayerManager.getSingleton().sendGlobalMessage(
					prefix + "@blu@"+client.getPlayerName() + ":@bla@ "
							+ command.substring(5));
//							if (command.substring(5).contains("#url#")) {
//							return;
//							}

//			try {
//				Connection con = DriverManager.getConnection("jdbc:mysql://localhost/donationsystem","root","NUIDSfnu7387ca");
//				String query = "INSERT INTO servermessage(playername,message,prefix,world) values ('" + client.getPlayerName() + "','" + command.substring(5).replaceAll("'","~") + "','" + prefix + "',";
//				String tempquery = "";
//				for (int i=1; i <= Config.MAX_WORLDS; i++) {
//					if (i != Config.WORLD_NUMBER) {
//						tempquery = query + Integer.toString(i) + ");";
//						//Statement statement = con.createStatement();
//						statement.executeUpdate(tempquery);
//					//	statement.close();
//					}
//				}
//			//	con.close();
//			}
//			catch (Exception e) 
//			{
//				e.printStackTrace();
//			}
			
		} else {
			client.getActionAssistant().sendMessage(
					"Syntax is ::yell <message>.");
		}
		} else {
			client.getActionAssistant().sendMessage("You do not have a high enough donator status yet to use this! Be sure to donate for these benefits!");
		}
	}

}
