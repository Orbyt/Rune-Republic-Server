package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.content.skill.Magic;

public class Help implements Command {

	@Override
	public void execute(Client c, String command) {
	if (c.inDuelArena()) {
			c.getActionAssistant().Send("You cannot use this here.");
			return;
		}
			c.getActionAssistant().clearQuestInterface();
			c.getActionAssistant().sendFrame126("@dre@Rune Republic Information Menu", 8144); // Title
		//	c.getActionAssistant().sendFrame126("@dbl@Players Online: @gre@" + PlayerManager.getSingleton().getPlayerCount(),8146 );
			c.getActionAssistant().sendFrame126("::links - Shows you a variety of useful CLICKABLE links.", 8147);
			c.getActionAssistant().sendFrame126("::changepass NEWPASSWORD - changes your password", 8148);
			c.getActionAssistant().sendFrame126("::players - view everyone online", 8149);
			c.getActionAssistant().sendFrame126("::resetattack - Sets your attack to 1",8150);
			c.getActionAssistant().sendFrame126("::resetdefense X - (X = level you want)",8151);
			c.getActionAssistant().sendFrame126("::resetstrength - Sets your strength to 1",8152);
			c.getActionAssistant().sendFrame126("::resetpray - Sets your prayer to 1",8153);
			c.getActionAssistant().sendFrame126("::train - Teleport to training island",8154);
			c.getActionAssistant().sendFrame126("::barrows - Takes you to barrows",8155);
			c.getActionAssistant().sendFrame126("::empty - Delete your inventory PERMANENTLY", 8156);
			c.getActionAssistant().sendFrame126("::cchelp - Clan chat Help", 8157);
			c.getActionAssistant().sendFrame126("::skulled - skulls you/lasts 20mins", 8158);
			c.getActionAssistant().sendFrame126("Check your magic tab for teleports",8159);
			c.getActionAssistant().sendFrame126("::slayerreset Reset your slayer task - costs 3 slayer points",8160);
			c.getActionAssistant().sendFrame126("::rag checks your maximum range hit",8161);
			c.getActionAssistant().sendFrame126("::melee checks your maximum melee hit",8162);
			c.getActionAssistant().sendFrame126("::char - Change Your Look",8163);
			c.getActionAssistant().sendFrame126("::gwd - Teleport to Godwars Dungeon",8164);
			c.getActionAssistant().sendFrame126("::dusties - Teleport to Dust Devils",8165);
			c.getActionAssistant().sendFrame126("::tav - Teleport to Taverley Dungeon",8166);
			c.getActionAssistant().sendFrame126("::dks - Teleport to Dagganoth Kings (Donator only)",8167);
			c.getActionAssistant().sendFrame126("::brim - Teleport to Brimhaven Dungeon",8168);
			c.getActionAssistant().sendFrame126("::shops - Teleport to shops",8169);
			c.getActionAssistant().sendFrame126("::trade - Teleport to the market place",8170);
			c.getActionAssistant().sendFrame126("::duel - Teleport to Duel Arena",8171);
			c.getActionAssistant().sendFrame126("::di - Teleport to Donator Island (D-PIN)",8172);
			c.getActionAssistant().sendFrame126("::die - Teleport to Expansion Donator Island (DPIN+EXPANSION-PIN)",8173);
			c.getActionAssistant().sendFrame126("::pet # - Summons a pet with that ID",8174);
			c.getActionAssistant().sendFrame126("::dismiss - Dismisses your pet",8175);
			c.getActionAssistant().sendFrame126("::setpin - Sets a bank pin",8176);
			c.getActionAssistant().showInterface(8134);
			c.flushOutStream();
	
	}
}