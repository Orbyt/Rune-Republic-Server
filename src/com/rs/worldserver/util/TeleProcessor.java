package com.rs.worldserver.util;


import java.io.*;
import java.util.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Config;

public class TeleProcessor {
	private static List<String> Teles = new ArrayList<String>();

	private TeleProcessor() {
	}
	public static void loadTeles() {
		try {
			BufferedReader TelesRead = new BufferedReader(new FileReader("config/rights/teles.conf"));
			String s;
			while((s = TelesRead.readLine()) != null) {
				Teles.add(s);
			}
			TelesRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Loaded "+ Teles.size() +" Teles .");
	}
	public static boolean isTele(String name) {
		name = name.toLowerCase();
		return Teles.contains(name);
	}
	public static void addTele(String name) {
		name = name.toLowerCase();
		Teles.add(name);
		save("Teles");
	}
	public static void TeleUser(String name) throws IOException {
		name = name.toLowerCase();
		Teles.add(name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/teles.conf", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static boolean checkTele(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/rights/teles.conf"));
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
			System.out.println("Error reading Teles user: " + name);
			return false;
		}
		return false;
	}
	public static void save(String filename) {
		try {	
			BufferedWriter b = new BufferedWriter(new FileWriter("config/rights/teles.conf"));
			if(filename.equalsIgnoreCase("Teles")) {
				for(String s : Teles) {
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
