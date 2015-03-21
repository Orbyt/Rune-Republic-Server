package com.rs.worldserver.model.player;
import java.util.ArrayList;


public class AchievementManager {
	
public static ArrayList<Achievements> combatAchievement = new ArrayList<Achievements>();
public static ArrayList<Achievements> skillingAchievement = new ArrayList<Achievements>();
public static ArrayList<Achievements> monsterAchievement = new ArrayList<Achievements>();
public static ArrayList<Achievements> miscAchievement = new ArrayList<Achievements>();
public static ArrayList<Achievements> pkingAchievement = new ArrayList<Achievements>();
	public static ArrayList<Achievements> getCombat() {
    	      return combatAchievement;
	}
	public static ArrayList<Achievements> getSkilling() {
    	      return skillingAchievement;
	}
	public static ArrayList<Achievements> getMonster() {
    	      return monsterAchievement;
	}
	public static ArrayList<Achievements> getMisc() {
    	      return miscAchievement;
	}
	public static ArrayList<Achievements> getPking() {
    	      return pkingAchievement;
	}

}
