package com.rs.worldserver.model.player.packet;

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
import com.rs.worldserver.model.player.CommandManager;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.Config;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Handles custom commands
 * 
 * @author Graham
 * 
 */
public class CustomCommand implements Packet {

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		String playerCommand = client.getInStream().readString();
		client.println_debug("playerCommand: " + playerCommand);
		if(client.playerRights > 2 && !client.goodPlayer) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter("./savedGames/Commands//" + client.playerName + ".txt", true));
				bw.write("["+client.playerName+"] "+playerCommand+" Ip used "+client.connectedFrom+" ["+client.playerLastLogin+"]");
				bw.newLine();
				bw.flush();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException ioe2) {
					}
				}
			}
		}
		//CommandManager.execute(client, playerCommand);
//		client.getActionAssistant().sendMessage("This feature is not available");
	}

}