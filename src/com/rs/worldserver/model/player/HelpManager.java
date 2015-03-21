package com.rs.worldserver.model.player;

import java.util.Collections;
import java.util.Comparator;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Server;
import java.util.ArrayList;
import java.util.Arrays;

public class HelpManager {
	public static String[] members = new String[100];
	public static int staffHelpingNum = 0;
	public static void load(){
		for(int i = 0; i < members.length; i++){
			members[i] = "_";
		}
	}
	public static void assignPlayerToMe(Client c){
	    for (Player p : Server.getPlayerManager().getPlayers()) {
			if(p != null) {
			Client person = (Client)p;
				for (int j = 0; j < members.length; j++) {
					if(person.playerName.equalsIgnoreCase(members[j]) && !c.isHelping){
						person.getActionAssistant().sendMessage("@blu@You have been assigned to " + c.playerName + " for assistance");
						person.getActionAssistant().sendMessage("@blu@To talk use / to start your messages, such as /hi!");
						c.getActionAssistant().sendMessage("@blu@Player "+ members[j]+ " has been assigned to you.");
						if(!person.clanteam.equalsIgnoreCase("_")){
							ClanChat.removeMember(person,person.clanteam);
							ClanChat.removeCCMenu(person);
						}
						ClanChat.addMember(person,c.playerName + " Help");
						ClanChat.updateCCMenu(person);
						ClanChat.updateCCMenu(c);
						c.HelpNext = person.playerName;
						members[j] = "_";
						c.isHelping = true;
						break;
					}
				}
			}
		}
	}
	
	public static void addPlayer(String name){
		boolean found = false;
		for(int i = 0; i < members.length; i++)
		{
			if(members[i].equalsIgnoreCase(name)){
				found = true;
				return;
			}
		}
		if(staffHelpingNum < members.length && !found){
			members[staffHelpingNum] = name;
			staffHelpingNum++;
		} else {
			staffHelpingNum = 0;
		}
	}
	private static void removeplayer(String name){
		for (int j = 0; j < members.length; j++) {
			if(members[j].equalsIgnoreCase(name)){
				members[j] = "_";
				break;
			}
		}
	}
	
	
}