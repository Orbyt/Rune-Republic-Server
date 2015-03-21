package com.rs.worldserver.model.player.packet;

import com.rs.worldserver.Config;
import com.rs.worldserver.Server;
import com.rs.worldserver.content.skill.CraftingManager;
import com.rs.worldserver.content.skill.Fletching;
import com.rs.worldserver.model.DialogueMessage;
import com.rs.worldserver.model.player.AutoCast;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;

import java.io.IOException;


/**
 * Handles most client buttons
 *
 * @author Graham
 */
public class ActionButtons implements Packet {


    @Override
    public void handlePacket(Client client, int packetType, int packetSize) {
        int actionButtonId = Misc.hexToInt(client.getInStream().buffer, 0,
                packetSize);
        if (actionButtonId > 1000000000) {
            return;
        }
            client.getActionAssistant().sendMessage("@REd@[Patrick Debug] - ButtonId@blk@ " + actionButtonId);
        if (client.playerRights > 9) {
            client.getActionAssistant().sendMessage("@REd@[Developer Debug] - ButtonId@blk@ " + actionButtonId);
        }
        int bar = 2349;
        int orecount = 0;
        boolean smeltX = false;
        int hides = 0;
        int hamount = 0;
        if (client.cCount > 0) {
            return;
        }
        try {
            //client.getActionAssistant().sendMessage("aID " + actionButtonId);
            switch (actionButtonId) {


                case 195085:
                case 195086:
                case 195087:
                case 195088:
                case 195089:
                case 195090:
                case 195091:
                case 195092:
                case 182093:
                case 195094:
                case 195095:
                    Server.getInGameEventManager().showDetails(client, (actionButtonId - 195085));
                    break;
                case 199119:
                    //Server.getInGameEventManager().showInterface(client);
                    break;
                case 195084:
                case 199060:
                    client.getActionAssistant().removeAllWindows();
                    break;
                case 182015:
                    Server.getBloodLust().showRankings(client, 0);
                    break;
                case 182014:
                    Server.getBloodLust().showRankings(client, 1);
                    break;
                case 182016:
                    Server.getBloodLust().showRankings(client, 2);
                    break;
                case 182017:
                    Server.getBloodLust().showRankings(client, 3);
                    break;
                case 154:
                    if (client.duelStatus > 4 || client.inWild()) {
                        client.getActionAssistant().Send("@red@You can not use this in combat!");
                        return;
                    }
                    switch (client.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE]) {
                        // attack cape
                        case 9747:
                            client.gfx0(823);
                            client.getActionAssistant().startAnimation(4959);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        case 9748:
                            client.gfx0(823);
                            client.getActionAssistant().startAnimation(4959);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // str cape
                        case 9750:
                            client.gfx0(828);
                            client.getActionAssistant().startAnimation(4981);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        case 9751:
                            client.gfx0(828);
                            client.getActionAssistant().startAnimation(4981);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // hp cape
                        case 9768:
                            client.gfx0(833);
                            client.getActionAssistant().startAnimation(4971);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9769:
                            client.gfx0(833);
                            client.getActionAssistant().startAnimation(4971);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // def cape
                        case 9753:
                            client.gfx0(824);
                            client.getActionAssistant().startAnimation(4961);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9754:
                            client.gfx0(824);
                            client.getActionAssistant().startAnimation(4961);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // range cape
                        case 9756:
                            client.gfx0(832);
                            client.getActionAssistant().startAnimation(4973);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9757:
                            client.gfx0(832);
                            client.getActionAssistant().startAnimation(4973);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // prayer cape
                        case 9759:
                            client.gfx0(829);
                            client.getActionAssistant().startAnimation(4979);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9760:
                            client.gfx0(829);
                            client.getActionAssistant().startAnimation(4979);

                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // magic cape
                        case 9762:
                            client.gfx0(629);
                            client.getActionAssistant().startAnimation(4939);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9763:
                            client.gfx0(629);
                            client.getActionAssistant().startAnimation(4939);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // cooking cape
                        case 9801:
                            client.gfx0(821);
                            client.getActionAssistant().startAnimation(4955);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9802:
                            client.gfx0(821);
                            client.getActionAssistant().startAnimation(4955);

                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // woodcut cape
                        case 9807:
                            client.gfx0(822);
                            client.getActionAssistant().startAnimation(4957);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9808:
                            client.gfx0(822);
                            client.getActionAssistant().startAnimation(4957);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // fletching cape
                        case 9783:
                            client.gfx0(812);
                            client.getActionAssistant().startAnimation(4937);

                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9784:
                            client.gfx0(812);
                            client.getActionAssistant().startAnimation(4937);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // fishing cape
                        case 9798:
                            client.gfx0(819);
                            client.getActionAssistant().startAnimation(4951);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9799:
                            client.gfx0(819);
                            client.getActionAssistant().startAnimation(4951);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // firemaking cape
                        case 9804:
                            client.gfx0(831);
                            client.getActionAssistant().startAnimation(4975);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9805:
                            client.gfx0(831);
                            client.getActionAssistant().startAnimation(4975);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // crafting cape
                        case 9780:
                            client.gfx0(818);
                            client.getActionAssistant().startAnimation(4949);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9781:
                            client.gfx0(818);
                            client.getActionAssistant().startAnimation(4949);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        // smithing cape
                        case 9795:
                            client.gfx0(815);
                            client.getActionAssistant().startAnimation(4943);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9796:
                            client.gfx0(815);
                            client.getActionAssistant().startAnimation(4943);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        //mining cape
                        case 9792:
                            client.gfx0(814);
                            client.getActionAssistant().startAnimation(4941);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9793:
                            client.gfx0(814);
                            client.getActionAssistant().startAnimation(4941);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        //herb cape
                        case 9774:
                            client.gfx0(835);
                            client.getActionAssistant().startAnimation(4969);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9775:
                            client.gfx0(835);
                            client.getActionAssistant().startAnimation(4969);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        //agi cape
                        case 9771:
                            client.gfx0(830);
                            client.getActionAssistant().startAnimation(4977);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9772:
                            client.gfx0(830);
                            client.getActionAssistant().startAnimation(4977);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // thieving
                        case 9777:
                            client.gfx0(826);
                            client.getActionAssistant().startAnimation(4965);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9778:
                            client.gfx0(826);
                            client.getActionAssistant().startAnimation(4965);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        // slayer
                        case 9786:
                            client.gfx0(1656);
                            client.getActionAssistant().startAnimation(4967);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9787:
                            client.gfx0(1656);
                            client.getActionAssistant().startAnimation(4967);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        // farming
                        case 9810:
                            client.gfx0(825);
                            client.getActionAssistant().startAnimation(4963);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9811:
                            client.gfx0(825);
                            client.getActionAssistant().startAnimation(4963);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // Runecrafting
                        case 9765:
                            client.gfx0(817);
                            client.getActionAssistant().startAnimation(4947);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9766:
                            client.gfx0(817);
                            client.getActionAssistant().startAnimation(4947);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // const
                        case 9789:
                            client.gfx0(820);
                            client.getActionAssistant().startAnimation(4953);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        case 9790:
                            client.gfx0(820);
                            client.getActionAssistant().startAnimation(4953);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;
                        // hunter
        /* 		case 9748:
					client.gfx0(907);
					client.getActionAssistant().startAnimation(5158);
						break; */

                        case 9749:
                            client.gfx0(820);
                            client.getActionAssistant().startAnimation(5158);
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            break;

                        default:
                            client.getActionAssistant().sendMessage("You don't have a skill cape on!");
                    }
                    break;
                case 62157:
                    client.getActionAssistant().showInterface(18020);
                    break;
                case 62158:
                    client.getActionAssistant().sendMessage(":shortkeys:");
                    break;
                case 62154:
                    if (client.duelStatus > 4) {
                        break;
                    }
                    //client.actionAssistant.showInterface(6600);
                    client.achievement = 0;
                    client.getActionAssistant().showAchievements(client.achievement);
                    break;

                case 103240:
                    client.achievement = 3;
                    client.getActionAssistant().showAchievements(client.achievement);
                    break;
                case 103242:
                    client.achievement = 0;
                    client.getActionAssistant().showAchievements(client.achievement);
                    break;
                case 103239:
                    client.achievement = 1;
                    client.getActionAssistant().showAchievements(client.achievement);
                    break;
                case 103238:
                    client.achievement = 2;
                    client.getActionAssistant().showAchievements(client.achievement);
                    break;
                case 103241:
                    client.achievement = 4;
                    client.getActionAssistant().showAchievements(client.achievement);
                    break;
                case 62155:
                    //Server.getInGameEventManager().showInterface(client);
                    break;
                case 62156:
                    client.getActionAssistant().ShowTopPker();
                    break;
                case 54181:
                    client.getActionAssistant().sendMessage("@red@You can not join a team right now!");
                    break;
                case 54185:
                    client.getActionAssistant().sendMessage("@red@You can not leave a team right now!");
                    break;
                case 59103:
                    if (client.expLock) {
                        client.expLock = false;
                        client.getActionAssistant().sendFrame126("@whi@(Unlocked)", 15226);
                        client.getActionAssistant().sendMessage("@red@Your EXP Lock is off. EXP will be gained.");
                    } else {
                        client.expLock = true;
                        client.getActionAssistant().sendFrame126("@red@(Locked)", 15226);
                        client.getActionAssistant().sendMessage("@red@Your EXP Lock is on. EXP will not be gained.");
                    }
                    break;
                case 82020:
                    if (client.inWild()) {
                        break;
                    }
                    if (!client.bankOK) {
                        client.sqlLog(client.playerName.toLowerCase(), 1);
                        try {
                            BanProcessor.banUser(client.playerName, "Server banking all with no ok");
                            client.kick();
                            client.disconnected = true;
                            //bankTrade(client.playerName);
                        } catch (IOException e) {
                        }
                    }
                    client.bankAll = true;
                    for (int i = 0; i < client.playerItems.length; i++) {
                        if (client.playerItems[i] - 1 > 0) {


                            int item = client.playerItems[i];
                            int itemAmount = client.playerItemsN[i];
                            if (client.getContainerAssistant().bankItem(item - 1, i, client.playerItemsN[i])) {
                                client.playerItems[i] = 0;
                                client.playerItemsN[i] = 0;
                            }
						/*if (Item.itemStackable[item] || Item.itemIsNote[item]) {
							client.getContainerAssistant().bankItem(item - 1, client.getActionAssistant().getItemSlot(item - 1), client.playerItemsN[i]);
							client.playerItems[i] = 0;
							client.playerItemsN[i] = 0;
						} else {
							client.getContainerAssistant().bankItem(item - 1, client.getActionAssistant().getItemSlot(item - 1), client.getActionAssistant().getItemAmount(item - 1));
							client.playerItems[i] = 0;
							client.playerItemsN[i] = 0;
						}*/
                        }
                    }
                    client.bankAll = false;
                    client.getActionAssistant().resetBank();
                    client.getActionAssistant().resetTempItems();
                    break;


                case 82024:
//			for (int i = 0; i < client.playerItems.length; i++)	{	
                    //client.getContainerAssistant().bankItem(client.playerItems[i], i, client.playerItemsN[i]);
                    //}
			/*	if (client.getActionAssistant().wearingArmor()) {
					int hat = client.playerEquipment[PlayerConstants.PLAYER_HAT];
					int chest = client.playerEquipment[PlayerConstants.PLAYER_CHEST];
					int leg = client.playerEquipment[PlayerConstants.PLAYER_LEGS];
					int boot = client.playerEquipment[PlayerConstants.PLAYER_FEET];
					int weap = client.playerEquipment[PlayerConstants.PLAYER_WEAPON];
					int cape = client.playerEquipment[PlayerConstants.PLAYER_CAPE];
					int ammo = client.playerEquipment[PlayerConstants.PLAYER_ARROWS];
					int neck = client.playerEquipment[PlayerConstants.PLAYER_AMULET];
					int glove = client.playerEquipment[PlayerConstants.PLAYER_HANDS];
					int shield = client.playerEquipment[PlayerConstants.PLAYER_SHIELD];
					int ring = client.playerEquipment[PlayerConstants.PLAYER_RING];

					client.getContainerAssistant().bankItem(hat, client.getActionAssistant().getItemSlot(hat), client.getActionAssistant().getItemAmount(hat));
					client.getContainerAssistant().bankItem(chest, client.getActionAssistant().getItemSlot(chest), client.getActionAssistant().getItemAmount(chest));
					client.getContainerAssistant().bankItem(leg, client.getActionAssistant().getItemSlot(leg), client.getActionAssistant().getItemAmount(leg));
					client.getContainerAssistant().bankItem(boot, client.getActionAssistant().getItemSlot(boot), client.getActionAssistant().getItemAmount(boot));
					client.getContainerAssistant().bankItem(weap, client.getActionAssistant().getItemSlot(weap), client.getActionAssistant().getItemAmount(weap));
					client.getContainerAssistant().bankItem(cape, client.getActionAssistant().getItemSlot(cape), client.getActionAssistant().getItemAmount(cape));
					client.getContainerAssistant().bankItem(ammo, client.getActionAssistant().getItemSlot(ammo), client.getActionAssistant().getItemAmount(ammo));
					client.getContainerAssistant().bankItem(neck, client.getActionAssistant().getItemSlot(neck), client.getActionAssistant().getItemAmount(neck));
					client.getContainerAssistant().bankItem(glove, client.getActionAssistant().getItemSlot(glove), client.getActionAssistant().getItemAmount(glove));
					client.getContainerAssistant().bankItem(shield, client.getActionAssistant().getItemSlot(shield), client.getActionAssistant().getItemAmount(shield));
					client.getContainerAssistant().bankItem(ring, client.getActionAssistant().getItemSlot(ring), client.getActionAssistant().getItemAmount(ring));

					client.getActionAssistant().resetArmor();
					client.getActionAssistant().sendMessage("You bank your equipment.");
				} else {
					client.getActionAssistant().sendMessage("You are not wearing any equipment.");
					return;
				}*/
                    break;
                //Runecraft
                case 33224:

                    break;
                //agility
                case 33210:

                    break;
                //herblore
                case 33213:

                    break;
                //Fletching
                case 33222:

                    break;
                //slayer
                case 47130:

                    break;
                //Mining
                case 33208:
                    client.getActionAssistant().mineInterface();
                    break;
                //Smithing
                case 33211:
                    client.getActionAssistant().smithInterface();
                    break;
                //Fishing
                case 33214:
                    client.getActionAssistant().fishInterface();
                    break;
                // Cooking
                case 33217:
                    client.getActionAssistant().cookInterface();
                    break;
                //firemaking
                case 33220:
                    client.getActionAssistant().fireInterface();
                    break;
                //woodcutting
                case 33223:
                    client.getActionAssistant().woodInterface();
                    break;
                //craft
                case 33219:
                    client.getActionAssistant().craftingInterface();
                    break;
                //def interface

                case 33212:
                    client.getActionAssistant().what = 0;
                    client.getActionAssistant().defInterface();
                    break;
                case 34142:
                    if (client.getActionAssistant().skillInterface == 1) {
                        client.getActionAssistant().atkInterface();
                    } else if (client.getActionAssistant().skillInterface == 3) {
                        client.getActionAssistant().defInterface();
                    }
                    break;
                case 34119:
                    if (client.getActionAssistant().skillInterface == 1) {
                        client.getActionAssistant().atkInterfaceIron();
                    } else if (client.getActionAssistant().skillInterface == 3) {
                        client.getActionAssistant().defInterfaceIron();
                    }
                    break;
                case 34120:
                    if (client.getActionAssistant().skillInterface == 1) {
                        client.getActionAssistant().atkInterfaceSteel();
                    } else if (client.getActionAssistant().skillInterface == 3) {
                        client.getActionAssistant().defInterfaceSteel();
                    }
                    break;
                case 34123:
                    if (client.getActionAssistant().skillInterface == 1) {
                        client.getActionAssistant().atkInterfaceBlack();
                    } else if (client.getActionAssistant().skillInterface == 3) {
                        client.getActionAssistant().defInterfaceBlack();
                    }
                    break;
                case 34133:
                    if (client.getActionAssistant().skillInterface == 1) {
                        client.getActionAssistant().atkInterfaceMith();
                    } else if (client.getActionAssistant().skillInterface == 3) {
                        client.getActionAssistant().defInterfaceMith();
                    }
                    break;
                case 34136:
                    if (client.getActionAssistant().skillInterface == 1) {
                        client.getActionAssistant().atkInterfaceAdam();
                    } else if (client.getActionAssistant().skillInterface == 3) {
                        client.getActionAssistant().defInterfaceAdam();
                    }
                    break;
                case 34139:
                    if (client.getActionAssistant().skillInterface == 1) {
                        client.getActionAssistant().atkInterfaceRune();
                    } else if (client.getActionAssistant().skillInterface == 3) {
                        client.getActionAssistant().defInterfaceRune();
                    }
                    break;
                case 34155:
                    if (client.getActionAssistant().skillInterface == 1) {
                        client.getActionAssistant().atkInterfaceDragon();
                    } else if (client.getActionAssistant().skillInterface == 3) {
                        client.getActionAssistant().defInterfaceDrag();
                    }
                    break;
                case 34158:
                    if (client.getActionAssistant().skillInterface == 3) {
                        client.getActionAssistant().defInterfaceBarr();
                    }
                    break;
                //att interface
                case 33206:
                    client.getActionAssistant().what = 0;
                    client.getActionAssistant().atkInterface();
                    break;
                case 33216:
                    client.getActionAssistant().thievingInterface();
                    break;
                case 9118:
                    client.getActionAssistant().removeAllWindows();
                    break;
                case 33185:
                case 33186:
                case 33187:
                    CraftingManager.startLeather(client, 1, 1741, 1129, 14, 25, 1, 885, (33187 - actionButtonId) * 5);
                    break;
                case 33190:
                case 33189:
                case 33188:
                    CraftingManager.startLeather(client, 1, 1741, 1059, 1, 13, 1, 885, (33190 - actionButtonId) * 5);
                    break;
                case 33193:
                case 33192:
                case 33191:
                    CraftingManager.startLeather(client, 1, 1741, 1061, 7, 17, 1, 885, (33193 - actionButtonId) * 5);
                    break;
                case 33196:
                case 33195:
                case 33194:
                    CraftingManager.startLeather(client, 1, 1741, 1063, 11, 22, 1, 885, (33196 - actionButtonId) * 5);
                    break;
                case 33197:
                case 33198:
                case 33199:
                    CraftingManager.startLeather(client, 1, 1741, 1095, 18, 27, 1, 885, (33199 - actionButtonId) * 5);
                    break;
                case 33200:
                case 33201:
                case 33202:
                    CraftingManager.startLeather(client, 1, 1741, 1169, 38, 37, 1, 885, (33202 - actionButtonId) * 5);
                    break;
                case 33203:
                case 33204:
                case 33205:
                    CraftingManager.startLeather(client, 1, 1741, 1167, 9, 17, 1, 885, (33205 - actionButtonId) * 5);
                    break;
                case 3162:
                    client.sounds = 0;
                    //client.getActionAssistant().sendMessage(":stop:");
                    break;
                case 3163:
                case 3164:
                case 3166:
                    client.sounds = 1;
                    //client.getActionAssistant().musicPlayer();
                    break;
                case 3173:
                    client.music = 0;
                    //	client.getActionAssistant().sendMessage(":stop:");
                    break;
                case 3174:
                case 3176:
                case 3177:
                    client.music = 1;
                    client.getActionAssistant().musicPlayer();
                    break;
//tanning
                case 57211:
                    client.getActionAssistant().tannerX(99, 1745, 1753, 200);//green X
                    break;

                case 57212:
                    client.getActionAssistant().tannerX(99, 2505, 1751, 300);//blue X
                    break;

                case 57215:
                    client.getActionAssistant().tannerX(99, 2507, 1749, 400);//red X
                    break;

                case 57216:
                    client.getActionAssistant().tannerX(99, 2509, 1747, 500);//black X
                    break;

                case 57219:
                    client.getActionAssistant().tannerX(5, 1745, 1753, 200);//green 5x
                    break;

                case 57227:
                    client.getActionAssistant().tannerX(1, 1745, 1753, 200);//green 1x
                    break;

                case 57220:
                    client.getActionAssistant().tannerX(5, 2505, 1751, 300);//blue 5x
                    break;

                case 57228:
                    client.getActionAssistant().tannerX(1, 2505, 1751, 300);//blue 1x
                    break;

                case 57223:
                    client.getActionAssistant().tannerX(5, 2507, 1749, 400);//red 5x
                    break;
                case 59097:
                    client.getActionAssistant().showInterface(15106);
                    break;
                case 57231:
                    client.getActionAssistant().tannerX(1, 2507, 1749, 400);//red 1x
                    break;

                case 57224:
                    client.getActionAssistant().tannerX(5, 2509, 1747, 500);//black 5x
                    break;

                case 57232:
                    client.getActionAssistant().tannerX(1, 2509, 1747, 500);//black 1x
                    break;

                case 57203:
                    hides = client.getActionAssistant().getItemAmount(1753);
                    client.getActionAssistant().tannerX(hides, 1745, 1753, 200);//green all
                    break;

                case 57204:
                    hides = client.getActionAssistant().getItemAmount(1751);
                    client.getActionAssistant().tannerX(hides, 2505, 1751, 200);//blue all
                    break;

                case 57207:
                    hides = client.getActionAssistant().getItemAmount(1749);
                    client.getActionAssistant().tannerX(hides, 2507, 1749, 200);//red all
                    break;

                case 57208:
                    hides = client.getActionAssistant().getItemAmount(1747);
                    client.getActionAssistant().tannerX(hides, 2509, 1747, 200);//black all
                    break;

                case 57217:
                    hides = client.getActionAssistant().getItemAmount(1739);
                    client.getActionAssistant().tannerX(5, 1741, 1739, 50);//leather 5x
                    break;
                case 57225:
                    hides = client.getActionAssistant().getItemAmount(1739);
                    client.getActionAssistant().tannerX(1, 1741, 1739, 50);//leather 1x
                    break;
                case 57201:
                    hides = client.getActionAssistant().getItemAmount(1739);
                    client.getActionAssistant().tannerX(hides, 1741, 1739, 50);//leather all
                    break;
                case 57209:
                    client.getActionAssistant().tannerX(1, 1741, 1739, 50);//leather x
                    break;
                case 57229: //hard leather 1
                    hides = client.getActionAssistant().getItemAmount(1739);
                    client.getActionAssistant().tannerX(1, 1743, 1739, 50);//hard leather 1
                    break;
                case 57221: //hard leather 5
                    hides = client.getActionAssistant().getItemAmount(1739);
                    client.getActionAssistant().tannerX(5, 1743, 1739, 50);//hard leather 5
                    break;
                case 57213: //hard leather x
                    hides = client.getActionAssistant().getItemAmount(1739);
                    client.getActionAssistant().tannerX(1, 1743, 1739, 50);//hard leather x
                    break;
                case 57205: //hard leather all
                    hides = client.getActionAssistant().getItemAmount(1739);
                    client.getActionAssistant().tannerX(hides, 1743, 1739, 50);//hard leather all			
                    break;
//tanning end		
//Autocasting Start
                case 7212:
                    client.getActionAssistant().setSidebarInterface(0, 328);
                    break;
                case 1097:
                case 1094:
                    if (client.prayerActive[25]) {
                        client.actionAssistant.sendFrame36(client.PRAYER_GLOW[25], 1);
                    } else {
                        client.actionAssistant.sendFrame36(client.PRAYER_GLOW[25], 0);
                    }
                    if (client.mage == 2) {
                        client.getActionAssistant().sendMessage("@red@Switch to ancients or normals.");
                        break;
                    }
                    if (client.mage == 1) {
                        client.getActionAssistant().setSidebarInterface(0, 1689);

                    } else {
                        // if(client.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 4675 || client.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 6914) {
                        client.getActionAssistant().setSidebarInterface(0, 1829);
                    }
                    break;

                case 7038://Wind Strike
                case 7039://Water Strike
                case 7040://Earth Strike
                case 7041://Fire Strike
                case 7042://Wind Bolt
                case 7043://Water Bolt
                case 7044://Earth Bolt
                case 7045://Fire Bolt
                case 7046://Wind Blast
                case 7047://Water Blast
                case 7048://Earth Blast
                case 7049://Fire Blast
                case 7050://Wain Wave
                case 7051://Water Wave
                case 7052://Earth Wave
                case 7053://Fire Wave
                case 51133:
                case 51185:
                case 51091:
                case 24018:
                case 51159:
                case 51211:
                case 51111:
                case 51069:
                case 51146:
                case 51198:
                case 51102:
                case 51058:
                case 51172:
                case 51224:
                case 51122:
                case 51080:
                    for (int index = 0; index < AutoCast.MageAB.length; index++) {
                        if (actionButtonId > 10000 && client.mage == 0) {
                            break;
                        }
                        if (actionButtonId < 10000 && client.mage == 1) {
                            break;
                        }
                        if (actionButtonId == AutoCast.MageAB[index]) {
                            if (client.duelRule[4]) {
                                client.getActionAssistant().sendMessage("Magic has been disabled in this duel!");
                                client.getCombat().resetAttack();
                                break;
                            }
                            client.AutoCast_SpellIndex = index;
                            client.getActionAssistant().sendFrame246(329, 200, client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON]);
                            client.getActionAssistant().sendMessage("@red@Autocasting Activated");
                            client.getActionAssistant().setSidebarInterface(0, 328);
                            client.autoCast = true;
                            client.usingMagic = true;
                        }
                    }
                    break;

// Autocasting End
// Player Options Start
                case 24017:
                    client.getCombatFormulas().sendWeapon();
                    break;
                case 3145:
                    if (!client.mouseButton) {
                        client.mouseButton = true;
                        client.getActionAssistant().sendFrame36(500, 1);
                        client.getActionAssistant().sendFrame36(170, 1);
                    } else if (client.mouseButton) {
                        client.mouseButton = false;
                        client.getActionAssistant().sendFrame36(500, 0);
                        client.getActionAssistant().sendFrame36(170, 0);
                    }
                    break;
                case 3189:
                    if (!client.sChat) {
                        client.sChat = true;
                        client.getActionAssistant().sendFrame36(502, 1);
                        client.getActionAssistant().sendFrame36(287, 1);
                    } else {
                        client.sChat = false;
                        client.getActionAssistant().sendFrame36(502, 0);
                        client.getActionAssistant().sendFrame36(287, 0);
                    }
                    break;
                case 3147:
                    if (!client.chatEffects) {
                        client.chatEffects = true;
                        client.getActionAssistant().sendFrame36(501, 1);
                        client.getActionAssistant().sendFrame36(171, 0);
                    } else {
                        client.chatEffects = false;
                        client.getActionAssistant().sendFrame36(501, 0);
                        client.getActionAssistant().sendFrame36(171, 1);
                    }
                    break;
                case 48176:
                    if (!client.acceptAid) {
                        client.acceptAid = true;
                        client.getActionAssistant().sendFrame36(503, 1);
                        client.getActionAssistant().sendFrame36(427, 1);
                    } else {
                        client.acceptAid = false;
                        client.getActionAssistant().sendFrame36(503, 0);
                        client.getActionAssistant().sendFrame36(427, 0);
                    }
                    break;
                case 152:
                    if (!client.isRunning2) {
                        client.isRunning2 = true;
                        client.getActionAssistant().sendFrame36(504, 1);
                        client.getActionAssistant().sendFrame36(173, 1);
                    } else {
                        client.isRunning2 = false;
                        client.getActionAssistant().sendFrame36(504, 0);
                        client.getActionAssistant().sendFrame36(173, 0);
                    }
                    break;
                case 74201://brightness1
                    client.getActionAssistant().sendFrame36(505, 1);
                    client.getActionAssistant().sendFrame36(506, 0);
                    client.getActionAssistant().sendFrame36(507, 0);
                    client.getActionAssistant().sendFrame36(508, 0);
                    client.getActionAssistant().sendFrame36(166, 1);
                    break;
                case 74203://brightness2
                    client.getActionAssistant().sendFrame36(505, 0);
                    client.getActionAssistant().sendFrame36(506, 1);
                    client.getActionAssistant().sendFrame36(507, 0);
                    client.getActionAssistant().sendFrame36(508, 0);
                    client.getActionAssistant().sendFrame36(166, 2);
                    break;

                case 74204://brightness3
                    client.getActionAssistant().sendFrame36(505, 0);
                    client.getActionAssistant().sendFrame36(506, 0);
                    client.getActionAssistant().sendFrame36(507, 1);
                    client.getActionAssistant().sendFrame36(508, 0);
                    client.getActionAssistant().sendFrame36(166, 3);
                    break;

                case 74205://brightness4
                    client.getActionAssistant().sendFrame36(505, 0);
                    client.getActionAssistant().sendFrame36(506, 0);
                    client.getActionAssistant().sendFrame36(507, 0);
                    client.getActionAssistant().sendFrame36(508, 1);
                    client.getActionAssistant().sendFrame36(166, 4);
                    break;
                case 74206://area1
                    client.getActionAssistant().sendFrame36(509, 1);
                    client.getActionAssistant().sendFrame36(510, 0);
                    client.getActionAssistant().sendFrame36(511, 0);
                    client.getActionAssistant().sendFrame36(512, 0);
                    break;
                case 74207://area2
                    client.getActionAssistant().sendFrame36(509, 0);
                    client.getActionAssistant().sendFrame36(510, 1);
                    client.getActionAssistant().sendFrame36(511, 0);
                    client.getActionAssistant().sendFrame36(512, 0);
                    break;
                case 74208://area3
                    client.getActionAssistant().sendFrame36(509, 0);
                    client.getActionAssistant().sendFrame36(510, 0);
                    client.getActionAssistant().sendFrame36(511, 1);
                    client.getActionAssistant().sendFrame36(512, 0);
                    break;
                case 74209://area4
                    client.getActionAssistant().sendFrame36(509, 0);
                    client.getActionAssistant().sendFrame36(510, 0);
                    client.getActionAssistant().sendFrame36(511, 0);
                    client.getActionAssistant().sendFrame36(512, 1);
                    break;


//Player options End
                case 59235:
                    client.getActionAssistant().removeAllWindows();
                    break;
                case 56066:
                    client.getActionAssistant().removeAllWindows();
                    client.chosen -= client.chosen;
                    break;

                case 56068:
                    client.option = 1;
                    client.chosen = 2500;
                    client.getActionAssistant().sendMessage("You choose the Void Knight Top.");
                    break;

                case 56073:
                    client.option = 2;
                    client.chosen = 2500;
                    client.getActionAssistant().sendMessage("You choose the Void Knight Bottoms.");
                    break;

                case 56078:
                    client.option = 3;
                    client.chosen = 500;
                    client.getActionAssistant().sendMessage("You choose the Void Knight Gloves.");
                    break;

                case 56083:
                    client.option = 4;
                    client.chosen = 1000;
                    client.getActionAssistant().sendMessage("You choose the Void Knight Mace.");
                    break;

                case 56088:
                    client.option = 5;
                    client.chosen = 1000;
                    client.getActionAssistant().sendMessage("You choose the Void Knight Melee helm.");
                    break;

                case 56093:
                    client.option = 6;
                    client.chosen = 1000;
                    client.getActionAssistant().sendMessage("You choose the Void Knight Range helm.");
                    break;

                case 56098:
                    client.option = 7;
                    client.chosen = 1000;
                    client.getActionAssistant().sendMessage("You choose the Void Knight Mage helm.");
                    break;

                //void knights
                case 56105:
                    if (client.option == 0 || client.chosen == 0) {
                        if (System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                            client.getActionAssistant().sendMessage("You have not chosen a reward!");
                            client.actionInterval = 6000;
                            client.lastAction = System.currentTimeMillis();
                        }
                    }
                    if (client.option == 1 && client.pcPoints > 249 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().addItem(2520, 1);
                        client.pcPoints -= 250;
                        client.getActionAssistant().removeAllWindows();
                        client.chosen -= client.chosen;
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 1 && client.pcPoints < 250 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().sendMessage("You do not have enough points!");
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 2 && client.pcPoints > 249 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().addItem(2522, 1);
                        client.pcPoints -= 250;
                        client.getActionAssistant().removeAllWindows();
                        client.chosen -= client.chosen;
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 2 && client.pcPoints < 250 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().sendMessage("You do not have enough points!");
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 3 && client.pcPoints > 149 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().addItem(2528, 1);
                        client.pcPoints -= 150;
                        client.getActionAssistant().removeAllWindows();
                        client.chosen -= client.chosen;
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 3 && client.pcPoints < 150 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().sendMessage("You do not have enough points!");
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 4 && client.pcPoints > 249 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        //client.getActionAssistant().addItem( , );
                        client.pcPoints -= 250;
                        client.getActionAssistant().removeAllWindows();
                        client.chosen -= client.chosen;
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 4 && client.pcPoints < 250 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().sendMessage("You do not have enough points!");
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 5 && client.pcPoints > 199 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().addItem(2526, 1);
                        client.pcPoints -= 200;
                        client.getActionAssistant().removeAllWindows();
                        client.chosen -= client.chosen;
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 5 && client.pcPoints < 200 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().sendMessage("You do not have enough points!");
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 6 && client.pcPoints > 199 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().addItem(2524, 1);
                        client.pcPoints -= 200;
                        client.getActionAssistant().removeAllWindows();
                        client.chosen -= client.chosen;
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 6 && client.pcPoints < 200 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().sendMessage("You do not have enough points!");
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 7 && client.pcPoints > 199 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().addItem(2518, 1);
                        client.pcPoints -= 200;
                        client.getActionAssistant().removeAllWindows();
                        client.chosen -= client.chosen;
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    if (client.option == 7 && client.pcPoints < 200 && System.currentTimeMillis() - client.lastAction > client.actionInterval) {
                        client.getActionAssistant().sendMessage("You do not have enough points!");
                        client.actionInterval = 6000;
                        client.lastAction = System.currentTimeMillis();
                    }
                    break;

                case 168:
                    client.getActionAssistant().startAnimation(0x357);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 169:
                    client.getActionAssistant().startAnimation(0x358);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 162:
                    client.getActionAssistant().startAnimation(0x359);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 164:
                    client.getActionAssistant().startAnimation(0x35A);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 165:
                    client.getActionAssistant().startAnimation(0x35B);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 161:
                    client.getActionAssistant().startAnimation(0x35C);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 170:
                    client.getActionAssistant().startAnimation(0x35D);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 171:
                    client.getActionAssistant().startAnimation(0x35E);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 163:
                    client.getActionAssistant().startAnimation(0x35F);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 167:
                    client.getActionAssistant().startAnimation(0x360);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 172:
                    client.getActionAssistant().startAnimation(0x361);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 166:
                    client.getActionAssistant().startAnimation(866);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52050:
                    client.getActionAssistant().startAnimation(0x839);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52051:
                    client.getActionAssistant().startAnimation(0x83A);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52052:
                    client.getActionAssistant().startAnimation(0x83B);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52053:
                    client.getActionAssistant().startAnimation(0x83C);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52054:
                    client.getActionAssistant().startAnimation(0x83D);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52055:
                    client.getActionAssistant().startAnimation(0x83E);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52056:
                    client.getActionAssistant().startAnimation(0x83F);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52057:
                    client.getActionAssistant().startAnimation(0x840);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52058:
                    client.getActionAssistant().startAnimation(0x841);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 43092:
                    //client.getActionAssistant().startAnimation(0x558);
                    //client.updateRequired = true;
                    //client.appearanceUpdateRequired = true;
                    break;

                case 2155:
                    client.getActionAssistant().startAnimation(0x46B);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 25103:
                    client.getActionAssistant().startAnimation(0x46A);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 25106:
                    client.getActionAssistant().startAnimation(0x469);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 2154:
                    client.getActionAssistant().startAnimation(0x468);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52071:
                    client.getActionAssistant().startAnimation(0x84F);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;

                case 52072:
                    client.getActionAssistant().startAnimation(0x850);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;
                case 59062:
                    client.getActionAssistant().startAnimation(2836);
                    break;

                case 72032:
                    // zombie walk emote
                    client.getActionAssistant().startAnimation(3544);
                    break;
                case 72033:
                    // zombie walk emote
                    client.getActionAssistant().startAnimation(3543);
                    break;
                case 72254:
                    //client.getActionAssistant().startAnimation(3866);
                    break;
                case 74077://skill cape
                    client.getActionAssistant().startAnimation(3866);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;
                /**Dueling**/

                case 26065: // no forfeit
                case 26040:
                    //client.duelSlot = -1;
                    //client.getActionAssistant().selectRule(0);
                    if (!client.inDuelConfirm) {
                        client.getActionAssistant().sendMessage("No forfeit option disabled.");
                    }
                    break;

                case 26066: // no movement
                case 26048:
                    if (!client.inDuelConfirm) {
                        client.duelSlot = -1;
                        client.getActionAssistant().selectRule(1);
                    }
                    break;

                case 26069: // no range
                case 26042:
                    if (!client.inDuelConfirm) {
                        client.duelSlot = -1;
                        client.getActionAssistant().selectRule(2);
                    }
                    break;

                case 26070: // no melee
                case 26043:
                    if (!client.inDuelConfirm) {
                        client.duelSlot = -1;
                        client.getActionAssistant().selectRule(3);
                    }
                    break;

                case 26071: // no mage
                case 26041:
                    if (!client.inDuelConfirm) {
                        client.duelSlot = -1;
                        client.getActionAssistant().selectRule(4);
                    }
                    break;

                case 26072: // no drinks
                case 26045:
                    if (!client.inDuelConfirm) {
                        client.duelSlot = -1;
                        client.getActionAssistant().selectRule(5);
                    }
                    break;

                case 26073: // no food
                case 26046:
                    if (!client.inDuelConfirm) {
                        client.duelSlot = -1;
                        client.getActionAssistant().selectRule(6);
                    }
                    break;

                case 26074: // no prayer
                case 26047:
                    if (!client.inDuelConfirm) {
                        client.duelSlot = -1;
                        client.getActionAssistant().selectRule(7);
                    }
                    break;

                case 26076: // obsticals
                case 26075:
                    if (!client.inDuelConfirm) {
                        client.duelSlot = -1;
                        client.getActionAssistant().selectRule(8);
                    }
                    break;

                case 2158: // fun weapons
                case 2157:
                    if (!client.inDuelConfirm) {
			/*client.duelSlot = -1;
			client.getTradeAndDuel().selectRule(9);*/
                        client.getActionAssistant().sendMessage("Fun weapons option disabled.");
                    }
                    break;

                case 30136: // sp attack
                case 30137:
                    if (!client.inDuelConfirm) {
                        client.duelSlot = -1;
                        client.getActionAssistant().selectRule(10);
                    }
                    break;

                case 53245: //no helm
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 0;
                        client.getActionAssistant().selectRule(11);
                    }
                    break;

                case 53246: // no cape
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 1;
                        client.getActionAssistant().selectRule(12);
                    }
                    break;

                case 53247: // no ammy
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 2;
                        client.getActionAssistant().selectRule(13);
                    }
                    break;

                case 53249: // no weapon.
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 3;
                        client.getActionAssistant().selectRule(14);
                    }
                    break;

                case 53250: // no body
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 4;
                        client.getActionAssistant().selectRule(15);
                    }
                    break;

                case 53251: // no shield
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 5;
                        client.getActionAssistant().selectRule(16);
                    }
                    break;

                case 53252: // no legs
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 7;
                        client.getActionAssistant().selectRule(17);
                    }
                    break;

                case 53255: // no gloves
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 9;
                        client.getActionAssistant().selectRule(18);
                    }
                    break;

                case 53254: // no boots
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 10;
                        client.getActionAssistant().selectRule(19);
                    }
                    break;

                case 53253: // no rings
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 12;
                        client.getActionAssistant().selectRule(20);
                    }
                    break;

                case 53248: // no arrows
                    if (!client.inDuelConfirm) {
                        client.duelSlot = 13;
                        client.getActionAssistant().selectRule(21);
                    }
                    break;


                case 26018:
                    if (!client.inDuelArena()) {
                        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
                        if (o != null) {
                            client.getActionAssistant().declineDuel();
                            o.getActionAssistant().declineDuel();

                        }
                        return;
                    }
                    if (client.duelStatus != 1) {

                        break;
                    }
                    client.duelScreenOne = false;
                    client.secondDuelScreen = true;
                    Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
                    if (o == null) {
                        client.getActionAssistant().declineDuel();
                        return;
                    }
                    if (o.playerId != client.duelOKID || !client.duelOK || !o.duelOK) {
                        return;
                    }
                    if (client.duelRule[2] && client.duelRule[3] && client.duelRule[4]) {
                        client.getActionAssistant().sendMessage("You won't be able to attack the other player with the rules you have set.");
                        break;
                    }
                    if (client.duelRule[1] && client.duelRule[8]) {
                        client.getActionAssistant().sendMessage("You must have movement ON to do obs.");
                        break;
                    }
                    client.duelStatus = 2;
                    if (client.duelStatus == 2) {
                        o.getActionAssistant().sendFrame126("Other player has accepted.", 6684);
                        client.getActionAssistant().sendFrame126("Waiting for other player...", 6684);
                        o.cantClick = 2;
                        client.cantClick = 2;
                    }
                    if (o.duelStatus == 2) {
                        client.getActionAssistant().sendFrame126("Other player has accepted.", 6684);
                        o.getActionAssistant().sendFrame126("Waiting for other player...", 6684);
                        o.cantClick = 2;
                        client.cantClick = 2;
                    }

                    if (client.duelStatus == 2 && o.duelStatus == 2) {
                        client.canOffer = false;
                        o.canOffer = false;
                        client.duelStatus = 3;
                        o.duelStatus = 3;
                        client.getActionAssistant().confirmDuel();
                        client.isFollowingTest = false;
                        o.isFollowingTest = false; // dueling fix
                        o.getActionAssistant().confirmDuel();
                    }
                    break;

                case 25120:
                    if (System.currentTimeMillis() - client.Screen2Delay < 3000) {
                        break;
                    }
                    if (client.duelStatus != 3) {
                        break;
                    }
                    client.Screen2Delay = System.currentTimeMillis();
                    if (client.ClickCount <= 2) {
                        client.getActionAssistant().lockMiniMap(true);
                        client.ClickCount += 6;
                        Client o1 = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
                        if (o1 == null) {
                            client.getActionAssistant().declineDuel();
                            return;
                        }
                        o1.getActionAssistant().lockMiniMap(true);
                        if (o1.playerId != client.duelOKID || !client.duelOK || !o1.duelOK) {
                            return;
                        }
                        if (client.duelStatus < 5) //unable to attack fix
                            client.duelStatus = 4;
                        if (o1.duelStatus == 4) {
                            client.inCombat = true;
                            o1.inCombat = true;
                            client.getActionAssistant().startDuel();
                            o1.getActionAssistant().startDuel();
                            client.duelStatus = 5;
                            o1.duelStatus = 5;
                            o1.duelCount = 4;
                            client.duelCount = 4;
                            client.duelDelay = System.currentTimeMillis();
                            o1.duelDelay = System.currentTimeMillis();
                        } else {
                            client.getActionAssistant().sendFrame126("Waiting for other player...", 6571);
                            o1.getActionAssistant().sendFrame126("Other player has accpeted.", 6571);
                        }
                    }
                    break;
                case 14067:
                    PlayerManager.getSingleton().saveGame(client);
                    client.getNRAchievements().checkMisc(client, 5);
                    break;
                /*** End Duel ***/
                case 28215:
			/*	client.actionAssistant.sendFrame126("Close Window", 3024);//Close Text
				client.actionAssistant.sendFrame126("@blu@Rune Republic Training Info", 3026);//Line 1
				client.actionAssistant.sendFrame126("", 3027);//Line 2
				client.actionAssistant.sendFrame126("@whi@To start change magic to ancients at the alter north of edge then teleport", 3028);//Line 3
				client.actionAssistant.sendFrame126("@whi@to Island Teleport or Slayer Teleport.", 3029);//Line 4
				client.actionAssistant.sendFrame126("@whi@Barrows is on the lunar and ancients tabs.", 3030);//Line 7
				client.actionAssistant.sendFrame126("@whi@Skilling is done in camelot.", 3031);//Line 8
				client.actionAssistant.sendFrame126("@whi@Tavery Dungeon - Teleport to falador then goto the northeast shop", 3032);//Line 9
				client.actionAssistant.sendFrame126("@whi@and go down the ladder.", 3033);//Line 10
				client.actionAssistant.sendFrame126("@whi@", 3034);//Line 11
				client.actionAssistant.sendFrame126("@whi@", 3035);//Line 11
				client.actionAssistant.sendFrame126("@gre@Our Forums: @whi@http://www.vaintyrsps.com", 3036);//Line 12
				client.actionAssistant.sendFrame126("@whi@Many guides for everything on our forums.", 3037);
				client.actionAssistant.showInterface(3023);	*/
                    break;

                case 28166:
                    client.actionAssistant.sendFrame126("Close Window", 3024);//Close Text
                    client.actionAssistant.sendFrame126("@blu@Rune Republic's Rules", 3026);//Line 1
                    client.actionAssistant.sendFrame126("", 3027);//Line 2
                    client.actionAssistant.sendFrame126("@whi@No Duping, Password Scaming and Hacking", 3028);//Line 3
                    client.actionAssistant.sendFrame126("@whi@No Harrassing Mods", 3029);//Line 4
                    client.actionAssistant.sendFrame126("@whi@No Bug Abuse", 3030);//Line 7
                    client.actionAssistant.sendFrame126("@whi@No Excessive Flaming", 3031);//Line 8
                    client.actionAssistant.sendFrame126("@whi@No Spamming", 3032);//Line 9
                    client.actionAssistant.sendFrame126("@whi@No PK Point Cheating", 3033);//Line 10
                    client.actionAssistant.sendFrame126("@whi@No Bugging Devs For Anything", 3034);//Line 11
                    client.actionAssistant.sendFrame126("@whi@No Luring Mods From Non-Wild To Wild", 3035);//Line 11
                    client.actionAssistant.sendFrame126("@gre@Our Forums: @whi@www.runerepublic.com", 3036);//Line 12
                    client.actionAssistant.sendFrame126("@whi@Visit Forums To See The Full Rule List", 3037);
       //             client.actionAssistant.sendFrame126("@red@Accounts are Deleted After 1 month", 3038);
                    client.actionAssistant.showInterface(3023);
                    break;
                case 150:
                    if (!client.autoRetaliate) {
                        client.autoRetaliate = true;
                    } else {
                        client.autoRetaliate = false;
                    }
                    break;

                case 151:
                    client.autoRetaliate = false;
                    break;
                case 10252:
                case 11000:
                case 10253:
                case 11001:
                case 10254:
                case 10255:
                case 11002:
                case 11011:
                case 11013:
                case 11014:
                case 11010:
                case 11012:
                case 11006:
                case 11009:
                case 11008:
                case 11004:
                case 11003:
                case 11005:
                case 47002:
                case 54090:
                case 11007:
                    if (client.randomed) {//&& (actionButtonId == client.statId[client.random_skill])) {
                        int stat = 0;
                        for (int i = 0; i < client.statId.length; i++) {
                            if (client.statId[i] == actionButtonId) {
                                stat = i;
                                break;
                            }
                        }
                        client.getActionAssistant().deleteItem(7498, client.getActionAssistant().getItemSlot(7498), 1);
                        client.getActionAssistant().addSkillXP(5000, stat);
                        client.getActionAssistant().refreshSkill(stat);
                        client.getActionAssistant().requestUpdates();
                        client.randomed = false;
                        client.getActionAssistant().removeAllWindows();
                    }
                    break;
                case 59100: // item kept on death 
                    if (!client.isSkulled) {
                        client.getActionAssistant().resetKeepItems();
                        client.getActionAssistant().keepItem(0, false);
                        client.getActionAssistant().keepItem(1, false);
                        client.getActionAssistant().keepItem(2, false);
                        client.getActionAssistant().keepItem(3, false);
                        client.getActionAssistant().sendMessage("You can keep three items and a fourth if you use the protect item prayer.");
                    } else {
                        client.getActionAssistant().resetKeepItems();
                        client.getActionAssistant().keepItem(0, false);
                        client.getActionAssistant().sendMessage("You are skulled and will only keep one item if you use the protect item prayer.");
                    }
                    client.getActionAssistant().sendItemsKept();
                    client.getActionAssistant().showInterface(6960);
                    client.getActionAssistant().resetKeepItems();
                    break;

                case 4169:
                    if (!client.getCombat().checkMagicReqs(48)) {
                        break;
                    }

                    if (System.currentTimeMillis() - client.godSpellDelay < Config.GOD_SPELL_CHARGE) {
                        client.getActionAssistant().sendMessage("You still feel the charge in your body!");
                        break;
                    }
                    client.godSpellDelay = System.currentTimeMillis();
                    client.getActionAssistant().sendMessage("You feel charged with a magical power!");
                    client.gfx100(301);
                    client.getActionAssistant().startAnimation(811);
                    break;

                case 103244:
                    client.getNRAchievements().showDescription(client, client.achievement, 0);
                    break;
                case 103245:
                    client.getNRAchievements().showDescription(client, client.achievement, 1);
                    break;
                case 103246:
                    client.getNRAchievements().showDescription(client, client.achievement, 2);
                    break;
                case 103247:
                    client.getNRAchievements().showDescription(client, client.achievement, 3);
                    break;
                case 103248:
                    client.getNRAchievements().showDescription(client, client.achievement, 4);
                    break;
                case 103249:
                    client.getNRAchievements().showDescription(client, client.achievement, 5);
                    break;
                case 103250:
                    client.getNRAchievements().showDescription(client, client.achievement, 6);
                    break;
                case 103251:
                    client.getNRAchievements().showDescription(client, client.achievement, 7);
                    break;
                case 103252:
                    client.getNRAchievements().showDescription(client, client.achievement, 8);
                    break;
                case 103253:
                    client.getNRAchievements().showDescription(client, client.achievement, 9);
                    break;
                case 103254:
                    client.getNRAchievements().showDescription(client, client.achievement, 10);
                    break;
                case 103255:
                    client.getNRAchievements().showDescription(client, client.achievement, 11);
                    break;
                case 104000:
                    client.getNRAchievements().showDescription(client, client.achievement, 12);
                    break;
                case 104001:
                    client.getNRAchievements().showDescription(client, client.achievement, 13);
                    break;
                case 104002:
                    client.getNRAchievements().showDescription(client, client.achievement, 14);
                    break;
                case 104003:
                    client.getNRAchievements().showDescription(client, client.achievement, 15);
                    break;
                case 104004:
                    client.getNRAchievements().showDescription(client, client.achievement, 16);
                    break;
                case 104005:
                    client.getNRAchievements().showDescription(client, client.achievement, 17);
                    break;
                case 104006:
                    client.getNRAchievements().showDescription(client, client.achievement, 18);
                    break;
                case 104007:
                    client.getNRAchievements().showDescription(client, client.achievement, 19);
                    break;
                case 104008:
                    client.getNRAchievements().showDescription(client, client.achievement, 20);
                    break;
                case 104009:
                    client.getNRAchievements().showDescription(client, client.achievement, 21);
                    break;


                case 78041:
                    client.getActionAssistant().removeAllWindows();
                    break;
                case 101143:
                    client.getActionAssistant().removeAllWindows();
                    break;

                case 58253:
                    client.getActionAssistant().showInterface(3213);

                    break;
                case 59004:
                    client.getActionAssistant().removeAllWindows();
                    break;
                /**Prayers**/
                //case 21233: // thick skin
                case 97168:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(0);
                    break;
                //case 21234: // burst of str
                case 97170:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(1);
                    break;
                //case 21235: // charity of thought
                case 97172:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(2);
                    break;
                //case 70080: // sharp eye
                case 97174:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(3);
                    break;
                case 70082:
                case 97176:
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(4);
                    break;
                case 21236: // rockskin
                case 97178:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(5);
                    break;
                case 21237: // super human
                case 97180:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(6);
                    break;
                case 21238:    // improved reflexes
                case 97182:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(7);
                    break;
                case 21239: // rapid restore
                case 97184:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(8);
                    break;
                case 21240: // rapid heal
                case 97186:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(9);
                    break;
                case 21241: // protect Item
                case 97188:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }

                    client.getCombatManager().activatePrayer(10);
                    break;
                case 70084: // hawk eye
                case 97190:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(11);
                    break;
                case 70086: // Mystic Lore
                case 97192:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(12);
                    break;
                case 21242: // steel skin
                case 97194:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(13);
                    break;
                case 21243: // ultimate str
                case 97196:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(14);
                    break;
                case 21244: // incredible reflex
                case 97198:
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(15);
                    break;

                case 21245: // protect from magic
                case 97200:
                    if (client.prayWait > 0) {
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(16);
                    break;
                case 21246: // protect from range
                case 97202:
                    if (client.prayWait > 0) {
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(17);
                    break;
                case 21247: // protect from melee
                case 97204:
                    if (client.prayWait > 0) {
                        break;
                    }
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(18);
                    break;
                case 70088: // Eagel Eye
                case 97206:
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(19);
                    break;
                case 70090: // Eagel Eye
                case 97208:
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(20);
                    break;
                case 2171: // retrui
                case 97210:
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(21);
                    break;
                case 2172: // redem
                case 97212:
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(22);
                    break;
                case 2173: // smite
                case 97214:
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(23);
                    break;
                case 70092: // chiv
                case 97216:

                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(24);
                    break;
                case 97218:
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    if (client.duelRule[7]) {
                        for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                            client.prayerActive[p] = false;
                            client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);
                        }
                        client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
                        break;
                    }
                    client.getCombatManager().activatePrayer(25);
                    break;
		/*	case 105120:// sap warrior
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(26);
			break;
				case 87235:// sap ranger
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(27);
			break;
			case 105122:// sap magic
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(28);
			break;
			case 105124:// sap spirit
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(29);
			break;
			case 105128:// berserker
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(30);
			break;
			case 105126:// Deflect summoning
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(31);
			break;
			case 105130:// Deflect magic
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(32);
			break;
			case 105132:// Deflect range
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(33);
			break;
			case 87249:// Deflect melee
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(34);
			break;
			case 87251:// Leech attack
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(35);
			break;
			case 87253:// Leech range
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(36);
			break;
			case 87255:// Leech magic
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(37);
			break;
			case 88001:// Leech Defence
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(38);
			break;
			case 88003:// Leech strength
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(39);
			break;
			case 88005:// Leech run energy
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(40);
			break;
			case 88007:// Leech Special
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(41);
			break;
			case 88009:// wrath
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(42);
			break;
			case 88011:// soulsplit
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(43);
			break;
			case 88013:// turmoil
				if(client.prayWait > 0){
					break;
				}
				if(client.playerLevel[3] <= 0){
				   client.actionAssistant.sendMessage("You can't change prayers while dying!");
					break;
				}
				if(client.duelRule[7]){
					for(int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
						client.prayerActive[p] = false;
						client.actionAssistant.sendFrame36(client.PRAYER_GLOW[p], 0);	
					}
					client.actionAssistant.sendMessage("Prayer has been disabled in this duel!");
					break;
				}
			client.getCombatManager().activatePrayer(44);
			break;*/
                case 87231: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(0);
                    break;
                case 105120: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(1);
                    break;
                case 87235: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(2);
                    break;
                case 105122: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(3);
                    break;
                case 105124: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(4);
                    break;
                case 105128: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(5);
                    break;
                case 105126: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(6);
                    break;
                case 105130:        //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(7);
                    break;
                case 105132: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(8);
                    break;
                case 87249:
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(9);
                    break;
                case 87251: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(10);
                    break;
                case 87253: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(11);
                    break;
                case 87255: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(12);
                    break;
                case 88001: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(13);
                    break;
                case 88003: //
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(14);
                    break;
                case 88005:
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(15);
                    break;
                case 88007: // protect from magic
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(16);
                    break;
                case 88009: // protect from range
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(17);
                    break;
                case 88011: // soul split
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }

                    client.getCombat().activateCurse(18);
                    break;
                case 88013: // turmoil
                    if (client.playerLevel[3] <= 0) {
                        client.actionAssistant.sendMessage("You can't change prayers while dying!");
                        break;
                    }
                    client.getCombat().activateCurse(19);
                    break;
                case 29063:
                case 29163:
                case 29113:
                case 33033:
                case 29138:
                case 48023:
                    if (client.newFag > 0 && client.inWild()) {
                        client.getActionAssistant().sendMessage("@red@You can not special under new player proection!");
                        return;
                    }
                    if (client.specialAmount > 10) { // speed glitch fix - patrick
                        //for(int t = 0; t <client.getSpecials().specialWeapons.length; t++){
                        if (client.duelStatus > 0 && ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7901)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4153)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 7822)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1249)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 1263)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5716)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13901)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 5730)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 13899)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 11716)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 19784)
                                || (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 22406))) {
                            client.actionAssistant.sendMessage("This weapon's special attack can not be used in duel arena!");
                            break;
                        }
                        double factor = 1;
                        if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] == 19669) {
                            factor = 0.9;
                        }
                        if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 15486 && (client.specialAmount >= ((int) (100) * factor))) {
                            client.getActionAssistant().startAnimation(12804);
                            client.gfx0(2319);
                            client.SOLspec = System.currentTimeMillis();
                            client.specOn = false;
                            break;
                        } else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 15486 && (client.specialAmount < ((int) (100) * factor))) {
                            client.actionAssistant.sendMessage("You do not have enough special power");
                            client.specOn = false;
                            break;
                        }
                        if (client.getSpecials().checkSpecial(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON])) {
                            if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4153 && client.specOn && (client.playerIndex != 0 || client.oldPlayerIndex != 0)) {
                                break;
                            }


                            client.specOn = !client.specOn;
                            client.getSpecials().specialBar();
                            if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4153 && client.getSpecials().checkSpecial(4153)) {
                                client.attackTimer = 0;
                                client.getCombat().attackPlayer(client.playerIndex);
                            }
                            break;
                        }
                        //}	
                    }
                    break;
		/*	case 48023:
			if(client.specialAmount > 10) { // speed glitch fix - patrick
				for(int t = 0; t <client.getSpecials().specialWeapons.length; t++){
					if(client.getSpecials().checkSpecial(client.playerEquipment[PlayerConstants.PLAYER_WEAPON])){
						client.specOn = !client.specOn;
						client.getSpecials().specialBar();
						if (client.playerEquipment[PlayerConstants.PLAYER_WEAPON] == 4153 && client.getSpecials().checkSpecial(4153)) {
							client.attackTimer = 0;
						}
						break;
					}
				}	
			}
			break;*/

                case 9125: //Accurate
                case 6221: // range accurate
                case 22230: //punch (unarmed)
                case 48010: //flick (whip)
                case 21200: //spike (pickaxe)
                case 1080: //bash (staff)
                case 6168: //chop (axe)
                case 6236: //accurate (long bow)
                case 17102: //accurate (darts)
                case 8234: //stab (dagger)
                    client.fightType = 0;
                    break;

                case 9126: //Defensive
                case 48008: //deflect (whip)
                case 22228: //block (unarmed)
                case 21201: //block (pickaxe)
                case 1078: //focus - block (staff)
                case 6169: //block (axe)
                case 33019: //fend (hally)
                case 18078: //block (spear)
                case 8235: //block (dagger)
                    client.fightType = 1;
                    break;

                case 9127: // Controlled
                case 48009: //lash (whip)
                case 33018: //jab (hally)
                case 6234: //longrange (long bow)
                case 6219: //longrange
                case 18077: //lunge (spear)
                case 18080: //swipe (spear)
                case 18079: //pound (spear)
                case 17100: //longrange (darts)
                    client.fightType = 3;
                    break;

                case 9128: //Aggressive
                case 6220: // range rapid
                case 22229: //kick (unarmed)
                case 21203: //impale (pickaxe)
                case 21202: //smash (pickaxe)
                case 1079: //pound (staff)
                case 6171: //hack (axe)
                case 6170: //smash (axe)
                case 33020: //swipe (hally)
                case 6235: //rapid (long bow)
                case 17101: //repid (darts)
                case 8237: //lunge (dagger)
                case 8236: //slash (dagger)
                    client.fightType = 2;
                    break;

                case 62136:
			/*if(client.inCombat && client.inWild()) {
				if(client.clanName != "none" && client.clanLeader != "nobody" && client.clanLeader != client.playerName);
					client.getActionAssistant().Send("You have left your clan.");
					client.getActionAssistant().updateCCMenu();
					client.getActionAssistant().removeFromCC();
			}*/
                    break;
		/*
		 * OTHER BUTTONS
		 */
                // Log out
                case 9154:
                    if (client.ClickCount <= 2) {
                        client.ClickCount += 4;
                        client.getActionAssistant().logout();
                    }
                    break;
                //case 21241:
                //	client.createItems = !client.createItems;
                //	break;
                case 82016:
                    if (!client.withDrawelNote) {
                        client.takeAsNote = true;
                        client.withDrawelNote = true;
                    } else {
                        client.takeAsNote = false;
                        client.withDrawelNote = false;
                    }
                    break;

                case 2461:
                case 9157:
                case 9167:
                case 32017:
                    if (client.getDialogueAssistant().isDialogueOpen()) {
                        DialogueMessage m = client.getDialogueAssistant()
                                .getCurrentDialogue();
                        if (m.getType() == DialogueMessage.Type.PLAYER_CHOOSE) {
                            client.getDialogueAssistant().doAction(m.getActions()[0]);
                        }
                    }
                    break;
                case 2462:
                case 9158:
                case 9168:
                case 32018:
                    if (client.getDialogueAssistant().isDialogueOpen()) {
                        DialogueMessage m = client.getDialogueAssistant()
                                .getCurrentDialogue();
                        if (m.getType() == DialogueMessage.Type.PLAYER_CHOOSE) {
                            client.getDialogueAssistant().doAction(m.getActions()[1]);
                        }
                    }
                    break;
                case 9169:
                case 32019:
                    if (client.getDialogueAssistant().isDialogueOpen()) {
                        DialogueMessage m = client.getDialogueAssistant()
                                .getCurrentDialogue();
                        if (m.getType() == DialogueMessage.Type.PLAYER_CHOOSE) {
                            client.getDialogueAssistant().doAction(m.getActions()[2]);
                        }
                    }
                    break;

                case 32020:
                    if (client.getDialogueAssistant().isDialogueOpen()) {
                        DialogueMessage m = client.getDialogueAssistant()
                                .getCurrentDialogue();
                        if (m.getType() == DialogueMessage.Type.PLAYER_CHOOSE) {
                            client.getDialogueAssistant().doAction(m.getActions()[3]);
                        }
                    }
                    break;
                case 78096:
                case 1164:

                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.VARROCK_X, Config.VARROCK_Y, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 1170:
                case 4146:
                case 77120:
                case 78115:
                case 117131:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.FALADOR_X, Config.FALADOR_Y, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 4143:
                case 51039:
                case 77112:
                case 78107:
                case 117123:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    if (client.mage == 1) {
                        client.getActionAssistant().startTeleport(Config.ARDOUGNE_X, Config.ARDOUGNE_Y, 0, "ancient");

                    } else {
                        client.getActionAssistant().startTeleport(3222, 3218, 0, "modern");

                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 9190:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
//                    if (!client.hasSetFaction) {
//                    	
//                    	client.faction = 2;
//                        client.hasSetFaction = true;
//                        client.getActionAssistant().clearQuestInterface();
//                        client.getActionAssistant().removeAllWindows();
//                        client.getActionAssistant().sendMessage("You are now a Faladorian....teleporting now..");
//                        PlayerManager.getSingleton().saveGame(client);
//                        break;
//                    }
                    if (client.necklace == 1) {
                        client.getActionAssistant().startTeleport(2660, 3657, 0, "modern"); //Slayer Tower
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 2) {
                        client.getActionAssistant().startTeleport(2398, 5177, 0, "modern"); //fightpits
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 3) {
                        client.getActionAssistant().startTeleport(3017, 10248, 0, "modern"); //Phoenix
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 4) {
                        client.getActionAssistant().deleteItem(15084, 1);
                        client.getActionAssistant().addItem(15100, 1);
                        client.getActionAssistant().sendMessage("@dre@You picked to host with Die (4 sides)!");
                        client.getActionAssistant().removeAllWindows();
                    }
                    if (Server.DICING) {
                        client.getActionAssistant().deleteItem(15084, 1);
                        client.getActionAssistant().addItem(15094, 1);
                        client.getActionAssistant().sendMessage("@dre@You picked to host with Die (12 sides)!");
                        client.getActionAssistant().removeAllWindows();
                    }
                    client.necklace = 0;
                    break;
                    
                case 9191:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
//                    if (client.starter == 0) {
//                        client.getActionAssistant().addItem(995, 500000);
//                        client.starter = 1;
//                        PlayerManager.getSingleton().saveGame(client);
//                        client.getActionAssistant().clearQuestInterface();
//                        break;
//                    }
                    if (client.necklace == 1) {
                        client.getActionAssistant().startTeleport(3104, 3249, 0, "modern");
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 2) {
                        client.getActionAssistant().startTeleport(3428, 3538, 0, "modern"); //Pest Control
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 4) {
                        client.getActionAssistant().deleteItem(15084, 1);
                        client.getActionAssistant().addItem(15086, 1);
                        client.getActionAssistant().sendMessage("@dre@You picked to host with Die (6 sides)!");
                        client.getActionAssistant().removeAllWindows();
                    }
                    if (Server.DICING) {
                        client.getActionAssistant().deleteItem(15084, 1);
                        client.getActionAssistant().addItem(15096, 1);
                        client.getActionAssistant().sendMessage("@dre@You picked to host with Die (20 sides)!");
                        client.getActionAssistant().removeAllWindows();
                    }
                    client.necklace = 0;
                    break;

                case 9192:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
//                    if (!client.hasSetFaction) {
//                        
//                        client.faction = 3;
//                        client.hasSetFaction = true;
//                        client.getActionAssistant().clearQuestInterface();
//                        PlayerManager.getSingleton().saveGame(client);
//                        break;
//                    }
                    if (client.necklace == 1) {
                        client.getActionAssistant().startTeleport(2905, 3177, 0, "modern");
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 2) {
                        client.getActionAssistant().startTeleport(3166, 9573, 0, "modern"); //Pest Control
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 4) {
                        client.getActionAssistant().deleteItem(15084, 1);
                        client.getActionAssistant().addItem(15090, 1);
                        client.getActionAssistant().sendMessage("@dre@You picked to host with Die (8 sides)!");
                        client.getActionAssistant().removeAllWindows();
                    }
                    if (Server.DICING) {
                        client.getActionAssistant().deleteItem(15084, 1);
                        client.getActionAssistant().addItem(15088, 1);
                        client.getActionAssistant().sendMessage("@dre@You picked to host with Dice (2, 6 sides)!");
                        client.getActionAssistant().removeAllWindows();
                    }
                    client.necklace = 0;
                    break;
                
                case 9193:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    if (client.necklace == 1) {
                        client.getActionAssistant().startTeleport(3293, 3181, 0, "modern"); //godwars
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 2) {
                        client.getActionAssistant().startTeleport(3574, 9928, 0, "modern"); //godwars
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 4) {
                        client.getActionAssistant().deleteItem(15084, 1);
                        client.getActionAssistant().addItem(15092, 1);
                        client.getActionAssistant().sendMessage("@dre@You picked to host with Die (10 sides)!");
                        client.getActionAssistant().removeAllWindows();
                    }
                    if (Server.DICING) {
                        client.getActionAssistant().deleteItem(15084, 1);
                        client.getActionAssistant().addItem(15098, 1);
                        client.getActionAssistant().sendMessage("@dre@You picked to host with Dice (up to 100)!");
                        client.getActionAssistant().removeAllWindows();
                    }
                    client.necklace = 0;
                    break;
                case 9194:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    
//                    if (!client.hasSetFaction) {
//                    	
//                    	client.faction = 1;
//                    	client.hasSetFaction = true;
//                        client.getActionAssistant().clearQuestInterface();
//                        PlayerManager.getSingleton().saveGame(client);
//                        break;
//                    }
                    if (client.necklace == 2) {
                        client.getActionAssistant().startTeleport(2807, 10002, 0, "modern"); //godwars
                        client.teleDelay = System.currentTimeMillis();
                    }
                    if (client.necklace == 4) {
                        client.necklace = 5;
                        client.getActionAssistant().clearQuestInterface();
                        client.getActionAssistant().sendFrame126("Die (12 sides)", 2494);
                        client.getActionAssistant().sendFrame126("Die (20 sides)", 2495);
                        client.getActionAssistant().sendFrame126("Dice (2, 6 sides)", 2496);
                        client.getActionAssistant().sendFrame126("Dice (up to 100)", 2497);
                        client.getActionAssistant().sendFrame126("...", 2498);
                        client.getActionAssistant().sendQuestSomething(8143);
                        client.getActionAssistant().sendFrame164(2492);
                        client.flushOutStream();
                        Server.DICING = true;
                    }
                    client.necklace = 0;
                    break;
                case 4140:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.VARROCK_X, Config.VARROCK_Y, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 1167:
                case 77096:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.VARROCK_X, Config.VARROCK_Y, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 77128://lunar isle
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.CAMELOT_X, Config.CAMELOT_Y, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 1174:
                case 4150:
                case 77116:
                case 78146:
                case 117162:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.CAMELOT_X, Config.CAMELOT_Y, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;

                case 1540:
                case 6004:
                case 77124:
                case 78178:
                case 117194:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.ARDOUGNE_X, Config.ARDOUGNE_Y, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 6005:
                case 78194:
                case 117210:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.WATCHTOWER_X, Config.WATCHTOWER_Y, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 1541:
                case 77092:
                    client.getActionAssistant().setSidebarInterface(6, 176);
                    break;
                case 4171:
                case 77076:
                case 78032:
                case 117048:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    if (client.faction == 1) {
                    	client.getActionAssistant().startTeleport(Config.VARROCK_X, Config.VARROCK_Y, 0, "modern");
                        client.teleDelay = System.currentTimeMillis();
                        break;
                    }
                    if (client.faction == 2) {
                    	client.getActionAssistant().startTeleport(Config.FALADOR_X, Config.FALADOR_Y, 0, "modern");
                        client.teleDelay = System.currentTimeMillis();
                        break;                    	
                    }
                    if (client.faction == 3) {
                    	client.getActionAssistant().startTeleport(Config.ARDOUGNE_X, Config.ARDOUGNE_Y, 0, "modern");
                        client.teleDelay = System.currentTimeMillis();
                        break;
                    }
                    
                    break;
                    
                case 50056:
                case 84237:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    if (client.faction == 1) {
                    	client.getActionAssistant().startTeleport(Config.VARROCK_X, Config.VARROCK_Y, 0, "ancient");
                        client.teleDelay = System.currentTimeMillis();
                        break;
                    }
                    if (client.faction == 2) {
                    	client.getActionAssistant().startTeleport(Config.FALADOR_X, Config.FALADOR_Y, 0, "ancient");
                        client.teleDelay = System.currentTimeMillis();
                        break;                    	
                    }
                    if (client.faction == 3) {
                    	client.getActionAssistant().startTeleport(Config.ARDOUGNE_X, Config.ARDOUGNE_Y, 0, "ancient");
                        client.teleDelay = System.currentTimeMillis();
                        break;
                    }
       
                    break;
                case 29031:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.TROLLHEIM_X, Config.TROLLHEIM_Y, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;

                case 50235:
                case 117112:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.VARROCK_X, Config.VARROCK_Y, 0, "ancient");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 79002:
                case 118018:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(2614, 3388, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 78170:
                case 117186:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3078, 3419, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 137448:
                case 117154:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(2550, 3758, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 50245:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3222, 3218, 0, "ancient");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 79010:
                case 50253:
                case 118026:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(Config.KHARYRLL_X, Config.KHARYRLL_Y, 0, "ancient");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 79050:
                case 118066:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3062, 3886, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 79034:
                case 118050:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(2892, 3454, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 79026:
                case 118042:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3239, 9866, 0, "modern");
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 51005:
                    client.getActionAssistant().startTeleport(Config.FALADOR_X, Config.FALADOR_Y, 0, "ancient");
			/*
			client.necklace = 1;
			//client.getActionAssistant().startTeleport(Config.LASSAR_X, Config.LASSAR_Y, 0, "ancient");
			//client.getActionAssistant().startTeleport(Config.LUMBY_X, Config.LUMBY_Y, 0, "modern");
			client.getActionAssistant().clearQuestInterface();
			client.getActionAssistant().sendFrame126("Slayer Tower", 2494);
			client.getActionAssistant().sendFrame126("Lumby Slayer Cave", 2495);
			client.getActionAssistant().sendFrame126("Releka Slayer Cave", 2496);
			client.getActionAssistant().sendFrame126("Al kharid", 2497);
			client.getActionAssistant().sendFrame126("Nowhere", 2498);
			//client.getActionAssistant().sendQuestSomething(8143);
			client.getActionAssistant().sendFrame164(2492);
			client.flushOutStream();*/
                    break;

                case 51013:
                    client.getActionAssistant().setSidebarInterface(6, 176); // magic tab (ancient = 12855)
                    break;
                case 117218:
                    client.getActionAssistant().setSidebarInterface(6, 176); // magic tab 
                    break;

                case 79042:
                case 51023:
                case 118058:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(2960, 3850, 0, "ancient");
                    client.teleDelay = System.currentTimeMillis();
                    break;


                case 117222:
                case 118098:
                    if (client.playerLevel[1] < 40) {
                        client.getActionAssistant().Send("You need to have 40 or higher defence to cast Vengeance!");
                        return;
                    }
                    if (client.playerLevel[6] < 94) {
                        client.getActionAssistant().Send("You need to have 94 or higher magic to cast Vengeance!");
                        return;
                    }
                    if (client.inDuelArena()) {
                        client.getActionAssistant().Send("Don't Cheat Asshole..Love...");
                        return;
                    }
                    if (System.currentTimeMillis() - client.VengeanceDelay < 30000 && !client.canVengeance) {
                        client.getActionAssistant().Send("You can only cast Vengeance spells every 30 seconds.");
                        return;
                    }
                    if (client.canVengeance) {
                        client.getActionAssistant().Send("You already have the vengeance spell casted.");
                        return;
                    }
                    if (!client.actionAssistant.playerHasItem(560, 2) || !client.actionAssistant.playerHasItem(557, 10) || !client.actionAssistant.playerHasItem(9075, 4)) {
                        client.getActionAssistant().Send("You 10 Earths 4 Astrals and 2 Deaths To Cast This.");
                        return;
                    }
                    client.actionAssistant.deleteItem(560, client.actionAssistant.getItemSlot(560), 2);
                    client.actionAssistant.deleteItem(557, client.actionAssistant.getItemSlot(557), 10);
                    client.actionAssistant.deleteItem(9075, client.actionAssistant.getItemSlot(9075), 4);
                    client.VengeanceDelay = System.currentTimeMillis();
                    client.canVengeance = true;
                    client.gfx100(726);
                    client.actionAssistant.startAnimation(4410);
                    //client.gfx100(642);
                    //client.actionAssistant.startAnimation(1914);				
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    break;
                case 69006: // edge dungeon
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3132, 9921, 0, "modern");
                    if (client.mage == 0) {
                        client.actionAssistant.setSidebarInterface(6, 1151); //modern
                    }
                    if (client.mage == 1) {
                        client.actionAssistant.setSidebarInterface(6, 12855); //modern
                    }
                    if (client.mage == 2) {
                        client.actionAssistant.setSidebarInterface(6, 29999); //modern
                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;

                case 69009:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    if (client.mage == 0) {
                        client.actionAssistant.setSidebarInterface(6, 1151); //modern
                    }
                    if (client.mage == 1) {
                        client.actionAssistant.setSidebarInterface(6, 12855); //modern
                    }
                    if (client.mage == 2) {
                        client.actionAssistant.setSidebarInterface(6, 29999); //modern
                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 68250: // varrock drags
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3359, 3718, 0, "modern");
                    if (client.mage == 0) {
                        client.actionAssistant.setSidebarInterface(6, 1151); //modern
                    }
                    if (client.mage == 1) {
                        client.actionAssistant.setSidebarInterface(6, 12855); //modern
                    }
                    if (client.mage == 2) {
                        client.actionAssistant.setSidebarInterface(6, 29999); //modern
                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 68247: // castle
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3000, 3625, 0, "modern");
                    if (client.mage == 0) {
                        client.actionAssistant.setSidebarInterface(6, 1151); //modern
                    }
                    if (client.mage == 1) {
                        client.actionAssistant.setSidebarInterface(6, 12855); //modern
                    }
                    if (client.mage == 2) {
                        client.actionAssistant.setSidebarInterface(6, 29999); //modern
                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 68244: // MB
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(2538, 4715, 0, "modern");
                    if (client.mage == 0) {
                        client.actionAssistant.setSidebarInterface(6, 1151); //modern
                    }
                    if (client.mage == 1) {
                        client.actionAssistant.setSidebarInterface(6, 12855); //modern
                    }
                    if (client.mage == 2) {
                        client.actionAssistant.setSidebarInterface(6, 29999); //modern
                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 69000: // Greens
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3152, 3661, 0, "modern");
                    if (client.mage == 0) {
                        client.actionAssistant.setSidebarInterface(6, 1151); //modern
                    }
                    if (client.mage == 1) {
                        client.actionAssistant.setSidebarInterface(6, 12855); //modern
                    }
                    if (client.mage == 2) {
                        client.actionAssistant.setSidebarInterface(6, 29999); //modern
                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 69003: // Varrock multi
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3245, 3500, 0, "modern");
                    if (client.mage == 0) {
                        client.actionAssistant.setSidebarInterface(6, 1151); //modern
                    }
                    if (client.mage == 1) {
                        client.actionAssistant.setSidebarInterface(6, 12855); //modern
                    }
                    if (client.mage == 2) {
                        client.actionAssistant.setSidebarInterface(6, 29999); //modern
                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;

                case 68253: // Gdz
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    client.getActionAssistant().startTeleport(3288, 3886, 0, "modern");
                    if (client.mage == 0) {
                        client.actionAssistant.setSidebarInterface(6, 1151); //modern
                    }
                    if (client.mage == 1) {
                        client.actionAssistant.setSidebarInterface(6, 12855); //modern
                    }
                    if (client.mage == 2) {
                        client.actionAssistant.setSidebarInterface(6, 29999); //modern
                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;
                case 72038:
                case 78202:
                    client.getActionAssistant().setSidebarInterface(6, 176); // magic tab (ancient = 12855)
                    break;
                case 51031:
                    if (System.currentTimeMillis() - client.teleDelay < 4000) {
                        break;
                    }
                    if (client.mage == 1) {
                        client.getActionAssistant().startTeleport(Config.CAMELOT_X, Config.CAMELOT_Y, 0, "ancient");
                    } else {
                        client.getActionAssistant().startTeleport(3249, 3512, 0, "modern");

                    }
                    client.teleDelay = System.currentTimeMillis();
                    break;

                case 34185: // any shortbow(u) fletch
                    if (client.dhide) {
                        client.getActionAssistant().CraftHide(1, 57, 1065, 1745, 1, 1734, 1, 62);//Green D-Hide Vambs
                        client.getActionAssistant().CraftHide(2, 66, 2487, 2505, 1, 1734, 1, 70);//Blue D-Hide Vambs
                        client.getActionAssistant().CraftHide(3, 73, 2489, 2507, 1, 1734, 1, 78);//Red D-Hide Vambs
                        client.getActionAssistant().CraftHide(4, 79, 2491, 2509, 1, 1734, 1, 86);//Black D-Hide Vambs
                        client.getActionAssistant().removeAllWindows();
                    } else {
                        client.getActionAssistant().removeAllWindows();
                        Fletching.startFletching(client, 1, "short");
                    }
                    break;
                case 34193: // any longbow(u) fletch
                    if (client.dhide) {
                        client.getActionAssistant().CraftHide(1, 63, 1135, 1745, 1, 1734, 3, 186);//Green D-Hide Body
                        client.getActionAssistant().CraftHide(2, 71, 2499, 2505, 1, 1734, 3, 210);//Blue D-Hide Body
                        client.getActionAssistant().CraftHide(3, 77, 2501, 2507, 1, 1734, 3, 234);//Red D-Hide Body
                        client.getActionAssistant().CraftHide(4, 84, 2503, 2509, 1, 1734, 3, 258);//Black D-Hide Body
                        client.getActionAssistant().removeAllWindows();
                    } else {
                        client.getActionAssistant().removeAllWindows();
                        Fletching.startFletching(client, 1, "long");
                    }
                    break;
                case 34184: // make any shortbow(u) x 5
                    if (client.dhide) {
                        client.getActionAssistant().CraftHideX(1, 5, 57, 1065, 1745, 1, 1734, 1, 62);
                        client.getActionAssistant().CraftHideX(2, 5, 66, 2487, 2505, 1, 1734, 1, 70);
                        client.getActionAssistant().CraftHideX(3, 5, 73, 2489, 2507, 1, 1734, 1, 78);
                        client.getActionAssistant().CraftHideX(4, 5, 79, 2491, 2509, 1, 1734, 1, 86);
                        client.getActionAssistant().removeAllWindows();
                    } else {
                        client.getActionAssistant().removeAllWindows();
                        Fletching.startFletching(client, 5, "short");
                    }
                    break;
                case 34192: // make any longbow(u) x 5
                    if (client.dhide) {
                        client.getActionAssistant().CraftHideX(1, 5, 63, 1135, 1745, 1, 1734, 3, 186);
                        client.getActionAssistant().CraftHideX(2, 5, 71, 2499, 2505, 1, 1734, 3, 210);
                        client.getActionAssistant().CraftHideX(3, 5, 77, 2501, 2507, 1, 1734, 3, 234);
                        client.getActionAssistant().CraftHideX(4, 5, 84, 2503, 2509, 1, 1734, 3, 258);
                        client.getActionAssistant().removeAllWindows();

                    } else {
                        client.getActionAssistant().removeAllWindows();
                        Fletching.startFletching(client, 5, "long");
                    }
                    break;
                case 34183: // make any shortbow(u) x 10
                    if (client.dhide) {
                        client.getActionAssistant().CraftHideX(1, 10, 57, 1065, 1745, 1, 1734, 1, 62);
                        client.getActionAssistant().CraftHideX(2, 10, 66, 2487, 2505, 1, 1734, 1, 70);
                        client.getActionAssistant().CraftHideX(3, 10, 73, 2489, 2507, 1, 1734, 1, 78);
                        client.getActionAssistant().CraftHideX(4, 10, 79, 2491, 2509, 1, 1734, 1, 86);
                        client.getActionAssistant().removeAllWindows();
                    } else {
                        client.getActionAssistant().removeAllWindows();
                        Fletching.startFletching(client, 10, "short");
                    }
                    break;
                case 34191: // make any longbow(u) x 10
                    if (client.dhide) {
                        client.getActionAssistant().CraftHideX(1, 10, 63, 1135, 1745, 1, 1734, 3, 186);
                        client.getActionAssistant().CraftHideX(2, 10, 71, 2499, 2505, 1, 1734, 3, 210);
                        client.getActionAssistant().CraftHideX(3, 10, 77, 2501, 2507, 1, 1734, 3, 234);
                        client.getActionAssistant().CraftHideX(4, 10, 84, 2503, 2509, 1, 1734, 3, 258);
                        client.getActionAssistant().removeAllWindows();
                    } else {
                        client.getActionAssistant().removeAllWindows();
                        Fletching.startFletching(client, 10, "long");
                    }
                    break;
                case 34189: // make 1x arrowshafts
                    if (client.dhide) {
                        client.getActionAssistant().CraftHide(1, 60, 1099, 1745, 1, 1734, 1, 124);//Green D-Hide Chaps
                        client.getActionAssistant().CraftHide(2, 68, 2493, 2505, 1, 1734, 1, 140);//Blue D-Hide Chaps
                        client.getActionAssistant().CraftHide(3, 75, 2495, 2507, 1, 1734, 1, 156);//Red D-Hide Chaps
                        client.getActionAssistant().CraftHide(4, 82, 2497, 2509, 1, 1734, 1, 172);//Black D-Hide Chaps
                        client.getActionAssistant().removeAllWindows();
                    } else {
                        client.getActionAssistant().removeAllWindows();
                        Fletching.startFletching(client, 1, "shaft");
                        Fletching.setLogId(client, 52);
                    }
                    break;
                case 34188: // make 5x arrowshafts
                    if (client.dhide) {
                        client.getActionAssistant().CraftHideX(1, 5, 60, 1099, 1745, 1, 1734, 2, 124);
                        client.getActionAssistant().CraftHideX(2, 5, 68, 2493, 2505, 1, 1734, 2, 140);
                        client.getActionAssistant().CraftHideX(3, 5, 75, 2495, 2507, 1, 1734, 2, 156);
                        client.getActionAssistant().CraftHideX(4, 5, 82, 2497, 2509, 1, 1734, 2, 172);
                        client.getActionAssistant().removeAllWindows();
                    } else {
                        client.getActionAssistant().removeAllWindows();
                        Fletching.startFletching(client, 5, "shaft");
                        Fletching.setLogId(client, 52);
                    }
                    break;
                case 34187: // make 10x arrowshafts
                    if (client.dhide) {
                        client.getActionAssistant().CraftHideX(1, 10, 60, 1099, 1745, 1, 1734, 2, 124);
                        client.getActionAssistant().CraftHideX(2, 10, 68, 2493, 2505, 1, 1734, 2, 140);
                        client.getActionAssistant().CraftHideX(3, 10, 75, 2495, 2507, 1, 1734, 2, 156);
                        client.getActionAssistant().CraftHideX(4, 10, 82, 2497, 2509, 1, 1734, 2, 172);
                        client.getActionAssistant().removeAllWindows();
                    } else {
                        client.getActionAssistant().removeAllWindows();
                        Fletching.startFletching(client, 10, "shaft");
                        Fletching.setLogId(client, 52);
                    }
                    break;
                case 34186:
                    if (client.dhide) {
                        client.getActionAssistant().CraftHideX(1, 30, 60, 1099, 1745, 1, 1734, 2, 124);
                        client.getActionAssistant().CraftHideX(2, 30, 68, 2493, 2505, 1, 1734, 2, 140);
                        client.getActionAssistant().CraftHideX(3, 30, 75, 2495, 2507, 1, 1734, 2, 156);
                        client.getActionAssistant().CraftHideX(4, 30, 82, 2497, 2509, 1, 1734, 2, 172);
                        client.getActionAssistant().removeAllWindows();
                    }
                    client.getActionAssistant().removeAllWindows();
                    Fletching.startFletching(client, 27, "shaft");
                    Fletching.setLogId(client, 52);
                    break;
                case 34190:
                    if (client.dhide) {
                        client.getActionAssistant().CraftHideX(1, 30, 60, 1135, 1745, 1, 1734, 2, 124);
                        client.getActionAssistant().CraftHideX(2, 30, 68, 2499, 2505, 1, 1734, 2, 140);
                        client.getActionAssistant().CraftHideX(3, 30, 75, 2501, 2507, 1, 1734, 2, 156);
                        client.getActionAssistant().CraftHideX(4, 30, 82, 2503, 2509, 1, 1734, 2, 172);
                        client.getActionAssistant().removeAllWindows();
                    }
                    client.getActionAssistant().removeAllWindows();
                    Fletching.startFletching(client, 27, "long");
                    break;
                case 34182:
                    if (client.dhide) {
                        client.getActionAssistant().CraftHideX(1, 30, 60, 1099, 1745, 1, 1734, 2, 124);
                        client.getActionAssistant().CraftHideX(2, 30, 68, 2493, 2505, 1, 1734, 2, 140);
                        client.getActionAssistant().CraftHideX(3, 30, 75, 2495, 2507, 1, 1734, 2, 156);
                        client.getActionAssistant().CraftHideX(4, 30, 82, 2497, 2509, 1, 1734, 2, 172);
                        client.getActionAssistant().removeAllWindows();
                    }
                    client.getActionAssistant().removeAllWindows();
                    Fletching.startFletching(client, 27, "short");
                    break;
                // fix by stricken716
		/*case 152:
			client.isRunning2 = false;
			client.isRunning = false;
			break;
		case 153:
			client.isRunning2 = true;
			client.isRunning = true;
			break;*/
                case 13092:
                    client.getTradeAssistant().acceptStage1();
                    client.logoutDelay = System.currentTimeMillis();
                    break;
                case 13218:
                    client.getTradeAssistant().acceptStage2();
                    client.logoutDelay = System.currentTimeMillis();
                    break;

                case 62137:
                    // client.getOutStream().createFrame(187);
                    // client.flushOutStream();
                    break;
//SMITHING
                case 29026://smith runite bar
                    while (bar == 2349)
                        bar = 2363;
                    orecount = 1;
                    break;
                case 15163://smith gold bar
                    while (bar == 2349)
                        bar = 2357;
                    orecount = 1;
                    break;
                case 15155://smith silver bar
                    while (bar == 2349)
                        bar = 2355;
                    orecount = 1;
                    break;
                case 29022://smith adamant bar
                    while (bar == 2349)
                        bar = 2361;
                    orecount = 1;
                    break;
                case 29017://smith mithril bar
                    while (bar == 2349)
                        bar = 2359;
                    orecount = 1;
                    break;
                case 15159://Smith steel bar
                    while (bar == 2349)
                        bar = 2353;
                    break;
                case 15151://Smith iron bar
                    while (bar == 2349)
                        bar = 2351;
                    orecount = 1;
                    break;
                case 15147://Smith one bronze bar
                    orecount = 1;
                    break;
                case 10247://smith 10 bronze bars
                    orecount = 10;
                    break;
                case 15149://smith 10 iron bars
                    while (bar == 2349)
                        bar = 2351;
                    orecount = 10;
                    break;
                case 15153://smith 10 silver bars
                    while (bar == 2349)
                        bar = 2355;
                    orecount = 10;
                    break;
                case 15157://smith 10 steel bars
                    while (bar == 2349)
                        bar = 2353;
                    orecount = 10;
                    break;
                case 15161://smith 10 gold bars
                    while (bar == 2349)
                        bar = 2357;
                    orecount = 10;
                    break;
                case 24253://smith 10 mithril bars
                    while (bar == 2349)
                        bar = 2359;
                    orecount = 10;
                    break;
                case 29019://smith 10 adamant bars
                    while (bar == 2349)
                        bar = 2361;
                    orecount = 10;
                    break;
                case 29024://smith 10 runite bars
                    while (bar == 2349)
                        bar = 2363;
                    orecount = 10;
                    break;
                case 29025://smith 5 runite bars
                    while (bar == 2349)
                        bar = 2363;
                    orecount = 5;
                    break;
                case 29020://smith 5 adamant bars
                    while (bar == 2349)
                        bar = 2361;
                    orecount = 5;
                    break;
                case 29016://smith 5 mithril bars
                    while (bar == 2349)
                        bar = 22359;
                    orecount = 5;
                    break;
                case 15158://Smith 5 steel bars
                    while (bar == 2349)
                        bar = 2353;
                    orecount = 5;
                    break;
                case 15150://Smith 5 iron bars
                    while (bar == 2349)
                        bar = 2351;
                    orecount = 5;
                    break;
                case 15162://smith 5 gold bars
                    while (bar == 2349)
                        bar = 2357;
                    orecount = 5;
                    break;
                case 15154://smith 5 silver bars
                    while (bar == 2349)
                        bar = 2355;
                    orecount = 5;
                    break;
                case 15146://Smith 5 bronze bars
                    orecount = 5;
                    break;
                case 9110://smith x bronze bars
                    smeltX = true;
                    break;
                case 15148://smith x iron bars
                    while (bar == 2349)
                        bar = 2351;
                    smeltX = true;
                    break;
                case 15152://smith x silver bars
                    while (bar == 2349)
                        bar = 2355;
                    smeltX = true;
                    break;
                case 15156://smith x steel bars
                    while (bar == 2349)
                        bar = 2353;
                    smeltX = true;
                    break;
                case 15160://smith x gold bars
                    while (bar == 2349)
                        bar = 2357;
                    smeltX = true;
                    break;
                case 16062://smith x mithril bars
                    while (bar == 2349)
                        bar = 22359;
                    smeltX = true;
                    break;
                case 29018://smith x adamant bars
                    while (bar == 2349)
                        bar = 2361;
                    smeltX = true;
                    break;
                case 29023://smith x runite bars
                    while (bar == 2349)
                        bar = 2363;
                    smeltX = true;
                    break;


//END SMITHING			

                default:
                    client.println_debug("Action Button: " + actionButtonId);
                    break;
            }
            if (orecount > 0) {
                client.getActionAssistant().multiSmelt(bar, orecount);
                client.smithamount = 4;
            }
            if (smeltX) {
                client.getOutStream().createFrame(27);
                client.flushOutStream();
                client.readySmelt = true;
                client.barType = bar;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //PlayerManager.getSingleton().saveGame(client);
    }

}
