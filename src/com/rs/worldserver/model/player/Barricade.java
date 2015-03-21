package com.rs.worldserver.model.player;
public class Barricade {

	public Barricade(int id, int x, int y, int z, int holder) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.holder = holder;
	}

	private int id;
	private int x;
	private int y;
	private int z;
	private int holder;
	
	public int getHolder(){
		return holder;
	}
	public int getId() {
		return id;
	}

	public int getAbsX() {
		return x;
	}

	public int getAbsY() {
		return y;
	}

	public int getHeightLevel() {
		return z;
	}
}