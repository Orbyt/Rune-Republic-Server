package com.rs.worldserver.model;

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

/**
 * Represents a single dialogue message.
 * 
 * @author Graham
 * 
 */
public class DialogueMessage {

	/**
	 * Dialog type.
	 * 
	 * @author Graham
	 * 
	 */
	public enum Type {
		PLAYER_SPEAK, NPC_SPEAK, PLAYER_CHOOSE, SKILL_LEVEL_UP,
	}

	/**
	 * Unique id.
	 */
	private int id;

	/**
	 * Type of message.
	 */
	private Type type;

	/**
	 * Total number of allowed messages.
	 */
	public static final int MESSAGES = 4;

	/**
	 * Total number of allowed actions.
	 */
	public static final int ACTIONS = 4;

	/**
	 * Array of messages.
	 */
	private String[] messages = new String[MESSAGES];

	/**
	 * Array of actions.
	 */
	private DialogueAction[] actions = new DialogueAction[ACTIONS];

	/**
	 * Question.
	 */
	private String question;

	/**
	 * Are the swords far away?
	 */
	private boolean swordsFar;

	/**
	 * Constructor for PLAYER_SPEAK and NPC_SPEAK.
	 * 
	 * @param id
	 * @param type
	 * @param nextAction
	 * @param messages
	 * @throws Exception
	 */
	public DialogueMessage(int id, Type type, DialogueAction nextAction,
			String[] messages) throws Exception {
		if (type == Type.PLAYER_CHOOSE) {
			throw new Exception(
					"Player choose type not valid for this constructor");
		}
		this.id = id;
		this.type = type;
		this.actions[0] = nextAction;
		this.messages = messages;
	}

	/**
	 * Constructor for PLAYER_CHOOSE.
	 * 
	 * @param id
	 * @param question
	 * @param actions
	 * @param messages
	 */
	public DialogueMessage(int id, boolean swordsFar, String question,
			DialogueAction[] actions, String[] messages) {
		this.id = id;
		this.swordsFar = swordsFar;
		this.type = Type.PLAYER_CHOOSE;
		this.question = question;
		this.actions = actions;
		this.messages = messages;
	}

	/**
	 * Set type only constructor.
	 * 
	 * @param type
	 */
	public DialogueMessage(Type type) {
		this.type = type;
	}

	/**
	 * Get the messages.
	 * 
	 * @return
	 */
	public String[] getMessages() {
		return this.messages;
	}

	/**
	 * Get the actions.
	 * 
	 * @return
	 */
	public DialogueAction[] getActions() {
		return this.actions;
	}

	/**
	 * Get the question.
	 * 
	 * @return
	 */
	public String getQuestion() {
		return this.question;
	}

	/**
	 * Gets the type.
	 * 
	 * @return
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Gets the id.
	 * 
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets message by id.
	 * 
	 * @param id
	 * @return
	 */
	public String getMessage(int id) {
		String msg = this.messages[id];
		if (msg == null) {
			msg = "";
		}
		return msg;
	}

	/**
	 * Are swords far away?
	 * 
	 * @return
	 */
	public boolean areSwordsFar() {
		return this.swordsFar;
	}

	/**
	 * Sets actions.
	 * 
	 * @param actions
	 */
	public void setActions(DialogueAction[] actions) {
		this.actions = actions;
	}

}
