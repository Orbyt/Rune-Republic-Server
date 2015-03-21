package com.rs.worldserver.model.player;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Server;
import java.util.HashMap;
import java.util.*;

public class CastleWarsSettings {
	private static HashMap<Client, Integer> waitingRoom = new HashMap<Client, Integer>();
    private static HashMap<Client, Integer> gameRoom = new HashMap<Client, Integer>();
	private static ArrayList<Barricade> barricades = new ArrayList<Barricade>();
	
	public static ArrayList<Barricade> getBarricades() {
		return barricades;
	}
	public static HashMap<Client, Integer> getGameRoom(){
		return gameRoom;
	}
	
	public static HashMap<Client, Integer> getWaitingRoom(){
		return gameRoom;
	}

}