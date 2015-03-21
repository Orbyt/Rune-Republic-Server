package com.rs.worldserver.model.player;

import com.rs.worldserver.model.player.*;

public class CombatEmotes {

		private Client client;

		public CombatEmotes(Client client) {
			this.client = client;
		}	
	public void getPlayerAnimIndex(){
		client.playerStandIndex = 0x328;
		client.playerTurnIndex = 0x337;
		client.playerWalkIndex = 0x333;
		client.playerTurn180Index = 0x334;
		client.playerTurn90CWIndex = 0x335;
		client.playerTurn90CCWIndex = 0x336;
		client.playerRunIndex = 0x338;
		switch(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]){
			case 18353:
			case 16423:
			case 16425:
				client.playerRunIndex= 1664;
				client.playerWalkIndex=1663;
				client.playerStandIndex =1662;
				break;
			case 15241:
				client.playerRunIndex= 2076;
				client.playerWalkIndex=2076;
				client.playerStandIndex =5869;
				break;
			case 4024:
			case 4029:
				client.playerRunIndex= 1381;
				client.playerWalkIndex=1380;
				client.playerStandIndex =1388;
				break;
			case 4026:
			case 4027:
				client.playerRunIndex= 1400;
				client.playerWalkIndex=1399;
				client.playerStandIndex =1401;
				break;
			case 4031:
				client.playerRunIndex= 219;
				client.playerWalkIndex=220;
				client.playerStandIndex =222;
				break;
			case 4726: // guthan 
			case 13905:
				client.playerStandIndex = 809;
				client.playerWalkIndex = 1146;
				client.playerRunIndex = 1210;
				break;
			case 4747: // torag			
				break;
			case 4718: // dharok
				client.playerStandIndex = 0x811;
				client.playerWalkIndex = 0x67F;
				client.playerRunIndex = 0x680;
				break;
			case 4710: // ahrim
				client.playerStandIndex = 809;
				client.playerWalkIndex = 1146;
				client.playerRunIndex = 1210;
				break;
			case 4755: // verac
				client.playerStandIndex = 1832;
				client.playerWalkIndex = 1830;
				client.playerRunIndex = 1831;
				break;
			case 4734: // karil
				client.playerStandIndex = 2074;
				client.playerWalkIndex = 2076;
				client.playerRunIndex = 2077;
				break;
			case 4084:
				client.playerStandIndex = 1461;
				client.playerWalkIndex = 1468;
				client.playerRunIndex = 1467;
				break;
			case 13660:
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
			case 4151:
			case 7822:
			case 7901:
			case 21373:
			case 21374:
				client.playerStandIndex = 1832;
				client.playerWalkIndex = 1660;
				client.playerRunIndex = 1661;
				break;
			case 21371:
			case 21372:
			case 21375:
				client.playerStandIndex = 11973;
				client.playerWalkIndex = 11975;
				client.playerRunIndex = 1661;
				break;
			case 1307: // 2handed
			case 1309:
			case 1313:
			case 1315:
			case 1317:
			case 1319:
			case 11694:
			case 11696:
			case 11700:
			case 11698:
			case 1311:
			case 11730:
			case 6609:
			case 7158:
			case 16909:
			case 18369:
			case 16907:
				client.playerWalkIndex = 7040;
				client.playerRunIndex = 7039;
				client.playerStandIndex = 7047;
				break;
			case 4153:
				client.playerStandIndex = 1662;
				client.playerWalkIndex = 1663;
				client.playerRunIndex = 1664;
				break;
			case 1305:
				client.playerStandIndex = 809;
				break;
			case 84: //staffs
			case 137:
			case 1381:
			case 1383:
			case 1385:
			case 1387:
			case 1389:
			case 1391:
			case 1393:
			case 1395:
			case 1397:
			case 1399:
			case 1401:
			case 1403:
			case 1405:
			case 1407:
			case 1409:
			case 3035:
			case 3054:
			case 4170:
			case 4675:
			case 6562:
			case 6914:
				client.playerStandIndex = 809;
				client.playerWalkIndex = 1146;
				client.playerRunIndex = 1210;
				break;
			case 839: //bows
			case 845:
			case 847:
			case 851:
			case 855:
			case 859:
			case 841:
			case 843:
			case 849:
			case 853:
			case 857:
			case 861:
			case 4214:
			case 4827:
case 15704:
			case 6724:
				client.playerStandIndex = 808;
				client.playerWalkIndex = 819;
				client.playerRunIndex = 824;
				break;		
				case 10887:
				client.playerStandIndex = 5869;
				client.playerWalkIndex = 5867;
				client.playerRunIndex = 5868;
				break;
		}
	}
	/**
	 * Block emotes
	 */
	public int getBlockEmote(){
		int weapon = client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON];
		int shield = client.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD];

		if (shield == 8850 || shield == 8844 ||shield == 8845 ||shield == 8846 ||shield == 8847 ||shield == 8848 ||shield == 8849 ||shield == 7935){
			return 4177;
		}
		if ((shield != -1 && shield != 8850) ||(shield != -1 && shield != 8844) ||(shield != -1 && shield != 8845) ||
			(shield != -1 && shield != 8846) ||(shield != -1 && shield != 8847) ||(shield != -1 && shield != 8848) ||
			(shield != -1 && shield != 8849) ||(shield != -1 && shield != 7935)
			){
			return 1156;
		} 
		switch (weapon){	
			case 18353:
			case 16423:
			case 16425:
				return 1666;
			case 15241:
				return 1666;
			case 10887:
				return 5866;
			case 4024:
			case 4029:
				return 1393;
			case 4026:
			case 4027:
				return 1403;
			case 4031:
				return 221;
			case 11694:
			case 16909:
			case 18369:
			case 16907:
			case 11696:
			case 11700:
			case 11698:
			case 11730: 
				return 7050;
			case 1307:
			case 7407:
			case 1309:
			case 1311:
			case 1315:
			case 1317:
			case 1319:
				return 410;
			case 4755:
				return 2063;
			case 13660:
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
			case 4151:
			case 7822:
			case 7901:
			case 21371:
			case 21372:
			case 21373:
			case 21374:
			case 21375:
				return 1659;
			case 839: //bows
			case 845:
			case 847:
			case 851:
			case 855:
			case 859:
			case 841:
			case 843:
			case 849:
			case 853:
			case 857:
			case 4212:
			case 4214:
			case 4215:
			case 4216:
			case 4217:
			case 4218:
			case 4219:
			case 4220:
			case 20171:
			case 4221:
			case 4222:
			case 4223:
			case 4827:
case 15704:
			case 6724:
			case 861:
				return 424;
			case 4153:
			
			default:
				return 404;
		}
	}	
	public int getWepAnim()	{
		if(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] <= 0){
			switch(client.fightType){
				case 0:
					return 422;
				
				case 2:
					return 423;
				
				case 1:
					return 451;
			}
		}
		switch(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]){
				case 15486:
			if (client.fightType == 2)
					return 12029;
				else if (client.fightType == 1)
					return 414;
				return 12028;
			case 13925:
					if (client.fightType == 3)
						return 13049;
					return 13048;				
			case 11716:
				return 12006;

			case 15241:
				return 12174;
			case 18353:
			case 16423:
			case 16425:
				return 13055;
			case 10887:
				return 5870;
								
			case 13907:
				return 382;
			case 4024:
			case 4029:
				return 1392;
			case 4026:
			case 4027:
				return 1402;
			case 4031:
				return 220;
			case 1377:
			case 1363:
			case 1365:
			case 1367:
			case 1369:
			case 1371:
			case 1373:
			case 1375:
				return 1833;
			case 3190:
			case 3192:
			case 3194:
			case 3196:
			case 3198:
			case 3200:
			case 3202:
			case 3204:
				return 440;
			case 18349:
				return 386;
			case 6528:
				return 2661;
			
			case 863:
			case 864:
			case 865:
			case 866: // knives
			case 867:
			case 868:
			case 869:
			case 870:
			case 871:
			case 872:
			case 873:
			case 874:
			case 875:
			case 876:
			case 3093:
			case 5628:
			case 5629:
			case 5630:
			case 5631:
			case 5632:
			case 5633:
			case 5634:
			case 5635:
			case 5636:
			case 5637:
			case 5638:
			case 5639:
			case 5640:
			case 5641:
			case 806:
			case 807:
			case 808:
			case 809: // darts
			case 810:
			case 811:
			case 812:
			case 813:
			case 814:
			case 815:
			case 816:
			case 817:
			case 5654:
			case 5655:
			case 5656:
			case 5657:
			case 5658:
			case 5659:
			case 5660:
			case 5661:
			case 5662:
			case 5663:
			case 5664:
			case 5665:
			case 5666:
			case 5667:
			case 13879:
				return 10501;
			case 13883:
				return 10504;
			case 825:
			case 826:
			case 827: // javelin
			case 828:
			case 829:
			case 830:
			case 800:
			case 801:
			case 802:
			case 803: // axes
			case 804:
			case 805:
				return 806;
			case 6522: // Toktz-xil-ul
				return 2614;
			case 4153: // granite maul
				return 1665;
			case 4726: // guthan 
			case 13905:
				return 2080;
			case 4747: // torag
				return 0x814;
			case 4718: // dharok
				if (client.fightType == 3)
					return 12003;
				return 12002;
			case 4710: // ahrim
				return 406;
			case 4755: // verac
				return 2062;
			case 4734: // karil
				return 2075;
			case 13660:
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
			case 4151:
			case 7822:
			case 7901:
			case 21371:
			case 21372:
			case 21373:
			case 21374:
			case 21375:
				return 11968;
			case 1205: //dagger
			case 1203:
			case 1207:
			case 1209:
			case 1211:
			case 1213:	
			case 5698:
			case 1215:
			case 1231:
			case 5680:
				return 402;
			case 1291: //swords
			case 1293:
			case 1295:
			case 1297:
			case 1299:
			case 1301:
			case 1303:
			case 1305:
			case 6607:
				if (client.fightType == 3)
					return 7048;
				return 451;
				// 2handed
			case 6609:
			case 7158:
			case 18369:
			case 16907:
			case 11696:
			case 11694:
			case 11698:
			case 11700:
			case 11730:
			case 16909:
			case 1307:
			case 1309:
			case 1311:
			case 1313:
			case 1315:
			case 1317:
			case 1319:
				if (client.fightType == 1)
					return 7049;
				else if (client.fightType == 3)
					return 7048;
				return 7041;				
			case 839: //bows
			case 845:
			case 847:
			case 851:
			case 855:
			case 859:
			case 841:
			case 843:
			case 849:
			case 853:
			case 857:
			case 861:
			case 4212:
			case 4214:
			case 4215:
			case 4216:
			case 4217:
			case 4218:
			case 4219:
			case 4220:
			case 20171:
			case 4221:
			case 4222:
			case 4223:
			case 4827:
			case 15701:
			case 15702:
			case 15703:
			case 15704:
			case 6724:
				return 426;
			case 9185:
			case 18357:
				return 4230;
			case 1378:
				return 451;
			case 13899:
			case 16955:
			case 13904:
			case 13902:
			case 7959:
			
			case 18365:
						
				if (client.fightType == 3)
					return 400;	
				return 451;
			default:
				return 451;
		}
	}	
	
}