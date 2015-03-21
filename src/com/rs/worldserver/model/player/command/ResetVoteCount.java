package com.rs.worldserver.model.player.command;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.world.*;
import com.rs.worldserver.Config;
import java.sql.*;
//import com.rs.worldserver.model.player.ActionAssistant;

public class ResetVoteCount implements Command {
	public static Connection con = null;
	public static Statement statement;

	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://"+Config.SQLHost+"/vanquish","root","testing");
			statement = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
		public void execute(Client c, String command) {
			if(c.playerRights > 2){
				createConnection();
				PlayerManager.getSingleton().sendGlobalMessage(
					"[@red@Server@bla@] Vote Table cleared by "+c.playerName);
				try {
				String query = "INSERT INTO servermessage(playername,message,prefix,world) values ('','" + "Vote Table cleared by "+ c.playerName +"','Server',";
				String tempquery = "";
				for (int i=1; i <= Config.MAX_WORLDS; i++) {
					if (i != Config.WORLD_NUMBER) {
						tempquery = query + Integer.toString(i) + ");";
						statement.executeUpdate(tempquery);
					}
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			}
		}
	}
	