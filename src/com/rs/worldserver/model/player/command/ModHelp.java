package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class ModHelp implements Command {

	@Override
	public void execute(Client c, String command) {
		if (c.playerRights > 3) {
			c.getActionAssistant().clearQuestInterface();
			c.getActionAssistant().sendFrame126("@dre@Rune Republic Information Menu", 8144); // Title
			c.getActionAssistant().sendFrame126("@dbl@Players Online: @gre@" + PlayerManager.getSingleton().getPlayerCount(),8146);
			c.getActionAssistant().sendFrame126(" ",8147);
			c.getActionAssistant().sendFrame126("::pass NEWPASSWORD - changes your password", 8148);
			c.getActionAssistant().sendFrame126("::players - view everyone online", 8149);
			c.getActionAssistant().sendFrame126("::home - Teleport To Edgeville",8150);
			c.getActionAssistant().sendFrame126("::char - Change Your Look",8151);	
			c.getActionAssistant().sendFrame126("::mods - Mods Only Help List",8152);		
			c.getActionAssistant().sendFrame126(" ",8153);
			c.getActionAssistant().sendFrame126("::xteleto - Tele to a player", 8154);
			c.getActionAssistant().sendFrame126("::xteletome - Tele a player to you", 8155);
			c.getActionAssistant().sendFrame126("::bank - Bank anywhere", 8156);
			c.getActionAssistant().sendFrame126("::spec - 5000 specs", 8157);
			c.getActionAssistant().sendFrame126("::mypos - Cord. position", 8158);
			c.getActionAssistant().sendFrame126("::kbd - Tele to King Black Drag", 8159);
			c.getActionAssistant().sendFrame126("::news - Server Wide Messages", 8160);
			c.getActionAssistant().sendFrame126("::kick - Kick A Player", 8161);
			c.getActionAssistant().sendFrame126("::tele - Tele to Cord.", 8162);
			c.getActionAssistant().sendFrame126("::ban - Ban A Player", 8163);
			c.getActionAssistant().sendFrame126("::ipban - Ip Ban", 8164);
			c.getActionAssistant().sendFrame126("::mute - Mute a player", 8165);
			c.getActionAssistant().sendFrame126("::jail - Jail A Player ::jail name", 8166);
			c.getActionAssistant().sendFrame126("::unjail - UnJail A Player", 8167);
			c.getActionAssistant().sendFrame126("::ipmute - Mute an Ip", 8168);
			c.getActionAssistant().sendFrame126("::eb - Turns easy ban on and off", 8169);
			c.getActionAssistant().showInterface(8134);
			c.flushOutStream();
		}
	}
}