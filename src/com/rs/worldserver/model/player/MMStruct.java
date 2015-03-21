package com.rs.worldserver.model.player;

/**
*t
* Stores information from the database 
* MM queue - Patrick August 12, 2012
*/
public class MMStruct {
	private int Id;
	private String user1;
	private String user2;
	private int world;
	private String type;
	private String date;
	
	public MMStruct(int id,String user1,String user2,int world,String type,String date){
		this.Id = id;
		this.user1 = user1;
		this.user2 = user2;
		this.world =world;
		this.type = type;
		this.date = date;
	}

	public String getType(){
		return type;
	}
	public int getId(){
		return this.Id;
	}
	
	public String getUser1(){
		return this.user1;
	}
	public String getUser2(){
		return this.user2;
	}
	public String getDate(){
		return this.date;
	}
	public int getWorld(){
		return this.world;
	}
}