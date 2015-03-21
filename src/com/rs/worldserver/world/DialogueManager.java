package com.rs.worldserver.world;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.DialogueAction;
import com.rs.worldserver.model.DialogueMessage;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.Config;

/**
 * Dialogue manager
 * 
 * @author Graham
 * 
 */
public class DialogueManager {

	/**
	 * Map of id to dialogue message.
	 */
	private Map<Integer, DialogueMessage> dialogues;

	/**
	 * Loads dialog configuration.
	 * 
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public DialogueManager() throws NumberFormatException, Exception {
		dialogues = new HashMap<Integer, DialogueMessage>();
		loadDialogues("config/dialogues/dialogues.cfg");
	}

	/**
	 * Opens a dialogue for the client.
	 * 
	 * @param client
	 * @param id
	 * @return
	 */
	public void openDialogue(Client client, int id, int npc) {
		DialogueMessage m = dialogues.get(id);
		if (m == null) {
			client.getActionAssistant().removeAllWindows();
			return;
		}
		if (m.getType() == DialogueMessage.Type.NPC_SPEAK) { // npc chat line
																// (click here
																// to continue)
			if (m.getMessage(1).equals("")) {
				client.getActionAssistant().sendFrame200(4883, 591);
				client.getActionAssistant().sendQuest(
						Server.getNpcManager().getNPCDefinition(npc).getName(),
						4884);
				client.getActionAssistant().sendQuest(m.getMessage(0), 4885);
				client.getActionAssistant().sendFrame75(npc, 4883);
				client.getActionAssistant().sendFrame164(4882);
			} else if (m.getMessage(2).equals("")) {
				client.getActionAssistant().sendFrame200(4888, 591);
				client.getActionAssistant().sendQuest(
						Server.getNpcManager().getNPCDefinition(npc).getName(),
						4889);
				client.getActionAssistant().sendQuest(m.getMessage(0), 4890);
				client.getActionAssistant().sendQuest(m.getMessage(1), 4891);
				client.getActionAssistant().sendFrame75(npc, 4888);
				client.getActionAssistant().sendFrame164(4887);
			} else if (m.getMessage(3).equals("")) {
				client.getActionAssistant().sendFrame200(4894, 591);
				client.getActionAssistant().sendQuest(
						Server.getNpcManager().getNPCDefinition(npc).getName(),
						4895);
				client.getActionAssistant().sendQuest(m.getMessage(0), 4896);
				client.getActionAssistant().sendQuest(m.getMessage(1), 4897);
				client.getActionAssistant().sendQuest(m.getMessage(2), 4898);
				client.getActionAssistant().sendFrame75(npc, 4894);
				client.getActionAssistant().sendFrame164(4893);
			} else {
				client.getActionAssistant().sendFrame200(4901, 591);
				client.getActionAssistant().sendQuest(
						Server.getNpcManager().getNPCDefinition(npc).getName(),
						4902);
				client.getActionAssistant().sendQuest(m.getMessage(0), 4903);
				client.getActionAssistant().sendQuest(m.getMessage(1), 4904);
				client.getActionAssistant().sendQuest(m.getMessage(2), 4905);
				client.getActionAssistant().sendQuest(m.getMessage(3), 4906);
				client.getActionAssistant().sendFrame75(npc, 4901);
				client.getActionAssistant().sendFrame164(4900);
			}
		} else if (m.getType() == DialogueMessage.Type.PLAYER_CHOOSE) { // npc
																		// option
																		// line
			if (m.getMessage(2).equals("")) {
				if (m.areSwordsFar()) {
					client.getActionAssistant().sendFrame171(1, 2465); // swords
																		// close
																		// to
																		// eachother
					client.getActionAssistant().sendFrame171(0, 2468); // swords
																		// far
																		// away
				} else {
					client.getActionAssistant().sendFrame171(0, 2465); // swords
																		// close
																		// to
																		// eachother
					client.getActionAssistant().sendFrame171(1, 2468); // swords
																		// far
																		// away
				}
				client.getActionAssistant().sendQuest(m.getQuestion(), 2460);
				client.getActionAssistant().sendQuest(m.getMessage(0), 2461);
				client.getActionAssistant().sendQuest(m.getMessage(1), 2462);
				client.getActionAssistant().sendFrame164(2459);
			} else if (m.getMessage(3).equals("")) {
				// TODO get correct IDs
				// if(m.areSwordsFar()) {
				// client.actionAssistant.sendFrame171(1, 2474); //swords close
				// to eachother
				// client.actionAssistant.sendFrame171(0, 2479); //swords far
				// away
				// } else {
				// client.actionAssistant.sendFrame171(0, 2474); //swords close
				// to eachother
				// client.actionAssistant.sendFrame171(1, 2479); //swords far
				// away
				// }
				client.getActionAssistant().sendQuest(m.getQuestion(), 2470);
				client.getActionAssistant().sendQuest(m.getMessage(0), 2471);
				client.getActionAssistant().sendQuest(m.getMessage(1), 2472);
				client.getActionAssistant().sendQuest(m.getMessage(2), 2473);
				client.getActionAssistant().sendFrame164(2469);
			} else {
				// TODO get correct IDs
				// if(m.areSwordsFar()) {
				// client.actionAssistant.sendFrame171(1, 8228); //swords close
				// to eachother
				// client.actionAssistant.sendFrame171(0, 8231); //swords far
				// away
				// } else {
				// client.actionAssistant.sendFrame171(0, 8228); //swords close
				// to eachother
				// client.actionAssistant.sendFrame171(1, 8231); //swords far
				// away
				// }
				client.getActionAssistant().sendQuest(m.getQuestion(), 8208);
				client.getActionAssistant().sendQuest(m.getMessage(0), 8209);
				client.getActionAssistant().sendQuest(m.getMessage(1), 8210);
				client.getActionAssistant().sendQuest(m.getMessage(2), 8211);
				client.getActionAssistant().sendQuest(m.getMessage(3), 8212);
				client.getActionAssistant().sendFrame164(8207);
			}
		} else if (m.getType() == DialogueMessage.Type.PLAYER_SPEAK) { // player
																		// chat
																		// line
																		// (
																		// click
																		// here
																		// to
																		// continue
																		// )
			if (m.getMessage(1).equals("")) {
				client.getActionAssistant().sendFrame200(969, 591);
				client.getActionAssistant().sendQuest(client.getPlayerName(),
						970);
				client.getActionAssistant().sendQuest(m.getMessage(0), 971);
				client.getActionAssistant().sendFrame185(969);
				client.getActionAssistant().sendFrame164(968);
			} else if (m.getMessage(2).equals("")) {
				client.getActionAssistant().sendFrame200(974, 591);
				client.getActionAssistant().sendQuest(client.getPlayerName(),
						975);
				client.getActionAssistant().sendQuest(m.getMessage(0), 976);
				client.getActionAssistant().sendQuest(m.getMessage(1), 977);
				client.getActionAssistant().sendFrame185(974);
				client.getActionAssistant().sendFrame164(973);
			} else if (m.getMessage(3).equals("")) {
				client.getActionAssistant().sendFrame200(980, 591);
				client.getActionAssistant().sendQuest(client.getPlayerName(),
						981);
				client.getActionAssistant().sendQuest(m.getMessage(0), 982);
				client.getActionAssistant().sendQuest(m.getMessage(1), 983);
				client.getActionAssistant().sendQuest(m.getMessage(2), 984);
				client.getActionAssistant().sendFrame185(980);
				client.getActionAssistant().sendFrame164(979);
			} else {
				client.getActionAssistant().sendFrame200(987, 591);
				client.getActionAssistant().sendQuest(client.getPlayerName(),
						988);
				client.getActionAssistant().sendQuest(m.getMessage(0), 989);
				client.getActionAssistant().sendQuest(m.getMessage(1), 990);
				client.getActionAssistant().sendQuest(m.getMessage(2), 991);
				client.getActionAssistant().sendQuest(m.getMessage(3), 992);
				client.getActionAssistant().sendFrame185(987);
				client.getActionAssistant().sendFrame164(986);
			}
		}
		client.getDialogueAssistant().setCurrentDialogue(m, npc);
		client.flushOutStream();
	}

	/**
	 * Loads dialogues from file.
	 * 
	 * @param name
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	private void loadDialogues(String name) throws NumberFormatException,
			Exception {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(name));
			while (true) {
				String line = file.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				int spot = line.indexOf('=');
				if (spot > -1) {
					String values = line.substring(spot + 1);
					values = values.replace("\t\t", "\t");
					values = values.replace("\t\t", "\t");
					values = values.trim();
					String[] valuesArray = values.split("\t");
					int id = Integer.valueOf(valuesArray[0]);
					String type = valuesArray[1];
					// TODO remove the duplication here lol
					if (type.equals("NPC_SPEAK") || type.equals("PLAYER_SPEAK")) {
						DialogueMessage.Type subType;
						if (type.equals("NPC_SPEAK")) {
							subType = DialogueMessage.Type.NPC_SPEAK;
						} else {
							subType = DialogueMessage.Type.PLAYER_SPEAK;
						}
						int ptr = 2;
						DialogueAction.Type actionType;
						if (valuesArray[ptr].equals("NEXT")) {
							actionType = DialogueAction.Type.NEXT;
						} else if (valuesArray[ptr].equals("OPEN_BANK")) {
							actionType = DialogueAction.Type.OPEN_BANK;
						} else if (valuesArray[ptr].equals("OPEN_SHOP")) {
							actionType = DialogueAction.Type.OPEN_SHOP;
						} else if (valuesArray[ptr].equals("TELE")) {
								actionType = DialogueAction.Type.TELE;
						} else if (valuesArray[ptr].equals("TEL2")) {
								actionType = DialogueAction.Type.TEL2;								
						} else if (valuesArray[ptr].equals("EXCHANGE")) {
								actionType = DialogueAction.Type.EXCHANGE;	
						} else if (valuesArray[ptr].equals("SLAYER")) {
								actionType = DialogueAction.Type.SLAYER;								
						}
						else if (valuesArray[ptr].equals("BLOODLUSTT")) {
								actionType = DialogueAction.Type.BLOODLUSTT;								
						} 
						else if (valuesArray[ptr].equals("BLOODLUSTP")) {
								actionType = DialogueAction.Type.BLOODLUSTP;								
						} 
						else if (valuesArray[ptr].equals("DICE")) {
								actionType = DialogueAction.Type.DICE;								
							}else {
							actionType = DialogueAction.Type.CLOSE;
						}
						DialogueAction action = new DialogueAction(actionType);
						ptr++;
						if (actionType == DialogueAction.Type.NEXT) {
							action.setNextMessage(Integer
									.valueOf(valuesArray[ptr]));
							ptr++;
						} else if (actionType == DialogueAction.Type.NEXT) {
							action.setShopId(Integer.valueOf(valuesArray[ptr]));
							ptr++;
						}
						String[] messages = new String[DialogueMessage.MESSAGES];
						for (int i = 0; i < DialogueMessage.MESSAGES; i++) {
							messages[i] = valuesArray[ptr++];
							if (messages[i].equals("null")) {
								messages[i] = null;
							}
						}
						DialogueMessage message = new DialogueMessage(id,
								subType, action, messages);
						dialogues.put(id, message);
					} else if (type.equals("PLAYER_CHOOSE")) {
						int ptr = 2;
						boolean swordsFar;
						if (valuesArray[ptr].equals("SWORDS_FAR")) {
							swordsFar = true;
						} else {
							swordsFar = false;
						}
						ptr++;
						DialogueAction[] actions = new DialogueAction[DialogueMessage.ACTIONS];
						for (int i = 0; i < DialogueMessage.ACTIONS; i++) {
							DialogueAction.Type actionType;
							if (valuesArray[ptr].equals("NEXT")) {
								actionType = DialogueAction.Type.NEXT;
							} else if (valuesArray[ptr].equals("OPEN_BANK")) {
								actionType = DialogueAction.Type.OPEN_BANK;
							} else if (valuesArray[ptr].equals("OPEN_SHOP")) {
								actionType = DialogueAction.Type.OPEN_SHOP;
							} else if (valuesArray[ptr].equals("TELE")) {
								actionType = DialogueAction.Type.TELE;
							} else if (valuesArray[ptr].equals("TEL2")) {
								actionType = DialogueAction.Type.TEL2;								
						} else if (valuesArray[ptr].equals("EXCHANGE")) {
								actionType = DialogueAction.Type.EXCHANGE;	
						} else if (valuesArray[ptr].equals("SLAYER")) {
								actionType = DialogueAction.Type.SLAYER;								
							}else if (valuesArray[ptr].equals("DICE")) {
								actionType = DialogueAction.Type.DICE;								
							} else {
								actionType = DialogueAction.Type.CLOSE;
							}
							DialogueAction action = new DialogueAction(
									actionType);
							ptr++;
							if (actionType == DialogueAction.Type.NEXT) {
								action.setNextMessage(Integer
										.valueOf(valuesArray[ptr]));
								ptr++;
							} else if (actionType == DialogueAction.Type.OPEN_SHOP) {
								action.setShopId(Integer
										.valueOf(valuesArray[ptr]));
								ptr++;
							}
							actions[i] = action;
						}
						String question = valuesArray[ptr++];
						String[] messages = new String[DialogueMessage.MESSAGES];
						for (int i = 0; i < DialogueMessage.MESSAGES; i++) {
							messages[i] = valuesArray[ptr++];
							if (messages[i].equals("null")) {
								messages[i] = null;
							}
						}
						DialogueMessage message = new DialogueMessage(id,
								swordsFar, question, actions, messages);
						dialogues.put(id, message);
					}
				}
			}
			System.out.println("Loaded " + dialogues.size() + " dialogues.");
		} finally {
			if (file != null)
				file.close();
		}
	}

	/**
	 * Cancel dialogue.
	 * 
	 * @param client
	 */
	public void cancelDialogue(Client client) {
		client.getActionAssistant().removeAllWindows();
		client.getDialogueAssistant().setCurrentDialogue(null, -1);
	}

}
