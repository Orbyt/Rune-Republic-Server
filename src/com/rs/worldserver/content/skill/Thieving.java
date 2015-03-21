package com.rs.worldserver.content.skill;

import com.rs.worldserver.Server;
import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.ActionAssistant;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCCombat;
import com.rs.worldserver.model.npc.NPCAction;

/**
 * Thieving skill handler
 * 
 * @author Chachi
 * Credits: Kite & Shard Team
 */
public class Thieving implements Skill {
	public static final int	THIEVING_DELAY = 2100;
	
	
	public static void stall(final Client player, final String recMessage, 
							int lvl, final int xpGain, final int item, final int itemAmount,final int objectID,final int x, final int y) {
		if(player.playerLevel[17] < lvl) {
  			player.getActionAssistant()
					.sendMessage("You need a thieving level of "+lvl+" to steal from this stall.");
			return;
 		}

		if (player.checkBusy()) {
				return;
		}
		if (player.getActionAssistant().freeSlots() >= 1) {	
			player.setBusy(true);
			player.getActionAssistant().startAnimation(881);
			EventManager.getSingleton().addEvent(player,"thieving", new Event() {

				@Override
				public void execute(EventContainer c) {
					if (Misc.random(3) == 1) {
						player.getActionAssistant().sendMessage("You failed..");
						//todo add NPC's attacking on fail
					} else {
						if(objectID == 2562){
							final GameObject st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.NORTH, 10,
									Server.getObjectManager().getDefinition(6573));
							final GameObject st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.NORTH, 10,
									Server.getObjectManager().getDefinition(2562));
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null,"thieving2", new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							}, Misc.random(3000));	
						}	
						if(objectID == 2564){
							final GameObject st1,st2;
							GameObject stall = Server.getObjectManager().getObjectAt(x,y,player.getHeightLevel());
							if (stall != null) {
								st1 = new GameObject(x, y, player
									.getHeightLevel(), stall.getFace(), 10,
									Server.getObjectManager().getDefinition(6573));
								st2 = new GameObject(x, y, player
									.getHeightLevel(), stall.getFace(), 10,
									Server.getObjectManager().getDefinition(2564));
							}
							else {
								st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.WEST, 10,
									Server.getObjectManager().getDefinition(6573));
								st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.WEST, 10,
									Server.getObjectManager().getDefinition(2564));
							}
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null, "thieving3",new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							}, Misc.random(3000));	
						}	
						if(objectID == 2563){
							final GameObject st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.WEST, 10,
									Server.getObjectManager().getDefinition(6573));
							final GameObject st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.WEST, 10,
									Server.getObjectManager().getDefinition(2563));
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null,"thieving4", new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							}, Misc.random(3000));	
						}	
						if(objectID == 2561 && x == 2667){
							final GameObject st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.NORTH, 10,
									Server.getObjectManager().getDefinition(6573));
							final GameObject st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.NORTH, 10,
									Server.getObjectManager().getDefinition(2561));
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null,"thieving5", new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							}, Misc.random(3000));	
						}	
						if(objectID == 2561 && x == 2655){
							final GameObject st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.SOUTH, 10,
									Server.getObjectManager().getDefinition(6573));
							final GameObject st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.SOUTH, 10,
									Server.getObjectManager().getDefinition(2561));
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null,"thieving6", new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							}, Misc.random(3000));	
						}						
						if(objectID == 2560 && x == 2656){
							final GameObject st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.SOUTH, 10,
									Server.getObjectManager().getDefinition(6573));
							final GameObject st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.SOUTH, 10,
									Server.getObjectManager().getDefinition(2560));
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null,"thieving7", new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							}, Misc.random(3000));	
						}
						if(objectID == 2560 && y == 3314){
							final GameObject st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.EAST, 10,
									Server.getObjectManager().getDefinition(6573));
							final GameObject st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.EAST, 10,
									Server.getObjectManager().getDefinition(2560));
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null,"thieving8", new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							},Misc.random(3000));	
						}						
						if(objectID == 2565){
							final GameObject st1,st2;
							GameObject stall = Server.getObjectManager().getObjectAt(x,y,player.getHeightLevel());
							if (stall != null) {
								st1 = new GameObject(x, y, player
									.getHeightLevel(), stall.getFace(), 10,
									Server.getObjectManager().getDefinition(6573));
								st2 = new GameObject(x, y, player
									.getHeightLevel(), stall.getFace(), 10,
									Server.getObjectManager().getDefinition(2565));
							}
							else {
								st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.EAST, 10,
									Server.getObjectManager().getDefinition(6573));
								st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.EAST, 10,
									Server.getObjectManager().getDefinition(2565));
							}
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null, "thieving9",new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							}, Misc.random(3000));	
						}	
						if(objectID == 4277){
							final GameObject st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.NORTH, 10,
									Server.getObjectManager().getDefinition(6573));
							final GameObject st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.NORTH, 10,
									Server.getObjectManager().getDefinition(4277));
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null,"thieving10", new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							}, Misc.random(3000));	
						}	
						if(objectID == 4278){
							final GameObject st1 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.NORTH, 10,
									Server.getObjectManager().getDefinition(6573));
							final GameObject st2 = new GameObject(x, y, player
									.getHeightLevel(), GameObject.Face.NORTH, 10,
									Server.getObjectManager().getDefinition(4278));
							Server.getObjectManager().addObject(st1);
							EventManager.getSingleton().addEvent(null,"thieving11", new Event() {

								@Override
								public void execute(EventContainer container) {
									Server.getObjectManager().replaceTemporaryObject(
											st1, st2);
									container.stop();
								}

								@Override
								public void stop() {

								}

							}, Misc.random(3000));	
						}						
						player.getActionAssistant().sendMessage("You steal from the stall " + recMessage);
						player.getActionAssistant().addSkillXP(xpGain, 17);
						player.getActionAssistant().addItem(item, itemAmount);
						player.getActionAssistant().refreshSkill(17);					
					}
					c.stop();
				}

				@Override
				public void stop() {
					player.getActionAssistant().startAnimation(2552);
					player.setBusy(false);

				}

			}, THIEVING_DELAY);
		} else {
			player.getActionAssistant().sendMessage(
					"You have no space in your inventory");
		}
	}

	public static void pickPocket(final Client player, final String npcName, final String recMessage, 
							int lvl, final int xpGain, final int item, final int itemAmount,final int npc) {
		int THIEVING_DELAYNPC;					
		if(player.playerLevel[17] < lvl) {
  			player.getActionAssistant()
					.sendMessage("You need a thieving level of "+lvl+" to steal from this Npc.");
			return;
 		}
		if (player.checkBusy()) {
				return;
		}
		if (player.getActionAssistant().freeSlots() >= 1) {	
			player.setBusy(true);
			player.getActionAssistant().startAnimation(881);
			if (Misc.random(3) == 1) {	
				EventManager.getSingleton().addEvent(player,"thieving12", new Event() {
					@Override
					
					public void execute(EventContainer c) {
						player.getActionAssistant().sendMessage("You failed..");
						player.gfx100(348);
						int freezeDelay = 10; 
						if(freezeDelay > 0 && player.freezeTimer == -5) { 
							player.freezeTimer = freezeDelay;
							player.stopMovement();
						}					
						c.stop();
					}
					@Override
					public void stop() {
					
					}
				}, 1000);
				EventManager.getSingleton().addEvent(player,"thieving 13", new Event() {
					@Override
					
					public void execute(EventContainer c) {
						c.stop();
					}
					@Override
					public void stop() {
						player.getActionAssistant().startAnimation(2552);
						player.setBusy(false);

					}
				}, 1000);
			} else {
				EventManager.getSingleton().addEvent(player,"thieving 14", new Event() {
					@Override
					public void execute(EventContainer c) {
						player.getActionAssistant().sendMessage("You pickpocket the "+npcName+" "+ recMessage);
						player.getActionAssistant().addSkillXP(xpGain, 17);
						player.getActionAssistant().addItem(item, itemAmount);
						player.getActionAssistant().refreshSkill(17);
						c.stop();
					}
					@Override
					public void stop() {
						player.getActionAssistant().startAnimation(2552);
						player.setBusy(false);

					}
				}, 1000);			
			}
		} else {
			player.getActionAssistant().sendMessage(
					"You have no space in your inventory");
		}	
	}
}