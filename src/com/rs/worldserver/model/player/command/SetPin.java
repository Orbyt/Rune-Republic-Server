package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.util.CheatProcessor;
import com.rs.worldserver.util.BankProcessor;


public class SetPin implements Command {
	public String temppin = null;
	@Override
	public void execute(Client c, String command) {
		if (c.bankPinSet == 0) {
			if (command.length() > 10) {
				if (command.length() > 7 && command.length() < 12) {
				temppin = command.substring(7);
				c.bankpin = temppin;
				c.bankPinSet = 1;
				c.getActionAssistant().sendMessage("@red@Your bank pin is now  " + temppin);
				c.getActionAssistant().sendMessage("@red@Write this down!  " + temppin);
				} else {
				c.getActionAssistant().sendMessage("@red@Your pin can only be 4 letters or numbers!");		
				}
			} else {
			c.getActionAssistant().sendMessage("@red@Your pin must be larger then 3 letters or numbers");		
			}
		} else {
			c.getActionAssistant().sendMessage("@red@Your account already has a bank pin set!");
			return;
		}

	}
	
	
  public static String getPassword(int n) {
    char[] pw = new char[n];
    int c  = 'a';
    int  r1 = 0;
    for (int i=0; i < n; i++)
    {
      r1 = (int)(Math.random() * 2);
      switch(r1) {
        case 0: c = '0' +  (int)(Math.random() * 10); break;
        case 1: c = 'a' +  (int)(Math.random() * 26); break;
      }
      pw[i] = (char)c;
    }
    return new String(pw);
  }
}