package com.rs.worldserver.util;


import java.io.*;
import java.util.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Config;

public class DuelProcessor {
	private static List<String> duel = new ArrayList<String>();
	private static List<String> vets = new ArrayList<String>();
	private static List<String> bloodlust1 = new ArrayList<String>();
	private static List<String> bloodlust2 = new ArrayList<String>();
	private static List<String> bloodlust3 = new ArrayList<String>();
	private DuelProcessor() {
	}
	public static void loadduel() {
		try {
			BufferedReader duelRead = new BufferedReader(new FileReader("config/rights/duel.conf"));
			String s;
			while((s = duelRead.readLine()) != null) {
				duel.add(s);
			}
			duelRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Loaded "+ duel.size() +" duel .");
	}
	
	public static void loadBL1() {
		try {
			BufferedReader duelRead = new BufferedReader(new FileReader("config/rights/bloodlust1.conf"));
			String s;
			while((s = duelRead.readLine()) != null) {
				s = s.toLowerCase();
				bloodlust1.add(s);
			}
			duelRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			BufferedReader duelReadd = new BufferedReader(new FileReader("config/rights/veterans.conf"));
			String se;
			while((se = duelReadd.readLine()) != null) {
				vets.add(se);
			}
			duelReadd.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Loaded "+ bloodlust1.size() +" bl1 . " +vets.size() +" also");
	}
	
		public static void loadBL2() {
		try {
			BufferedReader duelRead = new BufferedReader(new FileReader("config/rights/bloodlust2.conf"));
			String s;
			while((s = duelRead.readLine()) != null) {
				s = s.toLowerCase();
				bloodlust2.add(s);
			}
			duelRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Loaded "+ bloodlust2.size() +" bl2 .");
	}
	
		public static void loadBL3() {
		loadduel();
		try {
			BufferedReader duelRead = new BufferedReader(new FileReader("config/rights/bloodlust3.conf"));
			String s;
			while((s = duelRead.readLine()) != null) {
				s = s.toLowerCase();
				bloodlust3.add(s);
			}
			duelRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Loaded "+ bloodlust3.size() +" bl1 .");
	}
	public static boolean isBL1(String name) {
		name = name.toLowerCase();
		return bloodlust1.contains(name);
	}
	public static boolean isBL2(String name) {
		name = name.toLowerCase();
		return bloodlust2.contains(name);
	}
	public static boolean isBL3(String name) {
		name = name.toLowerCase();
		return bloodlust3.contains(name);
	}
	public static boolean isduel(String name) {
		name = name.toLowerCase();
		return duel.contains(name);
	}
	public static boolean isVet(String name) {
		name = name.toLowerCase();
		return vets.contains(name);
	}
	public static void addduel(String name) {
		name = name.toLowerCase();
		duel.add(name);
		save("duel");
	}
	public static void duelUser(String name) throws IOException {
		name = name.toLowerCase();
		duel.add(name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/duel.conf", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static boolean checkDuel(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/rights/duel.conf"));
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
			System.out.println("Error reading duel user: " + name);
			return false;
		}
		return false;
	}
	public static void save(String filename) {
		try {	
			BufferedWriter b = new BufferedWriter(new FileWriter("config/rights/duel.conf"));
			if(filename.equalsIgnoreCase("duel")) {
				for(String s : duel) {
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
