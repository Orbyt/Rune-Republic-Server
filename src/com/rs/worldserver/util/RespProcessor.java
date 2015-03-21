package com.rs.worldserver.util;

import com.rs.worldserver.Config;
import java.io.*;
import java.util.*;
import com.rs.worldserver.util.Misc;


public class RespProcessor {
	private static List<String> resps = new ArrayList<String>();

	private RespProcessor() {
	}
	public static void loadresps() {
		try {
			BufferedReader respsRead = new BufferedReader(new FileReader("config/rights/resps.conf"));
			String s;
			while((s = respsRead.readLine()) != null) {
				resps.add(s);
			}
			respsRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Loaded "+ resps.size() +" respers .");
	}
	public static boolean isresp(String name) {
		name = name.toLowerCase();
		return resps.contains(name);
	}
	public static void addresp(String name) {
		name = name.toLowerCase();
		resps.add(name);
		save("resps");
	}
	public static void respUser(String name) throws IOException {
		name = name.toLowerCase();
		resps.add(name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/resps.conf", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static boolean checkresp(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/rights/resps.conf"));
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
			System.out.println("Error reading resps user: " + name);
			return false;
		}
		return false;
	}
	public static void save(String filename) {
		try {	
			BufferedWriter b = new BufferedWriter(new FileWriter("config/rights/resps.conf"));
			if(filename.equalsIgnoreCase("resps")) {
				for(String s : resps) {
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
