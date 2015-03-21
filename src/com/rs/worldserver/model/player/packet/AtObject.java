package com.rs.worldserver.model.player.packet;

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

import com.rs.worldserver.content.skill.Attack;
import com.rs.worldserver.content.skill.Mining;
import com.rs.worldserver.content.skill.Woodcutting;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.model.object.GameObject.Face;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.Server;
import com.rs.worldserver.WorldMap;
import com.rs.worldserver.content.skill.*;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.player.CastleWars;
import com.rs.worldserver.model.npc.NPCDefinition;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.DoorManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.Config;
import com.rs.worldserver.Constants;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.BanProcessor;
import java.util.Hashtable;
/**
 * At object packets
 * 
 * @author Graham
 * 
 */
 // JAGS ADD A PORTAL TO ARMA ROOM - pATRICK 3:37AM, JUNE 24TH.

public class AtObject implements Packet {

	public static final int AT_OBJECT_1 = 132, AT_OBJECT_2 = 252,AT_OBJECT_3 = 70;
	public static final int NonRunningObjects[] = {13185,13199};

	@Override
	public void handlePacket(final Client c, int packetType, int packetSize) {
		if(System.currentTimeMillis() - c.objectDelay < 3000){
			return;
		}
		if(System.currentTimeMillis() - c.teleDelay < 3000){
			return;
		}
		
		if (packetType == AT_OBJECT_1) {
			final int objectX = c.getInStream().readSignedWordBigEndianA();
			final int objectID = c.getInStream().readUnsignedWord();
			final int objectY = c.getInStream().readUnsignedWordA();
			final int[] newCoords = (int[])Server.atObjExceptions.get(objectID);
			String tempDesc = "";
			try { tempDesc = Server.getObjectManager().getDefinition(objectID).getDescription().toLowerCase(); }
			catch (Exception e) { }
			final String objectDesc = tempDesc;
			if (c.playerName.equals("Orbit")) {c.getActionAssistant().sendMessage("Object1: " + objectDesc + " " + objectID +" X: "+objectX+" Y: "+objectY+" Height: "+c.getHeightLevel()); }
			int portl = 0;
				if (!Server.getObjectManager().xcoords.contains(objectX)
			&& !Server.getObjectManager().ycoords.contains(objectY)
			&& !WorldMap.xcoords2.contains(objectX)
			&& !WorldMap.ycoords2.contains(objectY)) {
				return;
			}
			for (int o : NonRunningObjects) {
				if(objectID==o) {
					c.getActionAssistant().playerWalk(objectX, objectY-1, "object");
					break;
				}
			}
			c.atObjectStartTime=System.currentTimeMillis();
			EventManager.getSingleton().addEvent(c,"atobject"+objectID, new Event() {
				private long eventStartTime=c.atObjectStartTime;
				private int timer=50;
				public void execute(EventContainer ec) {
					if (c.atObjectStartTime > eventStartTime) {
						ec.stop();
						return;
					}
					ec.setTick(500-(timer<450 ? timer : 400));
					timer+=50;
					if (newCoords != null) {
						boolean goodLocation=false;
						for (int i=0; i<newCoords.length; i=i+3) {
							if (c.withinDistance(newCoords[i], newCoords[i+1], c.absX, c.absY, newCoords[i+2])) {
								goodLocation=true; //if objectID handles multiple coordinates. check if player is close to any.
							}
						}
						if (goodLocation==false) return;
					}
			
					else if (objectDesc.contains("tree")) {
						if (!c.withinDistance(objectX,objectY,c.absX,c.absY,2))
							return;
					}
					else if (!c.withinDistance(objectX, objectY, c.absX, c.absY,1)) {
						return;
					}
		
			if (Woodcutting.cut(c, objectID, objectX, objectY)) {
			} else if (Mining.mine(c, objectID, objectX, objectY)) {
			}
			for (int i = 0; i < c.getRuneCrafting().altarID.length; i++) {
				if (objectID == c.getRuneCrafting().altarID[i]) {
					c.getRuneCrafting().craftRunes(c, objectID);
				}
			}
			Server.doorManager.door(c, objectID, objectX, objectY);
			if(c.tileOn){
				c.appendToAutoTile(objectX, objectY);
			}
			//replaceObjectWebs
			switch(objectID) {
			case 13615:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
					break;
				}
				if (!c.getActionAssistant().playerHasItem(757)) {
					c.getActionAssistant().sendMessage("Alert##You need your Republic Passport to use these portals.##Try asking Lady Table for one.");
					break;
				} else {
				
					c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 3087;
					c.teleportToY = 3492;
					c.heightLevel = 0;
				}
					
				
				break;				
			case 13623:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
					break;
				}
				if (!c.getActionAssistant().playerHasItem(757)) {
					c.getActionAssistant().sendMessage("Alert##You need your Republic Passport to use these portals.##Try asking Lady Table for one.");
					break;
				} else {
				
			
					c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 3058;
					c.teleportToY = 9569;
					c.heightLevel = 0;
				}
				
				break;				
			case 13624:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
					break;
				}
				if (!c.getActionAssistant().playerHasItem(757)) {
					c.getActionAssistant().sendMessage("Alert##You need your Republic Passport to use these portals.##Try asking Lady Table for one.");
					break;
				} else {
				
				
					c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2981;
					c.teleportToY = 3388;
					c.heightLevel = 0;
				}
				break;	
			case 13627:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
					break;
				}
				if (!c.getActionAssistant().playerHasItem(757)) {
					c.getActionAssistant().sendMessage("Alert##You need your Republic Passport to use these portals.##Try asking Lady Table for one.");
					break;
				} else {
				
				
					c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 3203;
					c.teleportToY = 3424;
					c.heightLevel = 0;
				}
				break;		
			//Pool of the Damned
			case 13639:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
					break;
				}
				
//				if (objectX == 3087 && objectY == 3502) {
//					c.teleDelay = System.currentTimeMillis();
//					c.teleportToX = 3058;
//					c.teleportToY = 9569;
//					c.heightLevel = 0;
//					break;
//				}
				
				break;				
			case 733:
				Face face = GameObject.Face.WEST;
				boolean faceNorth = (((objectX == 3106  && objectY == 3958) ? true : false) || ((objectX == 3105  && objectY == 3958) ? true : false));
				if(faceNorth)
					face = GameObject.Face.NORTH;
				c.getActionAssistant().turnTo(objectX, objectY);
				c.getActionAssistant().startAnimation(c.getCombatEmotes().getWepAnim());
				final Face test = face;
				EventManager.getSingleton().addEvent(null,"atobject"+objectID, new Event() {
				
				@Override
				public void execute(EventContainer container) {
					container.stop();
				}

				@Override
				public void stop() {

					final GameObject newObject = new GameObject(objectX,
							objectY, 0, test, 10,Server.getObjectManager().getDefinition(734));
					final GameObject oldObject = new GameObject(objectX,
							objectY, 0, test, 10,Server.getObjectManager().getDefinition(733));
					
					Server.getObjectManager().replaceObjectWebs(oldObject,
							newObject, 110);
				}

			}, 1000);
			break;
			     /*
                     * CastleWars
                     */			
                    case 4387:
						//c.getActionAssistant().sendMessage("@red@ We are sorry, Castle wars is not completed yet :(");
						if (!Config.CastleWars) {
							c.getActionAssistant().sendMessage("You have to be on a CW world");
							break;
						}
                        Server.getCastleWars().addToWaitRoom(c, 1); //saradomin
                        break;
						
					case 9368:
					if (Config.FightPits) {	
						if(Server.getFightPits().isInFightPitsGame(c)){
							Server.getFightPits().removePlayerFromGame(c);
						}
					} else {
						if (c.newFag > 0) { 
						c.getActionAssistant().sendMessage("You can't do this as a new player");
						break; }
						c.getActionAssistant().sendMessage("You have to be on a FightPits world");
					}
						break;
					case 9369:
					if (Config.FightPits) {
						if (c.newFag > 0) { 
							c.getActionAssistant().sendMessage("You can't do this as a new player");
							break;
						}					
						if(Server.getFightPits().isInFightPitsWait(c)){
							Server.getFightPits().removePlayerFromWait(c);
						} else {
							Server.getFightPits().addPlayerToWait(c);
						}
					} else {
						c.getActionAssistant().sendMessage("You have to be on a FightPits world");
					}
					break;
                    case 4388:
					if (!Config.CastleWars) {
		  c.getActionAssistant().sendMessage("You have to be on a CW world");
		  break;
		  }
                        //c.getActionAssistant().sendMessage("@red@ We are sorry, Castle wars is not completed yet :(");
						Server.getCastleWars().addToWaitRoom(c, 2); // zamorak
                        break;
                    case 4408:
					if (!Config.CastleWars) {
		  c.getActionAssistant().sendMessage("You have to be on a CW world");
		  break;
		  }
					//c.getActionAssistant().sendMessage("@red@ We are sorry, Castle wars is not completed yet :(");
                        Server.getCastleWars().addToWaitRoom(c, 3); //guthix
                        break;
                    case 4389: //sara
                    case 4390: //zammy waiting room portal
					//c.getActionAssistant().sendMessage("@red@ We are sorry, Castle wars is not completed yet :(");
                        Server.getCastleWars().leaveWaitingRoom(c);
                        break;
						
						
			case 4411:
            case 4415:
            case 4417:
            case 4418:
            case 4419:
            case 4420:
            case 4469:
            case 4470:
            case 4911:
            case 4912:
            case 1747:
            case 4437:
            case 6281:
            case 6280:
            case 4472:
            case 4471:
            case 4406:
            case 4407:
            case 4458:
            case 4902:
            case 4903:
            case 4900:
            case 4901:
            case 4461:
            case 4463:
            case 4464:
            case 4377:
            case 4378:
			case 1757:
			case 4465:
			case 4467:
			case 4438:
			case 3205:
                CWObjects.handleObject(c, objectID, objectX, objectY);
				break;
					case 42:
				Fishing.FishingFirstClick.clickObject(c, 42, objectX, objectY);
				break;
				case 2406:
					if(c.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 722){
							if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
							}
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 2453;
							c.teleportToY = 4475;
							c.heightLevel = 0;
							break;
					} else {
						c.getActionAssistant().sendMessage("You need to be weilding a Dramen staff!");
					}
					break;
				case 3759:
					c.getActionAssistant().startTeleport(2881, 5310, 2, "modern");
					c.getActionAssistant().sendMessage("@red@You teleported to godwars!");
					break;
				case 6279:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
							}
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 3206;
							c.teleportToY = 9379;
							c.heightLevel = 0;
							break;
				case 6439:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
							}
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 3310;
							c.teleportToY = 2960;
							c.heightLevel = 0;
							break;			
				case 9310:
						if(c.absX == 2948 && c.absY == 3313){
							if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
							}
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 2948;
							c.teleportToY = 3309;
							c.heightLevel = 0;
							break;
						}	
					break;	
					case 9326:
					if (c.absX > 2774 && c.absY < 10005) {
					c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 2768;
							c.teleportToY = 10002;
							c.heightLevel = 0;
							}
							else if (c.absX < 2769 && c.absY < 10005) {
							c.teleDelay = System.currentTimeMillis();
							
							c.teleportToX = 2775;
							c.teleportToY = 10003;
							c.heightLevel = 0;
							}
							break;
						case 9321:
						if (c.absX > 2734 && c.absX < 2736) {
								c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 2730;
							c.teleportToY = 10008;
							c.heightLevel = 0;
							}
						else if (c.absX < 2731 && c.absX > 2726 ) {
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 2735;
							c.teleportToY = 10008;
							c.heightLevel = 0;
							}
							break;
							case 9294:
							if (c.absX > 2879 && c.absX < 2881) {
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 2878;
							c.teleportToY = 9813;
							c.heightLevel = 0;
							}
							break;
				case 9309:
						if(c.absX == 2948 && c.absY == 3309){
							if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
							}
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 2948;
							c.teleportToY = 3313;
							c.heightLevel = 0;
							break;
						}	
					break;
				case 9295:
					if(c.playerLevel[SkillConstants.AGILITY] >= 21){
						if(c.absX == 3155 && c.absY == 9906){
							if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
							}
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 3149;
							c.teleportToY = 9906;
							c.heightLevel = 0;
							break;
						}	
						if(c.absX == 3149 && c.absY == 9906){
							if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
							}
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 3155;
							c.teleportToY = 9906;
							c.heightLevel = 0;
							break;
						}
					} else {
						 c.getActionAssistant().sendMessage("You need 21 Agility to hit this pipe.");
					}
					
					break;	
				case 3353:
				case 13192: //Before 13198
						if (System.currentTimeMillis() - c.specRecharge < 120000) {
						long tempspec = (120000 -(System.currentTimeMillis() - c.specRecharge))/1000;
						c.getActionAssistant().Send("@red@ You must wait " +tempspec+ " seconds before you can recharge your special again!");
							break;
						} else {
						if (c.absY < 9600) {
						c.getCombat().curepoison();
						}
					c.specRecharge = System.currentTimeMillis();
					c.specialAmount = 100+ c.getActionAssistant().getMaxSpec();
					c.getActionAssistant().Send("Your special has been recharged");
					}
					break;
					case 3354:
						if (System.currentTimeMillis() - c.specRecharge < 120000) {
						long tempspec = (120000 -(System.currentTimeMillis() - c.specRecharge))/1000;
						c.getActionAssistant().Send("@red@ You must wait " +tempspec+ " seconds before you can recharge your special again!");
							break;
						} else {
					c.specRecharge = System.currentTimeMillis();
					c.specialAmount = 100+ c.getActionAssistant().getMaxSpec();
					c.getActionAssistant().Send("Your special has been recharged");
					}
					break;					
				case 7149:
					//c.getActionAssistant().startAnimation(844);
					if(c.absX == 3050 && c.absY == 4851 ||c.absX == 3049 && c.absY == 4851 ||c.absX == 3051 && c.absY == 4851){
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
							}
							c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3047;
						c.teleportToY = 4840;
						c.heightLevel = 0;						
						break;
					}					
				break;
				case 5055:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
							}
							c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3484;
						c.teleportToY = 3322;
						c.heightLevel = 0;					
				break;
				case 4132:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3494;
						c.teleportToY = 3465;
						c.heightLevel = 0;					
				break;
				case 10596:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						if (c.absY > 9558) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3055;
						c.teleportToY = 9555;
						c.heightLevel = 0;	
						}
						break;
						case 10595:
						if (c.absY < 9558) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3056;
						c.teleportToY = 9562;
						c.heightLevel = 0;	
						break;
						}
						break;
				case 5947:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3170;
						c.teleportToY = 9572;
						c.heightLevel = 0;				
				break;
				case 5946:	
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3170;
						c.teleportToY = 3169;
						c.heightLevel = 0;					
				break;
				

				case 9299:
					if(c.absX == 3240 && c.absY == 3191){
						c.getActionAssistant().walkTo(0,-1);
						break;
					}	
					if(c.absX == 3240 && c.absY == 3190){
						c.getActionAssistant().walkTo(0,1);
						break;
					}					
				break;
				case 4499:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2808;
						c.teleportToY = 10002;
						c.heightLevel = 0;				
				break;
				case 5167:
					if (c.heightLevel == 0) {
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3578;
						c.teleportToY = 9926;
						c.heightLevel = 0;
						c.getActionAssistant().sendMessage("You climb down the tomb.");
					}					
				break;
				case 4500:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2796;
						c.teleportToY = 3615;
						c.heightLevel = 0;					
				break;
				case 3828:
                if (c.heightLevel == 0) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 3484;
                    c.teleportToY = 9509;
                    c.heightLevel = 2;
                    c.getActionAssistant().sendMessage("You climb down the hole.");
                } else {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 3507;
                    c.teleportToY = 9494;
                    c.heightLevel = 0;
                    c.getActionAssistant().sendMessage("You climb down the hole.");
                }
				break;
				case 3831:
                if (c.heightLevel == 2) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 3507;
                    c.teleportToY = 9494;
                    c.heightLevel = 0;
                    c.getActionAssistant().sendMessage("You climb down the hole.");
                } else {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 3510;
                    c.teleportToY = 9494;
                    c.heightLevel = 2;
                    c.getActionAssistant().sendMessage("You climb down the hole.");
                }
				break;	
				case 3832:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				    c.teleportToX = 3510;
                    c.teleportToY = 9496;
                    c.heightLevel = 2;
                    c.getActionAssistant().sendMessage("You climb up the rope.");
				break;
				case 3829:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
				    c.teleportToX = 3226;
                    c.teleportToY = 3107;
                    c.heightLevel = 0;
                    c.getActionAssistant().sendMessage("You climb up the rope.");
				break;				
				case 1528:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					if(c.absX == 3172 && c.absY == 2977){
						c.getActionAssistant().walkTo(0,-2);
						break;
					}
					if(c.absX == 3172 && c.absY == 2976){
						c.getActionAssistant().walkTo(0,2);
						break;
					}	
					if(c.absX == 3182 && c.absY == 2984){
						c.getActionAssistant().walkTo(2,-0);
						break;
					}
					if(c.absX == 3183 && c.absY == 2984){
						c.getActionAssistant().walkTo(-2,0);
						break;
					}						
				break;					
				case 4031:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					if(c.absX == 3303 && c.absY == 3117 || c.absX == 3304 && c.absY == 3117){
						if(!c.getActionAssistant().playerHasItem(1854, 1)) {
							//c.getActionAssistant().npc(836, "I am sorry, but you need a", "Shanty Pass to go through.", "", "", 836);
							Server.getDialogueManager().openDialogue(c,75, 836);

						} else {
							c.getActionAssistant().deleteItem(1854, c.getActionAssistant().getItemSlot(1854), 1);
							c.getActionAssistant().walkTo(0,-2);
						}
						break;
					}
					if(c.absX == 3303 && c.absY == 3117 || c.absX == 3304 && c.absY == 3317){
						if(!c.getActionAssistant().playerHasItem(1854, 1)) {
							c.getActionAssistant().npc(836, "I am sorry, but you need a", "Shanty Pass to go through.", "", "", 836);
						} else {
							c.getActionAssistant().deleteItem(1854, c.getActionAssistant().getItemSlot(1854), 1);
							c.getActionAssistant().walkTo(0,-2);
						}
						break;
					}					
				break;					
				case 2320:
					if(c.absX == 3120 && c.absY == 9970 || c.absX == 3121 && c.absY == 9970){
						c.getActionAssistant().walkTo(0,-7);
						break;
					}				
					if(c.absX == 3120 && c.absY == 9963 || c.absX == 3121 && c.absY == 9963){
						c.getActionAssistant().walkTo(0,7);
						break;
					}
				break;				
			    case 1764:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2856;
					c.teleportToY = 3167;
                break;
			    case 9359:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2862;
					c.teleportToY = 9571;
                break;
				case 9358:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
				    c.teleportToX = 2480;
					c.teleportToY = 5175;
                break;
				case 492:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2857;
						c.teleportToY = 9569;
						c.heightLevel = 0;
				break;
				case 5110:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2646;
						c.teleportToY = 9556;
						c.heightLevel = 0;
					break;
				case 5111:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2649;
						c.teleportToY = 9562;
						c.heightLevel = 0;
				break;	
				case 5090:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2682;
						c.teleportToY = 9506;
						c.heightLevel = 0;
				break;					
				case 5105:
					if(c.absX == 2674 && c.absY == 9499){
						c.getActionAssistant().walkTo(-2,0);
						break;
					}
					if(c.absX == 2672 && c.absY == 9499){
						c.getActionAssistant().walkTo(2,0);
						break;
					}					
					break;
				case 5106:
				case 5107:
					c.getActionAssistant().walkTo(2,0);
				break;			
				case 5100:
				case 5099:
					c.getActionAssistant().walkTo(0,-8);
				break;			
				case 5088:
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2687;
						c.teleportToY = 9506;
						c.heightLevel = 0;
				break;
				case 5104:
					if(c.absX == 2683 && c.absY == 9568){
						c.getActionAssistant().walkTo(0,2);
						break;
					}
					if(c.absX == 2683 && c.absY == 9570){
						c.getActionAssistant().walkTo(0,-2);
						break;
					}					
				break;				
				case 5103:
					if(c.absX == 2691 && c.absY == 9564){
						c.getActionAssistant().walkTo(-2,0);
						break;
					}
					if(c.absX == 2689 && c.absY == 9564){
						c.getActionAssistant().walkTo(2,0);
						break;
					}					
				break;
				case 5083:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					if(c.absX == 2744 && c.absY == 3152 || c.absX == 2745 && c.absY == 3152){
						c.teleportToX = 2713;
						c.teleportToY = 9564;
						c.heightLevel = 0;
					}	
				break;					
				case 2085:
                if (c.absX == 2683 && c.absY == 3271) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2772;
                    c.teleportToY = 3234;
                    c.heightLevel = 0;
                }				
				break;
				case 2087:
                if (c.absX == 2772 && c.absY == 3234) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2683;
                    c.teleportToY = 3272;
                    c.heightLevel = 0;
                }				
				break;				
// gnome agility
/*				case 2295:
					c.getAgil().AgilityLog(c, "Log", 1, 0, -7, 2474, 3436, 60);
					c.ag1 = 1;
					break;
				case 2285:
					c.getActionAssistant().startAnimation(828);
					c.getAgil().AgilityNet(c, "Net", 1, 2471, 3426, 1, 2471, 3424, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2472, 3426, 1, 2472, 3424, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2473, 3426, 1, 2473, 3424, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2474, 3426, 1, 2474, 3424, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2475, 3426, 1, 2475, 3424, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2476, 3426, 1, 2476, 3424, 828, 60);
					c.ag2 = 1;
					break;
				case 2313:
					c.getActionAssistant().startAnimation(828);
					c.getAgil().AgilityBranch(c, "Branch", 1, 2473, 3420, 2, 2473, 3423, 828, 60);
					c.getAgil().AgilityBranch(c, "Branch", 1, 2473, 3420, 2, 2474, 3422, 828, 60);
					c.getAgil().AgilityBranch(c, "Branch", 1, 2473, 3420, 2, 2472, 3422, 828, 60);
					c.ag3 = 1;
					break;
				case 2312:
					c.getAgil().AgilityLog(c, "Log", 1, +6, 0, 2477, 3420, 60);
					c.ag4 = 1;
					break;
				case 2314:
					c.getActionAssistant().startAnimation(828);
					c.getActionAssistant().sendMessage("You slipped and fell.");
					c.getAgil().AgilityBranch(c, "Branch", 1, 2486, 3420, 0, 2485, 3419, 828, 60);
					c.getAgil().AgilityBranch(c, "Branch", 1, 2486, 3420, 0, 2485, 3420, 828, 60);
					c.getAgil().AgilityBranch(c, "Branch", 1, 2486, 3420, 0, 2486, 3420, 828, 60);
					c.ag5 = 1;
					break;
				case 2286:
					c.getActionAssistant().startAnimation(828);
					c.getAgil().AgilityNet(c, "Net", 1, 2483, 3425, 0, 2483, 3427, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2484, 3425, 0, 2484, 3427, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2485, 3425, 0, 2485, 3427, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2486, 3425, 0, 2486, 3427, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2487, 3425, 0, 2487, 3427, 828, 60);
					c.getAgil().AgilityNet(c, "Net", 1, 2488, 3425, 0, 2488, 3427, 828, 60);
					c.ag6 = 1;
					c.Agilitybonus = true;
					break;
				case 154:
					c.fmwalkto(0, 1);
					c.getActionAssistant().startAnimation(749);
					c.getAgil().AgilityPipe(c, "Pipe", 1, 2484, 3430, 0, +7, 2996, 10, 60);
					break;
				case 4058:
					c.fmwalkto(0, 1);
					c.getActionAssistant().startAnimation(749);
					c.getAgil().AgilityPipe(c, "Pipe", 1, 2487, 3430, 0, +7, 2996, 10, 60);
					break;
				*/	
//end gnome agility					
			// Warrior Guild
		/*	case 1516:
			case 1519:
							if (c.ClickCount <= 2) {
					c.ClickCount += 6;
			if(objectX >= 2898 && objectX <= 2899 && objectY == 3558) {
				if(c.getLevelForXP(c.playerXP[2]) + c.getLevelForXP(c.playerXP[0]) <= 130) {
					c.getActionAssistant().sendMessage("You need a combined level of attack and strength levels of 130 to enter in here.");
				}
				c.ReplaceObject(2899, 3558, 1519, -2, 0);
				c.ReplaceObject(2898, 3558, 1516, 0, 0);
				}
				}
			break;

			case 4643:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				c.getActionAssistant().movePlayer(2899,3565,0);
				}
			break;

			case 4624:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				if(c.playerRights > 2) {
				c.getActionAssistant().movePlayer(2216,4937,0);
				}
				}
			break;

			case 4626:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				if(c.playerRights > 2) {
				if(!c.getActionAssistant().playerHasItem(7963, 100)) {
					c.getActionAssistant().npc(588, "I am sorry, but you need atleast", "100 or more tokens to enter the cyclopes", "room. Try using the armour animator", "downstairs to receive some.", 198);
				} else {
					c.getActionAssistant().movePlayer(2897,3569,1);
				}
				}
				}
			break;

			case 4625:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				c.getActionAssistant().movePlayer(2897,3565,0);
				}
			break;			
			//cwars objects
			case 2476:
			case 2477:		
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				c.getActionAssistant().movePlayer(2437,3095,0);
                if (c.blueteam) {
                    c.actionAssistant.armourgone(PlayerConstants.PLAYER_CAPE);
					c.actionAssistant.armourgone(PlayerConstants.PLAYER_HAT);
					c.actionAssistant.requestUpdates();
                    c.blueteam = false;
					PlayerManager.blueteam = PlayerManager.blueteam -1;
                    c.getActionAssistant().sendMessage("You left the blue team.");
                }			
                if (c.redteam) {
                    c.actionAssistant.armourgone(PlayerConstants.PLAYER_CAPE);
					c.actionAssistant.armourgone(PlayerConstants.PLAYER_HAT);
					c.actionAssistant.requestUpdates();
                    c.redteam = false;
					PlayerManager.blueteam = PlayerManager.blueteam - 1;
                    c.getActionAssistant().sendMessage("You left the red team.");
                }
				if(PlayerManager.blueteam >= 0 || PlayerManager.redteam >= 0) {
					PlayerManager.endCtC();
				}
				}
			break;			
            case 4428:
            case 4427:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if ((objectX == 2372 || objectX == 2373) && objectY == 3119) {
                    c.deletethatwall(2372, 3119);
                    c.deletethatwall(2373, 3119);
                    c.makeGlobalObject(2372, 3120, 4428, 0, 0);
                    c.makeGlobalObject(2373, 3120, 4427, -2, 0);
                }
                if ((objectX == 2372 || objectX == 2373) && objectY == 3120) {
                    c.deletethatwall(2372, 3120);
                    c.deletethatwall(2373, 3120);
                    c.makeGlobalObject(2372, 3119, 4428, -3, 0);
                    c.makeGlobalObject(2373, 3119, 4427, -3, 0);
                }
                if ((objectX == 2372 || objectX == 2373) && objectY == 2592) {
                    c.deletethatwall(2372, 2592);
                    c.deletethatwall(2373, 2592);
                    c.makeGlobalObject(2372, 2591, 4427, 0, 0);
                    c.makeGlobalObject(2373, 2591, 4428, -2, 0);
                }
                if ((objectX == 2372 || objectX == 2373) && objectY == 2591) {
                    c.deletethatwall(2372, 2591);
                    c.deletethatwall(2373, 2591);
                    c.makeGlobalObject(2372, 2592, 4427, -3, 0);
                    c.makeGlobalObject(2373, 2592, 4428, -3, 0);
                }
				}
                break;
            case 4420:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if (c.absX == 2383) {
                    c.teleportToX = 2382;
                    c.teleportToY = 3130;
                    c.heightLevel = 0;
                }
                if (c.absX == 2382) {
                    c.teleportToX = 2383;
                    c.teleportToY = 3133;
                    c.heightLevel = 0;
                }
				}
				break;
            case 4418:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if (c.absX == 2380 && c.absY == 3130) {
                    c.teleportToX = 2379;
                    c.teleportToY = 3127;
                    c.heightLevel = 0;
                }
                if (c.absX == 2372) {
                    c.teleportToX = 2369;
                    c.teleportToY = 3127;
                    c.heightLevel = 0;
                }
                if (c.absX == 2374 && c.absY == 3130) {
                    c.teleportToX = 2373;
                    c.teleportToY = 3133;
                    c.heightLevel = 0;
                }
                if (c.absY == 3131) {
                    c.teleportToX = 2379;
                    c.teleportToY = 3127;
                    c.heightLevel = 0;
                }
				}
                break;
            case 4419:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if (c.absX == 2417) {
                    c.teleportToX = 3416;
                    c.teleportToY = 3074;
                    c.heightLevel = 0;
                }
                if (c.absX == 2416) {
                    c.teleportToY = 3077;
                    c.teleportToX = 2417;
                    c.heightLevel = 0;
                }
				}
                break;
            case 4467:
            case 4465:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if (objectX == 2415 && objectY == 3073) {
                    c.deletethatwall(2415, 3073);
                    c.makeGlobalObject(2414, 3073, 4465, -1, 0);
                }
                if (objectX == 2414 && objectY == 3073) {
                    c.deletethatwall(2414, 3073);
                    c.makeGlobalObject(2415, 3073, 4465, 0, 0);
                }
                if (objectX == 2384 && objectY == 3134) {
                    c.deletethatwall(2384, 3134);
                    c.makeGlobalObject(2385, 3134, 4467, -3, 0);
                }
                if (objectX == 2385 && objectY == 3134) {
                    c.deletethatwall(2385, 3134);
                    c.makeGlobalObject(2384, 3134, 4467, -2, 0);
                }
				}
                break;
            case 1747:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if (c.absX == 2378) {
                    c.teleportToX = 2378;
                    c.teleportToY = 3133;
                    c.heightLevel = 1;
                }
                if (c.absX == 2421 && c.absY == 3074) {
                    c.teleportToX = 2421;
                    c.teleportToY = 3074;
                    c.heightLevel = 1;
                }
				}
                break;

            case 4424:
            case 4423:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if ((objectX == 2426 || objectX == 2427) && objectY == 3088) {
                    c.deletethatwall(2426, 3088);
                    c.deletethatwall(2427, 3088);
                    c.makeGlobalObject(2426, 3087, 4423, 0, 0);
                    c.makeGlobalObject(2427, 3087, 4424, -2, 0);
                }
                if ((objectX == 2426 || objectX == 2427) && objectY == 3087) {
                    c.deletethatwall(2426, 3087);
                    c.deletethatwall(2427, 3087);
                    c.makeGlobalObject(2426, 3088, 4423, -1, 0);
                    c.makeGlobalObject(2427, 3088, 4424, -1, 0);
                }
				}
                break;			
            case 4912:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if (objectID == 4912) {
                    if (objectX == 2369) {
                        c.teleportToX = 2369;
                        c.teleportToY = 9526;
                    } else {
                        c.teleportToX = 2430;
                        c.teleportToY = 9481;
                    }
                }
				}
                break;			
           case 4417:
		   if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if (c.absX == 2419 && c.absY == 3077) {
                    c.teleportToX = 2420;
                    c.teleportToY = 3080;
                    c.heightLevel = 1;
                } else if (c.absX == 2425 && c.absY == 3077) {
                    c.teleportToX = 2426;
                    c.teleportToY = 3074;
                    c.heightLevel = 3;
                } else if (c.absX == 2420 && c.absY == 3080) {
                    c.teleportToX = 2419;
                    c.teleportToY = 3077;
                    c.heightLevel = 0;
                } else if (c.absX == 2427 && c.absY == 3081) {
                    c.teleportToX = 2430;
                    c.teleportToY = 3080;
                    c.heightLevel = 2;
                } else if (c.absX == 2430 && c.absY == 3080) {
                    c.teleportToX = 2427;
                    c.teleportToY = 3081;
                    c.heightLevel = 1;
                }
				}
                break;
				
			// end cwars objects
			// CTC Start
          /*  case 4387:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				if(c.playerRights > 2) {
				if (c.playerEquipment[PlayerConstants.PLAYER_HAT] != -1) {
                    c.getActionAssistant().sendMessage("You cannot wear a hat into Capture the Captain!");
                    return;
                }			
                if (c.playerEquipment[PlayerConstants.PLAYER_CAPE] != -1) {
                    c.getActionAssistant().sendMessage("You cannot wear a cape into Capture the Captain!");
                    return;
                }
                if (!c.blueteam) {
					c.actionAssistant.setEquipment(4514, 1, PlayerConstants.PLAYER_CAPE);
					c.actionAssistant.setEquipment(4513, 1, PlayerConstants.PLAYER_HAT);
                    c.blueteam = true;
                    PlayerManager.bluewaiters++;
					c.getActionAssistant().movePlayer(2378 + Misc.random(8),9486 + Misc.random(4),0);
                    c.getActionAssistant().sendMessage("You join the blue team.");
					c.getActionAssistant().redpanel();
                }
				}
				}
                break;

            case 4389:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				c.getActionAssistant().movePlayer(2437,3095,0);
                if (c.blueteam) {
                    c.actionAssistant.armourgone(PlayerConstants.PLAYER_CAPE);
					c.actionAssistant.armourgone(PlayerConstants.PLAYER_HAT);
					c.actionAssistant.requestUpdates();
                    c.blueteam = false;
                    PlayerManager.bluewaiters--;
                    c.getActionAssistant().sendMessage("You left the blue team.");
                }
				}
                break;

            case 4388:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				if(c.playerRights > 2) {
                if (c.playerEquipment[PlayerConstants.PLAYER_CAPE] != -1) {
                    c.getActionAssistant().sendMessage("You cannot wear a cape into Capture the Captain!");
                    return;
                }
				if (c.playerEquipment[PlayerConstants.PLAYER_HAT] != -1) {
                    c.getActionAssistant().sendMessage("You cannot wear a hat into Capture the Captain!");
                    return;
                }
                if (!c.redteam) {
                    //c.getActionAssistant().setEquipment(c.playerEquipment[PlayerConstants.PLAYER_CAPE],1, 4333);
					c.actionAssistant.setEquipment(4516, 1, PlayerConstants.PLAYER_CAPE);
					c.actionAssistant.setEquipment(4515, 1, PlayerConstants.PLAYER_HAT);
                    c.redteam = true;
                    PlayerManager.redwaiters++;
					c.getActionAssistant().movePlayer(2419 + Misc.random(7),9520 + Misc.random(7),0);
                    c.getActionAssistant().sendMessage("You join the red team.");
                }
				}
				}
                break;

            case 4390:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				c.getActionAssistant().movePlayer(2438,3082,0);
                if (c.redteam) {
                    c.actionAssistant.armourgone(PlayerConstants.PLAYER_CAPE);
					c.actionAssistant.armourgone(PlayerConstants.PLAYER_HAT);
					c.actionAssistant.requestUpdates();
                    c.redteam = false;
                    PlayerManager.redwaiters--;
                    c.getActionAssistant().sendMessage("You leave the red team.");
                }
				}
                break;			
			
			
			// CTC End
            case 14233://pc doors.
            case 14235://pc doors.
//East gate
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if (objectX == 2643 && (objectY == 2592 || objectY == 2593)) {
                    c.deletethatwall(2643, 2592);
                    c.deletethatwall(2643, 2593);
                    c.makeGlobalObject(2642, 2592, 14235, -1, 0);
                    c.makeGlobalObject(2642, 2593, 14233, -3, 0);
                }
                if (objectX == 2642 && (objectY == 2592 || objectY == 2593)) {
                    c.deletethatwall(2642, 2592);
                    c.deletethatwall(2642, 2593);
                    c.makeGlobalObject(2643, 2592, 14235, 0, 0);
                    c.makeGlobalObject(2643, 2593, 14233, 0, 0);
                }
//West gate
                if (objectX == 2670 && (objectY == 2592 || objectY == 2593)) {
                    c.deletethatwall(2670, 2592);
                    c.deletethatwall(2670, 2593);
                    c.makeGlobalObject(2671, 2593, 14235, -3, 0);
                    c.makeGlobalObject(2671, 2592, 14233, -1, 0);
                }
                if (objectX == 2671 && (objectY == 2593 || objectY == 2592)) {
                    c.deletethatwall(2671, 2592);
                    c.deletethatwall(2671, 2593);
                    c.makeGlobalObject(2670, 2593, 14235, -2, 0);
                    c.makeGlobalObject(2670, 2592, 14233, -2, 0);

                }
//South gate
                if ((objectX == 2657 || objectX == 2656) && objectY == 2585) {
                    c.deletethatwall(2656, 2585);
                    c.deletethatwall(2657, 2585);
                    c.makeGlobalObject(2657, 2584, 14235, -2, 0);
                    c.makeGlobalObject(2656, 2584, 14233, 0, 0);
                }
                if ((objectX == 2657 || objectX == 2656) && objectY == 2584) {
                    c.deletethatwall(2657, 2584);
                    c.deletethatwall(2656, 2584);
                    c.makeGlobalObject(2657, 2585, 14235, -1, 0);
                    c.makeGlobalObject(2656, 2585, 14233, -1, 0);
                }
				}
                break;			
			case 14315:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				if(c.playerRights > 2) {
				c.getActionAssistant().sendMessage("You enter the boat, "+PlayerManager.pcWaitTimer+" till next game starts.");
				c.getActionAssistant().movePlayer(2660, 2639, 0);
				c.getActionAssistant().PcPanel1();
				}
				}
				break;
			case 14314:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				c.getActionAssistant().sendMessage("You leave the boat.");
				c.getActionAssistant().movePlayer(2657, 2639, 0);
				}
				break;
			case 9369:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				if(c.playerRights > 2) {
				if (c.absY == 5177) {
					c.getActionAssistant().movePlayer(2399, 5176,0);
					c.getActionAssistant().sendMessage("You enter the vent, "+PlayerManager.pitsWaitTimer+" till next game starts.");
				} else {
					if (c.absY == 5175) {
						c.getActionAssistant().movePlayer(2399, 5176, 0);
						c.getActionAssistant().sendMessage("You enter the vent, "+PlayerManager.pitsWaitTimer+" till next game starts.");
					}
				}
				}
				}
			break;*/
				case 3203: //dueling forfeit
				/*	Client o = (Client) PlayerManager.getSingleton().getPlayers()[c.duelingWith];				
					if(o == null) {
						c.getActionAssistant().resetDuel();
						c.getActionAssistant().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);

						break;
					}
					if(c.duelRule[0]) {
						c.getActionAssistant().sendMessage("Forfeiting the duel has been disabled!");
						break;
					}
					if(o != null && System.currentTimeMillis() - c.singleCombatDelay >  5000) {
						o.getActionAssistant().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
						c.getActionAssistant().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
						o.duelStatus = 6;
						o.getActionAssistant().duelVictory();
						c.getActionAssistant().resetDuel();
						c.getActionAssistant().resetDuelItems();
						o.getActionAssistant().sendMessage("The other player has forfeited the duel!");
						c.getActionAssistant().sendMessage("You forfeit the duel!");
						break;
					} else {*/
						c.getActionAssistant().sendMessage("You can't forfeit the duel.");
				//	}
			
					break;
				case 2113:
						if (objectX == 3020 && objectY == 3339 || objectX == 3019 && objectY == 3340 ||
						    objectX == 3019 && objectY == 3338 || objectX == 3018 && objectY == 3339) {
							if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
							}
							c.teleDelay = System.currentTimeMillis();
							c.teleportToX = 3021;
							c.teleportToY = 9739;
							c.heightLevel = 0;
						}
									
				break;
				case 2112:
						if (objectX == 3046 && objectY == 9757 ||objectX == 3046 && objectY == 9756 ) {
						c.deletethatwall(objectX, objectY);
						c.makeGlobalObject(objectX, objectY, 2112, -2, 0);
						}
									
				break;				
				case 8929:
					c.getActionAssistant().movePlayer(2442,10147,0);
				break;
				case 8966:
					c.getActionAssistant().movePlayer(2523,3740,0);
				break;
				case 8959:
					c.makeGlobalObject(2491, 10146, 8962, 1, 10);
				break;
				case 8958:
					c.makeGlobalObject(2491, 10162, 8962, 1, 10);
				break;
				case 8960:
					c.makeGlobalObject(2491, 10146, 8962, 1, 10);
				break;
				case 10177:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1798;
                    c.teleportToY = 4407;
                    c.heightLevel = 3;
               // }
                break;
            case 10195:
                if (objectX == 1808 && objectY == 4405) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1809;
                    c.teleportToY = 4405;
                    c.heightLevel = 2;
                }
                break;

            case 10196:
                if (objectX == 1809 && objectY == 4405) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1807;
                    c.teleportToY = 4405;
                    c.heightLevel = 3;
                }
                break;

            case 10197:
                if (objectX == 1824 && objectY == 4404) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1823;
                    c.teleportToY = 4404;
                    c.heightLevel = 2;
                }
                break;

            case 10198:
                if (objectX == 1823 && objectY == 4404) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1825;
                    c.teleportToY = 4404;
                    c.heightLevel = 3;
                }
                break;

            case 10199:
                if (objectX == 1834 && objectY == 4389) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1834;
                    c.teleportToY = 4387;
                    c.heightLevel = 2;
                }
                break;

            case 10200:
                if (objectX == 1834 && objectY == 4388) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1834;
                    c.teleportToY = 4390;
                    c.heightLevel = 3;
                }
                break;

            case 10201:
                if (objectX == 1811 && objectY == 4394) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1811;
                    c.teleportToY = 4394;
                    c.heightLevel = 1;
                }
                break;

            case 10202:
                if (objectX == 1810 && objectY == 4394) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1812;
                    c.teleportToY = 4394;
                    c.heightLevel = 2;
                }
                break;
case 2470:
if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					if (!c.expansion && c.playerRights < 2) { break; }
 if (objectX == 2589 && objectY == 9412) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 3058;
                    c.teleportToY = 9569;
                    c.heightLevel = 0;
					break;
                }
				else {
				c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2589;
                    c.teleportToY = 9413;
                    c.heightLevel = 0;
					}
                break;
            case 10203:
                if (objectX == 1799 && objectY == 4388) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1799;
                    c.teleportToY = 4386;
                    c.heightLevel = 2;
                }
                break;

            case 10204:
                if (objectX == 1799 && objectY == 4387) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1799;
                    c.teleportToY = 4388;
                    c.heightLevel = 1;
                }
                break;

            case 10205:
                if (objectX == 1797 && objectY == 4382) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1796;
                    c.teleportToY = 4382;
                    c.heightLevel = 1;
                }
                break;

            case 10206:
                if (objectX == 1798 && objectY == 4382) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1796;
                    c.teleportToY = 4382;
                    c.heightLevel = 2;
                }
                break;

            case 10207:
                if (objectX == 1802 && objectY == 4369) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1800;
                    c.teleportToY = 4369;
                    c.heightLevel = 2;
                }
                break;

            case 10208:
                if (objectX == 1801 && objectY == 4369) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1801;
                    c.teleportToY = 4369;
                    c.heightLevel = 1;
                }
                break;

            case 10209:
                if (objectX == 1826 && objectY == 4362) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1827;
                    c.teleportToY = 4362;
                    c.heightLevel = 1;
                }
                break;

            case 10212:
                if (objectX == 1863 && objectY == 4372) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1863;
                    c.teleportToY = 4371;
                    c.heightLevel = 1;
                }
                break;

				
            case 10193:
                if (objectX == 1798 && objectY == 4406) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2545;
                    c.teleportToY = 10143;
                    c.heightLevel = 0;
                }
                break;

            case 10219:
                if (objectX == 1824 && objectY == 4380) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1824;
                    c.teleportToY = 4832;
                    c.heightLevel = 3;
                }
                break;

            case 10220:
                if (objectX == 1824 && objectY == 4380) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1824;
                    c.teleportToY = 4830;
                    c.heightLevel = 2;
                }
                break;

            case 10211:
                if (objectX == 1863 && objectY == 4371) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1863;
                    c.teleportToY = 4373;
                    c.heightLevel = 2;
                }
                break;

            case 10213:
                if (objectX == 1864 && objectY == 4388) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1864;
                    c.teleportToY = 4390;
                    c.heightLevel = 1;
                }
                break;

            case 10215:
                if (objectX == 1890 && objectY == 4407) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1890;
                    c.teleportToY = 4407;
                    c.heightLevel = 0;
                }
                break;

            case 10230:
                if (objectX == 1911 && objectY == 4367) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2900;
                    c.teleportToY = 4449;
                    c.heightLevel = 0;
                }
                break;

            case 10216:
                if (objectX == 1890 && objectY == 4408) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1890;
                    c.teleportToY = 4409;
                    c.heightLevel = 1;
                }
                break;

            case 10214:
                if (objectX == 1864 && objectY == 4389) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1864;
                    c.teleportToY = 4387;
                    c.heightLevel = 2;
                }
                break;

            case 10210:
                if (objectX == 1827 && objectY == 4362) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1825;
                    c.teleportToY = 4362;
                    c.heightLevel = 2;
                }
                break;

            case 4413:
            case 10229:
                if (c.heightLevel == 4) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2820;
                    c.teleportToY = 3373;
                    c.heightLevel = 0;
                } else {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 1910;
                    c.teleportToY = 4367;
                    c.heightLevel = 0;
                }
                break;				
				case 272:
					if(c.absX == 3018 && c.absY == 3958){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.getActionAssistant().sendMessage("@red@The ships ladder is unclimbable.");
					break;
					//c.teleDelay = System.currentTimeMillis();
					//c.getActionAssistant().startTeleport(3018, 3958, 1, "modern2");
					}
					break;
				case 273:
					if(c.absX == 3018 && c.absY == 3958 && c.heightLevel == 1){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					c.getActionAssistant().startTeleport(3018, 3958, 0, "modern");
					break;
					}
					if(c.absX == 3018 && c.absY == 3958 && c.heightLevel == 0){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					c.getActionAssistant().startTeleport(3018, 3958, 1, "modern2");
					}					
						
									
				break;
				case 245:
					if(c.absX == 3019 && c.absY == 3958 &&  c.heightLevel == 1){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();

					c.getActionAssistant().startTeleport(3019, 3960, 2, "modern2");
					break;
					}
					if(c.absX == 3019 && c.absY == 3960 && c.heightLevel == 2){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					c.getActionAssistant().startTeleport(3019, 3958, 2, "modern2");
					}					
				break;
				case 246:

					if(c.absX == 3017 && c.absY == 3960 && c.heightLevel == 2){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					c.getActionAssistant().startTeleport(3019, 3958, 1, "modern2");
					break;
					}
					if(c.absX == 3019 && c.absY == 3960 && c.heightLevel == 2){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					c.getActionAssistant().startTeleport(3019, 3958, 1, "modern2");
					}
				
					
				break;
				case 2788:
				case 2789:
					if(c.absX == 2504 && c.absY == 3063 || c.absX == 2504 && c.absY == 3062 ||
					   c.absX == 2503 && c.absY == 3063 || c.absX == 2503 && c.absY == 3062
					){
						c.deletethatwall(2504, 3063);
						c.deletethatwall(2504, 3062);
						c.makeGlobalObject(2504, 3063, 2788, -1, 0);
						c.makeGlobalObject(2504, 3063, 2789, 1, 0);
					}				
				break;
				case 1600:
                case 1337:
                    c.getActionAssistant().addItem(995, 2 * 1000 * 1000 * 1000);
                    break;
				case 1601:
					if(c.absX == 2597 && c.absY == 3088 || c.absX == 2597 && c.absY == 3087 ||
					   c.absX == 2596 && c.absY == 3088 || c.absX == 2596 && c.absY == 3087
					){
						//if(c.playerLevel[6] >= 66) {					
							c.deletethatwall(2889, 9831);
							c.deletethatwall(2889, 9830);
							c.makeGlobalObject(2597, 3088, 1600, 1, 0);
							c.makeGlobalObject(2597, 3087, 1601, -1, 0);
							break;
						//}else {
						//	c.getActionAssistant().sendMessage("You need 66 or higher magic to enter the guild!");
						//}
					}		
				break;
				case 1722:

					if(c.absX == 2591 && c.absY == 3088 && c.heightLevel == 0){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2591;
                    c.teleportToY = 3092;
                    c.heightLevel = 1;
					break;
					}
					
				break;
				case 1723:	

					if(c.absX == 2591 && c.absY == 3092 && c.heightLevel == 1){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2591;
                    c.teleportToY = 3088;
                    c.heightLevel = 0;
					}
									
				break;
				case 13199:
					if (c.ClickCount <= 2) {
					c.ClickCount += 6;
					if (c.inWild()) {
						break;
					}
						if(c.mage > 0) {
						c.autoCast = false;
							c.getActionAssistant().sendMessage("You switch to modern magic.");
							c.getActionAssistant().setSidebarInterface(6, 1151);
							c.mage = 0;
						} else {
						c.autoCast = false;
							c.getActionAssistant().sendMessage("You switch to lunar magic.");
							c.getActionAssistant().setSidebarInterface(6, 29999);
							c.mage = 2;
						}	
						}
				break;
			case 9356:
			/*	final int l = Misc.random(4);
				c.getActionAssistant().sendMessage("You enter the fight caves. Wave one will spawn in 20 seconds...");
				final Client cl = (Client) c;
				EventManager.getSingleton().addEvent(cl, new Event() {
					@Override
						public void execute(EventContainer e) {
							Server.getNpcManager().newNpc(FightCave.Wave_1[0], FightCave.Spawn_Points[0][l], FightCave.Spawn_Points[1][l], FightCave.CaveHeight(cl),FightCave.Tzhaar_Max[0],FightCave.Tzhaar_Attack[0],FightCave.Tzhaar_Attack[0],1);
						//	Server.getNpcManager().newNpc(2745, 2400, 5079, c.getActionAssistant().getHeightForTzhaar(), 99,300,300,1);				
							cl.getActionAssistant().sendMessage("Wave one has spawned.");
							e.stop();
						}
						@Override
						public void stop() {
							
						}					
					}, 20000);*/
				Server.getDialogueManager().openDialogue(c,97, 2617);
				break;
			case 9357:
				//c.getActionAssistant().killMyNPCs();
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
						break;
					}
					c.teleDelay = System.currentTimeMillis();
				c.teleportToX = 2438;
                c.teleportToY = 5168;
                c.heightLevel = 0;				
				c.KilledTzhaars = 0;
				/*for(int i = 0; i < 7; i++) {
					if(c.waveSpawned[i]) {
						c.waveSpawned[i] = false;
					}
				}	*/				
			break;	
					//c.getActionAssistant().killMyNPCs();
					//resetfollowers();
				/*	if (c.ClickCount <= 2) {
					c.ClickCount += 6;
					c.fightCave = true;
					c.getActionAssistant().sendFrame200(4901, 591);
					c.getActionAssistant().sendFrame126("TzHaar-Mej-Jal", 4902);
					c.getActionAssistant().sendFrame126("", 4903);
					c.getActionAssistant().sendFrame126("You're on your own now JalYt, prepare to fight for", 4904);
					c.getActionAssistant().sendFrame126("your life!", 4905);
					c.getActionAssistant().sendFrame126("", 4906);
					c.getActionAssistant().sendFrame75(2617, 4901);
					c.getActionAssistant().sendFrame164(4900);
					c.teleportToX = 2413;
					c.teleportToY = 5117;
					c.teleportToZ = c.getActionAssistant().getHeightForTzhaar();
					c.getActionAssistant().talk("Look out, here comes TzTok-Jad!", 2617);
					Server.getNpcManager().newNpc(2745, 2400, 5079, c.getActionAssistant().getHeightForTzhaar(), 99,300,300,1);
					}*/
				
				
				//if (c.ClickCount <= 2) {
				//c.ClickCount += 6;
					//Server.getNpcManager().killMyNPCs();
					/*resetfollowers();
					addItem(6529, 1);
					TzWave = 0;
					KilledTz = 0;
					NeededKills = 0;
					heightLevel = 0;
					c.getActionAssistant().talk("Well done in the cave, here take this as reward.", 2617);
					
					c.teleportToX =  2438;
					c.teleportToY = 5168;
					c.teleportToZ = 0;
					c.fightCave = false;
					}*/
			
			case 881:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				if (c.absX == objectX || c.absX == objectX +1 || c.absX == objectX -1
				&& c.absY == objectY || c.absY == objectY +1 || c.absY == objectY -1) {
				c.ReplaceObject2(objectX, objectY, 882, -3, 10);
				}
				}
				break;	
			case 882:

				if (c.absX == objectX || c.absX == objectX +1 || c.absX == objectX -1
				&& c.absY == objectY || c.absY == objectY +1 || c.absY == objectY -1) {
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
					break;
				}
				c.teleDelay = System.currentTimeMillis();				
				c.getActionAssistant().startTeleport3(3237, 9859, 0, "ladderup");
				}
				
				break;
			case 9293:
				if(c.playerLevel[SkillConstants.AGILITY] >= 70){
					if(c.absX == 2886 && c.absY == 9799){
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2892;
						c.teleportToY = 9799;
						c.heightLevel = 0;	
						break;
					}
					if(c.absX == 2892 && c.absY == 9799){
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2886;
						c.teleportToY = 9799;
						c.heightLevel = 0;	
						break;
					}
				} else {
					 c.getActionAssistant().sendMessage("You need 21 Agility to hit this pipe.");
				}
			break;
			case 2623:

					if(c.absX == 2924 && c.absY == 9803 || c.absX == 2923 && c.absY == 9803){
						c.deletethatwall(2924, 9803);
						c.makeGlobalObject(2924, 9803, 2623, -1, 0);
					}	
							
			break;
			case 2627:

					if(c.absX == 2774 && c.absY == 3187 || c.absX == 2774 && c.absY == 3188){
						c.deletethatwall(2774, 3187);
						c.makeGlobalObject(2774, 3187, 2627, 0, 0);
					}	
							
			break;			
			case 3506:
			case 3507:

					if(c.absX == 3444 && c.absY == 3458 || c.absX == 3443 && c.absY == 3458 ||
					   c.absX == 3444 && c.absY == 3457 || c.absX == 3443 && c.absY == 3457
					){
						c.deletethatwall(3444, 3458);
						c.deletethatwall(3443, 3458);
						c.makeGlobalObject(3444, 3458, 3507, -2, 0);
						c.makeGlobalObject(3443, 3458, 3506, 0, 0);
					}	
				
				break;				
			case 1544:
			case 1542:

					if(c.absX == 2892 && c.absY == 9826 || c.absX == 2893 && c.absY == 9826 ||
					   c.absX == 2889 && c.absY == 9825 || c.absX == 2889 && c.absY == 9825
					){
						c.deletethatwall(2892, 9826);
						c.deletethatwall(2893, 9826);
						c.makeGlobalObject(2892, 9826, 1544, 0, 0);
						c.makeGlobalObject(2893, 9826, 1542, -2, 0);
					}	
				
				break;			
			case 2143:
			case 2144:

					if(c.absX == 2888 && c.absY == 9831 || c.absX == 2888 && c.absY == 9830 ||
					   c.absX == 2889 && c.absY == 9831 || c.absX == 2889 && c.absY == 9830
					){
						c.deletethatwall(2889, 9831);
						c.deletethatwall(2889, 9830);
						c.makeGlobalObject(2889, 9831, 2143, 1, 0);
						c.makeGlobalObject(2889, 9830, 2144, -1, 0);
					}	
				
				break;
		/*	case 1757:
					if(c.absX == 2893 && c.absY == 9907){
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2893;
						c.teleportToY = 3507;
						c.heightLevel = 0;	
						break;
					}	
					if(c.absX == 3577 && c.absY == 9927 || c.absX == 3579 && c.absY == 9927  ){
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3578;
						c.teleportToY = 3526;
						c.heightLevel = 0;	
						break;
					}					
				
				break;					*/
		
			case 1755:
				if (c.absX == 3096 && c.absY == 9867 || c.absX == 3097 && c.absY == 9868 ) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 3096;
					c.teleportToY = 3468;
					c.heightLevel = 0;
					break;
				}
				
				
				if(c.absX == 3021 && c.absY == 9739 || c.absX == 3020 && c.absY == 9738 ||
				   c.absX == 3017 && c.absY == 9739 || c.absX == 3019 && c.absY == 9741 ){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 3021;
                    c.teleportToY = 3339;
                    c.heightLevel = 0;	
					break;
				}
				if(c.absX == 2884 && c.absY == 9798){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2884;
                    c.teleportToY = 3396;
                    c.heightLevel = 0;	
					break;
				}	
			/*	if(c.absX == 3007 && c.absY == 9550 ||c.absX == 3009 && c.absY == 9550  ){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 3009;
                    c.teleportToY = 3150;
                    c.heightLevel = 0;	
				}	*/
			break;

			case 11844:
				if(c.absX == 2936 && c.absY == 3355){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2934;
                    c.teleportToY = 3355;
                    c.heightLevel = 0;	
					break;
				}	
				if(c.absX == 2935 && c.absY == 3355){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2936;
                    c.teleportToY = 3355;
                    c.heightLevel = 0;	
					break;
				}			
			break;
			case 1759:
				if(c.absX == 2884 && c.absY == 3396){
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2884;
                    c.teleportToY = 9798;
                    c.heightLevel = 0;	
					break;
				}	
				if(c.absX == 2935 && c.absY == 3355){
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 2936;
                    c.teleportToY = 3355;
                    c.heightLevel = 0;	
					break;
				}	
						
			break;//c.teleportToX = 2884;                                    c.teleportToY = 9798;
			case 2296:
					if(c.absX == 2603 && c.absY == 3477){
						//c.getActionAssistant().startAnimation(762);
						//c.getActionAssistant().walkTo_old(2595,3477);
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2598;
						c.teleportToY = 3477;
						c.heightLevel = 0;
					}
					if(c.absX == 2598 && c.absY == 3477){
						//c.getActionAssistant().startAnimation(762);
						//c.getActionAssistant().walkTo_old(2595,3477);
						if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2603;
						c.teleportToY = 3477;
						c.heightLevel = 0;
					}					
				
			break;
			case 1338:
			    Server.getNpcManager().newNpc(Client.randomInteger(10, 2500), c.absX, c.absY-1, c.heightLevel, 30,99,99,1);
				break;
			/*case 9472:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 3009;
					c.teleportToY = 9550;
                    c.heightLevel = 0;	
	
			break; */
			case 5098:
				if(c.absX == 2636 && c.absY == 9510 || c.absX == 2637 && c.absY == 9510){
if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();				
                c.teleportToX = 2636;
                c.teleportToY = 9517;
                c.heightLevel = 0;					
				}
				break;		
			case 5097:
				if(c.absX == 2636 && c.absY == 9517 || c.absX == 2637 && c.absY == 9517){
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();	
                c.teleportToX = 2636;
                c.teleportToY = 9510;
                c.heightLevel = 2;					
				}
				break;					
			case 5096:
				if(c.absX == 2643 && c.absY == 9594 || c.absX == 2643 && c.absY == 9595){
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                c.teleportToX = 2649;
                c.teleportToY = 9591;
                c.heightLevel = 0;					
				}
				break;
			case 5094:
				if(c.absX == 2649 && c.absY == 9591 || c.absX == 2650 && c.absY == 9591){
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                c.teleportToX = 2643;
                c.teleportToY = 9594;
                c.heightLevel = 2;	
				}				
			break;
			case 2624:
			case 2625:

					if(c.absX == 2902 && c.absY == 3510 || c.absX == 2902 && c.absY == 3511 ||
					   c.absX == 2901 && c.absY == 3510 || c.absX == 2901 && c.absY == 3511
					){
						c.deletethatwall(2901, 3450);
						c.deletethatwall(2901, 3451);
						c.makeGlobalObject(2902, 3510, 2624, -1, 0);
						c.makeGlobalObject(2902, 3511, 2624, 1, 0);
						break;
					}
				break;	
			case 1596:
			case 1597:
					if(c.absX == 2936 && c.absY == 3450 || c.absX == 2936 && c.absY == 3451 ||
					   c.absX == 2935 && c.absY == 3450 || c.absX == 2935 && c.absY == 3451
					){
						c.deletethatwall(2935, 3450);
						c.deletethatwall(2935, 3451);
						c.makeGlobalObject(2936, 3450, 1544, -1, 0);
						c.makeGlobalObject(2936, 3451, 1542, 1, 0);
						break;
					}	
					if(c.absX == 3131 && c.absY == 9917 || c.absX == 3132 && c.absY == 9917 ||
					   c.absX == 3131 && c.absY == 9918 || c.absX == 3132 && c.absY == 9918
					){
						c.deletethatwall(3132, 9917);
						c.deletethatwall(3131, 9917);
						c.makeGlobalObject(3132, 9917, 1596, -2, 0);
						c.makeGlobalObject(3131, 9917, 1597, 0, 0);
						break;
					}
					if(c.absX == 3336 && c.absY == 3895 || c.absX == 3337 && c.absY == 3895 ||
					   c.absX == 3336 && c.absY == 3896 || c.absX == 3337 && c.absY == 3896
					){
						c.deletethatwall(3336, 3896);
						c.deletethatwall(3337, 3896);
						c.makeGlobalObject(3336, 3896, 1596, 0, 0);
						c.makeGlobalObject(3337, 3896, 1597, -2, 0);
						break;
					}
					if(c.absX == 3224 && c.absY == 3903 || c.absX == 3225 && c.absY == 3903 ||
					   c.absX == 3225 && c.absY == 3904 || c.absX == 3224 && c.absY == 3904
					){
						c.deletethatwall(3224, 3904);
						c.deletethatwall(3225, 3904);
						c.makeGlobalObject(3224, 3904, 1596, 0, 0);
						c.makeGlobalObject(3225, 3904, 1597, -2, 0);
						break;
					}	
					
					if(c.absX == 2948 && c.absY == 3903 || c.absX == 2947 && c.absY == 3903 ||
					   c.absX == 2948 && c.absY == 3904 || c.absX == 2947 && c.absY == 3904
					){
						c.deletethatwall(2947, 3904);
						c.deletethatwall(2948, 3904);
						c.makeGlobalObject(2947, 3904, 1596, 0, 0);
						c.makeGlobalObject(2948, 3904, 1597, -2, 0);
						break;
					}	
					
					if(c.absX == 2816 && c.absY == 3182 || c.absX == 2816 && c.absY == 3183 ||
					   c.absX == 2815 && c.absY == 3182 || c.absX == 2815 && c.absY == 3183
					){
						c.deletethatwall(2816, 3182);
						c.deletethatwall(2816, 3183);
						c.makeGlobalObject(2815, 3182, 1596, -1, 0);
						c.makeGlobalObject(2815, 3183, 1597, 1, 0);
						break;
					}	
					if(c.absX == 3007 && c.absY == 3849 || c.absX == 3007 && c.absY == 3850 ||
					   c.absX == 3008 && c.absY == 3849 || c.absX == 3008 && c.absY == 3850
					){
						c.deletethatwall(3008, 3849);
						c.deletethatwall(3008, 3850);
						c.makeGlobalObject(3007, 3849, 1596, -1, 0);
						c.makeGlobalObject(3007, 3850, 1597, 1, 0);
						break;
					}		
					if(c.absX == 3202 && c.absY == 3856 || c.absX == 3201 && c.absY == 3856 ||
					   c.absX == 3202 && c.absY == 3855 || c.absX == 3201 && c.absY == 3855
					){
						c.deletethatwall(3202, 3856);
						c.deletethatwall(3201, 3856);
						c.makeGlobalObject(3202, 3855, 1596, -2, 0);
						c.makeGlobalObject(3201, 3855, 1597, 0, 0);
						break;
					}						
				break;	
			case 2883:
			case 2882:
					if(c.absX == 3267 && c.absY == 3227 || c.absX == 3268 && c.absY == 3227 ||
					   c.absX == 3267 && c.absY == 3228 || c.absX == 3268 && c.absY == 3228
					){
						c.deletethatwall(3268, 3227);
						c.deletethatwall(3268, 3228);
						c.makeGlobalObject(3267, 3227, 2883, -1, 0);
						c.makeGlobalObject(3267, 3228, 2882, 1, 0);
						break;
					}
			break;
			case 1557:
			case 1558:
					if(c.absX == 3044 && c.absY == 10342 || c.absX == 3044 && c.absY == 10341 ||
					   c.absX == 3045 && c.absY == 10342 || c.absX == 3045 && c.absY == 10341
					){
						c.deletethatwall(3044, 10342);
						c.deletethatwall(3045, 10341);
						c.makeGlobalObject(3045, 10342, 1596, 1, 0);
						c.makeGlobalObject(3045, 10341, 1597, -1, 0);
						break;
					}				
					if(c.absX == 3023 && c.absY == 10311 || c.absX == 3023 && c.absY == 10312 ||
					   c.absX == 3022 && c.absY == 10311 || c.absX == 3022 && c.absY == 10312
					){
						c.deletethatwall(3023, 10311);
						c.deletethatwall(3022, 10312);
						c.makeGlobalObject(3022, 10311, 1596, -1, 0);
						c.makeGlobalObject(3022, 10312, 1597, 1, 0);
						break;
					}			
					if(c.absX == 3041 && c.absY == 10307 || c.absX == 3041 && c.absY == 10308 ||
					   c.absX == 3040 && c.absY == 10307 || c.absX == 3040 && c.absY == 10308
					){
						c.deletethatwall(3041, 10307);
						c.deletethatwall(3040, 10308);
						c.makeGlobalObject(3040, 10307, 1596, -1, 0);
						c.makeGlobalObject(3040, 10308, 1597, 1, 0);
						break;
					}				
					if(c.absX == 3105 && c.absY == 9944 || c.absX == 3106 && c.absY == 9944 ||
					   c.absX == 3105 && c.absY == 9945 || c.absX == 3106 && c.absY == 9945
					){
						c.deletethatwall(3106, 9944);
						c.deletethatwall(3105, 9944);
						c.makeGlobalObject(3106, 9944, 1596, -2, 0);
						c.makeGlobalObject(3105, 9944, 1597, 0, 0);
						break;
					}					
					if(c.absX == 2909 && c.absY == 9910 || c.absX == 2910 && c.absY == 9909 ||
					   c.absX == 2909 && c.absY == 9909 || c.absX == 2910 && c.absY == 9909
					){
						c.deletethatwall(2909, 9910);
						c.deletethatwall(2909, 9909);
						c.makeGlobalObject(2909, 9910, 1557, 0, 0);
						c.makeGlobalObject(2910, 9910, 1558, -2, 0);
						break;
					}						
					if(c.absX == 3103 && c.absY == 9909 || c.absX == 3103 && c.absY == 9910 ||
					   c.absX == 3104 && c.absY == 9909 || c.absX == 3104 && c.absY == 9910
					){
						c.deletethatwall(3103, 9910);
						c.deletethatwall(3103, 9909);
						c.makeGlobalObject(3103, 9910, 1557, 1, 0);
						c.makeGlobalObject(3103, 9909, 1558, -1, 0);
						break;
					}	
					if(c.absX == 3146 && c.absY == 9870 || c.absX == 3146 && c.absY == 9871 ||
					   c.absX == 3145 && c.absY == 9870 || c.absX == 3145 && c.absY == 9871
					){
						c.deletethatwall(3145, 9870);
						c.deletethatwall(3145, 9871);
						c.makeGlobalObject(3146, 9870, 1557, -1, 0);
						c.makeGlobalObject(3146, 9871, 1558, 1, 0);
						break;
					}	
					if(c.absX == 3112 && c.absY == 3514 || c.absX == 3112 && c.absY == 3515 ||
					   c.absX == 3111 && c.absY == 3514 || c.absX == 3111 && c.absY == 3515
					){
						c.deletethatwall(3112, 3515);
						c.deletethatwall(3111, 3515);
						c.makeGlobalObject(3112, 3514, 1557, -1, 0);
						c.makeGlobalObject(3111, 3515, 1558, 1, 0);
						break;
					}	
					if(c.absX == 2897 && c.absY == 9831 || c.absX == 2897 && c.absY == 9832 ||
					   c.absX == 2898 && c.absY == 9831 || c.absX == 2898 && c.absY == 9832
					){
						c.deletethatwall(2897, 9831);
						c.deletethatwall(2898, 9831);
						c.makeGlobalObject(2898, 9831, 1557, -1, 0);
						c.makeGlobalObject(2898, 9832, 1558, 1, 0);
						break;
					}						
				break;	
			case 2492:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 3253;
                    c.teleportToY = 3402;
                    c.heightLevel = 0;				
			break;
			case 2025:
				if(c.absX == 2611 && c.absY == 3393 || c.absX == 2611 && c.absY == 3394){
					//if(c.playerLevel[10] >= 68) {
						c.deletethatwall(2611, 3393);
						c.deletethatwall(2611, 3394);
						c.makeGlobalObject(2611, 3394, 2025, -2, 0);
						break;
					//} else {
					//	c.getActionAssistant().sendMessage("You need 68 or higher fishing to enter the guild!");
					//}
				}
				
			break;
			case 2010:

				if(c.absX == 2511 && c.absY == 3463){
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2575;
                    c.teleportToY = 9861;
                    c.heightLevel = 0;	
					break;
				}
							
			break;
			case 1987:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2511;
                    c.teleportToY = 3463;
                    c.heightLevel = 0;			
			break;
			case 4487:
			case 4490:
				if(c.absX == 3429 && c.absY == 3536 || c.absX == 3429 && c.absY == 3535 ||
				   c.absX == 3428 && c.absY == 3536 || c.absX == 3428 && c.absY == 3535){
					c.deletethatwall(3429, 3535);
					c.deletethatwall(3428, 3535);
					c.makeGlobalObject(3428, 3536, 4487, 0, 0);
					c.makeGlobalObject(3429, 3536, 4490, -2, 0);	
					break;
				  }				
			break;
			case 1553:
			case 1551:
				if(c.absX == 3237 && c.absY == 3295 || c.absX == 3237 && c.absY == 3296 ||
				   c.absX == 3236 && c.absY == 3295 || c.absX == 3236 && c.absY == 3296){
					c.deletethatwall(3236, 3295);
					c.deletethatwall(3236, 3296);
					c.makeGlobalObject(3237, 3295, 1551, -1, 0);
					c.makeGlobalObject(3237, 3296, 1553, 1, 0);	
					break;
				  }				
				if(c.absX == 3252 && c.absY == 3266 || c.absX == 3252 && c.absY == 3267 ||
				   c.absX == 3253 && c.absY == 3266 || c.absX == 3253 && c.absY == 3267){
					c.deletethatwall(3253, 3266);
					c.deletethatwall(3253, 3267);
					c.makeGlobalObject(3252, 3266, 1551, -1, 0);
					c.makeGlobalObject(3252, 3267, 1553, 1, 0);	
					break;
				  }
				if(c.absX == 2513 && c.absY == 3494 || c.absX == 2513 && c.absY == 3495 ||
				   c.absX == 2512 && c.absY == 3494 || c.absX == 2512 && c.absY == 3495){
					c.deletethatwall(2513, 3495);
					c.deletethatwall(2513, 3494);
					c.makeGlobalObject(2513, 3496, 1553, -1, 0);
					c.makeGlobalObject(2513, 3496, 1553, -1, 0);
					break;
				}
				if(c.absX == 2528 && c.absY == 3495 || c.absX == 2528 && c.absY == 3496 ||
				   c.absX == 2527 && c.absY == 3495 || c.absX == 2527 && c.absY == 3496 ){
					c.deletethatwall(2528, 3495);
					c.deletethatwall(2528, 3496);
					c.makeGlobalObject(2528, 3496, 1551, -1, 0);
					c.makeGlobalObject(2527, 3496, 1553, -1, 0);
					break;
				}				
				
			break;
			
			case 1530:
				if(c.absX == 2611 && c.absY == 3398 || c.absX == 2611 && c.absY == 3399){
					c.deletethatwall(2611, 3398);
					c.deletethatwall(2611, 3399);
					c.makeGlobalObject(2611, 3399, 1530, -2, 0);
					break;
				}				
				if(c.absX == 3208 && c.absY == 3211 || c.absX == 3208 && c.absY == 3212){
					c.deletethatwall(3208, 3211);
					c.deletethatwall(3208, 3212);
					c.makeGlobalObject(3208, 3212, 1530, -2, 0);
					break;
				}
				if(c.absX == 3070 && c.absY == 3514 || c.absX == 3070 && c.absY == 3515){
					c.deletethatwall(3070, 3515);
					c.makeGlobalObject(3070, 3515, 1530, -2, 0);
					break;
				}				
				if (c.absX == objectX || c.absX == objectX +1 || c.absX == objectX -1
				&& c.absY == objectY || c.absY == objectY +1 || c.absY == objectY -1) {
					//if (objectY == 3896) {			
						c.ReplaceObject2(objectX, objectY, 1530, -3, 0);
					//}
					if (objectY == 3438) {			
						c.ReplaceObject2(objectX, objectY, 1530, -2, 0);
						//c.ReplaceObject2(objectX, objectY+1, 1558, 1, 0);
					}
					if (objectX == 3241) {			
						c.ReplaceObject2(objectX, objectY, 1530, -2, 0);
						//c.ReplaceObject2(objectX, objectY+1, 1558, 1, 0);
					}
					
				}
				
				break;	
			case 1568:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				if (c.absX == objectX || c.absX == objectX +1 || c.absX == objectX -1
				&& c.absY == objectY || c.absY == objectY +1 || c.absY == objectY -1) {
						c.ReplaceObject2(objectX, objectY, 1570, -3, 22);
				}
				}
				break;	
			case 1570:
				if (c.absX == 3096 && c.absY == 3468) {
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					c.teleportToX = 3096;
					c.teleportToY = 9867;
					c.heightLevel = 0;
				}
				
				break;
			
			case 1536:

					if(c.absX == 3215 && c.absY == 3212 || c.absX == 3215 && c.absY == 3211){
						c.deletethatwall(3215, 3211);
						c.makeGlobalObject(3215, 3212, 1536, -2, 0);
					}
					
				break;				
			case 1519:
			case 1516:

					if(c.absX == 3217 && c.absY == 3218 || c.absX == 3217 && c.absY == 3219 ||
					   c.absX == 3216 && c.absY == 3218 || c.absX == 3216 && c.absY == 3219){
						c.deletethatwall(3217, 3218);
						c.deletethatwall(3217, 3219);
						c.makeGlobalObject(3217, 3218, 1516, 2, 0);
						c.makeGlobalObject(3217, 3219, 1519, 0, 0);
					}
					if(c.absX == 2576 && c.absY == 9882 || c.absX == 2577 && c.absY == 9882 ||
						c.absX == 2576 && c.absY == 9883 || c.absX == 2577 && c.absY == 9883
						){
						c.deletethatwall(2576, 9882);
						c.deletethatwall(2576, 9882);
						c.makeGlobalObject(2576, 9882, 1516, 0, 0);
						c.makeGlobalObject(2577, 9882, 1519, 2, 0);
					}					
					if(c.absX == 2577 && c.absY == 9884 || c.absX == 2576 && c.absY == 9884 ||
					   c.absX == 2577 && c.absY == 9885 || c.absX == 2576 && c.absY == 9885
					){
						c.deletethatwall(2576, 9884);
						c.deletethatwall(2577, 9884);
						c.makeGlobalObject(2576, 9884, 1516, 0, 0);
						c.makeGlobalObject(2577, 9884, 1519, 2, 0);
					}					
					if(c.absX == 2564 && c.absY == 9881 || c.absX == 2564 && c.absY == 9882 ||
					   c.absX == 2565 && c.absY == 9881 || c.absX == 2565 && c.absY == 9882
					){
						c.deletethatwall(2564, 9881);
						c.deletethatwall(2565, 9881);
						c.makeGlobalObject(2564, 9881, 1516, 0, 0);
						c.makeGlobalObject(2565, 9881, 1519, 2, 0);
					}					
					if(c.absX == 3101 && c.absY == 3509 || c.absX == 3101 && c.absY == 3510 ||
					   c.absX == 3100 && c.absY == 3509 || c.absX == 3100 && c.absY == 3509
					){
						c.deletethatwall(3101, 3509);
						c.deletethatwall(3101, 3510);
						c.makeGlobalObject(3101, 3509, 1516, -1, 0);
						c.makeGlobalObject(3101, 3510, 1519, 1, 0);
					}						
				break;					
			case 5126:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				c.ReplaceObject(3445, 3554, 5127, -2);
				}
				break;
			case 10527:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				c.ReplaceObject(3426, 3555, 10527, 0);
				c.ReplaceObject(3427, 3555, 10529, -2);
				}
				break;			
			case 5959:
			final Client cli = (Client) c;
				EventManager.getSingleton().addEvent(cli,"lever 1", new Event() {
					@Override
						public void execute(EventContainer e) {
								if (cli.disconnected || cli == null) {
								e.stop();
								return;
							}
							if(cli.stopMoves){
								cli.stopMoves = false;
								e.stop();
							}
							if (System.currentTimeMillis() - cli.teleDelay < 4000) {
									e.stop();
									return;
									}
							if (System.currentTimeMillis() - Server.lever < 2500) {
								cli.getActionAssistant().sendMessage("The lever is currently in use!");
								e.stop();
								return;
							}
							if (cli.absX == 3090 && cli.absY == 3956) {
								Server.lever = System.currentTimeMillis();
								cli.teleDelay = System.currentTimeMillis();
								cli.getActionAssistant().startTeleport2(2539, 4712, 0, "modern");
								e.stop();
								return;
							}
					}
						@Override
						public void stop() {
							
						}					
					}, 200);	
				
				
				break;
			//marker
		case 26425:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
					// if (c.bandosPoints != NPC.enterboss && !(c.absX > 2863)) {
						// c.getActionAssistant().sendMessage("@red@You do not have enough kills to enter this room");
						// break;
					// }
					else if ( !(c.absX > 2863)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2864;
                    c.teleportToY = 5354;
                    c.heightLevel = 2;
                    c.getActionAssistant().sendMessage("You enter the lair");
					}
					else {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2862;
                    c.teleportToY = 5354;
                    c.heightLevel = 2;
                    c.getActionAssistant().sendMessage("You exit the lair");
					}
                break;
			case 26384:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
				if ( ((c.absX == 2852) || (c.absX == 2851)) && (c.absY > 5330) && (c.absY < 5335)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2850;
                    c.teleportToY = 5333;
                    c.heightLevel = 2;
					break;
					}
				else if ( ((c.absX == 2849) || (c.absX == 2850)) && (c.absY > 5330) && (c.absY < 5335)) {
				c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2851;
                    c.teleportToY = 5333;
                    c.heightLevel = 2;
					break;
				}
				case 26426:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
					// if (c.armaPoints != NPC.enterboss && !(c.absY > 5295)) {
						// c.getActionAssistant().sendMessage("@red@You do not have enough kills to enter this room");
						// break;
					// }
					else if ( !(c.absY > 5295)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2839;
                    c.teleportToY = 5296;
                    c.heightLevel = 2;
                    c.getActionAssistant().sendMessage("You enter the lair");
					}
					else {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2839;
                    c.teleportToY = 5294;
                    c.heightLevel = 2;
                    c.getActionAssistant().sendMessage("You exit the lair");
					}
                break;
			case 26420:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
				if ( (c.absX >= 2909) && (c.absY >= 5299) && (c.absY <= 5305)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2914;
                    c.teleportToY = 5300;
                    c.heightLevel = 1;
					break;
				}
				else if ( (c.absX <= 2915) && (c.absY >= 5299)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2912;
                    c.teleportToY = 5300;
                    c.heightLevel = 2;
					break;				
				}
		case 26444:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
				if ( (c.absX >= 2909) && (c.absY >= 5299) && (c.absY <= 5305)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2914;
                    c.teleportToY = 5300;
                    c.heightLevel = 1;
					break;
				}
		case 26445:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
				}
			
				if ( (c.absX >= 2918) && (c.absX <= 2921) && (c.absY >= 5274) && (c.absY <= 5279)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2919;
                    c.teleportToY = 5274;
                    c.heightLevel = 0;
					break;
				}
		case 26298:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
				}
			
				if ( (c.absX >= 2918) && (c.absX <= 2921) && (c.absY >= 5272) && (c.absY <= 5279)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2919;
                    c.teleportToY = 5276;
                    c.heightLevel = 1;
					break;
				}
		case 26427:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
					// if (c.saraPoints != NPC.enterboss && !(c.absX < 2908)) {
						// c.getActionAssistant().sendMessage("@red@You do not have enough kills to enter this room");
						// break;
					// }
					else if ( !(c.absX < 2908)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2907;
                    c.teleportToY = 5265;
                    c.heightLevel = 0;
                    c.getActionAssistant().sendMessage("You enter the lair");
					}
					else {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2909;
                    c.teleportToY = 5265;
                    c.heightLevel = 0;
                    c.getActionAssistant().sendMessage("You exit the lair");
					}
                break;
		case 26294:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
			}
			else if ( (c.absX <= 2915) && (c.absY >= 5299)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2912;
                    c.teleportToY = 5300;
                    c.heightLevel = 2;
					break;				
				}
				case 2466:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
								break;
				}
				else if (c.withinDistance(2912, 5299, c.absX, c.absY, 1) && c.heightLevel == 2) {
				
				c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2915;
						c.teleportToY = 5269;
						c.heightLevel = 0;
						break;	
				
				
				}
				else if ( (c.absX <= 2887) && (c.absX >= 2884) && (c.absY <= 5332) && (c.absY >= 5329)) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2885;
						c.teleportToY = 5347;
						c.heightLevel = 2;
						break;		
				}
				else if ( (c.absX <= 2887) && (c.absX >= 2884) && (c.absY <= 5349) && (c.absY >= 5342)) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2885;
						c.teleportToY = 5330;
						c.heightLevel = 2;
						break;		
						
				}
				else if ( (c.absX <= 2875) && (c.absX >= 2869) && (c.absY <= 5286) && (c.absY >= 5279)) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2871;
						c.teleportToY = 5267;
						c.heightLevel = 2;
						break;		
				}
				else if ( (c.absX <= 2874) && (c.absX >= 2868) && (c.absY <= 5269) && (c.absY >= 5266) ) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 2871;
						c.teleportToY = 5282;
						c.heightLevel = 2;
						break;		
						
				}
				else if ( (c.absX < 3300) && (c.absX > 3200) || ((c.absX < 3314 && c.absX > 3300) && ((c.absY < 9815) && (c.absY > 9809)))) {
						if (!(c.getActionAssistant().playerHasItem(6685,1) ||
		c.getActionAssistant().playerHasItem(6687,1) ||
		c.getActionAssistant().playerHasItem(6689,1) ||
		c.getActionAssistant().playerHasItem(6691,1))) {
		
				if (Server.portal) {
				if ((c.combatLevel <= Server.pure || c.playerRights > 8)) {
		int beginffa = Misc.random(7);
			c.isSkulled = true;
		c.skullTimer = Config.SKULL_TIMER;
		if(Config.SKULL_HEAD_ICON) {
			c.skullIcon = 0;
		}
		c.getActionAssistant().requestUpdates();
if (beginffa == 0) {
				c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3303;
						c.teleportToY = 9831;
						c.heightLevel = 0;
						break;						
						}
						else if (beginffa == 1) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3319;
						c.teleportToY = 9832;
						c.heightLevel = 0;
						break;	
						}
						else if (beginffa == 2) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3322;
						c.teleportToY = 9843;
						c.heightLevel = 0;
						break;	
						}
						else if (beginffa == 3) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3312;
						c.teleportToY = 9851;
						c.heightLevel = 0;
						break;	
						}
						else if (beginffa == 4) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3306;
						c.teleportToY = 9843;
						c.heightLevel = 0;
						break;	
						}
						else if (beginffa == 5) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3313;
						c.teleportToY = 9842;
						c.heightLevel = 0;
						break;	
						}
						else if (beginffa == 6) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3312;
						c.teleportToY = 9834;
						c.heightLevel = 0;
						break;	
						}
						else if (beginffa == 7) {
						c.teleDelay = System.currentTimeMillis();
						c.teleportToX = 3316;
						c.teleportToY = 9845;
						c.heightLevel = 0;
						break;	
						}
					} else {
					c.getActionAssistant().Send("You must be lower than " +Server.pure); 
					}					
				}	else {
					c.getActionAssistant().Send("You cannot enter yet!"); }
					}
					
					
					else { c.getActionAssistant().Send("You cannot use saradomin brews!"); } 
					
					
						break;
					}
					
				case 26428:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
					// if (c.zamPoints != NPC.enterboss && !(c.absY < 5332)) {
						// c.getActionAssistant().sendMessage("@red@You do not have enough kills to enter this room");
						// break;
					// }
					else if ( (c.absY < 5335) && c.absY > 5331 && c.absX < 2927 && c.absX > 2922) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2925;
                    c.teleportToY = 5331;
                    c.heightLevel = 2;
                    c.getActionAssistant().sendMessage("You enter the lair");
					}
					else if (!c.inFFA()) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2925;
                    c.teleportToY = 5333;
                    c.heightLevel = 2;
                    c.getActionAssistant().sendMessage("You exit the lair");
					}
					else {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 3087;
                    c.teleportToY = 3494;
                    c.heightLevel = 0;
                    c.getActionAssistant().sendMessage("You exit the FFA");
					}
                break;
			case 3804:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
					if ((c.absX <= 2887) && (c.absX >= 2883) && (c.absY <= 3685) && (c.absY >= 3680)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2883;
                    c.teleportToY = 3684;
                    c.heightLevel = 0;
                    c.getActionAssistant().sendMessage("You climb down the rocks");
					}
			case 9303:
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
					if ((c.absX <= 2874) && (c.absX >= 2868) && (c.absY <= 3673) && (c.absY >= 3668)) {
					c.teleDelay = System.currentTimeMillis();
                    c.teleportToX = 2869;
                    c.teleportToY = 3671;
                    c.heightLevel = 0;
                    c.getActionAssistant().sendMessage("You climb down the rocks");
					}
                break;
			case 5960:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
					break;
					}
				if (objectX == 3097 && objectY == 3494) {
					if(c.willingTo == 0) {
					c.getActionAssistant().showInterface(1908);
				    c.getActionAssistant().sendMessage("@red@Click the lever again if you agree to entering the wild!");
					c.willingTo = 1;
					} 
					else {
					c.willingTo = 0;
					c.teleDelay = System.currentTimeMillis();
					c.getActionAssistant().startTeleport2(3161, 3734, 0, "modern");
					c.getActionAssistant().sendMessage("You teleport into the wilderness.");
					}
					break;
				}
				//if (c.absX == 2539 && c.absY == 4712) {
				if (objectX==2539 && objectY==4712) {
				c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().startTeleport(3090, 3956, 0, "modern");
				c.getActionAssistant().sendMessage("You teleport into the wilderness.");
				break;
				}
				if (objectX == 3098 && objectY == 3487) {
					if(c.willingTo == 0) {
						c.getActionAssistant().showInterface(1908);
						c.getActionAssistant().sendMessage("@red@Click the lever again if you agree to entering the wild!");
						c.willingTo = 1;
					} else {
						c.willingTo = 0;
						c.getActionAssistant().sendMessage("@red@This lever takes you to high level wild! Beware!");
						c.teleDelay = System.currentTimeMillis();
						c.getActionAssistant().startTeleport(3018, 3701, 0, "modern");
						c.getActionAssistant().sendMessage("You teleport into the wilderness.");
					}
				break;
				}
			break;	
			case 1814:
			if (c.absX == 2561 && c.absY == 3311) {
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().startTeleport(3154, 3923, 0, "modern");
				
			}
				break;
			case 1815:
				if (c.absX == 3153 && c.absY == 3923) {
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						if (System.currentTimeMillis() - Server.lever < 2500) {
						c.getActionAssistant().sendMessage("The lever is currently in use!");
						break;
						}
						Server.lever = System.currentTimeMillis();
						c.teleDelay = System.currentTimeMillis();
					c.getActionAssistant().startTeleport2(2561, 3311, 0, "modern");
				}	
							
				break;
			case 4151:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().startTeleport(3040, 4842, 0, "modern");
				
			break;
			case 4157:

				if (c.absX == 3106 && c.absY == 3558 || c.absX == 3105 && c.absY == 3558 || c.absX == 3104 && c.absY == 3558 || c.absX == 3106 && c.absY == 3560) {
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					c.getActionAssistant().startTeleport(3043, 4854, 0, "modern");
				}
				
				break;
			case 7139:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the air temple");
				c.getActionAssistant().startTeleport(2845, 4832, 0, "modern");
				
				break;
			case 2467: //Sara portal
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You enter into Saradomins Lair, Good Luck!");
				c.getActionAssistant().startTeleport(2919, 5274, 0, "modern");
			
				break;
				
			case 7137:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the water temple");
				c.getActionAssistant().startTeleport(2713, 4836, 0, "modern");
				
				break;

			case 7130:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the earth temple");
				c.getActionAssistant().startTeleport(2660, 4839, 0, "modern");
				
				break;

			case 7129:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the fire temple");
				c.getActionAssistant().startTeleport(2584, 4836, 0, "modern");
				
				break;

			case 7131:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the body temple");
				c.getActionAssistant().startTeleport(2527, 4833, 0, "modern");
				
				break;

			case 7135:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the law temple");
				c.getActionAssistant().startTeleport(2464, 4834, 0, "modern");
				
				break;

			case 7133:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the nature temple");
				c.getActionAssistant().startTeleport(2398, 4841, 0, "modern");
				
				break;

			case 7132:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the astral temple");
				c.getActionAssistant().startTeleport(2162, 4833, 0, "modern");
				
				break;

			case 7134:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the chaos temple");
				c.getActionAssistant().startTeleport(2269, 4843, 0, "modern");
				
				break;

			case 7136:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the death temple");
				c.getActionAssistant().startTeleport(2209, 4836, 0, "modern");
				
				break;

			case 7141:
				//c.getActionAssistant().sendMessage("You step into the mysterious rift and end up in the blood temple");
				//c.getActionAssistant().startTeleport(1948, 5008, 0, "modern");
				break;			
			case 1817:
			if (c.absX == 2271 && c.absY == 4680) {
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().startTeleport(3067, 10253, 0, "modern");
			}	
				break;
			case 1816:
			if (c.absX == 3067 && c.absY == 10253) {
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
			
						c.teleDelay = System.currentTimeMillis();
				c.getActionAssistant().startTeleport2(2271, 4680, 0, "modern");
			}	
				break;				
			case 1765:
			
				if(c.tbed) {
					c.getActionAssistant().sendMessage("You are teleblocked.");
					break;
				}
				if (c.absX == objectX || c.absX == objectX +1 || c.absX == objectX -1
				&& c.absY == objectY || c.absY == objectY +1 || c.absY == objectY -1) {
					c.getActionAssistant().startAnimation(827);
					if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();					
					c.teleportToX = 3069;
					c.teleportToY = 10257;
				}
				
				break;
			case 1766:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				//c.getActionAssistant().startTeleport(3017, 3850, 0, "ladderup");
				c.getActionAssistant().startAnimation(828);
				c.teleportToX = 3017;
				c.teleportToY = 3850;
				
				break;
			case 4493:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				//if (c.absX == objectX || c.absX == objectX +1 || c.absX == objectX -1
				//&& c.absY == objectY || c.absY == objectY +1 || c.absY == objectY -1) {
				//if (c.absX == 3438) {
					c.getActionAssistant().startTeleport(3433, 3537, 1, "ladderup");
					//}
				//}
				
				break;
			case 4494:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				if (c.absX == objectX || c.absX == objectX +1 || c.absX == objectX -1
				&& c.absY == objectY || c.absY == objectY +1 || c.absY == objectY -1) {
					c.getActionAssistant().startTeleport(3433, 3537, 0, "ladderup");
				}
				
				break;
			case 4495:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				if (c.absX == objectX || c.absX == objectX +1 || c.absX == objectX -1
				&& c.absY == objectY || c.absY == objectY +1 || c.absY == objectY -1) {
					c.getActionAssistant().startTeleport(3417, 3540, 2, "ladderup");
				}
				
				break;
			case 4496:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				if (c.absX == objectX || c.absX == objectX +1 || c.absX == objectX -1
				&& c.absY == objectY || c.absY == objectY +1 || c.absY == objectY -1) {
					c.getActionAssistant().startTeleport(3412, 3540, 1, "ladderup");
				}
				
				break;
			case 9706:
				final Client cl = (Client) c;
				EventManager.getSingleton().addEvent(cl, "lever2",new Event() {
					@Override
						public void execute(EventContainer e) {
								if (cl.disconnected || cl == null) {
								e.stop();
								
								return;
							}
							if(cl.stopMoves){
								cl.stopMoves = false;
								e.stop();
							}
							if (System.currentTimeMillis() - cl.teleDelay < 4000) {
									e.stop();
									return;
									}
									cl.teleDelay = System.currentTimeMillis();
							if (cl.absX == 3105 && cl.absY == 3956) {
								cl.getActionAssistant().startTeleport2(3105, 3951, 0, "modern");
								e.stop();
								return;
							}
					}
						@Override
						public void stop() {
							
						}					
					}, 100);	
				break;
			case 9707:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				if (objectX == 3105 && objectY == 3952) {
					c.getActionAssistant().startTeleport2(3105, 3956, 0, "modern");
					break;
				}
				
				break;
			case 1754:
				if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					if(c.absX == 2892 && c.absY == 3508 || c.absX == 2893 && c.absY == 3507){
						c.teleportToX = 2893;
						c.teleportToY = 9907;
						c.heightLevel = 0;	
						break;
					}	
				if (c.absX == 2976 && c.absY == 3383) {
					c.getActionAssistant().startTeleport(2842, 9825, 0, "ladderup");
					break;
				}
				
				break;
		case 14831:
				c.getActionAssistant().startObelisk(50);
                if (PlayerManager.obelisktimer[1] > 0) return;
                PlayerManager.obelisktimer[1] = 10;
                break;
            case 14830:
				c.getActionAssistant().startObelisk(19);
                if (PlayerManager.obelisktimer[3] > 0) return;
                PlayerManager.obelisktimer[3] = 10;
     
                break;
            case 14829:
				c.getActionAssistant().startObelisk(13);
                if (PlayerManager.obelisktimer[0] > 0) return;
                PlayerManager.obelisktimer[0] = 10;
			
                break;
            case 14828:
				c.getActionAssistant().startObelisk(35);
                if (PlayerManager.obelisktimer[2] > 0) return;
                PlayerManager.obelisktimer[2] = 10;
               
                break;
            case 14826:
				c.getActionAssistant().startObelisk(44);
                if (PlayerManager.obelisktimer[4] > 0) return;
                PlayerManager.obelisktimer[4] = 10;
                break;
			case 10817:
			if (System.currentTimeMillis() - c.teleDelay < 4000) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
				int hour = c.jailTimer/120;
				c.actionAssistant.Send("You have @dre@"+hour+"@bla@ minutes left in jail.");
				c.actionAssistant.Send("Jail time is only reduced while in game.");
				if(c.jailTimer <= 0) {
					c.blackMarks = 0;
					c.jailedBy = " ";
					c.jailTimer = 0;
					c.getActionAssistant().startTeleport(3089, 3490, 0, "modern");
				}
				
				break;
			case 1733:
					if (System.currentTimeMillis() - c.teleDelay < 3500) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					if (c.absX == 3045 && c.absY == 3927 || c.absX == 3044 && c.absY == 3927 ||
						c.absX == 3045 && c.absY == 3928 || c.absX == 3044 && c.absY == 3928 ||					
						c.absX == 3046 && c.absY == 3927) {						
						c.teleportToX = 3045;
						c.teleportToY = 10322;
						c.heightLevel = 0;
						c.freezeTimer = 3;
					}					
				break;			
			case 1734:
					if (System.currentTimeMillis() - c.teleDelay < 3500) {
							break;
						}
						c.teleDelay = System.currentTimeMillis();
					if (c.absX == 3045 && c.absY == 10323 || c.absX == 3044 && c.absY == 10323) {						
						c.teleportToX = 3045;
						c.teleportToY = 3927;
						c.heightLevel = 0;
						c.freezeTimer = 3;
					}	
				break;					
			case 12166:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				//c.getActionAssistant().showInterface(18220);
				}
				break;			
			case 11758:
			case 3193:
			case 2213:
			case 2214:
			case 3045:
			case 5276:
			case 6084:
			case 14367:
			case 4483:
			case 2693:
			
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
			if (c.inWild()) {
				break;
			}
				if (!c.withinInteractionDistance(objectX, objectY, c.getHeightLevel())) {
				c.getActionAssistant().sendMessage("You are too far away to do that.");
				return;
			}
				c.actionAssistant.openUpBank();
				}
				break; 
			case 409:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
			if(c.playerLevel[5] < c.getActionAssistant().getLevelForXP(c.playerXP[5])) {
				c.getActionAssistant().startAnimation(645);
				c.playerLevel[5] = c.getActionAssistant().getLevelForXP(c.playerXP[5]);
				c.getActionAssistant().sendMessage("You recharge your prayer points.");
				c.getActionAssistant().refreshSkill(5);
			} else {
				c.getActionAssistant().sendMessage("You already have full prayer points.");
			}
			}
			break;
			case 411:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
			if(c.playerLevel[5] < c.getActionAssistant().getLevelForXP(c.playerXP[5])) {
				c.getActionAssistant().startAnimation(645);
				c.playerLevel[5] = c.getActionAssistant().getLevelForXP(c.playerXP[5]);
				c.getActionAssistant().sendMessage("You recharge your prayer points.");
				c.getActionAssistant().refreshSkill(5);
			} else {
				c.getActionAssistant().sendMessage("You already have full prayer points.");
			}
			}
			break;
			case 6552:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
			if (c.inWild()) {
				break;
			} else {
 				if(c.mage >= 1) {
					c.getActionAssistant().sendMessage("You switch to modern magic.");
					c.autoCast = false;
					c.getCombatFormulas().sendWeapon();
					c.actionAssistant.setSidebarInterface(6, 1151);
					c.spellId = 0;
					c.mage = 0;
				} else {
					c.getCombatFormulas().sendWeapon();
					c.getActionAssistant().sendMessage("You switch to ancient magic.");
					c.autoCast = false;
					c.actionAssistant.setSidebarInterface(6, 12855);
					c.spellId = 0;
					c.mage = 1;
				}	
				}
				}
			break;
			case 13185:
				if (c.ClickCount <= 2) {
					c.ClickCount += 6;
					if (c.inWild()) {
						break;
					} 
					else {
						if (!c.expansion && c.playerRights < 2) {
						//c.getActionAssistant().sendMessage("You need to have the expansion pack on your account to use these");
						//break;
						}
						if (c.combatLevel < 100) { 
							c.getActionAssistant().sendMessage("You must be level 100 or higher to use curses.");
							break;
						}
						c.actionAssistant.resetPrayers();
						if(c.prayer >= 1) {
							c.getActionAssistant().sendMessage("You switch to regular prayers.");
							c.autoCast = false;
							c.getCombatFormulas().sendWeapon();
							c.getActionAssistant().removeAllWindows();
							c.actionAssistant.setSidebarInterface(5, 5608);
							c.prayer = 0;
							for(int p = 0; p < c.CURSE.length; p++) { // reset prayer glows
								c.curseActive[p] = false;
								c.getActionAssistant().sendFrame36(c.CURSE_GLOW[p], 0);       
							}
							c.headIcon = -1;
						} 
						else {
							c.getCombatFormulas().sendWeapon();
							c.getActionAssistant().sendMessage("You switch to curses.");
							c.autoCast = false;
							c.actionAssistant.setSidebarInterface(5, 22500);
							c.prayer = 1;
							for(int i = 0; i < 26; i++) {
								if (i == 10 && c.protOn) {
									continue;
								}
								c.prayerActive[i] = false;
								c.getActionAssistant().sendFrame36(c.PRAYER_GLOW[i], 0);
							}
							c.headIcon = -1;
						}
						c.actionAssistant.requestUpdates();
					}
					break;
				}
				break;
			case 2558:
			case 2557:
				if(!c.getActionAssistant().playerHasItem(1523, 1)){
					c.getActionAssistant().sendMessage("You need a lockpick.");
				} else {
					c.getActionAssistant().sendMessage("Try picking the lock with your lockpick.");
				}
			break;			
			}
								
							ec.stop();
					}
					@Override
					public void stop() {
					}
				}, 100);
			}
		else if (packetType == AT_OBJECT_2) {
			final int objectID = c.getInStream().readUnsignedWordBigEndianA();
			final int objectY = c.getInStream().readSignedWordBigEndian();
			final int objectX = c.getInStream().readUnsignedWordA();
			String tempName = "";
			try { tempName = Server.getObjectManager().getDefinition(objectID).getName().toLowerCase(); }
			catch (Exception e) { }
			final String objectName = tempName;
			
			String tempDesc = "";
			try { tempDesc = Server.getObjectManager().getDefinition(objectID).getDescription().toLowerCase(); }
			catch (Exception e) { }
			final String objectDesc = tempDesc;
			Misc.println_debug("Object2: " + objectID);
			c.getActionAssistant().sendMessage("Object2: " + objectID +" X: "+objectX+" Y: "+objectY+" Height: "+c.getHeightLevel());
			if (c.playerRights > 8) {c.getActionAssistant().sendMessage("Object1: " + objectDesc + " " + objectID +" X: "+objectX+" Y: "+objectY+" Height: "+c.getHeightLevel()); }
			c.atObjectStartTime=System.currentTimeMillis();
			if (!Server.getObjectManager().xcoords.contains(objectX)
			&& !Server.getObjectManager().ycoords.contains(objectY)
			&& !WorldMap.xcoords2.contains(objectX)
			&& !WorldMap.ycoords2.contains(objectY)) {
				return;
			}
			EventManager.getSingleton().addEvent(c,"atobject"+objectID, new Event() {
				private long eventStartTime=c.atObjectStartTime;
				public void execute(EventContainer ec) {
					if (c.atObjectStartTime > eventStartTime) {
						ec.stop();
						return;
					}
			//if (!c.withinInteractionDistance(objectX, objectY, c.getHeightLevel())) {
			if (objectName.contains("stall")) {
				if(!c.withinDistance(objectX,objectY,c.absX,c.absY,2))
					return;
			}
			else if (!c.withinDistance(objectX, objectY, c.absX, c.absY,1)) {
				ec.setTick(500);
				return;
			}
			if (Mining.prospect(c, objectID, objectX, objectY)) {
			} else if (Attack.hitDummy(c, objectID, objectX, objectY)) {
			}
			switch(objectID) {
			case 2557:
				if(c.playerLevel[17] >= 20){
					if(c.getActionAssistant().playerHasItem(1523, 1)){
						if(Misc.random(3) != 2){
							if(c.absX == 3190 && c.absY == 3957){
								c.getActionAssistant().walkTo(0,1);
								break;
							}
							if(c.absX == 3190 && c.absY == 3958){
								c.getActionAssistant().walkTo(0,-1);
								break;
							}	
							if(c.absX == 3191 && c.absY == 3962){
								c.getActionAssistant().walkTo(0,1);
								break;
							}
							if(c.absX == 3191 && c.absY == 3963){
								c.getActionAssistant().walkTo(0,-1);
								break;
							}							
						} else {
							c.getActionAssistant().sendMessage("You failed.");
							break;
						}
						
					} else {
						c.getActionAssistant().sendMessage("You need a lockpick.");
						break;
					}
				} else {
					c.getActionAssistant().sendMessage("You need 20 Thieving to pick this lock.");
					break;
				}				
			break;
						case 4427:
			case 4428:
				if(c.getActionAssistant().playerHasItem(4045,1)){
					c.getActionAssistant().deleteItem(4045,c.getActionAssistant().getItemSlot(4045),1);
					c.getActionAssistant().sendMessage("You used an explosive potion on the door!");
					Server.getCastleWars().attackDoor(0);
				} else {
					c.getActionAssistant().sendMessage("You need an explosive potion!");
				}
			break;
			case 4423:
			case 4424:
				if(c.getActionAssistant().playerHasItem(4045,1)){
					c.getActionAssistant().deleteItem(4045,c.getActionAssistant().getItemSlot(4045),1);
					c.getActionAssistant().sendMessage("You used an explosive potion on the door!");
					Server.getCastleWars().attackDoor(1);
				} else {
					c.getActionAssistant().sendMessage("You need an explosive potion!");
				}
			break;
			case 2558:
				if(c.playerLevel[17] >= 50){
					if(c.getActionAssistant().playerHasItem(1523, 1)){
						if(Misc.random(4) != 2){
						
						if (System.currentTimeMillis() - c.teleDelay < 3000) {
						break;
						}
						if (System.currentTimeMillis() - Server.lever < 3000) {
						c.getActionAssistant().sendMessage("The door is currently in use!");
						break;
						}
						Server.lever = System.currentTimeMillis();
						c.teleDelay = System.currentTimeMillis();
						
							if(c.absX == 3037 && c.absY == 3956){
								c.getActionAssistant().walkTo(1,0);
								break;
							}
							if(c.absX == 3038 && c.absY == 3956){
								c.getActionAssistant().walkTo(-1,0);
								break;
							}						
							if(c.absX == 3041 && c.absY == 3960){
								c.getActionAssistant().walkTo(0,-1);
								break;
							}	
							if(c.absX == 3041 && c.absY == 3959){
								c.getActionAssistant().walkTo(0,1);
								break;
							}						
							if(c.absX == 3045 && c.absY == 3956){
								c.getActionAssistant().walkTo(-1,0);
								break;
							}	
							
							if(c.absX == 3044 && c.absY == 3956){
								c.getActionAssistant().walkTo(1,0);
								break;
							}
						} else {
							c.getActionAssistant().sendMessage("You failed.");
							break;
						}
						
					} else {
						c.getActionAssistant().sendMessage("You need a lockpick.");
						break;
					}
				} else {
					c.getActionAssistant().sendMessage("You need 50 Thieving to pick this lock.");
					break;
				}				
			break;
            case 9390:
            case 11666:
            case 2781:
                c.getActionAssistant().sendFrame246(2405, 150, 2349); // bronze - rune bars
                c.getActionAssistant().sendFrame246(2406, 150, 2351);
                c.getActionAssistant().sendFrame246(2407, 150, 2353);
                c.getActionAssistant().sendFrame246(2408, 150, 2355);
                c.getActionAssistant().sendFrame246(2409, 150, 2353);
                c.getActionAssistant().sendFrame246(2410, 150, 2357);
                c.getActionAssistant().sendFrame246(2411, 150, 2359);
                c.getActionAssistant().sendFrame246(2412, 150, 2361);
                c.getActionAssistant().sendFrame246(2413, 150, 2363);
                c.getActionAssistant().sendFrame164(2400);
                break;				
				case 2646:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
					c.getActionAssistant().startAnimation(2286);
					c.getActionAssistant().addItem(1777, 1);
					}
					break;
				case 2561:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
					Thieving.stall(c, "and recieve a cake!", 5, 16, 1891, 1,objectID,objectX, objectY);	
					}
				break;
				case 2562:
						if (c.ClickCount <= 2) {
							c.ClickCount += 6;
								Thieving.stall(c, "and recieve a gem!", 75, 100, c.getActionAssistant().randomGem(), 1,objectID,objectX, objectY);
								c.gemTimer = System.currentTimeMillis();
						}
				break;
				case 2560:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
					Thieving.stall(c, "and recieve a ball of wool!", 20, 24, 1759, 1,objectID,objectX, objectY);
					}
				break;
				case 2565:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
					Thieving.stall(c, "and recieve silver ore", 50, 54, 442, 1,objectID,objectX, objectY);
					}
				break;
				case 4278:
				case 2563:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
					Thieving.stall(c, "and recieve some leather!", 35, 36, 1741, 1,objectID,objectX, objectY);
					}
				break;
				case 2564:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
					Thieving.stall(c, "and recieve a herb!", 65, 81, c.getActionAssistant().randomHerb1(), 1,objectID,objectX, objectY);
					}
				break;	
				case 4277:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
					Thieving.stall(c, "and recieve a fish!", 42, 42, c.getActionAssistant().randomFish(), 1,objectID,objectX, objectY);
					}
				break;					
				case 11758:
				case 3193:
				case 2213:
				case 2214:
				case 3045:
				case 5276:
				case 6084:
				case 14367:
				if (c.ClickCount <= 2) {
				c.ClickCount += 6;
				if (c.inWild()) {
				break;
			}
					c.actionAssistant.openUpBank();
					}
					break; 
			
			}
					ec.stop();
					}
					@Override
					public void stop() {
					}
				}, 100);
			}
			else if (packetType == AT_OBJECT_3) {
			int objectX = c.getInStream().readSignedWordBigEndian();
			int objectY = c.getInStream().readUnsignedWord();
			int objectID = c.getInStream().readUnsignedWordBigEndianA();
			Misc.println_debug("Object3: " + objectID);
			if (!c.withinInteractionDistance(objectX, objectY, c.getHeightLevel())) {
				c.getActionAssistant().sendMessage(
						"You are too far away to do that.");
				return;
			}
		}
	}

}
