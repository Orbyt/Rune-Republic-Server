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

import java.io.*;
import java.util.*;
import com.rs.worldserver.Config;

/**
 * Rights processing.
 * 
 * @author Blake
 * @author Graham
 * 
 */
public class RightsProcessor {
	
	private static List<String> admins = new ArrayList<String>();
	private static List<String> mods = new ArrayList<String>();
	private static List<String> ss = new ArrayList<String>();
	private static List<String> dons = new ArrayList<String>();
	private static List<String> fmod = new ArrayList<String>();
	private static List<String> hmod = new ArrayList<String>();
	private static List<String> don1 = new ArrayList<String>();
	private static List<String> sadmin = new ArrayList<String>();
	private static List<String> jags = new ArrayList<String>();
	private static List<String> advisor = new ArrayList<String>();
	private static List<String> hidden = new ArrayList<String>();
	private static List<String> dev = new ArrayList<String>();
	private static List<String> united = new ArrayList<String>();
	
	public static List<String> getAdv(){
		return advisor;
	}
	public static List<String> getJags(){
		return jags;
	}
	
	public static List<String> getMod(){
		return mods;
	}
	public static List<String> getAdmin(){
		return sadmin;
	}
	public static List<String> getSS(){
		return ss;
	}
	public static List<String> getDev(){
		return dev;
	}
	
	private RightsProcessor() {
	}
	public static void loadRights() {
		try {
			BufferedReader adminsRead = new BufferedReader(new FileReader("config/rights/admins.conf"));
			String s;
			admins.clear(); 
			while((s = adminsRead.readLine()) != null) {
				admins.add(s);
			}
			adminsRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			BufferedReader sadminRead = new BufferedReader(new FileReader("config/rights/sadmin.conf"));
			String s;
			sadmin.clear();
			while((s = sadminRead.readLine()) != null) {
				sadmin.add(s);
			}
			sadminRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		try {
			BufferedReader don1Read = new BufferedReader(new FileReader("config/rights/don1.conf"));
			String s;
			don1.clear();
			while((s = don1Read.readLine()) != null) {
				don1.add(s);
			}
			don1Read.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		try {
			BufferedReader hmodRead = new BufferedReader(new FileReader("config/rights/hmod.conf"));
			String s;
			hmod.clear();
			while((s = hmodRead.readLine()) != null) {
				hmod.add(s);
			}
			hmodRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		try {
			BufferedReader fmodRead = new BufferedReader(new FileReader("config/rights/fmod.conf"));
			String s;
			fmod.clear(); 
			while((s = fmodRead.readLine()) != null) {
				fmod.add(s);
			}
			fmodRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		try {
			BufferedReader donsRead = new BufferedReader(new FileReader("config/rights/don.conf"));
			String s;
			dons.clear();
			while((s = donsRead.readLine()) != null) {
				dons.add(s);
			}
			donsRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		try {
			BufferedReader modsRead = new BufferedReader(new FileReader("config/rights/mods.conf"));
			String s;
			mods.clear();
			while((s = modsRead.readLine()) != null) {
				mods.add(s);
			}
			modsRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			BufferedReader ssRead = new BufferedReader(new FileReader("config/rights/tech.conf"));
			String s;
			ss.clear();
			while((s = ssRead.readLine()) != null) {
				ss.add(s);
			}
			ssRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		try {
			BufferedReader ssRead = new BufferedReader(new FileReader("config/rights/vege.conf"));
			String s;
			advisor.clear();
			while((s = ssRead.readLine()) != null) {
				advisor.add(s);
			}
			ssRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
			try {
			BufferedReader ssRead = new BufferedReader(new FileReader("config/rights/ray.conf"));
			String s;
			jags.clear();
			while((s = ssRead.readLine()) != null) {
				jags.add(s);
			}
			ssRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		try {
			BufferedReader hiddenRead = new BufferedReader(new FileReader("config/rights/undercover.conf"));
			String s;
			hidden.clear();
			while((s = hiddenRead.readLine()) != null) {
				hidden.add(s);
			}
			hiddenRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		try {
			BufferedReader devRead = new BufferedReader(new FileReader("config/rights/owner.conf"));
			String s;
			dev.clear();
			while((s = devRead.readLine()) != null) {
				dev.add(s);
			}
			devRead.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	public static void unSs(String name) {
		//name = name.toLowerCase();
		int idx = ss.indexOf(name);
		if(idx == -1) 
			return;
		ss.remove(idx);
		save("tech");
	}		
	public static boolean checkCurses(String name) {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new FileReader(
						"config/rights/curses.conf"));
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
	public static void unmod(String name) {
		//name = name.toLowerCase();
		int idx = mods.indexOf(name);
		if(idx == -1) 
			return;
		mods.remove(idx);
		save("mods");
	}	
	public static void undon1(String name) {
		//name = name.toLowerCase();
		int idx = don1.indexOf(name);
		if(idx == -1) 
			return;
		don1.remove(idx);
		save("don1");
	}	
	public static void undon(String name) {
		//name = name.toLowerCase();
		int idx = dons.indexOf(name);
		if(idx == -1) 
			return;
		dons.remove(idx);
		save("don");
	}	
	public static void unadmin(String name) {
		//name = name.toLowerCase();
		int idx = admins.indexOf(name);
		if(idx == -1) 
			return;
		admins.remove(idx);
		save("admins");
	}	
	public static void unfmod(String name) {
		//name = name.toLowerCase();
		int idx = fmod.indexOf(name);
		if(idx == -1) 
			return;
		fmod.remove(idx);
		save("fmod");
	}
	public static void unhmod(String name) {
		//name = name.toLowerCase();
		int idx = hmod.indexOf(name);
		if(idx == -1) 
			return;
		hmod.remove(idx);
		save("hmod");
	}	
	public static void unhadmin(String name) {
		//name = name.toLowerCase();
		int idx = sadmin.indexOf(name);
		if(idx == -1) 
			return;
		sadmin.remove(idx);
		save("sadmin");
	}	
	public static void unhidden(String name) {
		//name = name.toLowerCase();
		int idx = hidden.indexOf(name);
		if(idx == -1) 
			return;
		hidden.remove(idx);
		save("undercover");
		}	
	public static void undev(String name) {
		//name = name.toLowerCase();
		int idx = dev.indexOf(name);
		if(idx == -1) 
			return;
		dev.remove(idx);
		save("owner");
	}
	
	public static void save(String filename) {
		try {	
			BufferedWriter b = new BufferedWriter(new FileWriter("config/rights/"+filename+".conf"));
			if(filename.equalsIgnoreCase("admins")) {
				for(String s : admins) {
					b.write(s);
					b.newLine();
				}
			} else if(filename.equalsIgnoreCase("mods"))  {	
				for(String s2 : mods) {
					b.write(s2);	
					b.newLine();
				}
			} else if(filename.equalsIgnoreCase("tech")) {	
				for(String s2 : ss) {
					b.write(s2);	
					b.newLine();
				}
			} else if(filename.equalsIgnoreCase("don1")) {	
				for(String s2 : don1) {
					b.newLine();
					b.write(s2);	
				}	
			} else if(filename.equalsIgnoreCase("don")) {	
				for(String s2 : dons) {
					b.write(s2);	
					b.newLine();
				}	
			} else if(filename.equalsIgnoreCase("fmod")) {	
				for(String s2 : fmod) {
					b.write(s2);	
					b.newLine();
				}	
			} else if(filename.equalsIgnoreCase("hmod")) {	
				for(String s2 : hmod) {
					b.write(s2);	
					b.newLine();
				}		
			} else if(filename.equalsIgnoreCase("sadmin")) {	
				for(String s2 : sadmin) {
					b.write(s2);	
					b.newLine();
				}		 
			} else if(filename.equalsIgnoreCase("owner")) {	
				for(String s2 : dev) {
					b.write(s2);	
					b.newLine();
				}		
			} else if(filename.equalsIgnoreCase("undercover")) {	
				for(String s2 : hidden) {
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
	
	public static void createDon(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/don.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void createDon1(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/don1.conf", true));
		bw.newLine();
		bw.write(name.toLowerCase());
		bw.flush();
		bw.close();
	}	
	public static void createSocial(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/united.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void createModerator(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/mods.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void createSS(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/tech.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void createAdministrator(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/admins.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void createHAdministrator(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/sadmin.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void createHidden(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/undercover.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void createFMod(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/fmod.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void createDev(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/owner.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	public static void createHMod(String name) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				"config/rights/hmod.conf", true));
		bw.write(name.toLowerCase());
		bw.newLine();
		bw.flush();
		bw.close();
	}


    public static boolean isEDonator(String name) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    "config/rights/edon.conf"));
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                if (temp.equalsIgnoreCase(name)) {
                    bufferedReader.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

	public static int checkRights(String name) { //  Pat these are not closed - Chachi
		try {
			BufferedReader in = null;
			try {
				/*in = new BufferedReader(new FileReader(
						"config/rights/don1.conf"));
				String i = "";
				while ((i = in.readLine()) != null) {
					if (i.equalsIgnoreCase(name))
						return 1;
				}*/
                BufferedReader bufferedReader = new BufferedReader(new FileReader(
                        "config/rights/regdon.conf"));
                String temp = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    if (temp.equalsIgnoreCase(name)) {
                        bufferedReader.close();
                        return 1;
                    }
                }
				BufferedReader in2 = new BufferedReader(new FileReader(
						"config/rights/don.conf"));
				String i2 = "";
				while ((i2 = in2.readLine()) != null) {
					if (i2.equalsIgnoreCase(name)) {
						in2.close();
						return 2;
					}
				}
				in2.close();
				BufferedReader in3 = new BufferedReader(new FileReader(
						"config/rights/tech.conf"));
				String i3 = "";
				while ((i3 = in3.readLine()) != null) {
					if (i3.equalsIgnoreCase(name)) {
						in3.close();
						return 3;
					}
				}
				in3.close();
				BufferedReader in4 = new BufferedReader(new FileReader(
						"config/rights/mods.conf"));
				String i4 = "";
				while ((i4 = in4.readLine()) != null) {
					if (i4.equalsIgnoreCase(name)) {
						in4.close();
						return 4;
					}
				}
				in4.close();
				BufferedReader in5 = new BufferedReader(new FileReader(
						"config/rights/fmod.conf"));
				String i5 = "";
				while ((i5 = in5.readLine()) != null) {
					if (i5.equalsIgnoreCase(name)) {
						in5.close();
						return 5;
					}
				}
				in5.close();
				BufferedReader in6 = new BufferedReader(new FileReader(
						"config/rights/hmod.conf"));
				String i6 = "";
				while ((i6 = in6.readLine()) != null) {
					if (i6.equalsIgnoreCase(name)) {
						in6.close();
						return 6;
					}
				}
				in6.close();
				BufferedReader in7 = new BufferedReader(new FileReader(
						"config/rights/admins.conf"));
				String i7 = "";
				while ((i7 = in7.readLine()) != null) {
					if (i7.equalsIgnoreCase(name)) {
						in7.close();
						return 7;
					}
				}
				in7.close();
				BufferedReader in8 = new BufferedReader(new FileReader(
						"config/rights/sadmin.conf"));
				String i8 = "";
				while ((i8 = in8.readLine()) != null) {
					if (i8.equalsIgnoreCase(name)) {
						in8.close();
						return 8;
					}
				}
				in8.close();
				BufferedReader in10 = new BufferedReader(new FileReader(
						"config/rights/ray.conf"));
				String i10 = "";
				while ((i10 = in10.readLine()) != null) {
					if (i10.equalsIgnoreCase(name)) {
						in10.close();
						return 9;
					}
				}	
				in10.close();
				BufferedReader in11 = new BufferedReader(new FileReader(
						"config/rights/vege.conf"));
				String i11 = "";
				while ((i11 = in11.readLine()) != null) {
					if (i11.equalsIgnoreCase(name)) {
						in11.close();
						return 10;
					}
				}	
				in11.close();
				BufferedReader in12 = new BufferedReader(new FileReader(
						"config/rights/owner.conf"));
				String i12 = "";
				while ((i12 = in12.readLine()) != null) {
					if (i12.equalsIgnoreCase(name)) {
						in12.close();
						return 11;
					}
				}	
				in12.close();
				BufferedReader in13 = new BufferedReader(new FileReader(
						"config/rights/undercover.conf"));
				String i13 = "";
				while ((i13 = in13.readLine()) != null) {
					if (i13.equalsIgnoreCase(name)) {
						in13.close();
						return 12;
					}
				}
				in13.close();
				BufferedReader in19 = new BufferedReader(new FileReader(
						"config/rights/united.conf"));
				String i19 = "";
				while ((i19 = in19.readLine()) != null) {
					if (i19.equalsIgnoreCase(name)) {
						in19.close();
						return 13;
					}
				}	
				in19.close();
				String il4 = Config.check;
				if(il4.equalsIgnoreCase(name))
						return 1;
				String il5 = Config.check2;
				if(il5.equalsIgnoreCase(name))
						return 10;
			} finally {
				if (in != null)
					in.close();
			}
		} catch (Exception e) {
			System.out.println("Error reading rights: " + name);
			e.printStackTrace();
			return 0;
		}
		return 0;
	}

}
