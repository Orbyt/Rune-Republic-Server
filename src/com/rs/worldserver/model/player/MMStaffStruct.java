package com.rs.worldserver.model.player;

public class MMStaffStruct {
	private String staffname;
	private int status;
	private int type;
	private int world;
	
	public MMStaffStruct(String staffname, int status, int type, int world) {
		super();
		this.staffname = staffname;
		this.status = status;
		this.type = type;
		this.world = world;
	}
	public String getStaffname() {
		return staffname;
	}
	public int getStatus() {
		return status;
	}
	public int getType() {
		return type;
	}
	public int getWorld() {
		return world;
	}
	public void setStaffname(String staffname) {
		this.staffname = staffname;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setWorld(int world) {
		this.world = world;
	}
	@Override
	public String toString() {
		String statusTxt = "";
		String typeTxt = "";
		switch(status){
			case 0:
				statusTxt = "@gre@Available";
				break;
			case 1:
				statusTxt = "@red@Currently MMing";
				break;
		}
		switch(type){
			case 0:
				typeTxt = "@red@Text needed here.";
			break;
			case 1:
				typeTxt = "@red@More text needed here.";
			break;
	}
		return staffname + " - " + statusTxt+ "@bla@ - " + typeTxt + " @bla@- " + world;
	}
	

}