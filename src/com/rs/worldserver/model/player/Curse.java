package com.rs.worldserver.model.player;

import com.rs.worldserver.Config;
import com.rs.worldserver.Server;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.*;
import com.rs.worldserver.model.player.*;

public class Curse {

	private Client c;
	public Curse(Client c) {
		this.c = c;
	}

	public void resetCurse() {
		for(int p = 0; p < c.curseActive.length; p++) {
			c.curseActive[p] = false;
			c.getActionAssistant().sendFrame36(c.CURSE_GLOW[p], 0);
		}
		c.headIcon = -1;
		c.getActionAssistant().requestUpdates();
	}

	public void strCurse(int i) {
		for (int j = 0; j < str.length; j++) {
			if (str[j] != i) {
				c.curseActive[str[j]] = false;
				c.getActionAssistant().sendFrame36(c.CURSE_GLOW[str[j]], 0);
			}								
		}
	}
	public void atkCurse(int i) {
		for (int j = 0; j < atk.length; j++) {
			if (atk[j] != i) {
				c.curseActive[atk[j]] = false;
				c.getActionAssistant().sendFrame36(c.CURSE_GLOW[atk[j]], 0);
			}						
		}
	}
	public void defCurse(int i) {
		for (int j = 0; j < def.length; j++) {
			if (def[j] != i) {
				c.curseActive[def[j]] = false;
				c.getActionAssistant().sendFrame36(c.CURSE_GLOW[def[j]], 0);
			}								
		}
	}
	public void rngCurse(int i) {
		for (int j = 0; j < rng.length; j++) {
			if (rng[j] != i) {
				c.curseActive[rng[j]] = false;
				c.getActionAssistant().sendFrame36(c.CURSE_GLOW[rng[j]], 0);
			}								
		}
	}
	public void mgeCurse(int i) {
		for (int j = 0; j < mge.length; j++) {
			if (mge[j] != i) {
				c.curseActive[mge[j]] = false;
				c.getActionAssistant().sendFrame36(c.CURSE_GLOW[mge[j]], 0);
			}								
		}
	}
	public void sprtCurse(int i) {
		for (int j = 0; j < sprt.length; j++) {
			if (sprt[j] != i) {
				c.curseActive[sprt[j]] = false;
				c.getActionAssistant().sendFrame36(c.CURSE_GLOW[sprt[j]], 0);
			}								
		}
	}

	public int[] def = {13, 19};
	public int[] str = {14, 19};
	public int[] atk = {1, 10, 19};
	public int[] rng = {2, 11, 19};
	public int[] mge = {3, 12, 19};
	public int[] sprt = {4, 16, 19};//spirit

	public void activateCurse(int i) {
		if(c.duelRule[7]) {
			resetCurse();
			c.getActionAssistant().sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		if (c.playerLevel[1] < 30) {
			c.getActionAssistant().sendFrame36(c.CURSE_GLOW[i], 0);
			c.getActionAssistant().sendMessage("You need 30 Defence to use this prayer.");
			return;
		}
		//0 = pItem//1 = sapWar//2 = sapRng//3 = sapMge//4 = sapSprt
		//5 = berserk//6 = defSum//7 = defMge//8 = defRng//9 = defMel
		//10 = leechAtk//11 = leechRng//12 = leechMge//13 = leechDef//14 = leechStr
		//15 = leechEnrgy//16 = leechSpec//17 = wrath//18 = soul//19 = turmoil

		if(c.playerLevel[5] > 0 || !Config.PRAYER_POINTS_REQUIRED) {
			if(c.getActionAssistant().getLevelForXP(c.playerXP[5]) >= c.CURSE_LEVEL_REQUIRED[i] || !Config.PRAYER_LEVEL_REQUIRED) {
				boolean headIcon = false;
				switch(i) {
				case 0://pItem
					if(c.curseActive[0] == false) {
						//c.curseActive[0] = true;
						c.getActionAssistant().startAnimation(12567);
						c.gfx0(2213);
						//c.getActionAssistant().sendFrame36(c.CURSE_GLOW[0], 1);
						//c.lastProtItem = System.currentTimeMillis();
					} else {
						//c.curseActive[0] = false;
						//c.getActionAssistant().sendFrame36(c.CURSE_GLOW[0], 0);
					}

					break;

				case 1:
				case 10:
					if (c.curseActive[i] == false) {
						atkCurse(i); // FUNKAR LÅT VA!
					}
					break;

				case 2:
				case 11:
					if (c.curseActive[i] == false) {
						rngCurse(i); // FUNKAR!! LÅT VA!!!
					}
					break;

				case 3:
				case 12:
					if (c.curseActive[i] == false) {
						mgeCurse(i); // FUNKAR LÅT STÅ!
					}
					break;

				case 4:
				case 16:
					if (c.curseActive[i] == false) {
						sprtCurse(i); // FUNKAR!!
					}
					break;
				case 5:
					if(!c.zerkOn) {
						c.zerkOn = true;
						c.getActionAssistant().startAnimation(12589);
						c.gfx0(2266);
						c.getActionAssistant().sendMessage("You somehow feel your boosted stats will last longer.");
					} else {
						c.zerkOn = false;
					}
					break;

				case 13:
					if (c.curseActive[i] == false) {
						defCurse(i); // FUNKARRRR
					}
					break;
				case 14:
					if (c.curseActive[i] == false) {
						strCurse(i); // ALLL GOOOD
					}
					break;
				case 15:
					if (c.curseActive[i] == false) {
						c.curseActive[19] = false;
						c.getActionAssistant().sendFrame36(c.CURSE_GLOW[19], 0);
					}
					break;


				case 6:	
				case 7:					
				case 8:
				case 9:
					if(c.prayWait > 0) {
						c.getActionAssistant().sendMessage("You have been injured and can't use this prayer!");
						c.getActionAssistant().sendFrame36(c.CURSE_GLOW[7], 0);
						c.getActionAssistant().sendFrame36(c.CURSE_GLOW[8], 0);
						c.getActionAssistant().sendFrame36(c.CURSE_GLOW[9], 0);
						return;
					}
					if (i == 7) {
						//c.protMageDelay = System.currentTimeMillis();
					} else if (i == 8){
						//c.protRangeDelay = System.currentTimeMillis();
					}else if (i == 9){ }
						//c.protMeleeDelay = System.currentTimeMillis();
				case 17:
				case 18:
					headIcon = true;		
					for(int p = 6; p < 19; p++) {
						if(i != p && p != 10 && p != 11 && p != 10 && p != 12 && p != 13 && p != 14 && p != 15 && p != 16) {
							c.curseActive[p] = false;
							c.getActionAssistant().sendFrame36(c.CURSE_GLOW[p], 0);
						}
					}
				break;

				case 19://Turmoil - DONE
					if (c.curseActive[i] == false) {
						c.getActionAssistant().startAnimation(12565);
						c.gfx0(2226);
						strCurse(i);
						atkCurse(i);
						defCurse(i);
						mgeCurse(i);
						rngCurse(i);
						sprtCurse(i);
						c.curseActive[15] = false;
						c.usingPrayer = false;
						c.getActionAssistant().sendFrame36(c.CURSE_GLOW[15], 0);
					}
					break;
				}
				if(!headIcon) {
					if(c.curseActive[i] == false) {
						c.curseActive[i] = true;
						c.getActionAssistant().sendFrame36(c.CURSE_GLOW[i], 1);					
					} else {
						c.curseActive[i] = false;
						c.getActionAssistant().sendFrame36(c.CURSE_GLOW[i], 0);
					}
				} else {
					if(c.curseActive[i] == false) {
						c.curseActive[i] = true;
						c.getActionAssistant().sendFrame36(c.CURSE_GLOW[i], 1);
						c.headIcon = c.CURSE_HEAD_ICONS[i];
						c.getActionAssistant().requestUpdates();
					} else {
						c.curseActive[i] = false;
						c.getActionAssistant().sendFrame36(c.CURSE_GLOW[i], 0);
						c.headIcon = -1;
						c.getActionAssistant().requestUpdates();
					}
				}
			} else {
				c.getActionAssistant().sendFrame36(c.CURSE_GLOW[i],0);
				c.getActionAssistant().sendFrame126("You need a @blu@Prayer level of "+c.CURSE_LEVEL_REQUIRED[i]+" to use "+c.CURSE_NAME[i]+".", 357);
				c.getActionAssistant().sendFrame126("Click here to continue", 358);
				c.getActionAssistant().sendFrame164(356);
			}
		} else {
			c.getActionAssistant().sendFrame36(c.CURSE_GLOW[i],0);
			c.getActionAssistant().sendMessage("You have run out of prayer points!");
		}
		if (c.curseActive[i]==true) c.protItem.put(System.currentTimeMillis(),"ON"); // ERIC
		else if (c.curseActive[i]==false) c.protItem.put(System.currentTimeMillis(),"OFF"); // ERIC
	}

}