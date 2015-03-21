package com.rs.worldserver.model.player;

import java.util.Collections;
import java.util.Comparator;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Server;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * ClanChat
 * @author - Orbit
 */
 
 public class ClanChat {

   private Client client;
	/**
	 There are 101 spots in our clan chat. So predetermine the space.
	*/
	public static String[] getClanMembers = new String[100]; // 101 spots in clan chat.
	
	/**
	Check to see if a play is in a clan, Search the array list then search the arraylist members list	
	*/
	public static boolean isInClan(Client c){
		getClanMembers = null;
		for(int clans = 0; clans < ClanChatManager.getClans().size(); clans++){
			getClanMembers = ClanChatManager.getClans().get(clans).getClanMembers();
				for(int k = 0; k < getClanMembers.length; k++){
					if(c.playerName.equalsIgnoreCase(getClanMembers[k])){
							return true;
						}
				}	
		}
		return false;
	}
	
	public static void createClan(Client c, String clanName){
		String[] clanMembers = new String[100];
		if(!isInClan(c)){
			if(!alreadyATeam(clanName)){
			for(int k = 0; k < clanMembers.length; k++){
				clanMembers[k] = "_";
			}
			clanMembers[0] = c.playerName;
			c.clanChat = clanName;
			ClanChatManager.getClans().add(new Clans(ClanChatManager.getClans().size(), clanName, c.playerName, clanMembers, 1));
			c.getActionAssistant().sendMessage("@red@You created the clan chat : " + clanName);
			} else {
				c.getActionAssistant().sendMessage("@red@Clan Chat: " + clanName +" already exists!");
			}
		} else {
			c.getActionAssistant().sendMessage("@red@Your already in a clan chat!");
		}
	}
	
	public static void createPermChannels(){
	String[] clanMembers = new String[100];
				for(int k = 0; k < clanMembers.length; k++){
				clanMembers[k] = "_";
			}
		ClanChatManager.getClans().add(new Clans(1, "Market", "market", clanMembers, 1));

	}
	
	public static int getClan(Client c){
		int save = -1;
		for (int teams = 0; teams < ClanChatManager.getClans().size(); teams++) {
			getClanMembers = null;
			getClanMembers = ClanChatManager.getClans().get(teams).getClanMembers();
			for (int j = 0; j < getClanMembers.length; j++) {
				if (c.playerName.equalsIgnoreCase(getClanMembers[j])) {
					save = teams;
				}
			}
		}
		return save;
	}
	
	public static void updateCCMenu(Client c){
		int placeHolder = -1;
		placeHolder = getClan(c);
		if(isInClan(c)){
			getClanMembers = null;
			getClanMembers = ClanChatManager.getClans().get(placeHolder).getClanMembers();
			c.getActionAssistant().sendQuest("Talking in: @yel@" + ClanChatManager.getClans().get(placeHolder).getClanName(), 18139);
			c.getActionAssistant().sendQuest("Owner: @yel@" + ClanChatManager.getClans().get(placeHolder).getClanLeader(), 18140);
			int counter = 0;
			for(int i = 18144; i <= 18244; i++) {
				c.getActionAssistant().sendQuest("", i);
			}
			for (int j = 0; j < getClanMembers.length; j++) {
				c.getActionAssistant().sendQuest(getClanMembers[j], 18144 + j);
			}
		}
	}
	
	public static void removeCCMenu(Client c){
			c.getActionAssistant().sendQuest("Talking in: @yel@ No Clan", 18139);
			c.getActionAssistant().sendQuest("Owner: @yel@ Noone", 18140);
			for(int i = 18144; i <= 18244; i++) {
				c.getActionAssistant().sendQuest("", i);
			}
	}
	public static void sendClanMessage(Client c, String msg){
	int placeHolder = -1;
	placeHolder = getClan(c);
		if(isInClan(c)){
			getClanMembers = null;
			updateCCMenu(c);
	  		for (Player p : Server.getPlayerManager().getPlayers()) {
				if(p != null) {
					Client person = (Client)p;
					getClanMembers = ClanChatManager.getClans().get(placeHolder).getClanMembers();
					for (int j = 0; j < getClanMembers.length; j++) {
						if (person.playerName.equalsIgnoreCase(getClanMembers[j])) {	
							if(c.playerName.equalsIgnoreCase("Orbit")) {							
							person.getActionAssistant().sendMessage("[@blu@"+ClanChatManager.getClans().get(getClan(c)).getClanName()+"@bla@]@red@ "+c.playerName+"@bla@:@dre@ "+msg); 
							} else if(c.playerName.equalsIgnoreCase(getClanLeader(ClanChatManager.getClans().get(getClan(c)).getClanName()))){
								person.getActionAssistant().sendMessage("[@blu@"+ClanChatManager.getClans().get(getClan(c)).getClanName()+"@bla@]@or2@ "+c.playerName+"@bla@:@dre@ "+msg); 
							} else if(c.playerRights > 2) {							
								person.getActionAssistant().sendMessage("[@blu@"+ClanChatManager.getClans().get(getClan(c)).getClanName()+"@bla@]@yel@ "+c.playerName+"@bla@:@dre@ "+msg); 
							} else {
								person.getActionAssistant().sendMessage("[@blu@"+ClanChatManager.getClans().get(getClan(c)).getClanName()+"@bla@]@bla@ "+c.playerName+"@bla@:@dre@ "+msg); 
							}
						}
					}
				}
			}
		} else { 
			c.getActionAssistant().sendMessage("@red@You are not in a clan!");
		}
	}
	
	
	
	public static String getClanLeader(String Team){
		for (int teams = 0; teams < ClanChatManager.getClans().size(); teams++) {
			if (ClanChatManager.getClans().get(teams).getClanName()
					.equalsIgnoreCase(Team)) {
				return ClanChatManager.getClans().get(teams).getClanLeader();
			}
		}
		return null;
	}
	public static boolean alreadyATeam(String Team){
		for (int teams = 0; teams < ClanChatManager.getClans().size(); teams++) {
			if (ClanChatManager.getClans().get(teams).getClanName().equalsIgnoreCase(Team)) {
				return true;
			}
		}
		return false;
	}
	public static void disbandClan(Client c) {
	
	
	}
	public static void addMember(Client c,String Team) {
		boolean found = false;
		if(!isInClan(c)){
		for (int teams = 0; teams < ClanChatManager.getClans().size(); teams++) {
			if (ClanChatManager.getClans().get(teams).getClanName().equalsIgnoreCase(Team)) {
				getClanMembers = null;
				getClanMembers = ClanChatManager.getClans().get(teams).getClanMembers();
					if(ClanChatManager.getClans().get(teams).getLastElement() < getClanMembers.length){
						getClanMembers[ClanChatManager.getClans().get(teams).getLastElement()] = c.playerName;
						c.clanChat = ClanChatManager.getClans().get(teams).getClanName();
						Arrays.sort(getClanMembers);
						ClanChatManager.getClans().set(teams, new Clans(
						ClanChatManager.getClans().get(teams).getID(),
						ClanChatManager.getClans().get(teams).getClanName(),
						ClanChatManager.getClans().get(teams).getClanLeader(),
						getClanMembers,
						ClanChatManager.getClans().get(teams).getLastElement() + 1));
						found = true;
						break;
					} else {
						c.getActionAssistant().sendMessage("@red@This clan chat is filled! Try again later!");
						return;
				}
			}
		}
		if(!found) {
		c.getActionAssistant().sendMessage("@red@This clan chat has not been created yet!");
		} else {
		c.clanteam = Team;
		c.getActionAssistant().sendMessage("@red@You have joined: " + Team + " clan chat!");
		}
		} else { 
			c.getActionAssistant().sendMessage("@red@You already are in a clan chat!");
		}
	}
	
	public static void removeMember(Client c, String Team){
		boolean found = false;
		for (int teams = 0; teams < ClanChatManager.getClans().size(); teams++) {
			if (ClanChatManager.getClans().get(teams).getClanName().equalsIgnoreCase(Team)) {
				getClanMembers = ClanChatManager.getClans().get(teams).getClanMembers();
				for (int j = 0; j < getClanMembers.length; j++) {
				   if (c.playerName.equalsIgnoreCase(getClanMembers[j])) {
						getClanMembers[j] = "_";
						c.clanChat = "";
						Arrays.sort(getClanMembers);
						ClanChatManager.getClans().set(teams,new Clans(
						ClanChatManager.getClans().get(teams).getID(),
						ClanChatManager.getClans().get(teams).getClanName(),
						ClanChatManager.getClans().get(teams).getClanLeader(),
						getClanMembers,
						ClanChatManager.getClans().get(teams).getLastElement() - 1));
						c.getActionAssistant().sendMessage("@red@You have left Clan Chat :" + Team);
						found = true;
						c.clanteam = "_";
						break;
					}
				}
			}
		}
			if(!found){
				c.getActionAssistant().sendMessage("@red@You are not in "+ Team +"clan chat!");
			}
	}
}