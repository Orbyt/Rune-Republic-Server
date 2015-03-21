package com.rs.worldserver.model.player;
/*
*
* Top PKer class
* @author - Orbit
*
**/

public class TopPkers implements Comparable {
private int id = -1;
private String playerName;
private int playerRating,playerKills,playerDeaths = 0;

	public TopPkers(int id, String playerName, int playerKills, int playerDeaths, int playerRating){
		this.id = id;
		this.playerName = playerName;
		this.playerKills = playerKills;
		this.playerDeaths = playerDeaths;
		this.playerRating = playerRating;
	}

	public int getID(){
		return id;
	}

	public int getPlayerKills(){
		return playerKills;
	}

	public int getPlayerDeaths(){
		return playerDeaths;
	}

	public String getPlayerName(){
		return playerName;
	}

	public int getPlayerRating(){
		return playerRating;
	}
	public int compareTo(Object anotherPlayer) throws ClassCastException {
		if (!(anotherPlayer instanceof TopPkers))
		throw new ClassCastException("A player object expected.");
		int anotherPlayerRating = ((TopPkers) anotherPlayer).getPlayerRating();
		return this.playerRating - anotherPlayerRating;
	}


} 
