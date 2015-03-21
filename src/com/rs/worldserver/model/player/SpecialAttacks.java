package com.rs.worldserver.model.player;
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
import com.rs.worldserver.world.AnimationManager;
import com.rs.worldserver.world.StillGraphicsManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;


public class SpecialAttacks {

	private Client c;
	public SpecialAttacks(Client Client) {
		this.c = Client;

}
	public int[] cfg = {12323, 7574, 7599, 7549, 8493, 7499, 7576, 7474};

					
	public void specialBar() {
		boolean flag = false;
		for (int[] slot : SPECIAL) {
		//for(int x = 0; x < specialWeapons.length; x++) {
			if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == slot[0]){
			//if(c.playerEquipment[PlayerConstants.PLAYER_WEAPON] == specialWeapons[x]) {
				flag = true;
			}
		}
		for(int i = 0; i < cfg.length; i++) {
			c.getOutStream().createFrame(171);
			c.getOutStream().writeByte(flag ? 0 : 1);
			c.getOutStream().writeWord(cfg[i]);
			c.flushOutStream();
		}
		sendSpecial();
	}
	
	public void sendSpecial() {
	if(c.playerRights > 0) {
		int a = c.specialAmount;
		double b = (a/10);
			if(a < 0) {
				b = -1;
			}
			if(b < 10) {
				for(int i = (int)b; i <= 10 && i >= 0; i++) {
					fillSpecial(0, c.barId+i);

				}
			}

			if(b >= 10) { b = 9; fillSpecial(500, c.barId+9); }
			for(int fill = (int)b; fill >= 0 && fill <= 9 && b > 0; fill--) {
	
				fillSpecial(500, c.barId+fill);
			}
			if(c.specOn) {
				c.getActionAssistant().sendFrame126("@whi@SPECIAL ATTACK ("+c.specialAmount+"%)", c.barId+10);
			} else {
				c.getActionAssistant().sendFrame126("@blk@SPECIAL ATTACK ("+c.specialAmount+"%)", c.barId+10);
			}
	} else {
			int a = c.specialAmount;
			double b = (a/10);
			if(a < 0 || a > 100) {
				b = -1;
			}
			if(b < 10) {
				for(int i = (int)b; i <= 10 && i >= 0; i++) {
					fillSpecial(0, c.barId+i);
	
				}
			}

			if(b == 10) { b = 9; fillSpecial(500, c.barId+9); }
			for(int fill = (int)b; fill >= 0 && fill <= 9 && b > 0; fill--) {
	
				fillSpecial(500, c.barId+fill);
			}
			if(c.specOn) {
				c.getActionAssistant().sendFrame126("@whi@SPECIAL ATTACK ("+c.specialAmount+"%)", c.barId+10);
			} else {
				c.getActionAssistant().sendFrame126("@blk@SPECIAL ATTACK ("+c.specialAmount+"%)", c.barId+10);
			}
		}
	}
	public void fillSpecial(int i, int id) {
		c.getOutStream().createFrame(70);
		c.getOutStream().writeWord(i);
		c.getOutStream().writeWordBigEndian(0);
		c.getOutStream().writeWordBigEndian(id);
	}
	public boolean checkSpecial(int wep) {
		int x = c.specialAmount;
		double factor = 1;
							if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 19669) {
								 factor = 0.9;
							}
		for (int[] slot : SPECIAL) {
			if (wep == slot[0]){
				if(x >= slot[3] * factor) {
					return true;
				}	
			}	
		}
		return false;
	}
	
	public int getSpecialAnim(int wep) {
		for (int[] slot : SPECIAL) {
			if (wep == slot[0])
				return slot[1];
		}
		return -1;	
	}
	
	public int getSpecialGFX(int wep) {
		for (int[] slot : SPECIAL) {
			if (wep == slot[0])
				return slot[2];
		}
		return -1;
	}
	
	public int getSpecialDrain(int wep) {
		for (int[] slot : SPECIAL) {
			if (wep == slot[0])
				return slot[3];
		}
		return 25;	
	}
	public boolean doubleHit(int wep) {
		for (int[] slot : SPECIAL) {
			if (wep == slot[0]) {
				if (slot[4] == 1)
					return true;
			}
		}
		return false;	
	}
	

	public double getBonusDamage(int wep) {
		for (double[] slot : SPECIALDAMAGE) {
			if (wep == slot[0])
				return slot[1];
		}
		return 1.0;	
	}

	
	public static final double[][] SPECIALDAMAGE = {
		/*{ itemID, animation, gfx, drainamount, doublehit , bonus dmg}*/

		{ 5698, 1.2}, // d dagger (p++)
		{ 5680, 1.2}, // d dagger (p+)
		{ 1231, 1.2}, // d dagger (p)
		{ 1215, 1.2}, // d dagger
		{ 4587, 1.2}, //d scimitar
		{ 4151, 0.5}, //whip
		{ 7158, 1.0}, //d2h
		{ 3204, 0.5}, //d hally
		{ 1305, 1.3}, //d long
		{ 1434, 1.5}, //d mace
		{ 1377, 0}, //dba
		{ 861, 0.25}, //msb
		{ 11694, 1.5}, // ags
		{ 11696, 1.3}, //bgs
		{ 11698, 1.3}, //sgs
		{ 11700, 1.0}, //zgs
		{ 4153, 1.2}, // g maul
		{ 10887, 1.4}, //anchor
		{ 13660, 0}, //custom whip
		{ 13661, 0}, //custom whip
		{ 13662, 0}, //custom whip
		{ 13663, 0}, //custom whip
		{ 13664, 0}, //custom whip
		{ 13665, 0}, //custom whip
		{ 13666, 0}, //custom whip
		{ 13667, 0}, //custom whip
		{ 13668, 0}, //custom whip
		{ 13669, 0}, //custom whip
		{ 13670, 0}, //custom whip
		{ 13671, 0}, //custom whip
		{ 13902, 1.15}, //statius warhammer
		{ 13904, 1.15}, //statius warhammer (deg)
		{ 13905, 1.0}, //vesta spear
		{ 13907, 1.0}, //vesta spear (deg)
		{ 13883, 1.2}, //Morrigan's throwing axe
		{ 13879, 1.2}, //Morrigan's javelin
		{ 13899, 1.2}, //Vesta's longsword
		{ 13901, 1.3}, //Vesta's longsword (deg)
		{ 4220, 1.5}, // godbow
		{ 4221, 1.2}, // godbow
		{ 4222, 1.2}, // godbow
		{ 4223, 1.0}, // godbow
		{ 1249, 0}, //dragon spear 254 opp gfx
		{ 1263, 0}, //dragon spear(p) 254 opp gfx
		{ 6724, 1.5}, //seercull bow
		{ 11730, 1.0}, //saradomin sword
		{ 14484, 1.0}, //dragon claws //before 1.0
		{ 4827, 1.5}, //Dark bow
		{ 15704, 1.5}, //Dark bow
		{ 7806, 1.25}, //anger sword
		{ 7807, 1.25}, //anger battleaxe
		{ 7808, 1.25}, //anger mace
		{ 7822, 1.0}, //lava whip
		{ 7901, 1.5}, //lime whip
		{ 21371, 1.5}, //abby vine whip
		{ 21372, 1.5}, //abby vine whip
		{ 21373, 1.5}, //abby vine whip
		{ 21374, 1.5}, //abby vine whip
		{ 21375, 1.5}, //abby vine whip
		{ 15241, 1.5}, //handcannon  (Maybe put this double hit so its like rs?)
		{ 15486, 0}, // Staff of light
		{ 19784, 1.0},// Korasi
		{ 22406, 1.1},// Ancient mace
		{ 20171, 1.5},// Zaryte bow
	};
	
	public static int[][] SPECIAL = {
		/*{ itemID, animation, gfx, drainamount, doublehit }
			doublehit 1 for yes 0 for no
		*/
		{ 15486, 12804, 2319, 100, 0}, // staff of light, never reaches here anyway
		{ 5698, 1062, 252, 25, 1}, // d dagger (p++)
		{ 5680, 1062, 252, 25, 1}, // d dagger (p+)
		{ 1231, 1062, 252, 25, 1}, // d dagger (p)
		{ 1215, 1062, 252, 25, 1}, // d dagger
		{ 4587, 1872, 347, 60, 0}, //d scimitar
		{ 4151, 1658, -1, 50, 0}, //whip
		{ 21371, 1658, -1, 50, 0}, //abby vine whip
		{ 21372, 1658, -1, 50, 0}, //abby vine whip
		{ 21373, 1658, -1, 50, 0}, //abby vine whip
		{ 21374, 1658, -1, 50, 0}, //abby vine whip
		{ 21375, 1658, -1, 50, 0}, //abby vine whip
		{ 7158, 3157, 559, 50, 0}, //d2h
		{ 3204, 1203, 282, 25, 1}, //d hally
		{ 1305, 1058, 248, 25, 0}, //d long
		{ 1434, 1060, 251, 25, 0}, //d mace
		{ 1377, 1056, 246, 100, 0}, //dba
		{ 861, 1074, 250, 55, 1}, //msb
		{ 11694, 7074, 1222, 50, 0}, // ags
		{ 11696, 7073, 1223, 100, 0}, //bgs
		{ 11698, 7071, 1220, 50, 0}, //sgs
		{ 11700, 11978, 1221, 60, 0}, //zgs
		{ 4153, 1667, 340, 50, 0}, // g maul
		{ 10887, 5870, 1027, 50,0}, //anchor
		{ 13660, 1658, -1, 50, 0}, //custom whip
		{ 13661, 1658, -1, 50, 0}, //custom whip
		{ 13662, 1658, -1, 50, 0}, //custom whip
		{ 13663, 1658, -1, 50, 0}, //custom whip
		{ 13664, 1658, -1, 50, 0}, //custom whip
		{ 13665, 1658, -1, 50, 0}, //custom whip
		{ 13666, 1658, -1, 50, 0}, //custom whip
		{ 13667, 1658, -1, 50, 0}, //custom whip
		{ 13668, 1658, -1, 50, 0}, //custom whip
		{ 13669, 1658, -1, 50, 0}, //custom whip
		{ 13670, 1658, -1, 50, 0}, //custom whip
		{ 13671, 1658, -1, 50, 0}, //custom whip
		{ 13902, 10505, -1, 50, 0}, //statius warhammer
		{ 13904, 10505, -1, 50, 0}, //statius warhammer (deg)
		{ 13905, 10499, -1, 25, 0}, //vesta spear
		{ 13907, 10499, -1, 25, 0}, //vesta spear (deg)
		{ 13883, 10504, 1838, 25, 0}, //Morrigan's throwing axe
		{ 13879, 10501, 1836, 25, 0}, //Morrigan's javelin
		{ 13899, 10502, -1, 25, 0}, //Vesta's longsword
		{ 13901, 10502, -1, 25, 0}, //Vesta's longsword (deg)
		{ 4220, 426, 256, 100, 0}, // godbow
		{ 4221, 426, 256, 100, 0}, // godbow
		{ 4222, 426, 256, 100, 0}, // godbow
		{ 4223, 426, 256, 100, 0}, // godbow
		{ 1249, 1064, -1, 25, 0}, //dragon spear 254 opp gfx
		{ 1263, 1064, -1, 25, 0}, //dragon spear(p) 254 opp gfx
		{ 6724, 426, -1, 75, 0}, //seercull bow
		{ 11730, 7072, -1, 50, 1}, //saradomin sword
		{ 14484, 10961, 1950, 50, 1}, //dragon claws
		{ 4827, 426, 1099, 60, 1}, //Dark bow
		{ 15704, 426, 1099, 60, 1}, //Dark bow
		
		{ 7806, 2890, -1, 50, 0}, //anger sword
		{ 7807, 407, -1, 50, 0}, //anger battleaxe
		{ 7808, 2876, -1, 50, 0}, //anger mace
		{ 7822, 1658, -1, 50, 0}, //lava whip
		{ 7901, 1658, -1, 50, 0}, //lime whip
		{ 15241, 12175, 2141, 50, 1}, //handcannon  (Maybe put this double hit so its like rs?)
		{ 15486, 12804, 2320, 100, 0},// Staff of light
		{ 19784, 4000, 1247,60, 0}, // Korasi spec gfx on enemy is 1248
		{ 11716, 1064, -1, 25, 0,0}, //zamorakian spear 254 opp gfx
		{ 22406, 6147, 1052, 50, 0}, //ancient mace
		{ 20171, 426, 256, 50, 0} //zaryte bow
		
	};
}
