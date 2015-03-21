package com.rs.worldserver.model.player.command;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.util.*;
import com.rs.worldserver.model.player.ActionAssistant;

public class redeem implements Command {

	@Override
	public void execute(Client c, String command) {

		if (c.getActionAssistant().playerHasItem(1796,1)) {
			c.getActionAssistant().deleteItem(1796, c.getActionAssistant().getItemSlot(1796), 1);
			c.getActionAssistant().addItem(6570,1);
		}
		if (c.getActionAssistant().playerHasItem(1797,1)) {
			c.getActionAssistant().deleteItem(1797, c.getActionAssistant().getItemSlot(1797), 1);
			c.getActionAssistant().addItem(11663,1);
		}
		if (c.getActionAssistant().playerHasItem(1798,1)) {
			c.getActionAssistant().deleteItem(1798, c.getActionAssistant().getItemSlot(1798), 1);
			c.getActionAssistant().addItem(8839,1);
		}
		if (c.getActionAssistant().playerHasItem(1799,1)) {
			c.getActionAssistant().deleteItem(1799, c.getActionAssistant().getItemSlot(1799), 1);
			c.getActionAssistant().addItem(8840,1);
		}
		if (c.getActionAssistant().playerHasItem(1800,1)) {
			c.getActionAssistant().deleteItem(1800, c.getActionAssistant().getItemSlot(1800), 1);
			c.getActionAssistant().addItem(11664,1);
		}
		if (c.getActionAssistant().playerHasItem(1801,1)) {
			c.getActionAssistant().deleteItem(1801, c.getActionAssistant().getItemSlot(1801), 1);
			c.getActionAssistant().addItem(11665,1);
		}
		if (c.getActionAssistant().playerHasItem(1802,1)) {
			c.getActionAssistant().deleteItem(1802, c.getActionAssistant().getItemSlot(1802), 1);
			c.getActionAssistant().addItem(8850,1);
		}
		if (c.getActionAssistant().playerHasItem(1803,1)) {
			c.getActionAssistant().deleteItem(1803, c.getActionAssistant().getItemSlot(1803), 1);
			c.getActionAssistant().addItem(10551,1);
		}
		if (c.getActionAssistant().playerHasItem(1804,1)) {
			c.getActionAssistant().deleteItem(1804, c.getActionAssistant().getItemSlot(1804), 1);
			c.getActionAssistant().addItem(13866,1);
			c.zurhelm = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1805,1)) {
			c.getActionAssistant().deleteItem(1805, c.getActionAssistant().getItemSlot(1805), 1);
			c.getActionAssistant().addItem(13901,1);
			c.vlsdmg = 40000;
		}
		if (c.getActionAssistant().playerHasItem(1806,1)) {
			c.getActionAssistant().deleteItem(1806, c.getActionAssistant().getItemSlot(1806), 1);
			c.getActionAssistant().addItem(13889,1);
			c.vplate = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1807,1)) {
			c.getActionAssistant().deleteItem(1807, c.getActionAssistant().getItemSlot(1807), 1);
			c.getActionAssistant().addItem(13895,1);
			c.vleg = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1808,1)) {
			c.getActionAssistant().deleteItem(1808, c.getActionAssistant().getItemSlot(1808), 1);
			c.getActionAssistant().addItem(13907,1);
			c.vsdmg = 40000;
		}
		if (c.getActionAssistant().playerHasItem(1809,1)) {
			c.getActionAssistant().deleteItem(1809, c.getActionAssistant().getItemSlot(1809), 1);
			c.getActionAssistant().addItem(13863,1);
			c.zleg = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1810,1)) {
			c.getActionAssistant().deleteItem(1810, c.getActionAssistant().getItemSlot(1810), 1);
			c.getActionAssistant().addItem(13860,1);
			c.zplate = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1811,1)) {
			c.getActionAssistant().deleteItem(1811, c.getActionAssistant().getItemSlot(1811), 1);
			c.getActionAssistant().addItem(13872,1);
			c.mplate = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1812,1)) {
			c.getActionAssistant().deleteItem(1812, c.getActionAssistant().getItemSlot(1812), 1);
			c.getActionAssistant().addItem(13875,1);
			c.mleg = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1813,1)) {
			c.getActionAssistant().deleteItem(1813, c.getActionAssistant().getItemSlot(1813), 1);
			c.getActionAssistant().addItem(13878,1);
			c.morrhelm = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1814,1)) {
			c.getActionAssistant().deleteItem(1814, c.getActionAssistant().getItemSlot(1814), 1);
			c.getActionAssistant().addItem(13886,1);
			c.splate = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1815,1)) {
			c.getActionAssistant().deleteItem(1815, c.getActionAssistant().getItemSlot(1815), 1);
			c.getActionAssistant().addItem(13892,1);
			c.sleg = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1816,1)) {
			c.getActionAssistant().deleteItem(1816, c.getActionAssistant().getItemSlot(1816), 1);
			c.getActionAssistant().addItem(13898,1);
			c.stathelm = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1817,1)) {
			c.getActionAssistant().deleteItem(1817, c.getActionAssistant().getItemSlot(1817), 1);
			c.getActionAssistant().addItem(13904,1);
			c.statdmg = 60000;
		}
		if (c.getActionAssistant().playerHasItem(1818,1)) {
			c.getActionAssistant().deleteItem(1818, c.getActionAssistant().getItemSlot(1818), 1);
			c.getActionAssistant().addItem(8842,1);
		}
		if (c.getActionAssistant().playerHasItem(1819,1)) {
			c.getActionAssistant().deleteItem(1819, c.getActionAssistant().getItemSlot(1819), 1);
			c.getActionAssistant().addItem(7462,1);
		}
		if (c.getActionAssistant().playerHasItem(1820,1)) {
			c.getActionAssistant().deleteItem(1820, c.getActionAssistant().getItemSlot(1820), 1);
			c.getActionAssistant().addItem(1038,1);
		}
		if (c.getActionAssistant().playerHasItem(1821,1)) {
			c.getActionAssistant().deleteItem(1821, c.getActionAssistant().getItemSlot(1821), 1);
			c.getActionAssistant().addItem(1042,1);
		}
		if (c.getActionAssistant().playerHasItem(1822,1)) {
			c.getActionAssistant().deleteItem(1822, c.getActionAssistant().getItemSlot(1822), 1);
			c.getActionAssistant().addItem(1040,1);
		}
		if (c.getActionAssistant().playerHasItem(1823,1)) {
			c.getActionAssistant().deleteItem(1823, c.getActionAssistant().getItemSlot(1823), 1);
			c.getActionAssistant().addItem(1044,1);
		}
		if (c.getActionAssistant().playerHasItem(1843,1)) {
			c.getActionAssistant().deleteItem(1843, c.getActionAssistant().getItemSlot(1843), 1);
			c.getActionAssistant().addItem(1046,1);
		}
		if (c.getActionAssistant().playerHasItem(1852,1)) {
			c.getActionAssistant().deleteItem(1852, c.getActionAssistant().getItemSlot(1852), 1);
			c.getActionAssistant().addItem(1048,1);
		}
	}
}
