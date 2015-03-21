package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.model.player.CastleWars;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.Config;
import com.rs.worldserver.Server;
	import com.rs.worldserver.events.Event;
	import com.rs.worldserver.events.EventContainer;
	import com.rs.worldserver.events.EventManager;
public class CWObjects {

    public static void handleObject(Client c, int id, int x, int y) {
	//System.out.println(id);
     if (!Server.getCastleWars().isInCw(c)) {
            c.getActionAssistant().sendMessage("You have to be in castle wars before you can use these objects");
            return;
        }
		if (!Config.CastleWars) {
		  c.getActionAssistant().sendMessage("You have to be on a CW world");
		  return;
		  }
		final Client d = c;
        switch (id) {
        //cwars middle trapdoors (down)
        	case 1570:
        		 if (x == 2399 && y == 3099 && c.heightLevel == 0) {
                     c.getActionAssistant().movePlayer(2399, 9500, 0);
                 } else if (x == 2400 && y == 3108 && c.heightLevel == 0) {
                	 c.getActionAssistant().movePlayer(2400, 9507, 0);
                 }
        		 break;
        	
        		
        		
        		
            case 4469:
                if (Server.getCastleWars().isZammyTeam(c)) {
                    c.getActionAssistant().sendMessage("You are not allowed in the other teams spawn point.");
                    break;
                }
				else if(c.playerEquipment[3] == 4037 || c.playerEquipment[3] == 4039){
					 c.getActionAssistant().sendMessage("You are not allowed to bring the flag into the spawn point!");
                    break;
				}
                else if (x == 2426) {
                    if (c.getY() == 3080) {// && c.getX() == 2426) {
                        c.getActionAssistant().movePlayer(2426, 3081, c.heightLevel);
                    } else if (c.getY() == 3081) {// && c.getX() == 2426) {
                        c.getActionAssistant().movePlayer(2426, 3080, c.heightLevel);
                    }
                } else if (x == 2422) {
                    if (c.getX() == 2422) {// && c.getY() == 3076) {
                        c.getActionAssistant().movePlayer(2423, 3076, c.heightLevel);
                    } else if (c.getX() == 2423) {//&& c.getY() == 3076) {
                        c.getActionAssistant().movePlayer(2422, 3076, c.heightLevel);
                    }
                }
                break;
            case 4470:
                if (Server.getCastleWars().isSaraTeam(c)) {
                    c.getActionAssistant().sendMessage("You are not allowed in the other teams spawn point.");
                    break;
                }
				else if(c.playerEquipment[3] == 4037 || c.playerEquipment[3] == 4039){
					 c.getActionAssistant().sendMessage("You are not allowed to bring the flag into the spawn point!");
                    break;
				}
                else if (x == 2373 && y == 3126) {
                    if (c.getY() == 3126) { // && c.getX() == 2373) {
                        c.getActionAssistant().movePlayer(2373, 3127, 1);
                    } else if (c.getY() == 3127) {// && c.getX() == 2373) {
                        c.getActionAssistant().movePlayer(2373, 3126, 1);
                    }
                } else if (x == 2377 && y == 3131) {
                    if (c.getX() == 2376) {// && c.getY() == 3131) {
                        c.getActionAssistant().movePlayer(2377, 3131, 1);
                    } else if (c.getX() == 2377) { //&& c.getY() == 3131) {
                        c.getActionAssistant().movePlayer(2376, 3131, 1);
                    }
                }
                break;
            case 4417:
                if (x == 2428 && y == 3081 && c.getX() == 2427 && c.getY() == 3081 && c.heightLevel == 1) {
                    c.getActionAssistant().movePlayer(2430, 3080, 2);
                }
                else if (x == 2425 && y == 3074 && c.heightLevel == 2 /* && c.getX() == 2425 && c.getY() == 3077 */ ) {
                    c.getActionAssistant().movePlayer(2426, 3074, 3);
                }
                else if (x == 2419 && y == 3078 && c.heightLevel == 0) {
                    c.getActionAssistant().movePlayer(2420, 3080, 1);
                }
                break;
            case 4415:
                if (x == 2419 && y == 3080 && c.heightLevel == 1) {
                    c.getActionAssistant().movePlayer(2419, 3077, 0);
                }
                else if (x == 2430 && y == 3081 && c.getX() == 2430 && c.getY() == 3080 && c.heightLevel == 2) {
                    c.getActionAssistant().movePlayer(2427, 3081, 1);
                }
                else if (x == 2425 && y == 3074 && c.heightLevel == 3 && c.getX() == 2426 && c.getY() == 3074) {
                    c.getActionAssistant().movePlayer(2425, 3077, 2);
                }
                else if (x == 2374 && y == 3133 && c.getX() == 2373 && c.getY() == 3133 && c.heightLevel == 3) {
                    c.getActionAssistant().movePlayer(2374, 3130, 2);
                }
                else if (x == 2369 && y == 3126 && c.getX() == 2369 && c.getY() == 3127&& c.heightLevel == 2) {
                    c.getActionAssistant().movePlayer(2372, 3126, 1);
                }
                else if (c.getX() == 2379 && c.getY() == 3127 && c.heightLevel == 1) {
                    c.getActionAssistant().movePlayer(2380, 3130, 0);
                }
                break;
            case 4411:
				if(c.getX() == 2418 && c.getY() == 3126) {
					 c.getActionAssistant().movePlayer(2418, 3125, 0);
				}
				else if(c.getX() == 2418 && c.getY() == 3125) {
					c.getActionAssistant().movePlayer(2419, 3125, 0);
				}
				
				else if(c.getX() == 2419 && c.getY() == 3125) {
					if(x == 2419 && y == 3124){
						c.getActionAssistant().movePlayer(2419, 3124, 0);
					} else {
						c.getActionAssistant().movePlayer(2418,3125,0);
					}
				}
				
				else if(c.getX() == 2419 && c.getY() == 3124) {
					if(x == 2419 && y == 3123) {
						c.getActionAssistant().movePlayer(2419, 3123, 0);
					} else {
						c.getActionAssistant().movePlayer(2419, 3125, 0);
					}
				}
				
				else if(c.getX() == 2419 && c.getY() == 3123){
					if(x == 2420 && y == 3123){
						c.getActionAssistant().movePlayer(2420, 3123, 0);
					} else {
						c.getActionAssistant().movePlayer(2419, 3124, 0);
					}
				}
				else if(c.getX() == 2420 && c.getY() == 3123){
					if(x == 2419 && y == 3123){
						c.getActionAssistant().movePlayer(2419, 3123, 0);
					}
				}
				else if(c.getX() == 2420 && c.getY() == 3122){
					c.getActionAssistant().movePlayer(2420, 3123, 0);
				}
				
				else if(c.getX() == 2378 && c.getY() == 3083){
					c.getActionAssistant().movePlayer(2378, 3084, 0);
				}
				
                else if (x == 2421 && y == 3073 && c.heightLevel == 1) {
                    c.getActionAssistant().movePlayer(c.getX(), c.getY(), 0);
                }
				
				else if(c.getX() == 2378 && c.getY() == 3084){
					if(x == 2378 && y == 3085){
						c.getActionAssistant().movePlayer(2378, 3085, 0);
					}
				}
				
				else if(c.getX() == 2378 && c.getY() == 3085){
					if(x == 2378 && y == 3084){
						c.getActionAssistant().movePlayer(2378, 3084, 0);
					} else {	
						c.getActionAssistant().movePlayer(2377, 3085, 0);
					}
				}
				
				else if(c.getX() == 2377 && c.getY() == 3085){
					if(x == 2377 && y == 3086){
						c.getActionAssistant().movePlayer(2377, 3086, 0);
					} else {	
						c.getActionAssistant().movePlayer(2378, 3085, 0);
					}
				}
				
				else if(c.getX() == 2377 && c.getY() == 3086){
					if(x == 2377 && y == 3087){
						c.getActionAssistant().movePlayer(2377, 3087, 0);
					} else {	
						c.getActionAssistant().movePlayer(2377, 3085, 0);
					}
				}
				
				
				else if(c.getX() == 2377 && c.getY() == 3087){
					if(x == 2377 && y == 3088){
						c.getActionAssistant().movePlayer(2377, 3088, 0);
					} else {	
						c.getActionAssistant().movePlayer(2377, 3086, 0);
					}
				}
				
				else if(c.getX() == 2377 && c.getY() == 3088){
					if(x == 2377 && y == 3087){
						c.getActionAssistant().movePlayer(2377, 3087, 0);
					}
				}
				
				else if(c.getX() == 2377 && c.getY() == 3089){
					c.getActionAssistant().movePlayer(2377, 3088, 0);
				}

                break;
				
            case 4419:
                if (x == 2417 && y == 3074 && c.heightLevel == 0 && c.getX() == 2416 && (c.getY() == 3074 || c.getY() == 3075)) {
					c.getActionAssistant().movePlayer(2417, 3077, 0);
                }
                else if (x == 2417 && y == 3074 && c.heightLevel == 0 && c.getX() == 2417 && c.getY() == 3077) {
                    c.getActionAssistant().movePlayer(2416, 3074, 0);
                }
                break;
            case 4911:
                if (c.getX() == 2421 && c.getY() == 3074 && c.heightLevel == 1) {
                    c.getActionAssistant().movePlayer(2421, 3074, 0);
                }
                else if (c.getX() == 2378 && c.getY() == 3133 && c.heightLevel == 1) {
                    c.getActionAssistant().movePlayer(2378, 3133, 0);
                }
                break;
            case 1747:
                if (c.getX() == 2421 && c.getY() == 3074 && c.heightLevel == 0) {
                    c.getActionAssistant().movePlayer(2421, 3074, 1);
                }
                else if (c.getX() == 2378 && c.getY() == 3133 && c.heightLevel == 0) {
                    c.getActionAssistant().movePlayer(2378, 3133, 1);
                }
                break;
            case 4912:
                if (x == 2430 && y == 3082 && c.heightLevel == 0) {
                    c.getActionAssistant().movePlayer(2430, 9481, 0);
                }
                else if (x == 2369 && y == 3125 && c.heightLevel == 0) {
			
                    c.getActionAssistant().movePlayer(2369, 9525, 0);
                }
                break;
            case 1757:
			if (c.goodDistance(x, y, c.getX(), c.getY(),1)) {
                if (x == 2430 && y == 9482) {
                    c.getActionAssistant().movePlayer(2430, 3081, 0);
                } else  if (x == 2399 && y == 9499) {
					c.getActionAssistant().movePlayer(2396, 3101, 0);
				}
				else  if (x == 2369 && y == 9525) {
					c.getActionAssistant().movePlayer(2369, 3125, 0);
				} 
				else {
                    c.getActionAssistant().movePlayer(2403, 3106, 0);
                }
			}
                break;
			case 3205: 
			if (c.goodDistance(x, y, c.getX(), c.getY(),1)) {
			 if (x == 2396 && y == 3100) {
					c.getActionAssistant().movePlayer(2399, 9500, 0);
                } else if (x == 2403 && y == 9482) {
				
				 } else if (x == 2403 && y == 3107) {
				c.getActionAssistant().movePlayer(2400, 9507, 0);
				}else {
					c.getActionAssistant().movePlayer(2430, 9483, 0);
                }
			}
                break;
            case 4418:
                if (c.getX() == 2380 && c.getY() == 3130) {
                    c.getActionAssistant().movePlayer(2379, 3127, 1);
                }
                else if (x == 2369 && y == 3126 && c.getX() == 2372 && c.getY() == 3126 && c.heightLevel == 1) {
                    c.getActionAssistant().movePlayer(2369, 3127, 2);
                }
                else if (x == 2374 && y == 3131 && c.getX() == 2374 && c.getY() == 3130 && c.heightLevel == 2) {
                    c.getActionAssistant().movePlayer(2373, 3133, 3);
                }
                break;
            case 4420:
                if (x == 2382 && y == 3131 && c.heightLevel == 0) {
                    if (c.getX() >= 2383 && c.getY() > 3130) { // top of stairs
                        c.getActionAssistant().movePlayer(2382, 3130, 0);
                    } else { // bottom of stairs
                        c.getActionAssistant().movePlayer(2383, 3133, 0);
                    }
                }
                break;
			case 4438:
				if (x == 2400 && y == 9512) {
                   if(c.playerEquipment[3] == 1265){
						Server.getCastleWars().updateRocks(x,y,2);
						c.getActionAssistant().sendMessage("You clear the path!");
					} else {
						c.getActionAssistant().sendMessage("You need a bronze pick ax wielded to clear this path!");
					}
                } else if (x == 2391 && y == 9501) {
					if(c.playerEquipment[3] == 1265){
						Server.getCastleWars().updateRocks(x,y,2);
						c.getActionAssistant().sendMessage("You clear the path!");
					} else {
						c.getActionAssistant().sendMessage("You need a bronze pick ax wielded to clear this path!");
					}
				}  else if (x == 2409 && y == 9503){
					if(c.playerEquipment[3] == 1265){
						Server.getCastleWars().updateRocks(x,y,2);
						c.getActionAssistant().sendMessage("You clear the path!");
					} else {
						c.getActionAssistant().sendMessage("You need a bronze pick ax wielded to clear this path!");
					}
				} else if (x == 2401 && y == 9494) {
					if(c.playerEquipment[3] == 1265){
						Server.getCastleWars().updateRocks(x,y,2);
						c.getActionAssistant().sendMessage("You clear the path!");
					} else {
						c.getActionAssistant().sendMessage("You need a bronze pick ax wielded to clear this path!");
					}
				}
				break;
            case 4437:
	
				 if (x == 2400 && y == 9512) {
                   if(c.playerEquipment[3] == 1265){
						Server.getCastleWars().updateRocks(x,y,1);
						c.getActionAssistant().sendMessage("You clear some of the path!");
					} else {
						c.getActionAssistant().sendMessage("You need a bronze pick axe wielded to clear this path!");
					}
                } else if (x == 2391 && y == 9501) {
					if(c.playerEquipment[3] == 1265){
						Server.getCastleWars().updateRocks(x,y,1);
						c.getActionAssistant().sendMessage("You clear some of the path!");
					} else {
						c.getActionAssistant().sendMessage("You need a bronze pick axe wielded to clear this path!");
					}
				} else if (x == 2409 && y == 9503){
					if(c.playerEquipment[3] == 1265){
						Server.getCastleWars().updateRocks(x,y,1);
						c.getActionAssistant().sendMessage("You clear some of the path!");
					} else {
						c.getActionAssistant().sendMessage("You need a bronze pick axe wielded to clear this path!");
					}
				} else if (x == 2401 && y == 9494) {
					if(c.playerEquipment[3] == 1265){
						Server.getCastleWars().updateRocks(x,y,1);
						c.getActionAssistant().sendMessage("You clear some of the path!");
					} else {
						c.getActionAssistant().sendMessage("You need a bronze pick axe wielded to clear this path!");
					}
				}
                break;
            case 1568:
                if (x == 2399 && y == 3099) {
                    c.getActionAssistant().movePlayer(2399, 9500, 0);
                } else {
                    c.getActionAssistant().movePlayer(2400, 9507, 0);
                }
            case 6281:
                c.getActionAssistant().movePlayer(2370, 3132, 2);
                break;
            case 4472:
			if (c.goodDistance(x, y, c.getX(), c.getY(),1)) {
			if (Server.getCastleWars().isZammyTeam(c)) {
				if(c.playerEquipment[3] == 4037 || c.playerEquipment[3] == 4039){
					 c.getActionAssistant().sendMessage("You are not allowed to bring the flag into the spawn point!");
                    break;
				}
                c.getActionAssistant().movePlayer(2370, 3132, 1);
			}
			else {
				c.getActionAssistant().sendMessage("You are not allowed in the other teams spawn point.");
			}
			}
                break;
            case 6280:
                c.getActionAssistant().movePlayer(2429, 3075, 2);
                break;
            case 4471:
			if (c.goodDistance(x, y, c.getX(), c.getY(),1)) {
			if (Server.getCastleWars().isSaraTeam(c)) {
			if(c.playerEquipment[3] == 4037 || c.playerEquipment[3] == 4039){
					 c.getActionAssistant().sendMessage("You are not allowed to bring the flag into the spawn point!");
                    break;
				}
                 c.getActionAssistant().movePlayer(2429, 3075, 1);
			}
			else {
				c.getActionAssistant().sendMessage("You are not allowed in the other teams spawn point.");
			}
			}
             
                break;
            case 4406:
                Server.getCastleWars().removePlayerFromCw(c);
                break;
            case 4407:
                Server.getCastleWars().removePlayerFromCw(c);
                break;
            case 4458:
				if(c.isInSaraSafe() || c.isInZamSafe()){
					c.getActionAssistant().startAnimation(881);
					c.getActionAssistant().addItem(4049, 1);
					c.getActionAssistant().sendMessage("You get some bandages");
				}
                break;
            case 4902: //sara flag
				if(Server.getCastleWars().isZammyTeam(c)){
					Server.getCastleWars().getFlag(c);
				} else {
					if(c.playerEquipment[3] == 4039){
						Server.getCastleWars().captureFlag(c,4039);
						return;
					}
					c.getActionAssistant().sendMessage("@red@ You can not pick up your own flag!");
					return;
				}
				break;
            case 4377:

				   if(Server.getCastleWars().isSaraTeam(c) && c.playerEquipment[3] == 4039){
                        Server.getCastleWars().captureFlag(c,4037);
					}
                      break;
    
            case 4903: //zammy flag
				if(Server.getCastleWars().isSaraTeam(c)){
					Server.getCastleWars().getFlag(c);
				} else {
					if(c.playerEquipment[3] == 4037){
						Server.getCastleWars().captureFlag(c,4037);
						return;
					}
					c.getActionAssistant().sendMessage("@red@ You can not pick up your own flag!");
					return;
				}
			break;
			 case 4465:
			if (c.ClickCount <= 2) {
				c.ClickCount += 6;
                if (x == 2415 && y == 3073) {
                    c.deletethatwall(2415, 3073);
                    c.makeGlobalObject(2414, 3073, 4465, -1, 0);
                }
                if (x == 2414 && y == 3073) {
                    c.deletethatwall(2414, 3073);
                    c.makeGlobalObject(2415, 3073, 4465, 0, 0);
                }
				}
                break;
				case 4467:
				  if (x == 2384 && y == 3134) {
                    c.deletethatwall(2384, 3134);
                    c.makeGlobalObject(2385, 3134, 4467, -3, 0);
                }
                if (x == 2385 && y == 3134) {
                    c.deletethatwall(2385, 3134);
                    c.makeGlobalObject(2384, 3134, 4467, -2, 0);
                }
				break;
            case 4378:
					if(Server.getCastleWars().isZammyTeam(c) && c.playerEquipment[3] == 4037){
						Server.getCastleWars().captureFlag(c,4039);
					} 
					break;
            case 4461: //barricades
                c.getActionAssistant().sendMessage("You get a barricade!");
                c.getActionAssistant().addItem(4053, 1);
                break;
            case 4463: // explosive potion!
                c.getActionAssistant().sendMessage("You get an explosive potion!");
                c.getActionAssistant().addItem(4045, 1);
                break;
            case 4464: //pickaxe table
                c.getActionAssistant().sendMessage("You get a bronzen pickaxe for mining.");
                c.getActionAssistant().addItem(1265, 1);
                break;
            case 4900:
            case 4901:
                Server.getCastleWars().pickupFlag(c,id,x,y,c.heightLevel);
			
            default:
                break;

        }
    }
}
