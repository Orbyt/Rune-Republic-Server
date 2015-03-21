package com.rs.worldserver.model.player.command;

//Shard Revolutions Generic MMORPG Server
//Copyright (C) 2008  Graham Edgecombe

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.world.PlayerManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

//import sun.misc.BASE64Encoder;
//import sun.misc.CharacterEncoder;
import com.rs.worldserver.model.player.Player;


/**
 * Changing password command.
 * 
 * @author Graham 
 * 
 */
public class ChangePassword implements Command {
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
	@Override
	public void execute(Client client, String command) {
		if(command.length() == 11 ||command.length() == 12 ) {
			client.getActionAssistant().sendMessage("Enter a password!");
			return;
		}		
		if (command.length() > 11 || command.length() < 26){
			client.setPlayerPass(encrypt(command.substring(11))+client.salt);
			client.getActionAssistant().sendMessage(
					"Your new pass is \"" + command.substring(11) + "\"");
					client.getActionAssistant().sendMessage("@red@ If somebody asked you to change it, do not log out, change it to something else");
			PlayerManager.getSingleton().saveGame(client);
		} else {
			client.getActionAssistant().sendMessage(
					"Syntax is ::pass newpassword");
		}
	}

}