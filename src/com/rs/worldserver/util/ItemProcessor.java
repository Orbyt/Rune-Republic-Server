package com.rs.worldserver.util;

import com.rs.worldserver.Config;
import java.io.*;
import java.util.*;
import com.rs.worldserver.util.Misc;


public class ItemProcessor {
	private static List<String> Items = new ArrayList<String>();

	private ItemProcessor() {
	}
	public static void loadItems() {
		try {
			BufferedReader ItemsRead = new BufferedReader(new FileReader("config/rights/Items.conf"));
			String s;
			while((s = ItemsRead.readLine()) != null) {
				Items.add(s);
			}
			ItemsRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Loaded "+ Items.size() +" Items .");
	}
	public static boolean isItem(String name) {
		name = name.toLowerCase();
		return Items.contains(name);
	}
	public static void addItem(String name) {
		name = name.toLowerCase();
		Items.add(name);
		save("Items");
	}
	public static void ItemUser(String name) throws IOException {
		name = name.toLowerCase();
		Items.add(name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/Items.conf", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static boolean checkItem(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/rights/Items.conf"));
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
			System.out.println("Error reading Items user: " + name);
			return false;
		}
		return false;
	}
	public static void save(String filename) {
		try {	
			BufferedWriter b = new BufferedWriter(new FileWriter("config/rights/Items.conf"));
			if(filename.equalsIgnoreCase("Items")) {
				for(String s : Items) {
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
