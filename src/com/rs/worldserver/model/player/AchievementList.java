package com.rs.worldserver.model.player;
/*
 * AchievementList class constructor
 * @author - Orbit
 */
public class AchievementList {
	private int id = -1;
	private String achievementName;
	private boolean Completed;
	private int Received = 0;
	public AchievementList(int id, String achievementName, boolean Completed, int received){
		this.id = id;
		this.achievementName = achievementName;
		this.Completed = Completed;
		this.Received = received;
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		return achievementName;
	}
	
	public boolean getCompleted(){
		return Completed;
	}
	public int getReceived(){
		return Received;
	}

}
