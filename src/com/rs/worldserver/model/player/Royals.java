package com.rs.worldserver.model.player;

public class Royals {
	/*
	 * variables
	 */
	public String varrockSenators[] = new String[6];
	public String varrockKing = "";
	public String faladorSenators[] = new String[6];
	public String faladorKing = "";
	
	
	
	/*
	 * rank checks
	 */
	public String getVarrockKing() {
		return varrockKing;
	}
	
	public String getFaladorKing() {
		return faladorKing;
	}
	
	public boolean isVarrockKing(String name) {
		if (name.equalsIgnoreCase(varrockKing)) 
			return true;
		return false;
	}
	
	public boolean isFaladorKing(String name) {
		if (name.equalsIgnoreCase(faladorKing)) 
			return true;
		return false;
	}
	
	
	/*
	 * Loops through arrays using a for-each loop to check if supplied name is a senator.
	 * 
	 */
	public boolean isVarrockSenator(String name) {
		for (String s : varrockSenators) {
			if (s.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	public boolean isFaladorSenator(String name) {
		for (String s : faladorSenators) {
			if (s.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

}
