package com.rs.worldserver.model.player;

import com.rs.worldserver.Config;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public final class WeaponSpeed	{

	private static WeaponSpeed instance = new WeaponSpeed();
	private Map<Integer, Integer> speedMap = new HashMap<Integer, Integer>(4000);
	
	private WeaponSpeed()	{
	}
	
	/**
	 * @return instance for this class
	 */
	public static WeaponSpeed getInstance()	{
		return instance;
	}	
	
	/**
	 * Gets the speed of a desired weapon
	 * @return the speed returned from the map
	 */
	public int getWeaponSpeed(final int item)	{
		if(speedMap.containsKey(item))
			return speedMap.get(item);
			switch(item){
			case 21744:
				return 3600;
			case 13899:
			case 13901:
			case 18357:
			return 2750;
			case 11730:
			return 2500;
			case 13902:
			case 13904:
			case 18351:
			case 18367:
			case 19784:
			case 13905:
			case 13907:
			return 3000;
			case 21371:
			//case 7901:
			//case 7822:
			case 18349:
			return 2250;
			case 18353:
			case 16423:
			case 16425:
			case 16909:
			case 18369:
			case 16907:
			return 3700;
		/*case 13660:
		case 13661:
		case 13662:
		case 13663:
		case 13664:
		case 13665:
		case 13666:
		case 13667:
		case 13668:
		case 13669:
		case 13670:
		case 13671:
		return 2400;*/
		default:
		return 2000;
		}
	}
	
	/**
	 * @return the speed map
	 */
	public Map<Integer, Integer> getSpeedMap()	{
		return speedMap;
	}
	
	/**
	 * Adds a weapon and speed to the Map
	 * @param weapon the weapon id added
	 * @param speed the speed of the weapon
	 */
	public void addSpeed(final int weapon, final int speed)	{
		if(!speedMap.containsKey(weapon))	{
			speedMap.put(weapon, speed);
		} else {
			System.out.println("Speed already added!");
		}
	}
	
	/**
	 * Replaces a weapon speed in the Map
	 * @param weapon the weapon id
	 * @param speed the new speed of the weapon
	 */
	public void replaceSpeed(final int weapon, final int speed)	{
		if(speedMap.containsKey(weapon))	{
			speedMap.put(weapon, speed);
		} else {
			System.out.println(weapon+" speed doesn't exist, use addSpeed(in, int) instease");
		}
	}
	
	static	{
		try	{
			String line;
			String[] var;
			BufferedReader in = new BufferedReader(new FileReader("config/Weapon Speeds.dat"));
			while((line = in.readLine()) != null)	{
				var = line.split(":");
				getInstance().speedMap.put(Integer.parseInt(var[0]), Integer.parseInt(var[1]));
				var = null;
			}
			System.out.println("Loaded "+getInstance().speedMap.size()+" weapon speeds.");
			line = null;
			in = null;
			var = null;
			System.gc();
		} catch(IOException e)	{
			e.printStackTrace();
		}
	}	
}
