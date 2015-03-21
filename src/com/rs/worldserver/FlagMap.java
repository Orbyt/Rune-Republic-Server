package com.rs.worldserver;

import com.rs.worldserver.model.player.*;

public class FlagMap {

	private static boolean[][] flaggedLocations = new boolean[4000][11000];
	private static int[][] flaggedType = new int[4000][11000];
	
	public static void set(int[] pointer, boolean val,int type) {
		flaggedLocations[pointer[0]][pointer[1]] = val; 
		flaggedType[pointer[0]][pointer[1]] = type;
	}
	
	public static void set(int[] pointer, boolean val) {
		flaggedLocations[pointer[0]][pointer[1]] = val; 
	}
	
	public static boolean locationOccupied(int[] pointer, Client client) {
		if(pointer[0] <= 0 || pointer[1] <= 0 || pointer[2] < 0) {
			return false;
		}
		if (flaggedLocations[pointer[0]][pointer[1]]) {
			
			Tile[] entityTiles = TileManager.getTiles(client, client.getAbsX(), client.getAbsY(), client.heightLevel);
			
			for (Tile tiles : entityTiles) 
				if (tiles.getTile()[0] == pointer[0] && tiles.getTile()[1] == pointer[1]) 
					return false;
		}
		//System.out.println(flaggedLocations[pointer[0]][pointer[1]]);
		//System.out.println(flaggedType[pointer[0]][pointer[1]]);
		return flaggedLocations[pointer[0]][pointer[1]];
	}
	
	public static void resetFlaggedLocations() {
		for (int x = 0; x < 4000; x++) 
			for (int y = 0; y < 11000; y++)
				flaggedLocations[x][y] = false;
	}
	
}