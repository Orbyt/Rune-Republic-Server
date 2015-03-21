package com.rs.worldserver.model.player;

import com.rs.worldserver.model.player.*;
import com.rs.worldserver.model.ItemDefinition;
import com.rs.worldserver.Server;

public class MagicData {

	private Client c;
	public MagicData(Client c) {
		this.c = c;
	}
	
	public MagicData(){
	
	}
	
	public boolean hasComboRunes(int spellid){
		switch(spellid){
			/* god spells */
			case 1190:
			case 1191:
			case 1192:
				if(c.getActionAssistant().playerHasItem(4697,4) && c.getActionAssistant().playerHasItem(565,2)){
					c.getActionAssistant().deleteItem(4697, c.getActionAssistant().getItemSlot(4697), 4);
					c.getActionAssistant().deleteItem(565, c.getActionAssistant().getItemSlot(565), 2);
					return true;
				}
			break;
			//charge
			case 4169:
			if(c.getActionAssistant().playerHasItem(4697,3) && c.getActionAssistant().playerHasItem(565,3)){
					c.getActionAssistant().deleteItem(4697, c.getActionAssistant().getItemSlot(4697), 3);
					c.getActionAssistant().deleteItem(565, c.getActionAssistant().getItemSlot(565), 3);
					return true;
				}
			break;
			// entangle
			case 1592:
				if(c.getActionAssistant().playerHasItem(4698,5) && c.getActionAssistant().playerHasItem(561,4)){
					c.getActionAssistant().deleteItem(4698, c.getActionAssistant().getItemSlot(4698), 5);
					c.getActionAssistant().deleteItem(561, c.getActionAssistant().getItemSlot(561), 4);
					return true;
				}
				break;
			/** ice magic **/
			case 12891:
			case 31:
					if(c.getActionAssistant().playerHasItem(4695,6) && c.getActionAssistant().playerHasItem(565,2) && c.getActionAssistant().playerHasItem(560,4) ||
					c.getActionAssistant().playerHasItem(4698,6) && c.getActionAssistant().playerHasItem(565,2) && c.getActionAssistant().playerHasItem(560,4) ||
					c.getActionAssistant().playerHasItem(4694,6) && c.getActionAssistant().playerHasItem(565,2) && c.getActionAssistant().playerHasItem(560,4)
			 		){
					if(c.getActionAssistant().playerHasItem(4698)){
					c.getActionAssistant().deleteItem(4698, c.getActionAssistant().getItemSlot(4698), 6);
					} else if(c.getActionAssistant().playerHasItem(4695)){
						c.getActionAssistant().deleteItem(4695, c.getActionAssistant().getItemSlot(4695), 6);
					} else if(c.getActionAssistant().playerHasItem(4694)){
					c.getActionAssistant().deleteItem(4694, c.getActionAssistant().getItemSlot(4694), 6);
					}
					c.getActionAssistant().deleteItem(565, c.getActionAssistant().getItemSlot(565), 2);
					c.getActionAssistant().deleteItem(560, c.getActionAssistant().getItemSlot(560), 4);
					return true;
				}
			
			break;
			
			// shadow
			case 13023:
			case 29:
				if(c.getActionAssistant().playerHasItem(4695,4) && c.getActionAssistant().playerHasItem(566,3) && c.getActionAssistant().playerHasItem(560,4) && c.getActionAssistant().playerHasItem(565,2) ||
				   c.getActionAssistant().playerHasItem(4696,4) && c.getActionAssistant().playerHasItem(566,3) && c.getActionAssistant().playerHasItem(560,4) && c.getActionAssistant().playerHasItem(565,2) ||
					c.getActionAssistant().playerHasItem(4697,4) && c.getActionAssistant().playerHasItem(566,3)&& c.getActionAssistant().playerHasItem(560,4) && c.getActionAssistant().playerHasItem(565,2)
			 		){
					if(c.getActionAssistant().playerHasItem(4695)){
					c.getActionAssistant().deleteItem(4695, c.getActionAssistant().getItemSlot(4695), 4);
					} else if(c.getActionAssistant().playerHasItem(4696)){
					c.getActionAssistant().deleteItem(4696, c.getActionAssistant().getItemSlot(4696), 4);
					} else if(c.getActionAssistant().playerHasItem(4697)){
					c.getActionAssistant().deleteItem(4697, c.getActionAssistant().getItemSlot(4697), 4);
					}
						c.getActionAssistant().deleteItem(560, c.getActionAssistant().getItemSlot(560), 4);
					c.getActionAssistant().deleteItem(566, c.getActionAssistant().getItemSlot(566), 3);
					c.getActionAssistant().deleteItem(565, c.getActionAssistant().getItemSlot(565), 2);
					return true;
				}
			break;
			
			case 12975:
			case 28:
				if(c.getActionAssistant().playerHasItem(4697,4) && c.getActionAssistant().playerHasItem(565,2) && c.getActionAssistant().playerHasItem(560,4)){
					c.getActionAssistant().deleteItem(4697, c.getActionAssistant().getItemSlot(4697), 4);
					c.getActionAssistant().deleteItem(565, c.getActionAssistant().getItemSlot(565), 2);
					c.getActionAssistant().deleteItem(560, c.getActionAssistant().getItemSlot(560), 4);
					return true;
				}
			break;
			//misc
			case 21746:
				if(c.getActionAssistant().playerHasItem(4699,4) && c.getActionAssistant().playerHasItem(566,4) && c.getActionAssistant().playerHasItem(565,4) ||
				   c.getActionAssistant().playerHasItem(4696,4) && c.getActionAssistant().playerHasItem(566,4) && c.getActionAssistant().playerHasItem(565,4) ||
					c.getActionAssistant().playerHasItem(4698,4) && c.getActionAssistant().playerHasItem(566,4) && c.getActionAssistant().playerHasItem(565,4)
			 		){
					if(c.getActionAssistant().playerHasItem(4699)){
					c.getActionAssistant().deleteItem(4699, c.getActionAssistant().getItemSlot(4699), 4);
					} else if(c.getActionAssistant().playerHasItem(4698)){
					c.getActionAssistant().deleteItem(4698, c.getActionAssistant().getItemSlot(4698), 4);
					} else if(c.getActionAssistant().playerHasItem(4696)){
					c.getActionAssistant().deleteItem(4696, c.getActionAssistant().getItemSlot(4696), 4);
					}
					c.getActionAssistant().deleteItem(566, c.getActionAssistant().getItemSlot(566), 4);
					c.getActionAssistant().deleteItem(565, c.getActionAssistant().getItemSlot(565), 4);
					return true;
				}
				break;
		}
		return false;
	
	}
	public int getSpellHitDelay(){
		switch(c.MAGIC_SPELLS[c.spellId][0]){
			case 12871:
				return 3;
			case 12891: // ice barrage
				return 3;			
			default:
				return 3;
		}
	}
		
	public int getFreezeTimeAuto(int id){
		switch(id){
			case 19: // ice rush
				return 10;
						
			case 23: // ice burst
				return 10;
			
	
			case 27: // ice blitz
				return 11;
			
			case 31: // ice barrage
				return 15;
			
			default:
				return 0;
		}
	}
	
	public int getFreezeTime(){
		switch(c.MAGIC_SPELLS[c.spellId][0]){
			case 1572:
			case 12861: // ice rush
				return 10;
						
			case 1582:
			case 12881: // ice burst
				return 10;
			
			case 1592:
			case 12871: // ice blitz
				return 11;
			
			case 12891: // ice barrage
				return 15;
			
			default:
				return 0;
		}
	}

	public int getStartHeight()	{
		switch(c.MAGIC_SPELLS[c.spellId][0]){
			case 1562: // stun
				return 25;
			
			case 12939:// smoke rush
				return 35;
			
			case 12987: // shadow rush
				return 38;
			
			case 12861: // ice rush
				return 15;
			
			case 12951:  // smoke blitz
				return 38;
			
			case 12999: // shadow blitz
				return 25;
			
			case 12911: // blood blitz
				return 25;
			
			default:
				return 43;
		}
	}
	public int getStartGfxHeight(){
		switch(c.MAGIC_SPELLS[c.spellId][0]){
			case 12871:
			case 12891:
				return 0;
			
			default:
				return 100;
		}
	}	
	public int getEndHeight(){
		switch(c.MAGIC_SPELLS[c.spellId][0]){
			case 1562: // stun
				return 10;
			
			case 12939: // smoke rush
				return 20;
			
			case 12987: // shadow rush
				return 28;
			
			case 12861: // ice rush
				return 10;
			
			case 12951:  // smoke blitz
				return 28;
			
			case 12999: // shadow blitz
				return 15;
			
			case 12911: // blood blitz
				return 10;
				
			default:
				return 31;
		}
	}
	
	public int getStartDelay(){
		switch(c.MAGIC_SPELLS[c.spellId][0]){
			case 1539:
				return 60;
			
			default:
				return 53;
		}
	}
	
	public int getStaffNeeded(){
		switch(c.MAGIC_SPELLS[c.spellId][0]){
			case 1539:
				return 1409;
			
			case 12037:
				return 4170;
			
			case 1190:
				return 2415;
			
			case 1191:
				return 2416;
			case 1192:
				return 2417;
			case 21744:
			case 22168:
			case 21745:
			case 21746:
				return 13867;
			default:
				return 0;
		}
	}
	public int getCapes(){
		switch(c.MAGIC_SPELLS[c.spellId][0]){	
			case 1190:
				return 2412;
			
			case 1191:
				return 2413;
			
			case 1192:
				return 2414;
			
			default:
				return 0;
		}
	}
	public boolean godSpells(){
		switch(c.MAGIC_SPELLS[c.spellId][0]){	
			case 1190:
				return true;
			
			case 1191:
				return true;
			
			case 1192:
				return true;
			
			default:
				return false;
		}
	}
		
	public int getEndGfxHeight(){
		switch(c.MAGIC_SPELLS[c.spellId][0]){
			case 12987:	
			case 12901:		
			case 12861:
			case 12445:
			case 1192:
			case 13011:
			case 12919:
			case 12881:
			case 12999:
			case 12911:
			case 12871:
			case 13023:
			case 12929:
			case 12891:
				return 0;
			
			default:
				return 100;
		}
	}
	
}