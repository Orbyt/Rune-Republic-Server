package com.rs.worldserver;

public class Tile {
	
	private Object entity;
	
	private int[] tileLocation;
	
	public Tile(Object entity, int[] location) {
		setEntity(entity);
		setTile(location);
	}

	public void setTile(int[] tileLocation) {
		this.tileLocation = tileLocation;
	}

	public int[] getTile() {
		return tileLocation;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}
	
	private int[] pointer = new int[3];
	
	public Tile(int x, int y, int z) {
		this.pointer[0] = x;
		this.pointer[1] = y;
		this.pointer[2] = z;
	}
	
	public int[] getTile2() {
		return pointer;
	}
	
	public int getTileX() {
		return pointer[0];
	}
	
	public int getTileY() {
		return pointer[1];
	}
	
	public int getTileHeight() {
		return pointer[2];
	}	
	
}