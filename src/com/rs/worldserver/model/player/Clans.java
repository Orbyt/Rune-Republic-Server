package com.rs.worldserver.model.player;
/**
 * Clans list.
 * @author - Orbit
 */

 public class Clans {
	private int ID;
	private String clanName;
	private String clanLeader;
	private int lastElement;
	private String[] clanMembers = new String[100];
	
	public Clans(int ID, String clanName, String clanLeader,String[] clanMembers,int lastElement){
		this.ID = ID;
		this.clanName = clanName;
		this.clanLeader = clanLeader;
		this.clanMembers = clanMembers;
		this.lastElement =  lastElement;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public String getClanName(){
		return this.clanName;
	}
	
	public String getClanLeader(){
		return this.clanLeader;
	}
	
	public String[] getClanMembers(){
		return clanMembers;
	}
	public int getLastElement(){
		return lastElement;
	} 
 }