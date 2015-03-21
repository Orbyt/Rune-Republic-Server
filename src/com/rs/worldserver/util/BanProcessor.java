package com.rs.worldserver.util;

//Shard Revolutions Generic MMORPG Server
//Copyright (C) 2008  Graham Edgecombe

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import com.rs.worldserver.model.player.StringObject;

import java.io.*;
import java.util.*;

/**
 * Bans processing.
 * 
 * @author Blake
 * @author Graham
 * 
 */
public class BanProcessor {
	private static List<String> bans = new ArrayList<String>();
	private static List<String> mutes = new ArrayList<String>();
	private static List<String> permbans = new ArrayList<String>();
	//added
	private static List<String> ipbans = new ArrayList<String>();
	private static List<String> macbans = new ArrayList<String>();
	private static List<String> ipmutes = new ArrayList<String>();
	//end
	
	private BanProcessor() {
	}
	public static void loadPunishments() {
	/*	try {
			BufferedReader permbanRead = new BufferedReader(new FileReader("config/bans/permban.txt"));
			String s;
			permbans.clear(); //
			while((s = permbanRead.readLine()) != null) {
				permbans.add(s);
			}
			permbanRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		try {
			BufferedReader banRead = new BufferedReader(new FileReader("config/bans/accounts.txt"));
			String s;
			bans.clear(); //
			while((s = banRead.readLine()) != null) {
				bans.add(s);
			}
			banRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			BufferedReader muteRead = new BufferedReader(new FileReader("config/bans/mutes.txt"));
			String s;
			mutes.clear(); //
			while((s = muteRead.readLine()) != null) {
				mutes.add(s);
			}
			muteRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			BufferedReader ipmuteRead = new BufferedReader(new FileReader("config/bans/mutes2.txt"));
			String s;
			ipmutes.clear(); //
			while((s = ipmuteRead.readLine()) != null) {
				ipmutes.add(s);
			}
			ipmuteRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
//added
		try {
			BufferedReader ipbanRead = new BufferedReader(new FileReader("config/bans/ips.txt"));
			String s;
			ipbans.clear();
			while((s = ipbanRead.readLine()) != null) {
				ipbans.add(s);
			}
			ipbanRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		try {
			BufferedReader macbanRead = new BufferedReader(new FileReader("config/bans/macs.txt"));
			String s;
			macbans.clear();
			while((s = macbanRead.readLine()) != null) {
				macbans.add(s);
			}
			macbanRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		try {
			BufferedReader ipmuteRead = new BufferedReader(new FileReader("config/bans/mutes2.txt"));
			String s;
			ipmutes.clear();
			while((s = ipmuteRead.readLine()) != null) {
				ipmutes.add(s);
			}
			ipmuteRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
//end
		System.out.println("Loaded "+ bans.size() +" bans and "+mutes.size() +" mutes.");*/
	}

	
	public static void unban(String name) {
	/*	//loadPunishments();
		name = name.toLowerCase();
		//name = name.replaceAll(" ", "_");
		int idx = bans.indexOf(name);
		if(idx == -1) 
			return;
		bans.remove(idx);
		save("accounts");*/

	}
//added	
	public static void unIP(String name) {
		/*name = name.toLowerCase();
		int idx = ipbans.indexOf(name);
		if(idx == -1) 
			return;
		ipbans.remove(idx + 1);
		ipbans.remove(idx);
		save("ips");*/

	}

	public static void unMAC(String name) {
		/*name = name.toLowerCase();
		int idx = macbans.indexOf(name);
		if(idx == -1) 
			return;
		macbans.remove(idx + 1);
		macbans.remove(idx);
		save("macs");*/

	}
	
	public static void unipMute(String name) {
	/*	name = name.toLowerCase();
		int idx = ipmutes.indexOf(name);
		if(idx == -1) 
			return;
		ipmutes.remove(idx + 1);
		ipmutes.remove(idx);
		save("mutes2");*/
	}
	public static void unipMute2(String name) {
		int idx = ipmutes.indexOf(name);
		if(idx == -1) 
			return;
		ipmutes.remove(idx);
		save("mutes2");
	}
	
	public static void unMAC2(String name) {
		int idx = macbans.indexOf(name);
		if(idx == -1) 
			return;
		macbans.remove(idx);
		save("macs");
	}

	public static void unIP2(String name) {
		int idx = ipbans.indexOf(name);
		if(idx == -1) 
			return;
		ipbans.remove(idx);
		save("ips");
	}
//end
	
	public static void save(String filename) {
		try {	
			BufferedWriter b = new BufferedWriter(new FileWriter("config/bans/"+filename+".txt"));
			if(filename.equalsIgnoreCase("accounts")) {
				for(String s : bans) {
					b.write(s);
					b.newLine();
				}
			} 
//changed
			if(filename.equalsIgnoreCase("ips")) {
				for(String s : ipbans) {
					b.write(s);
					b.newLine();
				}
			}
			if(filename.equalsIgnoreCase("macs")) {
				for(String s : macbans) {
					b.write(s);
					b.newLine();
				}
			}
			if(filename.equalsIgnoreCase("mutes2")) {
				for(String s : ipmutes) {
					b.write(s);
					b.newLine();
				}
			}
//end
			else {	
				for(String s2 : mutes) {
					b.write(s2);	
					b.newLine();
				}
			}
			b.flush();
			b.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());	
		}
	}
		
	public static boolean isBanned(String name) {
		name = name.toLowerCase();
		//name = name.replaceAll(" ", "_");
		return bans.contains(name);
	}

	public static boolean isMuted(String  name) {
		name = name.toLowerCase();
		//name = name.replaceAll(" ", "_");
		return mutes.contains(name);
	}

	public static void addBan(String name) {
		name = name.toLowerCase();
		//name = name.replaceAll(" ", "_");
		bans.add(name);
		save("bans");
	}

	public static void addMute(String name) {
		name = name.toLowerCase();
		//name = name.replaceAll(" ", "_");
		mutes.add(name);
		save("mutes");
	}
	
	
	
	public static void muteUser(String name,String bannedBy) throws IOException {
		/*BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/bans/mutes.txt", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();*/
	}

	public static void yellMuteUser(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/bans/yellmutes.txt", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void pbanUser(String name) throws IOException {
		name = name.toLowerCase();
		//name = name.replaceAll(" ", "_");
		permbans.add(name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/bans/permban.txt", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void banUser(String name, String banner) throws IOException {
	/*
		name = name.toLowerCase();
		//name = name.replaceAll(" ", "_");
		bans.add(name);
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/bans/accounts.txt", true));
		bw.write(name);
		bw.newLine();
		bw.flush();
		bw.close();*/
	}

	public static void banIP(String ip,String bannedBy) throws IOException {
		ip = ip.toLowerCase(); //added
	/*	ipbans.add(ip); //added
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/bans/ips.txt", true));
		bw.write(ip);
		bw.newLine();
		bw.flush();
		bw.close();*/
	}

	public static void banUID(int uid) throws IOException {
		/*BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/bans/uids.txt", true));
		bw.write(String.valueOf(uid));
		bw.newLine();
		bw.flush();
		bw.close();*/
	}
	public static void banMAC(int uid, String name,String bannedBy) throws IOException {
		name = name.toLowerCase(); //added
		/*macbans.add(name); //added
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/bans/macs.txt", true));
		bw.write(name);	
		bw.newLine();		
		bw.write(String.valueOf(uid));
		bw.newLine();
		bw.flush();
		bw.close();*/
		String uid2 = String.valueOf(uid);
	}
	public static boolean checkUser(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/bans/accounts.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals(name))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned user: " + name);
			return false;
		}
		return false;
	}
	public static boolean checkFfa() {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/bans/stage1.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals("1"))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned user: ");
			return false;
		}
		return false;
	}
	public static boolean checkPort() {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/bans/stage2.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals("1"))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned user: ");
			return false;
		}
		return false;
	}
	
	public static boolean Banned(String name) {
	/*	try {
			BufferedReader in = new BufferedReader(new FileReader("config/bans/accounts.txt"));
			String Data = null;

			while ((Data = in.readLine()) != null) {
				if (name.equalsIgnoreCase(Data)) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;*/
		return false;
	}
	public static boolean checkPerm(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/bans/permban.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals(name))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned user: " + name);
			return false;
		}
		return false;
	}	
	
	public static boolean checkMuted(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/bans/mutes.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals(name))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned user: " + name);
			return false;
		}
		return false;
	}

	public static boolean checkIP(StringObject ip) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader("config/bans/ips.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (ip.equals(i))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned IP: " + ip);
			return false;
		}
		return false;
		/*if (VoteSql.checkIP(ip)) {
		return true;
		}
		return false;*/
	}

	public static boolean checkUID(int uid) {
	return false;/*
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(
						new FileReader("config/bans/uids.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals(String.valueOf(uid)))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned UID: " + uid);
			return false;
		}
		return false;*/
	}
	public static boolean checkFags(String name) {
	
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader("config/bans/fags2.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals(name.toLowerCase()))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned user: " + name);
			return false;
		}
		return false;
	}
	public static boolean checkBetaNames(String name) {
	
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader("config/beta/names.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equalsIgnoreCase(name.toLowerCase()))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned user: " + name);
			return false;
		}
		return false;
	}
	public static boolean checkMAC2(int mac) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(
						new FileReader("config/bans/fags.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals(String.valueOf(mac)))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned MAC: " + mac);
			return false;
		}
		return false;
	}
	public static boolean checkMAC(int mac) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(
						new FileReader("config/bans/macs.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals(String.valueOf(mac)))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned MAC: " + mac);
			return false;
		}
		return false;
		/*if (VoteSql.checkMAC(String.valueOf(mac))) {
		return true;
		}
		return false;*/
	}
	public static boolean checkYellMuted(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/bans/yellmutes.txt"));
				String i;
				while ((i = in.readLine()) != null) {
					if (i.equals(name))
						return true;
				}
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading banned user: " + name);
			return false;
		}
		return false;
	}

}