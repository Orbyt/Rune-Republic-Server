package com.rs.worldserver.util;


import java.io.*;
import java.util.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.Config;


public class BankProcessor {
	private static List<String> banks = new ArrayList<String>();

	private BankProcessor() {
	}
	public static void loadbanks() {
		try {
			BufferedReader banksRead = new BufferedReader(new FileReader("config/rights/banks.conf"));
			String s;
			while((s = banksRead.readLine()) != null) {
				banks.add(s);
			}
			banksRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Loaded "+ banks.size() +" banks .");
	}
	public static boolean isBank(String name) {
		name = name.toLowerCase();
		return banks.contains(name);
	}
	public static void addBank(String name) {
		name = name.toLowerCase();
		banks.add(name);
		save("banks");
	}
	public static void bankUser(String name) throws IOException {
		name = name.toLowerCase();
		banks.add(name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/banks.conf", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static boolean checkBank(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/rights/banks.conf"));
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
			System.out.println("Error reading banks user: " + name);
			return false;
		}
		return false;
	}
	public static boolean checkMM(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/rights/mm.conf"));
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
			System.out.println("Error reading banks user: " + name);
			return false;
		}
		return false;
	}
	public static void save(String filename) {
		try {	
			BufferedWriter b = new BufferedWriter(new FileWriter("config/rights/banks.conf"));
			if(filename.equalsIgnoreCase("banks")) {
				for(String s : banks) {
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
