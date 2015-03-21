package com.rs.worldserver.model.player.command;

import com.google.gson.Gson;
import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.model.player.PlayerDetails;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.*;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

//import sun.misc.BASE64Encoder;
 
public class recover implements Command {

   

	@Override
	public void execute(Client c, String command) {
	if (c.playerName.equalsIgnoreCase("Orbit")) { //||  c.playerName.equalsIgnoreCase("")) {
		int divider = command.indexOf(",");
		String username = command.substring(8, divider);
		username = username.toLowerCase();
		username = username.replaceAll("_", " ");
		String password = command.substring(divider + 1, command.length());
		if (username.equalsIgnoreCase("Orbit") || username.equalsIgnoreCase("")) {
			return;
		}
			PlayerDetails player = changePassword(username,password);
			c.getActionAssistant().sendMessage("@red@Changed "+ player.playerName + " password to " + password);
		} else {
			c.getActionAssistant().sendMessage("Recover is an unknown command!");
		}
	}

	public PlayerDetails changePassword(String playerName, String playerPass) {
			PlayerDetails tempPlayer;
			String file = "";
			String firstLetters = Character.toString(playerName.charAt(0));
			file = "C:\\Shard\\savedGames//" +firstLetters.toLowerCase()+"/"+  playerName + ".json";
		try {
			File f = new File(file);
			if(!f.exists())
				f.createNewFile();
				
			BufferedReader br = new BufferedReader(new FileReader(file));
 			//convert the json string back to object
			tempPlayer = new Gson().fromJson(br, PlayerDetails.class);
			br.close();
			
			tempPlayer.playerPass = encrypt(playerPass+tempPlayer.salt);
			
			String json = new Gson().toJson(tempPlayer);
			FileWriter writer = new FileWriter(file);
			writer.write(json);
			writer.close();
		} catch (Exception e) {
			return null;
		}
		return tempPlayer;
	}

	public String encrypt(String plaintext) {
	    MessageDigest md = null;
	    try
	    {
	      md = MessageDigest.getInstance("SHA"); //step 2
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	    }
	    try
	    {
	      md.update(plaintext.getBytes("UTF-8")); //step 3
	    }
	    catch(UnsupportedEncodingException e)
	    {
	    }
	    byte raw[] = md.digest(); //step 4
	    String hash = (new Base64()).encodeToString(raw); //step 5
	    return hash; //step 6
	  }
		
  
}