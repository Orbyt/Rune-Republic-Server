package com.rs.worldserver.model.player;

import com.rs.worldserver.Config;
import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.*;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.util.DuelProcessor;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.world.PlayerManager;

import java.io.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Lots of packets in here.
 *
 * @author Graham
 */
public class ActionAssistant {

    private Client client;
    private Hashtable allItems = new Hashtable(60); // ERIC

    public ActionAssistant(Client client) {
        this.client = client;
    }

    public void playerWalk(int x, int y, String codeline) {
        if (client.duelStatus == 2 || client.duelStatus == 3 || client.duelStatus == 4) {
            return;
        }
        if (client.duelRule[1]) {
            return;
        }

        if ((client.duelStatus >= 1 && client.duelStatus <= 4) || client.duelStatus == 6) {
            return;
        }
        //if(client.playerName.equalsIgnoreCase("Orbit")){
        //client.getActionAssistant().sendMessage("Following to " + x + " - " + y + " " +codeline);
        //}
        if (client.freezeTimer <= 0)
            PathFinder.getPathFinder().findRoute(client, x, y, true, 1, 1);
    }

    public boolean checkSpellId(Client c, int spell) {
        if (spell <= 12445 && c.mage != 0) {
            return false;
        }
        if (spell > 12445 && c.mage != 1 && c.mage != 2) {
            return false;
        }
        return true;
    }

    public void followPlayer() {
        if (PlayerManager.players[client.followId] == null
                || PlayerManager.players[client.followId].isDead) {
            client.getFollowing().resetFollowing();
            return;
        }
        if (client.freezeTimer > 0) {
            client.stopMovement();
            return;
        }
        if (client.isDead || client.playerLevel[3] <= 0)
            return;
        int otherX = PlayerManager.players[client.followId].getX();
        int otherY = PlayerManager.players[client.followId].getY();

        boolean sameSpot = (client.absX == otherX && client.absY == otherY);

        boolean hallyDistance = client.goodDistance(otherX, otherY, client.getX(),
                client.getY(), 2);

        boolean rangeWeaponDistance = client.goodDistance(otherX, otherY, client.getX(),
                client.getY(), 4);
        boolean bowDistance = client.goodDistance(otherX, otherY, client.getX(),
                client.getY(), 10);
        boolean mageDistance = client.goodDistance(otherX, otherY, client.getX(),
                client.getY(), 10);

        boolean castingMagic = (client.usingMagic)
                && mageDistance;
        boolean playerRanging = (client.usingRangeWeapon)
                && rangeWeaponDistance;
        boolean playerBowOrCross = (client.usingBow) && bowDistance;

        boolean standardMelee = client.goodDistance(otherX, otherY, client.getX(),
                client.getY(), 1);

        if (!client.goodDistance(otherX, otherY, client.getX(), client.getY(), 25)) {
            client.followId = 0;
            client.getFollowing().resetFollowing();
            return;
        }
        client.faceUpdate(client.followId + 32768);
        if (!sameSpot) {
            if (client.inWild()) {
                if (playerRanging || playerBowOrCross) {
                    client.stopMovement();
                    return;
                }
                if (castingMagic || playerRanging || playerBowOrCross) {
                    client.stopMovement();
                    return;
                }
                if (client.getCombat().usingHally() && hallyDistance) {
                    client.stopMovement();
                    return;
                }
                if (standardMelee) {
                    if (client.getCombatFormulas().diagonal(client.getAbsX(), client.getAbsY(), otherX, otherY)) {
                        if (client.freezeTimer <= 0) {
                            client.getActionAssistant().playerWalk(otherX, otherY - 1, "Follow diag ActionAssist");

                            client.faceUpdate(client.followId + 32768);
                        } else {
                            client.faceUpdate(client.followId + 32768);
                        }
                    }
                    client.stopMovement();
                    return;
                }
            }
        }
        if (otherX == client.absX && otherY == client.absY) {

            playerWalk(client.absX, client.absY - 1, "Follow = ActionAssist");

        } else if (client.isRunning2) {
            playerWalk(otherX, otherY - 1, "Follow run ActionAssist");
        } else {
            playerWalk(otherX, otherY - 1, "Follow e;se ActionAssist");
        }
        client.faceUpdate(client.followId + 32768);
    }


    public void createObjectHints(int x, int y, int height, int pos) {
        client.getOutStream().createFrame(254);
        client.getOutStream().writeByte(pos);
        client.getOutStream().writeWord(x);
        client.getOutStream().writeWord(y);
        client.getOutStream().writeByte(height);
        client.flushOutStream();
    }

    public void updateKillCount(int boss) {
        switch (boss) {
            case 0:
                client.getActionAssistant().sendFrame126(Integer.toString(client.armaPoints), 18056);
                break;
            case 1:
                client.getActionAssistant().sendFrame126(Integer.toString(client.bandosPoints), 18057);
                break;
            case 2:
                client.getActionAssistant().sendFrame126(Integer.toString(client.zamPoints), 18059);
                break;
            case 3:
                client.getActionAssistant().sendFrame126(Integer.toString(client.saraPoints), 18058);
                break;
        }

    }

    public int Potions[][] = {{3025, 3027, 3029, 3031}, {2435, 140, 142, 144},
            {2441, 158, 160, 162}, {2437, 146, 148, 150}, {2443, 164, 166, 168}, {2451, 190, 192, 194}, {6686, 6688, 6690, 6692}, {2447, 176, 178, 180}, {2449, 182, 184, 186}, {2445, 170, 172, 174}};

    public double GetItemValue(int ItemID) {
        double ShopValue = 1;
        double Overstock = 0;
        double TotPrice = 0;

        //for (int i = 0; i < server.itemHandler.MaxListedItems; i++) {
        //	if (server.itemHandler.ItemList[i] != null) {
        //		if (server.itemHandler.ItemList[i].itemId == ItemID) {
        ShopValue = Server.getItemManager().getItemDefinition(ItemID).getShopValue();
        //		}
        //	}
        //}
        TotPrice = (ShopValue * 1); // Calculates price for 1 item, in
        return TotPrice;
    }

    public void ItemKeptInfo(int Lose) {
        for (int i = 18104; i < 18124; i++) {
            client.getActionAssistant().sendFrame126("", i);
        }
        client.getActionAssistant().sendFrame126("Items Kept on Death", 18103);
        client.getActionAssistant().sendFrame126("Information", 18106);
        client.getActionAssistant().sendFrame126("Max items kept On death:", 18107);
        client.getActionAssistant().sendFrame126("~ " + Lose + " ~", 18108);
        client.getActionAssistant().sendFrame126("The normal amount of", 18110);
        client.getActionAssistant().sendFrame126("items kept is three.", 18111);
        switch (Lose) {
            case 0:
                client.getActionAssistant().sendFrame126("Items you will keep on death:", 18104);
                client.getActionAssistant().sendFrame126("Items you will keep on death:", 18105);
                client.getActionAssistant().sendFrame126("You're marked with a", 18113);
                client.getActionAssistant().sendFrame126("@red@skull. @lre@This reduces the", 18114);
                client.getActionAssistant().sendFrame126("items you keep from", 18117);
                client.getActionAssistant().sendFrame126("three to zero!", 18118);
                break;
            case 1:
                client.getActionAssistant().sendFrame126("Items you will keep on death:", 18104);
                client.getActionAssistant().sendFrame126("Items you will keep on death:", 18105);
                client.getActionAssistant().sendFrame126("You're marked with a", 18113);
                client.getActionAssistant().sendFrame126("@red@skull. @lre@This reduces the", 18114);
                client.getActionAssistant().sendFrame126("items you keep from", 18117);
                client.getActionAssistant().sendFrame126("three to zero!", 18118);
                client.getActionAssistant().sendFrame126("However, you also have", 18120);
                client.getActionAssistant().sendFrame126("the @red@Protect @lre@Items prayer", 18121);
                client.getActionAssistant().sendFrame126("active, which saves you", 18122);
                client.getActionAssistant().sendFrame126("one extra item!", 18123);
                break;
            case 3:
                client.getActionAssistant().sendFrame126("Items you will keep on death(if not skulled):", 18104);
                client.getActionAssistant().sendFrame126("Items you will keep on death(if not skulled):", 18105);
                client.getActionAssistant().sendFrame126("You have no factors", 18113);
                client.getActionAssistant().sendFrame126("affecting the items you", 18114);
                client.getActionAssistant().sendFrame126("keep.", 18117);
                break;
            case 4:
                client.getActionAssistant().sendFrame126("Items you will keep on death(if not skulled):", 18104);
                client.getActionAssistant().sendFrame126("Items you will keep on death(if not skulled):", 18105);
                client.getActionAssistant().sendFrame126("You have the @red@Protect", 18113);
                client.getActionAssistant().sendFrame126("@red@Items @lre@prayer active,", 18114);
                client.getActionAssistant().sendFrame126("which saves you one", 18117);
                client.getActionAssistant().sendFrame126("extra item!", 18118);
                break;
        }
    }

    public void ResetKeepItems() {
        client.WillKeepItem1 = 0;
        client.WillKeepItem1Slot = 0;
        client.WillKeepItem2 = 0;
        client.WillKeepItem2Slot = 0;
        client.WillKeepItem3 = 0;
        client.WillKeepItem3Slot = 0;
        client.WillKeepItem4 = 0;
        client.WillKeepItem4Slot = 0;
        client.WillKeepAmt1 = 0;
        client.WillKeepAmt2 = 0;
        client.WillKeepAmt3 = 0;
    }

    public void FindItemKeptInfo() {
        if (client.isSkulled && (client.prayerActive[10] || client.curseActive[0]))
            ItemKeptInfo(1);
        else if (!client.isSkulled && !client.prayerActive[10] && !client.curseActive[0])
            ItemKeptInfo(3);
        else if (!client.isSkulled && (client.prayerActive[10] || client.curseActive[0]))
            ItemKeptInfo(4);
    }

    public void StartBestItemScan() {
        if (client.isSkulled && !client.prayerActive[10] && !client.curseActive[0]) {
            ItemKeptInfo(0);
            return;
        }
        FindItemKeptInfo();
        ResetKeepItems();
        BestItem1();
    }

    public void BestItem1() {
        int BestValue = 0;
        int NextValue = 0;
        int ItemsContained = 0;
        client.WillKeepItem1 = 0;
        client.WillKeepItem1Slot = 0;
        for (int ITEM = 0; ITEM < 28; ITEM++) {
            if (client.playerItems[ITEM] > 0) {
                ItemsContained += 1;

                NextValue = (int) Math.floor(GetItemValue(client.playerItems[ITEM] - 1));
                if (NextValue > BestValue) {
                    BestValue = NextValue;
                    client.WillKeepItem1 = client.playerItems[ITEM] - 1;
                    client.WillKeepItem1Slot = ITEM;
                    if (client.playerItemsN[ITEM] > 2 && !client.prayerActive[10] && !client.curseActive[0]) {
                        client.WillKeepAmt1 = 3;
                    } else if (client.playerItemsN[ITEM] > 3 && (client.prayerActive[10] || client.curseActive[0])) {
                        client.WillKeepAmt1 = 4;
                    } else {
                        client.WillKeepAmt1 = client.playerItemsN[ITEM];
                    }
                }
            }
        }
        for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
            if (client.playerEquipment[EQUIP] > 0) {
                ItemsContained += 1;
                NextValue = (int) Math.floor(GetItemValue(client.playerEquipment[EQUIP]));
                if (NextValue > BestValue) {
                    BestValue = NextValue;
                    client.WillKeepItem1 = client.playerEquipment[EQUIP];
                    client.WillKeepItem1Slot = EQUIP + 28;
                    if (client.playerEquipmentN[EQUIP] > 2 && !client.prayerActive[10] && !client.curseActive[0]) {
                        client.WillKeepAmt1 = 3;
                    } else if (client.playerEquipmentN[EQUIP] > 3 && (client.prayerActive[10] || client.curseActive[0])) {
                        client.WillKeepAmt1 = 4;
                    } else {
                        client.WillKeepAmt1 = client.playerEquipmentN[EQUIP];
                    }
                }
            }
        }
        if (!client.isSkulled && ItemsContained > 1 && (client.WillKeepAmt1 < 3 || ((client.prayerActive[10] || client.curseActive[0]) && client.WillKeepAmt1 < 4))) {
            BestItem2(ItemsContained);
        }
    }

    public void BestItem2(int ItemsContained) {
        int BestValue = 0;
        int NextValue = 0;
        client.WillKeepItem2 = 0;
        client.WillKeepItem2Slot = 0;
        for (int ITEM = 0; ITEM < 28; ITEM++) {
            if (client.playerItems[ITEM] > 0) {
                NextValue = (int) Math.floor(GetItemValue(client.playerItems[ITEM] - 1));
                if (NextValue > BestValue && !(ITEM == client.WillKeepItem1Slot && client.playerItems[ITEM] - 1 == client.WillKeepItem1)) {
                    BestValue = NextValue;
                    client.WillKeepItem2 = client.playerItems[ITEM] - 1;
                    client.WillKeepItem2Slot = ITEM;
                    if (client.playerItemsN[ITEM] > 2 - client.WillKeepAmt1 && !client.prayerActive[10] && !client.curseActive[0]) {
                        client.WillKeepAmt2 = 3 - client.WillKeepAmt1;
                    } else if (client.playerItemsN[ITEM] > 3 - client.WillKeepAmt1 && (client.prayerActive[10] || client.curseActive[0])) {
                        client.WillKeepAmt2 = 4 - client.WillKeepAmt1;
                    } else {
                        client.WillKeepAmt2 = client.playerItemsN[ITEM];
                    }
                }
            }
        }
        for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
            if (client.playerEquipment[EQUIP] > 0) {
                NextValue = (int) Math.floor(GetItemValue(client.playerEquipment[EQUIP]));
                if (NextValue > BestValue && !(EQUIP + 28 == client.WillKeepItem1Slot && client.playerEquipment[EQUIP] == client.WillKeepItem1)) {
                    BestValue = NextValue;
                    client.WillKeepItem2 = client.playerEquipment[EQUIP];
                    client.WillKeepItem2Slot = EQUIP + 28;
                    if (client.playerEquipmentN[EQUIP] > 2 - client.WillKeepAmt1 && !client.prayerActive[10] && !client.curseActive[0]) {
                        client.WillKeepAmt2 = 3 - client.WillKeepAmt1;
                    } else if (client.playerEquipmentN[EQUIP] > 3 - client.WillKeepAmt1 && (client.prayerActive[10] || client.curseActive[0])) {
                        client.WillKeepAmt2 = 4 - client.WillKeepAmt1;
                    } else {
                        client.WillKeepAmt2 = client.playerEquipmentN[EQUIP];
                    }
                }
            }
        }
        if (!client.isSkulled && ItemsContained > 2 && (client.WillKeepAmt1 + client.WillKeepAmt2 < 3 || ((client.prayerActive[10] || client.curseActive[0]) && client.WillKeepAmt1 + client.WillKeepAmt2 < 4))) {
            BestItem3(ItemsContained);
        }
    }

    public void BestItem3(int ItemsContained) {
        int BestValue = 0;
        int NextValue = 0;
        client.WillKeepItem3 = 0;
        client.WillKeepItem3Slot = 0;
        for (int ITEM = 0; ITEM < 28; ITEM++) {
            if (client.playerItems[ITEM] > 0) {
                NextValue = (int) Math.floor(GetItemValue(client.playerItems[ITEM] - 1));
                if (NextValue > BestValue && !(ITEM == client.WillKeepItem1Slot && client.playerItems[ITEM] - 1 == client.WillKeepItem1) && !(ITEM == client.WillKeepItem2Slot && client.playerItems[ITEM] - 1 == client.WillKeepItem2)) {
                    BestValue = NextValue;
                    client.WillKeepItem3 = client.playerItems[ITEM] - 1;
                    client.WillKeepItem3Slot = ITEM;
                    if (client.playerItemsN[ITEM] > 2 - (client.WillKeepAmt1 + client.WillKeepAmt2) && !client.prayerActive[10] && !client.curseActive[0]) {
                        client.WillKeepAmt3 = 3 - (client.WillKeepAmt1 + client.WillKeepAmt2);
                    } else if (client.playerItemsN[ITEM] > 3 - (client.WillKeepAmt1 + client.WillKeepAmt2) && (client.prayerActive[10] || client.curseActive[0])) {
                        client.WillKeepAmt3 = 4 - (client.WillKeepAmt1 + client.WillKeepAmt2);
                    } else {
                        client.WillKeepAmt3 = client.playerItemsN[ITEM];
                    }
                }
            }
        }
        for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
            if (client.playerEquipment[EQUIP] > 0) {
                NextValue = (int) Math.floor(GetItemValue(client.playerEquipment[EQUIP]));
                if (NextValue > BestValue && !(EQUIP + 28 == client.WillKeepItem1Slot && client.playerEquipment[EQUIP] == client.WillKeepItem1) && !(EQUIP + 28 == client.WillKeepItem2Slot && client.playerEquipment[EQUIP] == client.WillKeepItem2)) {
                    BestValue = NextValue;
                    client.WillKeepItem3 = client.playerEquipment[EQUIP];
                    client.WillKeepItem3Slot = EQUIP + 28;
                    if (client.playerEquipmentN[EQUIP] > 2 - (client.WillKeepAmt1 + client.WillKeepAmt2) && !client.prayerActive[10] && !client.curseActive[0]) {
                        client.WillKeepAmt3 = 3 - (client.WillKeepAmt1 + client.WillKeepAmt2);
                    } else if (client.playerEquipmentN[EQUIP] > 3 - client.WillKeepAmt1 && (client.prayerActive[10] || client.curseActive[0])) {
                        client.WillKeepAmt3 = 4 - (client.WillKeepAmt1 + client.WillKeepAmt2);
                    } else {
                        client.WillKeepAmt3 = client.playerEquipmentN[EQUIP];
                    }
                }
            }
        }
        if (!client.isSkulled && ItemsContained > 3 && (client.prayerActive[10] || client.curseActive[0]) && ((client.WillKeepAmt1 + client.WillKeepAmt2 + client.WillKeepAmt3) < 4)) {
            BestItem4();
        }
    }

    public void BestItem4() {
        int BestValue = 0;
        int NextValue = 0;
        client.WillKeepItem4 = 0;
        client.WillKeepItem4Slot = 0;
        for (int ITEM = 0; ITEM < 28; ITEM++) {
            if (client.playerItems[ITEM] > 0) {
                NextValue = (int) Math.floor(GetItemValue(client.playerItems[ITEM] - 1));
                if (NextValue > BestValue && !(ITEM == client.WillKeepItem1Slot && client.playerItems[ITEM] - 1 == client.WillKeepItem1) && !(ITEM == client.WillKeepItem2Slot && client.playerItems[ITEM] - 1 == client.WillKeepItem2) && !(ITEM == client.WillKeepItem3Slot && client.playerItems[ITEM] - 1 == client.WillKeepItem3)) {
                    BestValue = NextValue;
                    client.WillKeepItem4 = client.playerItems[ITEM] - 1;
                    client.WillKeepItem4Slot = ITEM;
                }
            }
        }
        for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
            if (client.playerEquipment[EQUIP] > 0) {
                NextValue = (int) Math.floor(GetItemValue(client.playerEquipment[EQUIP]));
                if (NextValue > BestValue && !(EQUIP + 28 == client.WillKeepItem1Slot && client.playerEquipment[EQUIP] == client.WillKeepItem1) && !(EQUIP + 28 == client.WillKeepItem2Slot && client.playerEquipment[EQUIP] == client.WillKeepItem2) && !(EQUIP + 28 == client.WillKeepItem3Slot && client.playerEquipment[EQUIP] == client.WillKeepItem3)) {
                    BestValue = NextValue;
                    client.WillKeepItem4 = client.playerEquipment[EQUIP];
                    client.WillKeepItem4Slot = EQUIP + 28;
                }
            }
        }
    }

    public int mysteryBoxAmounts(int itemid) {
        switch (itemid) {
            case 537:
                return 15;
            case 2451:
                return 15;
            case 6686:
                return 15;
            case 2431:
                return 15;
            case 6915:
                return 2;
            case 6890:
                return 2;
            case 4717:
                return 1;
            case 4719:
                return 1;
            case 4721:
                return 1;
            case 4723:
                return 1;
            case 15273:
                return 20;
            case 11733:
                return 1;
            case 6921:
                return 1;
            case 4725:
                return 1;
            case 4727:
                return 1;
            case 4729:
                return 1;
            case 4731:
                return 1;
            case 4733:
                return 1;
            case 4735:
                return 1;
            case 4737:
                return 1;
            case 4739:
                return 1;
            case 4754:
                return 1;
            case 4756:
                return 1;
            case 4758:
                return 1;
            case 4760:
                return 1;
            case 4709:
                return 1;
            case 4711:
                return 1;
            case 4713:
                return 1;
            case 4715:
                return 1;
            case 4746:
                return 1;
            case 4748:
                return 1;
            case 4750:
                return 1;
            case 4752:
                return 1;
            case 6586:
                return 1;
            default:
                return 1;
        }
    }

    public void sendKillStreak(Client client) {


        PlayerManager.getSingleton().sendGlobalMessage("@bla@[@red@Kill Streak@bla@]: @red@" + client.playerName + " @bla@: on world: "
                + Config.WORLD_NUMBER + " with " + client.killStreak + " kills. Combat: " + client.getCombatLevel());
        String prefix = "@bla@";
        String command = "[@red@Kill Streak@bla@]: @red@" + client.playerName + " @bla@: on world: "
                + Config.WORLD_NUMBER + " with " + client.killStreak + " kills. Combat: " + client.getCombatLevel();
        try {
            //				Connection con = DriverManager.getConnection("jdbc:mysql://localhost/donationsystem","root","NUIDSfnu7387ca");
            String query = "INSERT INTO servermessage(playername,message,prefix,world) values ('" + " " + "','" + command.replaceAll("'", "~") + "','" + prefix + "',";
            String tempquery = "";
            for (int i = 1; i <= Config.MAX_WORLDS; i++) {
                if (i != Config.WORLD_NUMBER) {
                    tempquery = query + Integer.toString(i) + ");";
                    //Statement statement = con.createStatement();
                    //	statement.close();
                }
            }
            //	con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int getMaxSpec() {
        int specBonus = 0;
        if (client.killStreak == 12) {
            specBonus += 30;
        }
        return specBonus;
    }

    public int getMaxHP() {
        int hpBonus = 0;
        switch (client.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT]) {
            case 20135:
            case 20147:
            case 20159:
                hpBonus += 7;
                break;
        }
        switch (client.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST]) {
            case 20139:
            case 20151:
            case 20163:
                hpBonus += 20;
                break;
        }
        switch (client.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS]) {
            case 20143:
            case 20155:
            case 20167:
                hpBonus += 13;
                break;
        }
        if (client.killStreak == 6 && Config.killStreak) {
            hpBonus += 5;
        }
        if (client.killStreak == 8 && Config.killStreak) {
            hpBonus += 10;
        }
        return hpBonus;
    }

    public int getBonusForItem(int itemID) {
        int hpBonus = 0;
        switch (itemID) {
            case 20135:
            case 20147:
            case 20159:
                return 7;
            case 20139:
            case 20151:
            case 20163:
                return 20;
            case 20143:
            case 20155:
            case 20167:
                return 13;

        }
        return 0;
    }

    public boolean checkHpItem(int itemid) {
        switch (itemid) {
            case 20135:
            case 20147:
            case 20159:
            case 20139:
            case 20151:
            case 20163:
            case 20143:
            case 20155:
            case 20167:
                return true;
        }
        return false;
    }


    public void obelisk(int X, int Y, int ID) {
        for (Player p : Server.getPlayerManager().getPlayers()) {
            if (p != null) {
                Client person = (Client) p;
                if ((person.playerName != null || person.playerName != "null")) {
                    if (person.distanceToPoint(X, Y) < 40) {
                        //person.makeGlobalObject(ID, X, Y + 4, 0, 10);
                        //person.makeGlobalObject(ID, X + 4, Y, 0, 10);
                        //person.makeGlobalObject(ID, X + 4, Y + 4, 0, 10);
                        //person.makeGlobalObject(ID, X, Y, 0, 10);
                    }
                }
            }
        }
    }

    public void startObelisk(int silgar) {
        if (silgar == 50) {
            client.ReplaceObjectOblisk(3305, 3914, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3305, 3918, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3309, 3918, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3309, 3914, 0, 14825, -1, 10);
        } else if (silgar == 35) {
            client.ReplaceObjectOblisk(3104, 3796, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3104, 3792, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3108, 3796, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3108, 3792, 0, 14825, -1, 10);
        } else if (silgar == 13) {
            client.ReplaceObjectOblisk(3154, 3618, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3154, 3622, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3158, 3618, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3158, 3622, 0, 14825, -1, 10);
        } else if (silgar == 19) {
            client.ReplaceObjectOblisk(3225, 3669, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3229, 3665, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3225, 3665, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3229, 3669, 0, 14825, -1, 10);
        } else if (silgar == 44) {
            client.ReplaceObjectOblisk(2978, 3864, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(2982, 3864, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(2978, 3868, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(2982, 3868, 0, 14825, -1, 10);
        } else if (silgar == 27) {
            client.ReplaceObjectOblisk(3033, 3734, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3037, 3734, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3033, 3730, 0, 14825, -1, 10);
            client.ReplaceObjectOblisk(3037, 3730, 0, 14825, -1, 10);
        }
    }

    public void object(int objectId, int objectX, int objectY, int face, int objectType) {
        client.getOutStream().createFrame(85);
        client.getOutStream().writeByteC(objectY - (client.getMapRegionY() * 8));
        client.getOutStream().writeByteC(objectX - (client.getMapRegionX() * 8));
        client.getOutStream().createFrame(101);
        client.getOutStream().writeByteC((objectType << 2) + (face & 3));
        client.getOutStream().writeByte(0);

        if (objectId != -1) { // removing
            client.getOutStream().createFrame(151);
            client.getOutStream().writeByteS(0);
            client.getOutStream().writeWordBigEndian(objectId);
            client.getOutStream().writeByteS((objectType << 2) + (face & 3));
        }
        client.flushOutStream();
    }

    public void object2(int objectId, int objectX, int objectY, int face, int objectType) {
        for (Player p : Server.getPlayerManager().getPlayers()) {
            if (p != null) {
                Client c = (Client) p;
                c.ReplaceObject2(objectX, objectY, 6951, -1, 0);
            }
        }
    }

    public void resetObelisk(int silgar) {
        if (silgar == 50) {
            client.ReplaceObjectOblisk(3305, 3914, 0, 14831, -1, 10);
            client.ReplaceObjectOblisk(3305, 3918, 0, 14831, -1, 10);
            client.ReplaceObjectOblisk(3309, 3918, 0, 14831, -1, 10);
            client.ReplaceObjectOblisk(3309, 3914, 0, 14831, -1, 10);
        } else if (silgar == 35) {
            client.ReplaceObjectOblisk(3104, 3796, 0, 14828, -1, 10);
            client.ReplaceObjectOblisk(3104, 3792, 0, 14828, -1, 10);
            client.ReplaceObjectOblisk(3108, 3796, 0, 14828, -1, 10);
            client.ReplaceObjectOblisk(3108, 3792, 0, 14828, -1, 10);
        } else if (silgar == 13) {
            client.ReplaceObjectOblisk(3154, 3618, 0, 14829, -1, 10);
            client.ReplaceObjectOblisk(3154, 3622, 0, 14829, -1, 10);
            client.ReplaceObjectOblisk(3158, 3618, 0, 14829, -1, 10);
            client.ReplaceObjectOblisk(3158, 3622, 0, 14829, -1, 10);
        } else if (silgar == 19) {
            client.ReplaceObjectOblisk(3225, 3669, 0, 14830, -1, 10);
            client.ReplaceObjectOblisk(3229, 3665, 0, 14830, -1, 10);
            client.ReplaceObjectOblisk(3225, 3665, 0, 14830, -1, 10);
            client.ReplaceObjectOblisk(3229, 3669, 0, 14830, -1, 10);
        } else if (silgar == 44) {
            client.ReplaceObjectOblisk(2978, 3864, 0, 14826, -1, 10);
            client.ReplaceObjectOblisk(2982, 3864, 0, 14826, -1, 10);
            client.ReplaceObjectOblisk(2978, 3868, 0, 14826, -1, 10);
            client.ReplaceObjectOblisk(2982, 3868, 0, 14826, -1, 10);
        } else if (silgar == 27) {
            client.ReplaceObjectOblisk(3033, 3734, 0, 14827, -1, 10);
            client.ReplaceObjectOblisk(3037, 3734, 0, 14827, -1, 10);
            client.ReplaceObjectOblisk(3033, 3730, 0, 14827, -1, 10);
            client.ReplaceObjectOblisk(3037, 3730, 0, 14827, -1, 10);
        }
    }	
        /*public int smithing[] = { 0, 0, 0, 1, -1, 0 };

		public void SetSmithing(int WriteFrame) {
			client.getOutStream().createFrameVarSizeWord(53);
			client.getOutStream().writeWord(WriteFrame);
			client.getOutStream().writeWord(Item.SmithingItems.length);
			for (int i = 0; i < Item.SmithingItems.length; i++) {
				Item.SmithingItems[i][0] += 1;
				if (Item.SmithingItems[i][1] > 254) {
					client.getOutStream().writeByte(255); // item's stack count. if over 254,
					// write byte 255
					client.getOutStream().writeDWord_v2(Item.SmithingItems[i][1]); // and then
					// the real
					// value
					// with
					// writeDWord_v2
				} else {
					client.getOutStream().writeByte(Item.SmithingItems[i][1]);
				}
				if ((Item.SmithingItems[i][0] > 20000)
						|| (Item.SmithingItems[i][0] < 0)) {
					client.playerItems[i] = 20000;
				}
				client.getOutStream().writeWordBigEndianA(Item.SmithingItems[i][0]); // item
				// id
			}
			client.getOutStream().endFrameVarSizeWord();
		}	
		
		public boolean smithing() {
			if (playerHasItem(2347,1)) {
				int bars = 0;
				int Length = 22;
				int barid = 0;
				int Level = 0;
				int ItemN = 1;

				if (smithing[2] >= 4) {
					barid = (2349 + ((smithing[2] + 1) * 2));
				} else {
					barid = (2349 + ((smithing[2] - 1) * 2));
				}
				if ((smithing[2] == 1) || (smithing[2] == 2)) {
					Length += 1;
				} else if (smithing[2] == 3) {
					Length += 2;
				}
				for (int i = 0; i < Length; i++) {
					if (Item.smithing_frame[(smithing[2] - 1)][i][0] == smithing[4]) {
						bars = Item.smithing_frame[(smithing[2] - 1)][i][3];
						if (smithing[1] == 0) {
							smithing[1] = Item.smithing_frame[(smithing[2] - 1)][i][2];
						}
						ItemN = Item.smithing_frame[(smithing[2] - 1)][i][1];
					}
				}
				if (playerLevel[playerSmithing] >= smithing[1]) {
					if (AreXItemsInBag(barid, bars) == true) {
						if (freeSlots() > 0) {
							if ((smithing[0] == 1)) {
								actionAmount++;

								sendMessage("You start hammering the bar...");
								
								setAnimation(0x382);
								smithing[0] = 2;
							}
							if ((smithing[0] == 2)) {
								for (int i = 0; i < bars; i++) {
									deleteItem(barid, GetItemSlot(barid),
											playerItemsN[GetItemSlot(barid)]);
								}
								addSkillXP(
										((int) (150.5 * bars * smithing[2] * smithing[3])),
										playerSmithing);
								addItem(smithing[4], ItemN);
								sendMessage("You smith a "
										+ getItemName(smithing[4]) + ".");
								resetAnimation();
								if (smithing[5] <= 1) {
									
								} else {
						
									smithing[5] -= 1;
									smithing[0] = 1;
								}
							}
						} else {
							sendMessage("Not enough space in your inventory.");
							
							return false;
						}
					} else {
						sendMessage("You need " + bars + " " + getItemName(barid)
								+ " to smith a " + getItemName(smithing[4]));
						resetAnimation();
					}
				} else {
					sendMessage("You need " + smithing[1] + " "
							+ statName[playerSmithing] + " to smith a "
							+ getItemName(smithing[4]));
					return false;
				}
			} else {
				sendMessage("You need a " + getItemName(2347) + " to hammer bars.");
				return false;
			}
			return true;
		}	
		public void OpenSmithingFrame(int Type) {
			int Type2 = Type - 1;
			int Length = 22;

			if ((Type == 1) || (Type == 2)) {
				Length += 1;
			} else if (Type == 3) {
				Length += 2;
			}
			// Sending amount of bars + make text green if lvl is highenough
			sendFrame126("", 1132); // Wire
			sendFrame126("", 1096);
			sendFrame126("", 11459); // Lantern
			sendFrame126("", 11461);
			sendFrame126("", 1135); // Studs
			sendFrame126("", 1134);
			String bar, color, color2, name = "";

			if (Type == 1) {
				name = "Bronze ";
			} else if (Type == 2) {
				name = "Iron ";
			} else if (Type == 3) {
				name = "Steel ";
			} else if (Type == 4) {
				name = "Mithril ";
			} else if (Type == 5) {
				name = "Adamant ";
			} else if (Type == 6) {
				name = "Rune ";
			}
			for (int i = 0; i < Length; i++) {
				bar = "bar";
				color = "@red@";
				color2 = "@yel@";
				if (Item.smithing_frame[Type2][i][3] > 1) {
					bar = bar + "s";
				}
				if (playerLevel[playerSmithing] >= Item.smithing_frame[Type2][i][2]) {
					color2 = "@whi@";
				}
				int Type3 = Type2;

				if (Type2 >= 3) {
					Type3 = (Type2 + 2);
				}
				if (AreXItemsInBag((2349 + (Type3 * 2)),
						Item.smithing_frame[Type2][i][3]) == true) {
					color = "@gre@";
				}
				sendFrame126(color + "" + Item.smithing_frame[Type2][i][3] + ""
						+ bar, Item.smithing_frame[Type2][i][4]);
				String linux_hack = getItemName(Item.smithing_frame[Type2][i][0]);
				int index = getItemName(Item.smithing_frame[Type2][i][0]).indexOf(
						name);
				if (index > 0) {
					linux_hack = linux_hack.substring(index + 1);
					sendFrame126(linux_hack, Item.smithing_frame[Type2][i][5]);
				}
				// sendFrame126(
				// color2 + ""
				// + getItemName(Item.smithing_frame[Type2][i][0]).replace(name,
				// ""),
				// Item.smithing_frame[Type2][i][5]);
			}
			Item.SmithingItems[0][0] = Item.smithing_frame[Type2][0][0]; // Dagger
			Item.SmithingItems[0][1] = Item.smithing_frame[Type2][0][1];
			Item.SmithingItems[1][0] = Item.smithing_frame[Type2][4][0]; // Sword
			Item.SmithingItems[1][1] = Item.smithing_frame[Type2][4][1];
			Item.SmithingItems[2][0] = Item.smithing_frame[Type2][8][0]; // Scimitar
			Item.SmithingItems[2][1] = Item.smithing_frame[Type2][8][1];
			Item.SmithingItems[3][0] = Item.smithing_frame[Type2][9][0]; // Long
			// Sword
			Item.SmithingItems[3][1] = Item.smithing_frame[Type2][9][1];
			Item.SmithingItems[4][0] = Item.smithing_frame[Type2][18][0]; // 2
			// hand
			// sword
			Item.SmithingItems[4][1] = Item.smithing_frame[Type2][18][1];
			SetSmithing(1119);
			Item.SmithingItems[0][0] = Item.smithing_frame[Type2][1][0]; // Axe
			Item.SmithingItems[0][1] = Item.smithing_frame[Type2][1][1];
			Item.SmithingItems[1][0] = Item.smithing_frame[Type2][2][0]; // Mace
			Item.SmithingItems[1][1] = Item.smithing_frame[Type2][2][1];
			Item.SmithingItems[2][0] = Item.smithing_frame[Type2][13][0]; // Warhammer
			Item.SmithingItems[2][1] = Item.smithing_frame[Type2][13][1];
			Item.SmithingItems[3][0] = Item.smithing_frame[Type2][14][0]; // Battle
			// axe
			Item.SmithingItems[3][1] = Item.smithing_frame[Type2][14][1];
			Item.SmithingItems[4][0] = Item.smithing_frame[Type2][17][0]; // Claws
			Item.SmithingItems[4][1] = Item.smithing_frame[Type2][17][1];
			SetSmithing(1120);
			Item.SmithingItems[0][0] = Item.smithing_frame[Type2][15][0]; // Chain
			// body
			Item.SmithingItems[0][1] = Item.smithing_frame[Type2][15][1];
			Item.SmithingItems[1][0] = Item.smithing_frame[Type2][20][0]; // Plate
			// legs
			Item.SmithingItems[1][1] = Item.smithing_frame[Type2][20][1];
			Item.SmithingItems[2][0] = Item.smithing_frame[Type2][19][0]; // Plate
			// skirt
			Item.SmithingItems[2][1] = Item.smithing_frame[Type2][19][1];
			Item.SmithingItems[3][0] = Item.smithing_frame[Type2][21][0]; // Plate
			// body
			Item.SmithingItems[3][1] = Item.smithing_frame[Type2][21][1];
			Item.SmithingItems[4][0] = -1; // Lantern
			Item.SmithingItems[4][1] = 0;
			if ((Type == 2) || (Type == 3)) {
				color2 = "@red@";
				if (playerLevel[playerSmithing] >= Item.smithing_frame[Type2][22][2]) {
					color2 = "@whi@";
				}
				Item.SmithingItems[4][0] = Item.smithing_frame[Type2][22][0]; // Lantern
				Item.SmithingItems[4][1] = Item.smithing_frame[Type2][22][1];
				sendFrame126(color2 + ""
						+ getItemName(Item.smithing_frame[Type2][22][0]), 11461);
			}
			SetSmithing(1121);
			Item.SmithingItems[0][0] = Item.smithing_frame[Type2][3][0]; // Medium
			Item.SmithingItems[0][1] = Item.smithing_frame[Type2][3][1];
			Item.SmithingItems[1][0] = Item.smithing_frame[Type2][10][0]; // Full
			// Helm
			Item.SmithingItems[1][1] = Item.smithing_frame[Type2][10][1];
			Item.SmithingItems[2][0] = Item.smithing_frame[Type2][12][0]; // Square
			Item.SmithingItems[2][1] = Item.smithing_frame[Type2][12][1];
			Item.SmithingItems[3][0] = Item.smithing_frame[Type2][16][0]; // Kite
			Item.SmithingItems[3][1] = Item.smithing_frame[Type2][16][1];
			Item.SmithingItems[4][0] = Item.smithing_frame[Type2][6][0]; // Nails
			Item.SmithingItems[4][1] = Item.smithing_frame[Type2][6][1];
			SetSmithing(1122);
			Item.SmithingItems[0][0] = Item.smithing_frame[Type2][5][0]; // Dart
			// Tips
			Item.SmithingItems[0][1] = Item.smithing_frame[Type2][5][1];
			Item.SmithingItems[1][0] = Item.smithing_frame[Type2][7][0]; // Arrow
			// Heads
			Item.SmithingItems[1][1] = Item.smithing_frame[Type2][7][1];
			Item.SmithingItems[2][0] = Item.smithing_frame[Type2][11][0]; // Knives
			Item.SmithingItems[2][1] = Item.smithing_frame[Type2][11][1];
			Item.SmithingItems[3][0] = -1; // Wire
			Item.SmithingItems[3][1] = 0;
			if (Type == 1) {
				color2 = "@bla@";
				if (playerLevel[playerSmithing] >= Item.smithing_frame[Type2][22][2]) {
					color2 = "@whi@";
				}
				Item.SmithingItems[3][0] = Item.smithing_frame[Type2][22][0]; // Wire
				Item.SmithingItems[3][1] = Item.smithing_frame[Type2][22][1];
				sendFrame126(color2 + ""
						+ getItemName(Item.smithing_frame[Type2][22][0]), 1096);
			}
			Item.SmithingItems[4][0] = -1; // Studs
			Item.SmithingItems[4][1] = 0;
			if (Type == 3) {
				color2 = "@bla@";
				if (playerLevel[playerSmithing] >= Item.smithing_frame[Type2][23][2]) {
					color2 = "@whi@";
				}
				Item.SmithingItems[4][0] = Item.smithing_frame[Type2][23][0]; // Studs
				Item.SmithingItems[4][1] = Item.smithing_frame[Type2][23][1];
				sendFrame126(color2 + ""
						+ getItemName(Item.smithing_frame[Type2][23][0]), 1134);
			}
			SetSmithing(1123);
			showInterface(994);
			smithing[2] = Type;
		}
		
		public int CheckSmithing(int ItemID, int ItemSlot) {
			boolean GoFalse = false;
			int Type = -1;

			if (!playerHasItem(2347,1)) {
				showDialogue("You need a hammer to smith with.");
				return -1;
			}
			switch (ItemID) {
			case 2349:
				// Bronze Bar
				Type = 1;
				break;

			case 2351:
				// Iron Bar
				Type = 2;
				break;

			case 2353:
				// Steel Bar
				Type = 3;
				break;

			case 2359:
				// Mithril Bar
				Type = 4;
				break;

			case 2361:
				// Adamantite Bar
				Type = 5;
				break;

			case 2363:
				// Runite Bar
				Type = 6;
				break;

			default:
				sendMessage("You cannot smith this item.");
				GoFalse = true;
				break;
			}
			if (GoFalse == true) {
				return -1;
			}
			return Type;
		}
	//endsmithing	
		*/

    public int followDistance = 0;

    public void setFollowing(int followID, int followType, int followDistance) {
        client.getOutStream().createFrame(175);
        client.getOutStream().writeWord(followID);
        client.getOutStream().writeByte(followType);
        client.getOutStream().writeWord(followDistance);
    }

    public void createArrow(int type, int id) {
        client.getOutStream().createFrame(254); //The packet ID
        client.getOutStream().writeByte(type); //1=NPC, 10=Player
        client.getOutStream().writeWord(id); //NPC/Player ID
        client.getOutStream().write3Byte(0); //Junk
    }

    public void createArrow(int x, int y, int height, int pos) {
        client.getOutStream().createFrame(254); //The packet ID
        client.getOutStream().writeByte(pos); //Position on Square(2 = middle, 3 = west, 4 = east, 5 = south, 6 = north)
        client.getOutStream().writeWord(x); //X-Coord of Object
        client.getOutStream().writeWord(y); //Y-Coord of Object
        client.getOutStream().writeByte(height); //Height off Ground
    }


    public int[] r2nds = {221, 223, 225, 231, 221, 223, 225, 231, 235, 221, 223, 225, 231, 239, 241, 245, 247, 6693};
    public int[] rFish = {317, 321, 327, 331, 317, 321, 327, 331, 335, 341, 345, 349, 353, 359, 317, 321, 327, 331, 363};
    public int[] herb = {249, 251, 253, 255, 249, 251, 253, 255, 257, 259, 261, 263, 265, 267, 249, 251, 253, 255, 269};
    public int[] Task1 = {1653, 1677, 122, 1265};
    public int[] Task2 = {1653, 1677, 122, 1265};
    public int[] Task3 = {1653, 1677, 122, 1265, 1832};
    public int[] Task4 = {1653, 1677, 122, 1265, 1832, 1601};
    public int[] Task5 = {1653, 1677, 122, 1265, 1832, 1601, 1612};
    public int[] Task6 = {1653, 1677, 122, 1265, 1832, 1601, 1612, 1831};
    public int[] Task7 = {1653, 1677, 122, 1265, 1832, 1601, 1612, 1831, 1622, 112, 117};
    public int[] Task8 = {1653, 1677, 122, 1265, 1832, 1601, 1612, 1831, 1622, 1620, 112, 117};
    public int[] Task9 = {1653, 1677, 122, 1265, 1832, 1601, 1612, 1831, 1622, 1620, 112, 117, 1633};
    public int[] Task10 = {1653, 1677, 122, 1265, 1832, 1601, 1612, 1831, 1622, 1620, 112, 117, 1633, 1616};
    public int[] Task11 = {1653, 1677, 122, 1265, 1832, 1601, 1612, 1831, 1622, 1620, 112, 117, 1633, 1616};
    public int[] Task12 = {1653, 1677, 122, 1265, 1832, 1601, 1612, 1831, 1622, 1620, 112, 117, 1633, 1616, 1643, 1619};
    public int[] Task13 = {1653, 1677, 122, 1265, 1832, 1601, 1612, 1831, 1622, 1620, 112, 117, 1633, 1616, 1643, 1619, 1637, 941, 55};
    public int[] Task14 = {1653, 1677, 122, 1265, 1832, 1601, 1612, 1831, 1622, 1620, 112, 117, 1633, 1616, 1643, 1619, 1637, 941, 55, 1632};
    public int[] Task15 = {1677, 122, 1601, 1612, 1831, 1622, 1620, 112, 117, 1633, 1616, 1643, 1619, 1637, 941, 55, 1632, 1604};
    public int[] Task16 = {122, 122, 122, 1831, 1831, 1622, 1622, 1620, 1620, 1620, 112, 112, 117, 117, 1633, 1616, 1643, 1619, 1637, 941, 55, 1632, 1604, 1624};
    public int[] Task17 = {1620, 112, 117, 1633, 1616, 1643, 1619, 1637, 941, 55, 1632, 1604, 1624, 1609};
    public int[] Task18 = {1620, 1620, 112, 112, 117, 117, 1633, 1633, 1616, 1616, 1643, 1619, 1619, 1637, 941, 55, 1632, 1604, 1624, 1609, 84, 83, 1610};
    public int[] Task19 = {1620, 1620, 1616, 1616, 1643, 1643, 1619, 1619, 1637, 941, 55, 1632, 1604, 1624, 1609, 84, 83, 1610, 1613};
    public int[] Task20 = {1620, 1620, 1620, 1616, 1616, 1643, 1643, 1619, 1637, 941, 55, 1632, 1604, 1624, 1609, 84, 83, 1610, 1613, 1615, 6221};
    public int[] Task21 = {1620, 1620, 1620, 1616, 1616, 1616, 1643, 1643, 1619, 1619, 1637, 1637, 941, 941, 55, 1632, 1604, 1624, 1609, 84, 83, 1610, 1613, 1615, 2783, 6221};

    public int[] Task19a = {1619, 1619, 1637, 941, 55, 1632, 1604, 1624, 1609, 84, 83, 1610, 1613};
    public int[] Task20a = {55, 1632, 1604, 1624, 1609, 84, 83, 1610, 1613, 1615, 6221};
    public int[] Task21a = {1624, 1609, 84, 83, 1610, 1613, 1615, 2783, 6221};

    public int random2nds() {
        return r2nds[(int) (Math.random() * r2nds.length)];
    }

    public int randomOre() {
        return Config.oreDrop[(int) (Math.random() * Config.oreDrop.length)];
    }

    public int randomBar() {
        return Config.barDrop[(int) (Math.random() * Config.barDrop.length)];
    }

    public int randomBolt() {
        return Config.boltDrop[(int) (Math.random() * Config.boltDrop.length)];
    }

    public int randomFish() {
        return rFish[(int) (Math.random() * rFish.length)];
    }

    public int randomHerb1() {
        return herb[(int) (Math.random() * herb.length)];
    }

    public int randomTask1() {
        return Task1[(int) (Math.random() * Task1.length)];
    }

    public int randomTask2() {
        return Task2[(int) (Math.random() * Task2.length)];
    }

    public int randomTask3() {
        return Task3[(int) (Math.random() * Task3.length)];
    }

    public int randomTask4() {
        return Task4[(int) (Math.random() * Task4.length)];
    }

    public int randomTask5() {
        return Task5[(int) (Math.random() * Task5.length)];
    }

    public int randomTask6() {
        return Task6[(int) (Math.random() * Task6.length)];
    }

    public int randomTask7() {
        return Task7[(int) (Math.random() * Task7.length)];
    }

    public int randomTask8() {
        return Task8[(int) (Math.random() * Task8.length)];
    }

    public int randomTask9() {
        return Task9[(int) (Math.random() * Task9.length)];
    }

    public int randomTask10() {
        return Task10[(int) (Math.random() * Task10.length)];
    }

    public int randomTask11() {
        return Task11[(int) (Math.random() * Task11.length)];
    }

    public int randomTask12() {
        return Task12[(int) (Math.random() * Task12.length)];
    }

    public int randomTask13() {
        return Task13[(int) (Math.random() * Task13.length)];
    }

    public int randomTask14() {
        return Task14[(int) (Math.random() * Task14.length)];
    }

    public int randomTask15() {
        return Task15[(int) (Math.random() * Task15.length)];
    }

    public int randomTask16() {
        return Task16[(int) (Math.random() * Task16.length)];
    }

    public int randomTask17() {
        return Task17[(int) (Math.random() * Task17.length)];
    }

    public int randomTask18() {
        return Task18[(int) (Math.random() * Task18.length)];
    }

    public int randomTask19() {
        return Task19[(int) (Math.random() * Task19.length)];
    }

    public int randomTask20() {
        return Task20[(int) (Math.random() * Task20.length)];
    }

    public int randomTask21() {
        return Task21[(int) (Math.random() * Task21.length)];
    }

    public int randomTask19a() {
        return Task19[(int) (Math.random() * Task19a.length)];
    }

    public int randomTask20a() {
        return Task20[(int) (Math.random() * Task20a.length)];
    }

    public int randomTask21a() {
        return Task21[(int) (Math.random() * Task21a.length)];
    }

    public void giveTask() {
        int taskId = 0;
        client.slayerTask = randomTask1();
        if (client.combatLevel >= 3 && client.combatLevel <= 30) {
            client.slayerAmount = 10 + Misc.random(40);
        }
        if (client.combatLevel >= 31 && client.combatLevel <= 100) {
            client.slayerAmount = 75 + Misc.random(60);
        }
        if (client.combatLevel >= 101) {
            client.slayerAmount = 100 + Misc.random(100);
        }
        if (client.playerLevel[18] >= 5 && client.playerLevel[18] <= 60) {//hands 1653
            taskId = 1;
            client.slayerTask = randomTask2();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 7 && client.playerLevel[18] <= 60) {//cave bugs 1832
            taskId = 2;
            client.slayerTask = randomTask3();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 10 && client.playerLevel[18] <= 60) {//cave crawlers 1601
            taskId = 3;
            client.slayerTask = randomTask4();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 15 && client.playerLevel[18] <= 60) {//banshees 1612
            taskId = 4;
            client.slayerTask = randomTask5();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 17 && client.playerLevel[18] <= 60) {//cave slime 1831
            taskId = 5;
            client.slayerTask = randomTask6();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 20 && client.playerLevel[18] <= 60) {//Rock Slug 1622
            taskId = 6;
            client.slayerTask = randomTask7();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 25 && client.playerLevel[18] <= 60) {//Cockatrice 1620
            taskId = 7;
            client.slayerTask = randomTask8();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 30 && client.playerLevel[18] <= 60) {//pryefiend 1633
            taskId = 8;
            client.slayerTask = randomTask9();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 40 && client.playerLevel[18] <= 60) {//Basilisk 1616
            taskId = 9;
            client.slayerTask = randomTask10();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 45 && client.playerLevel[18] <= 60) {//Infernal Mage 1643
            taskId = 10;
            client.slayerTask = randomTask11();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 50 && client.playerLevel[18] <= 60) {//Bloodveld 1618
            taskId = 11;
            client.slayerTask = randomTask12();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 52 && client.playerLevel[18] <= 60) {//Jelly 1637
            taskId = 12;
            client.slayerTask = randomTask13();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 55 && client.playerLevel[18] <= 60) {//Turoth 1632
            taskId = 13;
            client.slayerTask = randomTask14();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 60) {//Abby Spec 1604
            taskId = 14;
            client.slayerTask = randomTask15();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 65) {//Dust Devil 1624
            taskId = 15;
            client.slayerTask = randomTask16();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 70) {//Kurask 1609
            taskId = 16;
            client.slayerTask = randomTask17();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 75) {//Gargs 1610
            taskId = 17;
            client.slayerTask = randomTask18();
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 80) {//Nechs 1613
            taskId = 18;
            if (client.absY > 9800) {
                client.slayerTask = randomTask19a();
            } else {
                client.slayerTask = randomTask19();
            }
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 85) {//Abbys 1615
            taskId = 19;
            if (client.absY > 9800) {
                client.slayerTask = randomTask20a();
            } else {
                client.slayerTask = randomTask20();
            }
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }
        if (client.playerLevel[18] >= 90) {//Dark Beast 2783
            taskId = 20;
            if (client.absY > 9800) {
                client.slayerTask = randomTask21a();
            } else {
                client.slayerTask = randomTask21();
            }
            if (client.combatLevel >= 3 && client.combatLevel <= 30) {
                client.slayerAmount = 10 + Misc.random(40);
            }
            if (client.combatLevel >= 31 && client.combatLevel <= 100) {
                client.slayerAmount = 75 + Misc.random(50);
            }
            if (client.combatLevel >= 101) {
                client.slayerAmount = 100 + Misc.random(100);
            }
        }

    }

    public void sendFrame34(int frame, int item, int slot, int amount) {
        client.getOutStream().createFrameVarSizeWord(34);
        client.getOutStream().writeWord(frame);
        client.getOutStream().writeByte(slot);
        client.getOutStream().writeWord(item + 1);
        client.getOutStream().writeByte(255);
        client.getOutStream().writeDWord(amount);
        client.getOutStream().endFrameVarSizeWord();
    }

    public void itemOnInterface(int frame, int item, int slot, int amount) {
        client.getOutStream().createFrameVarSizeWord(34);
        client.getOutStream().writeWord(frame);
        client.getOutStream().writeByte(slot);
        client.getOutStream().writeWord(item + 1);
        client.getOutStream().writeByte(255);
        client.getOutStream().writeDWord(amount);
        client.getOutStream().endFrameVarSizeWord();
    }

    public void openSilverInterface() {
        showInterface(13782);
        itemOnInterface(15445, 1716, 0, 1);//holy symbol
        itemOnInterface(15452, 1722, 0, 1);//unholy symbol
        itemOnInterface(15459, 2961, 0, 1);//silver sickle
        itemOnInterface(15466, 2961, 0, 1);//silvthrill rod(silver sickle item)
        itemOnInterface(15473, 5525, 0, 1);//tiara
        itemOnInterface(15481, 6748, 0, 1);//demonic sigil
    }

    public int[] mould = {1599, 1594, 2976, 2976, 5523, 6747};
    public int[] sItem = {1716, 1722, 2961, 2961, 5525, 6748};
    public int[] XPsilver = {50, 50, 50, 50, 50, 70};
    public int[] silverLevel = {10, 20, 30, 40, 50, 60};
    public boolean silverCraft, goldCraft;
    public int oldQ, oldAm;
    public int crafted;

    public void smeltSilver(int amount, int q) {
        oldQ = q;
        oldAm = amount;
        silverCraft = true;
        if (playerHasItem(mould[q]) && playerHasItem(2355)) {
            if (client.playerLevel[12] >= silverLevel[q]) {
                startAnimation(0x383);
                if (System.currentTimeMillis() - client.lastCraft > 1000) {
                    if (crafted < amount) {
                        client.lastCraft = System.currentTimeMillis();
                        removeAllWindows();
                        deleteItem(2355, 1);
                        addItem(sItem[q], 12);
                        //crafted++; the heck is the point
                        addSkillXP(XPsilver[q], 1);
                    } else {
                        silverCraft = false;
                        crafted = 0;
                    }
                }
            } else {
                sendMessage("You need a crafting level of " + silverLevel[q] + " to craft this.");
                silverCraft = false;
                crafted = 0;
            }
        } else {
            sendMessage("You need to have at least 1 silver bar and the right mould to craft silver.");
            silverCraft = false;
            crafted = 0;
        }
    }

    public int[] rings = {1635, 1637, 1639, 1641, 1643, 1645, 6575};
    public int[] necks = {1654, 1656, 1658, 1660, 1662, 1664, 6577};
    public int[] ammys = {1673, 1675, 1677, 1679, 1681, 1683, 6579};
    public int[] gneed = {-1, 1607, 1605, 1603, 1601, 1615, 6573};


    public void openGoldInterface() {
        showInterface(4161);
        int frames = 7;
        client.flushOutStream();
        for (int i = 0; i < frames; i++) {
            if (playerHasItem(1592)) {
                if (playerHasItem(gneed[i])) {
                    itemOnInterface(4233, rings[i], i, 1);
                    sendFrame126("", 4230);
                } else {
                    itemOnInterface(4233, -1, i, 1);
                }
            } else {
                itemOnInterface(4233, -1, i, 1);
                sendFrame126("You need a ring mould to craft rings.", 4230);
            }
            if (playerHasItem(1597)) {
                if (playerHasItem(gneed[i])) {
                    itemOnInterface(4239, necks[i], i, 1);
                    sendFrame126("", 4236);
                } else {
                    itemOnInterface(4239, -1, i, 1);
                }
            } else {
                itemOnInterface(4239, -1, i, 1);
                sendFrame126("You need a necklace mould to craft necklaces", 4236);
            }
            if (playerHasItem(1595)) {
                if (playerHasItem(gneed[i])) {
                    itemOnInterface(4245, ammys[i], i, 1);
                    sendFrame126("", 4242);
                } else {
                    itemOnInterface(4245, -1, i, 1);
                }
            } else {
                itemOnInterface(4245, -1, i, 1);
                sendFrame126("You need an amulet mould to craft amulets", 4242);
            }
        }
    }

    public int[] ringL = {5, 20, 27, 34, 43, 55, 67};
    public int[] ringX = {15, 40, 55, 70, 85, 100, 115};
    public int[] neckL = {6, 22, 29, 40, 56, 72, 82};
    public int[] neckX = {20, 55, 60, 75, 90, 105, 120};
    public int[] ammyL = {8, 24, 31, 50, 70, 80, 90};
    public int[] ammyX = {30, 65, 70, 85, 100, 150, 165};

    public void multiGoldCraft(final String type, final int amount, final int q) {
        client.walked = false;
        if (!goldCraft(type, 1, q)) return;
        EventManager.getSingleton().addEvent(client, "multigoldcraft", new Event() {
            int amountLeft = amount - 1;
            boolean success = true;

            public void stop() {
                client.getActionAssistant().startAnimation(2552);
                client.setBusy(false);
            }

            public void execute(EventContainer c) {
                if (amountLeft == 0 || client.walked || !success) {
                    client.setBusy(false);
                    c.stop();
                    return;
                }
                if (amountLeft > 0) {
                    amountLeft--;
                    if (client.walked || !success) {
                        client.setBusy(false);
                        c.stop();
                        return;
                    }
                    success = goldCraft(type, 1, q);
                } else {
                    client.setBusy(false);
                    c.stop();
                }
            }
        }, 1000);
    }

    public boolean goldCraft(String type, int amount, int q) {
        oldQ = q;
        oldAm = amount;
        goldCraft = true;
        if (type == "ring") {
            if (client.playerLevel[12] >= ringL[q]) {
                if (playerHasItem(1592, 1) && playerHasItem(2357, 1) && playerHasItem(gneed[q], 1)) {
                    if (System.currentTimeMillis() - client.lastCraft >= 1000) {
                        if (crafted < amount) {
                            startAnimation(0x383);
                            client.lastCraft = System.currentTimeMillis();
                            removeAllWindows();
                            deleteItem(2357, 1);
                            deleteItem(gneed[q], 1);
                            addItem(rings[q], 1);
                            //crafted++;
                            addSkillXP(ringX[q], 12);
                            return true;
                        } else {
                            goldCraft = false;
                            crafted = 0;
                            return false;
                        }
                    }
                } else {
                    sendMessage("You need to have at least 1 gold bar and the right mould.");
                    //sendMessage("You need to have at least 1 gold bar, the right mould and a "+Server.getItemManager().getItemDefinition(gneed[q]).getName().toLowerCase()+".");
                    goldCraft = false;
                    crafted = 0;
                    return false;
                }
            } else {
                sendMessage("You need a crafting level of " + ringL[q] + " to craft this.");
                goldCraft = false;
                crafted = 0;
                return false;
            }
        } else if (type == "neck") {
            if (client.playerLevel[12] >= neckL[q]) {
                if (playerHasItem(1597, 1) && playerHasItem(2357, 1) && playerHasItem(gneed[q], 1)) {
                    if (System.currentTimeMillis() - client.lastCraft >= 1000) {
                        if (crafted < amount) {
                            startAnimation(0x383);
                            client.lastCraft = System.currentTimeMillis();
                            removeAllWindows();
                            deleteItem(2357, 1);
                            deleteItem(gneed[q], 1);
                            addItem(necks[q], 12);
                            //crafted++;
                            addSkillXP(neckX[q], 12);
                            return true;
                        } else {
                            goldCraft = false;
                            crafted = 0;
                            return false;
                        }
                    }
                } else {
                    sendMessage("You need to have at least 1 gold bar and the right mould.");
                    //sendMessage("You need to have at least 1 gold bar, the right mould and a "+Server.getItemManager().getItemDefinition(gneed[q]).getName().toLowerCase()+".");
                    goldCraft = false;
                    crafted = 0;
                    return false;
                }
            } else {
                sendMessage("You need a crafting level of " + neckL[q] + " to craft this.");
                goldCraft = false;
                crafted = 0;
                return false;
            }
        } else {
            if (client.playerLevel[12] >= ammyL[q]) {
                if (playerHasItem(1595, 1) && playerHasItem(2357, 1) && playerHasItem(gneed[q], 1)) {
                    if (System.currentTimeMillis() - client.lastCraft >= 1000) {
                        if (crafted < amount) {
                            startAnimation(0x383);
                            client.lastCraft = System.currentTimeMillis();
                            removeAllWindows();
                            deleteItem(2357, 1);
                            deleteItem(gneed[q], 1);
                            addItem(ammys[q], 12);
                            //crafted++;
                            addSkillXP(ammyX[q], 12);
                            return true;
                        } else {
                            goldCraft = false;
                            crafted = 0;
                            return false;
                        }
                    }
                } else {
                    //sendMessage("You need to have at least 1 gold bar, the right mould and a "+Server.getItemManager().getItemDefinition(gneed[q]).getName().toLowerCase()+".");
                    sendMessage("You need to have at least 1 gold bar and the right mould.");
                    goldCraft = false;
                    crafted = 0;
                    return false;
                }
            } else {
                sendMessage("You need a crafting level of " + ammyL[q] + " to craft this.");
                goldCraft = false;
                crafted = 0;
                return false;
            }
        }
        return false;
    }

    public int HideID = 0;

    public void CraftHide(int Hide, int LevelNeeded, int ItemGained, int DeletedHide, int DeletedHideAmount, int DeletedThread, int DeletedThreadAmount, int EXPGained) {
        if (HideID == Hide) {
            if (client.playerLevel[12] >= LevelNeeded) {
                if ((playerHasItemAmount(DeletedHide, DeletedHideAmount))
                        && playerHasItemAmount(DeletedThread, DeletedThreadAmount)) {
                    startAnimation(885);
                    deleteItem(DeletedHide, DeletedHideAmount);
                    deleteItem(DeletedThread, DeletedThreadAmount);
                    addItem(ItemGained, 1);
                    addSkillXP(EXPGained, 12);
                    sendMessage("You craft the " + Server.getItemManager().getItemDefinition(DeletedHide).getName().toLowerCase() + " into " + Server.getItemManager().getItemDefinition(ItemGained).getName().toLowerCase());
                } else {
                    sendMessage("You need " + DeletedHideAmount + " " + Server.getItemManager().getItemDefinition(DeletedHide).getName().toLowerCase() + " to make a " + Server.getItemManager().getItemDefinition(ItemGained).getName().toLowerCase());
                }
            } else if (client.playerLevel[12] < LevelNeeded) {
                sendMessage("You need a Crafting level of " + LevelNeeded + " to make a " + Server.getItemManager().getItemDefinition(ItemGained).getName().toLowerCase());
                removeAllWindows();
            }
        }
        client.dhide = false;
    }

    public void CraftHideX(int Hide, int AmountToDo, int LevelNeeded, int ItemGained, int DeletedHide, int DeletedHideAmount, int DeletedThread, int DeletedThreadAmount, int EXPGained) {
        if (HideID == Hide) {
            if (client.playerLevel[12] >= LevelNeeded) {
                for (int X = 0; X < AmountToDo; X++) {
                    if ((playerHasItemAmount(DeletedHide, DeletedHideAmount))
                            && playerHasItemAmount(DeletedThread, DeletedThreadAmount)) {
                        startAnimation(885);
                        deleteItem(DeletedHide, DeletedHideAmount);
                        deleteItem(DeletedThread, DeletedThreadAmount);
                        addItem(ItemGained, 1);
                        addSkillXP(EXPGained, 12);
                        sendMessage("You craft the " + Server.getItemManager().getItemDefinition(DeletedHide).getName().toLowerCase() + " into " + Server.getItemManager().getItemDefinition(ItemGained).getName().toLowerCase());
                    } else {
                        if (X > 0 && AmountToDo == 30) break;
                        sendMessage("You need " + DeletedHideAmount + " " + Server.getItemManager().getItemDefinition(DeletedHide).getName().toLowerCase() + " to make a " + Server.getItemManager().getItemDefinition(ItemGained).getName().toLowerCase());
                        break;
                    }
                }
            } else if (client.playerLevel[12] < LevelNeeded) {
                sendMessage("You need a Crafting level of " + LevelNeeded + " to make a " + Server.getItemManager().getItemDefinition(ItemGained).getName().toLowerCase());
                removeAllWindows();
            }
        }
        client.dhide = false;
    }

    public void GreenDragonhideCrafting() {
        HideID = 1;
        sendFrame164(8880);
        sendFrame246(8883, 250, 1065);
        sendFrame246(8884, 200, 1099);
        sendFrame246(8885, 200, 1135);
        sendFrame126("Green d'hide body", 8897);
        sendFrame126("Green d'hide chaps", 8893);
        sendFrame126("Green d'hide vamb", 8889);
    }

    public void BlueDragonhideCrafting() {
        HideID = 2;
        sendFrame164(8880);
        sendFrame246(8883, 250, 2487);
        sendFrame246(8884, 200, 2493);
        sendFrame246(8885, 200, 2499);
        sendFrame126("Blue d'hide body", 8897);
        sendFrame126("Blue d'hide chaps", 8893);
        sendFrame126("Blue d'hide vambs", 8889);
    }

    public void RedDragonhideCrafting() {
        HideID = 3;
        sendFrame164(8880);
        sendFrame246(8883, 250, 2489);
        sendFrame246(8884, 200, 2495);
        sendFrame246(8885, 200, 2501);
        sendFrame126("Red d'hide body", 8897);
        sendFrame126("Red d'hide chaps", 8893);
        sendFrame126("Red d'hide vambs", 8889);
    }

    public void BlackDragonhideCrafting() {
        HideID = 4;
        sendFrame164(8880);
        sendFrame246(8883, 250, 2491);
        sendFrame246(8884, 200, 2497);
        sendFrame246(8885, 200, 2503);
        sendFrame126("Black d'hide body", 8897);
        sendFrame126("Black d'hide chaps", 8893);
        sendFrame126("Black d'hide vambs", 8889);
    }

    public void tanningInterface() {
        showInterface(14670);
        sendQuest("Leather", 14777);
        sendQuest("50gp", 14785);
        sendQuest("Hard Leather", 14781);
        sendQuest("100gp", 14789);
        sendFrame246(14769, 250, 1741);
        sendFrame246(14773, 250, 1743);
        sendFrame246(14771, 250, 1753);
        sendFrame246(14772, 250, 1751);
        sendFrame246(14775, 250, 1749);
        sendFrame246(14776, 250, 1747);
        sendQuest("", 14778);
        sendQuest("", 14786);
        sendQuest("", 14782);
        sendQuest("", 14790);
        int[] Line = {14779, 14787, 14783, 14791, 14780, 14788, 14784, 14792};
        String[] HideColor = {"Green", "Red", "Blue", "Black"};
        String[] HideCost = {"200", "300", "400", "500"};
        int HC = 0, i2 = 0;
        for (int i = 0; i < Line.length; i++) {
            if (HC == 0) {
                sendQuest(HideColor[(int) (i / 2)], Line[i]);
                HC = 1;
            } else {
                sendQuest(HideCost[(int) (i / 2)], Line[i]);
                HC = 0;
            }
        }
    }

    public void tannerX(int AmountToDo, int ItemGained, int DeletedItem, int MoneyLoss) {
        if (client.playerLevel[12] >= 0) {
            for (int X = 0; X < AmountToDo; X++) {
                if (!playerHasItemAmount(995, MoneyLoss)) {
                    sendMessage("You ran out of gp.");
                    removeAllWindows();
                    break;
                }
                if (playerHasItemAmount(DeletedItem, 1)) {
                    deleteItem(DeletedItem, client.getActionAssistant().getItemSlot(DeletedItem), 1);
                    deleteItem(995, MoneyLoss);
                    addItem(ItemGained, 1);
                    //sendMessage("The tanner tan's your hides!");
                    removeAllWindows();
                } else {
                    if (AmountToDo == 99) break;
                    sendMessage("You need " + Server.getItemManager().getItemDefinition(DeletedItem).getName().toLowerCase() + " in order to tan " + Server.getItemManager().getItemDefinition(ItemGained).getName().toLowerCase());
                    removeAllWindows();
                    break;
                }
            }
        }
    }

    public void musicPlayer() {

    }

    public void npc(int face, String line1, String line2, String line3, String line4, int npcID) {
        NPC npc = Server.getNpcManager().getNPC(npcID);
        sendFrame200(4901, face);
        sendFrame126(npc.getDefinition().getName(), 4902);
        sendFrame126("" + line1, 4903);
        sendFrame126("" + line2, 4904);
        sendFrame126("" + line3, 4905);
        sendFrame126("" + line4, 4906);
        sendFrame126("Click here to continue", 4907);
        sendFrame75(npcID, 4901);
        sendFrame164(4900);
        this.client.getDialogueAssistant().setCurrentDialogue(
                smeltMessage, -1);
    }

    public void PcPanel1() {
        showInterfaceWalkable(15892);
        //showInterface(15892);
        sendQuest("Pest Control", 15894);
        sendQuest("Game starts in: " + PlayerManager.pcWaitTimer + "", 15895);
        sendQuest("Points: " + client.pcPoints + "", 15897);
        sendQuest("", 15898);
        sendQuest("", 15899);
        sendQuest("", 15900);
        sendQuest("", 15901);
        sendQuest("", 15896);
        sendQuest("", 15902);
        sendQuest("", 15903);
        sendQuest("", 15904);
        sendQuest("", 15905);
        sendQuest("", 15906);
    }

    public void PcPanel2() {
        showInterfaceWalkable(15892);
        sendQuest("Purple portal", 15895);
        if (PlayerManager.portal1) {
            sendQuest("@red@Down", 15895);
        }
        sendQuest("Blue portal", 15897);
        if (PlayerManager.portal2) {
            sendQuest("@red@Down", 15897);
        }
        sendQuest("Yellow portal", 15898);
        if (PlayerManager.portal3) {
            sendQuest("@red@Down", 15898);
        }
        sendQuest("Red portal", 15899);
        if (PlayerManager.portal4) {
            sendQuest("@red@Down", 15899);
        }
        sendQuest("" + PlayerManager.pcGameTimer + "", 15900);
        sendQuest("", 15901);
        sendQuest("", 15896);
        sendQuest("", 15902);
        sendQuest("", 15903);
        sendQuest("", 15904);
        sendQuest("", 15905);
        sendQuest("", 15906);
    }

    public void redpanel() {
        if (inred || inblue) {
            showInterfaceWalkable(14500);
            sendQuest("Captain name: " + PlayerManager.RedCaptain.playerName, 14503);
            sendQuest("Captain name: " + PlayerManager.BlueCaptain.playerName, 118707);
            sendQuest("Captain HP: " + PlayerManager.RedCaptain.playerLevel[3], 14504);
            sendQuest("Captain HP: " + PlayerManager.BlueCaptain.playerLevel[3], 14506);
            sendQuest("" + PlayerManager.bluepoints, 14508);
            sendQuest("" + PlayerManager.redpoints, 14507);
            return;
        }
        showInterfaceWalkable(15892);
        sendQuest("@or1@CtC", 15894);
        sendQuest("Players waiting:  @red@" + PlayerManager.redwaiters + "      @blu@" + PlayerManager.bluewaiters, 15895);
        sendQuest("Players in-game: @red@" + PlayerManager.redteam + "      @blu@" + PlayerManager.blueteam, 15897);
        sendQuest("Points:                  @red@" + PlayerManager.redpoints + "      @blu@" + PlayerManager.bluepoints, 15898);
        sendQuest("Time left: " + PlayerManager.CtCtimer / 2 + " seconds", 15901);
        String game = "@red@Game in progress";
        if (PlayerManager.CtCtimer != 100)
            game = "";
        sendQuest(game, 15900);
        sendQuest("", 15896);
        sendQuest("", 15902);
        sendQuest("", 15903);
        sendQuest("", 15904);
        sendQuest("", 15905);
        sendQuest("", 15906);
        sendQuest("", 15899);
    }

    public void startround(int i) {
        if (client.inred) {
            client.getActionAssistant().movePlayer(2371 + Misc.random(6), 3128 + Misc.random(3), 0);
        }
        if (client.inblue) {
            client.getActionAssistant().movePlayer(2422 + Misc.random(6), 3077 + Misc.random(3), 0);
        }
        //ResetAttack();
        client.getActionAssistant().createPlayerHints(10, i);
    }

    public void endgame() {
        showInterfaceWalkable(13588);

        client.getActionAssistant().movePlayer(2439 + Misc.random(6), 3083 + Misc.random(3), 0);
        client.getActionAssistant().createPlayerHints(10, 0);
        client.headIcon = -1;
        client.updateRequired = true;
        client.appearanceUpdateRequired = true;
    }

    public void forceon(int Slot, int ID) {
        client.playerEquipment[Slot] = ID;
        client.playerEquipmentN[Slot] = 1;
        client.getOutStream().createFrameVarSizeWord(34);
        client.getOutStream().writeWord(1688);
        client.getOutStream().writeByte(Slot);
        client.getOutStream().writeWord(ID + 1);
        client.getOutStream().writeByte(1); //amount
        client.getOutStream().endFrameVarSizeWord();
    }

    public void armourgone(int Slot) {
        client.playerEquipment[Slot] = -1;
        client.playerEquipmentN[Slot] = 0;
        client.getOutStream().createFrame(34);
        client.getOutStream().writeWord(6);
        client.getOutStream().writeWord(1688);
        client.getOutStream().writeByte(Slot);
        client.getOutStream().writeWord(0);
        client.getOutStream().writeByte(0);
    }

    public boolean redteam = false;
    public boolean blueteam = false;
    public boolean inred = false;
    public boolean inblue = false;
    public boolean captain = false;


    public void resetpc() {
			/*for (int i = 0; i < server.npcHandler.maxNPCs; i++) {
				if (server.npcHandler.npcs[i] != null) {
					if(server.npcHandler.npcs[i].npcType == 3777 || server.npcHandler.npcs[i].npcType == 3778 || server.npcHandler.npcs[i].npcType == 3779 || server.npcHandler.npcs[i].npcType == 3780){
						server.npcHandler.npcs[i].client.getX() = 0;
						server.npcHandler.npcs[i].absY = 0;
					}
				}
			}*/
    }

    public void checkApp() {
        if (client.playerAppearance[0] == 0) {
            if (client.playerAppearance[1] < 0 || client.playerAppearance[1] > 9)
                client.playerAppearance[1] = 1;
            if (client.playerAppearance[7] < 10 || client.playerAppearance[7] > 17)
                client.playerAppearance[7] = 10;
            if (client.playerAppearance[2] < 18 || client.playerAppearance[2] > 25)
                client.playerAppearance[2] = 18;
            if (client.playerAppearance[3] < 26 || client.playerAppearance[3] > 32)
                client.playerAppearance[3] = 26;
            if (client.playerAppearance[4] < 33 || client.playerAppearance[4] > 35)
                client.playerAppearance[4] = 33;
            if (client.playerAppearance[5] < 36 || client.playerAppearance[5] > 41)
                client.playerAppearance[5] = 36;
            if (client.playerAppearance[6] < 42 || client.playerAppearance[6] > 44)
                client.playerAppearance[6] = 42;
        } else if (client.playerAppearance[0] == 1) {
            if (client.playerAppearance[1] < 45 || client.playerAppearance[1] > 55)
                client.playerAppearance[1] = 50;
            if (client.playerAppearance[7] > 0)
                client.playerAppearance[7] = 1;
            if (client.playerAppearance[2] < 61 || client.playerAppearance[2] > 66)
                client.playerAppearance[2] = 57;
            if (client.playerAppearance[3] < 67 || client.playerAppearance[3] > 69)
                client.playerAppearance[3] = 62;
            if (client.playerAppearance[4] < 70 || client.playerAppearance[4] > 78)
                client.playerAppearance[4] = 68;
            if (client.playerAppearance[5] < 79 || client.playerAppearance[5] > 81)
                client.playerAppearance[5] = 72;
            if (client.playerAppearance[6] < 82 || client.playerAppearance[6] > 84)
                client.playerAppearance[6] = 80;
        }
        client.appearanceUpdateRequired = true;
        client.updateRequired = true;
    }

    /**
     * Dueling
     */

    public CopyOnWriteArrayList<GameItem> otherStakedItems = new CopyOnWriteArrayList<GameItem>();
    public CopyOnWriteArrayList<GameItem> stakedItems = new CopyOnWriteArrayList<GameItem>();

    public void requestDuel(int id) {
        try {
            if (PlayerManager.updateAnnounced) {
                client.getActionAssistant().Send("Dueling Disabled During Updates.");
                client.getTradeAssistant().decline();
                client.cancelTasks();
                return;
            }
            if (client.inCombat) {
                client.getActionAssistant().sendMessage("You cant stake in combat!");
                return;
            }
            if (System.currentTimeMillis() - client.duelRequestTimer < 10000) {
                client.getActionAssistant().sendMessage("You can only request a duel 10 seconds after teleporting!");
                return;
            }
            if (Config.WORLD_NUMBER != 1 && Config.WORLD_NUMBER != 1 && Config.WORLD_NUMBER != 5) {
                client.getActionAssistant().sendMessage("You can only stake on world 2 or 4!");
                return;
            }
		/*	if (true) {
			client.getActionAssistant().sendMessage("@red@Click me: +http://www.runerepublic.com#url#");
			return;
			}*/
            if (client.duelScreenOne) return;
            resetDuel();
            resetDuelItems();
            //
            Client o = (Client) PlayerManager.getSingleton().getPlayers()[id];
            if (o.inCombat) {
                o.getActionAssistant().sendMessage("You cant stake in combat!");
                return;
            }
            if (System.currentTimeMillis() - o.duelRequestTimer < 10000) {
                client.getActionAssistant().sendMessage("You can only request a duel 10 seconds after another player teleporting!");
                return;
            }
            client.getCombat().curepoison();
            o.getCombat().curepoison();
            if (o == null) {
                return;
            }
            if (o.duelStatus > 0 || client.duelStatus > 0) {
                return;
            }
            client.duelRequested = true;
            client.duelingWith = id;
            if (client.duelStatus == 0 && o.duelStatus == 0 && client.duelRequested && o.duelRequested && client.duelingWith == o.getId() && o.duelingWith == client.getId()) {
                if (client.goodDistance(client.getX(), client.getY(), o.getX(), o.getY(), 1)) {
                    client.getActionAssistant().openDuel();
                    o.getActionAssistant().openDuel();
                    client.duelOK = true;
                    o.duelOK = true;
                    client.duelOKID = o.playerId;
                    o.duelOKID = client.playerId;
                } else {
                    client.getActionAssistant().sendMessage("You need to get closer to your opponent to start the duel.");
                }
            } else {
                if ((client.inDuelArena() && !(client.absX > 3379 && client.absY > 3264))) {
                    client.getActionAssistant().sendMessage("Sending duel request...");
                    o.getActionAssistant().sendMessage(client.playerName + ":duelreq:");
                } else {
                    client.getActionAssistant().sendMessage("You cannot challenge here!");
                }
            }

        } catch (Exception e) {
            Misc.println("Error requesting duel.");
        }

    }

    public void openDuel() {
        client.duelScreenOne = true;
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        client.killStreak = 0;
        o.killStreak = 0;
        o.getActionAssistant().writeQuestTab();
        client.getActionAssistant().writeQuestTab();
        for (int i = 1; i < Constants.MAXIMUM_PLAYERS; i++) {
            Player p = PlayerManager.getSingleton().getPlayers()[i];
            if (p != null) {
                Client person = (Client) PlayerManager.getSingleton().getPlayers()[i];
                if (person.playerId == client.playerId) {
                    continue;
                }
                if (person.secondDuelScreen || person.duelScreenOne) {
                    if ((person.duelingWith == client.playerId) && !(client.duelingWith == person.playerId)) {
                        try {
                            //BanProcessor.banUser(client.playerName,"duel screen part 2");
                            //client.kick();
                            //client.disconnected = true;
                            client.getActionAssistant().declineDuel();
                            o.getActionAssistant().declineDuel();
                            person.getActionAssistant().declineDuel();
                            //} catch (IOException e) {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        o.duelScreenOne = true;
        client.getCombat().curepoison();
        o.getCombat().curepoison();
        if (o == null) {
            return;
        }
        if (client.newFag > 0) {
            client.getActionAssistant().declineDuel();
            o.getActionAssistant().declineDuel();
            client.getActionAssistant().sendMessage("You cannot duel under New Player Protection. (" + (int) (client.newFag / 2) + " seconds remaining)");
            return;
        }
        //client.getActionAssistant().lockMiniMap(true);
        client.inCombat = true;
        o.inCombat = true;
        client.duelStatus = 1;
        refreshduelRules();
        refreshDuelScreen();
        client.canOffer = true;
        for (int i = 0; i < client.playerEquipment.length; i++) {
            sendDuelEquipment(client.playerEquipment[i], client.playerEquipmentN[i], i);
        }
        client.getActionAssistant().sendFrame126("Dueling with: " + o.playerName + " (level-" + o.getCombatLevel() + ")", 6671);
        client.getActionAssistant().sendFrame126("", 6684);
        client.getActionAssistant().sendFrame248(6575, 3321);
        client.getActionAssistant().resetItems(3322);

    }

    public void sendDuelEquipment(int itemId, int amount, int slot) {
        if (itemId != 0) {
            client.getOutStream().createFrameVarSizeWord(34);
            client.getOutStream().writeWord(13824);
            client.getOutStream().writeByte(slot);
            client.getOutStream().writeWord(itemId + 1);

            if (amount > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord(amount);
            } else {
                client.getOutStream().writeByte(amount);
            }
            client.getOutStream().endFrameVarSizeWord();
            client.flushOutStream();
        }

    }

    public void sendFrame87(int id, int state) {
        client.getOutStream().createFrame(87);
        client.getOutStream().writeWordBigEndian_dup(id);
        client.getOutStream().writeDWord_v1(state);
        client.flushOutStream();
    }

    public void refreshduelRules() {
        for (int i = 0; i < client.duelRule.length; i++) {
            client.duelRule[i] = false;
        }
        client.getActionAssistant().sendFrame87(286, 0);
        client.duelOption = 0;
    }

    public void refreshDuelScreen() {
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (o == null) {
            return;
        }
        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(6669);
        client.getOutStream().writeWord(stakedItems.toArray().length);
        int current = 0;
        for (GameItem item : stakedItems) {
            if (item.amount > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord_v2(item.amount);
            } else {
                client.getOutStream().writeByte(item.amount);
            }
            //if (item.id > 13672 || item.id < 0) {
            //item.id = 13672;
            //}
            client.getOutStream().writeWordBigEndianA(item.id + 1);

            current++;
        }

        if (current < 27) {
            for (int i = current; i < 28; i++) {
                client.getOutStream().writeByte(1);
                client.getOutStream().writeWordBigEndianA(-1);
            }
        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();

        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(6670);
        client.getOutStream().writeWord(o.getActionAssistant().stakedItems.toArray().length);
        current = 0;
        for (GameItem item : o.getActionAssistant().stakedItems) {
            if (item.amount > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord_v2(item.amount);
            } else {
                client.getOutStream().writeByte(item.amount);
            }
            //if (item.id > 13672 || item.id < 0) {
            //item.id = 13672;
            //}
            client.getOutStream().writeWordBigEndianA(item.id + 1);
            current++;
        }

        if (current < 27) {
            for (int i = current; i < 28; i++) {
                client.getOutStream().writeByte(1);
                client.getOutStream().writeWordBigEndianA(-1);
            }
        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();

    }


    public boolean stakeItem(int itemID, int fromSlot, int amount) {

        for (int i : Config.ITEM_TRADEABLE) {
            if (i == itemID) {
                client.getActionAssistant().sendMessage("You can't trade this item.");
                return false;
            }
        }
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (o == null) {
            declineDuel();
            return false;
        }
        if (o.duelStatus <= 0 || client.duelStatus <= 0) {
            declineDuel();
            o.getActionAssistant().declineDuel();
            return false;
        }
        if (!client.canOffer) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        changeDuelStuff();
        if (!Item.itemStackable[itemID]) {
            for (int a = 0; a < amount; a++) {
                if (client.getActionAssistant().playerHasItem(itemID, 1)) {
                    stakedItems.add(new GameItem(itemID, 1));
                    client.getActionAssistant().deleteItem(itemID, client.getActionAssistant().getItemSlot(itemID), 1);
                }
            }
            client.getActionAssistant().resetItems(3214);
            client.getActionAssistant().resetItems(3322);
            o.getActionAssistant().resetItems(3214);
            o.getActionAssistant().resetItems(3322);
            refreshDuelScreen();
            o.getActionAssistant().refreshDuelScreen();
            client.getActionAssistant().sendFrame126("", 6684);
            o.getActionAssistant().sendFrame126("", 6684);
        }

        if (!client.getActionAssistant().playerHasItem(itemID, amount)) {
            return false;
        }
        if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
            boolean found = false;
            for (GameItem item : stakedItems) {
                if (item.id == itemID) {
                    found = true;
                    item.amount += amount;
                    //	client.getActionAssistant().deleteItem(itemID, fromSlot, amount);
                    client.getActionAssistant().deleteItem(itemID, client.getActionAssistant().getItemSlot(itemID), amount);
                    break;
                }
            }
            if (!found) {
//					client.getActionAssistant().deleteItem(itemID, fromSlot, amount);
                client.getActionAssistant().deleteItem(itemID, client.getActionAssistant().getItemSlot(itemID), amount);
                stakedItems.add(new GameItem(itemID, amount));
            }
        }

        client.getActionAssistant().resetItems(3214);
        client.getActionAssistant().resetItems(3322);
        o.getActionAssistant().resetItems(3214);
        o.getActionAssistant().resetItems(3322);
        refreshDuelScreen();
        o.getActionAssistant().refreshDuelScreen();
        client.getActionAssistant().sendFrame126("", 6684);
        o.getActionAssistant().sendFrame126("", 6684);
        return true;
    }


    public boolean fromDuel(int itemID, int fromSlot, int amount) {
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (o == null) {
            declineDuel();
            return false;
        }
        if (o.duelStatus <= 0 || client.duelStatus <= 0) {
            declineDuel();
            o.getActionAssistant().declineDuel();
            return false;
        }
        if (!client.duelScreenOne || !o.duelScreenOne) {
            declineDuel();
            o.getActionAssistant().declineDuel();
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        if (Item.itemStackable[itemID]) {
            if (client.getActionAssistant().freeSlots() - 1 < (client.duelSpaceReq + 1)) {
                client.getActionAssistant().sendMessage("You have too many rules set to remove that item.");
                return false;
            }
        }

        if (!client.canOffer) {
            return false;
        }

        changeDuelStuff();
        boolean goodSpace = true;
        if (!Item.itemStackable[itemID]) {
            for (int a = 0; a < amount; a++) {
                for (GameItem item : stakedItems) {
                    if (item.id == itemID) {
                        if (!item.stackable) {
                            if (client.getActionAssistant().freeSlots() - 1 < (client.duelSpaceReq + 1)) {
                                goodSpace = false;
                                break;
                            }
                            stakedItems.remove(item);
                            client.getActionAssistant().addItem(itemID, 1);
                        } else {
                            if (client.getActionAssistant().freeSlots() - 1 < (client.duelSpaceReq + 1)) {
                                goodSpace = false;
                                break;
                            }
                            if (item.amount > amount) {
                                item.amount -= amount;
                                client.getActionAssistant().addItem(itemID, amount);
                            } else {
                                if (client.getActionAssistant().freeSlots() - 1 < (client.duelSpaceReq + 1)) {
                                    goodSpace = false;
                                    break;
                                }
                                amount = item.amount;
                                stakedItems.remove(item);
                                client.getActionAssistant().addItem(itemID, amount);
                            }
                        }
                        break;
                    }
                    o.duelStatus = 1;
                    client.duelStatus = 1;
                    client.getActionAssistant().resetItems(3214);
                    client.getActionAssistant().resetItems(3322);
                    o.getActionAssistant().resetItems(3214);
                    o.getActionAssistant().resetItems(3322);
                    client.getActionAssistant().refreshDuelScreen();
                    o.getActionAssistant().refreshDuelScreen();
                    o.getActionAssistant().sendFrame126("", 6684);
                }
            }
        }


        boolean found = false;
        for (GameItem item : stakedItems) {
            if (item.id == itemID) {
                if (!item.stackable) {
                    found = true;
                } else {
                    if (item.amount > amount) {
                        item.amount -= amount;
                        found = true;
                        client.getActionAssistant().addItem(itemID, amount);
                    } else {
                        amount = item.amount;
                        found = true;
                        stakedItems.remove(item);
                        client.getActionAssistant().addItem(itemID, amount);
                    }
                }
                break;
            }
        }

        if (found)
            o.duelStatus = 1;
        client.duelStatus = 1;
        client.getActionAssistant().resetItems(3214);
        client.getActionAssistant().resetItems(3322);
        o.getActionAssistant().resetItems(3214);
        o.getActionAssistant().resetItems(3322);
        client.getActionAssistant().refreshDuelScreen();
        o.getActionAssistant().refreshDuelScreen();
        o.getActionAssistant().sendFrame126("", 6684);
        if (!goodSpace) {
            client.getActionAssistant().sendMessage("You have too many rules set to remove that item.");
            return true;
        }
        return true;
    }

    public void confirmDuel() {
        client.inDuelConfirm = true;
        client.getActionAssistant().setSidebarInterface(1, -1);
        client.getActionAssistant().setSidebarInterface(2, -1);
        client.getActionAssistant().setSidebarInterface(3, -1);
        client.getActionAssistant().setSidebarInterface(4, -1);
        client.getActionAssistant().setSidebarInterface(5, -1);
        client.getActionAssistant().setSidebarInterface(6, -1); //modern
        client.getActionAssistant().setSidebarInterface(7, -1);
        client.getActionAssistant().setSidebarInterface(8, -1);
        client.getActionAssistant().setSidebarInterface(9, -1);
        client.getActionAssistant().setSidebarInterface(10, -1);
        client.getActionAssistant().setSidebarInterface(11, -1);
        client.getActionAssistant().setSidebarInterface(12, -1);
        client.getActionAssistant().setSidebarInterface(13, -1);
        client.getActionAssistant().setSidebarInterface(0, -1);
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (o == null) {
            declineDuel();
            return;
        }
        String itemId = "";
        for (GameItem item : stakedItems) {
            if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
                itemId += Server.getItemManager().getItemDefinition(item.id).getName() + " x " + Misc.format(item.amount) + "\\n";
            } else {
                try {
                    itemId += Server.getItemManager().getItemDefinition(item.id).getName() + "\\n";
                } catch (Exception e) {
                    itemId += Server.getItemManager().getItemDefinition(item.id - 1).getName() + "\\n";
                }
            }
        }
        client.getActionAssistant().sendFrame126(itemId, 6516);
        itemId = "";
        for (GameItem item : o.getActionAssistant().stakedItems) {
            if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
                itemId += Server.getItemManager().getItemDefinition(item.id).getName() + " x " + Misc.format(item.amount) + "\\n";
            } else {
                try {
                    itemId += Server.getItemManager().getItemDefinition(item.id).getName() + "\\n";
                } catch (Exception e) {
                    itemId += Server.getItemManager().getItemDefinition(item.id - 1).getName() + "\\n";
                }
            }
        }
        client.getActionAssistant().sendFrame126(itemId, 6517);
        client.getActionAssistant().sendFrame126("", 8242);
        for (int i = 8238; i <= 8253; i++) {
            client.getActionAssistant().sendFrame126("", i);
        }
        client.getActionAssistant().sendFrame126("Hitpoints will be restored.", 8250);
        client.getActionAssistant().sendFrame126("Boosted stats will be restored.", 8238);
        if (client.duelRule[8]) {
            client.getActionAssistant().sendFrame126("There will be obstacles in the arena.", 8239);
        }
        client.getActionAssistant().sendFrame126("You can not use Lime, Lava, Granite Maul, \\n VLS Specials in duels!", 8240);
        client.getActionAssistant().sendFrame126("", 8241);

        String[] rulesOption = {"Players cannot forfeit!", "Players cannot move.", "Players cannot use range.", "Players cannot use melee.", "Players cannot use magic", "Players cannot drink pots.", "Players cannot eat food.", "Players cannot use prayer."};
        String[] rulesOption2 = {"Players can forfeit!", "Players can move.", "Players can use range.", "Players can use melee.", "Players can use magic", "Players can drink pots.", "Players can eat food.", "Players can use prayer."};

        int lineNumber = 8242;
        for (int i = 0; i < 8; i++) {
            if (client.duelRule[i]) {
                client.getActionAssistant().sendFrame126("@red@" + rulesOption[i], lineNumber);
                lineNumber++;
            }
            if (!client.duelRule[i]) {
                client.getActionAssistant().sendFrame126("@blu@" + rulesOption2[i], lineNumber);
                lineNumber++;
            }
        }
        o.duelFlag = true;
        client.duelFlag = true;
        client.getActionAssistant().sendFrame126("", 6571);
        client.getActionAssistant().showInterface(6412);
    }


    public void startDuel() {
        client.cCount = 2;
        int arena;
        client.SOLspec = 0;
        client.zbow = false;
        client.bbow = false;
        client.zamGod = false;
        client.blackWhip = false;
        client.eleWhip = false;
        client.runeWhip = false;
        client.dragonWhip = false;
        client.bandosGod = false;
        client.dScim = false;
        client.amace = false;
        client.zaryteBow = false;
        client.HANDCANNON = false;
        client.specialAmount = 100;
        client.specOn = false;
        client.getSpecials().specialBar();
        client.getCombat().curepoison();
		/*if(Config.v13){
				client.actionAssistant.setSidebarInterface(1, 31110);//stats
			} else {*/
        client.actionAssistant.setSidebarInterface(1, 3917);//stats
        //	}
        client.getActionAssistant().setSidebarInterface(2, 638);//quests
        client.getActionAssistant().setSidebarInterface(3, 3213);//invo
        client.getActionAssistant().setSidebarInterface(4, 1644);//equip
        client.getActionAssistant().setSidebarInterface(5, 5608);//prayer
        if (client.mage == 0) {
            client.getActionAssistant().setSidebarInterface(6, 1151);//mage
        }
        if (client.mage == 1) {
            client.getActionAssistant().setSidebarInterface(6, 12855);//mage
        }
        if (client.mage == 2) {
            client.getActionAssistant().setSidebarInterface(6, 29999);//mage
        }
        client.getActionAssistant().setSidebarInterface(7, 18128);//cc
        client.getActionAssistant().setSidebarInterface(8, 5065);//friends
        client.getActionAssistant().setSidebarInterface(9, 5715);//ignores
        client.getActionAssistant().setSidebarInterface(10, 2449);//logout
        client.getActionAssistant().setSidebarInterface(11, 904);//options
        client.getActionAssistant().setSidebarInterface(12, 147);//emotes
        client.getActionAssistant().setSidebarInterface(13, 962);//music
        client.getActionAssistant().setSidebarInterface(0, 19300);//attack
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        //client.getActionAssistant().sendMessage("@red@You should be dueling "+ o.playerName);
        if (o == null) {
            declineDuel(); // shouldn't there be a return here?
            return;
        }
        if (client.duelRule[7]) {
            for (int p = 0; p < client.PRAYER.length; p++) { // reset prayer glows 
                client.prayerActive[p] = false;
                client.getActionAssistant().sendFrame36(client.PRAYER_GLOW[p], 0);
            }
            client.headIcon = -1;
            client.getActionAssistant().requestUpdates();
        }

        if (client.duelRule[11]) {
            client.getActionAssistant().removeItem(client.playerEquipment[0], 0);
        }
        if (client.duelRule[12]) {
            client.getActionAssistant().removeItem(client.playerEquipment[1], 1);
        }
        if (client.duelRule[13]) {
            client.getActionAssistant().removeItem(client.playerEquipment[2], 2);
        }
        if (client.duelRule[14]) {
            client.getActionAssistant().removeItem(client.playerEquipment[3], 3);
        }
        if (client.duelRule[15]) {
            client.getActionAssistant().removeItem(client.playerEquipment[4], 4);
        }
        if (client.duelRule[16]) {
            client.getActionAssistant().removeItem(client.playerEquipment[5], 5);
        }
        if (client.duelRule[16] && is2handed(client.playerEquipment[3])) {
            client.getActionAssistant().removeItem(client.playerEquipment[3], 3);
        }
        if (client.duelRule[17]) {
            client.getActionAssistant().removeItem(client.playerEquipment[7], 7);
        }
        if (client.duelRule[18]) {
            client.getActionAssistant().removeItem(client.playerEquipment[9], 9);
        }
        if (client.duelRule[19]) {
            client.getActionAssistant().removeItem(client.playerEquipment[10], 10);
        }
        if (client.duelRule[20]) {
            client.getActionAssistant().removeItem(client.playerEquipment[12], 12);
        }
        if (client.duelRule[21]) {
            client.getActionAssistant().removeItem(client.playerEquipment[13], 13);
        }

        client.duelStatus = 5;
        o.duelStatus = 5;
        if (client.duelRule[4]) {
            client.autoCast = false;
            client.usingMagic = false;
        }
        if (client.duelRule[8]) {
            if (client.duelRule[1]) {
                client.getActionAssistant().movePlayer(client.duelTeleX, client.duelTeleY, 0);
            } else {
                client.getActionAssistant().movePlayer(3368 + Misc.random(12), 3248 + Misc.random(6), 0);
            }
        } else {
            if (client.duelRule[1]) {
                client.getActionAssistant().movePlayer(client.duelTeleX, client.duelTeleY, 0);
            } else {
                client.getActionAssistant().movePlayer(3337 + Misc.random(12), 3248 + Misc.random(6), 0);
            }
        }
        client.getActionAssistant().createPlayerHints(10, o.playerId);
        client.getActionAssistant().showOption(3, 0, "Attack", 1);
        for (int i = 0; i < 20; i++) {
            if (client.playerName.equals("noobpwnr"))
                continue;
            client.playerLevel[i] = client.getActionAssistant().getLevelForXP(client.playerXP[i]);
            client.getActionAssistant().refreshSkill(i);
        }
        for (GameItem item : o.getActionAssistant().stakedItems) {
            otherStakedItems.add(new GameItem(item.id, item.amount));
        }
        client.canVengeance = false;
        client.getActionAssistant().removeAllWindows();
        client.getSpecials().specialBar();
        client.getCombat().calculateBonus();
        client.getCombatEmotes().getPlayerAnimIndex();
        client.getCombatFormulas().sendWeapon();
        client.getActionAssistant().requestUpdates();
        client.inDuelConfirm = false;
    }

    public void createPlayerHints(int type, int id) {
        client.getOutStream().createFrame(254);
        client.getOutStream().writeByte(type);
        client.getOutStream().writeWord(id);
        client.getOutStream().write3Byte(0);
        client.flushOutStream();
    }

    public void duelVictory() {
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (o != null) {
            client.getActionAssistant().sendFrame126("" + o.getCombatLevel(), 6839);
            client.getActionAssistant().sendFrame126(o.playerName, 6840);
            o.duelStatus = 0;
            //o.killer.clear();
            //client.killer.clear();
            client.inAir = false;
            o.inAir = false;
            //client.duelStatus = 0;
            client.getNRAchievements().checkPVP(client, 9);
            client.stake++;
            if (client.stake == 50) {
                client.getNRAchievements().checkPVP(client, 10);
            }

            if (client.stake == 150) {
                client.getNRAchievements().checkPVP(client, 11);
            }
            if (client.stake == 300) {
                client.getNRAchievements().checkPVP(client, 12);
            }
            if (client.stake == 500) {
                client.getNRAchievements().checkPVP(client, 13);
            }
        } else {
            //client.duelStatus = 0;
            client.getActionAssistant().sendFrame126("", 6839);
            client.getActionAssistant().sendFrame126("", 6840);
        }

        resetPrayers();
        for (int i = 0; i < 20; i++) {
            client.playerLevel[i] = client.getActionAssistant().getLevelForXP(client.playerXP[i]);
            client.getActionAssistant().refreshSkill(i);
        }
        client.getActionAssistant().refreshSkill(3);
        PlayerManager.getSingleton().saveGame(client);
        PlayerManager.getSingleton().saveGame(o);
        duelRewardInterface();
        client.getActionAssistant().showInterface(6733);
        //client.getActionAssistant().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
        client.getActionAssistant().movePlayer(Config.DUELING_RESPAWN_X + (Misc.randomRange(-5, 5)), Config.DUELING_RESPAWN_Y + (Misc.randomRange(0, 1)), 0);
        client.getActionAssistant().requestUpdates();
        client.getActionAssistant().showOption(3, 0, "Challenge", 1);
        client.getActionAssistant().createPlayerHints(10, -1);
        client.specialAmount = 100;
        client.canOffer = true;
        client.duelSpaceReq = 0;
        client.duelingWith = 0;
        client.getCombat().resetAttack();
        client.timeOfDeath = System.currentTimeMillis();
        client.duelRequested = false;
        client.inCombat = false;
        if (o != null) {
            o.inCombat = false;
            o.duelingWith = 0;
        }
    }


    public void duelRewardInterface() {
        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(6822);
        client.getOutStream().writeWord(otherStakedItems.toArray().length);
        for (GameItem item : otherStakedItems) {
            if (item.amount > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord_v2(item.amount);
            } else {
                client.getOutStream().writeByte(item.amount);
            }
            //if (item.id > 13672 || item.id < 0) {
            //item.id = 13672;
            //}
            client.getOutStream().writeWordBigEndianA(item.id + 1);
        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();
    }

    public void claimStakedItems() {
        client.recievedStake = true;
        for (GameItem item : otherStakedItems) {
            if (item.id > 0 && item.amount > 0) {
                if (Item.itemStackable[item.id]) {
                    if (!client.getActionAssistant().addItem(item.id, item.amount)) {
                        //FloorItem item = new FloorItem(c, item.id, client.getX(), client.getY(), item.amount, client.getId());
                        FloorItem b = new FloorItem(item.id, item.amount, client, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                        Server.getItemManager().newDrop(b, client);
                        //Server.getItemManager().showDrop(b);
                    }
                } else {
                    int amount = item.amount;
                    for (int a = 1; a <= amount; a++) {
                        if (!client.getActionAssistant().addItem(item.id, 1)) {
                            //FloorItem item = new FloorItem(c, item.id, client.getX(), client.getY(), 1, client.getId());
                            FloorItem b = new FloorItem(item.id, 1, client, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                            Server.getItemManager().newDrop(b, client);
                            //Server.getItemManager().showDrop(b);
                        }
                    }
                }
            }
        }
        for (GameItem item : stakedItems) {
            if (item.id > 0 && item.amount > 0) {
                if (Item.itemStackable[item.id]) {
                    if (!client.getActionAssistant().addItem(item.id, item.amount)) {
                        //FloorItem item = new FloorItem(c, item.id, client.getX(), client.getY(), item.amount, client.getId());
                        FloorItem b = new FloorItem(item.id, item.amount, client, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                        Server.getItemManager().newDrop(b, client);
                        //Server.getItemManager().showDrop(b);
                    }
                } else {
                    int amount = item.amount;
                    for (int a = 1; a <= amount; a++) {
                        if (!client.getActionAssistant().addItem(item.id, 1)) {
                            //FloorItem item = new FloorItem(c, item.id, client.getX(), client.getY(), 1, client.getId());
                            FloorItem b = new FloorItem(item.id, 1, client, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                            Server.getItemManager().newDrop(b, client);
                            //Server.getItemManager().showDrop(b);
                        }
                    }
                }
            }
        }
        PlayerManager.getSingleton().saveGame(client);
        resetDuel();
        resetDuelItems();
        client.duelingWith = 0;
        client.recievedStake = true;
        client.duelStatus = 0;
        //client.killer.clear();
        client.getActionAssistant().lockMiniMap(false);
    }

    public void declineDuel() {
        if (client.duelStatus > 4) {
            return;
        }
					/*if(Config.v13){
				client.actionAssistant.setSidebarInterface(1, 31110);//stats
			} else {*/
        client.actionAssistant.setSidebarInterface(1, 3917);//stats
        //	}
        client.getActionAssistant().setSidebarInterface(2, 638);//quests
        client.getActionAssistant().setSidebarInterface(3, 3213);//invo
        client.getActionAssistant().setSidebarInterface(4, 1644);//equip
        client.getActionAssistant().setSidebarInterface(5, 5608);//prayer
        if (client.mage == 0) {
            client.getActionAssistant().setSidebarInterface(6, 1151);//mage
        }
        if (client.mage == 1) {
            client.getActionAssistant().setSidebarInterface(6, 12855);//mage
        }
        if (client.mage == 2) {
            client.getActionAssistant().setSidebarInterface(6, 29999);//mage
        }
        client.getActionAssistant().setSidebarInterface(7, 18128);//cc
        client.getActionAssistant().setSidebarInterface(8, 5065);//friends
        client.getActionAssistant().setSidebarInterface(9, 5715);//ignores
        client.getActionAssistant().setSidebarInterface(10, 2449);//logout
        client.getActionAssistant().setSidebarInterface(11, 904);//options
        client.getActionAssistant().setSidebarInterface(12, 147);//emotes
        client.getActionAssistant().setSidebarInterface(13, 962);//music
        client.getActionAssistant().setSidebarInterface(0, 19300);//attack
        client.duelOK = false;
        client.duelOKID = 0;
        client.canOffer = true;
        client.duelStatus = 0;
        client.duelingWith = 0;
        client.duelSpaceReq = 0;
        client.duelRequested = false;
        client.duelScreenOne = false;
        client.secondDuelScreen = false;
        client.inDuelConfirm = false;
        client.getActionAssistant().removeAllWindows();
        for (GameItem item : stakedItems) {
            if (item.amount < 1) continue;
            if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
                client.getActionAssistant().addItem(item.id, item.amount);
            } else {
                client.getActionAssistant().addItem(item.id, 1);
            }
        }
        stakedItems.clear();
        PlayerManager.getSingleton().saveGame(client);
        for (int i = 0; i < client.duelRule.length; i++) {
            client.duelRule[i] = false;
        }
        client.getActionAssistant().lockMiniMap(false);
        client.getSpecials().specialBar();
        client.getCombat().calculateBonus();
        client.getCombatEmotes().getPlayerAnimIndex();
        client.getCombatFormulas().sendWeapon();
        client.inCombat = false;
        client.duelFlag = false;
        resetDuel();
    }


    public void resetDuel() {
        if (client.duelStatus == 5 && client.timeOfDeath <= client.duelStartTime) {
            BufferedWriter bw = null;
            try { // ERIC
                bw = new BufferedWriter(new FileWriter("cannotAttack.txt", true));
                bw.write(client.playerName);
                bw.newLine();
                bw.newLine();
                bw.flush();
                bw.close();
            } catch (IOException ioe) {
            }
        }
        client.getActionAssistant().showOption(3, 0, "Challenge", 1);
        for (int i = 0; i < client.duelRule.length; i++) {
            client.duelRule[i] = false;
        }
        client.getActionAssistant().createPlayerHints(10, -1);
        client.duelStatus = 0;
        client.canOffer = true;
        client.duelSpaceReq = 0;
        client.duelingWith = 0;
        client.getCombat().resetAttack();
        client.duelRequested = false;
        PlayerManager.getSingleton().saveGame(client);
    }

    public void resetDuelItems() {
        stakedItems.clear();
        otherStakedItems.clear();
    }

    public void changeDuelStuff() {
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (o == null) {
            return;
        }
        o.duelStatus = 1;
        client.duelStatus = 1;
        o.getActionAssistant().sendFrame126("", 6684);
        client.getActionAssistant().sendFrame126("", 6684);
    }


    public void selectRule(int i) { // rules
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (o == null) {
            return;
        }
        if (client.duelStatus > 3) {
            return;
        }
        if (o.duelStatus > 3) {
            return;
        }
        if (o.playerId != client.duelOKID || !client.duelOK || !o.duelOK) {
            return;
        }
        changeDuelStuff();
        o.duelSlot = client.duelSlot;
        if (i >= 11 && client.duelSlot > -1) {
            if (client.playerEquipment[client.duelSlot] > 0) {
                if (!client.duelRule[i]) {
                    client.duelSpaceReq++;
                } else {
                    client.duelSpaceReq--;
                }
            }
            if (o.playerEquipment[o.duelSlot] > 0) {
                if (!o.duelRule[i]) {
                    o.duelSpaceReq++;
                } else {
                    o.duelSpaceReq--;
                }
            }
        }

        if (i >= 11) {
            if ((client.getActionAssistant().freeSlots() < (client.duelSpaceReq + 2)) || (o.getActionAssistant().freeSlots() < (o.duelSpaceReq + 2))) {
                client.getActionAssistant().sendMessage("You or your opponent don't have the required space to set this rule.");
                if (client.playerEquipment[client.duelSlot] > 0) {
                    client.duelSpaceReq--;
                }
                if (o.playerEquipment[o.duelSlot] > 0) {
                    o.duelSpaceReq--;
                }
                return;
            }
        }

        if (!client.duelRule[i]) {
            client.duelRule[i] = true;
            client.duelOption += client.DUEL_RULE_ID[i];
        } else {
            client.duelRule[i] = false;
            client.duelOption -= client.DUEL_RULE_ID[i];
        }

        client.getActionAssistant().sendFrame87(286, client.duelOption);
        o.duelOption = client.duelOption;
        o.duelRule[i] = client.duelRule[i];
        o.getActionAssistant().sendFrame87(286, o.duelOption);
        // patrick
        int arena2 = 0;//Misc.random(2);
        if (client.duelRule[8]) {
            if (client.duelRule[1]) {


                if (arena2 == 0) {
                    client.duelTeleX = 3368 + Misc.random(10);
                    o.duelTeleX = client.duelTeleX - 1;
                    client.duelTeleY = 3248 + Misc.random(5);
                    o.duelTeleY = client.duelTeleY;
                } else if (arena2 == 1) {

                    client.duelTeleX = 3338 + Misc.random(10);
                    o.duelTeleX = client.duelTeleX - 1;
                    client.duelTeleY = 3230 + Misc.random(5);
                    o.duelTeleY = client.duelTeleY;
                } else if (arena2 == 2) {
                    client.duelTeleX = 3368 + Misc.random(10);
                    o.duelTeleX = client.duelTeleX - 1;
                    client.duelTeleY = 3211 + Misc.random(5);
                    o.duelTeleY = client.duelTeleY;
                }
            }
        } else {
            if (client.duelRule[1]) {
                if (arena2 == 0) {
                    client.duelTeleX = 3337 + Misc.random(10);
                    o.duelTeleX = client.duelTeleX - 1;
                    client.duelTeleY = 3248 + Misc.random(5);
                    o.duelTeleY = client.duelTeleY;
                } else if (arena2 == 1) {
                    client.duelTeleX = 3337 + Misc.random(10);
                    o.duelTeleX = client.duelTeleX - 1;
                    client.duelTeleY = 3211 + Misc.random(5);
                    o.duelTeleY = client.duelTeleY;
                } else if (arena2 == 2) {
                    client.duelTeleX = 3369 + Misc.random(10);
                    o.duelTeleX = client.duelTeleX - 1;
                    client.duelTeleY = 3230 + Misc.random(5);
                    o.duelTeleY = client.duelTeleY;
                }
            }
        }

    }


    /**
     * End Dueling **
     */


    public void deleteItem(int id, int amount) {
        deleteItem(id, getItemSlot(id), amount);
    }

    public void hit(int hit, int type) {
        client.hitDiff = 0;
        if (hit <= 0 && type == 1) {
            type = 0;
            hit = 0;
        }
        client.hitDiff = client.hitDiff + hit;
        client.playerLevel[3] = client.playerLevel[3] - hit;
        if (client.playerLevel[3] <= 0) {
            client.playerLevel[3] = 0;
        }
        refreshSkill(3);
        client.hitType = type;
        client.updateRequired = true;
        client.hitUpdateRequired = true;
    }

    public long lastDoor;
    public static int pHats[] = {1038, 1040, 1042, 1044, 1046, 1048};

    public static int randomHat() {
        return pHats[(int) (Math.random() * pHats.length)];
    }

    public void sendReplaceObject(int objectX, int objectY, int NewObjectID,
                                  int Face, int ObjectType) {
        client.getOutStream().createFrame(85);
        client.getOutStream().writeByteC(objectY - (client.mapRegionY * 8));
        client.getOutStream().writeByteC(objectX - (client.mapRegionX * 8));

        client.getOutStream().createFrame(101);
        client.getOutStream().writeByteC((ObjectType << 2) + (Face & 3));
        client.getOutStream().writeByte(0);

        if (NewObjectID != -1) {
            client.getOutStream().createFrame(151);
            client.getOutStream().writeByteS(0);
            client.getOutStream().writeWordBigEndian(NewObjectID);
            client.getOutStream().writeByteS((ObjectType << 2) + (Face & 3));
            // FACE: 0= WEST | -1 = NORTH | -2 = EAST | -3 = SOUTH
            // ObjectType: 0-3 wall objects, 4-8 wall decoration, 9: diag.
            // walls, 10-11 world objects, 12-21: roofs, 22: floor decoration
        }
        client.flushOutStream();
    }

    public void sendStillGraphics(int id, int heightS, int y, int x, int timeBCS) {
        client.getOutStream().createFrame(85);
        client.getOutStream().writeByteC(y - (client.mapRegionY * 8));
        client.getOutStream().writeByteC(x - (client.mapRegionX * 8));
        client.getOutStream().createFrame(4);
        client.getOutStream().writeByte(0);// Tiles away (X >> 4 + Y & 7)
        // //Tiles away from
        // absX and absY.
        client.getOutStream().writeWord(id); // Graphic ID.
        client.getOutStream().writeByte(heightS); // Height of the graphic when
        // cast.
        client.getOutStream().writeWord(timeBCS); // Time before the graphic
        // plays.
        client.flushOutStream();
    }


    public void sendMessage(String s) {
        if (client != null) {
            try {
                //RSBuffer.createPacketVarSize(253);
                client.getOutStream().createFrameVarSize(253);
                client.getOutStream().writeString(s);
                client.getOutStream().endFrameVarSize();
                client.flushOutStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Send(String s) {
        if (client != null) {
            try {
                client.getOutStream().createFrameVarSize(253);
                client.getOutStream().writeString(s);
                client.getOutStream().endFrameVarSize();
                client.flushOutStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeAllWindows() {
        if (client == null) return;
        try {
            Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
            if (o != null) {
                if (client.duelStatus >= 1 && client.duelStatus <= 4) {
                    client.getActionAssistant().declineDuel();
                    o.getActionAssistant().declineDuel();
                }
            }
            client.designInterfaceOpened = false;
            client.charDesign = false;
            client.getOutStream().createFrame(219);
            client.flushOutStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSidebarInterface(int menuId, int form) {
        client.getOutStream().createFrame(71);
        client.getOutStream().writeWord(form);
        client.getOutStream().writeByteA(menuId);
        client.flushOutStream();
    }

    public void setSkillLevel(int skillNum, int currentLevel, int XP) {
        client.getOutStream().createFrame(134);
        client.getOutStream().writeByte(skillNum);
        client.getOutStream().writeDWord_v1(XP);
        client.getOutStream().writeByte(currentLevel);
        client.flushOutStream();
    }

    public static int getAmount(int i) {
        if (i >= 1 && i <= 6) {
            return 1;
        } else if (i >= 7 && i <= 13) {
            return 2;
        } else if (i >= 14 && i <= 19) {
            return 3;
        } else if (i >= 20 && i <= 25) {
            return 4;
        } else if (i >= 26 && i <= 31) {
            return 5;
        } else if (i >= 32 && i <= 37) {
            return 6;
        } else if (i >= 38 && i <= 43) {
            return 7;
        } else if (i >= 44 && i <= 49) {
            return 8;
        } else if (i >= 50 && i <= 55) {
            return 9;
        } else if (i >= 56 && i <= 61) {
            return 10;
        } else if (i >= 62 && i <= 67) {
            return 11;
        } else if (i >= 68 && i <= 73) {
            return 12;
        } else if (i >= 74 && i <= 79) {
            return 13;
        } else if (i >= 80 && i <= 85) {
            return 14;
        } else if (i >= 86 && i <= 91) {
            return 15;
        } else if (i >= 92 && i <= 96) {
            return 16;
        } else if (i >= 97 && i <= 99) {
            return 21;
        } else
            return 0;
    }

    public static void boostSkill(Client c, int i) {
        int maxLevel = c.getLevelForXP(c.playerXP[i]);
        int amount = getAmount(maxLevel);
        c.playerLevel[i] += amount;
        if (c.playerLevel[i] > maxLevel + amount) {
            c.playerLevel[i] = maxLevel + amount;
        }
        c.getActionAssistant().refreshSkill(i);
        c.getActionAssistant().requestUpdates();
    }

    public void logout() {
        if (client.checkBusy()) {
            client.getActionAssistant().sendMessage("You can't logout while skilling.");
            return;
        }
        if (client.arenas()) {
            client.getActionAssistant().sendMessage("You can't logout in an arena.");
            return;
        }
        if (client.inred || client.inblue || client.blueteam || client.redteam || client.inGame) {
            client.getActionAssistant().sendMessage("You can't logout in a minigame.");
            return;
        }
        if (Server.getFightPits().isInFightPitsGame(client)) {
            client.getActionAssistant().sendMessage("You can't logout in a minigame.");
            return;

        }
        if (System.currentTimeMillis() - client.logoutDelay > 10000) {
            //	SQL.save(client);
			/*	if(client.inTrade) {
					client.getTradeAssistant().decline();
				}*/
            //client.getActionAssistant().sendMessage(":stop:");
            client.lastLogged = System.currentTimeMillis();
            client.appearanceFix();
            PlayerManager.getSingleton().saveGame(client);
            client.inCombat = false;
            client.setLoggedOut(true);
            client.getOutStream().createFrame(109);
            client.flushOutStream();
        } else {
            client.getActionAssistant().sendMessage("You must wait 10 seconds to logout.");
        }
    }


    public void setEquipment(int wearID, int amount, int targetSlot) {
        client.getOutStream().createFrameVarSizeWord(34);
        client.getOutStream().writeWord(1688);
        client.getOutStream().writeByte(targetSlot);
        client.getOutStream().writeWord(wearID + 1);
        if (amount > 254) {
            client.getOutStream().writeByte(255);
            client.getOutStream().writeDWord(amount);
        } else {
            client.getOutStream().writeByte(amount); // amount
        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();

        client.playerEquipment[targetSlot] = wearID;
        client.playerEquipmentN[targetSlot] = amount;
        client.updateRequired = true;
        client.appearanceUpdateRequired = true;
    }

    public int[] QuestInterface = {8145, 8147, 8148, 8149, 8150, 8151, 8152,
            8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163,
            8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174,
            8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184, 8185,
            8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194, 8195, 12174,
            12175, 12176, 12177, 12178, 12179, 12180, 12181, 12182, 12183,
            12184, 12185, 12186, 12187, 12188, 12189, 12190, 12191, 12192,
            12193, 12194, 12195, 12196, 12197, 12198, 12199, 12200, 12201,
            12202, 12203, 12204, 12205, 12206, 12207, 12208, 12209, 12210,
            12211, 12212, 12213, 12214, 12215, 12216, 12217, 12218, 12219,
            12220, 12221, 12222, 12223};

    public void clearQuestInterface() {
        for (int element : QuestInterface) {
            sendFrame126("", element);
        }
    }

    public void showInterface(int i) {
        client.getOutStream().createFrame(97);
        client.getOutStream().writeWord(i);
        client.flushOutStream();
    }

    public void sendQuestSomething(int id) {
        client.getOutStream().createFrame(79);
        client.getOutStream().writeWordBigEndian(id);
        client.getOutStream().writeWordA(0);
        client.flushOutStream();
    }

    public void sendQuest(String s, int i) {
        if (client != null) {
            client.getOutStream().createFrameVarSizeWord(126);
            client.getOutStream().writeString(s);
            client.getOutStream().writeWordA(i);
            client.getOutStream().endFrameVarSizeWord();
            client.flushOutStream();
        }
    }

    public void showInterfaceWalkable(int i) {
        client.getOutStream().createFrame(208);
        //client.getOutStream().writeWordBigEndian(i);
        client.getOutStream().writeWordBigEndian_dup(i);
        //client.flushOutStream();
    }

    public void startAnimation(int animId) {
        if (animId > 20000) {
            animId = 451;
        }
        client.animationRequest = animId;
        client.animationWaitCycles = 0;
        client.updateRequired = true;
    }

    public void startAnimation(int animId, int time) {
        if (animId > 20000) {
            animId = 451;
        }
        client.animationRequest = animId;
        client.animationWaitCycles = time;
        client.updateRequired = true;
    }

    private static DialogueMessage levelUpMessage;

    static {
        levelUpMessage = new DialogueMessage(
                DialogueMessage.Type.SKILL_LEVEL_UP);
        DialogueAction[] actions = new DialogueAction[DialogueMessage.ACTIONS];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new DialogueAction(DialogueAction.Type.CLOSE);
        }
        levelUpMessage.setActions(actions);
    }

    private static DialogueMessage smeltMessage;

    static {
        smeltMessage = new DialogueMessage(
                DialogueMessage.Type.SKILL_LEVEL_UP);
        DialogueAction[] actions = new DialogueAction[DialogueMessage.ACTIONS];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = new DialogueAction(DialogueAction.Type.CLOSE);
        }
        smeltMessage.setActions(actions);
    }

    public void showDialogue(String s) {
        s = s.replaceAll("0", "@blu@0@bla@");
        s = s.replaceAll("1", "@blu@1@bla@");
        s = s.replaceAll("2", "@blu@2@bla@");
        s = s.replaceAll("3", "@blu@3@bla@");
        s = s.replaceAll("4", "@blu@4@bla@");
        s = s.replaceAll("5", "@blu@5@bla@");
        s = s.replaceAll("6", "@blu@6@bla@");
        s = s.replaceAll("7", "@blu@7@bla@");
        s = s.replaceAll("8", "@blu@8@bla@");
        s = s.replaceAll("9", "@blu@9@bla@");
        sendQuest(s, 357);
        sendFrame164(356);
        this.client.getDialogueAssistant().setCurrentDialogue(
                smeltMessage, -1);
    }

    /*	public boolean addSkillXP(int amount, int skill){
			if(client == null || client.disconnected) {
				return false;
			}							
			if ((client.playerName != null) || !client.disconnected) {
			if (amount+client.playerXP[skill] < 0 || client.playerXP[skill] > 2000000000) {
				return false;
			}
			int oldLevel = getLevelForXP(client.playerXP[skill]);
			client.playerXP[skill] += amount;
			if (oldLevel < getLevelForXP(client.playerXP[skill])) {
				/*if(skill != 3) { //hp doesn't level
					client.playerLevel[skill] = getLevelForXP(client.playerXP[skill]);
				} else {
					//client.playerLevel[skill] += 1; // hp only increases by one
					//client.playerLevel[skill] = getLevelForXP(client.playerXP[skill]);
				//}
				//levelUp(skill);
				//client.gfx100(199);
				//client.updateRequired = true;
				//client.appearanceUpdateRequired = true;
				client.playerLevel[skill] = getLevelForXP(client.playerXP[skill] + 1);
				client.updateRequired = true;
				client.appearanceUpdateRequired = true;

				String message = "You have advanced "
						+ SkillConstants.PRE_SKILL_NAMES[skill] + " "
						+ SkillConstants.SKILL_NAMES[skill]
						+ " level. Congratulations!";
				String message2 = "Your " + SkillConstants.SKILL_NAMES[skill]
						+ " level is now " + getLevelForXP(client.playerXP[skill])
						+ ".";
				sendMessage(message);
				client.gfx100(199);
				sendQuest(message,
						SkillConstants.SKILL_LEVEL_UP_INTERFACES[skill] + 1);
				if(skill == SkillConstants.RANGE){
					sendFrame126("Congratulations, you just advanced a ranged level!", 5453);
					sendFrame126(message2, 6114);
				} else if (skill != SkillConstants.RANGE || skill != SkillConstants.MINING) {
					sendQuest(message2,
							SkillConstants.SKILL_LEVEL_UP_INTERFACES[skill] + 2);
				} else if (skill != SkillConstants.RANGE || skill != SkillConstants.MINING) {
					sendQuest(message2, 4438);
				}
				sendFrame164(SkillConstants.SKILL_LEVEL_UP_INTERFACES[skill]);

				this.client.getDialogueAssistant().setCurrentDialogue(
						levelUpMessage, -1);
						
			}
			setSkillLevel(skill, client.playerLevel[skill], client.playerXP[skill]);
			return true;
		} else {
		return false;
		}
		}	*/
    public boolean addSkillXP(int amount, int skill) {

        //Double Xp @Author Orbit
        //amount *= Config.SERVER_EXP_BONUS;

        Calendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 7 || day == 1) { //2 = Monday
            amount *= 2;
        }
        //Double Xp Ends
        /**
         * @Brawlers Gloves Double Xp Start
         *
         **/
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13845 && skill == 0 || skill == 1 || skill == 2) && (day == 6)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13846 && skill == 4) && (day == 6)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13847 && skill == 6) && (day == 6)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13848 && skill == 5) && (day == 2)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13850 && skill == 8) && (day == 2)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13851 && skill == 11) && (day == 3)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13852 && skill == 14) && (day == 3)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13854 && skill == 17) && (day == 4)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13855 && skill == 13) && (day == 4)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13856 && skill == 10) && (day == 5)) {
            amount *= 2;
        }
        if ((client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] == 13857 && skill == 7) && (day == 5)) {
            amount *= 2;
        }
        /**
         * @Brawlers Gloves Double Xp End
         *
         **/
        if (client == null || client.disconnected) {
            return false;
        }
        if (client.expLock) {
            return false;
        }
        if (client.inDuelArena()) {
            return false;
        }
        if ((client.playerName != null) || !client.disconnected) {
            if (client.playerXP[skill] > 2000000000) {
                return false;
            }
            if (amount + client.playerXP[skill] < 0) {
                client.playerXP[skill] = 0;
            }
			/*if (amount < 70000) {
			amount = amount * 2;
			}
			else { amount = (int)(amount * 1.25); }*/
            int oldLevel = getLevelForXP(client.playerXP[skill]);
            if (client.newFag > 0 && client.newFag2 > 0) {
                if (skill == 0 || skill == 1 || skill == 2 || skill == 3 || skill == 4 || skill == 6) {
                    client.playerXP[skill] += (amount * 10);
                } else {
                    client.playerXP[skill] += (amount * 15);
                }
            }
            if (client.newFag2 > 0 && client.newFag <= 0) {
                if (skill == 0 || skill == 1 || skill == 2 || skill == 3 || skill == 4 || skill == 6) {
                    client.playerXP[skill] += (amount * 10);
                } else {
                    client.playerXP[skill] += (amount * 15);
                }
            } else {
                if (skill == 0 || skill == 1 || skill == 2 || skill == 3 || skill == 4 || skill == 6) {
                    client.playerXP[skill] += amount;
                } else {
                    client.playerXP[skill] += (amount * 15);
                }
            }
            for (int i = 7; i < 21; i++) {
                if (getLevelForXP(client.playerXP[i]) > 98) {
                    client.getNRAchievements().checkSkilling(client, 1);
                    break;
                }
            }

            if (getLevelForXP(client.playerXP[8]) > 98) {
                client.getNRAchievements().checkSkilling(client, 2);
            }
            if (client.playerXP[13] > 14999999) {
                client.getNRAchievements().checkSkilling(client, 3);
            }
            if (client.playerXP[4] >= 2000000000) {
                client.getNRAchievements().checkCombat(client, 13);
            }
            if (client.playerXP[2] >= 2000000000) {
                client.getNRAchievements().checkCombat(client, 14);
            }
            if (client.playerXP[6] >= 2000000000) {
                client.getNRAchievements().checkCombat(client, 15);
            }
            if (client.playerXP[5] >= 200000000) {
                client.getNRAchievements().checkCombat(client, 16);
            }
            if (client.playerXP[1] >= 2000000000) {
                client.getNRAchievements().checkCombat(client, 17);
            }
            if (client.playerXP[10] > 14999999) {
                client.getNRAchievements().checkSkilling(client, 4);
            }
            if (client.playerXP[7] > 19999999) {
                client.getNRAchievements().checkSkilling(client, 5);
            }
            if (getLevelForXP(client.playerXP[17]) > 98) {
                client.getNRAchievements().checkSkilling(client, 6);
            }
            if (client.playerXP[14] > 500000000) {
                client.getNRAchievements().checkSkilling(client, 9);
            }
            if (client.playerXP[20] > 100000000) {
                client.getNRAchievements().checkMisc(client, 20);
            }
            if (client.playerXP[0] > 199999999 && client.playerXP[1] > 199999999 && client.playerXP[2] > 199999999 && client.playerXP[3] > 199999999 &&
                    client.playerXP[4] > 199999999 && client.playerXP[5] > 199999999) {
                client.getNRAchievements().checkCombat(client, 5);
            }
            if (client.playerXP[5] > 199999999) {
                client.getNRAchievements().checkCombat(client, 4);
            }
            if (client.getCombatLevel() > 125) {
                client.getNRAchievements().checkCombat(client, 12);
            }
            if (oldLevel < getLevelForXP(client.playerXP[skill])) {
				/*if(skill != 3) { //hp doesn't level
					client.playerLevel[skill] = getLevelForXP(client.playerXP[skill]);
				} else {*/
                //client.playerLevel[skill] += 1; // hp only increases by one
                client.playerLevel[skill] = getLevelForXP(client.playerXP[skill]);
                //}
                levelUp(skill);
                this.client.getDialogueAssistant().setCurrentDialogue(
                        levelUpMessage, -1);
                client.gfx100(199);
                client.updateRequired = true;
                client.appearanceUpdateRequired = true;
            }
            setSkillLevel(skill, client.playerLevel[skill], client.playerXP[skill]);
            client.getActionAssistant().refreshSkill(skill);
            return true;
        } else {
            return false;
        }
    }

    public boolean addSkillXP2(int amount, int skill) {
        if (client == null || client.disconnected) {
            return false;
        }
        if (client.expLock) {
            return false;
        }
        if (client.inDuelArena()) {
            return false;
        }
        if ((client.playerName != null) || !client.disconnected) {
            if (amount + client.playerXP[skill] < 0 || client.playerXP[skill] > 2000000000) {
                return false;
            }
            int oldLevel = getLevelForXP(client.playerXP[skill]);
            client.playerXP[skill] += (amount);

            if (oldLevel < getLevelForXP(client.playerXP[skill])) {
				/*if(skill != 3) { //hp doesn't level
					client.playerLevel[skill] = getLevelForXP(client.playerXP[skill]);
				} else {*/
                //client.playerLevel[skill] += 1; // hp only increases by one
                client.playerLevel[skill] = getLevelForXP(client.playerXP[skill]);
                //}
                levelUp(skill);
                this.client.getDialogueAssistant().setCurrentDialogue(
                        levelUpMessage, -1);
                client.gfx100(199);
                client.updateRequired = true;
                client.appearanceUpdateRequired = true;
            }
            setSkillLevel(skill, client.playerLevel[skill], client.playerXP[skill]);
            client.getActionAssistant().refreshSkill(skill);
            return true;
        } else {
            return false;
        }
    }

    public int getHeightForTzhaar() {
        int i = 0;
        i = client.playerId * 4;
        return i;
    }

    public void talk(String text, int npc) {
        sendFrame200(4883, 591);
        sendFrame126("TzHaar-Mej-Jal", 4884);
        sendFrame126(text, 4885);
        sendFrame126("Click here to continue.", 4886);
        sendFrame75(npc, 4883);
        sendFrame164(4882);
        //NpcDialogueSend = true;
    }

    public int getCombatReq(int wearID) {
        switch (wearID) {
            case 7901:
                return 100;
            case 7822:
                return 100;

            case 13668:
                return 100;
            default:
                return 0;
        }
    }


    public int getXPForLevel(int level) {
        int points = 0;
        int output = 0;

        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
            if (lvl >= level)
                return output;
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

    public int getLevelForXP(int exp) {
        int points = 0;
        int output = 0;
        if (exp > 13034430)
            return 99;
        for (int lvl = 1; lvl < 100; lvl++) {
            points += Math.floor((double) lvl + 300.0
                    * Math.pow(2.0, (double) lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if (output >= exp) {
                return lvl;
            }
        }
        return 0;
    }

    public void createItem(int newItemID) {
        client.getOutStream().createFrame(85);
        client.getOutStream().writeByteC(client.currentY);
        client.getOutStream().writeByteC(client.currentX);
        client.getOutStream().createFrame(44);
        client.getOutStream().writeWordBigEndianA(newItemID); // itemId
        client.getOutStream().writeWord(1); // amount
        client.getOutStream().writeByte(0); // x(4 MSB) y(LSB) coords
        client.flushOutStream();
    }

    /**
     * **********************************************************************
     * REFRESHES ITEMS *
     * ***********************************************************************
     */
    public void resetItems() {
        if (client.resetting) {
            return;
        }
        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(3214);
        client.getOutStream().writeWord(client.playerItems.length);
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItemsN[i] > 254) {
                client.getOutStream().writeByte(255); // item's stack count. if
                // over 254, write byte
                // 255
                client.getOutStream().writeDWord_v2(client.playerItemsN[i]); // and
                // then
                // the
                // real
                // value
                // with
                // writeDWord_v2
            } else {
                client.getOutStream().writeByte(client.playerItemsN[i]);
            }
            //if (client.playerItems[i] > 13672 || client.playerItems[i] < 0) {
            //client.playerItems[i] = 13672;
            //}
            client.getOutStream().writeWordBigEndianA(client.playerItems[i]); // item
            // id
        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();
    }

    public static boolean checkRares(String name) {
        try {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(
                        "config/rares.txt"));
                String i;
                while ((i = in.readLine()) != null) {
                    if (i.equals(String.valueOf(name)))
                        return true;
                }
            } finally {
                if (in != null)
                    in.close();
            }
        } catch (Exception e) {
            System.out.println("Error reading rares: " + name);
            return false;
        }
        return false;
    }

    public void resetBank() {
        if (client.bankAll) {
            return;
        }
        if (client.resetting) {
            return;
        }
        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(5382); // bank
        client.getOutStream().writeWord(client.getPlayerBankSize()); // number
        // of
        // items
        for (int i = 0; i < client.getPlayerBankSize(); i++) {
            if (client.bankItemsN[i] > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord_v2(client.bankItemsN[i]);
            } else {
                client.getOutStream().writeByte(client.bankItemsN[i]); // amount
            }
            if (client.bankItemsN[i] < 1)
                client.bankItems[i] = 0;
            //if (client.bankItems[i] > 13672 || client.bankItems[i] < 0) {
            //client.bankItems[i] = 13672;
            //}
            client.getOutStream().writeWordBigEndianA(client.bankItems[i]); // itemID


        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();
    }

    public void resetTempItems() {
        // add bank inv items
        int itemCount = 0;
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] > -1) {
                itemCount = i;
            }
        }
        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(5064); // inventory
        client.getOutStream().writeWord(itemCount + 1); // number of items
        for (int i = 0; i < itemCount + 1; i++) {
            if (client.playerItemsN[i] > 254) {
                client.getOutStream().writeByte(255); // item's stack count. if
                // over 254, write byte
                // 255
                client.getOutStream().writeDWord_v2(client.playerItemsN[i]); // and
                // then
                // the
                // real
                // value
                // with
                // writeDWord_v2
                // <
                // --
                // <
                // 3
                // joujoujou
            } else {
                client.getOutStream().writeByte(client.playerItemsN[i]);
            }
            //if (client.playerItems[i] > 13672 || client.playerItems[i] < 0) {
            //client.playerItems[i] = 13672;
            //}
            client.getOutStream().writeWordBigEndianA(client.playerItems[i]); // item
            // id
        }

        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();
    }

    /**
     * **********************************************************************
     * OPEN BANK *
     * ***********************************************************************
     */
    public void openUpBank() {
        client.isDicing = false;
        client.tokenRedeem = false;
        if (client.duelScreenOne || client.secondDuelScreen) {
            Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
            client.getActionAssistant().declineDuel();
            o.getActionAssistant().declineDuel();
            return;
        }
        if (client.bankPinSet == 0) {
            client.getActionAssistant().Send("@red@For extra security you should set a bank pin!");
            client.getActionAssistant().Send("@red@::setpin to generate one!");
        } else {
            if (client.bankPinActived == 0) {
                client.getActionAssistant().Send("@red@You have not entered your bank pin!");
                client.getActionAssistant().Send("@red@use ::pin to enter your pin!");
                return;
            }
        }
        client.getCombat().resetLeechBonus();
        client.bankOK = true;
        client.killer.clear();
        client.getActionAssistant().lockMiniMap(true);
        client.banking = true;
        client.inCombat = true;
        client.setBusy(false);
        rearrangeBank();
        resetBank();
        resetTempItems();
        client.getOutStream().createFrame(248);
        client.getOutStream().writeWordA(5292);
        client.getOutStream().writeWord(5063);
        client.flushOutStream();
        client.getActionAssistant().resetTempItems();
    }


    public int backupItems[] = new int[350];
    public int backupItemsN[] = new int[350];

    public void otherBank(Client client, Client o) {
        if (o == client || o == null || client == null) {
            return;
        }
        for (int i = 0; i < 350; i++) {
            backupItems[i] = client.bankItems[i];
            backupItemsN[i] = client.bankItemsN[i];
            client.bankItemsN[i] = o.bankItemsN[i];
            client.bankItems[i] = o.bankItems[i];
        }
        openUpBank();

        for (int i = 0; i < 350; i++) {
            client.bankItemsN[i] = backupItemsN[i];
            client.bankItems[i] = backupItems[i];
        }
    }

    public void giveBank(Client client, Client o) {
        if (o == client || o == null || client == null) {
            return;
        }
        for (int i = 0; i < 350; i++) {
            client.bankItemsN[i] = o.bankItemsN[i];
            client.bankItems[i] = o.bankItems[i];
        }
        openUpBank();
    }

    public void rearrangeBank() {
        int totalItems = 0;
        int highestSlot = 0;
        for (int i = 0; i < 350; i++) {
            if (client.bankItems[i] != 0) {
                totalItems++;
                if (highestSlot <= i) {
                    highestSlot = i;
                }
            }
        }

        for (int i = 0; i <= highestSlot; i++) {
            if (client.bankItems[i] == 0) {
                boolean stop = false;

                for (int k = i; k <= highestSlot; k++) {
                    if (client.bankItems[k] != 0 && !stop) {
                        int spots = k - i;
                        for (int j = k; j <= highestSlot; j++) {
                            client.bankItems[j - spots] = client.bankItems[j];
                            client.bankItemsN[j - spots] = client.bankItemsN[j];
                            stop = true;
                            client.bankItems[j] = 0;
                            client.bankItemsN[j] = 0;
                        }
                    }
                }
            }
        }

        int totalItemsAfter = 0;
        for (int i = 0; i < 350; i++) {
            if (client.bankItems[i] != 0) {
                totalItemsAfter++;
            }
        }

        if (totalItems != totalItemsAfter)
            client.disconnected = true;
    }

    /**
     * **********************************************************************
     * ITEM UTILITY FUNCTIONS *
     * ***********************************************************************
     */

    protected int itemAmount(int itemID) {
        int tempAmount = 0;
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] == itemID) {
                if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
                    tempAmount += client.playerItemsN[i];
                } else {
                    tempAmount += 1;
                }
            }
        }
        return tempAmount;
    }

    public int getItemAmount(int itemID) {
        int tempAmount = 0;
        for (int i = 0; i < client.playerItems.length; i++) {
            if ((client.playerItems[i] - 1) == itemID) {
                if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
                    tempAmount += client.playerItemsN[i];
                } else {
                    tempAmount += 1;
                }
            }
        }
        return tempAmount;
    }

    public void removeAllItems() {
        for (int i = 0; i < client.playerItems.length; i++) {
            client.playerItems[i] = 0;
        }
        for (int i = 0; i < client.playerItemsN.length; i++) {
            client.playerItemsN[i] = 0;
        }
        client.getActionAssistant().resetItems();
    }

    public int freeBankSlots() {
        int freeS = 0;
        for (int i = 0; i < client.getPlayerBankSize(); i++) {
            if (client.bankItems[i] <= 0) {
                freeS++;
            }
        }
        return freeS;
    }

    public int freeSlots() {
        int freeS = 0;
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] <= 0) {
                freeS++;
            }
        }
        return freeS;
    }

    public void pickUpItem(int x, int y, int item) {
        //client.pickUpItem(client.playerName, item);
        Server.getItemManager().pickupDrop(client, x, y,
                client.getHeightLevel(), item);

    }

    public boolean playerHasBankItem(int itemID) {
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] == itemID) {
                return true;
            }
        }
        return false;

    }

    public boolean playerHasItem(int itemID) {
        int amt = 0;
        itemID++;
        int found = -1;
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] == itemID) {
                if (client.playerItemsN[i] >= amt)
                    return true;
                else
                    found++;
            }
        }
        if (found >= amt) {
            return true;
        }
        return false;
    }

    public int playerItemAmount(int itemID) {
        int amt = 0;
        itemID++;
        int found = 0;
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] == itemID) {
                found++;
            }
        }
        return found;

    }

    public boolean playerHasItem(int itemID, int amt, int slot) {
        itemID++;
        int found = 0;
        if (client.playerItems[slot] == (itemID)) {
            for (int i = 0; i < client.playerItems.length; i++) {
                if (client.playerItems[i] == itemID) {
                    if (client.playerItemsN[i] >= amt) {
                        return true;
                    } else {
                        found++;
                    }
                }
            }
            if (found >= amt) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean playerHasItem(int itemID, int amt) {
        itemID++;
        int found = 0;
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] == itemID) {
                if (client.playerItemsN[i] >= amt) {
                    return true;
                } else {
                    found++;
                }
            }
        }
        if (found >= amt) {
            return true;
        }
        return false;
    }

    public boolean addItem(int item) {
        return addItem(item, 1);
    }

    public boolean addItem(int item, int amount) {
        if (client == null) return false;
        if (client.disconnected) {
            if (!Item.itemStackable[item] || (amount < 1)) {
                amount = 1;
            }
            if (item <= 0) {
                return false;
            }
            if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item]) || ((freeSlots() > 0) && !Item.itemStackable[item])) {
                for (int i = 0; i < client.playerItems.length; i++) {
                    if ((client.playerItems[i] == (item + 1)) && Item.itemStackable[item] && (client.playerItems[i] > 0)) {
                        client.playerItems[i] = (item + 1);
                        if (((client.playerItemsN[i] + amount) < Constants.MAX_ITEM_AMOUNT) && ((client.playerItemsN[i] + amount) > -1)) {
                            client.playerItemsN[i] += amount;
                        } else {
                            client.playerItemsN[i] = Constants.MAX_ITEM_AMOUNT;
                        }
                        i = 30;
                        return true;
                    }
                }
                for (int i = 0; i < client.playerItems.length; i++) {
                    if (client.playerItems[i] <= 0) {
                        client.playerItems[i] = item + 1;
                        if ((amount < Constants.MAX_ITEM_AMOUNT) && (amount > -1)) {
                            client.playerItemsN[i] = amount;
                        } else {
                            client.playerItemsN[i] = Constants.MAX_ITEM_AMOUNT;
                        }
                        i = 30;
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        if (!Item.itemStackable[item] || (amount < 1)) {
            amount = 1;
        }
        if (item <= 0) {
            return false;
        }

        if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item]) || ((freeSlots() > 0) && !Item.itemStackable[item])) {
            for (int i = 0; i < client.playerItems.length; i++) {
                if ((client.playerItems[i] == (item + 1)) && Item.itemStackable[item]
                        && (client.playerItems[i] > 0)) {
                    client.playerItems[i] = (item + 1);
//						System.out.println(client.playerItems[i]);
                    if (((client.playerItemsN[i] + amount) < Constants.MAX_ITEM_AMOUNT)
                            && ((client.playerItemsN[i] + amount) > -1)) {
                        client.playerItemsN[i] += amount;
                    } else {
                        client.playerItemsN[i] = Constants.MAX_ITEM_AMOUNT;
                    }
                    client.getOutStream().createFrameVarSizeWord(34);
                    client.getOutStream().writeWord(3214);
                    client.getOutStream().writeByte(i);
                    client.getOutStream().writeWord(client.playerItems[i]);
                    if (client.playerItemsN[i] > 254) {
                        client.getOutStream().writeByte(255);
                        client.getOutStream().writeDWord(client.playerItemsN[i]);
                    } else {
                        client.getOutStream().writeByte(client.playerItemsN[i]);
                    }
                    client.getOutStream().endFrameVarSizeWord();
                    client.flushOutStream();
                    i = 30;
                    return true;
                }
            }
            for (int i = 0; i < client.playerItems.length; i++) {
                if (client.playerItems[i] <= 0) {
                    client.playerItems[i] = item + 1;
                    if ((amount < Constants.MAX_ITEM_AMOUNT) && (amount > -1)) {
                        client.playerItemsN[i] = amount;
                    } else {
                        client.playerItemsN[i] = Constants.MAX_ITEM_AMOUNT;
                    }
                    client.getOutStream().createFrameVarSizeWord(34);
                    client.getOutStream().writeWord(3214);
                    client.getOutStream().writeByte(i);
                    client.getOutStream().writeWord(client.playerItems[i]);
                    if (client.playerItemsN[i] > 254) {
                        client.getOutStream().writeByte(255);
                        client.getOutStream().writeDWord(client.playerItemsN[i]);
                    } else {
                        client.getOutStream().writeByte(client.playerItemsN[i]);
                    }
                    client.getOutStream().endFrameVarSizeWord();
                    client.flushOutStream();
                    i = 30;
                    return true;
                }
            }
            return false;
        } else {
            //FloorItem b = new FloorItem(item, amount, client, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
            //Server.getItemManager().newDrop(b, client);
            //Server.getItemManager().showDrop(b);
            client.getActionAssistant().sendMessage("Not enough space in your inventory.");
            return false;
        }
    }

    public void dropItem(int id, int slot) {
        int iAmount = getItemAmount(id);
			/*if(iAmount > 30000){
				client.getActionAssistant().sendMessage("You can not drop this much.");
				return;
			}*/
        if (id == 7774 || id == 7776 || id == 21406) {
            client.getActionAssistant().sendMessage("You can not drop this!");
            return;
        }
        if (id != 995 && iAmount > 50000) {
            client.getActionAssistant().sendMessage("@red@You can not drop this many items! Please ::empty it");
            return;
        }
        if (client.playerItems[slot] == (id + 1)) {
            FloorItem i = new FloorItem(client.playerItems[slot] - 1, client.playerItemsN[slot], client, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
            Server.getItemManager().newDrop(i, client);
            deleteItem(client.playerItems[slot] - 1, slot,
                    client.playerItemsN[slot]);
        }
    }

    public void deleteItem(int id, int slot, int amount) {
        if (client != null) {
            if (id == 555 && client.FrozenBook) {
                client.FrozenBook = false;
                return;
            }
            if (client.playerItems[slot] == (id + 1)) {
                if (client.playerItemsN[slot] > amount)
                    client.playerItemsN[slot] -= amount;
                else {
                    client.playerItemsN[slot] = 0;
                    client.playerItems[slot] = 0;
                }
                resetItems();
            }
        }
    }

    public void replaceItem(int id, int newid, int slot, int newamt) {
        if (client != null) {
            if (id == -2) {
                if ((newid != -1) && Item.itemStackable[newid] && playerHasItem(newid, 1)) {
                    addItem(newid, newamt);
                    client.playerItemsN[slot] = 0;
                    client.playerItems[slot] = 0;
                } else {
                    int sloti = 0;
                    while (client.playerItems[slot + sloti] != 0) {
                        sloti++;
                        if (slot + sloti == 28) {
                            slot = 0;
                            sloti = 0;
                        }
                    }
                    client.playerItemsN[slot + sloti] = newamt;
                    client.playerItems[slot + sloti] = newid + 1;
                }
            }
            if (client.playerItems[slot] == (id + 1)) {
                if (Config.CastleWars && (newid == 4037 || newid == 4039)) {
                    Server.getCastleWars().dropFlag(client, newid);
                    client.playerItemsN[slot] = 0;
                    client.playerItems[slot] = 0;
                    resetItems();
                    return;
                }
                if ((newid != -1) && Item.itemStackable[newid] && playerHasItem(newid, 1)) {
                    addItem(newid, newamt);
                    client.playerItemsN[slot] = 0;
                    client.playerItems[slot] = 0;
                } else {
                    client.playerItemsN[slot] = newamt;
                    client.playerItems[slot] = newid + 1;
                }
            }
            resetItems();
        }
    }

    public boolean wear(int wearID, int slot) {
        int targetSlot = 0;
        boolean canWearItem = true;
        if (client.isDead) {
            return false;
        }
        if (client.playerLevel[3] <= 0) {
            return false;
        }
        if (client.playerAppearance[0] == 1) {
            if (wearID == 2653 || wearID == 2655) {
                client.getActionAssistant().sendMessage("You cannot use this item as a female!");
                return false;
            }
        }
        if (wearID != 4153) {
            client.getCombat().resetAttack();
        }
//			client.combatEventFix = -1;
        if (client.playerItems[slot] == (wearID + 1)) {
            getRequirements(Server.getItemManager().getItemDefinition(wearID).getName().toLowerCase(), wearID);
            String tempType = itemType(wearID);
            if (tempType.equalsIgnoreCase("cape")) {
                targetSlot = 1;
            } else if (tempType.equalsIgnoreCase("hat")) {
                targetSlot = 0;
            } else if (tempType.equalsIgnoreCase("amulet")) {
                targetSlot = 2;
            } else if (tempType.equalsIgnoreCase("arrows")) {
                targetSlot = 13;
            } else if (tempType.equalsIgnoreCase("body")) {
                targetSlot = 4;
            } else if (tempType.equalsIgnoreCase("shield")) {
                targetSlot = 5;
            } else if (tempType.equalsIgnoreCase("legs")) {
                targetSlot = 7;
            } else if (tempType.equalsIgnoreCase("gloves")) {
                targetSlot = 9;
            } else if (tempType.equalsIgnoreCase("boots")) {
                targetSlot = 10;
            } else if (tempType.equalsIgnoreCase("ring")) {
                targetSlot = 12;
            } else {
                targetSlot = 3;
                if (client.specOn = true) {
                    client.specOn = false;
                }
            }
            switch (wearID) {
                case 20888://Rainbow Party Hat
				case 9516://Black Santa
				case 9520://Adamant Party Hat
				case 9518://Rune Santa
					targetSlot = 0;
					if (client.specOn = true) { 
						client.specOn = false;
					}
				break;
				case 7959://Weird Chaotic Rapier
				case 18353://Chaotic Maul
					targetSlot = 3;
					if (client.specOn = true) { 
						client.specOn = false;
					}
				break;
			}
            if (client.inWild()) {
                switch (wearID) {
                    case 4024:
                    case 4026:
                    case 4027:
                    case 4029:
                    case 4031:
                    case 6583:
                        client.getActionAssistant().sendMessage("You cannot use this in the wild!");
                        return false;
                }
            }


            int combatLVLREQ = getCombatReq(wearID);
            if (client.getCombatLevel() < combatLVLREQ) {
                client.getActionAssistant().sendMessage("Your combat is too low! You need level " + combatLVLREQ);
                return false;
            }
            if (wearID == 4024) {     //Ninja
                client.isMonkeyNinja();
            }
            if (wearID == 4026) {    //Guard
                client.isMonkeyGuard();
            }
            if (wearID == 4027) {     //Elder
                client.isMonkeyElder();
            }
            if (wearID == 4029) {     //Zombie
                client.isMonkeyZombie();
            }
            if (wearID == 4031) {     //Small
                client.isMonkeySmall();
            }
            if (wearID <= 15000 && wearID != 4024 && wearID != 4026 && wearID != 4027 && wearID != 4029 && wearID != 4031) {
                if (client.monkey >= 1 && targetSlot == 3) {
                    client.playerIsNPC = false;
                    //client.stillgfx(359, absY, absX);
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                    client.monkey = 0;
                }
            }
            if (targetSlot == 0 && Config.CastleWars || targetSlot >= 0 && Server.getCastleWars().isInCwWait(client) || targetSlot == 1 && Config.CastleWars) {
                if (Server.getCastleWars().isSaraTeam(client) || Server.getCastleWars().isZammyTeam(client)) {
                    client.getActionAssistant().sendMessage("You can't wear this here!");
                    return false;
                }
            }
            if (Config.CastleWars && Server.getCastleWars().isInCwWait(client)) {
                client.getActionAssistant().sendMessage("You must wait to switch items!");
                return false;
            }
            if (wearID == 20772 || wearID == 20771) {


            }
            for (int i = 9000; i < 9015; i++) {
                if (wearID == i) {
                    client.getActionAssistant().sendMessage("@red@This Item is Broken!");
                    return false;
                }
            }
            if (wearID != 6583 && client.stone == 1 && targetSlot == 12) {
                client.playerIsNPC = false;
                client.updateRequired = true;
                client.appearanceUpdateRequired = true;
                client.stone = 0;
            }
            if (wearID == 6583) {
                client.playerNPCID = 2626;
                client.playerIsNPC = true;
                client.updateRequired = true;
                client.appearanceUpdateRequired = true;
                client.stone = 1;
            }
            if (wearID == 7890 && client.playerRights != 2) {
                client.getActionAssistant().sendMessage("You are not a respected Donator you can not wear this!");
                return false;
            }
            if (wearID == 4409 && !DuelProcessor.isBL1(client.playerName)) {
                client.getActionAssistant().sendMessage("You are not apart of the winning team of blood lust!");
                return false;
            }

            if (wearID == 4411 && !DuelProcessor.isBL2(client.playerName)) {
                client.getActionAssistant().sendMessage("You are not apart of the winning team of blood lust!");
                return false;
            }
            if (wearID == 4413 && !DuelProcessor.isBL3(client.playerName)) {
                client.getActionAssistant().sendMessage("You are not apart of the winning team of blood lust!");
                return false;
            }
            if (wearID == 7979 && client.playerRights == 0) {
                client.getActionAssistant().sendMessage("You are not a Donator you can not wear this!");
                return false;
            }

            if (client.duelRule[11] && targetSlot == 0) {
                client.getActionAssistant().sendMessage("Wearing hats has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[12] && targetSlot == 1) {
                client.getActionAssistant().sendMessage("Wearing capes has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[13] && targetSlot == 2) {
                client.getActionAssistant().sendMessage("Wearing amulets has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[14] && targetSlot == 3) {
                client.getActionAssistant().sendMessage("Wearing weapons has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[15] && targetSlot == 4) {
                client.getActionAssistant().sendMessage("Wearing bodies has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[16] && targetSlot == 5) {
                client.getActionAssistant().sendMessage("Wearing shield has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[16] && is2handed(wearID)) {
                client.getActionAssistant().sendMessage("Wearing 2handed weapons has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[17] && targetSlot == 7) {
                client.getActionAssistant().sendMessage("Wearing legs has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[18] && targetSlot == 9) {
                client.getActionAssistant().sendMessage("Wearing gloves has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[19] && targetSlot == 10) {
                client.getActionAssistant().sendMessage("Wearing boots has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[20] && targetSlot == 12) {
                client.getActionAssistant().sendMessage("Wearing rings has been disabled in this duel!");
                return false;
            }
            if (client.duelRule[21] && targetSlot == 13) {
                client.getActionAssistant().sendMessage("Wearing arrows has been disabled in this duel!");
                return false;
            }
            if (targetSlot == 10 || targetSlot == 7 || targetSlot == 5 || targetSlot == 4 || targetSlot == 0 || targetSlot == 9 || targetSlot == 10) {
                if (client.defenceLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[1]) < client.defenceLevelReq) {
                        client.getActionAssistant().sendMessage("You need a defence level of " + client.defenceLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.rangeLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[4]) < client.rangeLevelReq) {
                        client.getActionAssistant().sendMessage("You need a range level of " + client.rangeLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.magicLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[6]) < client.magicLevelReq) {
                        client.getActionAssistant().sendMessage("You need a magic level of " + client.magicLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
            }
            if (targetSlot == 3) {
                client.autoCast = false;
                client.autoCastMarker = false;
                client.KorasiSpec = false;
                if (client.attackLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[0]) < client.attackLevelReq) {
                        client.getActionAssistant().sendMessage("You need an attack level of " + client.attackLevelReq + " to wield this.");
                        canWearItem = false;
                    }
                }
                if (client.rangeLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[4]) < client.rangeLevelReq) {
                        client.getActionAssistant().sendMessage("You need a range level of " + client.rangeLevelReq + " to wield this.");
                        canWearItem = false;
                    }
                }
                if (client.magicLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[6]) < client.magicLevelReq) {
                        client.getActionAssistant().sendMessage("You need a magic level of " + client.magicLevelReq + " to wield this.");
                        canWearItem = false;
                    }
                }
                if (wearID >= 4220 && wearID <= 4223) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[1]) < client.defenceLevelReq) {
                        client.getActionAssistant().sendMessage("You need a defence level of " + client.defenceLevelReq + " to wield this.");
                        canWearItem = false;
                    }
                }
            }
            if (targetSlot == 1) {
                if (client.strengthLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[2]) < client.strengthLevelReq) {
                        client.getActionAssistant().sendMessage("You need a strength level of " + client.strengthLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.defenceLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[1]) < client.defenceLevelReq) {
                        client.getActionAssistant().sendMessage("You need a defence level of " + client.defenceLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.attackLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[0]) < client.attackLevelReq) {
                        client.getActionAssistant().sendMessage("You need an attack level of " + client.attackLevelReq + " to wield this.");
                        canWearItem = false;
                    }
                }
                if (client.rangeLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[4]) < client.rangeLevelReq) {
                        client.getActionAssistant().sendMessage("You need a range level of " + client.rangeLevelReq + " to wield this.");
                        canWearItem = false;
                    }
                }
                if (client.magicLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[6]) < client.magicLevelReq) {
                        client.getActionAssistant().sendMessage("You need a magic level of " + client.magicLevelReq + " to wield this.");
                        canWearItem = false;
                    }
                }
                if (client.herbLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[15]) < client.herbLevelReq) {
                        client.getActionAssistant().sendMessage("You need a herblore level of " + client.herbLevelReq + " to wield this.");
                        canWearItem = false;
                    }
                }
                if (client.hitLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[3]) < client.hitLevelReq) {
                        client.getActionAssistant().sendMessage("You need a hitpoint level of " + client.hitLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.prayerLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[5]) < client.prayerLevelReq) {
                        client.getActionAssistant().sendMessage("You need a prayer level of " + client.prayerLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.cookLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[7]) < client.cookLevelReq) {
                        client.getActionAssistant().sendMessage("You need a cooking level of " + client.cookLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.woodLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[8]) < client.woodLevelReq) {
                        client.getActionAssistant().sendMessage("You need a woodcutting level of " + client.woodLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.fletchLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[9]) < client.fletchLevelReq) {
                        client.getActionAssistant().sendMessage("You need a fletching level of " + client.fletchLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.fishLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[10]) < client.fishLevelReq) {
                        client.getActionAssistant().sendMessage("You need a fishing level of " + client.fishLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.fireLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[11]) < client.fireLevelReq) {
                        client.getActionAssistant().sendMessage("You need a firemaking level of " + client.fireLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.craftLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[12]) < client.craftLevelReq) {
                        client.getActionAssistant().sendMessage("You need a crafting level of " + client.craftLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.smithingLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[13]) < client.smithingLevelReq) {
                        client.getActionAssistant().sendMessage("You need a smithing level of " + client.smithingLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.miningLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[14]) < client.miningLevelReq) {
                        client.getActionAssistant().sendMessage("You need a mining level of " + client.miningLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.agilityLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[16]) < client.agilityLevelReq) {
                        client.getActionAssistant().sendMessage("You need a agility level of " + client.agilityLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.thiefLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[17]) < client.thiefLevelReq) {
                        client.getActionAssistant().sendMessage("You need a thieving level of " + client.thiefLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.slayerLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[18]) < client.slayerLevelReq) {
                        client.getActionAssistant().sendMessage("You need a slayer level of " + client.slayerLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.farmingLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[19]) < client.farmingLevelReq) {
                        client.getActionAssistant().sendMessage("You need a farming level of " + client.farmingLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
                if (client.rcLevelReq > 0) {
                    if (client.getActionAssistant().getLevelForXP(client.playerXP[20]) < client.rcLevelReq) {
                        client.getActionAssistant().sendMessage("You need a runecrafting level of " + client.rcLevelReq + " to wear this.");
                        canWearItem = false;
                    }
                }
            }
            if (!canWearItem) {
                return false;
            }
            int wearAmount = client.playerItemsN[slot];
            if (wearAmount < 1) {
                return false;
            }
            if (slot >= 0 && wearID >= 0) {
                if (targetSlot == 5 && is2handed(client.playerEquipment[3])) {
                    if (client.playerEquipment[3] >= -1) {
                        if (freeSlots() > 0) {
                            replaceItem(wearID, client.playerEquipment[3], slot, client.playerEquipmentN[3]);
                            client.playerEquipment[3] = -1;
                            client.playerEquipmentN[3] = 0;
                            client.getCombatFormulas().sendWeapon();
                            client.getCombatEmotes().getPlayerAnimIndex();
                            client.getOutStream().createFrame(34);
                            client.getOutStream().writeWord(6);
                            client.getOutStream().writeWord(1688);
                            client.getOutStream().writeByte(3);
                            client.getOutStream().writeWord(0);
                            client.getOutStream().writeByte(0);
                            client.flushOutStream();
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            client.getCombat().resetAttack2();
                            //	remove(client.playerEquipment[3], 3);
                        } else {
                            client.getActionAssistant().sendMessage("You don't have enough space in your inventory.");
                            return false;
                        }
                    }
                }
                if ((is2handed(wearID) && (targetSlot == 3))) {
                    if (client.playerEquipment[5] >= -1) {
                        if (freeSlots() > 0) {
                            replaceItem(-2, client.playerEquipment[5], slot, client.playerEquipmentN[5]);
                            replaceItem(wearID, client.playerEquipment[3], slot, client.playerEquipmentN[3]);
                            client.playerEquipment[5] = -1;
                            client.playerEquipmentN[5] = 0;
                            client.getCombatFormulas().sendWeapon();
                            client.getCombatEmotes().getPlayerAnimIndex();
                            client.getOutStream().createFrame(34);
                            client.getOutStream().writeWord(6);
                            client.getOutStream().writeWord(1688);
                            client.getOutStream().writeByte(5);
                            client.getOutStream().writeWord(0);
                            client.getOutStream().writeByte(0);
                            client.flushOutStream();
                            client.updateRequired = true;
                            client.appearanceUpdateRequired = true;
                            //client.getCombat().resetAttack();
                            //	remove(client.playerEquipment[5], 5);
                        } else {
                            client.getActionAssistant().sendMessage("You don't have enough space in your inventory.");
                            return false;
                        }
                    }
                    if (client.playerEquipment[3] >= -1) {
                        if (freeSlots() > 0) {
                            if (client.playerEquipment[targetSlot] != wearID && client.playerEquipment[targetSlot] >= 0) {
                                replaceItem(wearID, client.playerEquipment[3], slot, client.playerEquipmentN[3]);
                                client.getCombatFormulas().sendWeapon();
                                client.getCombatEmotes().getPlayerAnimIndex();
                                client.flushOutStream();
                                client.updateRequired = true;
                                client.appearanceUpdateRequired = true;
                                //	client.getCombat().resetAttack();
                                //	remove(client.playerEquipment[3], 3);
                            }
                        } else {
                            //	client.getActionAssistant().sendMessage("You don't have enough space in your inventory.");
                            //	return false;
                        }
                    }
                }

                if (Item.itemStackable[wearID] && client.playerEquipment[targetSlot] == wearID) {
                    wearAmount = client.playerEquipmentN[targetSlot] + wearAmount;
                    deleteItem(wearID, slot, wearAmount);
                } else {
                    replaceItem(wearID, client.playerEquipment[targetSlot], slot, client.playerEquipmentN[targetSlot]);
                }
            }
            client.dHit = false;
            client.getOutStream().createFrameVarSizeWord(34);
            client.getOutStream().writeWord(1688);
            client.getOutStream().writeByte(targetSlot);
            client.getOutStream().writeWord(wearID + 1);
            if (wearAmount > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord(wearAmount);
            } else {
                client.getOutStream().writeByte(wearAmount); // amount
            }
            client.getOutStream().endFrameVarSizeWord();
            client.flushOutStream();
            int wearHPBonus = getBonusForItem(wearID);
            int oldHPBonus = getBonusForItem(client.playerEquipment[targetSlot]);
            if (!(client.playerLevel[3] - oldHPBonus <= 1) && (oldHPBonus != wearHPBonus)) {
                client.playerLevel[3] -= oldHPBonus;
                refreshSkill(3);
            }
            client.playerEquipment[targetSlot] = wearID;
            client.playerEquipmentN[targetSlot] = wearAmount;
            if (checkHpItem(wearID) && (oldHPBonus != wearHPBonus)) {
                client.playerLevel[3] += getBonusForItem(wearID);
                refreshSkill(3);
            }
            if (wearID == 15486) {
                sendMessage("If you want to spec with this weapon type ::spec until we can update the menu");
            } else if (System.currentTimeMillis() - client.SOLspec < 60000) {
                client.actionAssistant.sendMessage("Your aura of protection fades");
                client.SOLspec = 0;

            }
            client.getCombatEmotes().getPlayerAnimIndex();
            client.getCombatFormulas().sendWeapon();
            client.updateRequired = true;
            client.appearanceUpdateRequired = true;
            return true;
        } else {
            return false;
        }
    }

    public String itemType(int item) {
        for (int i = 0; i < Item.capes.length; i++) {
            if (item == Item.capes[i])
                return "cape";
        }
        for (int i = 0; i < Item.hats.length; i++) {
            if (item == Item.hats[i])
                return "hat";
        }
        for (int i = 0; i < Item.boots.length; i++) {
            if (item == Item.boots[i])
                return "boots";
        }
        for (int i = 0; i < Item.gloves.length; i++) {
            if (item == Item.gloves[i])
                return "gloves";
        }
        for (int i = 0; i < Item.shields.length; i++) {
            if (item == Item.shields[i])
                return "shield";
        }
        for (int i = 0; i < Item.amulets.length; i++) {
            if (item == Item.amulets[i])
                return "amulet";
        }
        for (int i = 0; i < Item.arrows.length; i++) {
            if (item == Item.arrows[i])
                return "arrows";
        }
        for (int i = 0; i < Item.rings.length; i++) {
            if (item == Item.rings[i])
                return "ring";
        }
        for (int i = 0; i < Item.body.length; i++) {
            if (item == Item.body[i])
                return "body";
        }
        for (int i = 0; i < Item.legs.length; i++) {
            if (item == Item.legs[i])
                return "legs";
        }

        // Default
        return "weapon";
    }

    public void remove(int wearID, int slot) {
			if(Config.CastleWars && (wearID == 4042 ||wearID == 4041 ||wearID == 4037 ||wearID == 4039)){
				Server.getCastleWars().dropFlag(client,wearID);
				client.playerEquipment[slot]=-1;
				client.playerEquipmentN[slot]=0;
				client.getCombatFormulas().sendWeapon();
				client.getCombatEmotes().getPlayerAnimIndex();
				client.getOutStream().createFrame(34);
				client.getOutStream().writeWord(6);
				client.getOutStream().writeWord(1688);
				client.getOutStream().writeByte(slot);
				client.getOutStream().writeWord(0);
				client.getOutStream().writeByte(0);
				client.flushOutStream();
				client.updateRequired = true; 
				client.appearanceUpdateRequired = true;
				return;
			}
        if (wearID == 6583) {
            client.playerIsNPC = false;
            client.updateRequired = true;
            client.appearanceUpdateRequired = true;
            client.stone = 0;
        }
        if (slot == 3) {
            if (System.currentTimeMillis() - client.SOLspec < 60000) {
                client.actionAssistant.sendMessage("Your aura of protection fades");
                client.SOLspec = 0;
            }
        }
        if (client.playerEquipment[slot] > -1) {
            if (addItem(client.playerEquipment[slot], client.playerEquipmentN[slot])) {
                if (slot == 13) {
                    client.tempusingArrows = false;
                } else if (slot == 3) {
                    client.tempusingBow = false;
                    client.tempusingOtherRangeWeapons = false;
                    client.isNotMonkey();
                } else if ((slot == 0 && Config.CastleWars) || (slot == 1 && Config.CastleWars)) {
                    if (Server.getCastleWars().isSaraTeam(client) || Server.getCastleWars().isZammyTeam(client)) {
                        client.getActionAssistant().sendMessage("You can't remove this!");
                        return;
                    }
                }
                client.playerEquipment[slot] = -1;
                client.playerEquipmentN[slot] = 0;
                client.getCombatFormulas().sendWeapon();
                client.getCombatEmotes().getPlayerAnimIndex();
                client.getOutStream().createFrame(34);
                client.getOutStream().writeWord(6);
                client.getOutStream().writeWord(1688);
                client.getOutStream().writeByte(slot);
                client.getOutStream().writeWord(0);
                client.getOutStream().writeByte(0);
                client.flushOutStream();
					/*int hpBonus = getMaxHP();
						if (client.playerLevel[3] > (getLevelForXP(client.playerXP[3]) + hpBonus)) {
							client.playerLevel[3] = getLevelForXP(client.playerXP[3]) + hpBonus;
							refreshSkill(3);
						}*/
                if (!(client.playerLevel[3] - getBonusForItem(wearID) <= 1)) {
                    client.playerLevel[3] -= getBonusForItem(wearID);
                    refreshSkill(3);
                }

                client.updateRequired = true;
                client.appearanceUpdateRequired = true;
                client.getCombat().resetAttack();
            }
        }
    }

    /**
     * **********************************************************************
     * CHAT OPTIONS PACKET *
     * ***********************************************************************
     */
    public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
        client.getOutStream().createFrame(206);
        client.getOutStream().writeByte(publicChat); // On = 0, Friends = 1, Off
        // = 2, Hide = 3
        client.getOutStream().writeByte(privateChat); // On = 0, Friends = 1,
        // Off = 2
        client.getOutStream().writeByte(tradeBlock); // On = 0, Friends = 1, Off
        // = 2
        client.flushOutStream();
    }

    /**
     * **********************************************************************
     * WELCOME SCREEN *
     * ***********************************************************************
     */
    public void openWelcomeScreen(int recoveryChange, boolean memberWarning,
                                  int messages, int lastLoginIP, int lastLogin) {
        client.getOutStream().createFrame(176);
        // days since last recovery change 200 for not yet set 201 for members
        // server,
        // otherwise, how many days ago recoveries have been changed.
        client.getOutStream().writeByteC(recoveryChange);
        client.getOutStream().writeWordA(messages); // # of unread messages
        client.getOutStream().writeByte(memberWarning ? 1 : 0); // 1 for member
        // on
        // non-members
        // world warning
        client.getOutStream().writeDWord_v2(lastLoginIP); // ip of last login
        client.getOutStream().writeWord(lastLogin); // days
        client.flushOutStream();
    }

    /**
     * **********************************************************************
     * CLIENT CONFIGURATION *
     * ***********************************************************************
     */
    public void setClientConfig(int id, int state) {
        client.getOutStream().createFrame(36);
        client.getOutStream().writeWordBigEndian(id);
        client.getOutStream().writeByte(state);
        client.flushOutStream();
    }

    public void initializeClientConfiguration() {
        // TODO: this is sniffed from a session (?), yet have to figure out what
        // each of these does.
        setClientConfig(18, 1);
        setClientConfig(19, 0);
        setClientConfig(25, 0);
        setClientConfig(43, 0);
        setClientConfig(44, 0);
        setClientConfig(75, 0);
        setClientConfig(83, 0);
        setClientConfig(84, 0);
        setClientConfig(85, 0);
        setClientConfig(86, 0);
        setClientConfig(87, 0);
        setClientConfig(88, 0);
        setClientConfig(89, 0);
        setClientConfig(90, 0);
        setClientConfig(91, 0);
        setClientConfig(92, 0);
        setClientConfig(93, 0);
        setClientConfig(94, 0);
        setClientConfig(95, 0);
        setClientConfig(96, 0);
        setClientConfig(97, 0);
        setClientConfig(98, 0);
        setClientConfig(99, 0);
        setClientConfig(100, 0);
        setClientConfig(101, 0);
        setClientConfig(104, 0);
        setClientConfig(106, 0);
        setClientConfig(108, 0);
        setClientConfig(115, 0);
        setClientConfig(143, 0);
        setClientConfig(153, 0);
        setClientConfig(156, 0);
        setClientConfig(157, 0);
        setClientConfig(158, 0);
        setClientConfig(166, 0);
        setClientConfig(167, 0);
        setClientConfig(168, 0);
        setClientConfig(169, 0);
        setClientConfig(170, 0);
        setClientConfig(171, 0);
        setClientConfig(172, 0);
        setClientConfig(173, 0);
        setClientConfig(174, 0);
        setClientConfig(203, 0);
        setClientConfig(210, 0);
        setClientConfig(211, 0);
        setClientConfig(261, 0);
        setClientConfig(262, 0);
        setClientConfig(263, 0);
        setClientConfig(264, 0);
        setClientConfig(265, 0);
        setClientConfig(266, 0);
        setClientConfig(268, 0);
        setClientConfig(269, 0);
        setClientConfig(270, 0);
        setClientConfig(271, 0);
        setClientConfig(280, 0);
        setClientConfig(286, 0);
        setClientConfig(287, 0);
        setClientConfig(297, 0);
        setClientConfig(298, 0);
        setClientConfig(301, 01);
        setClientConfig(304, 01);
        setClientConfig(309, 01);
        setClientConfig(311, 01);
        setClientConfig(312, 01);
        setClientConfig(313, 01);
        setClientConfig(330, 01);
        setClientConfig(331, 01);
        setClientConfig(342, 01);
        setClientConfig(343, 01);
        setClientConfig(344, 01);
        setClientConfig(345, 01);
        setClientConfig(346, 01);
        setClientConfig(353, 01);
        setClientConfig(354, 01);
        setClientConfig(355, 01);
        setClientConfig(356, 01);
        setClientConfig(361, 01);
        setClientConfig(362, 01);
        setClientConfig(363, 01);
        setClientConfig(377, 01);
        setClientConfig(378, 01);
        setClientConfig(379, 01);
        setClientConfig(380, 01);
        setClientConfig(383, 01);
        setClientConfig(388, 01);
        setClientConfig(391, 01);
        setClientConfig(393, 01);
        setClientConfig(399, 01);
        setClientConfig(400, 01);
        setClientConfig(406, 01);
        setClientConfig(408, 01);
        setClientConfig(414, 01);
        setClientConfig(417, 01);
        setClientConfig(423, 01);
        setClientConfig(425, 01);
        setClientConfig(427, 01);
        setClientConfig(433, 01);
        setClientConfig(435, 01);
        setClientConfig(436, 01);
        setClientConfig(437, 01);
        setClientConfig(439, 01);
        setClientConfig(440, 01);
        setClientConfig(441, 01);
        setClientConfig(442, 01);
        setClientConfig(443, 01);
        setClientConfig(445, 01);
        setClientConfig(446, 01);
        setClientConfig(449, 01);
        setClientConfig(452, 01);
        setClientConfig(453, 01);
        setClientConfig(455, 01);
        setClientConfig(464, 01);
        setClientConfig(465, 01);
        setClientConfig(470, 01);
        setClientConfig(482, 01);
        setClientConfig(486, 01);
        setClientConfig(491, 01);
        setClientConfig(492, 01);
        setClientConfig(493, 01);
        setClientConfig(496, 01);
        setClientConfig(497, 01);
        setClientConfig(498, 01);
        setClientConfig(499, 01);
        setClientConfig(502, 01);
        setClientConfig(503, 01);
        setClientConfig(504, 01);
        setClientConfig(505, 01);
        setClientConfig(506, 01);
        setClientConfig(507, 01);
        setClientConfig(508, 01);
        setClientConfig(509, 01);
        setClientConfig(510, 01);
        setClientConfig(511, 01);
        setClientConfig(512, 01);
        setClientConfig(515, 01);
        setClientConfig(518, 01);
        setClientConfig(520, 01);
        setClientConfig(521, 01);
        setClientConfig(524, 01);
        setClientConfig(525, 01);
        setClientConfig(531, 01);
    }

    public boolean hasBoneCrusher() {
        if (isItemInBag(18337)) {
            return true;
        }
        return false;
    }

    /**
     * **********************************************************************
     * ITEM UTILITY FUNCTIONS *
     * ***********************************************************************
     */
    public int getItemSlotDegrade(int itemID) {
        for (int i = 0; i < client.playerItems.length; i++) {
            if ((client.playerItems[i] - 1) == itemID) {
                return i;
            }
        }
        return -1;
    }

    public int getItemSlot(int itemID) {
        for (int i = 0; i < client.playerItems.length; i++) {
            if ((client.playerItems[i] - 1) == itemID) {
                return i;
            }
        }
        return -1;
    }

    public boolean isItemInBag(int itemID) {
        for (int i = 0; i < client.playerItems.length; i++) {
            if ((client.playerItems[i] - 1) == itemID) {
                return true;
            }
        }
        return false;
    }

    /**
     * **********************************************************************
     * TURN TO METHOD *
     * ***********************************************************************
     */
    public void turnTo(int pointX, int pointY) {
        client.focusPointX = 2 * pointX + 1;
        client.focusPointY = 2 * pointY + 1;
        client.updateRequired = true;
    }

    public void overLoadEvent(final Client client) {
        client.overLoad = true;
        client.loops = 0;
        EventManager.getSingleton().addEvent(client, "c overload", new Event() {

            @Override
            public void execute(EventContainer c) {
                client.loops++;
                if (client.duelRule[5] || (client.inWild() && !client.inCWar())) {
                    //client.getActionAssistant().sendMessage("Potions have been disabled in this duel!");
                    return;
                }

                int abc = 7;
                client.playerLevel[6] += abc;
                if (client.playerLevel[6] > client.getLevelForXP(client.playerXP[6]) + abc) {
                    client.playerLevel[6] = abc + client.getLevelForXP(client.playerXP[6]);
                }

                abc = (int) Math.floor(4 + (client.getLevelForXP(client.playerXP[0]) / 5.2));
                client.playerLevel[4] += abc;
                if (client.playerLevel[4] > client.getLevelForXP(client.playerXP[4]) + abc) {
                    client.playerLevel[4] = abc + client.getLevelForXP(client.playerXP[4]);
                }
                abc = (int) (Math.floor(client.getLevelForXP(client.playerXP[0]) * 0.22) + 5);
                client.playerLevel[0] += abc;
                if (client.playerLevel[0] > client.getLevelForXP(client.playerXP[0]) + abc) {
                    client.playerLevel[0] = abc + client.getLevelForXP(client.playerXP[0]);
                }
                abc = ((int) Math.floor(client.getLevelForXP(client.playerXP[2]) * 0.22) + 5);


                client.playerLevel[2] += abc;
                if (client.playerLevel[2] > client.getLevelForXP(client.playerXP[2]) + abc) {
                    client.playerLevel[2] = abc + client.getLevelForXP(client.playerXP[2]);
                }
                abc = (int) (Math.floor(client.getLevelForXP(client.playerXP[1]) * 0.22) + 5);


                client.playerLevel[1] += abc;
                if (client.playerLevel[1] > client.getLevelForXP(client.playerXP[1]) + abc) {
                    client.playerLevel[1] = abc + client.getLevelForXP(client.playerXP[1]);
                }
                client.getActionAssistant().refreshSkill(6);
                client.getActionAssistant().refreshSkill(1);
                client.getActionAssistant().refreshSkill(0);
                client.getActionAssistant().refreshSkill(2);
                client.getActionAssistant().refreshSkill(4);

                if (client.loops >= 16) {
                    c.stop();
                }
            }

            @Override
            public void stop() {
                client.overLoad = false;
                sendMessage("Your overload boost has worn off");
                for (int level = 0; level <= 6; level++) {
                    if (level == 3) {
                        continue;
                    }
                    int total = (getLevelForXP(client.playerXP[level]));
                    if (client.playerLevel[level] > total) {
                        client.playerLevel[level] = getLevelForXP(client.playerXP[level]);
                        refreshSkill(level);
                    }
                }
            }

        }, 15000);


    }

    /**
     * **********************************************************************
     * NPC DIALOG PACKETS *
     * ***********************************************************************
     */
    public void sendFrame200(int i, int j) {
        client.getOutStream().createFrame(200);
        client.getOutStream().writeWord(i);
        client.getOutStream().writeWord(j);
        client.flushOutStream();
    } //caused error

    public void frame174(int i1, int i2, int i3) {

        client.getOutStream().createFrame(174);
        client.getOutStream().writeWord(i1);
        client.getOutStream().writeByte(i2);
        client.getOutStream().writeWord(i3);
        client.flushOutStream();
    }

    public void frame74(int music) {
        client.getOutStream().createFrame(74);//this sends the frame
        client.getOutStream().writeWordBigEndian(music);//this is the music id
    }

    public void sendFrame75(int npc, int i) {
        client.getOutStream().createFrame(75);
        client.getOutStream().writeWordBigEndianA(npc);
        client.getOutStream().writeWordBigEndianA(i);
        client.flushOutStream();
    }

    public void sendFrame164(int i) {
        client.getOutStream().createFrame(164);
        client.getOutStream().writeWordBigEndian_dup(i);
        client.flushOutStream();
    }

    public void sendFrame171(int i, int j) {
        client.getOutStream().createFrame(171);
        client.getOutStream().writeByte(i);
        client.getOutStream().writeWord(j);
        client.flushOutStream();
    }

    public void sendFrame36(int id, int state) {
        client.getOutStream().createFrame(36);
        client.getOutStream().writeWordBigEndian(id);
        client.getOutStream().writeByte(state);
        client.flushOutStream();
    }

    public void sendFrame185(int i) {
        client.getOutStream().createFrame(185);
        client.getOutStream().writeWordBigEndianA(i);
        client.flushOutStream();
    }

    public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
        client.getOutStream().createFrame(246);
        client.getOutStream().writeWordBigEndian(MainFrame);
        client.getOutStream().writeWord(SubFrame);
        client.getOutStream().writeWord(SubFrame2);
        client.flushOutStream();
    }

    public void openUpShop(int shopId) {
        Shop s = Server.getShopManager().getShops().get(shopId);
        if (s == null)
            return;
        sendQuest(s.getName(), 3901);
        sendFrame248(3824, 3822);
        resetItems(3823);
        resetShop(s);
        client.getExtraData().put("shop", shopId);
        client.flushOutStream();
    }

    public void sendFrame248(int MainFrame, int SubFrame) {
        client.getOutStream().createFrame(248);
        client.getOutStream().writeWordA(MainFrame);
        client.getOutStream().writeWord(SubFrame);
        client.flushOutStream();
    }

    public int playerItemAmountCount = 0;

    public boolean playerHasItemAmount(int itemID, int itemAmount) {
        if (itemID == -1 || itemAmount == -1) return true;
        playerItemAmountCount = 0;
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] == itemID + 1) {
                playerItemAmountCount += client.playerItemsN[i];
            }
            if (playerItemAmountCount >= itemAmount) {
                return true;
            }
        }
        return false;
    }

    public void resetItems(int WriteFrame) {
        if (client.resetting) {
            return;
        }
        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(WriteFrame);
        client.getOutStream().writeWord(client.playerItems.length);
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItemsN[i] > 254) {
                client.getOutStream().writeByte(255); // item's stack count. if
                // over 254, write byte
                // 255
                client.getOutStream().writeDWord_v2(client.playerItemsN[i]); // and
                // then
                // the
                // real
                // value
                // with
                // writeDWord_v2
            } else {
                client.getOutStream().writeByte(client.playerItemsN[i]);
            }
            client.getOutStream().writeWordBigEndianA(client.playerItems[i]); // item
            // id
        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();
    }

    public void resetShop(Shop shop) {
        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(3900);
        int count = 0;
        for (int i = 0; i < shop.getContainerSize(); i++) {
            Item si = shop.getItemBySlot(i);
            if (si != null) {
                count++;
            }
        }
        client.getOutStream().writeWord(count);
        for (int i = 0; i < shop.getContainerSize(); i++) {
            Item si = shop.getItemBySlot(i);
            if (si == null) {
                continue;
            }
            if (si.getAmount() > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord_v2(si.getAmount());
            } else {
                client.getOutStream().writeByte(si.getAmount());
            }
            client.getOutStream().writeWordBigEndianA(si.getId() + 1);
        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();
    }

    public void frame61(int i1) {
        client.getOutStream().createFrame(61);
        client.getOutStream().writeByte(i1);
    }

    public void sendFrame126(String s, int id) {
    	if(!client.checkPacket126Update(s, id)) {
			//Server.dataSaved += (string.length() + 4);
			return;
		}
        client.getOutStream().createFrameVarSizeWord(126);
        client.getOutStream().writeString(s);
        client.getOutStream().writeWordA(id);
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();
    }

    public void ShowTopPker() {
        if (TopPker.getTopPker().size() < 11) {
            switch (TopPker.getTopPker().size()) {
                case 0:
                    //client.getActionAssistant().sendFrame126("Rank 1 - " +TopPker.getTopPker().get(0).getPlayerName(), 12806);
                    client.getActionAssistant().sendFrame126("Rank 1 -  No one!", 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 -  No one!", 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 -  No one!", 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 -  No one!", 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 -  No one!", 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 -  No one!", 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 -  No one!", 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 -  No one!", 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 -  No one!", 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 -  No one!", 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;
                case 1:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 -  No one!", 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 -  No one!", 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 -  No one!", 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 -  No one!", 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 -  No one!", 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 -  No one!", 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 -  No one!", 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 -  No one!", 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;
                case 2:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 -  No one!", 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 -  No one!", 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 -  No one!", 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 -  No one!", 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 -  No one!", 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 -  No one!", 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 -  No one!", 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;
                case 3:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 - " + TopPker.getTopPker().get(2).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(2).getPlayerRating(), 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 -  No one!", 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 -  No one!", 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 -  No one!", 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 -  No one!", 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 -  No one!", 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 -  No one!", 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;
                case 4:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 - " + TopPker.getTopPker().get(2).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(2).getPlayerRating(), 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 - " + TopPker.getTopPker().get(3).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(3).getPlayerRating(), 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 -  No one!", 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 -  No one!", 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 -  No one!", 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 -  No one!", 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 -  No one!", 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;
                case 5:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 - " + TopPker.getTopPker().get(2).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(2).getPlayerRating(), 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 - " + TopPker.getTopPker().get(3).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(3).getPlayerRating(), 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 - " + TopPker.getTopPker().get(4).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(4).getPlayerRating(), 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 -  No one!", 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 -  No one!", 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 -  No one!", 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 -  No one!", 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;
                case 6:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 - " + TopPker.getTopPker().get(2).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(2).getPlayerRating(), 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 - " + TopPker.getTopPker().get(3).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(3).getPlayerRating(), 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 - " + TopPker.getTopPker().get(4).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(4).getPlayerRating(), 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 - " + TopPker.getTopPker().get(5).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(5).getPlayerRating(), 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 -  No one!", 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 -  No one!", 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 -  No one!", 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;
                case 7:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 - " + TopPker.getTopPker().get(2).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(2).getPlayerRating(), 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 - " + TopPker.getTopPker().get(3).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(3).getPlayerRating(), 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 - " + TopPker.getTopPker().get(4).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(4).getPlayerRating(), 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 - " + TopPker.getTopPker().get(5).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(5).getPlayerRating(), 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 - " + TopPker.getTopPker().get(6).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(6).getPlayerRating(), 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 -  No one!", 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 -  No one!", 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);

                    break;
                case 8:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 - " + TopPker.getTopPker().get(2).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(2).getPlayerRating(), 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 - " + TopPker.getTopPker().get(3).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(3).getPlayerRating(), 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 - " + TopPker.getTopPker().get(4).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(4).getPlayerRating(), 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 - " + TopPker.getTopPker().get(5).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(5).getPlayerRating(), 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 - " + TopPker.getTopPker().get(6).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(6).getPlayerRating(), 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 - " + TopPker.getTopPker().get(7).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(7).getPlayerRating(), 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 -  No one!", 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;
                case 9:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 - " + TopPker.getTopPker().get(2).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(2).getPlayerRating(), 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 - " + TopPker.getTopPker().get(3).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(3).getPlayerRating(), 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 - " + TopPker.getTopPker().get(4).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(4).getPlayerRating(), 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 - " + TopPker.getTopPker().get(5).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(5).getPlayerRating(), 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 - " + TopPker.getTopPker().get(6).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(6).getPlayerRating(), 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 - " + TopPker.getTopPker().get(7).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(7).getPlayerRating(), 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 - " + TopPker.getTopPker().get(8).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(8).getPlayerRating(), 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 -  No one!", 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;
                case 10:
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
                    client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
                    client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
                    client.getActionAssistant().sendFrame126("Rank 3 - " + TopPker.getTopPker().get(2).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(2).getPlayerRating(), 11204);
                    client.getActionAssistant().sendFrame126("Rank 4 - " + TopPker.getTopPker().get(3).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(3).getPlayerRating(), 11205);
                    client.getActionAssistant().sendFrame126("Rank 5 - " + TopPker.getTopPker().get(4).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(4).getPlayerRating(), 11206);
                    client.getActionAssistant().sendFrame126("Rank 6 - " + TopPker.getTopPker().get(5).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(5).getPlayerRating(), 11207);
                    client.getActionAssistant().sendFrame126("Rank 7 - " + TopPker.getTopPker().get(6).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(6).getPlayerRating(), 11208);
                    client.getActionAssistant().sendFrame126("Rank 8 - " + TopPker.getTopPker().get(7).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(7).getPlayerRating(), 11209);
                    client.getActionAssistant().sendFrame126("Rank 9 - " + TopPker.getTopPker().get(8).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(8).getPlayerRating(), 11210);
                    client.getActionAssistant().sendFrame126("Rank 10 - " + TopPker.getTopPker().get(9).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(9).getPlayerRating(), 11211);
                    client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
                    client.actionAssistant.showInterface(11200);
                    break;

            }
        } else {


            client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11213);
            client.getActionAssistant().sendFrame126("Rank 1 - " + TopPker.getTopPker().get(0).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(0).getPlayerRating(), 11214);
            client.getActionAssistant().sendFrame126("Rank 2 - " + TopPker.getTopPker().get(1).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(1).getPlayerRating(), 11203);
            client.getActionAssistant().sendFrame126("Rank 3 - " + TopPker.getTopPker().get(2).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(2).getPlayerRating(), 11204);
            client.getActionAssistant().sendFrame126("Rank 4 - " + TopPker.getTopPker().get(3).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(3).getPlayerRating(), 11205);
            client.getActionAssistant().sendFrame126("Rank 5 - " + TopPker.getTopPker().get(4).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(4).getPlayerRating(), 11206);
            client.getActionAssistant().sendFrame126("Rank 6 - " + TopPker.getTopPker().get(5).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(5).getPlayerRating(), 11207);
            client.getActionAssistant().sendFrame126("Rank 7 - " + TopPker.getTopPker().get(6).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(6).getPlayerRating(), 11208);
            client.getActionAssistant().sendFrame126("Rank 8 - " + TopPker.getTopPker().get(7).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(7).getPlayerRating(), 11209);
            client.getActionAssistant().sendFrame126("Rank 9 - " + TopPker.getTopPker().get(8).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(8).getPlayerRating(), 11210);
            client.getActionAssistant().sendFrame126("Rank 10 - " + TopPker.getTopPker().get(9).getPlayerName() + " - Rating - " + TopPker.getTopPker().get(9).getPlayerRating(), 11211);
            client.getActionAssistant().sendFrame126("Your rating is currently: " + TopPkersList.getRating(client.playerName), 11216);
            client.actionAssistant.showInterface(11200);
        }

    }

    public String getColor(int type, int id) {
        String color = "@red@";
        switch (type) {
            case 0:        // combat
                if (client.getNRAchievements().getCompletedAchievement(client, type, id) > 0) {
                    color = "@gre@";
                }
                break;
            case 1:        // Misc	
                if (client.getNRAchievements().getCompletedAchievement(client, type, id) > 0) {
                    color = "@gre@";
                }
                break;
            case 2:        // mons
                if (client.getNRAchievements().getCompletedAchievement(client, type, id) > 0) {
                    color = "@gre@";
                }
                break;
            case 3:        // pking
                if (client.getNRAchievements().getCompletedAchievement(client, type, id) > 0) {
                    color = "@gre@";

                }
                break;
            case 4:        // ski;
                if (client.getNRAchievements().getCompletedAchievement(client, type, id) > 0) {
                    color = "@gre@";
                }
                break;
        }

        return color;
    }

    public void showAchievements(int type) {

        switch (type) {
            case 0: // Combat
                for (int k = 0; k < 20; k++) {
                    client.getActionAssistant().sendFrame126("", 26713 + k); // name
                    client.getActionAssistant().sendFrame126("", 26814 + k); // desc
                    client.getActionAssistant().sendFrame126("", 27115 + k); // points
                }
                for (int i = 0; i < AchievementManager.getCombat().size(); i++) { // 100 is max.
                    client.getActionAssistant().sendFrame126(getColor(type, i) + AchievementManager.getCombat().get(i).getName(), 26713 + i); // name
                    if (getColor(type, i).equals("@gre@")) {
                        client.getActionAssistant().sendFrame126("Completed!", 26814 + i); // desc
                    } else {
                        client.getActionAssistant().sendFrame126("" + AchievementManager.getCombat().get(i).getDesc(), 26814 + i); // desc
                    }
                    client.getActionAssistant().sendFrame126("" + AchievementManager.getCombat().get(i).getPoints(), 27115 + i); // points
                }
                client.getActionAssistant().showInterface(26600);
                break;
            case 1:    // Misc
                for (int k = 0; k < 20; k++) {
                    client.getActionAssistant().sendFrame126("", 26713 + k); // name
                    client.getActionAssistant().sendFrame126("", 26814 + k); // desc
                    client.getActionAssistant().sendFrame126("", 27115 + k); // points
                }
                for (int i = 0; i < AchievementManager.getMisc().size(); i++) { // 100 is max.
                    client.getActionAssistant().sendFrame126(getColor(type, i) + AchievementManager.getMisc().get(i).getName(), 26713 + i); // name
                    if (getColor(type, i).equals("@gre@")) {
                        client.getActionAssistant().sendFrame126("Completed!", 26814 + i); // desc
                    } else {
                        client.getActionAssistant().sendFrame126("" + AchievementManager.getMisc().get(i).getDesc(), 26814 + i); // desc
                    }
                    client.getActionAssistant().sendFrame126("" + AchievementManager.getMisc().get(i).getPoints(), 27115 + i); // points
                }
                client.getActionAssistant().showInterface(26600);
                break;
            case 2: // Monsters
                for (int k = 0; k < 20; k++) {
                    client.getActionAssistant().sendFrame126("", 26713 + k); // name
                    client.getActionAssistant().sendFrame126("", 26814 + k); // desc
                    client.getActionAssistant().sendFrame126("", 27115 + k); // points
                }
                for (int i = 0; i < AchievementManager.getMonster().size(); i++) { // 100 is max.
                    client.getActionAssistant().sendFrame126(getColor(type, i) + AchievementManager.getMonster().get(i).getName(), 26713 + i); // name
                    if (getColor(type, i).equals("@gre@")) {
                        client.getActionAssistant().sendFrame126("Completed!", 26814 + i); // desc
                    } else {
                        client.getActionAssistant().sendFrame126("" + AchievementManager.getMonster().get(i).getDesc(), 26814 + i); // desc
                    }
                    client.getActionAssistant().sendFrame126("" + AchievementManager.getMonster().get(i).getPoints(), 27115 + i); // points
                }
                client.getActionAssistant().showInterface(26600);
                break;
            case 3: // Pking
                for (int k = 0; k < 20; k++) {
                    client.getActionAssistant().sendFrame126("", 26713 + k); // name
                    client.getActionAssistant().sendFrame126("", 26814 + k); // desc
                    client.getActionAssistant().sendFrame126("", 27115 + k); // points
                }
                for (int i = 0; i < AchievementManager.getPking().size(); i++) { // 100 is max.
                    client.getActionAssistant().sendFrame126(getColor(type, i) + AchievementManager.getPking().get(i).getName(), 26713 + i); // name
                    if (getColor(type, i).equals("@gre@")) {
                        client.getActionAssistant().sendFrame126("Completed!", 26814 + i); // desc
                    } else {
                        client.getActionAssistant().sendFrame126("" + AchievementManager.getPking().get(i).getDesc(), 26814 + i); // desc
                    }
                    client.getActionAssistant().sendFrame126("" + AchievementManager.getPking().get(i).getPoints(), 27115 + i); // points
                }
                client.getActionAssistant().showInterface(26600);
                break;
            case 4: // Skilling
                for (int k = 0; k < 20; k++) {
                    client.getActionAssistant().sendFrame126("", 26713 + k); // name
                    client.getActionAssistant().sendFrame126("", 26814 + k); // desc
                    client.getActionAssistant().sendFrame126("", 27115 + k); // points
                }
                for (int i = 0; i < AchievementManager.getSkilling().size(); i++) { // 100 is max.
                    client.getActionAssistant().sendFrame126(getColor(type, i) + AchievementManager.getSkilling().get(i).getName(), 26713 + i); // name
                    if (getColor(type, i).equals("@gre@")) {
                        client.getActionAssistant().sendFrame126("Completed!", 26814 + i); // desc
                    } else {
                        client.getActionAssistant().sendFrame126("" + AchievementManager.getSkilling().get(i).getDesc(), 26814 + i); // desc
                    }
                    client.getActionAssistant().sendFrame126("" + AchievementManager.getSkilling().get(i).getPoints(), 27115 + i); // points
                }
                client.getActionAssistant().showInterface(26600);
                break;

        }
        client.getActionAssistant().sendFrame126("@red@Castle Wars Points: " + client.cwPoints, 19159);
    }

    public Double calculateKillDeathRatio(int pkp, int pld) {
        double ratio = 0.000;
        if (pld == 0) {
            pld++;
        }
        ratio = ((double) pkp) / ((double) pld);
        double result = ratio * 100;
        result = Math.round(result);
        result = result / 100;
        return result;
    }

    public void writeQuestTab() {
        //sendFrame126("@gre@Rune Republic Info",20064);
        sendFrame126("@gre@Rune Republic Displays", 663);
        sendFrame126("@red@" + client.pkp, 19162);
        sendFrame126("@red@" + client.pkd, 19164);
        sendFrame126("@red@" + calculateKillDeathRatio(client.pkp, client.pkd), 19166);
        //sendFrame126("@gre@Players Online: @yel@"+PlayerManager.getSingleton().getPlayerCount(), 19159);
        sendFrame126("@red@Server Rules", 7334);//Demon Slaye
        for (int i = 16026; i <= 16125; i++) {
            sendFrame126("", i);
        }
        sendFrame126("Achievements", 16026);
        sendFrame126("Events Calendar", 16027);
        sendFrame126("Top Pker", 16028);
        sendFrame126("Client Settings", 16029);
        sendFrame126("Short Keys", 16030);
        sendFrame126("Current EP: @gre@" + client.EP + "%", 16031);
        sendFrame126("Current Kill Streak: @gre@" + client.killStreak, 16032);
        sendFrame126("Pk Points: @gre@" + client.pkpoints + "", 16033);
        sendFrame126("Vote Points: @gre@" + client.vPoints + "", 16034);
        sendFrame126("Donator Points: @gre@" + client.dPoints + "", 16035);
        sendFrame126("CastleWars Points: @gre@" + client.cwPoints, 16036);

        sendFrame126("", 7383);//Demon Slayer
        sendFrame126("", 7336);
        sendFrame126("Varrock Teleport", 13037);
        sendFrame126("Lumbridge Teleport", 13047);
        sendFrame126("Canifis Teleport", 13055);
        sendFrame126("Falador Teleport", 13063);
        sendFrame126("Camelot", 13089);
        sendFrame126("Ardougne Teleport", 13097);
        sendFrame126("A teleportation spell", 13090);
        sendFrame126("A teleportation spell", 13098);
        sendFrame126("Varrock Teleport", 1300);
        sendFrame126("A teleportation spell", 1301);
        sendFrame126("Varrock Teleport", 21833);
        sendFrame126("A teleportation spell", 21834);
        sendFrame126("Lumbridge Teleport", 1325);
        sendFrame126("A teleportation spell", 1326);
        sendFrame126("Special Teleports", 22232);
        sendFrame126("Camelot Teleport", 22415);
        sendFrame126("Ardougne Teleport", 22490);
        sendFrame126("Ice Plateau", 22307);
        sendFrame126("Special Teleports", 18472);
        sendFrame126("A teleportation spell", 18473);
        sendFrame126("Lumbridge Teleport", 21933);
        sendFrame126("A teleportation spell", 21934);
        sendFrame126("Falador Teleport", 22123);

        sendFrame126("Falador Teleport", 1350);
        sendFrame126("A teleportation spell", 1351);
        sendFrame126("Camelot Teleport", 1382);
        sendFrame126("A teleportation spell", 1383);
        sendFrame126("Ardougne Teleport", 1415);
        sendFrame126("A teleportation spell", 1416);
        sendFrame126("Home Teleport", 7457);
        sendFrame126("A teleportation spell", 7458);

        sendFrame126("A teleportation spell", 18473);
        sendFrame126("Watchtower Teleport", 1454);
        sendFrame126("A teleportation spell", 1455);
        sendFrame126("Special Teleports", 13071);
        sendFrame126("A teleportation spell", 13072);
        sendFrame126("Ice Plateau", 13081);
        sendFrame126("A teleportation spell", 13082);
        sendFrame126("Pk: " + client.pkpoints, 3985);
    }

    public void levelUp(int skill) {
        int totalLevel = 0;
        if (Config.v13) {
            totalLevel = (getLevelForXP(client.playerXP[0]) + getLevelForXP(client.playerXP[1]) + getLevelForXP(client.playerXP[2]) + getLevelForXP(client.playerXP[3]) + getLevelForXP(client.playerXP[4]) + getLevelForXP(client.playerXP[5]) + getLevelForXP(client.playerXP[6]) + getLevelForXP(client.playerXP[7]) + getLevelForXP(client.playerXP[8]) + getLevelForXP(client.playerXP[9]) + getLevelForXP(client.playerXP[10]) + getLevelForXP(client.playerXP[11]) + getLevelForXP(client.playerXP[12]) + getLevelForXP(client.playerXP[13]) + getLevelForXP(client.playerXP[14]) + getLevelForXP(client.playerXP[15]) + getLevelForXP(client.playerXP[16]) + getLevelForXP(client.playerXP[17]) + getLevelForXP(client.playerXP[18]) + getLevelForXP(client.playerXP[19]) + getLevelForXP(client.playerXP[20]) + getLevelForXP(client.playerXP[21]) + getLevelForXP(client.playerXP[22]) + getLevelForXP(client.playerXP[23]) + getLevelForXP(client.playerXP[24]));
				/*sendFrame126("Total Level:", 31199);
				sendFrame126(""+totalLevel+"", 31200);*/
            sendFrame126("Total Level: " + totalLevel, 3984);
        } else {
            totalLevel = (getLevelForXP(client.playerXP[0]) + getLevelForXP(client.playerXP[1]) + getLevelForXP(client.playerXP[2]) + getLevelForXP(client.playerXP[3]) + getLevelForXP(client.playerXP[4]) + getLevelForXP(client.playerXP[5]) + getLevelForXP(client.playerXP[6]) + getLevelForXP(client.playerXP[7]) + getLevelForXP(client.playerXP[8]) + getLevelForXP(client.playerXP[9]) + getLevelForXP(client.playerXP[10]) + getLevelForXP(client.playerXP[11]) + getLevelForXP(client.playerXP[12]) + getLevelForXP(client.playerXP[13]) + getLevelForXP(client.playerXP[14]) + getLevelForXP(client.playerXP[15]) + getLevelForXP(client.playerXP[16]) + getLevelForXP(client.playerXP[17]) + getLevelForXP(client.playerXP[18]) + getLevelForXP(client.playerXP[19]) + getLevelForXP(client.playerXP[20]));
            sendFrame126("Total Lvl: " + totalLevel, 3984);
        }
        if (totalLevel == 2079) {
            client.getNRAchievements().checkSkilling(client, 10);
        }
        if (client.getCombatLevel() == 126) {
            client.getNRAchievements().checkCombat(client, 12);
        }
        switch (skill) {
            case 0:
                sendFrame126("Congratulations, you just advanced an attack level!", 6248);
                sendFrame126("Your attack level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6249);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced an attack level.");
                sendFrame164(6247);
                break;

            case 1:
                sendFrame126("Congratulations, you just advanced a defence level!", 6254);
                sendFrame126("Your defence level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6255);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a defence level.");
                sendFrame164(6253);
                break;

            case 2:
                sendFrame126("Congratulations, you just advanced a strength level!", 6207);
                sendFrame126("Your strength level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6208);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a strength level.");
                sendFrame164(6206);
                break;

            case 3:
                sendFrame126("Congratulations, you just advanced a hitpoints level!", 6217);
                sendFrame126("Your hitpoints level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6218);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a hitpoints level.");
                sendFrame164(6216);
                break;

            case 4:
                sendFrame126("Congratulations, you just advanced a ranged level!", 5453);
                sendFrame126("Your ranged level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6114);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a ranging level.");
                sendFrame164(4443);
                break;

            case 5:
                sendFrame126("Congratulations, you just advanced a prayer level!", 6243);
                sendFrame126("Your prayer level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6244);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a prayer level.");
                sendFrame164(6242);
                break;

            case 6:
                sendFrame126("Congratulations, you just advanced a magic level!", 6212);
                sendFrame126("Your magic level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6213);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a magic level.");
                sendFrame164(6211);
                break;

            case 7:
                sendFrame126("Congratulations, you just advanced a cooking level!", 6227);
                sendFrame126("Your cooking level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6228);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a cooking level.");
                sendFrame164(6226);
                break;

            case 8:
                sendFrame126("Congratulations, you just advanced a woodcutting level!", 4273);
                sendFrame126("Your woodcutting level is now " + getLevelForXP(client.playerXP[skill]) + ".", 4274);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a woodcutting level.");
                sendFrame164(4272);
                break;

            case 9:
                sendFrame126("Congratulations, you just advanced a fletching level!", 6232);
                sendFrame126("Your fletching level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6233);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a fletching level.");
                sendFrame164(6231);
                break;

            case 10:
                sendFrame126("Congratulations, you just advanced a fishing level!", 6259);
                sendFrame126("Your fishing level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6260);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a fishing level.");
                sendFrame164(6258);
                break;

            case 11:
                sendFrame126("Congratulations, you just advanced a fire making level!", 4283);
                sendFrame126("Your firemaking level is now " + getLevelForXP(client.playerXP[skill]) + ".", 4284);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a fire making level.");
                sendFrame164(4282);
                break;

            case 12:
                sendFrame126("Congratulations, you just advanced a crafting level!", 6264);
                sendFrame126("Your crafting level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6265);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a crafting level.");
                sendFrame164(6263);
                break;

            case 13:
                sendFrame126("Congratulations, you just advanced a smithing level!", 6222);
                sendFrame126("Your smithing level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6223);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a smithing level.");
                sendFrame164(6221);
                break;

            case 14:
                sendFrame126("Congratulations, you just advanced a mining level!", 4417);
                sendFrame126("Your mining level is now " + getLevelForXP(client.playerXP[skill]) + ".", 4438);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a mining level.");
                sendFrame164(4416);
                break;

            case 15:
                sendFrame126("Congratulations, you just advanced a herblore level!", 6238);
                sendFrame126("Your herblore level is now " + getLevelForXP(client.playerXP[skill]) + ".", 6239);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a herblore level.");
                if (getLevelForXP(client.playerXP[skill]) == 99) {
                    client.getNRAchievements().checkSkilling(client, 11);
                }

                sendFrame164(6237);
                break;

            case 16:
                sendFrame126("Congratulations, you just advanced a agility level!", 4278);
                sendFrame126("Your agility level is now " + getLevelForXP(client.playerXP[skill]) + ".", 4279);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced an agility level.");
                if (getLevelForXP(client.playerXP[skill]) == 99) {
                    client.getNRAchievements().checkCombat(client, 18);
                }
                sendFrame164(4277);
                break;

            case 17:
                sendFrame126("Congratulations, you just advanced a thieving level!", 4263);
                sendFrame126("Your theiving level is now " + getLevelForXP(client.playerXP[skill]) + ".", 4264);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a thieving level.");
                sendFrame164(4261);
                break;

            case 18:
                try {
                    sendFrame126("Congratulations, you just advanced a slayer level!", 12123);
                    sendFrame126("Your slayer level is now " + getLevelForXP(client.playerXP[skill]) + ".", 12124);
                    client.getActionAssistant().sendMessage("Congratulations, you just advanced a slayer level.");
                    sendFrame164(12122);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 20:
                sendFrame126("Congratulations, you just advanced a runecrafting level!", 4268);
                sendFrame126("Your runecrafting level is now " + getLevelForXP(client.playerXP[skill]) + ".", 4269);
                client.getActionAssistant().sendMessage("Congratulations, you just advanced a runecrafting level.");
                sendFrame164(4267);
                break;

        }
        PlayerManager.getSingleton().saveGame(client);
    }


    public void refreshSkill(int i) {
			/*if(Config.v13){
				switch (i) {
			case 0://attack
			sendFrame126("" + client.playerLevel[0] + "", 31114);
			sendFrame126("" + getLevelForXP(client.playerXP[0]) + "", 31115);
			sendFrame126("Exp: "+ client.playerXP[0]+ "", 31113);
			//System.out.println("wtf");
			break;
			
			case 1://defence
			sendFrame126("" + client.playerLevel[1] + "", 31124);
			sendFrame126("" + getLevelForXP(client.playerXP[1]) + "", 31125);
			sendFrame126("Exp: " + client.playerXP[1] + "", 31123);
			break;
			
			case 2://strength
			sendFrame126("" + client.playerLevel[2] + "", 31119);
			sendFrame126("" + getLevelForXP(client.playerXP[2]) + "", 31120);
			sendFrame126("Exp: " + client.playerXP[2] + "", 31118);
			break;
			
			case 3://hitpoints
			sendFrame126("" + client.playerLevel[3] + "", 31159);
			sendFrame126("" + getLevelForXP(client.playerXP[3]) + "", 31160);
			sendFrame126("Exp: " + client.playerXP[3] + "", 31158);
			break;
			
			case 4://ranged
			sendFrame126("" + client.playerLevel[4] + "", 31129);
			sendFrame126("" + getLevelForXP(client.playerXP[4]) + "", 31130);
			sendFrame126("Exp: " + client.playerXP[4] + "", 31128);
			break;
			
			case 5://prayer
			sendFrame126("" + client.playerLevel[5] + "", 31134);
			sendFrame126("" + getLevelForXP(client.playerXP[5]) + "", 31135);
			sendFrame126("Exp: " + client.playerXP[5] + "", 31133);
			sendFrame126("" +client.playerLevel[5]+"/"+getLevelForXP(client.playerXP[5])+"", 687);//Prayer frame
			break;
			
			case 6://magic
			sendFrame126("" + client.playerLevel[6] + "", 31139);
			sendFrame126("" + getLevelForXP(client.playerXP[6]) + "", 31140);
			sendFrame126("Exp: " + client.playerXP[6] + "", 31138);
			break;
			
			case 7://cooking
			sendFrame126("" + client.playerLevel[7] + "", 31219);
			sendFrame126("" + getLevelForXP(client.playerXP[7]) + "", 31220);
			sendFrame126("Exp:" + client.playerXP[7] + "", 31218);
			break;
			
			case 8://woodcutting
			sendFrame126("" + client.playerLevel[8] + "", 31229);
			sendFrame126("" + getLevelForXP(client.playerXP[8]) + "", 31230);
			sendFrame126("Exp: " + client.playerXP[8] + "", 31228);
			break;
			
			case 9://fletching
			sendFrame126("" + client.playerLevel[9] + "", 31184);
			sendFrame126("" + getLevelForXP(client.playerXP[9]) + "", 31185);
			sendFrame126("Exp: " + client.playerXP[9] + "", 31183);
			break;
			
			case 10://fishing
			sendFrame126("" + client.playerLevel[10] + "", 31214);
			sendFrame126("" + getLevelForXP(client.playerXP[10]) + "", 31215);
			sendFrame126("Exp: " + client.playerXP[10] + "", 31213);
			break;
			
			case 11://firemaking
			sendFrame126("" + client.playerLevel[11] + "", 31224);
			sendFrame126("" + getLevelForXP(client.playerXP[11]) + "", 31225);
			sendFrame126("Exp: " + client.playerXP[11] + "", 31223);
			break;
			
			case 12://crafting
			sendFrame126("" + client.playerLevel[12] + "", 31179);
			sendFrame126("" + getLevelForXP(client.playerXP[12]) + "", 31180);
			sendFrame126("Exp: " + client.playerXP[12] + "", 31178);
			break;
			
			case 13://smithing
			sendFrame126("" + client.playerLevel[13] + "", 31209);
			sendFrame126("" + getLevelForXP(client.playerXP[13]) + "", 31210);
			sendFrame126("Exp: " + client.playerXP[13] + "", 31208);
			break;
			
			case 14://mining
			sendFrame126("" + client.playerLevel[14] + "", 31204);
			sendFrame126("" + getLevelForXP(client.playerXP[14]) + "", 31205);
			sendFrame126("Exp: " + client.playerXP[14] + "", 31203);
			break;
			
			case 15://herblore
			sendFrame126("" + client.playerLevel[15] + "", 31169);
			sendFrame126("" + getLevelForXP(client.playerXP[15]) + "", 31170);
			sendFrame126("Exp: " + client.playerXP[15] + "", 31168);
			break;
			
			case 16://agility
			sendFrame126("" + client.playerLevel[16] + "", 31164);
			sendFrame126("" + getLevelForXP(client.playerXP[16]) + "", 31165);
			sendFrame126("Exp: " + client.playerXP[16] + "", 31163);
			break;
			
			case 17://thieving
			sendFrame126("" + client.playerLevel[17] + "", 31174);
			sendFrame126("" + getLevelForXP(client.playerXP[17]) + "", 31175);
			sendFrame126("Exp: " + client.playerXP[17] + "", 31173);
			break;
			
			case 18://slayer
			sendFrame126("" + client.playerLevel[18] + "", 31189);
			sendFrame126("" + getLevelForXP(client.playerXP[18]) + "", 31190);
			sendFrame126("Exp: " + client.playerXP[18] + "", 31188);
			break;
			
			case 19://farming
			sendFrame126("" + client.playerLevel[19] + "", 31234);
			sendFrame126("" + getLevelForXP(client.playerXP[19]) + "", 31235);
			sendFrame126("Exp: " + client.playerXP[19] + "", 31233);
			break;
			
			case 20://runecrfating
			sendFrame126("" + client.playerLevel[20] + "", 31144);
			sendFrame126("" + getLevelForXP(client.playerXP[20]) + "", 31145);
			sendFrame126("Exp: " + client.playerXP[20] + "", 31143);
			break;

			case 21://dungeoneering
			sendFrame126("" + client.playerLevel[21] + "", 31154);
			sendFrame126("" + getLevelForXP(client.playerXP[21]) + "", 31155);
			sendFrame126("Exp: " + client.playerXP[21] + "", 31153);
			break;

			case 22://hunter
			sendFrame126("" + client.playerLevel[22] + "", 31194);
			sendFrame126("" + getLevelForXP(client.playerXP[22]) + "", 31195);
			sendFrame126("Exp: " + client.playerXP[22] + "", 31193);
			break;

			case 23://summoning
			sendFrame126("" + client.playerLevel[23] + "", 31239);
			sendFrame126("" + getLevelForXP(client.playerXP[23]) + "", 31240);
			sendFrame126("Exp: " + client.playerXP[23] + "", 31238);
			break;

			case 24://construction
			sendFrame126("" + client.playerLevel[24] + "", 31149);
			sendFrame126("" + getLevelForXP(client.playerXP[24]) + "", 31150);
			sendFrame126("Exp: " + client.playerXP[24] + "", 31148);
			break;
		}
				*/
        if (Config.v13) {
            switch (i) {
                case 0:
                    sendFrame126("" + client.playerLevel[0] + "", 4004);
                    sendFrame126("" + getLevelForXP(client.playerXP[0]) + "", 4005);
                    sendFrame126("" + client.playerXP[0] + "", 4044);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[0]) + 1) + "", 4045);
                    break;

                case 1:
                    sendFrame126("" + client.playerLevel[1] + "", 4008);
                    sendFrame126("" + getLevelForXP(client.playerXP[1]) + "", 4009);
                    sendFrame126("" + client.playerXP[1] + "", 4056);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[1]) + 1) + "", 4057);
                    break;

                case 2:
                    sendFrame126("" + client.playerLevel[2] + "", 4006);
                    sendFrame126("" + getLevelForXP(client.playerXP[2]) + "", 4007);
                    sendFrame126("" + client.playerXP[2] + "", 4050);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[2]) + 1) + "", 4051);
                    break;

                case 3:
                    sendFrame126("" + client.playerLevel[3] + "", 4016);
                    sendFrame126("" + getLevelForXP(client.playerXP[3]) + "", 4017);
                    sendFrame126("" + client.playerXP[3] + "", 4080);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[3]) + 1) + "", 4081);
                    break;

                case 4:
                    sendFrame126("" + client.playerLevel[4] + "", 4010);
                    sendFrame126("" + getLevelForXP(client.playerXP[4]) + "", 4011);
                    sendFrame126("" + client.playerXP[4] + "", 4062);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[4]) + 1) + "", 4063);
                    break;

                case 5:
                    sendFrame126("" + client.playerLevel[5] + "", 4012);
                    sendFrame126("" + getLevelForXP(client.playerXP[5]) + "", 4013);
                    sendFrame126("" + client.playerXP[5] + "", 4068);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[5]) + 1) + "", 4069);
                    sendFrame126("Prayer: " + client.playerLevel[5] + "/" + getLevelForXP(client.playerXP[5]) + "", 687);//Prayer frame
                    break;

                case 6:
                    sendFrame126("" + client.playerLevel[6] + "", 4014);
                    sendFrame126("" + getLevelForXP(client.playerXP[6]) + "", 4015);
                    sendFrame126("" + client.playerXP[6] + "", 4074);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[6]) + 1) + "", 4075);
                    break;

                case 7:
                    sendFrame126("" + client.playerLevel[7] + "", 4034);
                    sendFrame126("" + getLevelForXP(client.playerXP[7]) + "", 4035);
                    sendFrame126("" + client.playerXP[7] + "", 4134);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[7]) + 1) + "", 4135);
                    break;

                case 8:
                    sendFrame126("" + client.playerLevel[8] + "", 4038);
                    sendFrame126("" + getLevelForXP(client.playerXP[8]) + "", 4039);
                    sendFrame126("" + client.playerXP[8] + "", 4146);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[8]) + 1) + "", 4147);
                    break;

                case 9:
                    sendFrame126("" + client.playerLevel[9] + "", 4026);
                    sendFrame126("" + getLevelForXP(client.playerXP[9]) + "", 4027);
                    sendFrame126("" + client.playerXP[9] + "", 4110);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[9]) + 1) + "", 4111);
                    break;

                case 10:
                    sendFrame126("" + client.playerLevel[10] + "", 4032);
                    sendFrame126("" + getLevelForXP(client.playerXP[10]) + "", 4033);
                    sendFrame126("" + client.playerXP[10] + "", 4128);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[10]) + 1) + "", 4129);
                    break;

                case 11:
                    sendFrame126("" + client.playerLevel[11] + "", 4036);
                    sendFrame126("" + getLevelForXP(client.playerXP[11]) + "", 4037);
                    sendFrame126("" + client.playerXP[11] + "", 4140);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[11]) + 1) + "", 4141);
                    break;

                case 12:
                    sendFrame126("" + client.playerLevel[12] + "", 4024);
                    sendFrame126("" + getLevelForXP(client.playerXP[12]) + "", 4025);
                    sendFrame126("" + client.playerXP[12] + "", 4104);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[12]) + 1) + "", 4105);
                    break;

                case 13:
                    sendFrame126("" + client.playerLevel[13] + "", 4030);
                    sendFrame126("" + getLevelForXP(client.playerXP[13]) + "", 4031);
                    sendFrame126("" + client.playerXP[13] + "", 4122);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[13]) + 1) + "", 4123);
                    break;

                case 14:
                    sendFrame126("" + client.playerLevel[14] + "", 4028);
                    sendFrame126("" + getLevelForXP(client.playerXP[14]) + "", 4029);
                    sendFrame126("" + client.playerXP[14] + "", 4116);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[14]) + 1) + "", 4117);
                    break;

                case 15:
                    sendFrame126("" + client.playerLevel[15] + "", 4020);
                    sendFrame126("" + getLevelForXP(client.playerXP[15]) + "", 4021);
                    sendFrame126("" + client.playerXP[15] + "", 4092);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[15]) + 1) + "", 4093);
                    break;

                case 16:
                    sendFrame126("" + client.playerLevel[16] + "", 4018);
                    sendFrame126("" + getLevelForXP(client.playerXP[16]) + "", 4019);
                    sendFrame126("" + client.playerXP[16] + "", 4086);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[16]) + 1) + "", 4087);
                    break;

                case 17:
                    sendFrame126("" + client.playerLevel[17] + "", 4022);
                    sendFrame126("" + getLevelForXP(client.playerXP[17]) + "", 4023);
                    sendFrame126("" + client.playerXP[17] + "", 4098);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[17]) + 1) + "", 4099);
                    break;

                case 18:
                    sendFrame126("" + client.playerLevel[18] + "", 12166);
                    sendFrame126("" + getLevelForXP(client.playerXP[18]) + "", 12167);
                    sendFrame126("" + client.playerXP[18] + "", 12171);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[18]) + 1) + "", 12172);
                    break;

                case 19:
                    sendFrame126("" + client.playerLevel[19] + "", 13926);
                    sendFrame126("" + getLevelForXP(client.playerXP[19]) + "", 13927);
                    sendFrame126("" + client.playerXP[19] + "", 13921);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[19]) + 1) + "", 13922);
                    break;

                case 20:
                    sendFrame126("" + client.playerLevel[20] + "", 4152);
                    sendFrame126("" + getLevelForXP(client.playerXP[20]) + "", 4153);
                    sendFrame126("" + client.playerXP[20] + "", 4157);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[20]) + 1) + "", 4158);
                    break;

                case 21://dungeoneering
                    sendFrame126("" + client.playerLevel[21] + "", 18168);
                    sendFrame126("" + getLevelForXP(client.playerXP[21]) + "", 18172);
                    sendFrame126("" + client.playerXP[21] + "", 4157);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[21]) + 1) + "", 4158);
                    break;

                case 22://hunter
                    sendFrame126("" + client.playerLevel[22] + "", 18166);
                    sendFrame126("" + getLevelForXP(client.playerXP[22]) + "", 18170);
                    sendFrame126("" + client.playerXP[22] + "", 4157);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[22]) + 1) + "", 4158);
                    break;

                case 23://summoning
                    sendFrame126("" + client.playerLevel[23] + "", 18167);
                    sendFrame126("" + getLevelForXP(client.playerXP[23]) + "", 18171);
                    sendFrame126("" + client.playerXP[23] + "", 4157);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[23]) + 1) + "", 4158);
                    break;

                case 24://construction
                    sendFrame126("" + client.playerLevel[24] + "", 18165);
                    sendFrame126("" + getLevelForXP(client.playerXP[24]) + "", 18169);
                    sendFrame126("" + client.playerXP[24] + "", 4157);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[24]) + 1) + "", 4158);
                    break;
            }
        } else {
            switch (i) {
                case 0:
                    sendFrame126("" + client.playerLevel[0] + "", 4004);
                    sendFrame126("" + getLevelForXP(client.playerXP[0]) + "", 4005);
                    sendFrame126("" + client.playerXP[0] + "", 4044);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[0]) + 1) + "", 4045);
                    break;

                case 1:
                    sendFrame126("" + client.playerLevel[1] + "", 4008);
                    sendFrame126("" + getLevelForXP(client.playerXP[1]) + "", 4009);
                    sendFrame126("" + client.playerXP[1] + "", 4056);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[1]) + 1) + "", 4057);
                    break;

                case 2:
                    sendFrame126("" + client.playerLevel[2] + "", 4006);
                    sendFrame126("" + getLevelForXP(client.playerXP[2]) + "", 4007);
                    sendFrame126("" + client.playerXP[2] + "", 4050);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[2]) + 1) + "", 4051);
                    break;

                case 3:
                    sendFrame126("" + client.playerLevel[3] + "", 4016);
                    sendFrame126("" + getLevelForXP(client.playerXP[3]) + "", 4017);
                    sendFrame126("" + client.playerXP[3] + "", 4080);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[3]) + 1) + "", 4081);
                    break;

                case 4:
                    sendFrame126("" + client.playerLevel[4] + "", 4010);
                    sendFrame126("" + getLevelForXP(client.playerXP[4]) + "", 4011);
                    sendFrame126("" + client.playerXP[4] + "", 4062);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[4]) + 1) + "", 4063);
                    break;

                case 5:
                    sendFrame126("" + client.playerLevel[5] + "", 4012);
                    sendFrame126("" + getLevelForXP(client.playerXP[5]) + "", 4013);
                    sendFrame126("" + client.playerXP[5] + "", 4068);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[5]) + 1) + "", 4069);
                    sendFrame126("Prayer: " + client.playerLevel[5] + "/" + getLevelForXP(client.playerXP[5]) + "", 687);//Prayer frame
                    break;

                case 6:
                    sendFrame126("" + client.playerLevel[6] + "", 4014);
                    sendFrame126("" + getLevelForXP(client.playerXP[6]) + "", 4015);
                    sendFrame126("" + client.playerXP[6] + "", 4074);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[6]) + 1) + "", 4075);
                    break;

                case 7:
                    sendFrame126("" + client.playerLevel[7] + "", 4034);
                    sendFrame126("" + getLevelForXP(client.playerXP[7]) + "", 4035);
                    sendFrame126("" + client.playerXP[7] + "", 4134);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[7]) + 1) + "", 4135);
                    break;

                case 8:
                    sendFrame126("" + client.playerLevel[8] + "", 4038);
                    sendFrame126("" + getLevelForXP(client.playerXP[8]) + "", 4039);
                    sendFrame126("" + client.playerXP[8] + "", 4146);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[8]) + 1) + "", 4147);
                    break;

                case 9:
                    sendFrame126("" + client.playerLevel[9] + "", 4026);
                    sendFrame126("" + getLevelForXP(client.playerXP[9]) + "", 4027);
                    sendFrame126("" + client.playerXP[9] + "", 4110);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[9]) + 1) + "", 4111);
                    break;

                case 10:
                    sendFrame126("" + client.playerLevel[10] + "", 4032);
                    sendFrame126("" + getLevelForXP(client.playerXP[10]) + "", 4033);
                    sendFrame126("" + client.playerXP[10] + "", 4128);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[10]) + 1) + "", 4129);
                    break;

                case 11:
                    sendFrame126("" + client.playerLevel[11] + "", 4036);
                    sendFrame126("" + getLevelForXP(client.playerXP[11]) + "", 4037);
                    sendFrame126("" + client.playerXP[11] + "", 4140);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[11]) + 1) + "", 4141);
                    break;

                case 12:
                    sendFrame126("" + client.playerLevel[12] + "", 4024);
                    sendFrame126("" + getLevelForXP(client.playerXP[12]) + "", 4025);
                    sendFrame126("" + client.playerXP[12] + "", 4104);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[12]) + 1) + "", 4105);
                    break;

                case 13:
                    sendFrame126("" + client.playerLevel[13] + "", 4030);
                    sendFrame126("" + getLevelForXP(client.playerXP[13]) + "", 4031);
                    sendFrame126("" + client.playerXP[13] + "", 4122);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[13]) + 1) + "", 4123);
                    break;

                case 14:
                    sendFrame126("" + client.playerLevel[14] + "", 4028);
                    sendFrame126("" + getLevelForXP(client.playerXP[14]) + "", 4029);
                    sendFrame126("" + client.playerXP[14] + "", 4116);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[14]) + 1) + "", 4117);
                    break;

                case 15:
                    sendFrame126("" + client.playerLevel[15] + "", 4020);
                    sendFrame126("" + getLevelForXP(client.playerXP[15]) + "", 4021);
                    sendFrame126("" + client.playerXP[15] + "", 4092);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[15]) + 1) + "", 4093);
                    break;

                case 16:
                    sendFrame126("" + client.playerLevel[16] + "", 4018);
                    sendFrame126("" + getLevelForXP(client.playerXP[16]) + "", 4019);
                    sendFrame126("" + client.playerXP[16] + "", 4086);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[16]) + 1) + "", 4087);
                    break;

                case 17:
                    sendFrame126("" + client.playerLevel[17] + "", 4022);
                    sendFrame126("" + getLevelForXP(client.playerXP[17]) + "", 4023);
                    sendFrame126("" + client.playerXP[17] + "", 4098);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[17]) + 1) + "", 4099);
                    break;

                case 18:
                    sendFrame126("" + client.playerLevel[18] + "", 12166);
                    sendFrame126("" + getLevelForXP(client.playerXP[18]) + "", 12167);
                    sendFrame126("" + client.playerXP[18] + "", 12171);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[18]) + 1) + "", 12172);
                    break;

                case 19:
                    sendFrame126("" + client.playerLevel[19] + "", 13926);
                    sendFrame126("" + getLevelForXP(client.playerXP[19]) + "", 13927);
                    sendFrame126("" + client.playerXP[19] + "", 13921);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[19]) + 1) + "", 13922);
                    break;

                case 20:
                    sendFrame126("" + client.playerLevel[20] + "", 4152);
                    sendFrame126("" + getLevelForXP(client.playerXP[20]) + "", 4153);
                    sendFrame126("" + client.playerXP[20] + "", 4157);
                    sendFrame126("" + getXPForLevel(getLevelForXP(client.playerXP[20]) + 1) + "", 4158);
                    break;

            }


        }
    }

    /**
     * Burying bones
     */

    public void buryBone(int buryXp, int delay, int itemId, int itemSlot) {
        if (client.inDuelArena()) {
            return;
        }
        client.attackTimer += 1400;
        if (System.currentTimeMillis() - client.buryDelay > delay) {
            if (playerHasItem(itemId, 1, itemSlot)) {
                client.buryDelay = System.currentTimeMillis();
                addSkillXP(buryXp, 5);
                startAnimation(827);
                deleteItem(itemId, itemSlot, 1);
                sendMessage("You bury the " + Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase() + ".");
                refreshSkill(5);
                requestUpdates();
                if (client.sounds == 1) {
                    frame174(1104, 050, 000);
                }
            }
        }
    }

    /**
     * Show option, attack, trade, follow etc
     */
    public String optionType = "null";

    public void showOption(int i, int l, String s, int a) {
        if (!optionType.equalsIgnoreCase(s)) {
            optionType = s;
            client.getOutStream().createFrameVarSize(104);
            client.getOutStream().writeByteC(i);
            client.getOutStream().writeByteA(l);
            client.getOutStream().writeString(s);
            client.getOutStream().endFrameVarSize();
            client.flushOutStream();
        }
    }

    public void clanChatMenu() {
        sendQuest("", 6272);
        sendQuest("", 6271);
        sendQuest("", 9926);
        sendQuest("Talking in: @whi@" + client.clanName, 18139);
        sendQuest("Owner: @whi@" + client.clanLeader, 18140);
        sendQuest("", 18144);
        sendQuest("", 18244);
        sendQuest("", 16533);
        sendQuest("", 16534);

        for (int i = 18144; i <= 18244; i++) {
            if (client.clanMembers[i - 18144] != null) {
                sendQuest("@whi@" + client.clanMembers[i - 18144], client.CCID[i - 18144]);
            } else {
                sendQuest("", client.CCID[i - 18144]);
            }
        }
    }

    public void updateCCMenu() {
        // for (Player p : Server.getPlayerManager().getPlayers()) {
        // if(p != null) {
        // Client person = (Client)p;
        // if((person.playerName != null || person.playerName != "null")) {
        // Client c2 = (Client) Server.getPlayerManager().getPlayerByName(person.clanLeader);
        // if(person.clanLeader == client.clanLeader) {
        // person.getActionAssistant().sendQuest("Talking in: @yel@" + person.clanName, 18139);
        // person.getActionAssistant().sendQuest("Owner: @yel@" + person.clanLeader, 18140);
        // for(int i=18144; i<=18244; i++) {
        // if(c2.clanMembers[i-18144] != null) {
        // person.getActionAssistant().sendQuest("@whi@"+c2.clanMembers[i-18144], client.CCID[i-18144]);
        // } else {
        // person.getActionAssistant().sendQuest("", client.CCID[i-18144]);
        // }
        // }
        // }
        // }
        // }

    }

    public void removeFromCC() {
        if (client != null) {
            Client c2 = (Client) Server.getPlayerManager().getPlayerByName(client.clanLeader);
            for (int i = 16502; i <= 16532; i++) {
                if (c2.clanMembers[i - 16502] == client.playerName) {
                    c2.clanMembers[i - 16502] = null;
                    updateCCMenu();
                    break;
                }
            }
            client.clanName = "None";
            client.clanLeader = "Nobody";
            clanChatMenu();
        }
    }


    public void sendClanMessage(Client sender, String msg) {
        for (Player p : Server.getPlayerManager().getPlayers()) {
            if (p != null) {
                Client person = (Client) p;
                if ((person.playerName != null || person.playerName != "null")) {
                    if (person.clanLeader == client.clanLeader) {
                        person.getActionAssistant().sendMessage("[@blu@" + sender.clanName + "@bla@] " + sender.playerName + ":@dre@ " + msg);
                    }
                }
            }
        }
    }


    public void sendItemsKept() {
        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(6963);
        client.getOutStream().writeWord(client.itemKeptId.length);
        for (int i = 0; i < client.itemKeptId.length; i++) {
            if (client.playerItemsN[i] > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord_v2(1);
            } else {
                client.getOutStream().writeByte(1);
            }
            if (client.itemKeptId[i] > 0) {
                client.getOutStream().writeWordBigEndianA(client.itemKeptId[i] + 1);
            } else {
                client.getOutStream().writeWordBigEndianA(0);
            }
        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();
    }


    /**
     * Item kept on death
     */

    public void keepItem(int keepItem, boolean deleteItem) {
        double value = 0;
        int item = 0;
        int slotId = 0;
        boolean itemInInventory = false;
        allItems.clear(); // ERIC
        client.mostExpensiveItem = 0; // ERIC
        client.anyItems = false; // ERIC
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] - 1 > 0) {
                client.anyItems = true;
                allItems.put(client.playerItems[i] - 1, client.invSlot[i]); // ERIC
                //int inventoryItemValue = c.getShops().getItemShopValue(c.playerItems[i] - 1);
                double inventoryItemValue = 1;
                try {
                    inventoryItemValue = Server.getItemManager().getItemDefinition(client.playerItems[i] - 1).getShopValue();
                } catch (Exception e) {
                }
                if (client.playerItems[i] - 1 == 6524) {
                    inventoryItemValue = 60000;
                } else if (client.playerItems[i] - 1 == 13668) {
                    inventoryItemValue = 6500001;
                } else if (client.playerItems[i] - 1 == 13660) {
                    inventoryItemValue = 6500002;
                } else if (client.playerItems[i] - 1 == 13661) {
                    inventoryItemValue = 6500003;
                } else if (client.playerItems[i] - 1 == 13662) {
                    inventoryItemValue = 6500004;
                } else if (client.playerItems[i] - 1 == 13663) {
                    inventoryItemValue = 6500005;
                } else if (client.playerItems[i] - 1 == 13664) {
                    inventoryItemValue = 6500006;
                } else if (client.playerItems[i] - 1 == 13665) {
                    inventoryItemValue = 6500007;
                } else if (client.playerItems[i] - 1 == 13666) {
                    inventoryItemValue = 6500008;
                } else if (client.playerItems[i] - 1 == 13667) {
                    inventoryItemValue = 6500009;
                } else if (client.playerItems[i] - 1 == 13669) {
                    inventoryItemValue = 6500010;
                } else if (client.playerItems[i] - 1 == 13670) {
                    inventoryItemValue = 6500011;
                } else if (client.playerItems[i] - 1 == 13671) {
                    inventoryItemValue = 6500012;
                } else if (client.playerItems[i] - 1 == 7806) {
                    inventoryItemValue = 5500000;
                } else if (client.playerItems[i] - 1 == 7807) {
                    inventoryItemValue = 5500000;
                } else if (client.playerItems[i] - 1 == 7808) {
                    inventoryItemValue = 5500000;
                } else if (client.playerItems[i] - 1 == 7809) {
                    inventoryItemValue = 5500000;
                } else if (client.playerItems[i] - 1 == 7822) {
                    inventoryItemValue = 20000000;
                } else if (client.playerItems[i] - 1 == 7901) {
                    inventoryItemValue = 35000000;
                } else if (client.playerItems[i] - 1 == 7462) {
                    inventoryItemValue = 5;
                } else if (client.playerItems[i] - 1 == 7461) {
                    inventoryItemValue = 4;
                } else if (client.playerItems[i] - 1 == 7460) {
                    inventoryItemValue = 3;
                } else if (client.playerItems[i] - 1 == 7459) {
                    inventoryItemValue = 2;
                }
                if (inventoryItemValue > value) client.mostExpensiveItem = client.playerItems[i] - 1; // ERIC
                if (inventoryItemValue > value && (!client.invSlot[i])) {
                    value = inventoryItemValue;
                    item = client.playerItems[i] - 1;
                    slotId = i;
                    itemInInventory = true;
                }
            }
        }
        for (int i1 = 0; i1 < client.playerEquipment.length; i1++) {
            if (client.playerEquipment[i1] > 0) {
                client.anyItems = true; // ERIC
                allItems.put(client.playerEquipment[i1], client.equipSlot[i1]); // ERIC
                double equipmentItemValue = 1;
                try {
                    equipmentItemValue = Server.getItemManager().getItemDefinition(client.playerEquipment[i1]).getShopValue();
                } catch (Exception e) {
                }
                if (client.playerEquipment[i1] == 6524) {
                    equipmentItemValue = 60000;
                } else if (client.playerEquipment[i1] == 13668) {
                    equipmentItemValue = 6500001;
                } else if (client.playerEquipment[i1] == 13660) {
                    equipmentItemValue = 6500002;
                } else if (client.playerEquipment[i1] == 13661) {
                    equipmentItemValue = 6500003;
                } else if (client.playerEquipment[i1] == 13662) {
                    equipmentItemValue = 6500004;
                } else if (client.playerEquipment[i1] == 13663) {
                    equipmentItemValue = 6500005;
                } else if (client.playerEquipment[i1] == 13664) {
                    equipmentItemValue = 6500006;
                } else if (client.playerEquipment[i1] == 13665) {
                    equipmentItemValue = 6500007;
                } else if (client.playerEquipment[i1] == 13666) {
                    equipmentItemValue = 6500008;
                } else if (client.playerEquipment[i1] == 13667) {
                    equipmentItemValue = 6500009;
                } else if (client.playerEquipment[i1] == 13669) {
                    equipmentItemValue = 6500010;
                } else if (client.playerEquipment[i1] == 13670) {
                    equipmentItemValue = 6500011;
                } else if (client.playerEquipment[i1] == 13671) {
                    equipmentItemValue = 6500012;
                } else if (client.playerEquipment[i1] == 7806) {
                    equipmentItemValue = 5500000;
                } else if (client.playerEquipment[i1] == 7807) {
                    equipmentItemValue = 5500000;
                } else if (client.playerEquipment[i1] == 7808) {
                    equipmentItemValue = 5500000;
                } else if (client.playerEquipment[i1] == 7809) {
                    equipmentItemValue = 5500000;
                } else if (client.playerEquipment[i1] == 7822) {
                    equipmentItemValue = 20000000;
                } else if (client.playerEquipment[i1] == 7901) {
                    equipmentItemValue = 35000000;
                } else if (client.playerEquipment[i1] == 7462) {
                    equipmentItemValue = 5;
                } else if (client.playerEquipment[i1] == 7461) {
                    equipmentItemValue = 4;
                } else if (client.playerEquipment[i1] == 7460) {
                    equipmentItemValue = 3;
                } else if (client.playerEquipment[i1] == 7459) {
                    equipmentItemValue = 2;
                }
                if (equipmentItemValue > value) client.mostExpensiveItem = client.playerEquipment[i1]; // ERIC
                if (equipmentItemValue > value && (!client.equipSlot[i1])) {
                    value = equipmentItemValue;
                    item = client.playerEquipment[i1];
                    slotId = i1;
                    itemInInventory = false;
                }
            }
        }
        if (itemInInventory) {
            client.invSlot[slotId] = true;
            if (deleteItem) {
                deleteItem(client.playerItems[slotId] - 1, getItemSlot(client.playerItems[slotId] - 1), client.playerItemsN[slotId]);
            }
        } else {
            client.equipSlot[slotId] = true;
            if (deleteItem) {
                deleteEquipment(item, slotId);
            }
        }
        client.itemKeptId[keepItem] = item;
    }

    /**
     * Reset items kept on death
     */

    public void resetKeepItems() {
        for (int i = 0; i < client.itemKeptId.length; i++) {
            client.itemKeptId[i] = -1;
        }
        for (int i1 = 0; i1 < client.invSlot.length; i1++) {
            client.invSlot[i1] = false;
        }
        for (int i2 = 0; i2 < client.equipSlot.length; i2++) {
            client.equipSlot[i2] = false;
        }
    }

    public boolean sendInventoryItem(int item, int amount, int slot) {

        if (item < 1 || item > Integer.MAX_VALUE)
            return false;

        if (!Item.itemStackable[item] && client.getActionAssistant().freeSlots() < 1) {
            client.getActionAssistant().sendMessage("You don't have enough space in your inventory.");
            return false;
        }

        /**
         * Add stackable item.
         */
        if (Item.itemStackable[item]) {
            if (client.getActionAssistant().freeSlots() > 1 || client.getActionAssistant().playerHasItem(item, 1)) {
                for (int i = 0; i < client.playerItems.length; i++) {
                    if (client.playerItems[i] == (item + 1)) {
                        if ((client.playerItemsN[i] + amount) < Integer.MAX_VALUE && (client.playerItemsN[i] + amount) > -1) {
                            client.playerItemsN[i] += amount;
                        } else {
                            client.getActionAssistant().sendMessage("You don't have enough space in your inventory.");
                            return false;
                        }
                        client.getOutStream().createFrameVarSizeWord(34);
                        client.getOutStream().writeWord(3214);
                        client.getOutStream().writeByte(i);
                        client.getOutStream().writeWord(client.playerItems[i]);
                        if (client.playerItemsN[i] > 254) {
                            client.getOutStream().writeByte(255);
                            client.getOutStream().writeDWord(client.playerItemsN[i]);
                        } else {
                            client.getOutStream().writeByte(client.playerItemsN[i]); // amount
                        }
                        client.getOutStream().endFrameVarSizeWord();
                        return true;
                    }
                }
            }
        }
        /**
         * Add single slot item.
         */
        boolean canAddToSlot = true;

        for (int i = 0; i < client.playerItems.length; i++) {
            if (i == slot) {
                if (client.playerItems[i] > 0) {
                    canAddToSlot = false;
                }
            }
        }
        if (canAddToSlot) {
            for (int i = 0; i < client.playerItems.length; i++) {
                if (i == slot) {
                    if ((client.playerItemsN[i] + amount) < Integer.MAX_VALUE && (client.playerItemsN[i] + amount) > -1) {
                        client.playerItems[i] = item + 1;
                        client.playerItemsN[i] += amount;
                    } else {
                        client.getActionAssistant().sendMessage("You don't have enough space in your inventory.");
                        return false;
                    }
                    client.getOutStream().createFrameVarSizeWord(34);
                    client.getOutStream().writeWord(3214);
                    client.getOutStream().writeByte(i);
                    client.getOutStream().writeWord(client.playerItems[i]);
                    if (client.playerItemsN[i] > 254) {
                        client.getOutStream().writeByte(255);
                        client.getOutStream().writeDWord(client.playerItemsN[i]);
                    } else {
                        client.getOutStream().writeByte(client.playerItemsN[i]); // amount						}
                        client.getOutStream().endFrameVarSizeWord();
                        client.flushOutStream();
                        return true;
                    }
                }
            }
        } else {
            for (int i = 0; i < client.playerItems.length; i++) {
                if (client.playerItems[i] < 1) {
                    if ((client.playerItemsN[i] + amount) < Integer.MAX_VALUE && (client.playerItemsN[i] + amount) > -1) {
                        client.playerItems[i] = item + 1;
                        client.playerItemsN[i] += amount;
                    } else {
                        client.getActionAssistant().sendMessage("You don't have enough space in your inventory.");
                        return false;
                    }
                    client.getOutStream().createFrameVarSizeWord(34);
                    client.getOutStream().writeWord(3214);
                    client.getOutStream().writeByte(i);
                    client.getOutStream().writeWord(client.playerItems[i]);
                    if (client.playerItemsN[i] > 254) {
                        client.getOutStream().writeByte(255);
                        client.getOutStream().writeDWord(client.playerItemsN[i]);
                    } else {
                        client.getOutStream().writeByte(client.playerItemsN[i]); // amount
                    }
                    client.getOutStream().endFrameVarSizeWord();
                    client.flushOutStream();
                    return true;
                }
            }
        }
        client.getActionAssistant().sendMessage("You don't have enough space in your inventory.");
        return false;
    }

    public void removeItem(int wearID, int slot) {
        if (client.getOutStream() != null && client != null) {
            if (client.playerEquipment[slot] > -1) {
                if (addItem(client.playerEquipment[slot], client.playerEquipmentN[slot])) {
                    if (client.playerEquipment[slot] == 3) {
                        client.autoCastMarker = false;
                        client.autoCast = false;
                    }
                    client.playerEquipment[slot] = -1;
                    client.playerEquipmentN[slot] = 0;
                    client.getCombat().calculateBonus();
                    client.getCombatEmotes().getPlayerAnimIndex();
                    client.getCombatFormulas().sendWeapon();
                    client.getOutStream().createFrame(34);
                    client.getOutStream().writeWord(6);
                    client.getOutStream().writeWord(1688);
                    client.getOutStream().writeByte(slot);
                    client.getOutStream().writeWord(0);
                    client.getOutStream().writeByte(0);
                    client.flushOutStream();
                    int hpBonus = getMaxHP();
                    if (client.playerLevel[3] >= (getLevelForXP(client.playerXP[3]) + hpBonus)) {
                        client.playerLevel[3] = getLevelForXP(client.playerXP[3]) + hpBonus;
                    }
                    client.updateRequired = true;
                    client.appearanceUpdateRequired = true;
                }
            }
        }
    }

    /**
     * delete all items
     */

    public void deleteAllItems() {
        for (int i1 = 0; i1 < client.playerEquipment.length; i1++) {
            if (client.playerEquipment[i1] == 6656) {

            } else {
                deleteEquipment(client.playerEquipment[i1], i1);
                //PlayerSave.saveGame(Server.PlayerManager.players[client.playerId]);
            }
        }
        for (int i = 0; i < client.playerItems.length; i++) {
            if (client.playerItems[i] - 1 == 6656) {

            } else {
                deleteItem(client.playerItems[i] - 1, getItemSlot(client.playerItems[i] - 1), client.playerItemsN[i]);
                //PlayerSave.saveGame(Server.PlayerManager.players[client.playerId]);
            }
        }
    }


    /**
     * Drop all items for your killer
     */
    public void dropItems() {
        client.setKillerid();
        Client highestHitter = (Client) PlayerManager.getSingleton().getPlayers()[client.killerId];
        client.killer.clear();
        client.killer.clear();
        //	String DeathItems[] = "";
        String temp = "";
        String temp2 = "";
        //		String DeathAmount[] = "";
        for (int i = 0; i < client.playerItems.length; i++) {
            //if(client.playerItems[i] != 7454 ||client.playerItems[i] != 7455 ||client.playerItems[i] != 7456 ||client.playerItems[i] != 7457 ||client.playerItems[i] != 7458 ||client.playerItems[i] != 7459){
            if (client.playerItems[i] == 7454 || client.playerItems[i] == 7455 ||
                    client.playerItems[i] == 7456 || client.playerItems[i] == 7457 ||
                    client.playerItems[i] == 7458 || client.playerItems[i] == 7459 ||
                    client.playerItems[i] == 7460 || client.playerItems[i] == 7461 ||
                    client.playerItems[i] == 7463 || client.playerItems[i] == 7462 || client.playerItems[i] == 7837


                    ) {
                deleteItem(client.playerItems[i] - 1, getItemSlot(client.playerItems[i] - 1), client.playerItemsN[i]);
            }
            if (client.playerItems[i] <= 0) continue;
            if (client.playerItems[i] == 6571 || client.playerItems[i] == 10552 ||
                    client.playerItems[i] == 431 || client.playerItems[i] == 8844 || client.playerItems[i] == 8845
                    || client.playerItems[i] == 8846 || client.playerItems[i] == 8840 || client.playerItems[i] == 8847 || client.playerItems[i] == 8848
                    || client.playerItems[i] == 8851 || client.playerItems[i] == 8849 || client.playerItems[i] == 7935 || client.playerItems[i] == 8850 || client.playerItems[i] == 20073
                    || client.playerItems[i] == 7971 || client.playerItems[i] == 7949 || client.playerItems[i] == 7950 || client.playerItems[i] == 8839 || client.playerItems[i] == 8840 || client.playerItems[i] == 8841 ||
                    client.playerItems[i] == 8842 || client.playerItems[i] == 8843 || (client.playerItems[i] >= 11664 && client.playerItems[i] <= 11667) ||
                    client.playerItems[i] == 6656 || (client.playerItems[i] >= 15015 && client.playerItems[i] <= 15021) || client.playerItems[i] == 10547 || client.playerItems[i] == 10549 || client.playerItems[i] == 15221 ||
                    client.playerItems[i] == 15221 ||
				/*	client.playerItems[i] == 13902 ||
			client.playerItems[i] == 13890 ||
			client.playerItems[i] == 13896 ||
			client.playerItems[i] == 13908 ||
			client.playerItems[i] == 13864 ||
			client.playerItems[i] == 13870 ||
			client.playerItems[i] == 13867 ||
			client.playerItems[i] == 13861 ||
			client.playerItems[i] == 13873 ||
			client.playerItems[i] == 13876 ||
			client.playerItems[i] == 13879 ||
			client.playerItems[i] == 13887 ||
			client.playerItems[i] == 13893 ||
			client.playerItems[i] == 13899 ||*/
                    client.playerItems[i] == 7837 ||
                    client.playerItems[i] == 7774 ||
                    client.playerItems[i] == 1464 ||

                    client.playerItems[i] == 13905 || client.playerItems[i] == 11664 || client.playerItems[i] == 8840 ||
                    client.playerItems[i] == 7924 || client.playerItems[i] == 8841 ||
                    client.playerItems[i] == 11664 || client.playerItems[i] == 7941 || client.playerItems[i] == 3108 ||
                    client.playerItems[i] == 11666 || client.playerItems[i] == 7943 || client.playerItems[i] == 19784 || client.playerItems[i] == 21407 || client.playerItems[i] == 20071 || client.playerItems[i] == 19749
                    || client.playerItems[i] == 20772 || client.playerItems[i] == 20771 || client.playerItems[i] == 20773
                    ) {

                FloorItem item = new FloorItem((client.playerItems[i] - 1), client.playerItemsN[i], client, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                if (client != null) {
                    //Server.getItemManager().newDrop(item, client);
                    if (
                            client.playerItems[i] == 7923 ||
                                    client.playerItems[i] == 7940 ||
                                    client.playerItems[i] == 7942) {
                        Server.getItemManager().newDrop(item, client);
                    } else {
                        client.al.add(client.playerItems[i] - 1);
                        client.alnum.add(client.playerItemsN[i]);
                    }
                }

            } else {
                FloorItem item = new FloorItem((client.playerItems[i] - 1), client.playerItemsN[i], highestHitter, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                temp += Integer.toString(client.playerItems[i] - 1) + ",";
                temp2 += Integer.toString(client.playerItemsN[i]) + ",";
                if (highestHitter != null) {
                    Server.getItemManager().newDrop(item, highestHitter);
                } else {
                    Server.getItemManager().newDrop(item, null);
                    Server.getItemManager().showDrop(item);
                }
            }
            client.playerItems[i] = 0;
            client.playerItemsN[i] = 1;
            //}
        }
        temp = null;
        temp2 = null;
        for (int i = 0; i < client.playerEquipment.length; i++) {
            if (client.playerEquipment[i] == 7454 || client.playerEquipment[i] == 7455 ||
                    client.playerEquipment[i] == 7456 || client.playerEquipment[i] == 7457 ||
                    client.playerEquipment[i] == 7458 || client.playerEquipment[i] == 7459 ||
                    client.playerEquipment[i] == 7460 || client.playerEquipment[i] == 7461 ||
                    client.playerEquipment[i] == 7462 || client.playerEquipment[i] == 7463
                    ) {
                deleteEquipment(client.playerEquipment[i], i);
            }
            if (client.playerEquipment[i] <= -1 || client.playerEquipment[i] <= 0) continue;
            if (client.playerEquipment[i] == 6570 || client.playerEquipment[i] == 6656 || client.playerEquipment[i] == 6571 || client.playerEquipment[i] == 10551 ||
                    client.playerEquipment[i] == 431 || client.playerEquipment[i] == 8844 || client.playerEquipment[i] == 8845
                    || client.playerEquipment[i] == 8846 || client.playerEquipment[i] == 8847 || client.playerEquipment[i] == 8848
                    || client.playerEquipment[i] == 8849 || client.playerEquipment[i] == 7935 || client.playerEquipment[i] == 8850
                    || client.playerEquipment[i] == 7971 || client.playerEquipment[i] == 7949 || client.playerEquipment[i] == 7950 ||
                    client.playerEquipment[i] == 11663 || client.playerEquipment[i] == 8839 || client.playerEquipment[i] == 3107 ||
                    client.playerEquipment[i] == 7923 || client.playerEquipment[i] == 8840 || client.playerEquipment[i] == 10548 || client.playerEquipment[i] == 10550 ||
                    client.playerEquipment[i] == 11664 || client.playerEquipment[i] == 7940 || client.playerEquipment[i] == 19785 || client.playerEquipment[i] == 19786 ||
                    (client.playerEquipment[i] >= 11664 && client.playerEquipment[i] <= 11667) || (client.playerEquipment[i] >= 8839 && client.playerEquipment[i] <= 8842) ||
                    client.playerEquipment[i] == 11665 || client.playerEquipment[i] == 7942 || client.playerEquipment[i] == 20072 || client.playerEquipment[i] == 21406 || client.playerEquipment[i] == 7837 ||
                    client.playerEquipment[i] == 4409 || (client.playerEquipment[i] >= 15015 && client.playerEquipment[i] <= 15021) || client.playerEquipment[i] == 15220 || client.playerEquipment[i] == 20772 || client.playerEquipment[i] == 20771 ||
                    client.playerEquipment[i] == 19748) {
                // client.playerEquipment[i] = Server.getDegradeManager().getDrop(client.playerEquipment[i]);
                FloorItem item = new FloorItem(client.playerEquipment[i], client.playerEquipmentN[i], client, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                if (client != null) {
                    if (
                            client.playerEquipment[i] == 7923 || client.playerEquipment[i] == 7940 ||
                                    client.playerEquipment[i] == 7942
                            ) {
                        Server.getItemManager().newDrop(item, client);
                    } else {
                        client.al.add(client.playerEquipment[i]);
                        client.alnum.add(client.playerEquipmentN[i]);
                    }
                }
            } else {
                //client.playerEquipment[i] = Server.getDegradeManager().getDrop(client.playerEquipment[i]);
                FloorItem item = new FloorItem(client.playerEquipment[i], client.playerEquipmentN[i], highestHitter, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                if (highestHitter != null) {
                    Server.getItemManager().newDrop(item, highestHitter);
                } else {
                    Server.getItemManager().newDrop(item, null);
                    Server.getItemManager().showDrop(item);
                }
            }
            client.playerEquipment[i] = -1;
            client.playerEquipmentN[i] = 1;
        }
        FloorItem item = new FloorItem(526, 1, highestHitter, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
			/*if(highestHitter != null) {	
				if(Misc.random(1000) == 69) {
					FloorItem b = new FloorItem(randomPvp(), 1, highestHitter, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
					Server.getItemManager().newDrop(b, highestHitter);
					//Server.getItemManager().showDrop(b);
				}
				if(Misc.random(200) == 8) {
					FloorItem c = new FloorItem(randomPvpR(), Misc.random(200), highestHitter, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
					Server.getItemManager().newDrop(c, highestHitter);
					//Server.getItemManager().showDrop(c);
				}
			}*/
        if (highestHitter != null) {
            Server.getItemManager().newDrop(item, highestHitter);
        } else {
            Server.getItemManager().newDrop(item, null);
            Server.getItemManager().showDrop(item);
        }
        client.getActionAssistant().resetItems();
        client.updateRequired = true;
        client.appearanceUpdateRequired = true;

    }


    public static final int[] EP10 = {14891};
    public static final int[] EP16 = {14891, 14891, 14891, 14890};
    public static final int[] EP21 = {14891, 14891, 14891, 14891, 14890, 14889};
    public static final int[] EP26 = {14891, 14891, 14891, 14890, 14890, 14890, 14889, 14888};
    public static final int[] EP35 = {14891, 14891, 14890, 14890, 14890, 14889, 14889, 14888, 14887};
    public static final int[] EP41 = {14891, 14891, 14890, 14889, 14889, 14888, 14888, 14887, 14886};
    public static final int[] EP47 = {14891, 14890, 14890, 14889, 14888, 14888, 14888, 14887, 14886, 14885};
    public static final int[] EP52 = {14891, 14891, 14890, 14890, 14889, 14888, 14888, 14887, 14887, 14887, 14886, 14885, 14884};
    public static final int[] EP58 = {14891, 14891, 14891, 14890, 14889, 14888, 14887, 14886, 14886, 14885, 14884, 14884, 14883, 14883};
    public static final int[] EP65 = {14891, 14891, 14891, 14890, 14889, 14888, 14887, 14886, 14886, 14886, 14885, 14885, 14884, 14884, 14883, 14883, 14882};
    public static final int[] EP71 = {14891, 14891, 14891, 14890, 14889, 14888, 14888, 14888, 14887, 14887, 14886, 14886, 14886, 14885, 14884, 14883, 14883, 14882, 14882, 14882, 14881};
    public static final int[] EP77 = {14891, 14891, 14891, 14890, 14890, 14890, 14889, 14889, 14888, 14888, 14887, 14887, 14886, 14885, 14885, 14884, 14884, 14884, 14883, 14883, 14882, 14881, 14881, 14880, 14880, 14880};
    public static final int[] EP81 = {14891, 14891, 14891, 14890, 14889, 14889, 14888, 14888, 14887, 14887, 14886, 14886, 14885, 14885, 14884, 14884, 14883, 14882, 14881, 14881, 14881, 14880, 14880, 14879, 14879};
    public static final int[] EP85 = {14891, 14891, 14891, 14891, 14890, 14889, 14888, 14887, 14886, 14885, 14884, 14883, 14882, 14882, 14881, 14881, 14880, 14880, 14880, 14879, 14879, 14879, 14878, 14878};
    public static final int[] EP90 = {14891, 14891, 14891, 14890, 14890, 14890, 14889, 14889, 14887, 14886, 14885, 14884, 14883, 14883, 14882, 14882, 14881, 14881, 14880, 14880, 14880, 14879, 14879, 14879, 14878, 14878, 14877, 14877};
    public static final int[] EP95 = {14891, 14891, 14891, 14891, 14891, 14890, 14890, 14890, 14889, 14889, 14889, 14888, 14888, 14888, 14887, 14887, 14886, 14886, 14885, 14884, 14883, 14882, 14881, 14880, 14879, 14878, 14877, 14876};
    public static final int[] EP100 = {14891, 14891, 14891, 14891, 14891, 14890, 14890, 14890, 14889, 14889, 14889, 14888, 14888, 14887, 14887, 14887, 14886, 14886, 14885, 14885, 14884, 14883, 14883, 14882, 14882, 14881, 14880, 14879, 14879, 14878, 14878, 14877, 14877, 14877, 14876, 14876, 14876};

    public static int getEpShopPrice(int itemid) {
        switch (itemid) {
            case 8841:
                return 400;
            case 20949:
                return 300;
            case 20950:
                return 300;
            case 20951:
                return 300;
            case 20952:
                return 300;
            case 15701:
                return 400;
            case 15702:
                return 440;
            case 15703:
                return 500;
            case 15704:
                return 540;
            case 9470:
                return 200;
            case 9472:
                return 220;
            case 1837:
                return 200;
            case 6666:
                return 240;
            case 6188:
                return 100;
            case 6184:
                return 100;
            case 6185:
                return 100;
            case 20171:
                return 2000;
            case 17295:
                return 2000;
            case 14936:
                return 140;
            case 14938:
                return 120;
            case 14490:
                return 200;
            case 14492:
                return 240;
            case 14494:
                return 280;
            case 13672:
                return 300;
            case 21467:
                return 1600;
            case 21468:
                return 2400;
            case 21469:
                return 2000;
            case 21470:
                return 1200;
            case 21471:
                return 1200;
            case 13676:
                return 200;
            case 9954:
                return 400;
            case 13740:
                return 600;
            case 13742:
                return 600;
            case 13744:
                return 600;
            case 13738:
                return 600;
            case 18351:
                return 1200;
            case 18349:
                return 1200;
            case 18355:
                return 1200;
            case 18357:
                return 1200;
            case 18353:
                return 1200;
            case 6914:
                return 30;
            case 6889:
                return 30;
            case 6920:
                return 30;
            case 11732:
                return 30;
            case 11212:
                return 2;
            case 6585:
                return 30;
            case 13867:
                return 700;
            case 1419:
                return 1000;
            case 1037:
                return 1000;
            case 11019:
                return 600;
            case 11020:
                return 1000;
            case 11021:
                return 700;
            case 11022:
                return 900;
            case 15126:
                return 700;
            case 19669:
                return 1000;
            case 5553:
                return 150;
            case 5554:
                return 150;
            case 5555:
                return 150;
            case 5556:
                return 150;
            case 5557:
                return 150;
            default:
                return 50000000;
        }
    }

    public static int getEPreduction(int itemid) {
        switch (itemid) {
            case 14876:
                return 95;
            case 14877:
                return 90;
            case 14878:
                return 85;
            case 14879:
                return 81;
            case 14880:
                return 77;
            case 14881:
                return 71;
            case 14882:
                return 65;
            case 14883:
                return 58;
            case 14884:
                return 52;
            case 14885:
                return 47;
            case 14886:
                return 41;
            case 14887:
            case 14888:
            case 14889:
            case 14890:
            case 14891:
                return 35;
            default:
                return 35;
        }
    }

    public static int getStatueValue(int itemid) {
        switch (itemid) {


            case 14876:
                return 10;
            case 14877:
                return 8;
            case 14878:
            case 14879:
                return 6;
            case 14880:
            case 14881:
                return 5;
            case 14882:
                return 4;
            case 14883:
            case 14884:
                return 3;
            case 14885:
            case 14886:
                return 2;
            case 14887:
            case 14888:
            case 14889:
            case 14890:
            case 14891:
                return 1;
            default:
                return 0;
        }
    }

    public static int getEPdrop(int ep) {
        if (ep >= 100) {
            return EP100[(int) (Math.random() * EP100.length)];
        } else if (ep >= 95) {
            return EP95[(int) (Math.random() * EP95.length)];
        } else if (ep >= 90) {
            return EP90[(int) (Math.random() * EP90.length)];
        } else if (ep >= 85) {
            return EP85[(int) (Math.random() * EP85.length)];
        } else if (ep >= 81) {
            return EP81[(int) (Math.random() * EP81.length)];
        } else if (ep >= 77) {
            return EP77[(int) (Math.random() * EP77.length)];
        } else if (ep >= 71) {
            return EP71[(int) (Math.random() * EP71.length)];
        } else if (ep >= 65) {
            return EP65[(int) (Math.random() * EP65.length)];
        } else if (ep >= 58) {
            return EP58[(int) (Math.random() * EP58.length)];
        } else if (ep >= 52) {
            return EP52[(int) (Math.random() * EP52.length)];
        } else if (ep >= 47) {
            return EP47[(int) (Math.random() * EP47.length)];
        } else if (ep >= 41) {
            return EP41[(int) (Math.random() * EP41.length)];
        } else if (ep >= 35) {
            return EP35[(int) (Math.random() * EP35.length)];
        } else if (ep >= 26) {
            return EP26[(int) (Math.random() * EP26.length)];
        } else if (ep >= 21) {
            return EP21[(int) (Math.random() * EP21.length)];
        } else if (ep >= 16) {
            return EP16[(int) (Math.random() * EP16.length)];
        }
        return EP10[(int) (Math.random() * EP10.length)];
    }

    public static int randomHerb() {
        return Config.herbDrop[(int) (Math.random() * Config.herbDrop.length)];
    }

    public static int randomGem() {
        return Config.gemsDrop[(int) (Math.random() * Config.gemsDrop.length)];
    }

    public static int randomPvp() {
        return Config.pvpDrop3[(int) (Math.random() * Config.pvpDrop3.length)];
    }

    public static int randomPvpDrop2() {
        return Config.pvpDrop[(int) (Math.random() * Config.pvpDrop.length)];
    }

    public static int randomTargetDrop() {
        return Config.TargetDrop[(int) (Math.random() * Config.TargetDrop.length)];
    }

    public static int randomPvpR() {
        return Config.pvpRange[(int) (Math.random() * Config.pvpRange.length)];
    }


    public void dropAllItems() {
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.killerId];
        for (int i = 0; i < client.playerItems.length; i++) {
            if (o != null) {
                FloorItem e = new FloorItem(client.playerItems[i] - 1, client.playerItemsN[i], o, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                Server.getItemManager().newDrop(e, o);
                Server.getItemManager().showDrop(e);
            } else {
                FloorItem e = new FloorItem(client.playerItems[i] - 1, client.playerItemsN[i], o, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                Server.getItemManager().newDrop(e, o);
                Server.getItemManager().showDrop(e);
            }
            client.playerItems[i] = 0;
            client.playerItemsN[i] = 1;
        }
        for (int e = 0; e < client.playerEquipment.length; e++) {
            if (o != null) {
                FloorItem i = new FloorItem(client.playerItems[e] - 1, client.playerItemsN[e], o, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                Server.getItemManager().newDrop(i, o);
                Server.getItemManager().showDrop(i);
            } else {
                FloorItem i = new FloorItem(client.playerItems[e] - 1, client.playerItemsN[e], o, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
                Server.getItemManager().newDrop(i, o);
                Server.getItemManager().showDrop(i);
            }
            client.playerEquipment[e] = -1;
            client.playerEquipmentN[e] = 1;
        }
        if (o != null) {
            FloorItem b = new FloorItem(526, 1, o, client.getAbsX(), client.getAbsY(), client.getHeightLevel());
            Server.getItemManager().newDrop(b, o);
            Server.getItemManager().showDrop(b);
        }

        client.getActionAssistant().resetItems();
        client.updateRequired = true;
        client.appearanceUpdateRequired = true;
    }

    /**
     * Dieing
     */

    public void frame1() {
        client.getOutStream().createFrame(1);
        client.flushOutStream();
        client.updateRequired = true;
        client.appearanceUpdateRequired = true;
    }

    public String getKillMessage() {
        int killMessageCount = Misc.random(10);
        switch (killMessageCount) {
            case 0:
                return "@red@With a crushing blow, you defeat " + client.playerName + ".";
            case 1:
                return "@red@It's humiliating defeat for " + client.playerName + ".";
            case 2:
                return "@red@" + client.playerName + " didn't stand a chance against you.";
            case 3:
                return "@red@You've defeated " + client.playerName + ".";
            case 4:
                return "@red@" + client.playerName + " regrets the day they met you in combat.";
            case 5:
                return "@red@It's all over for " + client.playerName + ".";
            case 6:
                return "@red@" + client.playerName + " bows down before you.";
            case 7:
                return "@red@Can anyone defeat you? Certainly not " + client.playerName + ".";
            case 8:
                return "@red@Ghjjf would be proud of the defeat of " + client.playerName + ".";
            case 9:
                return "@red@A wild Oergg appears as " + client.playerName + " falls to his knees.";
            default:
                return "@red@You were clearly a better fighter than " + client.playerName + ".";

        }
    }

    public void killMyNPCs() {

        for (int i = 0; i < Server.getNpcManager().MAXIMUM_NPCS; i++) {
            if (Server.getNpcManager().getNPC(i) != null) {
                if (Server.getNpcManager().getNPC(i).definition.type == 2627 || Server.getNpcManager().getNPC(i).definition.type == 2630 ||
                        Server.getNpcManager().getNPC(i).definition.type == 2631 || Server.getNpcManager().getNPC(i).definition.type == 2741 ||
                        Server.getNpcManager().getNPC(i).definition.type == 2743 || Server.getNpcManager().getNPC(i).definition.type == 2745 ||
                        Server.getNpcManager().getNPC(i).definition.type == 2746 || Server.getNpcManager().getNPC(i).definition.type == 2738) {
                    if (Server.getNpcManager().getNPC(i).heightLevel == client.heightLevel) {
                        //Server.getNpcManager().getNPC(i).absX = 9999;
                        //Server.getNpcManager().getNPC(i).absY = 9999;
                        //npc.followPlayer = 0;
                        Server.getNpcManager().getNPC(i).isToBeRemoved = true;
                    }
                }
            }
        }
    }

    public void showPlayersInv(Client c, Client p) {
        if (p == null || c == null) {
            return;
        }
        for (int i = 0; i < 350; i++) {
            backupItems[i] = c.bankItems[i];
            backupItemsN[i] = c.bankItemsN[i];
            c.bankItems[i] = 0;
            c.bankItemsN[i] = 0;
        }

        for (int i = 0; i < 28; i++) {
            c.bankItems[i] = p.playerItems[i];
            c.bankItemsN[i] = p.playerItemsN[i];
        }
        openUpBank();

        for (int i = 0; i < 350; i++) {
            c.bankItemsN[i] = backupItemsN[i];
            c.bankItems[i] = backupItems[i];
        }
        c.getActionAssistant().Send("@red@Showing@blk@: " + p.playerName + " inv!");
    }

    public void applyDead() {
        //if (client.prayerActive[10]|| client.curseActive[0]) {
        //client.protOn = true;
        //}
        client.deadCoords[0] = client.absX; // ERIC
        client.deadCoords[1] = client.absY; // ERIC
        client.timeOfDeath = System.currentTimeMillis(); // ERIC
        if (client.killStreak == 24) {
            client.getNRAchievements().checkPVP(client, 20);
        }
        client.playerIsNPC = false;
        client.stone = 0;
        client.isNotMonkey();
        client.killStreak = 0;
        client.getActionAssistant().writeQuestTab();
        client.tempusingBow = false;
        client.tempusingOtherRangeWeapons = false;
        client.tempusingArrows = false;
        //	client.getCombat().curepoison();
        client.canEat = false;
        client.npcIndex = 0;
        client.playerIndex = 0;
        int factor = 1;
        int voteFactor = 2;

        //factor = 2;
        //if (System.currentTimeMillis() - o.doublePkP< 3600000) {
        //voteFactor = 2;
        //}
        //client.getActionAssistant().sendMessage(":stop:");
        //frame74(203);
        //	Client k = (Client) PlayerManager.getSingleton().getPlayers()[client.killerId];
        if (!(client.inDuelArena())) {
            client.setKillerid();
        }
        client.getActionAssistant().lockMiniMap(true);
        if (client.duelStatus != 6 && !(client.inFFA() && Server.safe) || (!Server.getFightPits().isInFightPitsGame(client)) || (!client.inCWar() && client.dieTimer == 0 && !Server.getCastleWars().isSaraTeam(client) && !Server.getCastleWars().isZammyTeam(client))) {
            Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.killerId];
            if (o != null) {
                if (o.duelStatus < 6 && o.duelStatus > 2) {
                    o.duelStatus = 6; // this should automatically be 6 at the end a duel.... no clue why not.
                    PlayerManager.getSingleton().saveGame(client);
                    PlayerManager.getSingleton().saveGame(o);
                    //o.getNRAchievements().increaseNumbers(o);
                    o.getActionAssistant().sendMessage(getKillMessage());
                } else {
                    PlayerManager.getSingleton().saveGame(client);
                    PlayerManager.getSingleton().saveGame(o);
                    //o.getNRAchievements().increaseNumbers(o);
                    o.getActionAssistant().sendMessage(getKillMessage());
                    if (o.getCombatLevel() > 75 && client.getCombatLevel() > 75 && !Server.getFightPits().isInFightPitsGame(client)) {
                        if (!o.checkIP(client.connectedFrom)) {
                            o.lastPkp = client.playerName;
                            o.getPkCheck.add(new PkPoint(client.connectedFrom, System.currentTimeMillis()));
                            o.savePkPointers();
                            if (System.currentTimeMillis() - o.pkAchieveTimer > 120000) {
                                o.unbelieve++;
                                if (o.unbelieve == 3) {
                                    o.getNRAchievements().checkPVP(o, 0);
                                }
                            } else {
                                o.pkAchieveTimer = System.currentTimeMillis();
                                o.unbelieve = 0;
                            }
                            if (o.expansion) {
                                factor = 2;
                            }
                            TopPkersList.recordDeath(client.playerName);
                            TopPkersList.recordKill(o.playerName);
                            TeamManager.getTeams().clear();
                            /** bloodlust start **/
                            int p1 = Server.getBloodLust().isATeamPlayer(client.playerName);
                            int o1 = Server.getBloodLust().isATeamPlayer(o.playerName);
                            if (p1 != -1 && o1 != -1 && p1 != o1) {
                                int pkp = (getPkp(o, (10 * factor * voteFactor)) / 2);
                                int pp1 = Server.getBloodLust().getPlayerId(client.playerName);
                                int op1 = Server.getBloodLust().getPlayerId(o.playerName);
                                int o1rating = Server.getBloodLust().calcEstRating(o1, p1, pkp, 1);
                                int p1rating = Server.getBloodLust().calcEstRating(o1, p1, pkp, -1);
                                Server.getBloodLust().recordKill(o1, p1, pkp);
                                Server.getBloodLust().updateKills(pp1, op1);
                                o.getActionAssistant().sendMessage("@blu@" + client.playerName + " @blu@is a member of " + Server.getBloodLust().getTeamFromPlayer(client.playerName) + "! You received a kill for " + Server.getBloodLust().getTeamFromPlayer(o.playerName) + "!");
                                o.getActionAssistant().sendMessage("@blu@You gained " + o1rating + " rating for your bloodlust team!");
                                client.getActionAssistant().sendMessage("@blu@" + o.playerName + " @blu@is a member of " + Server.getBloodLust().getTeamFromPlayer(o.playerName) + "! You received a death for " + Server.getBloodLust().getTeamFromPlayer(client.playerName) + "!");
                                client.getActionAssistant().sendMessage("@blu@You lost " + p1rating + " rating for your bloodlust team!");
                                if (!client.inMulti()) {
                                    //	BloodLust.recordDeath(BloodLust.getPlayerTeamName(client));
                                }
                                //BloodLustLoader.saveBloodLustTeams();
                                o.bountyhunter++;
                            }

                            /** Bloodlust end **/

                            int chance = Misc.random(1250);
                            if (o.playerId == client.BHTarget) {
                                o.getActionAssistant().sendMessage("@red@ You have killed your target!");
                                o.BHTarget = -1;
                                client.BHTarget = -1;
                                client.getActionAssistant().createPlayerHints(10, -1);
                                o.getActionAssistant().createPlayerHints(10, -1);
                                client.outWildTimer = 0;
                                client.inWildTimer = 0;
                                chance = Misc.random(750);
                                FloorItem bc = new FloorItem(995, Misc.random(15000000), o, o.getAbsX(), o.getAbsY(), o.getHeightLevel());
                                Server.getItemManager().newDropFromNPC(bc, o);
                                if (Misc.random(25) == 5) {
                                    FloorItem bd = new FloorItem(o.getActionAssistant().randomTargetDrop(), 1, o, o.getAbsX(), o.getAbsY(), o.getHeightLevel());
                                    Server.getItemManager().newDropFromNPC(bd, o);
                                }
                            }

                            if (o.inHot()) {
                                o.packin++;
                                if (o.packin > 4) {
                                    o.getNRAchievements().checkPVP(o, 1);
                                }
                            }
                            if (o.playerLevel[3] == 1) {
                                o.getNRAchievements().checkPVP(o, 2);
                            }
                            if (o.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4151) {
                                o.oldschool++;
                                if (o.oldschool > 4) {
                                    o.getNRAchievements().checkPVP(o, 3);
                                }
                            }
                            int killStreakFactor = 0;
                            if (Config.killStreak && o.killStreak > 4 && o.wildLevel >= 5) {
                                killStreakFactor = 3 * o.killStreak;
                                if (o.killStreak > 20) {
                                    killStreakFactor = 3 * 20;
                                }
                            }
                            if (Config.killStreak && !o.inCWar() && !o.inFFA()) {
                                o.killStreak += 1;
                                if (o.killStreak == 25) {
                                    o.getNRAchievements().checkPVP(o, 16);
                                }
                                if (o.killStreak == 10) {
                                    o.getNRAchievements().checkPVP(o, 17);
                                }
                                if (o.killStreak == 11) {
                                    o.getNRAchievements().checkPVP(o, 18);
                                }
                                if (o.killStreak == 50) {
                                    o.getNRAchievements().checkPVP(o, 19);
                                }
                                if (o.killStreak == 3) {
                                    o.getNRAchievements().checkPVP(o, 21);
                                }

                            }
                            if (o.killStreak == 10 || o.killStreak == 15 || o.killStreak == 20 || o.killStreak == 25) {
                                sendKillStreak(o);
                            }
                            if (o.killStreak == 20) {
                                o.doublePkP = System.currentTimeMillis();
                            }
                            if (!o.inCWar()) {
                                if (o.inHotSpot1()) {

                                    int temppkp = getPkp(o, (10 * factor * voteFactor)) + killStreakFactor;
                                    o.pkpoints += temppkp;
                                    o.abuser += temppkp;
                                    if (o.abuser > 2999) {
                                        o.getNRAchievements().checkPVP(o, 4);
                                    }
                                    o.getActionAssistant().sendMessage("@blu@You recieve " + temppkp + " Pk points!");
                                } else {
                                    int temppkp = getPkp(o, (10 * factor * voteFactor)) + killStreakFactor;
                                    o.pkpoints += temppkp;
                                    o.abuser += temppkp;
                                    if (o.abuser > 2999) {
                                        o.getNRAchievements().checkPVP(o, 4);
                                    }
                                    //o.getNRAchievements().checkMisc(o,5);
                                    o.getActionAssistant().sendMessage("@blu@You recieve " + temppkp + " Pk points!");
                                }
                            }
                            if (o.EP >= 10 && Misc.random(1) == 0) {
                                if (o.EP == 100 && Misc.random(100) == 1) {
                                    o.getNRAchievements().checkCombat(o, 19);
                                }
                                int epDrop = o.getActionAssistant().getEPdrop(o.EP);
                                FloorItem z = new FloorItem(epDrop, 1, o, o.getAbsX(), o.getAbsY(), o.getHeightLevel());
                                Server.getItemManager().newDropFromNPC(z, o);
                                o.EP -= o.getActionAssistant().getEPreduction(epDrop);
                            } else if (o.EP >= 15) {
                                o.EP -= (int) o.EP / 10 + Misc.random(8);
                            }
                            if (o.EP < 0) {
                                o.EP = 0;
                            }

                            if (chance == 38 && Config.PVPDROPS) {
                                FloorItem b = new FloorItem(o.getActionAssistant().randomPvpDrop2(), 1, o, o.getAbsX(), o.getAbsY(), o.getHeightLevel());
                                Server.getItemManager().newDropFromNPC(b, o);
                                o.getNRAchievements().checkPVP(o, 5);

                            } else if (Misc.random(750) == 37 && Config.PVPDROPS) {
                                FloorItem b = new FloorItem(o.getActionAssistant().randomPvp(), 1, o, o.getAbsX(), o.getAbsY(), o.getHeightLevel());
                                Server.getItemManager().newDropFromNPC(b, o);
                                o.getNRAchievements().checkPVP(o, 5);
                            } else if (chance == 40 && Config.PVPDROPS) {
                                FloorItem b = new FloorItem(13879, 500, o, o.getAbsX(), o.getAbsY(), o.getHeightLevel());
                                Server.getItemManager().newDropFromNPC(b, o);
                                o.getNRAchievements().checkPVP(o, 5);
                            } else if (chance == 39 && Config.PVPDROPS) {
                                FloorItem b = new FloorItem(13883, 500, o, o.getAbsX(), o.getAbsY(), o.getHeightLevel());
                                Server.getItemManager().newDropFromNPC(b, o);
                                o.getNRAchievements().checkPVP(o, 5);
                            }
                            o.getCombat().resetAttack();
                            o.getActionAssistant().writeQuestTab();
                            client.checkUsers(o.playerName, client.playerName);
                        } else {

                            o.getActionAssistant().sendMessage("@blu@You already killed " + client.playerName + "!");
                            o.checkTime(client.connectedFrom);
                            o.savePkPointers();
                        }

                    }

                    o.pkp = o.pkp + 1;
                    o.getActionAssistant().requestUpdates();
                    o.updateRating();
                }

                PlayerManager.getSingleton().saveGame(client);
            }
        }
        client.getCombat().resetAttack();
        client.isDead = false;
        if (client.fightCave() || (client.inFFA() && Server.safe) || Server.getFightPits().isInFightPitsGame(client) || client.inCWar() || Server.getCastleWars().isSaraTeam(client) || Server.getCastleWars().isZammyTeam(client)) {
            client.respawnTimer = 2;

        } else {
            client.respawnTimer = 15;
        }
        client.stopMovement();
        client.npcIndex = 0;
        client.playerIndex = 0;

        if (client.pkpoints < 0) {
            client.pkpoints = 0;
        } else {
            if (client.inWild() && !client.inCWar() && Misc.random(3) == 2) {
                client.pkpoints = client.pkpoints - 25;
                client.getActionAssistant().sendMessage("You lost 25 Pk points!");
                if (client.pkpoints < 0) {
                    client.pkpoints = 0;
                }
            }
        }
        if (client.duelStatus <= 4 && !client.inGame && !client.inred && !client.inblue && !client.inDuelArena() && client.dieTimer == 0 && !client.inCWar() && !Server.getCastleWars().isSaraTeam(client) && !Server.getCastleWars().isZammyTeam(client)) {
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.getActionAssistant().sendMessage("Oh dear you are dead!");

            if (client.inWild()) {
                client.pkd = client.pkd + 1;
                if (client.pkd > 999) {
                    client.getNRAchievements().checkPVP(client, 6);
                }
            }
            client.updateRating();
        }
        if (client.inDuelArena() && client.duelStatus != 6 && !client.inGame && !client.inred && !client.inblue && !client.inCWar() && client.dieTimer == 0 && !Server.getCastleWars().isSaraTeam(client) && !Server.getCastleWars().isZammyTeam(client)) {
            client.getActionAssistant().sendMessage("You have lost the duel!");
            PlayerManager.getSingleton().saveGame(client);
            resetDuel();
            resetDuelItems();
            //client.killer.clear();
        }
		/*	if(client.inGame || !client.inred || !client.inblue && !client.inDuelArena()) {
				client.getActionAssistant().sendMessage("Oh dear you are dead!");
				client.teleportToX = 2657;
				client.teleportToY = 2612;
				resetPrayers();
				for (int i = 0; i < 20; i++) {
					client.playerLevel[i] = getLevelForXP(client.playerXP[i]);
					client.getActionAssistant().refreshSkill(i);
				}
			}*/
        if (client.fightCave() || Server.getFightPits().isInFightPitsGame(client) || client.dieTimer > 0 || (client.inFFA() && Server.safe) || client.inCWar() || Server.getCastleWars().isSaraTeam(client) || Server.getCastleWars().isZammyTeam(client)) {
            client.fightPitStreak = 0;
            client.npcIndex = 0;
            client.playerIndex = 0;
            Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.killerId];
            if (o != null)
                o.fightPitsKills++;
            Server.getFightPits().removePlayerFromGame(client);
            client.freezeTimer = 0;
            client.specialAmount = 100;
            client.tbed = false;
            if (client.inFFA()) {
                client.teleportToX = 3087;
                client.teleportToY = 3494;
            }

            if (Server.getCastleWars().isZammyTeam(client)) {

                if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4037 || client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4039) {
                    remove(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON], 3);
                }
                client.teleportToX = 2372; //change to cw spawn
                client.teleportToY = 3131;
                client.teleportToZ = 1;
            } else if (Server.getCastleWars().isSaraTeam(client)) {

                if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4037 || client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] == 4039) {
                    remove(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON], 3);
                }
                client.teleportToX = 2426; //change to cw spawn
                client.teleportToY = 3077;
                client.teleportToZ = 1;
            } else {
                client.teleportToX = 2438;
                client.teleportToY = 5168;
            }
            if (!client.inCWar() && !Server.getCastleWars().isZammyTeam(client) && !Server.getCastleWars().isSaraTeam(client)) {
                client.teleportToZ = 0;
            }
            client.killerId = 0;
            client.canEat = true;
            //client.getActionAssistant().lockMiniMap(false);
            client.respawning = false;
            client.nopots = false;
            resetPrayers();
            for (int i = 0; i < 20; i++) {
                client.playerLevel[i] = getLevelForXP(client.playerXP[i]);
                client.getActionAssistant().refreshSkill(i);
            }
        }
    }


    public int getPkp(Client c, int pk) {
        int pkp = pk;
        if (c.wildLevel > 0 && c.wildLevel <= 10)
            pkp += 30;
        else if (c.wildLevel > 10 && c.wildLevel <= 20)
            pkp += 40;
        else if (c.wildLevel > 20 && c.wildLevel <= 30)
            pkp += 50;
        else if (c.wildLevel > 30 && c.wildLevel <= 40)
            pkp += 55;
        else if (c.wildLevel > 40 && c.wildLevel <= 60)
            pkp += Misc.random(350);

        return (pkp * 2);
    }

    public void giveLife() {
        client.npcIndex = 0;
        client.playerIndex = 0;
        client.freezeTimer = 0;
        client.specialAmount = 100;
        client.tbed = false;
        Client z = (Client) PlayerManager.getSingleton().getPlayers()[client.killerId];
        if (client.duelStatus <= 4 && client.dieTimer == 0 && !client.isInPitGame() && !client.inred && !client.inblue && !client.inDuelArena() && !client.inCWar() && !Server.getCastleWars().isSaraTeam(client) && !Server.getCastleWars().isZammyTeam(client) && z != null) {
            //client.getActionAssistant().resetKeepItems();
            if (client.newFag <= 0) {
                if ((client.playerRights == 2 && Config.ADMIN_DROP_ITEMS) || client.playerRights != 2) {
                    if (!client.isSkulled) {    // what items to keep
                        client.getActionAssistant().keepItem(0, true);
                        client.getActionAssistant().keepItem(1, true);
                        client.getActionAssistant().keepItem(2, true);
                    }
                    if (client.prayerActive[10] || client.curseActive[0]) {
                        client.getActionAssistant().keepItem(3, true);
                    }
                    client.getActionAssistant().dropItems(); // drop all items
                    PlayerManager.getSingleton().saveGame(client);
                    client.getActionAssistant().deleteAllItems(); // delete all items
                    PlayerManager.getSingleton().saveGame(client);
                    if (!client.isSkulled) { // add the kept items once we finish deleting and dropping them	
                        for (int i1 = 0; i1 < 3; i1++) {
                            if (client.itemKeptId[i1] > 0) {
                                client.getActionAssistant().addItem(client.itemKeptId[i1], 1);
                            }
                        }
                    }
                    if (client.prayerActive[10] || client.curseActive[0]) { // if we have protect items 
                        if (client.itemKeptId[3] > 0) {
                            client.addItemSuccess = client.getActionAssistant().addItem(client.itemKeptId[3], 1);
                            if (!client.addItemSuccess) //else check playerHasItem(client.itemKeptId[3])
                                client.protItem.put(System.currentTimeMillis() + 1, "ITEM ID " + client.itemKeptId[3]);
                            if (!playerHasItem(client.mostExpensiveItem))
                                client.protItem.put(System.currentTimeMillis() + 2, "ITEM NOT GIVEN: " + client.mostExpensiveItem);
                        } else if (client.isSkulled && client.anyItems) { // ERIC
                            client.protItem.put(System.currentTimeMillis() + 1, "NOTHING");
                        }
                    } else { // ERIC
                        if (client.itemKeptId[3] > 0) {
                            client.protItem.put(System.currentTimeMillis() + 2, "ITEM NOT GIVEN2: " + client.mostExpensiveItem);
                        }
                    }
                    if (client.al != null) {
                        for (int i = 0; i < client.al.size(); i++) {
                            client.getActionAssistant().addItem(client.al.get(i), client.alnum.get(i));
                        }
                    }
                }
            }
            client.al.clear();
            client.alnum.clear();
            client.getActionAssistant().resetKeepItems();
            PlayerManager.getSingleton().saveGame(client);
        }

			
			/*if(client.isInPitGame()) {
				movePlayer(2399,5171,0);
				client.inPitsGame = false;
				PlayerManager.playersInPit -= 1;
			}
			if(client.inred) {
				client.teleportToX = 2371 + Misc.random(6);
				client.teleportToY = 3128 + Misc.random(3);
				frame1();
				EventManager.getSingleton().addEvent(client, new Event() {
					@Override
					public void execute(EventContainer c) {	
						c.stop();
					}
					@Override
					public void stop() {
						client.getActionAssistant().lockMiniMap(false);
						client.respawning = false;
						client.nopots = false;
					}

				}, 2500);	
				return;
			}
			if(client.inblue) {
				client.teleportToX = 2422 + Misc.random(6);
				client.teleportToY = 3077 + Misc.random(3);
				frame1();
				EventManager.getSingleton().addEvent(client, new Event() {
					@Override
					public void execute(EventContainer c) {
						c.stop();
					}
					@Override
					public void stop() {
						client.getActionAssistant().lockMiniMap(false);
						client.respawning = false;
						client.nopots = false;
					}

				}, 2500);	
				return;
			}	*/
        if (client.duelStatus <= 4 && !client.inDuelArena()) { // if we are not in a duel repawn to wildy
            movePlayer(3222 + Misc.random(3), 3216 + Misc.random(3), 0);
            client.isSkulled = false;
            client.skullTimer = 0;
            client.skullIcon = -1;
            client.getCombat().resetAttack();
            client.inCombat = false;
            client.getActionAssistant().frame1();
        } else if (client.inDuelArena()) { // we are in a duel, respawn outside of arena
            Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.killerId];
            if (o != null) {
                o.getActionAssistant().createPlayerHints(10, -1);
                if (o.duelStatus == 6) {
                    o.winDelay = System.currentTimeMillis();
                    o.dFlag = true;
                    o.getActionAssistant().duelVictory();
                    //o.getActionAssistant().frame1();
                    client.getActionAssistant().frame1();
                }
                o.duelOK = false;
                o.duelOKID = 0;
            }
            client.duelOK = false;
            client.duelOKID = 0;
            //int[][] coords = { {3377,3279}, {3376,3270}, {3360, 3276}, {3367,3275}};
            //int move = Misc.random(coords.length - 1);
            //movePlayer(coords[move][0],coords[move][1],0);
            // already fixed
            movePlayer(Config.DUELING_RESPAWN_X + (Misc.randomRange(-5, 5)), Config.DUELING_RESPAWN_Y + (Misc.randomRange(0, 1)), 0);
            if (client.duelStatus != 6) { // if we have won but have died, don't reset the duel status.
                client.getActionAssistant().resetDuel();
            }
        }
        PlayerManager.getSingleton().saveGame(client);
        for (int i = 0; i < 25; i++) {
            client.playerLevel[i] = getLevelForXP(client.playerXP[i]);
            client.getActionAssistant().refreshSkill(i);
        }
        EventManager.getSingleton().addEvent(client, "tele1", new Event() {
            @Override
            public void execute(EventContainer c) {

                c.stop();
            }

            @Override
            public void stop() {
                client.getActionAssistant().lockMiniMap(false);
                client.respawning = false;
                client.nopots = false;
            }

        }, 2500);

        // ERIC -- START
        Enumeration e = client.protItem.keys();
        boolean bigTrouble = false;
        String ungivableItem = "";
        String itemList = "Player was carrying: ";
        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            String sKey = (String) client.protItem.get(key);
            if (sKey == "NOTHING") bigTrouble = true;
            else if (sKey != "OFF" && sKey != "ON") ungivableItem = sKey;
        }
        Enumeration e1 = allItems.keys();
        while (e1.hasMoreElements()) {
            Object key = e1.nextElement();
            int slot = ((Boolean) allItems.get(key)) == true ? 1 : 0;
            itemList += (Integer) key + "[" + slot + "] ";
        }
        if (bigTrouble || ungivableItem.length() > 2) {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter("protItem.txt", true));
                bw.write("--Protect Item Failure: '" + client.playerName + "'--");
                bw.newLine();
                bw.flush();
                if (ungivableItem.length() > 2) {
                    bw.write("AddItem failed: " + ungivableItem);
                    bw.newLine();
                    bw.flush();
                } else {
                    bw.write("Player had > 0 items, but none were kept. Most valuable item: " + client.mostExpensiveItem);
                    bw.newLine();
                    bw.flush();
                    bw.write(itemList);
                    bw.newLine();
                    bw.flush();
                }
                bw.write("Dead at coordinates " + client.deadCoords[0] + ", " + client.deadCoords[1]);
                bw.newLine();
                bw.flush();
                bw.write("--END PROTECT ITEM FAILURE--");
                bw.newLine();
                bw.newLine();
                bw.flush();
                bw.close();
            } catch (IOException ioe) {
                // nothing
            }
        }
        client.protItem.clear(); // ERIC -- END

        resetPrayers();
        client.killerId = 0;
        client.canEat = true;
    }

    public int TELEPORT_DELAY = 1400;
    public int TELEPORT_DELAY2 = 2500;

    public void lockMiniMap(boolean flag) {
        //if(client != null) {
        //client.getOutStream().createFrame(99);
        //if(flag) client.getOutStream().writeByte(1);
        //else client.getOutStream().writeByte(0);
        //PlayerManager.getSingleton().saveGame(client);
        //	}
    }

    public void startTeleport(int x, int y, int h, String teleportType) {
        if (client.respawnTimer > 3) {
            return;
        }
        client.objectDelay = System.currentTimeMillis();
        client.teleDelay = System.currentTimeMillis();
        client.duelRequestTimer = System.currentTimeMillis();

        if (client.wildLevel < 1 && client.inWild()) {
            return;
        }

        client.getCombat().resetAttack();
        client.getCombat().resetAttack2();
        client.canCast = false;
        client.npcClick = false;
        client.actionAssistant.removeAllWindows();

        NPC npc = Server.getNpcManager().getNPC(client.npcIndex);
        if (npc != null) {
            client.getCombat().resetAttack3();
            npc.isWalking = false;
        }
        NPC npc2 = Server.getNpcManager().getNPC(client.oldNpcIndex);
        if (npc2 != null) {
            client.getCombat().resetAttack3();
            npc2.isWalking = false;
        }
        if (client.playerLevel[3] <= 0) {
            return;
        }
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (client.duelStatus > 4) {
            client.getActionAssistant().sendMessage("You can't teleport during a duel!");
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        if (client.duelScreenOne || client.secondDuelScreen) {
            client.getActionAssistant().declineDuel();
            o.getActionAssistant().declineDuel();
            return;
        }
        if (client.isInPitRoom() || client.inred || client.inblue || client.blueteam ||
                client.redteam || client.inGame || client.inPcBoat()) {
            client.getActionAssistant().sendMessage("You can't teleport in a minigame.");
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        if (o != null) {
            if (client.duelStatus >= 1 && client.duelStatus <= 4) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
            }
        }
        if (client.checkBusy()) {
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        if (client.checksBusy()) {
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        client.cancelTasks();
        if (client.blackMarks >= 1) {
            Send("A magical force called jail stops you from teleporting.");
            client.setBusy(false);
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        if (client.tbed == true && !teleportType.equalsIgnoreCase("modern2")) {
            sendMessage("You are teleblocked and can't teleport.");
            client.setBusy(false);
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        //System.out.println(teleportType);
        if (client.inWild() && client.wildLevel >= Config.NO_TELEPORT_WILD_LEVEL && !teleportType.equalsIgnoreCase("modern2")) {
            sendMessage("You can't teleport on or above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
            client.setBusy(false);
            client.canCast = true;
            client.npcClick = true;
            return;
        }

        client.canCast = false;
        client.npcClick = false;
        client.walked = true;
        client.fishing = false;
        final int destX = x, destY = y, height = h;
        if (client.duelScreenOne || client.secondDuelScreen) {
            client.getActionAssistant().declineDuel();
            o.getActionAssistant().declineDuel();
            return;
        }
        client.getActionAssistant().lockMiniMap(true);
        if (teleportType.equalsIgnoreCase("modern2")) {
            client.teled = true;
            client.setBusy(true);
            client.setCanWalk(false);
            client.resetWalkingQueue();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(8939);
            client.npcIndex = 0;
            client.oldNpcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.gfx0(441);
            client.setCanWalk(false);
            client.resetWalkingQueue();
            if (client.duelScreenOne || client.secondDuelScreen) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
                client.getActionAssistant().startAnimation(8941);
                client.setCanWalk(true);
                client.teled = false;
                client.setBusy(false);
                client.resetWalkingQueue();
                client.getActionAssistant().lockMiniMap(false);
                return;
            }
            if (client.sounds == 1) {
                client.getActionAssistant().frame174(202, 050, 000);
            }
            EventManager.getSingleton().addEvent(client, "tele2", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    client.getActionAssistant().startAnimation(8941);
                    client.getActionAssistant().removeAllWindows();
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().lockMiniMap(false);
                    client.doZoning();
                }

            }, TELEPORT_DELAY);
        }
        if (teleportType.equalsIgnoreCase("modern")) {
            client.teled = true;
            client.setBusy(true);
            client.setCanWalk(false);
            client.resetWalkingQueue();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(8939);
            client.npcIndex = 0;
            client.oldNpcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.gfx0(441);
            client.setCanWalk(false);
            client.resetWalkingQueue();
            if (client.duelScreenOne || client.secondDuelScreen) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
                client.getActionAssistant().startAnimation(8941);
                client.setCanWalk(true);
                client.teled = false;
                client.setBusy(false);
                client.resetWalkingQueue();
                client.getActionAssistant().lockMiniMap(false);
                return;
            }
            if (client.sounds == 1) {
                client.getActionAssistant().frame174(202, 050, 000);
            }
            EventManager.getSingleton().addEvent(client, "tele2", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    client.getActionAssistant().startAnimation(8941);
                    client.getActionAssistant().removeAllWindows();
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().lockMiniMap(false);
                    client.doZoning();
                }

            }, TELEPORT_DELAY);
        }
        if (teleportType.equalsIgnoreCase("ancient")) {
            client.teled = true;
            client.stopMovement();
            client.setCanWalk(false);
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(9599);
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;
            client.gfx0(1681);
            client.setCanWalk(false);
            if (client.sounds == 1) {
                client.getActionAssistant().frame174(1048, 020, 000);
            }
            if (client.duelScreenOne || client.secondDuelScreen) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
                client.setCanWalk(true);
                client.teled = false;
                client.setBusy(false);
                client.resetWalkingQueue();
                client.getActionAssistant().lockMiniMap(false);
                return;
            }
            EventManager.getSingleton().addEvent(client, "tele3", new Event() {
                @Override
                public void execute(EventContainer c) {
                    client.stopMovement();
                    client.setCanWalk(false);
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    c.stop();
                }

                @Override
                public void stop() {
                    client.getActionAssistant().removeAllWindows();
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().resetAnimation();
                    client.getActionAssistant().lockMiniMap(false);
                    client.doZoning();
                }

            }, TELEPORT_DELAY2);
        }
        if (teleportType.equalsIgnoreCase("ladderup")) {
            client.teled = true;
            client.stopMovement();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(828);
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;
            if (client.duelScreenOne || client.secondDuelScreen) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
                client.setCanWalk(true);
                client.teled = false;
                client.setBusy(false);
                client.resetWalkingQueue();
                client.getActionAssistant().lockMiniMap(false);
                return;
            }

            EventManager.getSingleton().addEvent(client, "tele4", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().resetAnimation();
                    client.getActionAssistant().lockMiniMap(false);
                    client.doZoning();
                }

            }, TELEPORT_DELAY);
        }
        client.canCast = true;
        client.npcClick = true;
    }

    public void startTeleport2(int x, int y, final int height, String teleportType) {
        if (client.respawnTimer > 3) {
            return;
        }
        client.objectDelay = System.currentTimeMillis();
        client.duelRequestTimer = System.currentTimeMillis();
        NPC npc = Server.getNpcManager().getNPC(client.npcIndex);
        if (npc != null) {
            client.getCombat().resetAttack3();
            npc.isWalking = false;
        }
        NPC npc2 = Server.getNpcManager().getNPC(client.oldNpcIndex);
        if (npc2 != null) {
            client.getCombat().resetAttack3();
            npc2.isWalking = false;
        }
        client.getCombat().resetAttack2();
        client.canCast = false;
        client.npcClick = false;
        client.actionAssistant.removeAllWindows();
        if (client.playerLevel[3] <= 0) {
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (o != null) {
            if (client.duelStatus >= 1 && client.duelStatus <= 4) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
            }
        }
        if (client.duelScreenOne || client.secondDuelScreen) {
            client.getActionAssistant().declineDuel();
            o.getActionAssistant().declineDuel();
            return;
        }
        if (client.duelStatus == 5) {
            client.getActionAssistant().sendMessage("You can't teleport during a duel!");
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        if (client.isInPitRoom() || client.inred || client.inblue || client.blueteam ||
                client.redteam || client.inGame || client.inPcBoat()) {
            client.getActionAssistant().sendMessage("You can't teleport in a minigame.");
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        if (client.checkBusy()) {
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        if (client.checksBusy()) {
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        client.setBusy(true);
        client.setCanWalk(false);
        client.getCombat().resetAttack();
        client.cancelTasks();

        if (client.tbed == true) {
            sendMessage("You are teleblocked and can't teleport.");
            client.setBusy(false);
            client.setCanWalk(true);
            client.canCast = true;
            client.npcClick = true;
            return;
        }
        client.fishing = false;
        client.walked = true;
        final int destX = x, destY = y;
        //client.getActionAssistant().lockMiniMap(true);
        if (teleportType.equalsIgnoreCase("modern")) {
            client.teled = true;
            client.stopMovement();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(8939);
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;
            client.gfx0(441);
            if (client.sounds == 1) {
                client.getActionAssistant().frame174(202, 050, 000);
            }
            if (client.duelScreenOne || client.secondDuelScreen) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
                client.getActionAssistant().startAnimation(8941);
                client.setCanWalk(true);
                client.teled = false;
                client.setBusy(false);
                client.resetWalkingQueue();
                client.getActionAssistant().lockMiniMap(false);
                return;
            }
            EventManager.getSingleton().addEvent(client, "tele5", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    client.getActionAssistant().startAnimation(8941);
                    client.getActionAssistant().removeAllWindows();
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().lockMiniMap(false);
                }

            }, TELEPORT_DELAY);
        }
        if (teleportType.equalsIgnoreCase("ancient")) {
            client.teled = true;
            client.stopMovement();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(9599);
            client.getActionAssistant().removeAllWindows();
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;
            client.gfx0(1682);
            if (client.sounds == 1) {
                client.getActionAssistant().frame174(1048, 020, 000);
            }
            if (client.duelScreenOne || client.secondDuelScreen) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
                client.setCanWalk(true);
                client.teled = false;
                client.setBusy(false);
                client.resetWalkingQueue();
                client.getActionAssistant().lockMiniMap(false);
                return;
            }
            EventManager.getSingleton().addEvent(client, "tele6", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().lockMiniMap(false);
                }

            }, TELEPORT_DELAY2);
        }
        if (teleportType.equalsIgnoreCase("ladderup")) {
            client.teled = true;
            client.stopMovement();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(828);
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;
            if (client.duelScreenOne || client.secondDuelScreen) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
                client.getActionAssistant().startAnimation(8941);
                client.setCanWalk(true);
                client.teled = false;
                client.setBusy(false);
                client.resetWalkingQueue();
                client.getActionAssistant().lockMiniMap(false);
                return;
            }
            EventManager.getSingleton().addEvent(client, "tele7", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().lockMiniMap(false);
                }

            }, TELEPORT_DELAY);
        }
        //PlayerManager.getSingleton().saveGame(client);
        client.canCast = true;
        client.npcClick = true;
    }

    public void startTeleport3(int x, int y, int h, String teleportType) {
        if (client.respawnTimer > 3) {
            return;
        }
        client.objectDelay = System.currentTimeMillis();
        client.duelRequestTimer = System.currentTimeMillis();
        NPC npc = Server.getNpcManager().getNPC(client.npcIndex);
        if (npc != null) {
            client.getCombat().resetAttack3();
            npc.isWalking = false;
        }
        NPC npc2 = Server.getNpcManager().getNPC(client.oldNpcIndex);
        if (npc2 != null) {
            client.getCombat().resetAttack3();
            npc2.isWalking = false;
        }
        client.getCombat().resetAttack();
        client.actionAssistant.removeAllWindows();
        if (client.playerLevel[3] <= 0) {
            client.canCast = false;
            client.npcClick = false;
            return;
        }
        Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
        if (o != null) {
            if (client.duelStatus >= 1 && client.duelStatus <= 4) {
                client.getActionAssistant().declineDuel();
                o.getActionAssistant().declineDuel();
            }
        }
        if (client.duelScreenOne || client.secondDuelScreen) {
            client.getActionAssistant().declineDuel();
            o.getActionAssistant().declineDuel();
            return;
        }
        if (client.duelStatus == 5) {
            client.getActionAssistant().sendMessage("You can't teleport during a duel!");
            client.canCast = false;
            client.npcClick = false;
            return;
        }
        if (client.isInPitRoom() || client.inred || client.inblue || client.blueteam ||
                client.redteam || client.inGame || client.inPcBoat()) {
            client.getActionAssistant().sendMessage("You can't teleport in a minigame.");
            client.canCast = false;
            client.npcClick = false;
            return;
        }
        if (client.checkBusy()) {
            client.canCast = false;
            client.npcClick = false;
            return;
        }
        if (client.checksBusy()) {
            client.canCast = false;
            client.npcClick = false;
            return;
        }
        client.cancelTasks();
        client.getCombat().resetAttack();
        client.canCast = false;
        client.npcClick = false;
        client.walked = true;
        client.fishing = false;
        final int destX = x, destY = y, height = h;
        //client.getActionAssistant().lockMiniMap(true);
        if (teleportType.equalsIgnoreCase("modern")) {
            client.teled = true;
            client.setBusy(true);
            client.setCanWalk(false);
            client.resetWalkingQueue();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(8939);
            client.npcIndex = 0;
            client.oldNpcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.gfx0(441);
            client.setCanWalk(false);
            client.resetWalkingQueue();
            if (client.sounds == 1) {
                client.getActionAssistant().frame174(202, 050, 000);
            }
            EventManager.getSingleton().addEvent(client, "tele8", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    client.getActionAssistant().startAnimation(8941);
                    client.getActionAssistant().removeAllWindows();
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().lockMiniMap(false);
                    client.doZoning();
                }

            }, TELEPORT_DELAY);
        }
        if (teleportType.equalsIgnoreCase("ancient")) {
            client.teled = true;
            client.stopMovement();
            client.setCanWalk(false);
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(9599);
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;
            client.gfx0(1681);
            client.setCanWalk(false);
            if (client.sounds == 1) {
                client.getActionAssistant().frame174(1048, 020, 000);
            }
            EventManager.getSingleton().addEvent(client, "Tele9", new Event() {
                @Override
                public void execute(EventContainer c) {
                    client.stopMovement();
                    client.setCanWalk(false);
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().resetAnimation();
                    client.getActionAssistant().lockMiniMap(false);
                    client.doZoning();
                }

            }, TELEPORT_DELAY2);
        }
        if (teleportType.equalsIgnoreCase("none")) {
            client.teled = true;
            client.stopMovement();
            client.getActionAssistant().removeAllWindows();
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;


            EventManager.getSingleton().addEvent(client, "Tele10", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().resetAnimation();
                    client.getActionAssistant().lockMiniMap(false);
                    client.doZoning();
                }

            }, TELEPORT_DELAY);
        }
        if (teleportType.equalsIgnoreCase("ladderup")) {
            client.teled = true;
            client.stopMovement();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(828);
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;


            EventManager.getSingleton().addEvent(client, "Tele11", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.teled = false;
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().resetAnimation();
                    client.getActionAssistant().lockMiniMap(false);
                    client.doZoning();

                }

            }, TELEPORT_DELAY);
        }
        //PlayerManager.getSingleton().saveGame(client);	
        client.canCast = true;
        client.npcClick = true;
    }

    public void portTele(int x, int y, final int height, String teleportType) {
        if (client.checkBusy())
            return;
        if (client.checksBusy())
            return;
        client.getCombat().resetAttack();
        client.cancelTasks();
        if (client.tbed == true) {
            sendMessage("You are teleblocked and can't teleport.");
            client.setBusy(false);
            client.setCanWalk(true);
            return;
        }
        client.canCast = false;
        final int destX = x, destY = y;
        if (teleportType.equalsIgnoreCase("modern")) {

            EventManager.getSingleton().addEvent(client, "Tele12", new Event() {

                @Override
                public void execute(EventContainer c) {

                    c.stop();
                }

                @Override
                public void stop() {
                    if (!client.portals()) {
                        return;
                    }
                    client.setBusy(true);
                    client.setCanWalk(false);
                    client.getCombat().resetAttack();
                    //client.getActionAssistant().lockMiniMap(true);
                    client.teled = true;
                    client.stopMovement();
                    client.getActionAssistant().removeAllWindows();
                    client.getActionAssistant().startAnimation(8939);
                    client.npcIndex = 0;
                    client.playerIndex = 0;
                    client.oldPlayerIndex = 0;
                    client.oldNpcIndex = 0;
                    client.gfx0(342);
                    if (client.sounds == 1) {
                        client.getActionAssistant().frame174(202, 050, 000);
                    }
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    client.getActionAssistant().startAnimation(8941);
                    client.setCanWalk(true);
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().lockMiniMap(false);
                }

            }, 5000);
        }
        if (teleportType.equalsIgnoreCase("ancient")) {
            client.stopMovement();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(9599);
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;
            client.gfx0(392);
            if (client.sounds == 1) {
                client.getActionAssistant().frame174(1048, 020, 000);
            }
            EventManager.getSingleton().addEvent(client, "Tele13", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    c.stop();
                }

                @Override
                public void stop() {
                    client.teled = false;
                    client.setCanWalk(true);
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().lockMiniMap(false);
                }

            }, TELEPORT_DELAY2);
        }
        if (teleportType.equalsIgnoreCase("ladderup")) {
            client.stopMovement();
            client.getActionAssistant().removeAllWindows();
            client.getActionAssistant().startAnimation(828);
            client.npcIndex = 0;
            client.playerIndex = 0;
            client.oldPlayerIndex = 0;
            client.oldNpcIndex = 0;

            EventManager.getSingleton().addEvent(client, "Tele14", new Event() {

                @Override
                public void execute(EventContainer c) {
                    client.teleportToX = destX;
                    client.teleportToY = destY;
                    client.teleportToZ = height;
                    c.stop();
                }

                @Override
                public void stop() {
                    client.setCanWalk(true);
                    client.setBusy(false);
                    client.resetWalkingQueue();
                    client.getActionAssistant().lockMiniMap(false);
                }

            }, TELEPORT_DELAY);
        }
        //PlayerManager.getSingleton().saveGame(client);
        client.canCast = true;
    }

    public void moveObPlayer(int x, int y, int h) {
        if (!client.tbed) {
            client.clickTime = 4;
            client.resetWalkingQueue();
            client.teleportToX = x;
            client.teleportToY = y;
            client.teleportToZ = h;
            client.bonusDamage = 0;
        } else {
            client.getActionAssistant().sendMessage("@red@You are teleblocked!");
        }
    }

    public void movePlayer(int x, int y, int h) {
        client.clickTime = 1;
        client.resetWalkingQueue();
        client.teleportToX = x;
        client.teleportToY = y;
        client.teleportToZ = h;
        client.bonusDamage = 0;
        client.doZoning();
    }

    public void reducePrayerLevel() {
        if (client.playerRights > 3) {
            client.prayerDelay = System.currentTimeMillis();
            if (client.playerLevel[5] - 1 > 0) {
                client.playerLevel[5] -= 1;
            } else {
                sendMessage("You have run out of prayer points!");
                if (client.sounds == 1) {
                    frame174(437, 050, 000);
                }
                client.prayOff = 0;
                client.playerLevel[5] = 0;
                resetPrayers();
                //client.protOn = false;
                client.prayerId = -1;
            }
            refreshSkill(5);
            sendFrame126("@gre@ " + client.playerLevel[5] + " / " + getLevelForXP(client.playerXP[5]), 13356);
        } else {
        }
    }

    public void resetPrayers() {
        for (int i = 0; i < 26; i++) {
            //if (i == 10 && client.protOn) {
            //continue;
            //}
            client.prayerActive[i] = false;
            sendFrame36(client.PRAYER_GLOW[i], 0);
        }
        for (int p = 0; p < client.CURSE.length; p++) { // reset prayer glows
            client.curseActive[p] = false;
            sendFrame36(client.CURSE_GLOW[p], 0);
        }
        client.headIcon = -1;
        //client.protOn = false;
        requestUpdates();
    }

    public void resetRegPrayers() {
        for (int i = 0; i < 26; i++) {
            client.prayerActive[i] = false;
            //	sendFrame36(client.PRAYER_GLOW[i], 0);
        }
        //	requestUpdates();
    }

    public void resetCursePrayers() {
        for (int p = 0; p < client.CURSE.length; p++) { // reset prayer glows
            client.curseActive[p] = false;
//								sendFrame36(client.CURSE_GLOW[p], 0);   
        }
        //		 	requestUpdates();
    }

    public void requestUpdates() {
        client.updateRequired = true;
        client.appearanceUpdateRequired = true;
    }

    public void sendFrame106(int i1) {
        client.getOutStream().createFrame(106);
        client.getOutStream().writeByteC(i1);
    }

    public void digSpade() {
        if (client.cluelevel == 0) {
            client.getActionAssistant().Send("Read your clue!");
            return;
        }
        client.digs++;
        if (client.digs >= 100) {
            client.getNRAchievements().checkMisc(client, 0);
        }
        if (client.absX == 3010 && client.absY == 3653 && client.clueString.equalsIgnoreCase("Dig at the exact spot in Level 17 Wild")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 2944 && client.absY == 3332 && client.clueString.equalsIgnoreCase("Dig at the exact spot in the city of Falador")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 3230 && client.absY == 3606 && client.clueString.equalsIgnoreCase("Dig at the exact spot in Level 11 Wild")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 1867 && client.absY == 4366 && client.heightLevel == 1 && client.clueString.equalsIgnoreCase("Dig at the exact spot in Dagganoth Dungeon")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 2901 && client.absY == 5359 && client.heightLevel == 2 && client.clueString.equalsIgnoreCase("Dig at the exact spot in Godwars Dungeon")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 3069 && client.absY == 10260 && client.clueString.equalsIgnoreCase("Dig at the exact spot near King Black Drag entrance")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 2531 && client.absY == 4723) {
            if (client.clueString.equalsIgnoreCase("Dig at the exact spot at Mage Bank")) {
                if (playerHasItem(client.cluelevel, 1)) {
                    client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                    client.getActionAssistant().addItem(2830, 1);
                    client.getActionAssistant().Send("You dig..");
                    client.getActionAssistant().startAnimation(409);
                    client.getActionAssistant().Send("You find a casket!");
                    client.cluelevel = 0;
                    client.clueString = "none";
                    return;
                }
            }
        }
        if (client.absX == 2877 && client.absY == 3491 && client.clueString.equalsIgnoreCase("Dig at the exact spot near Taverly")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 3290 && client.absY == 3903 && client.clueString.equalsIgnoreCase("Dig at the exact spot at Greater Demons")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 2817 && client.absY == 9830 && client.clueString.equalsIgnoreCase("Dig at the exact spot near Black Dragons")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 3179 && client.absY == 3964 && client.clueString.equalsIgnoreCase("Dig at the exact spot near Axe Hut")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 3226 && client.absY == 3444 && client.clueString.equalsIgnoreCase("Dig at the exact spot near Varrock Castle")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 2501 && client.absY == 3496 && client.clueString.equalsIgnoreCase("Dig at the exact spot near Waterfall")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 2678 && client.absY == 3533 && client.clueString.equalsIgnoreCase("Dig at the exact spot near Coal Trucks Track")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 2633 && client.absY == 3230 && client.clueString.equalsIgnoreCase("Dig at the exact spot near Ardougne")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 3225 && client.absY == 3205 && client.clueString.equalsIgnoreCase("Lumbridge swamp has a fern?")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 2540 && client.absY == 2949 && client.clueString.equalsIgnoreCase("I can see a landing strip from here!")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 3469 && client.absY == 3520 && client.clueString.equalsIgnoreCase("Dig at the exact spot near Canfis")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                return;
            }
        }
        if (client.absX == 3129 && client.absY == 3684 && client.clueString.equalsIgnoreCase("Dig at the exact spot near Graveyard Dragons")) {
            if (playerHasItem(client.cluelevel, 1)) {
                client.getActionAssistant().deleteItem(client.cluelevel, getItemSlot(client.cluelevel), 1);
                client.getActionAssistant().addItem(2830, 1);
                client.getActionAssistant().Send("You dig..");
                client.getActionAssistant().startAnimation(409);
                client.getActionAssistant().Send("You find a casket!");
                client.cluelevel = 0;
                client.clueString = "none";
                //return;
            }
        }
    }

    /**
     * Weapon Requirements
     */
    public void resetAnimation() {
        client.getActionAssistant().startAnimation(2552);
        client.getCombatEmotes().getPlayerAnimIndex();
        client.getActionAssistant().requestUpdates();


    }

    public void getRequirements(String itemName, int itemId) {
        client.herbLevelReq = client.miningLevelReq = client.fletchLevelReq = client.craftLevelReq = client.hitLevelReq = client.agilityLevelReq = client.woodLevelReq = client.fishLevelReq = client.slayerLevelReq = client.thiefLevelReq = client.cookLevelReq = client.fireLevelReq = client.rcLevelReq = client.prayerLevelReq = client.smithingLevelReq = client.farmingLevelReq = client.attackLevelReq = client.defenceLevelReq = client.strengthLevelReq = client.rangeLevelReq = client.magicLevelReq = 0;
        switch (itemId) {
            case 6128: // Rock-shell helm
                client.defenceLevelReq = 40;
                return;
            case 6129: //Rock-shell plate
                client.defenceLevelReq = 40;
                return;
            case 6130: //Rock-shell legs 
                client.defenceLevelReq = 40;
                return;
            case 6524: //Toktz-ket-xil
                client.defenceLevelReq = 60;
                return;
            case 9185: //Rune crossbow
                client.rangeLevelReq = 61;
                return;
            case 13870:
            case 13873:
            case 13876:
                client.defenceLevelReq = 78;
                client.rangeLevelReq = 78;
                return;
            case 18353:  //Chaotic maul
                client.attackLevelReq = 80;
                return;
			case 16425:// Primal maul
            case 16909: // Primal 2h sword
                client.attackLevelReq = 99;
                return;
            case 18351: // Chaotic longsword
                client.attackLevelReq = 80;
                return;
            case 13899:  //Vesta's longsword
                client.attackLevelReq = 78;
                return;
            case 13905: // Vesta's spear
                client.attackLevelReq = 78;
                return;
            case 13902: // Statius's warhammer
                client.attackLevelReq = 78;
                return;
            case 11730:  //Saradomin sword
                client.attackLevelReq = 70;
                return;
            case 20072: // Dragon defender
                client.defenceLevelReq = 60;
                client.attackLevelReq = 60;
                return;
            case 10390: // Saradomin coif
                client.defenceLevelReq = 78;
                return;
            case 10386: // Saradomin d'hide
                client.defenceLevelReq = 78;
                return;
            case 10388: // Saradomin chaps
                client.defenceLevelReq = 78;
                return;
            case 10384: // Saradomin bracers
                client.defenceLevelReq = 78;
                return;
            case 10374: // Zamorak coif
                client.defenceLevelReq = 78;
                return;
            case 10370: // Zamorak d'hide
                client.defenceLevelReq = 78;
                return;
            case 10372: // Zamorak chaps
                client.defenceLevelReq = 78;
                return;
            case 10368: // Zamorak bracers
                client.defenceLevelReq = 78;
                return;
            case 10382: // Guthix coif
                client.defenceLevelReq = 78;
                return;
            case 10378: // Guthix d'hide
                client.defenceLevelReq = 78;
                return;
            case 10380: // Guthix chaps
                client.defenceLevelReq = 78;
                return;
            case 10376: // Guthix bracers
                client.defenceLevelReq = 78;
                return;
            case 13263:  //slayer helmet
                client.defenceLevelReq = 10;
                return;
            case 10550: // ranger hat
                client.defenceLevelReq = 40;
                return;
            case 10548: // fighter hat
                client.defenceLevelReq = 40;
                return;
            case 19785: // elite void knight robe top
                client.defenceLevelReq = 42;
                return;
            case 19786: // elite void knight robe bottom
                client.defenceLevelReq = 42;
                return;
            case 3122: // granite shield
                client.defenceLevelReq = 50;
                return;
            case 6528: // tzhaar-ket-om
                client.strengthLevelReq = 60;
                return;
            case 16755: // celestial hood
                client.defenceLevelReq = 99;
                return;
            case 16865: // celestial robe bottom
                client.defenceLevelReq = 99;
                return;
            case 16931: // celestial shoes
                client.defenceLevelReq = 99;
                return;
            case 17171: // celestial gloves
                client.defenceLevelReq = 99;
                return;
            case 17237: // celestial robe top
                client.defenceLevelReq = 99;
                return;
            case 17061: // sagitarrian coif
                client.defenceLevelReq = 99;
                return;
            case 17193: // sagitarrian body
                client.defenceLevelReq = 99;
                return;
            case 17215: // sagitarrian vambraces
                client.defenceLevelReq = 99;
                return;
            case 17317: // sagitarrian boots
                client.defenceLevelReq = 99;
                return;
            case 17339: // sagitarrian chaps
                client.defenceLevelReq = 99;
                return;
            case 22366:
            case 22367:
            case 22368:
            case 22369:
                client.magicLevelReq = 80;
                return;
            case 22362:
            case 22363:
            case 22364:
            case 22365:
                client.rangeLevelReq = 80;
                return;
            case 14499: // dagon'hai hat
                client.defenceLevelReq = 20;
                return;
            case 14497: // dagon'hai robe top
                client.defenceLevelReq = 20;
                return;
            case 14501: // dagon'hai robe bottom
                client.defenceLevelReq = 20;
                return;
            case 18349: // chaotic rapier
                client.attackLevelReq = 80;
                return;
            case 18357: // chaotic crossbow
                client.rangeLevelReq = 80;
                return;
            case 6137: // skeletal helm
                client.defenceLevelReq = 40;
                return;
            case 6139: // skeletal top
                client.defenceLevelReq = 40;
                return;
            case 6141: // skeletal bottoms
                client.defenceLevelReq = 40;
                return;
            case 6147: // skeletal boots
                client.defenceLevelReq = 40;
                return;
            case 6153: // skeletal gloves
                client.defenceLevelReq = 40;
                return;
            case 21467: // trickster helm
                client.defenceLevelReq = 80;
                return;
            case 21468: // trickster robe
                client.defenceLevelReq = 80;
                return;
            case 21469: // trickster robe legs
                client.defenceLevelReq = 80;
                return;
            case 21470: // trickster gloves
                client.defenceLevelReq = 80;
                return;
            case 21471: // trickster boots
                client.defenceLevelReq = 80;
                return;
            case 17017: // celestial catalytic staff
                client.magicLevelReq = 99;
                return;
            case 19893: // spirit cape
                client.defenceLevelReq = 50;
                return;

            case 19443:
            case 19445:
            case 19447:
            case 19449:
            case 19451:
            case 19453:
            case 19455:
            case 19457:
            case 19459:
            case 19461:
            case 19463:
            case 19465:
                client.defenceLevelReq = 40;
                client.rangeLevelReq = 70;
                return;

            case 19308:
            case 19311:
            case 19314:
            case 19317:
            case 19320:
                client.prayerLevelReq = 65;
                return;
            case 19784:
                client.attackLevelReq = 78;
                return;

            case 20159:
            case 20163:
            case 20167:
                client.magicLevelReq = 80;
                client.defenceLevelReq = 80;
                return;

            case 20135:
            case 20139:
            case 20143:
                client.strengthLevelReq = 80;
                client.defenceLevelReq = 80;
                return;
            case 18363:
            case 18361:
            case 18359:
                client.defenceLevelReq = 80;
                return;
            case 20147:
            case 20151:
            case 20155:
                client.rangeLevelReq = 80;
                client.defenceLevelReq = 80;
                return;

            case 16291:
                client.defenceLevelReq = 90;
                return;
            case 16357:
                client.defenceLevelReq = 90;
                return;
            case 16687:
                client.defenceLevelReq = 90;
                return;
            case 16709:
                client.defenceLevelReq = 90;
                return;
            case 17257:
                client.defenceLevelReq = 90;
                return;
            case 17359:
                client.defenceLevelReq = 90;
                return;
            case 17361:
                client.defenceLevelReq = 99;
                return;
            case 13869:
                client.attackLevelReq = 70;
                return;
            case 10887:
                client.attackLevelReq = 60;
                return;
            case 13901:
                client.attackLevelReq = 80;
                return;
            case 13904:
                client.attackLevelReq = 80;
                return;
            case 13907:
                client.attackLevelReq = 80;
                return;


            case 13860:
            case 13863:
            case 13866:
            case 13889:
            case 13895:
            case 13886:
            case 13892:
            case 13898:
                client.defenceLevelReq = 20;
                return;
            case 21736:
            case 21744:
            case 21752:
            case 21760:
                client.defenceLevelReq = 70;
                client.prayerLevelReq = 70;
                client.magicLevelReq = 70;
                return;
            case 22482:
            case 22490:
            case 22486:
                client.magicLevelReq = 85;
                client.defenceLevelReq = 85;
                return;
            case 22494:
                client.magicLevelReq = 80;
                return;
            case 10330:
            case 10338:
            case 10348:
            case 10332:
            case 10346:
            case 10334:
            case 10342:
            case 10350:
            case 10352:
            case 10340:
            case 10336:
                client.defenceLevelReq = 30;
                return;
            case 6914:
            case 6889:
                client.magicLevelReq = 60;
                return;
            case 843:
            case 845:
                client.rangeLevelReq = 5;
                return;
            case 847:
            case 849:
                client.rangeLevelReq = 20;
                return;
            case 851:
            case 853:
                client.rangeLevelReq = 30;
                return;
            case 855:
            case 857:
                client.rangeLevelReq = 40;
                return;
            case 859:
            case 861:
                client.rangeLevelReq = 50;
                return;
            case 4827:
                client.rangeLevelReq = 60;
                return;
            case 15241:
                client.rangeLevelReq = 75;
            case 1169:
                client.rangeLevelReq = 20;
                return;
            case 1131:
            case 2589:
                client.rangeLevelReq = 1;
                client.defenceLevelReq = 10;
                return;
            case 1133:
                client.rangeLevelReq = 20;
                client.defenceLevelReq = 20;
                return;
            case 7959:
                client.attackLevelReq = 90;
                client.strengthLevelReq = 90;
                return;
            case 1097:
                client.rangeLevelReq = 20;
                return;
            case 4151: // if you don't want to use names 
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
                client.attackLevelReq = 70;
                return;
            case 21777:
                client.magicLevelReq = 77;
                client.attackLevelReq = 40;
                return;

            case 10828:
            case 7841:
                client.defenceLevelReq = 45;
                return;
            case 2621:
                client.defenceLevelReq = 70;
                return;
            case 1313: // if you don't want to use names 
                client.attackLevelReq = 75;
                return;
            case 6724: // seercull
                client.rangeLevelReq = 60; // idk if that is correct
                return;

            case 4220:
            case 4221:
            case 4222:
            case 4223:
                client.rangeLevelReq = 99;
                client.magicLevelReq = 99;
                client.defenceLevelReq = 75;
                return;
            case 2497:
            case 2491:
                client.rangeLevelReq = 70;

                return;
            case 10551:
                client.defenceLevelReq = 40;
                return;
            case 18355:
                client.magicLevelReq = 80;
                return;
            case 15486:
                client.magicLevelReq = 75;
                client.attackLevelReq = 75;
                return;
            case 3101:
                client.attackLevelReq = 40;
                return;
            case 3100:
                client.attackLevelReq = 30;
                return;
            case 4153:
                client.attackLevelReq = 50;
                return;
            case 3099:
                client.attackLevelReq = 20;
                return;
            case 13744:
            case 13740:
            case 13742:
            case 13738:
                client.defenceLevelReq = 75;
                client.prayerLevelReq = 70;
                return;
            case 13734:
                client.defenceLevelReq = 40;
                client.prayerLevelReq = 55;
                return;
            case 13736:
                client.defenceLevelReq = 70;
                client.prayerLevelReq = 60;
                return;
            case 7806:
            case 7807:
            case 7808:
            case 7809:
                client.attackLevelReq = 60;
                return;
            case 3753:
            case 3749:
            case 3751:
            case 3755:
                client.defenceLevelReq = 45;
                client.attackLevelReq = 60;
                return;
            case 2501:
                client.defenceLevelReq = 40;
                client.rangeLevelReq = 60;
                return;
            case 1135:
                client.defenceLevelReq = 40;
                client.rangeLevelReq = 40;
                return;
            case 2499:
                client.defenceLevelReq = 40;
                client.rangeLevelReq = 50;
                return;
            case 11694:
            case 11696:
            case 11700:
            case 7896:
            case 11698:
                client.attackLevelReq = 75;
                return;
            case 18346:
                client.magicLevelReq = 48;
                return;
            case 18333:
                client.magicLevelReq = 30;
                return;
            case 18371:
                client.magicLevelReq = 45;
                return;
            case 15773:
            case 16955:
                client.strengthLevelReq = 99;
                return;
            case 16907:
                client.attackLevelReq = 99;
                return;
            case 16359:
                client.defenceLevelReq = 99;
                return;
            case 16689:
                client.defenceLevelReq = 99;
                return;
            case 16711:
                client.defenceLevelReq = 99;
                return;
            case 17259:
                client.defenceLevelReq = 99;
                return;
            case 16293:
                client.defenceLevelReq = 99;
                return;
            case 18365:
                client.attackLevelReq = 45;
                return;
            case 18367:
                client.attackLevelReq = 45;
                return;
            case 18369:
                client.attackLevelReq = 45;
                return;
            case 7901:
            case 7822:
                client.attackLevelReq = 70;
                return;
            case 7460:
                client.defenceLevelReq = 11;
                return;
            case 22359:
            case 22358:
            case 22360:
            case 22361:
                client.attackLevelReq = 80;
                client.defenceLevelReq = 65;
                return;
            case 21790:
            case 21793:
            case 21787:
            case 11283:
                client.defenceLevelReq = 75;
                return;
            case 20171:
					client.rangeLevelReq = 99;
					client.magicLevelReq = 99;
					client.defenceLevelReq = 75;
					return;
            case 2503:
                client.defenceLevelReq = 40;
                client.rangeLevelReq = 70;
                return;
            case 2653:
            case 2655:
            case 2657:
            case 2659:
            case 2661:
            case 2663:
            case 2665:
            case 2667:
            case 2669:
            case 2671:
            case 2673:
            case 2675:
            case 3478:
            case 3479:
            case 3480:
            case 3481:
            case 3483:
            case 3485:
            case 3486:
            case 3488:
            case 3676:
            case 3748:
                client.defenceLevelReq = 40;
                return;
            case 11663:
            case 8839:
            case 8840:
            case 11664:
            case 11665:
                client.defenceLevelReq = 42;
                return;
            case 7398:
            case 7399:
            case 7400:
                client.defenceLevelReq = 20;
                client.magicLevelReq = 40;
                return;
            case 11724:
            case 11726:
                client.defenceLevelReq = 65;
                client.attackLevelReq = 70;
                return;
            case 11718:
            case 11720:
            case 11722:
                client.defenceLevelReq = 70;
                client.rangeLevelReq = 70;
                return;
            case 5574:
            case 5575:
            case 5576:
                client.defenceLevelReq = 20;
                return;
            case 7454:
                client.defenceLevelReq = 1;
                return;
            case 7455:
                client.defenceLevelReq = 1;
                return;
            case 7456:
                client.defenceLevelReq = 1;
                return;
            case 7457:
                client.defenceLevelReq = 1;
                return;
            case 7458:
                client.defenceLevelReq = 1;
                return;
            case 7459:
                client.defenceLevelReq = 1;
                return;
            case 7461:
                client.defenceLevelReq = 41;
                return;
            case 7462:
                client.defenceLevelReq = 41;
                return;
            case 4675:
                client.attackLevelReq = 50;
                client.magicLevelReq = 50;
                return;
            //skill capes
            case 9747:
            case 9748:
                client.attackLevelReq = 99;
                return;
            case 9750:
            case 9751:
                client.strengthLevelReq = 99;
                return;
            case 9754:
            case 9753:
                client.defenceLevelReq = 99;
                return;
            case 9756:
            case 9757:
                client.rangeLevelReq = 99;
                return;
            case 9759:
            case 9760:
                client.prayerLevelReq = 99;
                return;
            case 9762:
            case 9763:
                client.magicLevelReq = 99;
                return;
            case 9804:
            case 9805:
                client.fireLevelReq = 99;
                return;
            case 9765:
            case 9766:
                client.rcLevelReq = 99;
                return;
            case 9768:
            case 9769:
                client.hitLevelReq = 99;
                return;
            case 9771:
            case 9772:
                client.agilityLevelReq = 99;
                return;
            case 9774:
            case 9775:
                client.herbLevelReq = 99;
                return;
            case 9777:
            case 9778:
                client.thiefLevelReq = 99;
                return;
            case 9780:
            case 9781:
                client.craftLevelReq = 99;
                return;
            case 9783:
            case 9784:
                client.fletchLevelReq = 99;
                return;
            case 9786:
            case 9787:
                client.slayerLevelReq = 99;
                return;
            case 9792:
            case 9793:
                client.miningLevelReq = 99;
                return;
            case 9795:
            case 9796:
                client.smithingLevelReq = 99;
                return;
            case 9798:
            case 9799:
                client.fishLevelReq = 99;
                return;
            case 9801:
            case 9802:
                client.cookLevelReq = 99;
                return;
            case 9807:
            case 9808:
                client.woodLevelReq = 99;
                return;
            case 9810:
            case 9811:
                client.farmingLevelReq = 99;
                return;
            case 13887:
            case 13893:
            case 13884:
            case 13896:
            case 13890:
            case 13858:
            case 13861:
            case 13864:
                client.defenceLevelReq = 78;
                return;
            case 13879:
            case 13883:
                client.rangeLevelReq = 75;
                return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("mystic")) {
            if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("staff")) {
                client.magicLevelReq = 20;
                client.attackLevelReq = 40;
            } else {
                client.magicLevelReq = 40;
                client.defenceLevelReq = 20;
            }
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("infinity")) {
            client.magicLevelReq = 50;
            client.defenceLevelReq = 25;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("splitbark")) {
            client.magicLevelReq = 40;
            client.defenceLevelReq = 40;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("rock")) {
            client.defenceLevelReq = 45;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("green")) {
            if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("d'hide")) {
                client.rangeLevelReq = 40;
            }
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("blue")) {
            if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("d'hide")) {
                client.rangeLevelReq = 50;
            }
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("red")) {
            if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("d'hide")) {
                client.rangeLevelReq = 60;
            }
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("black")) {
            if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("d'hide")) {
                client.rangeLevelReq = 70;
            }
        }

        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("bronze")) {
            if (!Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("knife") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("dart") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("javelin") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("thrownaxe")) {
                client.attackLevelReq = client.defenceLevelReq = 1;
            }
            return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("black")) {
            if (!Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("cavalier") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("knife") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("dart") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("javelin") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("thrownaxe")) {
                client.attackLevelReq = client.defenceLevelReq = 10;
            }
            return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("iron")) {
            if (!Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("knife") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("dart") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("javelin") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("thrownaxe")) {
                client.attackLevelReq = client.defenceLevelReq = 1;
            }
            return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("steel")) {
            if (!Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("knife") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("dart") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("javelin") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("thrownaxe")) {
                client.attackLevelReq = client.defenceLevelReq = 5;
            }
            return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("mithril")) {
            if (!Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("knife") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("dart") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("javelin") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("thrownaxe")) {
                client.attackLevelReq = client.defenceLevelReq = 20;
            }
            return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("adamant")) {
            if (!Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("knife") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("dart") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("javelin") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("thrownaxe")) {
                client.attackLevelReq = client.defenceLevelReq = 30;
            }
            return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("rune")) {
            if (!Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("crafter") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("Crossbow") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("knife") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("dart") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("javelin") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("thrownaxe")) {
                client.attackLevelReq = client.defenceLevelReq = 40;
            }
            return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("anti")) {
            client.attackLevelReq = client.defenceLevelReq = 1;
            return;
        }

        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("dragon")) {
            if (!Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("hide") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("green") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("red") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("blue") && !Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("black")) {
                client.attackLevelReq = client.defenceLevelReq = 60;
                return;
            }
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("anti")) {
            client.attackLevelReq = client.defenceLevelReq = 1;
            return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("crystal")) {
            if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("shield")) {
                client.defenceLevelReq = 70;
            } else {
                client.rangeLevelReq = 70;
            }
            return;
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("ahrim")) {
            if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("staff")) {
                client.magicLevelReq = 70;
                client.attackLevelReq = 70;
            } else {
                client.magicLevelReq = 70;
                client.defenceLevelReq = 70;
            }
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("karil")) {
            if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("crossbow")) {
                client.rangeLevelReq = 70;
            } else {
                client.rangeLevelReq = 70;
                client.defenceLevelReq = 70;
            }
        }
        if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("verac") || Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("guthan") || Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("dharok") || Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("torag")) {

            if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("hammers")) {
                client.attackLevelReq = 70;
                client.strengthLevelReq = 70;
            } else if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("axe")) {
                client.attackLevelReq = 70;
                client.strengthLevelReq = 70;
            } else if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("warspear")) {
                client.attackLevelReq = 70;
                client.strengthLevelReq = 70;
            } else if (Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase().contains("flail")) {
                client.attackLevelReq = 70;
                client.strengthLevelReq = 70;
            } else {
                client.defenceLevelReq = 70;
            }
        }


    }

    public void Craft(final int skillAdd, final int itemDelete, final int itemAdd, final int skillNeeded) {
        if (client.playerLevel[12] < skillNeeded) {
            Send("You need " + skillNeeded + " crafting to cut this gem.");
            return;
        }
        if (client.checkBusy()) {
            return;
        }

        client.setBusy(true);
        client.getActionAssistant().startAnimation(885);
        EventManager.getSingleton().addEvent(client, "craft action", new Event() {

            @Override
            public void execute(EventContainer c) {
                client.getActionAssistant().addSkillXP(skillAdd, 12);
                client.getActionAssistant().deleteItem(itemDelete, getItemSlot(itemDelete), 1);
                client.getActionAssistant().addItem(itemAdd, 1);
                client.getActionAssistant().Send("You cut the gem.");
                client.getActionAssistant().refreshSkill(12);
                client.getActionAssistant().requestUpdates();
                c.stop();
            }

            @Override
            public void stop() {
                //client.getActionAssistant().startAnimation(-1);
                client.setBusy(false);

            }

        }, 1000);

    }

    /**
     * Eating food
     */

    public void eatFood(int heal, int delay, int itemId, int itemSlot) {

        if (client.duelRule[6]) {
            sendMessage("Food has been disabled in this duel!");
            return;
        } else {
				/*if (client.duelStatus >4) {
					Client o = (Client) PlayerManager.getSingleton().getPlayers()[client.duelingWith];
					if (o != null) {
						if (o.duelStatus > 4 && o.duelRule[6]) {
							try {
										BanProcessor.banUser(client.playerName,"");
										client.kick();
										client.disconnected = true;
							} catch (IOException e) {
										e.printStackTrace();
							}
						}
					}
				}*/

        }

        if (!client.canEat) {
            return;
        }
        if (!client.isDead && System.currentTimeMillis() - client.foodDelay > delay) {
            client.attackTimer += 1000;
            if (playerHasItem(itemId, 1, itemSlot)) {
                if (getLevelForXP(client.playerXP[3]) > client.playerLevel[3]) {
                    sendMessage("You eat the " + Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase() + " and it restores some health.");
                } else {
                    sendMessage("You eat the " + Server.getItemManager().getItemDefinition(itemId).getName().toLowerCase() + ".");
                }
                client.foodDelay = System.currentTimeMillis();
                int hpBonus = getMaxHP();
                if (hpBonus > 0) {
                    if (client.playerLevel[3] + heal >= (getLevelForXP(client.playerXP[3]) + hpBonus) && itemId != 15272) {
                        client.playerLevel[3] = getLevelForXP(client.playerXP[3]) + hpBonus;
                    } else {
                        if (itemId == 15272 && (client.playerLevel[3] + heal >= getLevelForXP(client.playerXP[3]) + 10 + hpBonus)) {
                            client.playerLevel[3] = getLevelForXP(client.playerXP[3]) + 10 + hpBonus;
                        } else {
                            client.playerLevel[3] += heal;
                        }
                    }
                } else if ((client.playerLevel[3] + heal >= getLevelForXP(client.playerXP[3])) && itemId != 15272) {
                    client.playerLevel[3] = getLevelForXP(client.playerXP[3]);
                } else {
                    if (itemId == 15272 && (client.playerLevel[3] + heal >= getLevelForXP(client.playerXP[3]) + 10)) {
                        client.playerLevel[3] = getLevelForXP(client.playerXP[3]) + 10;
                    } else {
                        client.playerLevel[3] += heal;
                    }
                }


                refreshSkill(3);
                startAnimation(829);
                deleteItem(itemId, itemSlot, 1);
                switch (itemId) {
                    case 1891:
                        addItem(1893, 1);
                        break;

                    case 1893:
                        addItem(1895, 1);
                        break;
                    case 4049:
                        client.playerLevel[5] += 19;
                        if (client.playerLevel[5] > client.getLevelForXP(client.playerXP[5])) {
                            client.playerLevel[5] = client.getLevelForXP(client.playerXP[5]);
                        }
                        client.getActionAssistant().refreshSkill(5);
                        client.updateRequired = true;
                        client.appearanceUpdateRequired = true;
                        break;
                }
                if (client.sounds == 1) {
                    frame174(317, 050, 000);
                }
                client.getCombat().resetAttack(); //eat and stop attacking
            }
            EventManager.getSingleton().addEvent(client, "drinkpot", new Event() {

                @Override
                public void execute(EventContainer c) {

                    c.stop();
                }

                @Override
                public void stop() {
                    requestUpdates();
                    client.setBusy(false);

                }

            }, 1400);
        } else {
        }
    }

    public void drinkPot(int itemId, int itemSlot) {
        if (client.checkBusy()) {
            return;
        }
        if (client.duelRule[5]) {
            sendMessage("Food has been disabled in this duel!");
            return;
        }
        client.setBusy(true);
        client.getActionAssistant().startAnimation(829);
        if (client.sounds == 1) {
            client.getActionAssistant().frame174(334, 050, 000);
        }

        EventManager.getSingleton().addEvent(client, "drinkpot", new Event() {

            @Override
            public void execute(EventContainer c) {

                c.stop();
            }

            @Override
            public void stop() {
                requestUpdates();
                client.setBusy(false);

            }

        }, 900);
    }

    public void cameraShake(int type, int shake, int bobbing, int i4) {
        client.getOutStream().createFrame(35);
        client.getOutStream().writeByte(type); // 0-4 different shakes, like 0 is horizantal shake, 4 is vertical shake
        client.getOutStream().writeByte(shake); // How hard it's gonna shake
        client.getOutStream().writeByte(bobbing); // Camera bobbing up and down??
        client.getOutStream().writeByte(i4); // IDK?
        client.flushOutStream();
    }

    public void multiSmelt(final int ID, final int orecount) {
        client.walked = false;
        if (!smelt(ID)) return;
        EventManager.getSingleton().addEvent(client, "multismithing", new Event() {
            int amountLeft = orecount - 1;
            boolean success = true;

            public void stop() {
                client.getActionAssistant().startAnimation(2552);
                client.setBusy(false);
            }

            public void execute(EventContainer c) {
                if (amountLeft == 0 || client.walked || !success) {
                    client.setBusy(false);
                    c.stop();
                    return;
                }
                if (amountLeft > 0) {
                    amountLeft--;
                    if (client.walked || !success) {
                        client.setBusy(false);
                        c.stop();
                        return;
                    }
                    success = smelt(ID);
                } else {
                    client.setBusy(false);
                    c.stop();
                }
            }
        }, 2100);
    }

    public boolean smelt(int ID) {
        int BarXP = 0;
        int CoalID = 0;
        int CoalID2 = 453;
        int coalamount = 0;
        removeAllWindows();
        switch (ID) {
            case 2349://Bronze Bar!
                if (getLevelForXP(client.playerXP[13]) < 0) {
                    showDialogue("You need a smithing level of 1 to smelt bronze bars");
                    return false;
                }
                BarXP = 62;
                CoalID = 436;
                CoalID2 = 438;
                coalamount = 1;
                break;
            case 2355://Silver bar!
                if (getLevelForXP(client.playerXP[13]) < 20) {
                    showDialogue("You need a smithing level of@blu@ 20 @bla@to smelt silver bars");
                    return false;
                }
                BarXP = 137;
                CoalID = 442;
                CoalID2 = -1;
                coalamount = 0;
                break;
            case 2357://gold bar!
                if (getLevelForXP(client.playerXP[13]) < 40) {
                    showDialogue("You need a smithing level of@blu@ 40 @bla@to smelt gold bars");
                    return false;
                }
                BarXP = 225;
                CoalID = 444;
                CoalID2 = -1;
                coalamount = 0;
                break;
            case 2351://Iron bar!
                if (getLevelForXP(client.playerXP[13]) < 15) {
                    showDialogue("You need a smithing level of@blu@ 15 @bla@to smelt iron bars");
                    return false;
                }
                BarXP = 125;
                CoalID = 440;
                CoalID2 = -1;
                coalamount = 0;
                if (Misc.random(3) == 1) {
                    showDialogue("You accidently drop the iron ore into the oven!");
                    return false;
                }
                break;
            case 2353://Steel bar!
                if (getLevelForXP(client.playerXP[13]) < 30) {
                    showDialogue("You need a smithing level of@blu@ 30 @bla@to smelt steel bars");
                    return false;
                }
                BarXP = 175;
                CoalID = 440;
                CoalID2 = 453;
                coalamount = 2;
                break;
            case 2359://Mithril bar!
                if (getLevelForXP(client.playerXP[13]) < 50) {
                    showDialogue("You need a smithing level of@blu@ 50 @bla@to smelt mithril bars");
                    return false;
                }
                BarXP = 300;
                CoalID = 447;
                CoalID2 = 453;
                coalamount = 4;
                break;
            case 2361://Adamantite bar!
                if (getLevelForXP(client.playerXP[13]) < 70) {
                    showDialogue("You need a smithing level of@blu@ 70 @bla@to smelt adamant bars");
                    return false;
                }
                BarXP = 375;
                CoalID = 449;
                CoalID2 = 453;
                coalamount = 6;
                break;
            case 2363://Runite bar!
                if (getLevelForXP(client.playerXP[13]) < 85) {
                    showDialogue("You need a smithing level of@blu@ 85 @bla@to smelt runite bars");
                    return false;
                }
                BarXP = 500;
                CoalID = 451;
                CoalID2 = 453;
                coalamount = 8;
                break;
        }
        //Send("CoaldID: "+CoalID+" CoalID2: " +CoalID2);
        if (CoalID2 != -1) {
            if (!playerHasItem(CoalID, 1)) {
                showDialogue("You need 1 " + Server.getItemManager().getItemDefinition(CoalID).getName() + " and " + coalamount + " " + Server.getItemManager().getItemDefinition(CoalID2).getName() + " to make a " + Server.getItemManager().getItemDefinition(ID).getName());
                return false;
            }
        }
        if (coalamount > 0) {
            if (getItemAmount(CoalID2) < coalamount) {
                showDialogue("You need 1 " + Server.getItemManager().getItemDefinition(CoalID).getName() + " and " + coalamount + " " + Server.getItemManager().getItemDefinition(CoalID2).getName() + " to make a " + Server.getItemManager().getItemDefinition(ID).getName());
                return false;
            }
        }
        if (!playerHasItemAmount(CoalID, 1)) {
            showDialogue("You need @blu@1@bla@ " + Server.getItemManager().getItemDefinition(CoalID).getName() + " to make a " + Server.getItemManager().getItemDefinition(ID).getName());
            return false;
        }

        if (coalamount > 0)
            for (int i = 0; i < coalamount; i++)
                deleteItem(CoalID2, 1);
        deleteItem(CoalID, 1);
        addSkillXP(BarXP, 13);
        client.getActionAssistant().refreshSkill(13);
        addItem(ID, 1);
        startAnimation(899);
        if (client.sounds == 1) {
            client.getActionAssistant().frame174(352, 000, 010);
        }
        return true;
    }

    public int daggers[] = {1205, 1203, 1207, 1209, 1211, 1213};
    public int axes[] = {1351, 1349, 1353, 1355, 1357, 1359};
    public int swords[] = {1277, 1279, 1281, 1285, 1287, 1289};
    public int scimitars[] = {1321, 1323, 1325, 1329, 1331, 1333};
    public int longswords[] = {1291, 1293, 1295, 1299, 1301, 1303};
    public int maces[] = {1422, 1420, 1424, 1428, 1430, 1432};
    public int warhammers[] = {1337, 1335, 1339, 1343, 1345, 1347};
    public int battleaxes[] = {1375, 1363, 1365, 1369, 1371, 1373};
    public int twohs[] = {1307, 1309, 1311, 1315, 1317, 1319};
    public int kiteshields[] = {1189, 1191, 1193, 1197, 1199, 1201};
    public int sqshields[] = {1173, 1175, 1177, 1181, 1183, 1185};
    public int fullhelms[] = {1155, 1153, 1157, 1159, 1161, 1163};
    public int medhelms[] = {1139, 1137, 1141, 1143, 1145, 1147};
    public int platebodies[] = {1117, 1115, 1119, 1121, 1123, 1127};
    public int chainbodies[] = {1103, 1101, 1105, 1109, 1111, 1113};
    public int platelegs[] = {1075, 1067, 1069, 1071, 1073, 1079};
    public int plateskirt[] = {1087, 1081, 1083, 1085, 1091, 1093};
    public int arrowtips[] = {39, 40, 41, 42, 43, 44};
    public int darttips[] = {819, 820, 821, 822, 823, 824};
    public int claws[] = {3095, 3096, 3097, 3099, 3100, 3101};
    public int knives[] = {864, 863, 865, 866, 867, 868};
    public int smithing = 0;

    public void smithinginterface() {
        addItemToSmith(plateskirt[smithing], 2, 1121, 1);
        addItemToSmith(platelegs[smithing], 1, 1121, 1);
        addItemToSmith(darttips[smithing], 0, 1123, 10);
        addItemToSmith(arrowtips[smithing], 1, 1123, 15);
        addItemToSmith(battleaxes[smithing], 3, 1120, 1);
        addItemToSmith(warhammers[smithing], 2, 1120, 1);
        addItemToSmith(daggers[smithing], 0, 1119, 1);
        addItemToSmith(axes[smithing], 0, 1120, 1);
        addItemToSmith(swords[smithing], 1, 1119, 1);
        addItemToSmith(maces[smithing], 1, 1120, 1);
        addItemToSmith(scimitars[smithing], 2, 1119, 1);
        addItemToSmith(longswords[smithing], 3, 1119, 1);
        addItemToSmith(battleaxes[smithing], 3, 1120, 1);
        addItemToSmith(chainbodies[smithing], 0, 1121, 1);
        addItemToSmith(platebodies[smithing], 3, 1121, 1);
        addItemToSmith(kiteshields[smithing], 3, 1122, 1);
        addItemToSmith(sqshields[smithing], 2, 1122, 1);
        addItemToSmith(medhelms[smithing], 0, 1122, 1);
        addItemToSmith(fullhelms[smithing], 1, 1122, 1);
        addItemToSmith(twohs[smithing], 4, 1119, 1);
        addItemToSmith(claws[smithing], 4, 1120, 1);
        addItemToSmith(knives[smithing], 2, 1123, 10);
        showInterface(994);
    }

    public void addItemToSmith(int id, int slot, int column, int amount) {

        client.getOutStream().createFrameVarSizeWord(34); // init item to smith screen
        client.getOutStream().writeWord(column); // Column Across Smith Screen
        client.getOutStream().writeByte(4); // Total Rows?
        client.getOutStream().writeDWord(slot); // Row Down The Smith Screen
        client.getOutStream().writeWord(id + 1); // item
        client.getOutStream().writeByte(amount); // how many there are?
        client.getOutStream().endFrameVarSizeWord();
    }

    public int smithamount = 0;
    public int Bar[] = {2349, 2351, 2353, 2359, 2361, 2363};
    public int barXP[] = {125, 250, 375, 500, 625, 750};

    public void multiSmith(final int slot, final int interfaceID, final int amount) {
        client.walked = false;
        if (!smith(slot, interfaceID)) return;
        EventManager.getSingleton().addEvent(client, "multismithing", new Event() {
            int amountLeft = amount - 1;
            boolean success = true;

            public void stop() {
                client.getActionAssistant().startAnimation(2552);
                client.setBusy(false);
            }

            public void execute(EventContainer c) {
                if (amountLeft == 0 || client.walked || !success) {
                    client.setBusy(false);
                    c.stop();
                    return;
                }
                if (amountLeft > 0) {
                    amountLeft--;
                    if (client.walked || !success) {
                        client.setBusy(false);
                        c.stop();
                        return;
                    }
                    success = smith(slot, interfaceID);
                } else {
                    client.setBusy(false);
                    c.stop();
                }
            }
        }, 1000);
    }

    public boolean smith(int slot, int slot2) {
        if (!playerHasItem(2347, 1)) {
            showDialogue("You need a hammer to smith with.");
            return false;
        }

        if (smithing == 0) {
            if (slot == 0 && slot2 == 1119 && daggers[smithing] == 1205 && client.playerLevel[13] < 1) {
                removeAllWindows();
                showDialogue("You need a smithing level of @blu@1@blk@ to make that item");
                return false;
            }
            if (slot == 0 && slot2 == 1120 && axes[smithing] == 1351 && client.playerLevel[13] < 1) {
                removeAllWindows();
                showDialogue("You need a smithing level of @blu@1@blk@ to make that item");
                return false;
            }

        }
        int bars = 3;
        if (slot == 0 && slot2 == 1119)
            bars = 1;
        else if (slot == 1 && slot2 == 1119)
            bars = 1;
        else if (slot == 2 && slot2 == 1119)
            bars = 2;
        else if (slot == 3 && slot2 == 1119)
            bars = 2;
        else if (slot == 0 && slot2 == 1120)
            bars = 1;
        else if (slot == 1 && slot2 == 1120)
            bars = 1;
        else if (slot == 3 && slot2 == 1121)
            bars = 5;
        else if (slot == 1 && slot2 == 1122)
            bars = 2;
        else if (slot == 0 && slot2 == 1122)
            bars = 1;
        else if (slot == 2 && slot2 == 1122)
            bars = 2;
        else if (slot == 4 && slot2 == 1120)
            bars = 2;
        else if (slot2 == 1123)
            bars = 1;
        removeAllWindows();
        if (!playerHasItemAmount(Bar[smithing], bars)) {
            sendMessage("You need " + bars + " bars to make that item");
            return false;
        }
        startAnimation(898);
        if (client.sounds == 1) {
            client.getActionAssistant().frame174(468, 000, 010);
        }
        for (int i = 0; i < bars; i++)
            deleteItem(Bar[smithing], 1);
        addSkillXP(barXP[smithing] * bars, 13);
        client.getActionAssistant().refreshSkill(13);
        if (slot == 0 && slot2 == 1119)
            addItem(daggers[smithing], 1);
        else if (slot == 1 && slot2 == 1119)
            addItem(swords[smithing], 1);
        else if (slot == 2 && slot2 == 1119)
            addItem(scimitars[smithing], 1);
        else if (slot == 3 && slot2 == 1119)
            addItem(longswords[smithing], 1);
        else if (slot == 4 && slot2 == 1119)
            addItem(twohs[smithing], 1);
        else if (slot == 0 && slot2 == 1120)
            addItem(axes[smithing], 1);
        else if (slot == 1 && slot2 == 1120)
            addItem(maces[smithing], 1);
        else if (slot == 2 && slot2 == 1120)
            addItem(warhammers[smithing], 1);
        else if (slot == 3 && slot2 == 1120)
            addItem(battleaxes[smithing], 1);
        else if (slot == 0 && slot2 == 1121)
            addItem(chainbodies[smithing], 1);
        else if (slot == 1 && slot2 == 1121)
            addItem(platelegs[smithing], 1);
        else if (slot == 2 && slot2 == 1121)
            addItem(plateskirt[smithing], 1);
        else if (slot == 3 && slot2 == 1121)
            addItem(platebodies[smithing], 1);
        else if (slot == 0 && slot2 == 1122)
            addItem(medhelms[smithing], 1);
        else if (slot == 1 && slot2 == 1122)
            addItem(fullhelms[smithing], 1);
        else if (slot == 2 && slot2 == 1122)
            addItem(sqshields[smithing], 1);
        else if (slot == 3 && slot2 == 1122)
            addItem(kiteshields[smithing], 1);
        else if (slot == 0 && slot2 == 1123)
            addItem(darttips[smithing], 10);
        else if (slot == 1 && slot2 == 1123)
            addItem(arrowtips[smithing], 15);
        else if (slot == 2 && slot2 == 1123) {
            addItem(knives[smithing], 10);
        } else if (slot == 4 && slot2 == 1120)
            addItem(claws[smithing], 1);
        return true;
    }

    public void deleteArrow() {
        if (client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_ARROWS] == 1) {
            deleteEquipment(client.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS], PlayerEquipmentConstants.PLAYER_ARROWS);
            client.tempusingArrows = false;
        }
        if (client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_ARROWS] != 0) {
            client.getOutStream().createFrameVarSizeWord(34);
            client.getOutStream().writeWord(1688);
            client.getOutStream().writeByte(PlayerEquipmentConstants.PLAYER_ARROWS);
            client.getOutStream().writeWord(client.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] + 1);
            if (client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_ARROWS] - 1 > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord(client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_ARROWS] - 1);
            } else {
                client.getOutStream().writeByte(client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_ARROWS] - 1);
            }
            client.getOutStream().endFrameVarSizeWord();
            client.flushOutStream();
            client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_ARROWS] -= 1;
        }
        client.updateRequired = true;
        client.appearanceUpdateRequired = true;
    }

    public int getEquipmentSlot(int itemID) {
        for (int i = 0; i < client.playerEquipment.length; i++) {
            if ((client.playerEquipment[i] - 1) == itemID) {
                return i;
            }
        }
        return -1;
    }

    public void deleteEquipment(int i, int j) {
        if (i < -1) {
            return;
        }
        client.playerEquipment[j] = -1;
        client.playerEquipmentN[j] = 0;
        client.getOutStream().createFrame(34);
        client.getOutStream().writeWord(6);
        client.getOutStream().writeWord(1688);
        client.getOutStream().writeByte(j);
        client.getOutStream().writeWord(0);
        client.getOutStream().writeByte(0);
        client.updateRequired = true;
        client.appearanceUpdateRequired = true;
    }

    public void deleteEquipment() {
        if (client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_WEAPON] == 1) {
            client.tempusingBow = false;
            client.tempusingOtherRangeWeapons = false;
            deleteEquipment(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON], PlayerEquipmentConstants.PLAYER_WEAPON);

        }
        if (client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_WEAPON] != 0) {
            client.getOutStream().createFrameVarSizeWord(34);
            client.getOutStream().writeWord(1688);
            client.getOutStream().writeByte(PlayerEquipmentConstants.PLAYER_WEAPON);
            client.getOutStream().writeWord(client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] + 1);
            if (client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_WEAPON] - 1 > 254) {
                client.getOutStream().writeByte(255);
                client.getOutStream().writeDWord(client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_WEAPON] - 1);
            } else {
                client.getOutStream().writeByte(client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_WEAPON] - 1);
            }
            client.getOutStream().endFrameVarSizeWord();
            client.flushOutStream();
            client.playerEquipmentN[PlayerEquipmentConstants.PLAYER_WEAPON] -= 1;
        }
        client.updateRequired = true;
        client.appearanceUpdateRequired = true;
    }


    public void walkToReverse(int i, int j) {
        client.newWalkCmdSteps = 0;
        if (++client.newWalkCmdSteps > 50) client.newWalkCmdSteps = 0;
        int k = client.getX() + i;
        k -= client.mapRegionX * 8;
        client.newWalkCmdX[0] = client.newWalkCmdY[0] = tmpNWCX[0] = tmpNWCY[0] = 0;
        int l = client.getY();
        l -= client.mapRegionY * 8;
        client.isRunning2 = false;
        client.isRunning = false;
        client.newWalkCmdX[0] += k;
        client.newWalkCmdY[0] += l;
        client.poimiY = l;
        client.poimiX = k;
    }

    protected static int tmpNWCY[] = new int[Constants.WALKING_QUEUE_SIZE];
    protected static int tmpNWCX[] = new int[Constants.WALKING_QUEUE_SIZE];

    public void walkTo(int i, int j) {

        client.newWalkCmdSteps = 0;
        if (++client.newWalkCmdSteps > 50)
            client.newWalkCmdSteps = 0;
        int k = client.getX() + i;
        k -= client.mapRegionX * 8;
        client.newWalkCmdX[0] = client.newWalkCmdY[0] = 0;
        int l = client.getY() + j;
        l -= client.mapRegionY * 8;

        for (int n = 0; n < client.newWalkCmdSteps; n++) {
            client.newWalkCmdX[0] += k;
            client.newWalkCmdY[0] += l;
        }
    }

    public void walkTo_old(int X, int Y) {
        int firstStepX = client.getInStream().readSignedWordBigEndianA();
        int tmpFSX = client.absX;
        client.absX -= client.mapRegionX * 8;
        for (int i = 1; i < client.newWalkCmdSteps; i++) {
            client.newWalkCmdX[i] = client.getInStream().readSignedByte();
            client.newWalkCmdY[i] = client.getInStream().readSignedByte();
            client.tmpNWCX[i] = client.newWalkCmdX[i];
            client.tmpNWCY[i] = client.newWalkCmdY[i];
        }
        client.newWalkCmdX[0] = client.newWalkCmdY[0] = client.tmpNWCX[0] = client.tmpNWCY[0] = 0;
        int firstStepY = client.getInStream().readSignedWordBigEndian();
        int tmpFSY = client.absY;
        client.absY -= client.mapRegionY * 8;
        client.newWalkCmdIsRunning = client.getInStream().readSignedByteC() == 1;
        for (int i = 0; i < client.newWalkCmdSteps; i++) {
            client.newWalkCmdX[i] += X;
            client.newWalkCmdY[i] += Y;
        }
        //println_debug("Walking to X:" + X + " Y:" + Y);
    }

    public int getMove(int place1, int place2) {
        if ((place1 - place2) == 0) {
            return 0;
        } else if ((place1 - place2) < 0) {
            return 1;
        } else if ((place1 - place2) > 0) {
            return -1;
        }
        return 0;
    }

    /**
     * two handed weapon check
     */
    public boolean is2handed(int itemId) {

        switch (itemId) {
            case 16907:
            case 1307:
            case 1309:
            case 1311:
            case 1315:
            case 1317:
            case 841:
            case 4734:
            case 4212:
            case 4215:
            case 4216:
            case 4217:
            case 4218:
            case 4219:
            case 4747:
            case 1314:
            case 4213:
            case 6609:
            case 1319:
            case 4718:
            case 4726:
            case 11694:
            case 11696:
            case 11700:
            case 11698:
            case 843:
            case 845:
            case 849:
            case 847:
            case 853:
            case 851:
            case 859:
            case 861:
            case 1409:
            case 4710:
            case 857:
            case 7158:
            case 4755:
            case 4220:
            case 4221:
            case 4222:
            case 4223:
            case 6724: // seercull
            case 13905:
            case 11716:
            case 10887:
            case 15241:
            case 16425:
            case 16423:
            case 18369:
            case 15701:
            case 15702:
            case 15703:
            case 15704:
            case 3190:
            case 3095:
            case 3096:
            case 3097:
            case 3098:
            case 3099:
            case 3100:
            case 20171:
            case 3101:
            case 3192:
            case 3194:
            case 3196:
            case 3198:
            case 3200:
            case 3202:
            case 18353:
            case 16909:
                return true;
            case 14484:
                return true;
            case 13645:
                return true;
            case 4827:
                return true;
            case 6528:
                return true;
            case 4153:
                return true;
            case 11730:
                return true;
            case 3204:
                return true;
            case 1313:
                return true;

        }
        return false;
    }

    public void sendSkillInterface(int id[]) {
        client.getOutStream().createFrameVarSizeWord(53);
        client.getOutStream().writeWord(8847); // 8847
        client.getOutStream().writeWord(id.length);
        for (int i = 0; i < id.length; i++) {
            client.getOutStream().writeByte(1);
            if (id[i] > 0) {
                client.getOutStream().writeWordBigEndianA(id[i] + 1);
            } else {
                client.getOutStream().writeWordBigEndianA(0);
            }
        }
        client.getOutStream().endFrameVarSizeWord();
        client.flushOutStream();
    }

    private int item[] = new int[40];
    public int what = 0;
    public long lastAct = 0;
    public int skillInterface = 0;
    private String des[] = new String[40];
    private int lvl[] = new int[40];
    private String opt[] = new String[40];

    public void skillInterface(String title, String description[], int level[], String option[], int id[], int member, int selected, int skillid) {
        if (System.currentTimeMillis() - lastAct > 2000) {
            lastAct = System.currentTimeMillis();
            sendFrame126("@dre@" + title, 8716);
            skillInterface = skillid;
            int d = 0;
            for (int i = 8720; i < 8760; i++) {
                if (lvl[d] != 0) {
                    sendFrame126("" + level[d], i);
                } else {
                    sendFrame126("", i);
                }
                d++;
            }
            int d2 = 0;
            for (int i2 = 8760; i2 < 8800; i2++) {
                sendFrame126(description[d2], i2);
                d2++;
            }
            if (member == 1) {
                sendFrame126("@blu@Members only", 8849);// members only skill
            } else {
                sendFrame126("", 8849);// members only skill
            }

            sendFrame126(option[0], 8846); // first option
            sendFrame126(option[1], 8823);// bronze
            sendFrame126(option[2], 8824);// iron
            sendFrame126(option[3], 8827);//steel
            sendFrame126(option[4], 8837);//mithril
            sendFrame126(option[5], 8840);
            sendFrame126(option[6], 8843);
            sendFrame126(option[7], 8859);
            sendFrame126(option[8], 8862);
            sendFrame126(option[9], 8865);
            sendFrame126(option[10], 15303);
            sendFrame126(option[11], 15306);
            sendFrame126(option[12], 15309);
            what = selected;
            if (selected == 1) {
                sendFrame126("@red@" + option[0], 8846);// first option
            } else if (selected == 2) {
                sendFrame126("@red@" + option[1], 8823);// option 2
            } else if (selected == 3) {
                sendFrame126("@red@" + option[2], 8824);// option 3
            } else if (selected == 4) {
                sendFrame126("@red@" + option[3], 8827);// option 4
            } else if (selected == 5) {
                sendFrame126("@red@" + option[4], 8837);// option 5
            } else if (selected == 6) {
                sendFrame126("@red@" + option[5], 8840);// option 6
            } else if (selected == 7) {
                sendFrame126("@red@" + option[6], 8843);// option 7
            } else if (selected == 8) {
                sendFrame126("@red@" + option[7], 8859);// option 8
            } else if (selected == 9) {
                sendFrame126("@red@" + option[8], 8862);// option 9
            } else if (selected == 10) {
                sendFrame126("@red@" + option[9], 8865);// option 10
            } else if (selected == 11) {
                sendFrame126("@red@" + option[10], 15303);// option 11
            } else if (selected == 12) {
                sendFrame126("@red@" + option[11], 15306);// option 12
            } else if (selected == 13) {
                sendFrame126("@red@" + option[12], 15309);// option 13
            }
            sendSkillInterface(id);
            showInterface(8714);
        }
    }

    public void defInterface() {
        if (what != 1) {
            for (int i = 0; i < 40; i++) {
                item[i] = 0;
                lvl[i] = 0;
                des[i] = "";
            }
            for (int i = 0; i < 13; i++) {
                opt[i] = "";
            }
            opt[0] = "Bronze";
            opt[1] = "Iron";
            opt[2] = "Steel";
            opt[3] = "Black";
            opt[4] = "Mithril";
            opt[5] = "Adamant";
            opt[6] = "Rune";
            opt[7] = "Dragon";
            opt[8] = "Barrows";
            item[0] = 1173; // item
            des[0] = "Bronze sq shield"; // description
            lvl[0] = 1; // level
            item[1] = 1189;
            des[1] = "Bronze kite shield";
            lvl[1] = 1;
            item[2] = 1139;
            des[2] = "Bronze medium helmet";
            lvl[2] = 1;
            item[3] = 1155;
            des[3] = "Bronze full helm";
            lvl[3] = 1;
            item[4] = 1103;
            des[4] = "Bronze chain body";
            lvl[4] = 1;
            item[5] = 1117;
            des[5] = "Bronze platebody";
            lvl[5] = 1;
            item[6] = 1087;
            des[6] = "Bronze plateskirt";
            lvl[6] = 1;
            item[7] = 1075;
            des[7] = "Bronze platelegs";
            lvl[7] = 1;
            item[8] = 4119;
            des[8] = "@blu@Members: @bla@Bronze boots";
            lvl[8] = 1;
            skillInterface("Defence - Bronze armour", des, lvl, opt, item, 0/* membrs only? */, 1/* Which tab selected? 1 is first */, 3/*different for skills*/);
        }
    }

    public void defInterfaceIron() {
        if (what != 2) {
            for (int i = 0; i < 40; i++) {
                item[i] = 0;
                lvl[i] = 0;
                des[i] = "";
            }
            for (int i = 0; i < 13; i++) {
                opt[i] = "";
            }
            opt[0] = "Bronze";
            opt[1] = "Iron";
            opt[2] = "Steel";
            opt[3] = "Black";
            opt[4] = "Mithril";
            opt[5] = "Adamant";
            opt[6] = "Rune";
            opt[7] = "Dragon";
            opt[8] = "Barrows";
            item[0] = 1175; // item
            des[0] = "Iron sq shield"; // description
            lvl[0] = 1; // level
            item[1] = 1191;
            des[1] = "Iron kite shield";
            lvl[1] = 1;
            item[2] = 1137;
            des[2] = "Iron medium helmet";
            lvl[2] = 1;
            item[3] = 1153;
            des[3] = "Iron full helm";
            lvl[3] = 1;
            item[4] = 1101;
            des[4] = "Iron chain body";
            lvl[4] = 1;
            item[5] = 1115;
            des[5] = "Iron platebody";
            lvl[5] = 1;
            item[6] = 1081;
            des[6] = "Iron plateskirt";
            lvl[6] = 1;
            item[7] = 1067;
            des[7] = "Iron platelegs";
            lvl[7] = 1;
            item[8] = 4121;
            des[8] = "@blu@Members: @bla@Iron boots";
            lvl[8] = 1;
            skillInterface("Defence - Iron armour", des, lvl, opt, item, 0/* membrs only? */, 2/* Which tab selected? 1 is first */, 3/*different for skills*/);
        }
    }

    public void defInterfaceSteel() {
        if (what != 3) {
            for (int i = 0; i < 40; i++) {
                item[i] = 0;
                lvl[i] = 0;
                des[i] = "";
            }
            for (int i = 0; i < 13; i++) {
                opt[i] = "";
            }
            opt[0] = "Bronze";
            opt[1] = "Iron";
            opt[2] = "Steel";
            opt[3] = "Black";
            opt[4] = "Mithril";
            opt[5] = "Adamant";
            opt[6] = "Rune";
            opt[7] = "Dragon";
            opt[8] = "Barrows";
            item[0] = 1177; // item
            des[0] = "Steel sq shield"; // description
            lvl[0] = 5; // level
            item[1] = 1193;
            des[1] = "Steel kite shield";
            lvl[1] = 5;
            item[2] = 1141;
            des[2] = "Steel medium helmet";
            lvl[2] = 5;
            item[3] = 1157;
            des[3] = "Steel full helm";
            lvl[3] = 5;
            item[4] = 1105;
            des[4] = "Steel chain body";
            lvl[4] = 5;
            item[5] = 1119;
            des[5] = "Steel platebody";
            lvl[5] = 5;
            item[6] = 1083;
            des[6] = "Steel plateskirt";
            lvl[6] = 5;
            item[7] = 1069;
            des[7] = "Steel platelegs";
            lvl[7] = 5;
            item[8] = 4123;
            des[8] = "@blu@Members: @bla@Steel boots";
            lvl[8] = 5;
            skillInterface("Defence - Steel armour", des, lvl, opt, item, 0/* membrs only? */, 3/* Which tab selected? 1 is first */, 3/*different for skills*/);
        }
    }

    public void defInterfaceBlack() {
        if (what != 4) {
            for (int i = 0; i < 40; i++) {
                item[i] = 0;
                lvl[i] = 0;
                des[i] = "";
            }
            for (int i = 0; i < 13; i++) {
                opt[i] = "";
            }
            opt[0] = "Bronze";
            opt[1] = "Iron";
            opt[2] = "Steel";
            opt[3] = "Black";
            opt[4] = "Mithril";
            opt[5] = "Adamant";
            opt[6] = "Rune";
            opt[7] = "Dragon";
            opt[8] = "Barrows";
            item[0] = 1179; // item
            des[0] = "Black sq shield"; // description
            lvl[0] = 10; // level
            item[1] = 1195;
            des[1] = "Black kite shield";
            lvl[1] = 10;
            item[2] = 1151;
            des[2] = "Black medium helmet";
            lvl[2] = 10;
            item[3] = 1165;
            des[3] = "Black full helm";
            lvl[3] = 10;
            item[4] = 1107;
            des[4] = "Black chain body";
            lvl[4] = 10;
            item[5] = 1125;
            des[5] = "Black platebody";
            lvl[5] = 10;
            item[6] = 1089;
            des[6] = "Black plateskirt";
            lvl[6] = 10;
            item[7] = 1077;
            des[7] = "Black platelegs";
            lvl[7] = 10;
            item[8] = 4125;
            des[8] = "@blu@Members: @bla@Black boots";
            lvl[8] = 10;
            skillInterface("Defence - Black armour", des, lvl, opt, item, 0/* membrs only? */, 4/* Which tab selected? 1 is first */, 3/*different for skills*/);
        }
    }

    public void defInterfaceMith() {
        if (what != 5) {
            for (int i = 0; i < 40; i++) {
                item[i] = 0;
                lvl[i] = 0;
                des[i] = "";
            }
            for (int i = 0; i < 13; i++) {
                opt[i] = "";
            }
            opt[0] = "Bronze";
            opt[1] = "Iron";
            opt[2] = "Steel";
            opt[3] = "Black";
            opt[4] = "Mithril";
            opt[5] = "Adamant";
            opt[6] = "Rune";
            opt[7] = "Dragon";
            opt[8] = "Barrows";
            item[0] = 1181; // item
            des[0] = "Mithril sq shield"; // description
            lvl[0] = 20; // level
            item[1] = 1197;
            des[1] = "Mithril kite shield";
            lvl[1] = 20;
            item[2] = 1143;
            des[2] = "Mithril medium helmet";
            lvl[2] = 20;
            item[3] = 1159;
            des[3] = "Mithril full helm";
            lvl[3] = 20;
            item[4] = 1109;
            des[4] = "Mithril chain body";
            lvl[4] = 20;
            item[5] = 1121;
            des[5] = "Mithril platebody";
            lvl[5] = 20;
            item[6] = 1085;
            des[6] = "Mithril plateskirt";
            lvl[6] = 20;
            item[7] = 1071;
            des[7] = "Mithril platelegs";
            lvl[7] = 20;
            item[8] = 4127;
            des[8] = "@blu@Members: @bla@Mithril boots";
            lvl[8] = 20;
            skillInterface("Defence - Mithril armour", des, lvl, opt, item, 0/* membrs only? */, 5/* Which tab selected? 1 is first */, 3/*different for skills*/);
        }
    }

    public void defInterfaceAdam() {
        if (what != 6) {
            for (int i = 0; i < 40; i++) {
                item[i] = 0;
                lvl[i] = 0;
                des[i] = "";
            }
            for (int i = 0; i < 13; i++) {
                opt[i] = "";
            }
            opt[0] = "Bronze";
            opt[1] = "Iron";
            opt[2] = "Steel";
            opt[3] = "Black";
            opt[4] = "Mithril";
            opt[5] = "Adamant";
            opt[6] = "Rune";
            opt[7] = "Dragon";
            opt[8] = "Barrows";
            item[0] = 1183; // item
            des[0] = "Adamant sq shield"; // description
            lvl[0] = 30; // level
            item[1] = 1199;
            des[1] = "Adamant kite shield";
            lvl[1] = 30;
            item[2] = 1145;
            des[2] = "Adamant medium helmet";
            lvl[2] = 30;
            item[3] = 1161;
            des[3] = "Adamant full helm";
            lvl[3] = 30;
            item[4] = 1111;
            des[4] = "Adamant chain body";
            lvl[4] = 30;
            item[5] = 1123;
            des[5] = "Adamant platebody";
            lvl[5] = 30;
            item[6] = 1091;
            des[6] = "Adamant plateskirt";
            lvl[6] = 30;
            item[7] = 1073;
            des[7] = "Adamant platelegs";
            lvl[7] = 30;
            item[8] = 4129;
            des[8] = "@blu@Members: @bla@Adamant boots";
            lvl[8] = 30;
            skillInterface("Defence - Adamant armour", des, lvl, opt, item, 0/* membrs only? */, 6/* Which tab selected? 1 is first */, 3/*different for skills*/);
        }
    }

    public void defInterfaceRune() {
        if (what != 7) {
            for (int i = 0; i < 40; i++) {
                item[i] = 0;
                lvl[i] = 0;
                des[i] = "";
            }
            for (int i = 0; i < 13; i++) {
                opt[i] = "";
            }
            opt[0] = "Bronze";
            opt[1] = "Iron";
            opt[2] = "Steel";
            opt[3] = "Black";
            opt[4] = "Mithril";
            opt[5] = "Adamant";
            opt[6] = "Rune";
            opt[7] = "Dragon";
            opt[8] = "Barrows";
            item[0] = 1185; // item
            des[0] = "Rune sq shield"; // description
            lvl[0] = 40; // level
            item[1] = 1201;
            des[1] = "Rune kite shield";
            lvl[1] = 40;
            item[2] = 1147;
            des[2] = "Rune medium helmet";
            lvl[2] = 40;
            item[3] = 1163;
            des[3] = "Rune full helm";
            lvl[3] = 40;
            item[4] = 1113;
            des[4] = "Rune chain body";
            lvl[4] = 40;
            item[5] = 1127;
            des[5] = "Rune platebody";
            lvl[5] = 40;
            item[6] = 1093;
            des[6] = "Rune plateskirt";
            lvl[6] = 40;
            item[7] = 1079;
            des[7] = "Rune platelegs";
            lvl[7] = 40;
            item[8] = 4131;
            des[8] = "@blu@Members: @bla@Rune boots";
            lvl[8] = 40;
            skillInterface("Defence - Rune armour", des, lvl, opt, item, 0/* membrs only? */, 7/* Which tab selected? 1 is first */, 3/*different for skills*/);
        }
    }

    public void defInterfaceDrag() {
        if (what != 8) {
            for (int i = 0; i < 40; i++) {
                item[i] = 0;
                lvl[i] = 0;
                des[i] = "";
            }
            for (int i = 0; i < 13; i++) {
                opt[i] = "";
            }
            opt[0] = "Bronze";
            opt[1] = "Iron";
            opt[2] = "Steel";
            opt[3] = "Black";
            opt[4] = "Mithril";
            opt[5] = "Adamant";
            opt[6] = "Rune";
            opt[7] = "Dragon";
            opt[8] = "Barrows";
            item[0] = 1187; // item
            des[0] = "Dragon sq shield"; // description
            lvl[0] = 60; // level
            item[1] = 1149;
            des[1] = "Dragon med helmet";
            lvl[1] = 60;
            item[2] = 3140;
            des[2] = "Dragon chain body";
            lvl[2] = 60;
            item[3] = 4585;
            des[3] = "Dragon plateskirt";
            lvl[3] = 60;
            item[4] = 4087;
            des[4] = "Dragon platelegs";
            lvl[4] = 60;
            skillInterface("Defence - Dragon armour", des, lvl, opt, item, 1/* membrs only? */, 8/* Which tab selected? 1 is first */, 3/*different for skills*/);
        }
    }

    public void defInterfaceBarr() {
        if (what != 9) {
            for (int i = 0; i < 40; i++) {
                item[i] = 0;
                lvl[i] = 0;
                des[i] = "";
            }
            for (int i = 0; i < 13; i++) {
                opt[i] = "";
            }
            opt[0] = "Bronze";
            opt[1] = "Iron";
            opt[2] = "Steel";
            opt[3] = "Black";
            opt[4] = "Mithril";
            opt[5] = "Adamant";
            opt[6] = "Rune";
            opt[7] = "Dragon";
            opt[8] = "Barrows";
            item[0] = 4708; // item
            des[0] = "Ahrim's hood (with 70 Magic)"; // description
            lvl[0] = 70; // level
            item[1] = 4712;
            des[1] = "Ahrim's robetop (with 70 Magic)";
            lvl[1] = 70;
            item[2] = 4714;
            des[2] = "Ahrim's robeskirt (with 70 Magic)";
            lvl[2] = 70;
            item[3] = 4716;
            des[3] = "Dharok's helm";
            lvl[3] = 70;
            item[4] = 4720;
            des[4] = "Dharok's platebody";
            lvl[4] = 70;
            item[5] = 4722;
            des[5] = "Dharok's platelegs";
            lvl[5] = 70;
            item[6] = 4724;
            des[6] = "Guthan's helm";
            lvl[6] = 70;
            item[7] = 4728;
            des[7] = "Guthan's platebody";
            lvl[7] = 70;
            item[8] = 4730;
            des[8] = "Guthan's chainskirt";
            lvl[8] = 70;
            item[9] = 4732;
            des[9] = "Karil's coif (with 70 Ranged)";
            lvl[9] = 70;
            item[10] = 4736;
            des[10] = "Karil's leather top (with 70 Ranged)";
            lvl[10] = 70;
            item[11] = 4738;
            des[11] = "Karil's leather skirt (with 70 Ranged)";
            lvl[11] = 70;
            item[12] = 4745;
            des[12] = "Torag's helm";
            lvl[12] = 70;
            item[13] = 4749;
            des[13] = "Torag's platebody";
            lvl[13] = 70;
            item[14] = 4751;
            des[14] = "Torag's platelegs";
            lvl[14] = 70;
            item[15] = 4753;
            des[15] = "Verac's helm";
            lvl[15] = 70;
            item[16] = 4757;
            des[16] = "Verac's brassard";
            lvl[16] = 70;
            item[4] = 4759;
            des[4] = "Verac's plateskirt";
            lvl[4] = 70;
            skillInterface("Defence - Dragon armour", des, lvl, opt, item, 1/* membrs only? */, 9/* Which tab selected? 1 is first */, 3/*different for skills*/);
        }
    }

    public void atkInterface(String name, int lvl, int option, int id[]) {
        if (System.currentTimeMillis() - lastAct > 2000) {
            lastAct = System.currentTimeMillis();
            sendFrame126("@dre@Attack", 8716);
            skillInterface = 1;
            for (int i = 8720; i < 8799; i++) {
                sendFrame126("", i);
            }
            if (name == "Dragon") {
                sendFrame126(lvl + "", 8720);
                sendFrame126("@blu@Members: @bla@" + name + " dagger", 8760);
                sendFrame126(lvl + "", 8721);
                sendFrame126("@blu@Members: @bla@" + name + " hatchet", 8761);
                sendFrame126(lvl + "", 8722);
                sendFrame126("@blu@Members: @bla@" + name + " mace", 8762);
                sendFrame126(lvl + "", 8723);
                sendFrame126("@blu@Members: @bla@" + name + " longsword", 8763);
                sendFrame126(lvl + "", 8724);
                sendFrame126("@blu@Members: @bla@" + name + " scimitar", 8764);
                sendFrame126(lvl + "", 8725);
                sendFrame126("@blu@Members: @bla@" + name + " spear", 8765);
                sendFrame126(lvl + "", 8726);
                sendFrame126("@blu@Members: @bla@" + name + " battleaxe", 8766);
                sendFrame126(lvl + "", 8727);
                sendFrame126("@blu@Members: @bla@" + name + " two-handed sword", 8767);
                sendFrame126(lvl + "", 8728);
                sendFrame126("@blu@Members: @bla@" + name + " halberd (with 10 Strength)", 8768);
            } else {
                sendFrame126(lvl + "", 8720);
                sendFrame126(name + " dagger", 8760);
                sendFrame126(lvl + "", 8721);
                sendFrame126(name + " hatchet", 8761);
                sendFrame126(lvl + "", 8722);
                sendFrame126(name + " mace", 8762);
                sendFrame126(lvl + "", 8723);
                sendFrame126("@blu@Members: @bla@" + name + " claws", 8763);
                sendFrame126(lvl + "", 8724);
                sendFrame126(name + " sword", 8764);
                sendFrame126(lvl + "", 8725);
                sendFrame126(name + " longsword", 8765);
                sendFrame126(lvl + "", 8726);
                sendFrame126(name + " scimitar", 8766);
                sendFrame126(lvl + "", 8727);
                sendFrame126(name + " spear", 8767);
                sendFrame126(lvl + "", 8728);
                sendFrame126(name + " warhammer", 8768);
                sendFrame126(lvl + "", 8729);
                sendFrame126(name + " battleaxe", 8769);
                sendFrame126(lvl + "", 8730);
                sendFrame126(name + " two-handed sword", 8770);
                sendFrame126(lvl + "", 8731);
                sendFrame126("@blu@Members: @bla@" + name + " halberd (with 10 Strength)", 8771);
            }
            sendFrame126("", 8849);// members only skill
            sendFrame126("Bronze", 8846); // first option
            sendFrame126("Iron", 8823);// bronze
            sendFrame126("Steel", 8824);// iron
            sendFrame126("Black", 8827);//steel
            sendFrame126("Mithril", 8837);//mithril
            sendFrame126("Adamant", 8840);
            sendFrame126("Rune", 8843);
            sendFrame126("Dragon", 8859);
            sendFrame126("", 8862);
            what = option;
            if (option == 1) {
                sendFrame126("@red@Bronze", 8846); // first option
            } else if (option == 2) {
                sendFrame126("@red@Iron", 8823);// bronze
            } else if (option == 3) {
                sendFrame126("@red@Steel", 8824);// iron
            } else if (option == 4) {
                sendFrame126("@red@Black", 8827);//steel
            } else if (option == 5) {
                sendFrame126("@red@Mithril", 8837);//mithril
            } else if (option == 6) {
                sendFrame126("@red@Adamant", 8840);
            } else if (option == 7) {
                sendFrame126("@red@Rune", 8843);
            } else if (option == 8) {
                sendFrame126("@red@Dragon", 8859);
            }
            sendFrame126("", 8865);
            sendFrame126("", 15303);
            sendFrame126("", 15306);
            sendFrame126("", 15309);

            sendSkillInterface(id);
            showInterface(8714);
        }
    }

    public void atkInterface() {// bronze
        if (what != 1) {
            for (int i = 0; i < 31; i++) {
                item[i] = 0;
            }
            item[0] = 1205; // dagger
            item[1] = 1351; // axe
            item[2] = 1422; // mace
            item[3] = 3095; // claws
            item[4] = 1277; // sword
            item[5] = 1291; // longsword
            item[6] = 1321; // scimitar
            item[7] = 1237; // spear
            item[8] = 1337; // warhammer
            item[9] = 1375; // b-axe
            item[10] = 1307; // 2h
            item[11] = 3190; // halberd
            atkInterface("Bronze", 1, 1, item);
        }
    }

    public void atkInterfaceIron() {
        if (what != 2) {
            for (int i = 0; i < 31; i++) {
                item[i] = 0;
            }
            item[0] = 1203; // dagger
            item[1] = 1349; // axe
            item[2] = 1420; // mace
            item[3] = 3096; // claws
            item[4] = 1279; // sword
            item[5] = 1293; // longsword
            item[6] = 1323; // scimitar
            item[7] = 1239; // spear
            item[8] = 1335; // warhammer
            item[9] = 1363; // b-axe
            item[10] = 1309; // 2h
            item[11] = 3192; // halberd
            atkInterface("Iron", 1, 2, item);
        }
    }

    public void atkInterfaceSteel() {
        if (what != 3) {
            for (int i = 0; i < 31; i++) {
                item[i] = 0;
            }
            item[0] = 1207; // dagger
            item[1] = 1353; // axe
            item[2] = 1424; // mace
            item[3] = 3097; // claws
            item[4] = 1281; // sword
            item[5] = 1295; // longsword
            item[6] = 1325; // scimitar
            item[7] = 1241; // spear
            item[8] = 1339; // warhammer
            item[9] = 1365; // b-axe
            item[10] = 1311; // 2h
            item[11] = 3194; // halberd
            atkInterface("Steel", 5, 3, item);
        }
    }

    public void atkInterfaceBlack() {
        if (what != 4) {
            for (int i = 0; i < 31; i++) {
                item[i] = 0;
            }
            item[0] = 1217; // dagger
            item[1] = 1361; // axe
            item[2] = 1426; // mace
            item[3] = 3098; // claws
            item[4] = 1283; // sword
            item[5] = 1297; // longsword
            item[6] = 1327; // scimitar
            item[7] = 4580; // spear
            item[8] = 1341; // warhammer
            item[9] = 1367; // b-axe
            item[10] = 1313; // 2h
            item[11] = 3196; // halberd
            atkInterface("Black", 10, 4, item);
        }
    }

    public void atkInterfaceMith() {
        if (what != 5) {
            for (int i = 0; i < 31; i++) {
                item[i] = 0;
            }
            item[0] = 1209; // dagger
            item[1] = 1355; // axe
            item[2] = 1428; // mace
            item[3] = 3099; // claws
            item[4] = 1285; // sword
            item[5] = 1299; // longsword
            item[6] = 1329; // scimitar
            item[7] = 1243; // spear
            item[8] = 1343; // warhammer
            item[9] = 1369; // b-axe
            item[10] = 1315; // 2h
            item[11] = 3198; // halberd
            atkInterface("Mithril", 20, 5, item);
        }
    }

    public void atkInterfaceAdam() {
        if (what != 6) {
            for (int i = 0; i < 31; i++) {
                item[i] = 0;
            }
            item[0] = 1211; // dagger
            item[1] = 1357; // axe
            item[2] = 1430; // mace
            item[3] = 3100; // claws
            item[4] = 1287; // sword
            item[5] = 1301; // longsword
            item[6] = 1331; // scimitar
            item[7] = 1245; // spear
            item[8] = 1345; // warhammer
            item[9] = 1371; // b-axe
            item[10] = 1317; // 2h
            item[11] = 3200; // halberd
            atkInterface("Adamant", 30, 6, item);
        }
    }

    public void atkInterfaceRune() {
        if (what != 7) {
            for (int i = 0; i < 31; i++) {
                item[i] = 0;
            }
            item[0] = 1213; // dagger
            item[1] = 1359; // axe
            item[2] = 1432; // mace
            item[3] = 3101; // claws
            item[4] = 1289; // sword
            item[5] = 1303; // longsword
            item[6] = 1333; // scimitar
            item[7] = 1247; // spear
            item[8] = 1347; // warhammer
            item[9] = 1373; // b-axe
            item[10] = 1319; // 2h
            item[11] = 3202; // halberd
            atkInterface("Rune", 40, 7, item);
        }
    }

    public void atkInterfaceDragon() {
        if (what != 8) {
            for (int i = 0; i < 31; i++) {
                item[i] = 0;
            }
            item[0] = 1215; // dagger
            item[1] = 6739; // axe
            item[2] = 1434; // mace
            item[3] = 1305; // longsword
            item[4] = 4587; // scimitar
            item[5] = 1249; // spear
            item[6] = 1377; // b-axe
            item[7] = 7158; // 2h
            item[8] = 3204; // halberd
            atkInterface("Dragon", 60, 8, item);
        }
    }

    public void mazeRandom() {
        client.teleportToX = 2371;
        client.teleportToY = 3128;

    }

    public void triggerRandom() {
        //if (!client.randomed) {
        client.random_skill = Misc.random(client.statName.length) - 1;
        if (client.random_skill < 0)
            client.random_skill = 0;
        client.getActionAssistant().sendFrame126("Click the @or1@" + client.statName[client.random_skill] + " @yel@button for 100k Exp", 2810);
        client.getActionAssistant().sendFrame126("", 2811);
        client.getActionAssistant().sendFrame126("", 2831);
        client.randomed = true;
        client.getActionAssistant().showInterface(2808);
        //}
    }

    public void craftingInterface() {
        sendFrame126("@dre@Crafting Interface", 8716);
        for (int i = 8720; i < 8799; i++) {
            sendFrame126("", i);
        }
        sendFrame126("1", 8720);
        sendFrame126("Leather Gloves", 8760);
        sendFrame126("5", 8721);
        sendFrame126("Gold Ring", 8761);
        sendFrame126("6", 8722);
        sendFrame126("Gold necklace", 8762);
        sendFrame126("7", 8723);
        sendFrame126("Leather Boots", 8763);
        sendFrame126("8", 8724);
        sendFrame126("Gold Amulet", 8764);
        sendFrame126("9", 8725);
        sendFrame126("Leather Cowl", 8765);
        sendFrame126("11", 8726);
        sendFrame126("Leather Vambraces", 8766);
        sendFrame126("14", 8727);
        sendFrame126("Leather Body", 8767);
        sendFrame126("16", 8728);
        sendFrame126("Unstrug Holy Symbol", 8768);
        sendFrame126("17", 8729);
        sendFrame126("Unstrug Unholy Symbol", 8769);
        sendFrame126("18", 8730);
        sendFrame126("Leather Chaps", 8770);
        sendFrame126("20", 8731);
        sendFrame126("Cut Sapphire", 8771);
        sendFrame126("20", 8732);
        sendFrame126("Sapphire Ring", 8772);
        sendFrame126("22", 8733);
        sendFrame126("Sapphire Necklace", 8773);
        sendFrame126("24", 8734);
        sendFrame126("Sapphire Amulet", 8774);
        sendFrame126("27", 8735);
        sendFrame126("Cut Emeralds", 8775);
        sendFrame126("27", 8736);
        sendFrame126("Emerald Ring", 8776);
        sendFrame126("28", 8737);
        sendFrame126("Hard Leather Body", 8777);
        sendFrame126("29", 8738);
        sendFrame126("Emerald Necklace", 8778);
        sendFrame126("31", 8739);
        sendFrame126("Emerald Amulet", 8779);
        sendFrame126("34", 8740);
        sendFrame126("Cut Rubys", 8780);
        sendFrame126("34", 8741);
        sendFrame126("Ruby Ring", 8781);
        sendFrame126("38", 8742);
        sendFrame126("Leather Coif", 8782);
        sendFrame126("40", 8743);
        sendFrame126("Ruby Necklace", 8783);
        sendFrame126("41", 8744);
        sendFrame126("Studded Body", 8784);
        sendFrame126("43", 8745);
        sendFrame126("Cut Diamonds", 8785);
        sendFrame126("", 8746);
        sendFrame126("", 8786);
        sendFrame126("", 8747);
        sendFrame126("", 8787);
        sendFrame126("", 8748);
        sendFrame126("", 8788);
        sendFrame126("", 8749);
        sendFrame126("", 8789);
        sendFrame126("", 8750);
        sendFrame126("", 8790);
        sendFrame126("", 8751);
        sendFrame126("", 8791);
        sendFrame126("", 8752);
        sendFrame126("", 8792);
        sendFrame126("", 8753);
        sendFrame126("", 8793);
        sendFrame126("", 8754);
        sendFrame126("", 8794);
        sendFrame126("", 8755);
        sendFrame126("", 8795);
        sendFrame126("", 8756);
        sendFrame126("", 8796);
        sendFrame126("", 8757);
        sendFrame126("", 8797);
        sendFrame126("", 8758);
        sendFrame126("", 8798);
        sendFrame126("", 8759);
        sendFrame126("", 8799);


        sendFrame126("", 8849);// members only skill
        sendFrame126("", 8827);//steel
        sendFrame126("", 8837);//mithril
        sendFrame126("", 8840);
        sendFrame126("", 8843);
        sendFrame126("", 8846); //Skills
        sendFrame126("", 8823);
        sendFrame126("", 8824);
        sendFrame126("", 8859);
        sendFrame126("", 8862);
        sendFrame126("", 8865);
        sendFrame126("", 15303);
        sendFrame126("", 15306);
        sendFrame126("", 15309);

        sendFrame34(8847, 1059, 0, 1);
        sendFrame34(8847, 1635, 1, 1);
        sendFrame34(8847, 1654, 2, 1);
        sendFrame34(8847, 1061, 3, 1);
        sendFrame34(8847, 1673, 4, 1);
        sendFrame34(8847, 1167, 5, 1);
        sendFrame34(8847, 1063, 6, 1);
        sendFrame34(8847, 1129, 7, 1);
        sendFrame34(8847, 1718, 8, 1);
        sendFrame34(8847, 1724, 9, 1);
        sendFrame34(8847, 1095, 10, 1);
        sendFrame34(8847, 1607, 11, 1);
        sendFrame34(8847, 1637, 12, 1);
        sendFrame34(8847, 1656, 13, 1);
        sendFrame34(8847, 1675, 14, 1);
        sendFrame34(8847, 1605, 15, 1);
        sendFrame34(8847, 1639, 16, 1);
        sendFrame34(8847, 1131, 17, 1);
        sendFrame34(8847, 1658, 18, 1);
        sendFrame34(8847, 1677, 19, 1);
        sendFrame34(8847, 1603, 20, 1);
        sendFrame34(8847, 1641, 21, 1);
        sendFrame34(8847, 1169, 22, 1);
        sendFrame34(8847, 1660, 23, 1);
        sendFrame34(8847, 1133, 24, 1);
        sendFrame34(8847, 1601, 25, 1);
        showInterface(8714);
    }

    /*public void fishInterface() {
			sendFrame126("@dre@Fishing Interface", 8716);
			for(int i = 8720;i<8799;i++) {
				sendFrame126("",i);
			}
			for(int i = 1;i<26;i++) {
				sendFrame34(8847,-1,i,1);
			}		
			sendFrame126("1",8720);
			sendFrame126("",8760);
			sendFrame34(8847,,0,1);
			
			sendFrame126("",8721);			// starter interface
			sendFrame126("", 8761);
			sendFrame34(8847,,1,1);
			
			sendFrame126("",8722);
			sendFrame126("",8762);
			sendFrame34(8847,,2,1);
			
			sendFrame126("",8723);
			sendFrame126("", 8763);
			sendFrame34(8847,,3,1);
			
			sendFrame126("",8724);
			sendFrame126("", 8764);
			sendFrame34(8847,,4,1);
			
			sendFrame126("",8725);
			sendFrame126("",8765);
			sendFrame34(8847,,5,1);
			
			sendFrame126("",8726);
			sendFrame126("", 8766);
			sendFrame34(8847,,6,1);
			
			sendFrame126("",8727);
			sendFrame126("", 8767);
			sendFrame34(8847,,7,1);
			
			sendFrame126("", 8728);
			sendFrame126("", 8768);
			sendFrame34(8847,,8,1);
			
			sendFrame126("", 8729);
			sendFrame126("", 8769);	
			sendFrame34(8847,,9,1);	
			
			sendFrame126("", 8730);
			sendFrame126("", 8770);	
			sendFrame34(8847,,10,1);	
			
			sendFrame126("", 8731);
			sendFrame126("", 8771);	
			sendFrame34(8847,,11,1);			
			sendFrame126("", 8732);
			sendFrame126("", 8772);	
			sendFrame34(8847,,12,1);	
			
			sendFrame126("", 8733);
			sendFrame126("", 8773);	
			sendFrame34(8847,,13,1);	
			
			sendFrame126("", 8734);
			sendFrame126("", 8774);	
			sendFrame34(8847,,14,1);	
			
			sendFrame126("", 8735);
			sendFrame126("", 8775);	
			sendFrame34(8847,,15,1);	
			
			sendFrame126("", 8736);
			sendFrame126("", 8776);	
			sendFrame34(8847,,16,1);	
			
			sendFrame126("",8849);// members only skill
			sendFrame126("",8827);//steel
			sendFrame126("",8837);//mithril
			sendFrame126("",8840);
			sendFrame126("",8843);
			sendFrame126("",8846); //Skills
			sendFrame126("",8823);
			sendFrame126("",8824);
			sendFrame126("",8859);
			sendFrame126("",8862);
			sendFrame126("",8865);
			sendFrame126("",15303);
			sendFrame126("",15306);
			sendFrame126("",15309);
			showInterface(8714);
		}	*/
    public void mineInterface() {
        sendFrame126("@dre@Mining Interface", 8716);
        for (int i = 8720; i < 8799; i++) {
            sendFrame126("", i);
        }
        for (int i = 1; i < 26; i++) {
            sendFrame34(8847, -1, i, 1);
        }
        sendFrame126("1", 8720);
        sendFrame126("Rune Essence", 8760);
        sendFrame34(8847, 1436, 0, 1);

        sendFrame126("1", 8721);            // starter interface
        sendFrame126("Clay", 8761);
        sendFrame34(8847, 434, 1, 1);

        sendFrame126("1", 8722);
        sendFrame126("Copper Ore", 8762);
        sendFrame34(8847, 436, 2, 1);

        sendFrame126("1", 8723);
        sendFrame126("Tin Ore", 8763);
        sendFrame34(8847, 438, 3, 1);

        sendFrame126("15", 8724);
        sendFrame126("Iron Ore", 8764);
        sendFrame34(8847, 440, 4, 1);

        sendFrame126("20", 8725);
        sendFrame126("Silver Ore", 8765);
        sendFrame34(8847, 442, 5, 1);

        sendFrame126("30", 8726);
        sendFrame126("Coal", 8766);
        sendFrame34(8847, 453, 6, 1);

        sendFrame126("40", 8727);
        sendFrame126("Gold Ore", 8767);
        sendFrame34(8847, 444, 7, 1);

        sendFrame126("55", 8728);
        sendFrame126("Mithril Ore", 8768);
        sendFrame34(8847, 447, 8, 1);

        sendFrame126("70", 8729);
        sendFrame126("Adamantite Ore", 8769);
        sendFrame34(8847, 449, 9, 1);

        sendFrame126("85", 8730);
        sendFrame126("Runite Ore", 8770);
        sendFrame34(8847, 451, 10, 1);

        sendFrame126("", 8849);// members only skill
        sendFrame126("", 8827);//steel
        sendFrame126("", 8837);//mithril
        sendFrame126("", 8840);
        sendFrame126("", 8843);
        sendFrame126("", 8846); //Skills
        sendFrame126("", 8823);
        sendFrame126("", 8824);
        sendFrame126("", 8859);
        sendFrame126("", 8862);
        sendFrame126("", 8865);
        sendFrame126("", 15303);
        sendFrame126("", 15306);
        sendFrame126("", 15309);
        showInterface(8714);
    }

    public void smithInterface() {
        sendFrame126("@dre@Smithing Interface", 8716);
        for (int i = 8720; i < 8799; i++) {
            sendFrame126("", i);
        }
        for (int i = 1; i < 26; i++) {
            sendFrame34(8847, -1, i, 1);
        }
        sendFrame126("1", 8720);
        sendFrame126("Bronze Bar(1 Copper Ore 1 Tin Ore)", 8760);
        sendFrame34(8847, 2349, 0, 1);

        sendFrame126("15", 8721);
        sendFrame126("Iron Bar(1 Iron ore)", 8761);
        sendFrame34(8847, 2351, 1, 1);

        sendFrame126("20", 8722);
        sendFrame126("Silver Bar(1 Silver Ore)", 8762);
        sendFrame34(8847, 2355, 2, 1);

        sendFrame126("30", 8723);
        sendFrame126("Steel Bar(1 Iron Ore 2 Coal)", 8763);
        sendFrame34(8847, 2353, 3, 1);

        sendFrame126("40", 8724);
        sendFrame126("Gold Bar(1 Gold Ore)", 8764);
        sendFrame34(8847, 2357, 4, 1);

        sendFrame126("50", 8725);
        sendFrame126("Mithril bar(1 Mithril Ore 4 Coal)", 8765);
        sendFrame34(8847, 2359, 5, 1);

        sendFrame126("70", 8726);
        sendFrame126("Adamantite Bar(1 Adamantite Ore 6 Coal)", 8766);
        sendFrame34(8847, 2361, 6, 1);

        sendFrame126("85", 8727);
        sendFrame126("Runite Bar(1 Runite Ore 8 Coal)", 8767);
        sendFrame34(8847, 2363, 7, 1);

        sendFrame126("", 8849);// members only skill
        sendFrame126("", 8827);//steel
        sendFrame126("", 8837);//mithril
        sendFrame126("", 8840);
        sendFrame126("", 8843);
        sendFrame126("", 8846); //Skills
        sendFrame126("", 8823);
        sendFrame126("", 8824);
        sendFrame126("", 8859);
        sendFrame126("", 8862);
        sendFrame126("", 8865);
        sendFrame126("", 15303);
        sendFrame126("", 15306);
        sendFrame126("", 15309);
        showInterface(8714);
    }

    public void fishInterface() {
        sendFrame126("@dre@Fishing Interface", 8716);
        for (int i = 8720; i < 8799; i++) {
            sendFrame126("", i);
        }
        for (int i = 1; i < 26; i++) {
            sendFrame34(8847, -1, i, 1);
        }
        sendFrame126("1", 8720);
        sendFrame126("Raw Shrimp", 8760);
        sendFrame34(8847, 317, 0, 1);

        sendFrame126("5", 8721);
        sendFrame126("Raw Sardine", 8761);
        sendFrame34(8847, 327, 1, 1);

        sendFrame126("10", 8722);
        sendFrame126("Raw Herring", 8762);
        sendFrame34(8847, 345, 2, 1);

        sendFrame126("15", 8723);
        sendFrame126("Raw Anchovies", 8763);
        sendFrame34(8847, 321, 3, 1);

        sendFrame126("16", 8724);
        sendFrame126("Raw Mackerel", 8764);
        sendFrame34(8847, 353, 4, 1);

        sendFrame126("20", 8725);
        sendFrame126("Raw Trout", 8765);
        sendFrame34(8847, 335, 5, 1);

        sendFrame126("23", 8726);
        sendFrame126("Raw Cod", 8766);
        sendFrame34(8847, 341, 6, 1);

        sendFrame126("25", 8727);
        sendFrame126("Raw Pike", 8767);
        sendFrame34(8847, 349, 7, 1);

        sendFrame126("30", 8728);
        sendFrame126("Raw Salmon", 8768);
        sendFrame34(8847, 331, 8, 1);

        sendFrame126("35", 8729);
        sendFrame126("Raw Tuna", 8769);
        sendFrame34(8847, 359, 9, 1);

        sendFrame126("40", 8730);
        sendFrame126("Raw Lobster", 8770);
        sendFrame34(8847, 377, 10, 1);

        sendFrame126("46", 8731);
        sendFrame126("Raw Bass", 8771);
        sendFrame34(8847, 363, 11, 1);

        sendFrame126("50", 8732);
        sendFrame126("Raw Swordfish", 8772);
        sendFrame34(8847, 371, 12, 1);

        sendFrame126("53", 8733);
        sendFrame126("Raw Lava Eel", 8773);
        sendFrame34(8847, 2148, 13, 1);

        sendFrame126("62", 8734);
        sendFrame126("Raw Monkfish", 8774);
        sendFrame34(8847, 7946, 14, 1);

        sendFrame126("65", 8735);
        sendFrame126("Raw Karambwan", 8775);
        sendFrame34(8847, 3142, 15, 1);

        sendFrame126("76", 8736);
        sendFrame126("Raw Shark", 8776);
        sendFrame34(8847, 383, 16, 1);

        sendFrame126("79", 8737);
        sendFrame126("Raw Sea Turtle", 8777);
        sendFrame34(8847, 395, 17, 1);

        sendFrame126("81", 8738);
        sendFrame126("Raw Manta Ray", 8778);
        sendFrame34(8847, 389, 18, 1);

        sendFrame126("", 8849);// members only skill
        sendFrame126("", 8827);//steel
        sendFrame126("", 8837);//mithril
        sendFrame126("", 8840);
        sendFrame126("", 8843);
        sendFrame126("", 8846); //Skills
        sendFrame126("", 8823);
        sendFrame126("", 8824);
        sendFrame126("", 8859);
        sendFrame126("", 8862);
        sendFrame126("", 8865);
        sendFrame126("", 15303);
        sendFrame126("", 15306);
        sendFrame126("", 15309);
        showInterface(8714);
    }

    public void cookInterface() {
        sendFrame126("@dre@Cooking Interface", 8716);
        for (int i = 8720; i < 8799; i++) {
            sendFrame126("", i);
        }
        for (int i = 1; i < 26; i++) {
            sendFrame34(8847, -1, i, 1);
        }
        sendFrame126("1", 8720);
        sendFrame126("Raw Beef", 8760);
        sendFrame34(8847, 2132, 0, 1);

        sendFrame126("1", 8721);
        sendFrame126("Raw Shrimps", 8761);
        sendFrame34(8847, 317, 1, 1);

        sendFrame126("1", 8722);
        sendFrame126("Raw Anchovies", 8762);
        sendFrame34(8847, 321, 2, 1);

        sendFrame126("1", 8723);
        sendFrame126("Raw Sardine", 8763);
        sendFrame34(8847, 327, 3, 1);

        sendFrame126("5", 8724);
        sendFrame126("Raw Herring", 8764);
        sendFrame34(8847, 345, 4, 1);

        sendFrame126("10", 8725);
        sendFrame126("Raw Mackerel", 8765);
        sendFrame34(8847, 353, 5, 1);

        sendFrame126("15", 8726);
        sendFrame126("Raw Trout", 8766);
        sendFrame34(8847, 335, 6, 1);

        sendFrame126("18", 8727);
        sendFrame126("Raw Cod", 8767);
        sendFrame34(8847, 341, 7, 1);

        sendFrame126("20", 8728);
        sendFrame126("Raw Pike", 8768);
        sendFrame34(8847, 349, 8, 1);

        sendFrame126("25", 8729);
        sendFrame126("Raw Salmon", 8769);
        sendFrame34(8847, 331, 9, 1);

        sendFrame126("30", 8730);
        sendFrame126("Raw Karambwan", 8770);
        sendFrame34(8847, 3142, 10, 1);

        sendFrame126("30", 8731);
        sendFrame126("Raw Tuna", 8771);
        sendFrame34(8847, 359, 11, 1);

        sendFrame126("40", 8732);
        sendFrame126("Raw  Lobster", 8772);
        sendFrame34(8847, 379, 12, 1);

        sendFrame126("43", 8733);
        sendFrame126("Raw bass", 8773);
        sendFrame34(8847, 363, 13, 1);

        sendFrame126("45", 8734);
        sendFrame126("Raw Swordfish", 8774);
        sendFrame34(8847, 371, 14, 1);

        sendFrame126("53", 8735);
        sendFrame126("Raw Lava Eel", 8775);
        sendFrame34(8847, 2148, 15, 1);

        sendFrame126("62", 8736);
        sendFrame126("Raw Monkfish", 8776);
        sendFrame34(8847, 7946, 16, 1);

        sendFrame126("80", 8737);
        sendFrame126("Raw Shark", 8777);
        sendFrame34(8847, 383, 17, 1);

        sendFrame126("82", 8738);
        sendFrame126("Raw Sea Turtle", 8778);
        sendFrame34(8847, 395, 18, 1);

        sendFrame126("91", 8739);
        sendFrame126("Raw Manta", 8779);
        sendFrame34(8847, 389, 19, 1);

        sendFrame126("", 8849);// members only skill
        sendFrame126("", 8827);//steel
        sendFrame126("", 8837);//mithril
        sendFrame126("", 8840);
        sendFrame126("", 8843);
        sendFrame126("", 8846); //Skills
        sendFrame126("", 8823);
        sendFrame126("", 8824);
        sendFrame126("", 8859);
        sendFrame126("", 8862);
        sendFrame126("", 8865);
        sendFrame126("", 15303);
        sendFrame126("", 15306);
        sendFrame126("", 15309);

        showInterface(8714);
    }

    public void fireInterface() {
        sendFrame126("@dre@Firemaking Interface", 8716);
        for (int i = 8720; i < 8799; i++) {
            sendFrame126("", i);
        }
        sendFrame126("1", 8720);
        sendFrame126("Normal Logs", 8760);
        sendFrame126("15", 8721);
        sendFrame126("Oak Logs", 8761);
        sendFrame126("30", 8722);
        sendFrame126("Willow Logs", 8762);
        sendFrame126("35", 8723);
        sendFrame126("Teak Logs", 8763);
        sendFrame126("45", 8724);
        sendFrame126("Maple Logs", 8764);
        sendFrame126("50", 8725);
        sendFrame126("Mahogany Logs", 8765);
        sendFrame126("60", 8726);
        sendFrame126("Yew Logs", 8766);
        sendFrame126("75", 8727);
        sendFrame126("Magic Logs", 8767);
        sendFrame126("", 8728);
        sendFrame126("", 8768);
        sendFrame126("", 8729);
        sendFrame126("", 8769);
        sendFrame126("", 8730);
        sendFrame126("", 8770);
        sendFrame126("", 8731);
        sendFrame126("", 8771);
        sendFrame126("", 8732);
        sendFrame126("", 8772);
        sendFrame126("", 8733);
        sendFrame126("", 8773);
        sendFrame126("", 8734);
        sendFrame126("", 8774);
        sendFrame126("", 8735);
        sendFrame126("", 8775);
        sendFrame126("", 8736);
        sendFrame126("", 8776);
        sendFrame126("", 8737);
        sendFrame126("", 8777);
        sendFrame126("", 8738);
        sendFrame126("", 8778);
        sendFrame126("", 8739);
        sendFrame126("", 8779);
        sendFrame126("", 8740);
        sendFrame126("", 8780);
        sendFrame126("", 8741);
        sendFrame126("", 8781);
        sendFrame126("", 8742);
        sendFrame126("", 8782);
        sendFrame126("", 8743);
        sendFrame126("", 8783);
        sendFrame126("", 8744);
        sendFrame126("", 8784);
        sendFrame126("", 8745);
        sendFrame126("", 8785);
        sendFrame126("", 8746);
        sendFrame126("", 8786);
        sendFrame126("", 8747);
        sendFrame126("", 8787);
        sendFrame126("", 8748);
        sendFrame126("", 8788);
        sendFrame126("", 8749);
        sendFrame126("", 8789);
        sendFrame126("", 8750);
        sendFrame126("", 8790);
        sendFrame126("", 8751);
        sendFrame126("", 8791);
        sendFrame126("", 8752);
        sendFrame126("", 8792);
        sendFrame126("", 8753);
        sendFrame126("", 8793);
        sendFrame126("", 8754);
        sendFrame126("", 8794);
        sendFrame126("", 8755);
        sendFrame126("", 8795);
        sendFrame126("", 8756);
        sendFrame126("", 8796);
        sendFrame126("", 8757);
        sendFrame126("", 8797);
        sendFrame126("", 8758);
        sendFrame126("", 8798);
        sendFrame126("", 8759);
        sendFrame126("", 8799);

        sendFrame126("", 8849);// members only skill
        sendFrame126("", 8827);//steel
        sendFrame126("", 8837);//mithril
        sendFrame126("", 8840);
        sendFrame126("", 8843);
        sendFrame126("", 8846); //Skills
        sendFrame126("", 8823);
        sendFrame126("", 8824);
        sendFrame126("", 8859);
        sendFrame126("", 8862);
        sendFrame126("", 8865);
        sendFrame126("", 15303);
        sendFrame126("", 15306);
        sendFrame126("", 15309);
        for (int i = 1; i < 26; i++) {
            sendFrame34(8847, -1, i, 1);
        }
        sendFrame34(8847, 1511, 0, 1);
        sendFrame34(8847, 1521, 1, 1);
        sendFrame34(8847, 1519, 2, 1);
        sendFrame34(8847, 6333, 3, 1);
        sendFrame34(8847, 1517, 4, 1);
        sendFrame34(8847, 6332, 5, 1);
        sendFrame34(8847, 1515, 6, 1);
        sendFrame34(8847, 1513, 7, 1);
        showInterface(8714);
    }

    public void woodInterface() {
        sendFrame126("@dre@Woodcutting Interface", 8716);
        for (int i = 8720; i < 8799; i++) {
            sendFrame126("", i);
        }
        sendFrame126("1", 8720);
        sendFrame126("Normal Trees", 8760);
        sendFrame126("15", 8721);
        sendFrame126("Oak Trees", 8761);
        sendFrame126("30", 8722);
        sendFrame126("Willow Trees", 8762);
        sendFrame126("35", 8723);
        sendFrame126("Teak Trees", 8763);
        sendFrame126("45", 8724);
        sendFrame126("Maple Trees", 8764);
        sendFrame126("50", 8725);
        sendFrame126("Mahogany Trees", 8765);
        sendFrame126("60", 8726);
        sendFrame126("Yew Trees", 8766);
        sendFrame126("75", 8727);
        sendFrame126("Magic Trees", 8767);
        sendFrame126("", 8728);
        sendFrame126("", 8768);
        sendFrame126("", 8729);
        sendFrame126("", 8769);
        sendFrame126("", 8730);
        sendFrame126("", 8770);
        sendFrame126("", 8731);
        sendFrame126("", 8771);
        sendFrame126("", 8732);
        sendFrame126("", 8772);
        sendFrame126("", 8733);
        sendFrame126("", 8773);
        sendFrame126("", 8734);
        sendFrame126("", 8774);
        sendFrame126("", 8735);
        sendFrame126("", 8775);
        sendFrame126("", 8736);
        sendFrame126("", 8776);
        sendFrame126("", 8737);
        sendFrame126("", 8777);
        sendFrame126("", 8738);
        sendFrame126("", 8778);
        sendFrame126("", 8739);
        sendFrame126("", 8779);
        sendFrame126("", 8740);
        sendFrame126("", 8780);
        sendFrame126("", 8741);
        sendFrame126("", 8781);
        sendFrame126("", 8742);
        sendFrame126("", 8782);
        sendFrame126("", 8743);
        sendFrame126("", 8783);
        sendFrame126("", 8744);
        sendFrame126("", 8784);
        sendFrame126("", 8745);
        sendFrame126("", 8785);
        sendFrame126("", 8746);
        sendFrame126("", 8786);
        sendFrame126("", 8747);
        sendFrame126("", 8787);
        sendFrame126("", 8748);
        sendFrame126("", 8788);
        sendFrame126("", 8749);
        sendFrame126("", 8789);
        sendFrame126("", 8750);
        sendFrame126("", 8790);
        sendFrame126("", 8751);
        sendFrame126("", 8791);
        sendFrame126("", 8752);
        sendFrame126("", 8792);
        sendFrame126("", 8753);
        sendFrame126("", 8793);
        sendFrame126("", 8754);
        sendFrame126("", 8794);
        sendFrame126("", 8755);
        sendFrame126("", 8795);
        sendFrame126("", 8756);
        sendFrame126("", 8796);
        sendFrame126("", 8757);
        sendFrame126("", 8797);
        sendFrame126("", 8758);
        sendFrame126("", 8798);
        sendFrame126("", 8759);
        sendFrame126("", 8799);
        sendFrame126("", 8849);// members only skill
        sendFrame126("", 8827);//steel
        sendFrame126("", 8837);//mithril
        sendFrame126("", 8840);
        sendFrame126("", 8843);
        sendFrame126("", 8846); //Skills
        sendFrame126("", 8823);
        sendFrame126("", 8824);
        sendFrame126("", 8859);
        sendFrame126("", 8862);
        sendFrame126("", 8865);
        sendFrame126("", 15303);
        sendFrame126("", 15306);
        sendFrame126("", 15309);
        for (int i = 1; i < 26; i++) {
            sendFrame34(8847, -1, i, 1);
        }
        sendFrame34(8847, 1511, 0, 1);
        sendFrame34(8847, 1521, 1, 1);
        sendFrame34(8847, 1519, 2, 1);
        sendFrame34(8847, 6333, 3, 1);
        sendFrame34(8847, 1517, 4, 1);
        sendFrame34(8847, 6332, 5, 1);
        sendFrame34(8847, 1515, 6, 1);
        sendFrame34(8847, 1513, 7, 1);
        showInterface(8714);
    }

    public void thievingInterface() {
        sendFrame126("@dre@Thieving Interface", 8716);
        for (int i = 8720; i < 8799; i++) {
            sendFrame126("", i);
        }
        sendFrame126("50", 8720);
        sendFrame126("Hero", 8760);
        sendFrame126("60", 8721);
        sendFrame126("Gnome", 8761);
        sendFrame126("70", 8722);
        sendFrame126("Paladin", 8762);
        sendFrame126("99", 8723);
        sendFrame126("Thieving Skill Cape", 8763);
        sendFrame126("", 8728);
        sendFrame126("", 8768);
        sendFrame126("", 8729);
        sendFrame126("", 8769);
        sendFrame126("", 8730);
        sendFrame126("", 8770);
        sendFrame126("", 8731);
        sendFrame126("", 8771);
        sendFrame126("", 8732);
        sendFrame126("", 8772);
        sendFrame126("", 8733);
        sendFrame126("", 8773);
        sendFrame126("", 8734);
        sendFrame126("", 8774);
        sendFrame126("", 8735);
        sendFrame126("", 8775);
        sendFrame126("", 8736);
        sendFrame126("", 8776);
        sendFrame126("", 8737);
        sendFrame126("", 8777);
        sendFrame126("", 8738);
        sendFrame126("", 8778);
        sendFrame126("", 8739);
        sendFrame126("", 8779);
        sendFrame126("", 8740);
        sendFrame126("", 8780);
        sendFrame126("", 8741);
        sendFrame126("", 8781);
        sendFrame126("", 8742);
        sendFrame126("", 8782);
        sendFrame126("", 8743);
        sendFrame126("", 8783);
        sendFrame126("", 8744);
        sendFrame126("", 8784);
        sendFrame126("", 8745);
        sendFrame126("", 8785);
        sendFrame126("", 8746);
        sendFrame126("", 8786);
        sendFrame126("", 8747);
        sendFrame126("", 8787);
        sendFrame126("", 8748);
        sendFrame126("", 8788);
        sendFrame126("", 8749);
        sendFrame126("", 8789);
        sendFrame126("", 8750);
        sendFrame126("", 8790);
        sendFrame126("", 8751);
        sendFrame126("", 8791);
        sendFrame126("", 8752);
        sendFrame126("", 8792);
        sendFrame126("", 8753);
        sendFrame126("", 8793);
        sendFrame126("", 8754);
        sendFrame126("", 8794);
        sendFrame126("", 8755);
        sendFrame126("", 8795);
        sendFrame126("", 8756);
        sendFrame126("", 8796);
        sendFrame126("", 8757);
        sendFrame126("", 8797);
        sendFrame126("", 8758);
        sendFrame126("", 8798);
        sendFrame126("", 8759);
        sendFrame126("", 8799);

        sendFrame126("", 8849);// members only skill
        sendFrame126("", 8827);//steel
        sendFrame126("", 8837);//mithril
        sendFrame126("", 8840);
        sendFrame126("", 8843);
        sendFrame126("", 8846); //Skills
        sendFrame126("", 8823);
        sendFrame126("", 8824);
        sendFrame126("", 8859);
        sendFrame126("", 8862);
        sendFrame126("", 8865);
        sendFrame126("", 15303);
        sendFrame126("", 15306);
        sendFrame126("", 15309);
        for (int i = 1; i < 26; i++) {
            sendFrame34(8847, -1, i, 1);
        }
        sendFrame34(8847, 3259, 0, 1);
        sendFrame34(8847, 3257, 1, 1);
        sendFrame34(8847, 3255, 2, 1);
        sendFrame34(8847, 2710, 3, 1);
        showInterface(8714);
    }

    public void resetArmor() {
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_FEET] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] = 0;
        client.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] = 0;
    }

    public boolean wearingArmor() {
        if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_HAT] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_CHEST] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_LEGS] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_FEET] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_WEAPON] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_CAPE] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_ARROWS] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_AMULET] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_HANDS] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_SHIELD] > 0)
            return true;
        else if (client.playerEquipment[PlayerEquipmentConstants.PLAYER_RING] > 0)
            return true;
        else
            return false;
    }


}
