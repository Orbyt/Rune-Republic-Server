package com.rs.worldserver.model.player;
/*
 * Achievements class constructor
 * @author - Orbit
 */
public class Achievements{
	private int id = -1;
	private String achievementName,achievementDesc,items,itemAmounts;
	private int points = 0;
	
	public Achievements(int id, String achievementName, String achievementDesc, int points, String items, String amounts){
		this.id = id;
		this.achievementName = achievementName;
		this.achievementDesc = achievementDesc;
		this.points = points;
		this.items = items;
		this.itemAmounts = amounts;
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		return achievementName;
	}
	
	public String getDesc(){
		return achievementDesc;
	}
	
	public String getItems(){
		return items;
	}
	
	public int getPoints(){
		return points;
	}
	public String getItemAmount(){
		return itemAmounts;
	}

}
