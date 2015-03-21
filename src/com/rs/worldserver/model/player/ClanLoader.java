package com.rs.worldserver.model.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
/** 
@author - Orbit
*
*/
public class ClanLoader {
	private Client 		client;
	static Scanner dIn = null;
	static int length = 0;
	static String line = null;
	static int divider;
	static int id;
	static String clanName = null;
	static String clanLeader = null;
	static int i;
	public ClanLoader() {
	
	}	/**
	*@function - Loads all the teams into an arraylist.
	* Note - This should be rewritten and optimiazed, - Patrick 10/4/2010
	*
	*/

	public static void loadClans() {

	}
	
}