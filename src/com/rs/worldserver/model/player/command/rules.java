package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class rules implements Command {

	@Override
	public void execute(Client c, String command) {
			c.getActionAssistant().clearQuestInterface();
			c.getActionAssistant().sendFrame126("@red@Rune Republic Rules", 8144); // Title
c.getActionAssistant().sendFrame126("Do not bug abuse.", 8147);
c.getActionAssistant().sendFrame126("Do not advertise other servers.", 8148);
//c.getActionAssistant().sendFrame126("Do not buy or sell Rune Republic items for non Rune Republic items.", 8149);
c.getActionAssistant().sendFrame126("Do not disrespect or impersonate staff members.",8150);
c.getActionAssistant().sendFrame126("Do not scam players for donator pins or items.",8151);
c.getActionAssistant().sendFrame126("Do not hack or attempt to hack other players.",8152);
c.getActionAssistant().sendFrame126("Do not PK Point Cheat.",8153);
c.getActionAssistant().sendFrame126("Do not DDoS.",8154);
c.getActionAssistant().sendFrame126("-", 8155);
c.getActionAssistant().sendFrame126("Sharing accounts is at your own risk.", 8156);
c.getActionAssistant().sendFrame126("-",8157);
c.getActionAssistant().sendFrame126("To APPEAL a punishment, please visit the forums.",8158); 
			c.getActionAssistant().showInterface(8134);
			c.flushOutStream();
	
	}
}