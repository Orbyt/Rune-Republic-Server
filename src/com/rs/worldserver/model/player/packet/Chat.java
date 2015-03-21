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
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import java.sql.*;

/**
 * Chat packets
 * 
 * @author Graham
 * 
 */
public class Chat implements Packet {

public static Connection con = null;
	public static Statement statement;
	private String Vulgar[] = { "Noclip"," noclip "," ::noclip"," ::noclip ","::noclip ", " noclip","noclip ","noclip", "::noclip","no clip", "n o c l i p" };
	private boolean isVulgar(String j){
            for(int i = 0; i < Vulgar.length; i++) {
                    if(j.contains(Vulgar[i]))
                            return true;
            }
            return false;
    /*}public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://"+Config.SQLHost+"/vanquish","root","testing");
			statement = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	}public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://"+Config.SQLHost+"/vanquish","root","testing");
			statement = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean chatAllowed(String chattxr, Client client) {
					String chattxt = client.decodeChat(client.chatText,client.chatTextSize);
					boolean ChatAllowed = true;
						String legalChars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ .!@#$%^&*()[]-,'`:<>=;?";
						 for ( int i = 0; i < chattxt.length(); i++ )
							{	
							 if ( legalChars.indexOf( chattxt.charAt( i ) ) < 0 )
							{
								ChatAllowed = false;
							}
						}
						/*if (!ChatAllowed) {
								client.getActionAssistant().sendMessage("Please don't use symbols for now, numbers and letters only");
                                return false;
						}*/
						if (chattxt.length() > 80)
							{
								client.getActionAssistant().sendMessage("Please keep your text 80 characters or less");
                                return false;
							}
						return true;
	}
	public static final int REGULAR_CHAT = 4, UPDATE_CHAT_OPTIONS = 95,
			FRIEND_ADD = 188, FRIEND_REMOVE = 215, IGNORE_ADD = 133,
			IGNORE_REMOVE = 74, PRIVATE_MESSAGE = 126;

	@Override
	public void handlePacket(Client client, int packetType, int packetSize) {
		switch (packetType) {
		case UPDATE_CHAT_OPTIONS:
			int tradeAndCompete = client.getInStream().readUnsignedByte();
            client.pmstatus = client.getInStream().readUnsignedByte();
			if (client.pmstatus > 2) { client.pmstatus = 0; }
			//client.getActionAssistant().sendMessage("Status: "+client.pmstatus);
            int publicChat = client.getInStream().readUnsignedByte();
			
			try { 
			//	Connection con = DriverManager.getConnection("jdbc:mysql://localhost/donationsystem","root","NUIDSfnu7387ca");
				//Statement statement = con.createStatement();
				String query = "UPDATE storage SET pmstatus=" + client.pmstatus + " WHERE name='" + client.playerName + "'";
				statement.executeUpdate(query);
			//	statement.close();
				//con.close();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
            for (int i1 = 1; i1 < Constants.MAXIMUM_PLAYERS; i1++) {
				Client p = (Client) PlayerManager.getSingleton().getPlayers()[i1];
				if (p == null)
					continue;
				if (!p.isActive || p.disconnected)
					continue;
					Client o = (Client) p;
					if(o != null) {
						o.getFriendsAssistant().updatePM(client.playerId, 1);
					}
                }
            
            break;
		case REGULAR_CHAT:
			if (client.isMuted() || client.Muted > 0) {
				client.getActionAssistant().sendMessage("You are muted for " +(client.mutetimer/2) +" more seconds");
				break;
			}
			if (packetSize <= 2) {
			client.getActionAssistant().sendMessage("An error has occured with chat processing");
			break;
			}
			if (isVulgar(Misc.textUnpack(client.chatText, packetSize - 2).toLowerCase())) {
                               client.getActionAssistant().sendMessage("Please refrain from mentioning this.");
                                break;
                        }
								

			client.chatTextEffects = client.getInStream().readUnsignedByteS();
			client.chatTextColor = client.getInStream().readUnsignedByteS();
			client.chatTextSize = (byte) (packetSize - 2);
			client.getInStream().readBytes_reverseA(client.chatText,
					client.chatTextSize, 0);
					String chattxt = client.decodeChat(client.chatText,client.chatTextSize);
					if (!chatAllowed(chattxt,client)) {
						break;
					}
			client.chatTextUpdateRequired = true;
			client.println_debug("Text [" + client.chatTextEffects + ","
					+ client.chatTextColor + "]: "
					+ Misc.textUnpack(client.chatText, packetSize - 2));
			//System.out.println(client.decodeChat(client.chatText,client.chatTextSize));
			break;
		case FRIEND_ADD:
			client.getFriendsAssistant().addFriend(
					client.getInStream().readQWord());
			break;
		case FRIEND_REMOVE:
			client.getFriendsAssistant().removeFriend(
					client.getInStream().readQWord());
					
			break;
		case IGNORE_ADD:
			client.getFriendsAssistant().addIgnore(
					client.getInStream().readQWord());
			break;
		case IGNORE_REMOVE:
			client.getFriendsAssistant().removeIgnore(
					client.getInStream().readQWord());
			break;
		case PRIVATE_MESSAGE:
			long to = client.getInStream().readQWord();
			int chatTextSize = (byte) (packetSize - 8);
			byte[] chatText = new byte[256];
			boolean cantPM = false;
			client.getInStream().readBytes(chatText, chatTextSize, 0);
            String msg = client.decodeChat(chatText, chatTextSize);
			if (client.isMuted() || client.Muted > 0) {
				client.getActionAssistant().sendMessage("You are muted for " +(client.mutetimer/2) +" more seconds");
				break;
			}
			
			
			if (client.helpOn){ 
				 if(client.playerRights < 2) { // only staff can pm you. 
					 client.getActionAssistant().sendMessage("@red@This staff member is currently helping players"); 
					 client.getActionAssistant().sendMessage("@red@If you need help please do ::needhelp"); 
					 break; 
				 } 
            } 
			if(client.getPlayerName().equalsIgnoreCase("Armani")){
				client.getActionAssistant().sendMessage("@red@No one may PM God! :)"); 
				break; 
			}
			
			if (client.newFag > 0) {
			//client.getActionAssistant().sendMessage("@red@PM disabled for new players due to abuse"); 
				//break; 
				}
				//String chattxt2 = client.decodeChat(client.chatText,client.chatTextSize);
				//	if (!chatAllowed(chattxt2,client)) {
					//	break;
				//	}
			client.getFriendsAssistant().sendMessage(to, chatText, chatTextSize);
			//System.out.println(client.decodeChat(chatText,chatTextSize));
            client.procMsg(msg);
			break;
		}
	}

}
