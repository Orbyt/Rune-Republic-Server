package com.rs.worldserver.model.player;
import com.rs.worldserver.*;
import com.rs.worldserver.util.*;
public class DegradeManager {
	
	private int [][] chaos = { {18349,18350},{18351,18352},{18353,18354},{18355,18356},
							 {18357,18358},{18359,18360} };
	
	private int [][] pvp = { {22482, 22484, 22452},
							{22486, 22488, 22454},
							{22490, 22492, 22456},
							{13899, 13901,9000},
							{13887, 13889, 9001},
							{13893, 13895,9002},
							{13905, 13906,9003},
							{13861, 13863,9004},
							{13867, 13869,9005},
							{13864, 13866,9006},
							{13858, 13860,9007}, 
							{13870, 13872,9008},
							{13873, 13875,9009},
							{13876, 13878,9010},
							{13884, 13886,9011},
							{13890, 13892,9012},
							{13896, 13898,9013},
							};
					
	public DegradeManager(){
		
	
	}
	public void degradeNew(Client client){/*
	if(Misc.random(4) == 0){
		for(int i = 0; i < client.playerEquipment.length; i++){
			for(int k = 0; k < pvp.length; k++){
				//System.out.println(i + " " + pvp[k][0]);
				if(client.playerEquipment[i] == pvp[k][0]){
					client.getActionAssistant().sendMessage("@red@Your "+Server.getItemManager().getItemDefinition(client.playerEquipment[i]).getName().toLowerCase()+ " has degraded!");
					client.getOutStream().createFrameVarSizeWord(34);
					client.getOutStream().writeWord(1688);
					client.getOutStream().writeByte(i); // slot
					client.getOutStream().writeWord(pvp[k][1] + 1);
					client.getOutStream().writeByte(1); // amount
					client.getOutStream().endFrameVarSizeWord();
					client.flushOutStream();
					client.playerEquipment[i] = pvp[k][1];
					client.playerEquipmentN[i] = 1;
					client.getSpecials().specialBar();
					client.getCombat().calculateBonus();
					client.getCombatEmotes().getPlayerAnimIndex();
					client.getCombatFormulas().sendWeapon();
					client.appearanceUpdateRequired = true;
					client.updateRequired = true;
					return;
				}
			}
		}
	}*/
	}
	public int getDrop(int itemid){
	/*	for(int i = 0; i < chaos.length; i++){
				if(itemid == chaos[i][0])
					return chaos[i][1];
		}
		for(int k = 0; k < pvp.length; k++){
			if(itemid == pvp[k][0] || itemid == pvp[k][1]){
				return pvp[k][2];
			}
		}*/
		return itemid;
	}
	
	public void doDegrade(Client client, int damage){/*
		if(Misc.random(2) == 0){
		for(int i = 0; i < client.playerEquipment.length; i++){
			int d =  degrade(client,client.playerEquipment[i],damage);
			if( d == -1 ){
				client.playerEquipment[i] = -1;
				client.playerEquipmentN[i] = -1;
				client.getOutStream().createFrameVarSizeWord(34);
					client.getOutStream().writeWord(1688);
					client.getOutStream().writeByte(i); // slot
					client.getOutStream().writeWord(0);
					client.getOutStream().writeByte(0); // amount
					client.getOutStream().endFrameVarSizeWord();
				client.getSpecials().specialBar();
				client.getCombat().calculateBonus();
				client.getCombatEmotes().getPlayerAnimIndex();
				client.getCombatFormulas().sendWeapon();
				client.appearanceUpdateRequired = true;
				client.updateRequired = true;
			}
		}
		} */
	}
	
	
	public void repair(Client c){
		
	}
	
	private boolean repairItem(Client c, int oldItem, int newItem){
		int slot = c.getActionAssistant().getItemSlot(995);
		if(slot == -1){
			c.getActionAssistant().sendMessage("@red@You do not have any money!");
			return false;
		}
		if(c.playerItemsN[slot] >= 150000000){
			c.getActionAssistant().sendMessage((oldItem) + " " +(newItem));
			c.getActionAssistant().deleteItem(995, 150000000);
			c.getActionAssistant().addItem(newItem, 1);
			c.getActionAssistant().deleteItem(oldItem, 1);
			c.getActionAssistant().sendMessage("@red@Your "+Server.getItemManager().getItemDefinition(oldItem).getName().toLowerCase()+ " was repaired!");
			return true;
		}
			c.getActionAssistant().sendMessage("@red@You do not enough money! (150m) needed");
			return false;			
	}
	/**
	*  Degrades armor will return the new itemid if needs to be damaged*
	* 
	*/
	public int degrade(Client c, int itemid,int damage){ /*
		if(c.degradeItems == null){
			c.degradeItems = new int[pvp.length+chaos.length];
			for(int i = 0; i < c.degradeItems.length; i++)
				c.degradeItems[i] = 0;
		}
		for(int j = 0; j < c.degradeItems.length; j++){
			for(int i = 0; i < chaos.length; i++){
				if(itemid == chaos[i][0]){
					c.degradeItems[j] += damage;
					//c.getActionAssistant().sendMessage("@red@Your "+Server.getItemManager().getItemDefinition(itemid).getName().toLowerCase()+ " degraded slightly!");
						
					if(c.degradeItems[j] >= 20000){
						c.degradeItems[j] = 0;
						c.getActionAssistant().addItem(chaos[i][1], 1);
						c.getActionAssistant().sendMessage("@red@Your "+Server.getItemManager().getItemDefinition(itemid).getName().toLowerCase()+ " has broke!");
						return -1;
					}
				}
			}
			for(int k = 0; k < pvp.length; k++){
				if(itemid == pvp[k][0] || itemid == pvp[k][1]){
					c.degradeItems[j] += damage;
					//c.getActionAssistant().sendMessage("@red@Your "+Server.getItemManager().getItemDefinition(itemid).getName().toLowerCase()+ " degraded slightly!");
					if(c.degradeItems[j] >= 15000){
						c.degradeItems[j] = 0;
						c.getActionAssistant().addItem(pvp[k][2], 1);
						c.getActionAssistant().sendMessage("@red@Your "+Server.getItemManager().getItemDefinition(itemid).getName().toLowerCase()+ " has broke!");
						return -1;
					}
				}
			}
		} */
		return itemid;
	}
}