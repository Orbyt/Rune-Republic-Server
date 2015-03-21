package com.rs.worldserver.world;

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

import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;
import com.rs.worldserver.Config;
import com.rs.worldserver.util.Misc;

/**
 * Holds animation data.
 * 
 * @author Graham
 * 
 */
public class AnimationManager {

	/*
	 * Prevents an instance of this class being created.
	 */
	private AnimationManager() {
	}

	public static int getDeathAnimation(Client client) {
		return 0x900;
	}
	public static int getDefendAnimation(NPC npc) {
		switch (npc.getDefinition().getType()) {		
		case 8528:
		return 12700;
		case 14696:
		return 15470;
		case 10127:
		return 13170;
		case 10110:
		return 13000;
		case 7134:
		return 8755;
		case 9964:
		return 13716;
		case 9947:
		return 13771;
		case 9928:
		return 13335;
		case 5666:
		return 5897;
		case 3847:
		return 3990;
		case 8549:
		return 11077;
		case 6024:
		return 6538;
		case 297:
		return 7221;
		case 8281:
		return 10666;
		case 8283:
		return 10673;
		case 1532:
		return -1;
		case 8282:
		return 10670;
		case 78:
		return 4916;
		case 750:
		return 6728;
				case 118:
		case 119:
		case 120:
		case 121:
		return 100;
		case 3836:
		return 6237;
		case 6250:
		return 7017;
		case 6248:
		return 6375;
		case 9911://Har'Lakk the Riftsplitter
		return Config.anim;
		case 9780: //Rammernaut
		return 13698;
		case 9737://Night-gazer Khighorahk
		return 13420;
		case 9463://ICE STRYKEWYRM
		return 12792;
		case 9465://DESERT STRYKEWYRM
		return 12792;
		case 9467://JUNGLE STRYKEWYRM
		return 12792;
		case 4813://vyrewatch
		return Config.anim;
		case 5188://nial swiftling
		return Config.anim;
		case 5247://PENANCE QUEEN
				return 5408;
		case 8596://ava of creation
				return 11198;
		case 8597://ava of destruction
				return 11203;
		case 10665:
				return Config.anim;
		case 8133:
		return 10059;
		case 8366:
		return 10923;
			case 2631:
			case 2632:
				return 2629;
			case 2626:
			case 2627:
				return 2619;
			case 2629:
			case 2630:
			case 2738:
				return 2626;
			case 2744:
			case 2743:
				return 9268;
			case 2741:
			case 2742:
				return 9253;
			case 2745:
				return 9278;		
		
		
			case 2591:
				return 2606;
			case 2604:
			case 2610:
			case 2617:
			case 2619:
				return 2606;

			case 1612:
				return 1525;
			case 123:
			case 122:
				return 12406;
			case 90:
			case 91:
			case 92:
			case 93:
			case 3581:
				return 5489;
			case 12:
			case 15:
			case 17:
			case 3259:
				return 424;
				//GWD
			case 6204:
				return 6944;
			case 6206:
				return 6944;
			case 6208:
				return 6944;
			case 6261:
				return 6155;
			case 6263:
				return 6155;
			case 6227:
				return 6955;
			case 6225:
			case 6246:
				return 6955;
			case 6223:
				return 6955;
			case 6252:
				return 7010;
			case 770:
				return 7017;
			case 771:
				return 6375;
			case 58:
			case 59:
			case 60:
			case 61:
			case 62:
			case 63:
			case 64:
			case 134:
			case 977:
			case 1004:
			case 1009:
			case 1221:
			case 1473:
			case 1474:
			case 1478:
			case 1524:
			case 2034:
			case 2035:
			case 2491:
			case 2492:
			case 2850:
				return 144;
			case 100:
			case 102:
			case 101:
			case 298:
			case 299:
			case 444:
			case 445:
				return 312;
			case 89:
			case 133:
			case 987:
				return 290;
			case 6203:
				return 6944;
			case 6222:
				return 6974;
			case 6265:
				return 6155;
			case 6247:
				return 6966;
			case 6260:
				return 7061;
			case 1604:
				return 9468;
			case 1643:
				return 424;
			case 3200:
				return 3145;
			case 1618:
			case 1619:
				return 1550;
			case 1616:
			case 1617:
				return 261;
			case 1637:
			case 1638:
			case 1639:
			case 1640:
			case 1642:
				return 1585;
			case 1648:
			case 1649:
			case 1650:
			case 1651:
			case 1652:
			case 1653:
			case 1654:
			case 1655:
			case 1656:
			case 1657:
				return 9446;
			
			case 1624:
				return 1555;
			case 49:
				return 6563;
			case 269:
				return 158;
			case 941:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 5363:
			case 1589:
			case 1590:
			case 1591:
			case 1592:
			case 50:
				return 89;
			case 82:
			case 83:
			case 84:
			case 934:
			case 677:
			case 752:
			case 1472:
				return 65;
			case 110:
			case 112:
			return 4657;
			case 113:
			case 114:
			case 115:
			case 111:
			return 4671;
			case 117:
			case 116:
			return 4651;
			case 1582:
			case 1583:
			case 1584:
			case 1585:
			case 1586:
			case 1587:
			case 221:
			case 1588:
			
				return Config.anim;
			case 912:
			case 913:
				return 424;
// battlemages
			case 914:
				return 194;
			case 1610:
			case 1611:
				return 9455;
			case 2783:
				return 2732;
			case 1613:
				return 9489;
			case 1615: // Aby Demon
				return 1537;
			
			case 2033: //rat
				return 138;	
			
			case 2031: // bloodveld
				return 2070;
			
			case 81: // cow
			case 397:
			case 955:
			case 1691:
			case 1766:
			case 1767:
			case 1768:
			case 2310:
			case 3309:
				return 60;
			
			case 21: // hero
			case 3816:
				return 1156;	
			
			case 41: // chicken
				return 55;	
			
			case 9: // guard
			case 32: // guard
			case 20: // paladin
			case 124:
			return 1156;	
			
			case 1338: // dagannoth
			case 1340:
			case 1342:
				return 1341;
			case 3803:
			case 3804:
			case 3805:
			case 3806:
			case 3807:
			case 3809:	
				return 421;
			case 19: // white knight
				return 1156;
		
			case 125: // ice warrior
				return 1156;
			
			case 2028: // karil
			return 424;
					
			case 2025: // ahrim
				return 2079;
			
			case 2026: // dharok
				return 424;
			
			case 2027: // guthan
				return 383;
			
			case 2029: // torag
				return 1834;
			case 2881:
				return 2852;
			case 2882:
				return 2852;
			case 2883:
				return 2852;
			case 2030: // verac
				return 2063;
			case 3340:
				return 3311;
			case 1609:
				return 9441;
			case 1632:
				return 1596;
			case 1633:
				return 1581;
			case 1620:
				return 1560;
			case 1622:
				return 9508;
			case 1265:
				return 1313;
			case 127:
				return 186;	
			default:
			return 424;		
		
		}
	}
	public static int getDeathAnimation(NPC npc) {
		switch (npc.getDefinition().getType()) {
		case 8528:
		return 12694;
		case 14696:
			return 15473;
		case 8133:
		return 10059;
		case 10127:
		return 13171;
		case 10110:
		return 13005;
		case 7134:
		return 8757;
		case 9964:
		return 13715;
		case 9947:
		return 13772;
		case 9928:
		return 13336;
		case 5666:
		return 5898;
		case 3847:
		return 3993;
		case 8549:
		return 11078;
		case 6024:
		return 6537;
		case 1532:
		return -1;
		case 297:
		return 7220;
		case 8281:
		return 10666;
		case 8282:
		return 10670;
		case 8283:
		return 10673;
		case 49:
				return 6564;
		case 78:
		return 4918;
		case 750:
		return 6727;
				case 118:
		case 119:
		case 120:
		case 121:
		return 102;
		case 3836:
		return 6233;
		case 6250:
		return 7016;
		case 6248:
		return 6377;
		case 6247://zilyanna
		return 6965;
		case 9911://Har'Lakk the Riftsplitter
		return 13762;
		case 9780: //Rammernaut
		return 13702;
		case 9737://Night-gazer Khighorahk
		return 13424;
		case 9463://ICE STRYKEWYRM
		return 12793;
		case 9465://DESERT STRYKEWYRM
		return 12793;
		case 9467://JUNGLE STRYKEWYRM
		return 12793;
		case 4813://vyrewatch
		return Config.anim;
		case 5188://nial swiftling
		return Config.anim;
		case 5247://PENANCE QUEEN
				return 5412;
		case 8596:
				return 11199;
				case 8597:
				return 11204;
		case 8366: //td
		return 10924;
			case 2631:
			case 2632:
				return 2630;
			case 2626:
			case 2627:
				return 2620;
			case 2629:
			case 2630:
			case 269:
				return 7016;
			case 2738:
				return 2627;
			case 2744:
			case 2743:
				return 9269;
			case 2741:
			case 2742:
				return 9247;
			case 2745:
				return 9279;		
		
		
			case 2591:
				return 2607;
			case 2604:
			case 2617:
				return 2607;
			case 2610:
			case 2619:
				return 2608;
			case 127:
				return 188;
			case 1265:
				return 1314;
			case 1622:
				return 9508;
			case 1620:
				return 1563;
			case 1633:
				return 1580;
			case 1632:
				return 1597;
			case 1609:
				return 9440;
			case 1612:
				return 1524;
			case 123:
			case 122:
				return 474;
			case 90:
			case 91:
			case 92:
			case 93:
			case 3581:
				return 5489;
 
			case 58:
			case 59:
			case 60:
			case 61:
			case 62:
			case 63:
			case 64:
			case 134:
			case 977:
			case 1004:
			case 1009:
			case 1221:
			case 1473:
			case 1474:
			case 1478:
			case 1524:
			case 2034:
			case 2491:
			case 2492:
			case 2850:
				return 146;		
			case 100:
			case 102:
			case 101:
			case 298:
			case 299:
			case 444:
			case 445:
				return 313;
			case 89:
			case 133:
			case 987:
				return 292;
			case 6203:
				return 6946;
			case 6222:
				return 6975;
			case 6265:
				return 6156;
			case 6260:
				return 7062;
			case 3803:
			case 3804:
			case 3805:
			case 3806:
			case 3807:
			case 3809:
				return 0x900;
			case 50:
			case 941:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 1589:
			case 1590:
			case 1591:
			case 1592:
			
				return 92;
			
			case 1604:
				return 9467;
			case 1643:
				return 2304;
			case 3200:
				return 3147;
			case 1619:
				return 1553;
			case 1616:
				return 264;
			case 1637:
				return 1587;
			case 1648:
			case 1649:
			case 1650:
			case 1651:
			case 1652:
			case 1653:
			case 1654:
			case 1655:
			case 1656:
			case 1657:
				return 9445;
			case 1624:
				return 1558;
			case 914:
				return 196;
			case 913: // battle mages
			case 912:
				return 0x900;
			case 1610:
			case 1611:
				return 2;
			case 2035: //spider
			return 146;
			case 1613:
			return 9488;
			case 2033: //rat
			return 141;
			case 1615:
			return 1538;// Abbys
			case 2783:
			return 2733;//dark beast
			case 2031: // bloodvel
			return 2073;

			
			case 81: // cow
			case 397:
			case 955:
			case 1691:
			case 1766:
			case 1767:
			case 1768:
			case 2310:
			case 3309:
				return 62;
			
			case 41: // chicken
			return 57;
			
			case 1338: // dagannoth
			case 1340:
			case 1342:
				return 1342;
			case 110:
			case 112:
			return 4659;
			case 111:
			return 4673;
			case 117:
			case 116:
			return 4653;
			case 1582:
			case 1583:
			case 1584:
			case 1585:
			case 1586:
			case 1587:
			case 1588:
			case 221:
				return Config.anim;
			case 125: // ice warrior
			return 843;
			case 6263: // jags edit cases npcid:
				return 6156;
			case 6261:
				return 6156; // amination id
			case 6252:
				return 7011;
			case 770:
				return 7016;
			case 771:
				return 6377;
			case 6206:
				return 6946;
			case 6204:
				return 6946;
			case 6208:
				return 6946;
			case 6227:
				return 6956;
			case 6225:
			case 6246:
				return 6956;
			case 6223:
				return 6956;
			case 82:
			case 83:
			case 84:
			case 677:
			case 934:
			case 752:
			case 1472:
				return 67;
			case 2881:
			case 2882:
			case 2883:
				return 2856;
			case 3777:
			case 3778:
			case 3779:
			case 3780:
				return 3930;
				
		
		default:
			return 0x900;
		}
	}

	public static int getAttackAnimation(Client client) {
		int weapon = client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
		switch (weapon) {
		case 7959:
		case 18349:
		return 400;
		case 13907:
		case 13905:
		return 382;
		case 13879:
		return 10501;
		case 13883:
		return 10504;
		case 4153:
			return 1665;
			case 18353:
			return 2661;
		case 3190:
		case 3192:
		case 3194:
		case 3196:
		case 3198:
		case 3200:
		
		case 3202:
		case 3204:
			return 440; // halberd
		case 4151:
			return 1658; // whip
		case 1215:
		case 1231:
		case 5680:
		case 5698:
			return 402; // dragon dagger
		case 1307:
		case 1309:
		case 1311:
		case 1313:
		case 16909:
		case 1315:
		case 1317:
		case 1319:
		
			//if (client.fightType == 2)
		return 7041;
			//return 407;
		case 1277:
		case 1279:
		case 1281:
		case 1283:
		case 1285:
		case 1287:
		case 1289:
		case 1291:
		case 1293:
		case 1295:
		case 1297:
		case 1299:
		case 1301:
		case 1303:
		case 1305:
			return 451; // sword slash emote
		case 1349:
		case 1351:
		case 1353:
		case 1355:
		case 1357:
		case 1359:
		case 1361:
		case 1363:
		case 1365:
		case 1367:
		case 1369:
		case 1371:
		case 1373:
		case 1375:
		case 1377:
		case 6739:
			return 1833;
		default:
			if (client.fightType == 2)
				return 423;
			return 422;
		}
	}

	public static int getDefendAnimation(Client client) {
		int weapon = client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
		int shield = client.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD];
		if (shield != -1) {
			return 403;
		}
		switch (weapon) {
		case 1307:
		case 1309:
		case 1311:
		case 1313:
		case 1315:
		case 1317:
		case 1319:
			return 410;
		default:
			return 404;
		}
	}

	public static int getAttackAnimation(NPC npc) {
		switch (npc.getDefinition().getType()) {
		//arma has lots of attacks
		// case 6227:
			// return 6954;
		// case 6225:
			// return 6951;
		// case 6223:
			// return 6951;
			case 769:
			return 6252;
		case 771:
			return 6376;
		case 770:
			return 7018;
		case 6263:
			return 6154;
		case 6261:
			return 6154;
		case 6204:
			return 6945;
		case 6206:
			return 6945;
		case 6208:
			return 6945;
		default:
			return 422;
		}
	}



}
