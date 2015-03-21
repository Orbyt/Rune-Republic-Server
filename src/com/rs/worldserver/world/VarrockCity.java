package com.rs.worldserver.world;

import com.rs.worldserver.model.Entity;
import com.rs.worldserver.model.player.Player;


/**
 * 
 * This class maps out the city of Varrock
 * 
 * It contains methods which determine if a player is in a building in Varrock
 * 
 * @author main
 *
 */

public class VarrockCity extends Entity {
	
	 /*
	 * Sets player X and Y to values found in absX and absY from Entity class
	 */
   
	
	/*
	 * Is player in Varrock?
	 * This method tests a general square area and then eliminates odd shaped areas through boolean methods
	 * 
	 * Bottom Left Area is that spot in SW Varrock where the walls of Varrock arent a nice square
	 */
	
	public boolean inVarrock(int absX, int absY) {
		if ( (absX >= 3174 && absX <= 3272 && absY <= 3506 && absY >= 3382) && !inBottomLeftArea(absX, absY) &&
		     !inTopRightArea(absX, absY) && !inNorthGateArea(absX, absY)) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Is player in that SW corner of Varrock where the walls arent a perfect square?
	 */
	
	public boolean inBottomLeftArea(int absX, int absY) {
		if (absX >= 3173 && absX <= 3181 && absY <= 3398 && absY >= 3381) {
			return true;
		}
		return false;
	}
	
	/*
	 * Is player in that NE corner of Varrock outside the city walls?
	 */
	
	public boolean inTopRightArea(int absX, int absY) {
		if (absX >= 3252 && absY >= 3493) {
			return true;
		}
		return false;
	}
	
	/*
	 *  Is player in area directly north of the North Gate?
	 */
	
	public boolean inNorthGateArea(int absX, int absY) {
		if (absX >= 3231 && absX <= 3251 && absY >= 3501 && absY <= 3506) {
			return true;
		}
		return false;
	}

}
