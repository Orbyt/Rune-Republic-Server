package com.rs.worldserver.content.skill;

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

/**
 * Skill constants
 * 
 * @author Graham
 * 
 */
public class SkillConstants {

	public final static int ATTACK = 0;
	public final static int DEFENCE = 1;
	public final static int STRENGTH = 2;
	public final static int HITPOINTS = 3;
	public final static int RANGE = 4;
	public final static int PRAYER = 5;
	public final static int MAGIC = 6;
	public final static int COOKING = 7;
	public final static int WOODCUTTING = 8;
	public final static int FLETCHING = 9;
	public final static int FISHING = 10;
	public final static int FIREMAKING = 11;
	public final static int CRAFTING = 12;
	public final static int SMITHING = 13;
	public final static int MINING = 14;
	public final static int HERBLORE = 15;
	public final static int AGILITY = 16;
	public final static int THIEVING = 17;
	public final static int SLAYER = 18;
	public final static int FARMING = 19;
	public final static int RUNECRAFTING = 20;

	public final static int SKILLS_COUNT = 21;

	public final static String[] SKILL_NAMES = { "attack", "defence",
			"strength", "hitpoints", "range", "prayer", "magic", "cooking",
			"woodcutting", "fletching", "fishing", "firemaking", "crafting",
			"smithing", "mining", "herblore", "agility", "thieving", "slayer",
			"farming", "runecrafting" };

	public final static String[] PRE_SKILL_NAMES = { "an", "a", "a", "a", "a",
			"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "an", "a",
			"a", "a", "a" };
	public final static int[] SKILL_LEVEL_UP_INTERFACES = { 6247, 6253, 6206,
			6216, 4443, 6242, 6211, 6226, 4272, 6231, 6258, 4282, 6263, 6221,
			4416, 6237, 4277, 4261, 12122,4261, 4267 };

}
