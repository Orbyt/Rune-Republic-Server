package com.rs.worldserver.model.player;

import java.util.HashMap;
import java.util.Map;

import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.FloorItem;
import com.rs.worldserver.model.ItemDefinition;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCDefinition;
import com.rs.worldserver.world.AnimationManager;
import com.rs.worldserver.world.StillGraphicsManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.util.*;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.player.packet.Attack;

public class FightCave {

    /**
     * Lava bat
     */
    public static final int Level_22_1 = 2627;
    public static final int Level_22_2 = 2628;

    /**
     * Smaller lava guy
     */
    public static final int Level_44_Split_1 = 2738;

    /**
     * Big Lava guys
     */
    public static final int Level_45_1 = 2629;
    public static final int Level_45_2 = 2630;

    /**
     *Rangers
     */
    public static final int Level_90_1 = 2631;
    public static final int Level_90_2 = 2632;

    /**
     * lizard
     */
    public static final int Level_180_1 = 2741;
    public static final int Level_180_2 = 2742;
    
    /**
     * mager
     */
    public static final int Level_360_1 = 2743;
    public static final int Level_360_2 = 2744;

    /**
     * Tokz-jad
     */
    public static final int Level_720 = 2745;

    /**
     *
     * Healers
     */
    public static final int Level_108 = 2746;
	
    /**
     *
     * Waves
     */

	public static int[] Tzhaar_Npcs = { 2627, 2628, 2738, 2629, 2630, 2631, 2632, 2741,
		2742, 2743, 2744, 2745, 2746};
	public static int[] Tzhaar_Attack = { 10,10,10,	20,20,60,60,90,	90,150,150,300,150
		};
	public static int[] Tzhaar_Defense = { 10,10,10,20	,20	,60,60,90,90,150,150,300,150
		};	
	public static int[] Tzhaar_Max = { 2,2,	2,8	,8,12,12,16,16,30,30,99,16
		};		
	public static final int[] Wave_1 = {Level_22_1};
	public static final int[] Wave_2 = {Level_22_1, Level_45_2};
	public static final int[] Wave_3 = {Level_22_1, Level_45_2, Level_90_1};
	public static final int[] Wave_4 = {Level_22_1, Level_45_2, Level_90_1, Level_180_1};
	public static final int[] Wave_5 = {Level_22_1, Level_45_2, Level_90_1, Level_180_1, Level_360_1};
	public static final int[] Wave_6 = {Level_360_1, Level_360_1};
	public static final int[] Wave_7 = {Level_720};
	public static final int[][] Spawn_Points = {
		{2388, 2394, 2406, 2412, 2398},
		{5097, 5087, 5084, 5109, 5073}
	};
	
    /**
     *
     * @return heightlevel
     */
    public static final int CaveHeight(Client c) {
        int h = 0;
        if(c != null) {
            h = c.playerId * 4;
        }
        return h;
    }
}