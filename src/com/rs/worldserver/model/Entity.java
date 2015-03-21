package com.rs.worldserver.model;

// TODO move most of the Player/NPC classes into here as a lot of it is shared

/**
 * Represents an entity (player or NPC).
 * 
 * @author Graham
 * 
 */
public abstract class Entity {

	/**
	 * Current position.
	 */
	public int heightLevel;
	public int absY;
	public int absX;

	/**
	 * @param absX
	 *            the absX to set
	 */
	public void setAbsX(int absX) {
		this.absX = absX;
	}

	/**
	 * @return the absX
	 */
	public int getAbsX() {
		return absX;
	}
	public int getX() {
		return absX;
	}
	/**
	 * @param absY
	 *            the absY to set
	 */
	public void setAbsY(int absY) {
		this.absY = absY;
	}

	/**
	 * @return the absY
	 */
	public int getAbsY() {
		return absY;
	}
	public int getY() {
		return absY;
	}
	/**
	 * @param heightLevel
	 *            the heightLevel to set
	 */
	public void setHeightLevel(int heightLevel) {
		this.heightLevel = heightLevel;
	}

	/**
	 * @return the heightLevel
	 */
	public int getHeightLevel() {
		return heightLevel;
	}

}
