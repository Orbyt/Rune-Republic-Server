package com.rs.worldserver.model.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Config;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.Server;
import com.rs.worldserver.util.Misc;
/**
@Author - Orbit
DiceManager.java
*/

public class DiceManager {
	
	public static final int ROLL_TIMER = 1000, DICE_BAG = 15084;

	interface Data {
		public int diceId();
		public int diceSize();
		public int diceGfx();
	}
	
	enum Dice implements Data {
		DIE_6_SIDES(15086, 6, 2072),
		DICE_6_SIDES(15088, 12, 2074),
		DIE_8_SIDES(15090, 8, 2071),
		DIE_10_SIDES(15092, 10, 2070),
		DIE_12_SIDES(15094, 12, 2073),
		DIE_20_SIDES(15096, 20, 2068),
		DICE_UP_TO_100(15098, 100, 2075), //15098
		DIE_4_SIDES(15100, 4, 2069);
		
		private int id, sides, gfx;
		
		Dice(int id, int sides, int gfx) {
			this.id = id;
			this.sides = sides;
			this.gfx = gfx;
		}
		
		@Override
		public int diceId() {
			return id;
		}

		@Override
		public int diceSize() {
			return sides;
		}

		@Override
		public int diceGfx() {
			return gfx;
		}	
	}
	
	/**
	 * Handles rolling of the dice.
	 * @param c
	 * 		The player.
	 * @param id
	 * 		The id of the dice.
	 * @param clan
	 * 		If the player is in a clan.
	 * @return
	 * 		Whether or not to roll the dice.
	 */
	public static boolean rollDice(Client c, int id) {
		if(c.lastRoll < System.currentTimeMillis()){
			c.lastRoll = System.currentTimeMillis()+ROLL_TIMER;
			for(Dice d: Dice.values()) {
				if(d.diceId() == id) {
					selfRoll(c, Misc.random(d.diceSize()-1)+1, d.diceId());
					return true;
				}
			}
		} else {
			for(Dice d: Dice.values()) {
				if(d.diceId() == id) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Handles the rolling of the dice to a player.
	 * @param c
	 * 		The player.
	 * @param roll
	 * 		What the player rolled on the dice.
	 * @param item
	 * 		The id of the dice.
	 */
	public static void selfRoll(Client c, int roll, int item) {
	for(Dice d: Dice.values()) {
	if (item == 15098 && roll >= 1) {
		c.forcedChat("[Official Dicer]: " +c.playerName+ " has rolled ["+roll+"%] on the percentile dice.");
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(d.diceGfx());
	} else if (item == 15086 && roll >= 1) {
		c.forcedChat("[Official Dicer]: " +c.playerName+ " has rolled ["+roll+"] on the "+ Server.getItemManager().getItemDefinition(item).getName() +".");
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(d.diceGfx());
	} else if (item == 15088 && roll >= 2) {
		c.forcedChat("[Official Dicer]: " +c.playerName+ " has rolled ["+roll+"] on the "+ Server.getItemManager().getItemDefinition(item).getName() +".");
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(d.diceGfx());
	} else if (item == 15090 && roll >= 1) {
		c.forcedChat("[Official Dicer]: " +c.playerName+ " has rolled ["+roll+"] on the "+ Server.getItemManager().getItemDefinition(item).getName() +".");
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(d.diceGfx());
	} else if (item == 15092 && roll >= 1) {
		c.forcedChat("[Official Dicer]: " +c.playerName+ " has rolled ["+roll+"] on the "+ Server.getItemManager().getItemDefinition(item).getName() +".");
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(d.diceGfx());
	} else if (item == 15094 && roll >= 1) {
		c.forcedChat("[Official Dicer]: " +c.playerName+ " has rolled ["+roll+"] on the "+ Server.getItemManager().getItemDefinition(item).getName() +".");
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(d.diceGfx());
	} else if (item == 15096 && roll >= 1) {
		c.forcedChat("[Official Dicer]: " +c.playerName+ " has rolled ["+roll+"] on the "+ Server.getItemManager().getItemDefinition(item).getName() +".");
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(d.diceGfx());
	} else if (item == 15100 && roll >= 1) {
		c.forcedChat("[Official Dicer]: " +c.playerName+ " has rolled ["+roll+"] on the "+ Server.getItemManager().getItemDefinition(item).getName() +".");
		c.getActionAssistant().startAnimation(11900);
		c.gfx0(d.diceGfx());
	}
	}
}
}