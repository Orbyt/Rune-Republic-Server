package com.rs.worldserver.content.skill;


import com.rs.worldserver.Server;
import com.rs.worldserver.content.Skill;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.object.GameObject;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.PlayerEquipmentConstants;


public class RuneCrafting implements Skill {

        /**
         * Rune essence ID constant.
         */
        private static final int RUNE_ESS = 1436;

        /**
         * Pure essence ID constant.
         */
        private static final int PURE_ESS = 7936;

        /**
         * An array containing the rune item numbers.
         */
        private int[] runes = {
                556, 558, 555, 557, 554, 559, 564, 562, 561, 563, 560, 565
        };

        /**
         * An array containing the object IDs of the runecrafting altars.
         */
        public int[] altarID = {
                2478, 2479, 2480, 2481, 2482, 2483, 2484, 2487, 2486, 2485, 2488, 2489
        };

        private double[] exp ={
                5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5, 10, 11
        };

        /**
         * 2D Array containing the levels required to craft the specific rune.
         */
        private int[][] craftLevelReq = {
                {556, 1}, {558, 2}, {555, 5}, {557, 9}, {554, 14}, {559, 20},
                {564, 27}, {562, 35}, {561, 44}, {563, 54}, {560, 65}, {565, 77}
        };

        /**
         * 2D Array containing the levels that you can craft multiple runes.
         */
        private int[][] multipleRunes = {
                {11, 22, 33, 44, 55, 66, 77, 88, 99},
                {14, 28, 42, 56, 70, 84, 98},
                {19, 38, 57, 76, 95},
                {26, 52, 78},
                {35, 70},
                {46, 92},
                {59},
                {74},
                {91}
        };

        /**
         * Checks through all 28 item inventory slots for the specified item.
         */
        private boolean itemInInv(Client p, int itemID, int slot, boolean checkWholeInv) {
                if (checkWholeInv) {
                        for (int i = 0; i < 28; i++) {
                                if (p.playerItems[i] == itemID + 1) {
                                        return true;
                                }
                        }
                } else {
                        if (p.playerItems[slot] == itemID + 1) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * Replaces essence in the inventory with the specified rune.
         */
        private void replaceEssence(Client p, int essType, int runeID, int multiplier) {
                for (int i = 0; i < 28; i++) {
                        if (itemInInv(p, essType, i, false)) {
                                p.getActionAssistant().deleteItem(essType, i, 1);
                                p.getActionAssistant().addItem(runeID, 1 * multiplier);
                        }
                }
        }

        /**
         * Crafts the specific rune.
         */
        public void craftRunes(final Client p, int altarID) {
                if (System.currentTimeMillis() - p.runeCraftDelay < 1000) {
                        return;
                }
                int runeID = 0;

                for (int i = 0; i < this.altarID.length; i++) {
                        if (altarID == this.altarID[i]) {
                                runeID = runes[i];
                        }
                }
                for (int i = 0; i < craftLevelReq.length; i++) {
                        if (runeID == runes[i]) {
                                if (p.playerLevel[20] >= craftLevelReq[i][1]) {
                                        if (p.getActionAssistant().playerHasItem(1436,1)) {
                                                int multiplier = 0;

                                                int[] nonMultiples = {
                                                    563, 560, 565
                                                };

                                                boolean isMultipleRune = true;
												int amount = p.getActionAssistant().getItemAmount(1436);
                                                for (int j = 0; j < nonMultiples.length; j++) {
                                                        if (runeID == nonMultiples[j]) {
                                                                isMultipleRune = false;
                                                        }
                                                }
                                                if (isMultipleRune) {
                                                        for (int j = 0; j < multipleRunes[i].length; j++) {
                                                                if (p.playerLevel[20] >= multipleRunes[i][j]) {
                                                                        multiplier = j+2;
																		//p.getActionAssistant().Send("x1"+multiplier);
                                                                } else if(multiplier == 1 || multiplier == 0) {
                                                                        multiplier = 1;	
																		//p.getActionAssistant().Send("x2"+multiplier);
                                                                }
                                                        }
                                                }
                                                replaceEssence(p, itemInInv(p, RUNE_ESS, 0, true) ? RUNE_ESS : PURE_ESS, runeID, multiplier);
                                                p.runeCraftDelay = System.currentTimeMillis();
                                                p.getActionAssistant().startAnimation(791);
                                                p.getActionAssistant().addSkillXP((int) exp[i]*amount, 20);
												p.getActionAssistant().refreshSkill(20);
												if(p.sounds == 1){
                                                p.getActionAssistant().frame174(481, 0, 0);
												}
                                                p.gfx100(186);
												p.getActionAssistant().Send("You bind the temple's power into" + " " +Server.getItemManager().getItemDefinition(runeID).getName().toLowerCase() + "s.");
                                                return;
                                        }
                                        p.getActionAssistant().sendMessage("You need to have essence to craft runes!");
                                        return;
                                }
                                p.getActionAssistant().sendMessage("You need a Runecrafting level of "+ craftLevelReq[i][1] +" to craft this rune.");
                        }
                }
        }

}