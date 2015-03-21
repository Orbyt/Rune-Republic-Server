package com.rs.worldserver.content.skill;

import com.rs.worldserver.Server;
import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;

/**
 * Herblore skill handler
 * 
 * @author Chachi
 * 
 */
/*
 * Class Pots
 *
 * Revision 1
 *
 * November 02, 2009
 *
 * By: Hunter (Drake)
 */

public class Pots {
    public static boolean correct = false;
    public static int newPot = 0;
    public static int newOther = 0;
    public static int dose = 0;
	
    public static void combine(Client c, int pot1, int pot2) {
        switch(pot1) {
		case 15309://3
                if(pot2 == 15309) {//3
                    correct = true;
                    newPot = 15308;//4
                    newOther = 15310;//2
                    dose = 4;
                } else if(pot2 == 15310) {//2
                    correct = true;
                    newPot = 15308;//4
                    newOther = 15311;//1
                    dose = 4;
                } else if(pot2 == 15311) {//1
                    correct = true;
                    newPot = 15308;//4
                    newOther = 229;//0
                    dose = 4;
                }
                break;
			case 15310://2
                if(pot2 == 15309) {//3
                    correct = true;
                    newPot = 15308;//4
                    newOther = 15311;//1
                    dose = 4;
                } else if(pot2 == 15310) {//2
                    correct = true;
                    newPot = 15308;//4
                    newOther = 229;//0
                    dose = 4;
                } else if(pot2 == 15311) {//1
                    correct = true;
                    newPot = 15309;//3
                    newOther = 229;
                    dose = 3;
                }
                break;
			case 15311://1
                if(pot2 == 15309) {//3
                    correct = true;
                    newPot = 15308;//4
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 15310) {//2
                    correct = true;
                    newPot = 15309;//3
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 15311) {//1
                    correct = true;
                    newPot = 15310;//2
                    newOther = 229;
                    dose = 2;
                }
                break;
			case 15313://3
                if(pot2 == 15313) {//3
                    correct = true;
                    newPot = 15312;//4
                    newOther = 15314;//2
                    dose = 4;
                } else if(pot2 == 15314) {//2
                    correct = true;
                    newPot = 15312;//4
                    newOther = 15315;//1
                    dose = 4;
                } else if(pot2 == 15315) {//1
                    correct = true;
                    newPot = 15312;//4
                    newOther = 229;//0
                    dose = 4;
                }
                break;
			case 15314://2
                if(pot2 == 15313) {//3
                    correct = true;
                    newPot = 15312;//4
                    newOther = 15315;//1
                    dose = 4;
                } else if(pot2 == 15314) {//2
                    correct = true;
                    newPot = 15312;//4
                    newOther = 229;//0
                    dose = 4;
                } else if(pot2 == 15315) {//1
                    correct = true;
                    newPot = 15313;//3
                    newOther = 229;
                    dose = 3;
                }
                break;
			case 15315://1
                if(pot2 == 15313) {//3
                    correct = true;
                    newPot = 15312;//4
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 15314) {//2
                    correct = true;
                    newPot = 15313;//3
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 15315) {//1
                    correct = true;
                    newPot = 15314;//2
                    newOther = 229;
                    dose = 2;
                }
                break;
					case 15317://3
                if(pot2 == 15317) {//3
                    correct = true;
                    newPot = 15316;//4
                    newOther = 15318;//2
                    dose = 4;
                } else if(pot2 == 15318) {//2
                    correct = true;
                    newPot = 15316;//4
                    newOther = 15319;//1
                    dose = 4;
                } else if(pot2 == 15319) {//1
                    correct = true;
                    newPot = 15316;//4
                    newOther = 229;//0
                    dose = 4;
                }
                break;
			case 15318://2
                if(pot2 == 15317) {//3
                    correct = true;
                    newPot = 15316;//4
                    newOther = 15319;//1
                    dose = 4;
                } else if(pot2 == 15318) {//2
                    correct = true;
                    newPot = 15316;//4
                    newOther = 229;//0
                    dose = 4;
                } else if(pot2 == 15319) {//1
                    correct = true;
                    newPot = 15317;//3
                    newOther = 229;
                    dose = 3;
                }
                break;
			case 15319://1
                if(pot2 == 15317) {//3
                    correct = true;
                    newPot = 15316;//4
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 15318) {//2
                    correct = true;
                    newPot = 15317;//3
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 15319) {//1
                    correct = true;
                    newPot = 15318;//2
                    newOther = 229;
                    dose = 2;
                }
                break;
				

            case 117:
                if(pot2 == 115) {
                    correct = true;
                    newPot = 113;
                    newOther = 119;
                    dose = 4;
                } else if(pot2 == 117) {
                    correct = true;
                    newPot = 113;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 119) {
                    correct = true;
                    newPot = 115;
                    newOther = 229;
                    dose = 3;
                }
                break;
					case 15321://3
                if(pot2 == 15321) {//3
                    correct = true;
                    newPot = 15320;//4
                    newOther = 15322;//2
                    dose = 4;
                } else if(pot2 == 15322) {//2
                    correct = true;
                    newPot = 15320;//4
                    newOther = 15323;//1
                    dose = 4;
                } else if(pot2 == 15323) {//1
                    correct = true;
                    newPot = 15320;//4
                    newOther = 229;//0
                    dose = 4;
                }
                break;
			case 15322://2
                if(pot2 == 15321) {//3
                    correct = true;
                    newPot = 15320;//4
                    newOther = 15323;//1
                    dose = 4;
                } else if(pot2 == 15322) {//2
                    correct = true;
                    newPot = 15320;//4
                    newOther = 229;//0
                    dose = 4;
                } else if(pot2 == 15323) {//1
                    correct = true;
                    newPot = 15321;//3
                    newOther = 229;
                    dose = 3;
                }
                break;
			case 15323://1
                if(pot2 == 15321) {//3
                    correct = true;
                    newPot = 15320;//4
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 15322) {//2
                    correct = true;
                    newPot = 15321;//3
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 15323) {//1
                    correct = true;
                    newPot = 15322;//2
                    newOther = 229;
                    dose = 2;
                }
                break;
				
					case 15325://3
                if(pot2 == 15325) {//3
                    correct = true;
                    newPot = 15324;//4
                    newOther = 15326;//2
                    dose = 4;
                } else if(pot2 == 15326) {//2
                    correct = true;
                    newPot = 15324;//4
                    newOther = 15327;//1
                    dose = 4;
                } else if(pot2 == 15327) {//1
                    correct = true;
                    newPot = 15324;//4
                    newOther = 229;//0
                    dose = 4;
                }
                break;
			case 15326://2
                if(pot2 == 15325) {//3
                    correct = true;
                    newPot = 15324;//4
                    newOther = 15327;//1
                    dose = 4;
                } else if(pot2 == 15326) {//2
                    correct = true;
                    newPot = 15324;//4
                    newOther = 229;//0
                    dose = 4;
                } else if(pot2 == 15327) {//1
                    correct = true;
                    newPot = 15325;//3
                    newOther = 229;
                    dose = 3;
                }
                break;
			case 15327://1
                if(pot2 == 15325) {//3
                    correct = true;
                    newPot = 15324;//4
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 15326) {//2
                    correct = true;
                    newPot = 15325;//3
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 15327) {//1
                    correct = true;
                    newPot = 15326;//2
                    newOther = 229;
                    dose = 2;
                }
                break;
				
				
				
				
				

				
            case 115:
                if(pot2 == 115) {
                    correct = true;
                    newPot = 113;
                    newOther = 117;
                    dose = 4;
                } else if(pot2 == 117) {
                    correct = true;
                    newPot = 113;
                    newOther = 119;
                    dose = 4;
                } else if(pot2 == 119) {
                    correct = true;
                    newPot = 113;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 119:
                if(pot2 == 115) {
                    correct = true;
                    newPot = 113;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 117) {
                    correct = true;
                    newPot = 115;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 119) {
                    correct = true;
                    newPot = 117;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 121:
                if(pot2 == 121) {
                    correct = true;
                    newPot = 2428;
                    newOther = 123;
                    dose = 4;
                } else if(pot2 == 123) {
                    correct = true;
                    newPot = 2428;
                    newOther = 125;
                    dose = 4;
                } else if(pot2 == 125) {
                    correct = true;
                    newPot = 2428;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 123:
                if(pot2 == 121) {
                    correct = true;
                    newPot = 2428;
                    newOther = 125;
                    dose = 4;
                } else if(pot2 == 123) {
                    correct = true;
                    newPot = 2428;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 125) {
                    correct = true;
                    newPot = 121;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 125:
                if(pot2 == 121) {
                    correct = true;
                    newPot = 2428;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 123) {
                    correct = true;
                    newPot = 121;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 125) {
                    correct = true;
                    newPot = 123;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 127:
                if(pot2 == 127) {
                    correct = true;
                    newPot = 2430;
                    newOther = 129;
                    dose = 4;
                } else if(pot2 == 129) {
                    correct = true;
                    newPot = 2430;
                    newOther = 131;
                    dose = 4;
                } else if(pot2 == 131) {
                    correct = true;
                    newPot = 2430;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 129:
                if(pot2 == 127) {
                    correct = true;
                    newPot = 2430;
                    newOther = 131;
                    dose = 4;
                } else if(pot2 == 129) {
                    correct = true;
                    newPot = 2430;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 131) {
                    correct = true;
                    newPot = 127;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 131:
                if(pot2 == 127) {
                    correct = true;
                    newPot = 2430;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 129) {
                    correct = true;
                    newPot = 127;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 131) {
                    correct = true;
                    newPot = 129;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 133:
                if(pot2 == 133) {
                    correct = true;
                    newPot = 2432;
                    newOther = 135;
                    dose = 4;
                } else if(pot2 == 135) {
                    correct = true;
                    newPot = 2432;
                    newOther = 137;
                    dose = 4;
                } else if(pot2 == 137) {
                    correct = true;
                    newPot = 2432;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 135:
                if(pot2 == 133) {
                    correct = true;
                    newPot = 2432;
                    newOther = 119;
                    dose = 4;
                } else if(pot2 == 135) {
                    correct = true;
                    newPot = 2432;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 137) {
                    correct = true;
                    newPot = 133;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 137:
                if(pot2 == 133) {
                    correct = true;
                    newPot = 2432;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 135) {
                    correct = true;
                    newPot = 133;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 137) {
                    correct = true;
                    newPot = 135;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 139:
                if(pot2 == 139) {
                    correct = true;
                    newPot = 2434;
                    newOther = 141;
                    dose = 4;
                } else if(pot2 == 141) {
                    correct = true;
                    newPot = 2434;
                    newOther = 143;
                    dose = 4;
                } else if(pot2 == 143) {
                    correct = true;
                    newPot = 2434;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 141:
                if(pot2 == 139) {
                    correct = true;
                    newPot = 2434;
                    newOther = 143;
                    dose = 4;
                } else if(pot2 == 141) {
                    correct = true;
                    newPot = 2434;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 143) {
                    correct = true;
                    newPot = 139;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 143:
                if(pot2 == 139) {
                    correct = true;
                    newPot = 2434;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 141) {
                    correct = true;
                    newPot = 139;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 143) {
                    correct = true;
                    newPot = 141;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 145:
                if(pot2 == 145) {
                    correct = true;
                    newPot = 2436;
                    newOther = 147;
                    dose = 4;
                } else if(pot2 == 147) {
                    correct = true;
                    newPot = 2436;
                    newOther = 149;
                    dose = 4;
                } else if(pot2 == 149) {
                    correct = true;
                    newPot = 2436;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 147:
                if(pot2 == 145) {
                    correct = true;
                    newPot = 2436;
                    newOther = 149;
                    dose = 4;
                } else if(pot2 == 147) {
                    correct = true;
                    newPot = 2436;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 149) {
                    correct = true;
                    newPot = 145;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 149:
                if(pot2 == 145) {
                    correct = true;
                    newPot = 2436;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 147) {
                    correct = true;
                    newPot = 145;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 149) {
                    correct = true;
                    newPot = 147;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 151:
                if(pot2 == 151) {
                    correct = true;
                    newPot = 2438;
                    newOther = 153;
                    dose = 4;
                } else if(pot2 == 153) {
                    correct = true;
                    newPot = 2438;
                    newOther = 155;
                    dose = 4;
                } else if(pot2 == 155) {
                    correct = true;
                    newPot = 2438;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 153:
                if(pot2 == 151) {
                    correct = true;
                    newPot = 2438;
                    newOther = 155;
                    dose = 4;
                } else if(pot2 == 153) {
                    correct = true;
                    newPot = 2438;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 155) {
                    correct = true;
                    newPot = 151;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 155:
                if(pot2 == 151) {
                    correct = true;
                    newPot = 2438;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 153) {
                    correct = true;
                    newPot = 151;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 155) {
                    correct = true;
                    newPot = 153;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 157:
                if(pot2 == 157) {
                    correct = true;
                    newPot = 2440;
                    newOther = 159;
                    dose = 4;
                } else if(pot2 == 159) {
                    correct = true;
                    newPot = 2440;
                    newOther = 161;
                    dose = 4;
                } else if(pot2 == 161) {
                    correct = true;
                    newPot = 2440;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 159:
                if(pot2 == 157) {
                    correct = true;
                    newPot = 2440;
                    newOther = 161;
                    dose = 4;
                } else if(pot2 == 159) {
                    correct = true;
                    newPot = 2440;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 161) {
                    correct = true;
                    newPot = 157;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 161:
                if(pot2 == 157) {
                    correct = true;
                    newPot = 2440;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 159) {
                    correct = true;
                    newPot = 157;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 161) {
                    correct = true;
                    newPot = 159;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 163:
                if(pot2 == 163) {
                    correct = true;
                    newPot = 2442;
                    newOther = 165;
                    dose = 4;
                } else if(pot2 == 165) {
                    correct = true;
                    newPot = 2442;
                    newOther = 167;
                    dose = 4;
                } else if(pot2 == 167) {
                    correct = true;
                    newPot = 2442;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 165:
                if(pot2 == 163) {
                    correct = true;
                    newPot = 2442;
                    newOther = 167;
                    dose = 4;
                } else if(pot2 == 165) {
                    correct = true;
                    newPot = 2442;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 167) {
                    correct = true;
                    newPot = 163;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 167:
                if(pot2 == 163) {
                    correct = true;
                    newPot = 2442;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 165) {
                    correct = true;
                    newPot = 163;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 167) {
                    correct = true;
                    newPot = 165;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 169:
                if(pot2 == 169) {
                    correct = true;
                    newPot = 2444;
                    newOther = 171;
                    dose = 4;
                } else if(pot2 == 171) {
                    correct = true;
                    newPot = 2444;
                    newOther = 173;
                    dose = 4;
                } else if(pot2 == 173) {
                    correct = true;
                    newPot = 2444;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 171:
                if(pot2 == 169) {
                    correct = true;
                    newPot = 2444;
                    newOther = 173;
                    dose = 4;
                } else if(pot2 == 171) {
                    correct = true;
                    newPot = 2444;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 173) {
                    correct = true;
                    newPot = 169;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 173:
                if(pot2 == 169) {
                    correct = true;
                    newPot = 2444;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 171) {
                    correct = true;
                    newPot = 169;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 173) {
                    correct = true;
                    newPot = 171;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 175:
                if(pot2 == 175) {
                    correct = true;
                    newPot = 2446;
                    newOther = 177;
                    dose = 4;
                } else if(pot2 == 177) {
                    correct = true;
                    newPot = 2446;
                    newOther = 179;
                    dose = 4;
                } else if(pot2 == 179) {
                    correct = true;
                    newPot = 2446;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 177:
                if(pot2 == 175) {
                    correct = true;
                    newPot = 2446;
                    newOther = 179;
                    dose = 4;
                } else if(pot2 == 177) {
                    correct = true;
                    newPot = 2446;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 179) {
                    correct = true;
                    newPot = 175;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 179:
                if(pot2 == 175) {
                    correct = true;
                    newPot = 2446;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 177) {
                    correct = true;
                    newPot = 175;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 179) {
                    correct = true;
                    newPot = 177;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 181:
                if(pot2 == 181) {
                    correct = true;
                    newPot = 2448;
                    newOther = 183;
                    dose = 4;
                } else if(pot2 == 183) {
                    correct = true;
                    newPot = 2448;
                    newOther = 185;
                    dose = 4;
                } else if(pot2 == 185) {
                    correct = true;
                    newPot = 2448;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 183:
                if(pot2 == 181) {
                    correct = true;
                    newPot = 2448;
                    newOther = 185;
                    dose = 4;
                } else if(pot2 == 183) {
                    correct = true;
                    newPot = 2448;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 185) {
                    correct = true;
                    newPot = 181;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 185:
                if(pot2 == 181) {
                    correct = true;
                    newPot = 2448;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 183) {
                    correct = true;
                    newPot = 181;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 185) {
                    correct = true;
                    newPot = 183;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 2454:
                if(pot2 == 2454) {
                    correct = true;
                    newPot = 2452;
                    newOther = 2456;
                    dose = 4;
                } else if(pot2 == 2456) {
                    correct = true;
                    newPot = 2452;
                    newOther = 2458;
                    dose = 4;
                } else if(pot2 == 2458) {
                    correct = true;
                    newPot = 2452;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 2456:
                if(pot2 == 2454) {
                    correct = true;
                    newPot = 2452;
                    newOther = 2458;
                    dose = 4;
                } else if(pot2 == 2456) {
                    correct = true;
                    newPot = 2452;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 2458) {
                    correct = true;
                    newPot = 2454;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 2458:
                if(pot2 == 2454) {
                    correct = true;
                    newPot = 2452;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 2456) {
                    correct = true;
                    newPot = 2454;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 2458) {
                    correct = true;
                    newPot = 2456;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 3010:
                if(pot2 == 3010) {
                    correct = true;
                    newPot = 3008;
                    newOther = 3012;
                    dose = 4;
                } else if(pot2 == 3012) {
                    correct = true;
                    newPot = 3008;
                    newOther = 3014;
                    dose = 4;
                } else if(pot2 == 3014) {
                    correct = true;
                    newPot = 3008;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 3012:
                if(pot2 == 3010) {
                    correct = true;
                    newPot = 3008;
                    newOther = 3014;
                    dose = 4;
                } else if(pot2 == 3012) {
                    correct = true;
                    newPot = 3008;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3014) {
                    correct = true;
                    newPot = 3010;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 3014:
                if(pot2 == 3010) {
                    correct = true;
                    newPot = 3008;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3012) {
                    correct = true;
                    newPot = 3010;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 3014) {
                    correct = true;
                    newPot = 3012;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 3018:
                if(pot2 == 3018) {
                    correct = true;
                    newPot = 3016;
                    newOther = 3020;
                    dose = 4;
                } else if(pot2 == 3020) {
                    correct = true;
                    newPot = 3016;
                    newOther = 3022;
                    dose = 4;
                } else if(pot2 == 3022) {
                    correct = true;
                    newPot = 3016;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 3020:
                if(pot2 == 3018) {
                    correct = true;
                    newPot = 3016;
                    newOther = 3022;
                    dose = 4;
                } else if(pot2 == 3020) {
                    correct = true;
                    newPot = 3016;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3022) {
                    correct = true;
                    newPot = 3018;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 3022:
                if(pot2 == 3018) {
                    correct = true;
                    newPot = 3016;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3020) {
                    correct = true;
                    newPot = 3018;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 3022) {
                    correct = true;
                    newPot = 3020;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 3026:
                if(pot2 == 3026) {
                    correct = true;
                    newPot = 3024;
                    newOther = 3028;
                    dose = 4;
                } else if(pot2 == 3028) {
                    correct = true;
                    newPot = 3024;
                    newOther = 3030;
                    dose = 4;
                } else if(pot2 == 3030) {
                    correct = true;
                    newPot = 3024;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 3028:
                if(pot2 == 3026) {
                    correct = true;
                    newPot = 3024;
                    newOther = 3030;
                    dose = 4;
                } else if(pot2 == 3028) {
                    correct = true;
                    newPot = 3024;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3030) {
                    correct = true;
                    newPot = 3026;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 3030:
                if(pot2 == 3026) {
                    correct = true;
                    newPot = 3024;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3028) {
                    correct = true;
                    newPot = 3026;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 3030) {
                    correct = true;
                    newPot = 3028;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 3034:
                if(pot2 == 3034) {
                    correct = true;
                    newPot = 3032;
                    newOther = 3036;
                    dose = 4;
                } else if(pot2 == 3036) {
                    correct = true;
                    newPot = 3032;
                    newOther = 3038;
                    dose = 4;
                } else if(pot2 == 3038) {
                    correct = true;
                    newPot = 3032;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 3036:
                if(pot2 == 3034) {
                    correct = true;
                    newPot = 3032;
                    newOther = 3038;
                    dose = 4;
                } else if(pot2 == 3036) {
                    correct = true;
                    newPot = 3032;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3038) {
                    correct = true;
                    newPot = 3034;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 3038:
                if(pot2 == 3034) {
                    correct = true;
                    newPot = 3032;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3036) {
                    correct = true;
                    newPot = 3034;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 3038) {
                    correct = true;
                    newPot = 3036;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 3042:
                if(pot2 == 3042) {
                    correct = true;
                    newPot = 3040;
                    newOther = 3044;
                    dose = 4;
                } else if(pot2 == 3044) {
                    correct = true;
                    newPot = 3040;
                    newOther = 3046;
                    dose = 4;
                } else if(pot2 == 3046) {
                    correct = true;
                    newPot = 3040;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 3044:
                if(pot2 == 3042) {
                    correct = true;
                    newPot = 3040;
                    newOther = 3046;
                    dose = 4;
                } else if(pot2 == 3044) {
                    correct = true;
                    newPot = 3040;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3046) {
                    correct = true;
                    newPot = 3042;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 3046:
                if(pot2 == 3042) {
                    correct = true;
                    newPot = 3040;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 3044) {
                    correct = true;
                    newPot = 3042;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 3046) {
                    correct = true;
                    newPot = 3044;
                    newOther = 229;
                    dose = 2;
                }
                break;
            case 6687:
                if(pot2 == 6687) {
                    correct = true;
                    newPot = 6685;
                    newOther = 6689;
                    dose = 4;
                } else if(pot2 == 6689) {
                    correct = true;
                    newPot = 6685;
                    newOther = 6691;
                    dose = 4;
                } else if(pot2 == 6691) {
                    correct = true;
                    newPot = 6685;
                    newOther = 229;
                    dose = 4;
                }
                break;
            case 6689:
                if(pot2 == 6687) {
                    correct = true;
                    newPot = 6685;
                    newOther = 6691;
                    dose = 4;
                } else if(pot2 == 6689) {
                    correct = true;
                    newPot = 6685;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 6691) {
                    correct = true;
                    newPot = 6687;
                    newOther = 229;
                    dose = 3;
                }
                break;
            case 6691:
                if(pot2 == 6687) {
                    correct = true;
                    newPot = 6685;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 6689) {
                    correct = true;
                    newPot = 6687;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 6691) {
                    correct = true;
                    newPot = 6689;
                    newOther = 229;
                    dose = 2;
                }
                break;
			case 189:
                if(pot2 == 191) {
                    correct = true;
                    newPot = 2450;
                    newOther = 193;
                    dose = 4;
                } else if(pot2 == 193) {
                    correct = true;
                    newPot = 2450;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 189) {
                    correct = true;
                    newPot = 2450;
                    newOther = 191;
                    dose = 2;
                }
			break;	
			case 191:
                if(pot2 == 191) {
                    correct = true;
                    newPot = 2450;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 193) {
                    correct = true;
                    newPot = 2450;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 189) {
                    correct = true;
                    newPot = 2450;
                    newOther = 193;
                    dose = 2;
                }
			break;	
			case 193:
                if(pot2 == 191) {
                    correct = true;
                    newPot = 189;
                    newOther = 229;
                    dose = 4;
                } else if(pot2 == 193) {
                    correct = true;
                    newPot = 191;
                    newOther = 229;
                    dose = 3;
                } else if(pot2 == 189) {
                    correct = true;
                    newPot = 2450;
                    newOther = 229;
                    dose = 2;
                }
			break;			
            default:
                c.getActionAssistant().sendMessage("Unknown Potion ID.");
                break;
        }
		if(dose == 1 || dose == 0){
			correct = false;
		}
        if(correct) {
            c.getActionAssistant().deleteItem(pot1, c.getActionAssistant().getItemSlot(pot1), 1);
            c.getActionAssistant().deleteItem(pot2, c.getActionAssistant().getItemSlot(pot2), 1);
            c.getActionAssistant().addItem(newPot, 1);
            c.getActionAssistant().addItem(newOther, 1);
            c.getActionAssistant().sendMessage("You have combined the liquid into " +dose+ " doses.");
        } else {
            c.getActionAssistant().sendMessage("You cannot combine these potions.");
            return;
        }
		newPot = 0;
		newOther = 0;
		dose = 0;
		correct = false;
    }
}