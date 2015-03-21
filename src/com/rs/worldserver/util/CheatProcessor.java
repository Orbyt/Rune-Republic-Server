package com.rs.worldserver.util;


import java.io.*;
import java.util.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Config;


public class CheatProcessor {
	private static List<String> cheats = new ArrayList<String>();

	private CheatProcessor() {
	}
	public static void loadCheats() {
		try {
			BufferedReader cheatsRead = new BufferedReader(new FileReader("config/rights/cheats.conf"));
			String s;
			while((s = cheatsRead.readLine()) != null) {
				cheats.add(s);
			}
			cheatsRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Loaded "+ cheats.size() +" cheaters .");
	}
	public static boolean isCheat(String name) {
		name = name.toLowerCase();
		return cheats.contains(name);
	}
	public static void addCheat(String name) {
		name = name.toLowerCase();
		cheats.add(name);
		save("cheats");
	}
	public static void cheatUser(String name) throws IOException {
		name = name.toLowerCase();
		cheats.add(name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/cheats.conf", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static boolean checkCheat(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/rights/cheats.conf"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equalsIgnoreCase(name))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading cheats user: " + name);
			return false;
		}
		return false;
	}
	public static void save(String filename) {
		try {	
			BufferedWriter b = new BufferedWriter(new FileWriter("config/rights/cheats.conf"));
			if(filename.equalsIgnoreCase("cheats")) {
				for(String s : cheats) {
					b.write(s);
					b.newLine();
				}
			} 
			b.flush();
			b.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());	
		}
	}
}
