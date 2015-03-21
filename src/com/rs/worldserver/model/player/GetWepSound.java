package com.rs.worldserver.model.player;

import java.io.*;
import com.rs.worldserver.Constants;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCCombat;
import com.rs.worldserver.model.npc.NPCAction;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Config;

public class GetWepSound {
public GetWepSound() { }
public int GetWeaponSound(int playerId)
{
Client c = (Client)	PlayerManager.getSingleton().getPlayers()[playerId];
if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4718) {
	return 1055;
}
if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4747) {
	return 1062;
	}
if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4726) {
	return 1064;
	}
if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4755) {
	return 1059;
	}	
if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4566) {
	return 1174;
	}
if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11694 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11696 || 
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11700 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7896 ||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11698) {
	return 396;
	}

if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4566) {
	return 1174;
	}
if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4220
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4221
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4222
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4223) {
		return 367;
	}	
if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 772
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1379
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1381
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1383
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1385
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1387
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1389
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1391
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1393
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1395
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1397
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1399
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1401
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1403
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1405
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1407
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1409)//Staff wack
	{
        return 394;
	}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 839
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13879
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13883
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 841
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 843
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 845
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 847
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 849
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 851
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 853
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 855
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 857
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 859
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 861
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4734
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4827
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 2023//RuneC'Bow
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4212
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4214
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4934)//Bows/Crossbows
	{
        return 370;
	}
	if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1363
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1365
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1367
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1369
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1371
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1373
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1375
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1377
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1349
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1351
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1353
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1355
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1357
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1359
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1361)//BattleAxes/Axes
	{
        return 399;
	}
		if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 6609
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7808
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1307
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1309
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1311
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1313
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1315
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1317
                || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1319) //2hs/Dharok GreatAxe
        {
        return 400;
		}
	if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1321 
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1323 
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1325 
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1327 
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1329 
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1331 
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1333
	|| c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4587) //Scimitars
        {
        return 396;
		}
	if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1277	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1279	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1281	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1283	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1285	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1287	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1289	|| //swords
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1291	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1293	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1295	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1297	||	
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1299	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1301	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1303	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1307	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1309	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1311	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1313	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1315	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1317	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1319	||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1305){
	return 425;
	}
		
	if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4068) // Godswords
        {
        return 390;
		}
if (c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4151 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13660 || 
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13661 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13662 || 
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13663 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13664 || 
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13665 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13666 || 
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13667 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13668 || 
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13669 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13670 ||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7901 || c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7822 ||
	c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13671) // whips
        {
        return 1080;

		} else {
            return 398;//Daggers(this is enything that isn't added, so if you didn't add a skimmy or something, it would play this sound)
		}
		}
}
