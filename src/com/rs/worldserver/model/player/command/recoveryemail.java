package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.*;
import java.io.*;
import java.net.*;
 
public class recoveryemail implements Command {
	String username;
	String password;
	String playersName;
   

	@Override
	public void execute(Client c, String command) {
	String[] args = command.split(" ");
	if (args.length != 3) {c.getActionAssistant().sendMessage("Something went wrong, use ::setrecoveryemail email email"); return;}
	if (args[1].equalsIgnoreCase("email")) {c.getActionAssistant().sendMessage("Something went wrong, use ::setrecoveryemail email email"); return;}
	if (!args[1].equalsIgnoreCase(args[2])) { c.getActionAssistant().sendMessage("Your email address didn't match"); return;}
	
	String test = c.playerName+" ' " +args[1].toLowerCase()+ " ' ";
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(Config.HD1 +":\\xamp\\htdocs\\emails.txt", true));
			bw.write(test);//+" "+ bankItems +" "+ bankItemsN);
			bw.newLine();
			bw.flush();
			c.getActionAssistant().sendMessage("Success! Check your email in 60 seconds");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
					//printOut("Error writing system log.");
					ioe2.printStackTrace();
			}
		}
	}

	
		
}