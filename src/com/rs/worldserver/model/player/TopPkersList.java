package com.rs.worldserver.model.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;

/**
@author - Orbit
*/ 

public class TopPkersList {
	private Client c;
	public TopPkersList(Client c) {
		this.c = c;
	} 
	
	public static boolean isOnList(String name) {
		for (int spot = 0; spot < TopPker.getTopPker().size(); spot++) {
			if(TopPker.getTopPker().get(spot).getPlayerName().equalsIgnoreCase(name)){
				return true;
			}				
		}
		return false;
	}
	
	public static int getRating(String name){
		for (int spot = 0; spot < TopPker.getTopPker().size(); spot++) {
			if(TopPker.getTopPker().get(spot).getPlayerName().equalsIgnoreCase(name)){
				return TopPker.getTopPker().get(spot).getPlayerRating();
			}				
		}
		return 0;
	}
	public static void sortPkers(){
	   Comparator comparator = Collections.reverseOrder();
	   Collections.sort( TopPker.topPker,comparator);
	   for(int i = 0; i < TopPker.getTopPker().size(); i++){
					TopPker.getTopPker().set(i,new TopPkers(
					i+1,
					TopPker.getTopPker().get(i).getPlayerName(),
					TopPker.getTopPker().get(i).getPlayerKills(),
					TopPker.getTopPker().get(i).getPlayerDeaths(),
					TopPker.getTopPker().get(i).getPlayerRating()));
		}
	}
		public static void addPlayer(String playerName){
			TopPker.getTopPker().add(new TopPkers(TopPker.getTopPker().size(), playerName, 0, 0 , 0));
		}
		public static void recordKill(String playerName) {
			if(!isOnList(playerName)){
				 addPlayer(playerName);
			}
		for (int teams = 0; teams < TopPker.getTopPker().size(); teams++) {
			if (TopPker.getTopPker().get(teams).getPlayerName().equalsIgnoreCase(playerName)) {
					TopPker.getTopPker().set(teams ,new TopPkers(
					TopPker.getTopPker().get(teams).getID(),
					TopPker.getTopPker().get(teams).getPlayerName(),
					TopPker.getTopPker().get(teams).getPlayerKills() + 1,
					TopPker.getTopPker().get(teams).getPlayerDeaths(),
					TopPker.getTopPker().get(teams).getPlayerRating()));
					setRating();
					sortPkers();
					
					break;
			}
		}
	}
	
	public static void setRating(){
	   int rating = 0;
	   for(int i = 0; i < TopPker.getTopPker().size(); i++){
		    rating = (TopPker.getTopPker().get(i).getPlayerKills() * 50);
		    rating = rating - (TopPker.getTopPker().get(i).getPlayerDeaths() * 10);
            TopPker.getTopPker().set(i ,new TopPkers(
			TopPker.getTopPker().get(i).getID(),
			TopPker.getTopPker().get(i).getPlayerName(),
			TopPker.getTopPker().get(i).getPlayerKills(),
			TopPker.getTopPker().get(i).getPlayerDeaths(),
			rating));
            rating = 0;
	   }
   
    }
   
		public static void recordDeath(String playerName) {
			if(!isOnList(playerName)){
				 addPlayer(playerName);
			}
		for (int teams = 0; teams < TopPker.getTopPker().size(); teams++) {
			if (TopPker.getTopPker().get(teams).getPlayerName().equalsIgnoreCase(playerName)) {
					TopPker.getTopPker().set(teams ,new TopPkers(
					TopPker.getTopPker().get(teams).getID(),
					TopPker.getTopPker().get(teams).getPlayerName(),
					TopPker.getTopPker().get(teams).getPlayerKills(),
					TopPker.getTopPker().get(teams).getPlayerDeaths() + 1,
					TopPker.getTopPker().get(teams).getPlayerRating()));
					setRating();
					sortPkers();
					
					break;
			}
		}
	}

	

}
