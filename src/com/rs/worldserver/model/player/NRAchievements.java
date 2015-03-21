package com.rs.worldserver.model.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.*;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.world.ItemManager;
import com.rs.worldserver.Server;
/** 
@author - Orbit
*
*/

public class NRAchievements {
	private Client c;
	public NRAchievements(Client c) {
		this.c = c;
	}
	
	public NRAchievements(){
	
	}

	public void checkCombat(Client client,int id) { // done
		if(client.getNRAchievements().getCompletedAchievement(client,0,id) == 0){
			client.getNRAchievements().displayCongrats(client,0,id);
		}
	}
	
	public void checkPVP(Client client,int id){
		if(client.getNRAchievements().getCompletedAchievement(client,3,id) == 0){
			client.getNRAchievements().displayCongrats(client,3,id);
		}
	}
	
	public void checkSkilling(Client client,int id){
		if(client.getNRAchievements().getCompletedAchievement(client,4,id) == 0){
			client.getNRAchievements().displayCongrats(client,4,id);
		}
	}
	public void displayCongrats(Client c, int type, int id){
	
	}
	
	public void showDescription(Client c, int type, int id){
	
	}
	public void checkMonsters(Client client,int id){ // done
		if(client.getNRAchievements().getCompletedAchievement(client,2,id) == 0){
			client.getNRAchievements().displayCongrats(client,2,id);
		}
	}
	
	public void checkMisc(Client client,int id){ // done
		if(client.getNRAchievements().getCompletedAchievement(client,1,id) == 0){
			client.getNRAchievements().displayCongrats(client,1,id);
		}
	}
	public void getReward(Client client, int type, int id) {
		if(client.getActionAssistant().freeSlots() > 27)	{
			switch(type) {
				case 0: // Combat
					if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
						if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
							String reward = AchievementManager.getCombat().get(id).getItems();
							String amount = AchievementManager.getCombat().get(id).getItemAmount();
							String[] Items = reward.split(";");
							String[] ItemAmount = amount.split(",");
							client.getActionAssistant().sendFrame126("You have completed the achievement @red@" +AchievementManager.getCombat().get(id).getName(), 15835);
							client.getActionAssistant().sendFrame126("", 15840);
							client.getActionAssistant().sendFrame126("You were awarded!", 15838);
							if(Items[2].equals("null") && Items[1].equals("null")) {
								client.getActionAssistant().sendFrame126(" " +
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n", 15839);
							} else if(Items[2].equals("null") && !Items[1].equals("null")){
								client.getActionAssistant().sendFrame126(" " +
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n", 15839);
							} else {
								client.getActionAssistant().sendFrame126(" " +
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n"+
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[2])).getName() +" x " + ItemAmount[2] + "\\n", 15839);
							}
							client.getActionAssistant().showInterface(15831);
							for(int i = 0; i < Items.length; i++ ){
								if(!Items[i].equalsIgnoreCase("null")){
									client.getActionAssistant().addItem(Integer.parseInt(Items[i]),Integer.parseInt(ItemAmount[i]));
								}
							}
							client.getNRAchievements().updateReceivedAchievement(client,type,id);
							client.rewardPoints= client.rewardPoints + AchievementManager.getCombat().get(id).getPoints();
						} else {
							client.getActionAssistant().sendMessage("@gre@You already received your reward!");
						}
					} else {
						client.getActionAssistant().sendMessage("@blu@ debug - Not completed - " +  AchievementManager.getCombat().get(id).getName());
					}
					break;
				case 1:	// Misc
					if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
						if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
							String reward = AchievementManager.getMisc().get(id).getItems();
							String amount = AchievementManager.getMisc().get(id).getItemAmount();
							String[] Items = reward.split(";");
							String[] ItemAmount = amount.split(",");
							client.getActionAssistant().sendFrame126("You have completed the achievement @red@" +AchievementManager.getMisc().get(id).getName(), 15835);
							client.getActionAssistant().sendFrame126("", 15840);
							client.getActionAssistant().sendFrame126("You were awarded!", 15838);
							
							if(Items[2].equals("null") && Items[1].equals("null")) {
								client.getActionAssistant().sendFrame126(" " +
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n", 15839);
							} else if(Items[2].equals("null") && !Items[1].equals("null")){
								client.getActionAssistant().sendFrame126(" " +
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n", 15839);
							} else {
								client.getActionAssistant().sendFrame126(" " +
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n"+
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[2])).getName() +" x " + ItemAmount[2] + "\\n", 15839);
							}
							client.getActionAssistant().showInterface(15831);
							for(int i = 0; i < Items.length; i++ ){
								if(!Items[i].equalsIgnoreCase("null")){
									client.getActionAssistant().addItem(Integer.parseInt(Items[i]),Integer.parseInt(ItemAmount[i]));
								}
							}
							client.getNRAchievements().updateReceivedAchievement(client,type,id);						
							client.rewardPoints= client.rewardPoints + AchievementManager.getMisc().get(id).getPoints();
						} else {
							client.getActionAssistant().sendMessage("@gre@You already received your reward!");
						}
					} else {
						client.getActionAssistant().sendMessage("@blu@ debug - Not completed - " +  AchievementManager.getMisc().get(id).getName());
					}	
					break;
				case 2: // Monsters
					if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
						if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
						if (id == 7) {
						String reward = "Slayer Points";
							String amount = "15";
							client.getActionAssistant().sendFrame126("You have completed the achievement @red@" +AchievementManager.getMonster().get(id).getName(), 15835);
							client.getActionAssistant().sendFrame126("You were awarded!", 15838);
							client.getActionAssistant().sendFrame126("",15840);
							client.getActionAssistant().sendFrame126(" " +
								reward +" x " + amount + "\\n", 15839);
							client.getActionAssistant().showInterface(15831);
							client.slayerPoints += 15;
							client.getNRAchievements().updateReceivedAchievement(client,type,id);
							client.rewardPoints= client.rewardPoints + AchievementManager.getMonster().get(id).getPoints();
							break;
							}
						
							String reward = AchievementManager.getMonster().get(id).getItems();
							String amount = AchievementManager.getMonster().get(id).getItemAmount();
							String[] Items = reward.split(";");
							String[] ItemAmount = amount.split(",");
							client.getActionAssistant().sendFrame126("You have completed the achievement @red@" +AchievementManager.getMonster().get(id).getName(), 15835);
							client.getActionAssistant().sendFrame126("You were awarded!", 15838);
							client.getActionAssistant().sendFrame126("",15840);
							if(Items[2].equals("null") && Items[1].equals("null")) {
								client.getActionAssistant().sendFrame126(" " +
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n", 15839);
							} else if(Items[2].equals("null") && !Items[1].equals("null")){
								client.getActionAssistant().sendFrame126(" " +
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n", 15839);
							} else {
								client.getActionAssistant().sendFrame126(" " +
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n"+
								Server.getItemManager().getItemDefinition(Integer.parseInt(Items[2])).getName() +" x " + ItemAmount[2] + "\\n", 15839);
							}
							client.getActionAssistant().showInterface(15831);
							for(int i = 0; i < Items.length; i++ ){
								if(!Items[i].equalsIgnoreCase("null")){
									client.getActionAssistant().addItem(Integer.parseInt(Items[i]),Integer.parseInt(ItemAmount[i]));
								}
							}
							client.getNRAchievements().updateReceivedAchievement(client,type,id);
							client.rewardPoints= client.rewardPoints + AchievementManager.getMonster().get(id).getPoints();
						}  else {
							client.getActionAssistant().sendMessage("@gre@You already received your reward!");
						}
					} else {
						client.getActionAssistant().sendMessage("@blu@ debug - Not completed - " +  AchievementManager.getMonster().get(id).getName());
					}		
					break;
				case 3: // Pking
					if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
						if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
						String reward = AchievementManager.getPking().get(id).getItems();
						String amount = AchievementManager.getPking().get(id).getItemAmount();
						String[] Items = reward.split(";");
						String[] ItemAmount = amount.split(",");
						client.getActionAssistant().sendFrame126("You have completed the achievement @red@" +AchievementManager.getPking().get(id).getName(), 15835);
						client.getActionAssistant().sendFrame126("You were awarded!", 15838);
						client.getActionAssistant().sendFrame126("",15840);
						if(Items[2].equals("null") && Items[1].equals("null")) {
							client.getActionAssistant().sendFrame126(" " +
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n", 15839);
						} else if(Items[2].equals("null") && !Items[1].equals("null")){
							client.getActionAssistant().sendFrame126(" " +
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n", 15839);
						} else {
							client.getActionAssistant().sendFrame126(" " +
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n"+
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[2])).getName() +" x " + ItemAmount[2] + "\\n", 15839);
							}
						client.getActionAssistant().showInterface(15831);
						for(int i = 0; i < Items.length; i++ ){
							if(!Items[i].equalsIgnoreCase("null")){
								if(Items[i].equalsIgnoreCase("pkp")){
									client.pkpoints += Integer.parseInt(ItemAmount[i]);
									continue;
								} else {
									client.getActionAssistant().addItem(Integer.parseInt(Items[i]),Integer.parseInt(ItemAmount[i]));
								}
							}
						}
						client.getNRAchievements().updateReceivedAchievement(client,type,id);
						client.rewardPoints= client.rewardPoints + AchievementManager.getPking().get(id).getPoints();
					}  else {
							client.getActionAssistant().sendMessage("@gre@You already received your reward!");
					}
				} else {
						client.getActionAssistant().sendMessage("@blu@ debug - Not completed - " +  AchievementManager.getPking().get(id).getName());
					}		
					break;
				case 4:
				if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
						if(client.getNRAchievements().getCompletedAchievement(client,type,id) == 1){
						String reward = AchievementManager.getSkilling().get(id).getItems();
						String amount = AchievementManager.getSkilling().get(id).getItemAmount();
						String[] Items = reward.split(";");
						String[] ItemAmount = amount.split(",");
						client.getActionAssistant().sendFrame126("You have completed the achievement @red@" +AchievementManager.getSkilling().get(id).getName(), 15835);
						client.getActionAssistant().sendFrame126("You were awarded!", 15838);
						client.getActionAssistant().sendFrame126("",15840);
						if(Items[2].equals("null") && Items[1].equals("null")) {
							client.getActionAssistant().sendFrame126(" " +
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n", 15839);
						} else if(Items[2].equals("null") && !Items[1].equals("null")){
							client.getActionAssistant().sendFrame126(" " +
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n", 15839);
						} else {
							client.getActionAssistant().sendFrame126(" " +
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[0])).getName() +" x " + ItemAmount[0] + "\\n"+
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[1])).getName() +" x " + ItemAmount[1] + "\\n"+
							Server.getItemManager().getItemDefinition(Integer.parseInt(Items[2])).getName() +" x " + ItemAmount[2] + "\\n", 15839);
							}
						client.getActionAssistant().showInterface(15831);
						for(int i = 0; i < Items.length; i++ ){
							if(!Items[i].equalsIgnoreCase("null")){
								if(Items[i].equalsIgnoreCase("pkp")){
									client.pkpoints += Integer.parseInt(ItemAmount[i]);
									continue;
								} else {
									client.getActionAssistant().addItem(Integer.parseInt(Items[i]),Integer.parseInt(ItemAmount[i]));
								}
							}
						}
						client.getNRAchievements().updateReceivedAchievement(client,type,id);
						client.rewardPoints= client.rewardPoints + AchievementManager.getSkilling().get(id).getPoints();
					}  else {
							client.getActionAssistant().sendMessage("@gre@You already received your reward!");
					}
				} else {
						client.getActionAssistant().sendMessage("@blu@ debug - Not completed - " +  AchievementManager.getSkilling().get(id).getName());
					}	
					break;
			}
		} else {
			client.getActionAssistant().sendMessage("@gre@You must have no Items in your inventory to recieve ur Items.");	

		}
	}
	/**  */
	public void updateReceivedAchievement(Client client, int type, int id){
		switch(type){
			case 0:
			client.achievements[type][id] = 2;
				break;
			case 1:
			client.achievements[type][id] = 2;
				break;
			case 2:
			client.achievements[type][id] = 2;
				break;
			case 3:
			client.achievements[type][id] = 2;
				break;
			case 4:
			client.achievements[type][id] = 2;
				break;
		}
	}
	
		// this will be used for updating received.
	// format is Type - Catagory for Achievement
	// ID = the achievement id
	// completed should be 1
	// received should be 0.
	
	public void updateCompleteAchievement(Client client, int type, int id){
		switch(type){
			case 0:
			client.achievements[type][id] = 1;
				break;
			case 1:
			client.achievements[type][id] = 1;
				break;
			case 2:
			client.achievements[type][id] = 1;
				break;
			case 3:
			client.achievements[type][id] = 1;
				break;
			case 4:
			client.achievements[type][id] = 1;
				break;
		}
	}
	
	//should return completed.
	public int getCompletedAchievement(Client client, int type, int id){
		int completed = 0;
			return 0;
	}
}
