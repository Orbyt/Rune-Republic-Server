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

import com.rs.worldserver.Config;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Packet;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;

/**
 * Bank x items packets
 *
 * @author Graham
 */
public class BankX implements Packet {

    public static final int PART1 = 135, PART2 = 208;
    public boolean buying = false;

    @Override
    public void handlePacket(Client client, int packetType, int packetSize) {
        if (packetType == PART1) {
            client.getOutStream().createFrame(27);
            client.setBankXremoveSlot(client.getInStream().readSignedWordBigEndian());
            client.setBankXinterfaceID(client.getInStream().readUnsignedWordA());
            client.setBankXremoveID(client.getInStream().readSignedWordBigEndian());

			/*client.println_debug(client.getBankXinterfaceID()
					+ "Bank X Items Part1: " + client.getBankXremoveID()
					+ " ja slot: " + client.getBankXremoveSlot());*/
        } else if (packetType == PART2) {
            int bankXamount = client.getInStream().readDWord();
            if (bankXamount <= 0) {
                return;
            }
            if (bankXamount > 2000000000) {
                return;
            }
            if (client.readySmelt) {
                client.getActionAssistant().multiSmelt(client.barType, bankXamount);
                client.barType = 0;
                client.readySmelt = false;
                return;
            }

            if (client.bloodlust) {
                String name = Integer.toString(bankXamount);
                System.out.println(name);
                client.bloodlust = false;
                return;
            }
            if (client.isJailing) {

                for (Player p : Server.getPlayerManager().getPlayers()) {
                    if (p == null)
                        continue;
                    if (!p.isActive || p.disconnected)
                        continue;
                    if (p.jailedBy.equalsIgnoreCase(" ") || p.jailedBy.equalsIgnoreCase("")) {
                        if (p.getPlayerName().equalsIgnoreCase(client.jailName)) {
                            if (p.getPlayerName().equalsIgnoreCase(client.playerName)) {
                                client.getActionAssistant().Send("@red@You can't jail yourself");
                                break;
                            }

                            Client d = (Client) p;
                            if (d.duelStatus > 4) {
                                client.getActionAssistant().Send("@red@This player is in a duel!");
                                return;
                            }
                            if (d.playerRights > 2) {
                                client.getActionAssistant().Send("@red@You can't jail mods.");
                                break;
                            }
                            d.tbed = false;
                            d.fishing = false;
                            d.walked = true;
                            d.getActionAssistant().startTeleport3(2095, 4428, 0, "modern");
                            d.blackMarks = 1;
                            if (bankXamount == 0) {
                                bankXamount = 1;
                            }
                            d.jailedBy = client.playerName;
                            d.jailTimer = bankXamount * 120;
                            PlayerManager.getSingleton().saveGame(d);
                            d.getActionAssistant().Send("@red@You have been jailed by " + client.playerName + " for " + bankXamount + " minutes.");
                            client.getActionAssistant().Send("@red@You have jailed " + d.playerName + ".");
                            client.jailName = "";
                            client.isJailing = false;
                        }
                    }
                }
                return;
            }
            if (client.tokenRedeem) {
                if (bankXamount > 800) {
                    bankXamount = 800;
                }
                int gold = client.getActionAssistant().getItemAmount(995);
                int tokens = client.getActionAssistant().getItemAmount(7775);
                if (tokens <= 0) {
                    return;
                }
                int hold = (int) Math.floor((2000000000 - gold) / 2500000);
                if (bankXamount > tokens) {
                    bankXamount = tokens;
                }
                if (bankXamount > hold) {
                    bankXamount = hold;
                }
                if (bankXamount <= 0) {
                    client.tokenRedeem = false;
                    return;
                }
                client.getActionAssistant().deleteItem(7775, bankXamount);
                client.getActionAssistant().addItem(995, bankXamount * 2500000);
                client.tokenRedeem = false;
                return;
            }
            if (client.isDicing) {
                if (client.newFag > 0) {
                    client.getActionAssistant().sendMessage("You can not dice as a new player!");
                    return;
                }
                if (bankXamount <= 0) {
                    client.isDicing = false;
                    return;
                }
                if (Config.WORLD_NUMBER == 5) {
                    client.isDicing = false;
                    return;
                }
                int gold = client.getActionAssistant().getItemAmount(995);
                if (gold <= 0) {
                    client.getActionAssistant().Send("@red@You have nothing to risk!");
                    client.isDicing = false;
                    return;
                }
                if (bankXamount > gold) {
                    bankXamount = gold;
                }
                if (client.getActionAssistant().freeSlots() < 3) {
                    client.getActionAssistant().Send("@red@You need atleast 3 inventory slots!");
                    client.isDicing = false;
                    return;
                }
				/*if (((gold/1000000) + (bankXamount)/1000000) > 2000) {
					client.getActionAssistant().Send("@red@You will not have room for all your winnings if you risk that much!");
					return;
				}*/
                int remainder2 = (int) Math.floor(bankXamount / 2500000);
                int goldLeft2 = bankXamount - (2500000 * remainder2);
                if ((gold + goldLeft2) > 2000000000) {
                    client.getActionAssistant().Send("@red@You will not have room for all your winnings if you risk that much!");
                    client.isDicing = false;
                    return;
                }
                client.getActionAssistant().deleteItem(995, bankXamount);
                int Dice = Misc.random(99) + 1;
                long epoch = System.currentTimeMillis() / 1000;
                if (Dice > 56) {
                    client.getActionAssistant().Send("@red@Congratulations! You've won your " + bankXamount + "x GP risk!");
                    if (bankXamount < 2500000) {
                        client.getActionAssistant().addItem(995, bankXamount * 2);
                    } else {
                        int remainder = (int) Math.floor(bankXamount / 2500000);
                        int goldLeft = bankXamount - (2500000 * remainder);
                        client.getActionAssistant().addItem(995, bankXamount + goldLeft);
                        client.getActionAssistant().addItem(7775, remainder);
                    }
                } else {
                    client.getActionAssistant().Send("@red@Sorry, you have lost");
                }
                client.forcedChat("I rolled a " + Dice);
                client.isDicing = false;
                return;
            }
            //System.out.println("sell x" + client.getBankXinterfaceID());
            if (client.getBankXinterfaceID() == 5064) {
                client.getContainerAssistant().bankItem(
                        client.playerItems[client.getBankXremoveSlot()],
                        client.getBankXremoveSlot(), bankXamount);
            } else if (client.getBankXinterfaceID() == 3823) {
			/*if (bankXamount > 90000) {
												client.getActionAssistant().sendMessage("You cannot sell that many at once!");
                                                return;
			}
			client.getContainerAssistant().sellItem(client.playerItems[client.getBankXremoveSlot()]-1, client.getBankXremoveSlot(), bankXamount);*/
                return;
            } else if (client.getBankXinterfaceID() == 5382) {
                client.getContainerAssistant().fromBank(client.bankItems[client.getBankXremoveSlot()], client.getBankXremoveSlot(), bankXamount);
            } else if (client.getBankXinterfaceID() == 3322) {
                if (client.duelStatus <= 0) {
                    client.getTradeAssistant().tradeItem(client.playerItems[client.getBankXremoveSlot()] - 1, client.getBankXremoveSlot(), bankXamount);
                } else {
                    if (!client.secondDuelScreen) {
                        client.getActionAssistant().stakeItem(client.playerItems[client.getBankXremoveSlot()] - 1, client.getBankXremoveSlot(), bankXamount);
                    }
                }

            } else if (client.getBankXinterfaceID() == 3415) {
                if (!client.secondTradeWindow) {
                    client.getTradeAssistant().fromTrade(client.getTradeAssistant().getOffer()[client.getBankXremoveSlot()] - 1, client.getBankXremoveSlot(), bankXamount);
                }
            } else if (client.getBankXinterfaceID() == 3822) {
                System.out.println("sell x" + client.getBankXinterfaceID());

            } else if (client.getBankXinterfaceID() == 3900) { //Shop
                if ((client.getBankXremoveID() == 7774 && bankXamount > 251) || (client.getBankXremoveID() == 7775 && bankXamount > 251) ||
                        (client.getBankXremoveID() == 7776 && bankXamount > 251) || (client.getBankXremoveID() == 555 && bankXamount > 5000) ||
                        (client.getBankXremoveID() == 384 && bankXamount > 1000) || (client.getBankXremoveID() == 386 && bankXamount > 1000) ||
                        (client.getBankXremoveID() == 7462 && bankXamount > 20) || (client.getBankXremoveID() == 7461 && bankXamount > 20) ||
                        (client.getBankXremoveID() == 7460 && bankXamount > 20) || (client.getBankXremoveID() == 7459 && bankXamount > 20) ||
                        (client.getBankXremoveID() >= 4708 && client.getBankXremoveID() <= 4751 && bankXamount > 10) ||
                        (client.getBankXremoveID() >= 6733 && client.getBankXremoveID() <= 6737 && bankXamount > 10) ||
                        (client.getBankXremoveID() == 11732 && bankXamount > 10) ||
                        (client.getBankXremoveID() == 6920 && bankXamount > 10) ||
                        (client.getBankXremoveID() == 6585 && bankXamount > 10) || (client.getBankXremoveID() == 7774 && bankXamount > 1)
                        ) {
                    client.getActionAssistant().sendMessage("You cannot buy that many at once!");
                    return;
                } else if (bankXamount < 30001) {
                    client.getContainerAssistant().buyItem(client.getBankXremoveID(), client.getBankXremoveSlot(), bankXamount);
                } else {
                    client.getActionAssistant().sendMessage("You cannot buy more than 30,000 at a time.");
                    return;
                }
            } else if (client.getBankXinterfaceID() == 6669) {
                if (!client.secondDuelScreen) {
                    client.getActionAssistant().fromDuel(client.getBankXremoveID(), client.getBankXremoveSlot(), bankXamount);
                }
            }
        }
    }

}
