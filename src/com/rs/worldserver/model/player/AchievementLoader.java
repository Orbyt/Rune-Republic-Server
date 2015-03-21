package com.rs.worldserver.model.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Config;
/**
@Author - Patrick McDonald
AchievementLoader.java

This will parse all the cfg files for achievements and store them in the respective arraylist from Achievements.

*/

public class AchievementLoader {
	private Client client;
	static Scanner dIn = null;
	static int length = 0;
	static String line = null;
	static int divider;
	static int id;
	static String Name = null;
	static String Description = null;
	static String ItemAmounts = null;
	static String Items = null;
	static int Points;
	static int i;
	public AchievementLoader() {

	}
	public static void loadAchievements(){
		loadCombat();
		loadPk();
		loadSkilling();
		loadMonster();
		loadMisc();
	}
	public static void loadCombat() {
		int Counter = 0;
		try {
			dIn = new Scanner(new BufferedReader(new FileReader("config/achievements/combat.cfg"))); // load combat.cfg
			while (dIn.hasNextLine()) {
				line = dIn.nextLine();
				length = line.length();
				if (length != 0) {
					if (line.startsWith("[Achievement]")) {
						Counter++;
						id = -1;
					} else if (line.startsWith("Id")) {
						divider = line.indexOf("=");
						id = Integer.parseInt(line.substring(divider + 2, line.length()));
					} else if (line.startsWith("Name")) {
						divider = line.indexOf("=");
						Name = line.substring(divider + 2, line.length());
					}
					else if (line.startsWith("Description")) {
						divider = line.indexOf("=");
						Description = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Points")) {
						divider = line.indexOf("=");
						Points = Integer.parseInt(line.substring(
						divider + 2, line.length()));
					} else if (line.startsWith("Items")) {
						divider = line.indexOf("=");
						Items = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Amount")) {
						divider = line.indexOf("=");
						ItemAmounts = line.substring(divider + 2, line.length());
						System.out.println("Added Combat " + ItemAmounts);
					} else if (line.startsWith("[End]")) {
					AchievementManager.getCombat().add(new Achievements(id, Name,Description,Points,Items,ItemAmounts)); // add into our
					// array list.
					i = 0;
					}
				}
			}	
			dIn.close();
			dIn = null;
			System.out.println("Loaded " +Counter+ " Combat Achievements!");
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
	}

	public static void loadPk() {
		int Counter = 0;
		try {
			dIn = new Scanner(new BufferedReader(new FileReader("config/achievements/pking.cfg"))); // load pking.cfg
			while (dIn.hasNextLine()) {
				line = dIn.nextLine();
				length = line.length();
				if (length != 0) {
					if (line.startsWith("[Achievement]")) {
						Counter++;
						id = -1;
					} else if (line.startsWith("Id")) {
						divider = line.indexOf("=");
						id = Integer.parseInt(line.substring(divider + 2, line.length()));
					} else if (line.startsWith("Name")) {
						divider = line.indexOf("=");
						Name = line.substring(divider + 2, line.length());
					}
					else if (line.startsWith("Description")) {
						divider = line.indexOf("=");
						Description = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Points")) {
						divider = line.indexOf("=");
						Points = Integer.parseInt(line.substring(
						divider + 2, line.length()));
					} else if (line.startsWith("Items")) {
						divider = line.indexOf("=");
						Items = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Amount")) {
						divider = line.indexOf("=");
						ItemAmounts = line.substring(divider + 2, line.length());
						System.out.println("Added Pking " + ItemAmounts);
					} else if (line.startsWith("[End]")) {
					AchievementManager.getPking().add(new Achievements(id, Name,Description,Points,Items,ItemAmounts)); // add into our
					// array list.
					i = 0;
					}
				}
			}
			dIn.close();
			System.out.println("Loaded " +Counter+ " Pking Achievements!");
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
	}

	public static void loadSkilling() {
		int Counter = 0;
		try {
			dIn = new Scanner(new BufferedReader(new FileReader("config/achievements/skilling.cfg"))); // load skilling.cfg
			while (dIn.hasNextLine()) {
				line = dIn.nextLine();
				length = line.length();
				if (length != 0) {
					if (line.startsWith("[Achievement]")) {
						Counter++;
						id = -1;
					} else if (line.startsWith("Id")) {
						divider = line.indexOf("=");
						id = Integer.parseInt(line.substring(divider + 2, line.length()));
					} else if (line.startsWith("Name")) {
						divider = line.indexOf("=");
						Name = line.substring(divider + 2, line.length());
					}
					else if (line.startsWith("Description")) {
						divider = line.indexOf("=");
						Description = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Points")) {
						divider = line.indexOf("=");
						Points = Integer.parseInt(line.substring(
						divider + 2, line.length()));
					} else if (line.startsWith("Items")) {
						divider = line.indexOf("=");
						Items = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Amount")) {
						divider = line.indexOf("=");
						ItemAmounts = line.substring(divider + 2, line.length());
						System.out.println("Added Skilling " + ItemAmounts);
					} else if (line.startsWith("[End]")) {
					AchievementManager.getSkilling().add(new Achievements(id, Name,Description,Points,Items,ItemAmounts)); // add into our
					// array list.
					i = 0;
					}
				}
			}
			dIn.close();
			dIn = null;
			System.out.println("Loaded " +Counter+ " Skilling Achievements!");
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
	}

	public static void loadMonster() {
		int Counter = 0;
		try {
			dIn = new Scanner(new BufferedReader(new FileReader("config/achievements/monster.cfg"))); // load monster.cfg
			while (dIn.hasNextLine()) {
				line = dIn.nextLine();
				length = line.length();
				if (length != 0) {
					if (line.startsWith("[Achievement]")) {
						Counter++;
						id = -1;
					} else if (line.startsWith("Id")) {
						divider = line.indexOf("=");
						id = Integer.parseInt(line.substring(divider + 2, line.length()));
					} else if (line.startsWith("Name")) {
						divider = line.indexOf("=");
						Name = line.substring(divider + 2, line.length());
					}
					else if (line.startsWith("Description")) {
						divider = line.indexOf("=");
						Description = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Points")) {
						divider = line.indexOf("=");
						Points = Integer.parseInt(line.substring(
						divider + 2, line.length()));
					} else if (line.startsWith("Items")) {
						divider = line.indexOf("=");
						Items = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Amount")) {
						divider = line.indexOf("=");
						ItemAmounts = line.substring(divider + 2, line.length());
						System.out.println("Added Monster " + ItemAmounts);
					} else if (line.startsWith("[End]")) {
					AchievementManager.getMonster().add(new Achievements(id, Name,Description,Points,Items,ItemAmounts)); // add into our
					// array list.
					i = 0;
					}
				}
			}
			dIn.close();
			dIn = null;
			System.out.println("Loaded " +Counter+ " Monster Killing Achievements!");
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
	}

	public static void loadMisc() {
		int Counter = 0;
		try {
			dIn = new Scanner(new BufferedReader(new FileReader("config/achievements/misc.cfg"))); // load misc.cfg
			while (dIn.hasNextLine()) {
				line = dIn.nextLine();
				length = line.length();
				if (length != 0) {
					if (line.startsWith("[Achievement]")) {
						Counter++;
						id = -1;
					} else if (line.startsWith("Id")) {
						divider = line.indexOf("=");
						id = Integer.parseInt(line.substring(divider + 2, line.length()));
					} else if (line.startsWith("Name")) {
						divider = line.indexOf("=");
						Name = line.substring(divider + 2, line.length());
					}
					else if (line.startsWith("Description")) {
						divider = line.indexOf("=");
						Description = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Points")) {
						divider = line.indexOf("=");
						Points = Integer.parseInt(line.substring(
						divider + 2, line.length()));
					} else if (line.startsWith("Items")) {
						divider = line.indexOf("=");
						Items = line.substring(divider + 2, line.length());
					} else if (line.startsWith("Amount")) {
						divider = line.indexOf("=");
						ItemAmounts = line.substring(divider + 2, line.length());
						//System.out.println("Added Misc" + ItemAmounts);
					} else if (line.startsWith("[End]")) {
					AchievementManager.getMisc().add(new Achievements(id, Name,Description,Points,Items,ItemAmounts)); // add into our
					// array list.
					i = 0;
					}
				}
			}
			dIn.close();
			dIn = null;
			System.out.println("Loaded " +Counter+ " Misc Achievements!");
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		}
	}
}
