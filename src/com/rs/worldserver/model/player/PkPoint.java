package com.rs.worldserver.model.player;
public class PkPoint {
	private String ip = "";
	private long timeAdded = 0;
	
	public PkPoint(String ip,long timeAdded){
		this.ip = ip;
		this.timeAdded = timeAdded;
	}
	public String getIP(){
	
		return this.ip;
	}
	
	public long getTimeAdded(){
	
		return this.timeAdded;
	}

}