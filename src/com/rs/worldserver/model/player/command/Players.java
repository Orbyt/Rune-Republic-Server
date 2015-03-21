package com.rs.worldserver.model.player.command;

//Shard Revolutions Generic MMORPG Server
//Copyright (C) 2008  Graham Edgecombe

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.

//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of base64
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
/**
 * Max command
 * 
 * @author Graham
 * 
 */
public class Players implements Command {

	@Override
	public void execute(Client c, String command) {
			c.getActionAssistant().Send("There are currently "+PlayerManager.getSingleton().getPlayerCount()+" people playing!");
			/*c.getActionAssistant().clearQuestInterface();
			for(int i = 0; i < Constants.MAXIMUM_PLAYERS; i++) {
					Client p = (Client) PlayerManager.getSingleton().getPlayers()[i];
					if (p == null)
						continue;
					if (!p.isActive || p.disconnected)
						continue;
				
                   String title = "";                   
                   /* if(p.playerRights == 1){
                        title = "@blu@Fag: ";
                        
                    }
					if(p.playerRights == 2){
                        title = "@whi@$$$: ";
                        
                    }
					if(p.playerRights == 3){
                        title = "@blue=@???: ";
                        
                    }
					if(p.playerRights = 4){
                        title = "@red@Admin: ";
                        
                    }
					if(p.playerRights == 10){
                        title = "@red@Owner: ";
                        
                    }    *             
                    title += "@dbl@" + p.playerName;
                    String extra = "";
					if(title == "" || title == null) {
						
					} else {
						c.getActionAssistant().sendFrame126("" + extra + "" + title +" @yel@Combat   "+ p.combatLevel+" - Pk Points "+ p.pkpoints, 8146+i);
						c.getActionAssistant().sendFrame126("@gre@Current Players", 8144);  //Title
					}
				
			}
			c.getActionAssistant().showInterface(8134);
			c.flushOutStream();*/
		}

	}

