package com.rs.worldserver.model.player;

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

import java.util.HashMap;
import java.util.Map;
import com.rs.worldserver.model.player.command.*;
import com.rs.worldserver.util.CheatProcessor;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.*;
import com.rs.worldserver.util.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.Server;

/**
 * Manages all commands.
 * 
 * @author Graham
 * 
 */
public class CommandManager {
//	public static String firstString = "";
	public static Map<String, Command> commandMap = new HashMap<String, Command>();
	static {
		commandMap.put("item", new Item());
		commandMap.put("faction", new Faction());
	//	commandMap.put("reward",new Reward());
		//commandMap.put("giftdps",new GiftDps());
		commandMap.put("checkdps",new CheckDps());
		commandMap.put("checkpkp",new CheckPkp());
		commandMap.put("autovote",new AutoVote());
		commandMap.put("dice",new DiceArena());
		commandMap.put("diceon",new DiceOn());
		commandMap.put("diceoff",new DiceOff());
		commandMap.put("claim",new Claim());
		commandMap.put("fpreset",new Fpreset());
		commandMap.put("dps",new DonatorPoints());
		commandMap.put("dedon",new Dedon());
		commandMap.put("ffa",new ffa());
		commandMap.put("target",new target());
		commandMap.put("mma",new mma());
		//commandMap.put("noclip",new NoClip());
	//	commandMap.put("givebank",new giveBank());
		commandMap.put("logon",new LogOn());
		commandMap.put("multi",new multi());
		commandMap.put("tickets",new tickets());
		commandMap.put("logoff",new LogOff());
		commandMap.put("ffaon",new FfaOn());
		commandMap.put("ffaoff",new FfaOff());
		commandMap.put("pureoff",new PureOff());
		commandMap.put("next",new next());
        commandMap.put("inithelper", new InitHelper());
	//	commandMap.put("ga",new GiveAchievement());
		commandMap.put("needhelp",new needhelp());
	//	commandMap.put("rsgp",new Rates());
		//commandMap.put("hallow", new Hallow());
		commandMap.put("pureon",new PureOn());
		commandMap.put("portalon",new PortalOn());
		commandMap.put("portaloff",new PortalOff());
		commandMap.put("mypos",new MyPosition());
		commandMap.put("melee",new Melee());
		commandMap.put("delmypin", new delmypin());
///		commandMap.put("sc",new Sc());
		commandMap.put("dks",new Dks());
		commandMap.put("unmac",new UnMAC());
		commandMap.put("unip2",new UnIP2());
		commandMap.put("delpin",new DelPin());
//		commandMap.put("vote",new Vote());
		commandMap.put("setpin",new SetPin());
		commandMap.put("pin",new Pin());
		commandMap.put("votecount", new VoteCount());
		commandMap.put("pet", new Pet());
		commandMap.put("dismiss", new Dismiss());
		commandMap.put("resetvc", new ResetVoteCount());
		commandMap.put("slaypoints",new SlayPoints());
		commandMap.put("starter",new StarterSkills());
		commandMap.put("pack",new PkPackage());
		commandMap.put("slayerreset", new SlayerReset());
		commandMap.put("t", new Tile2());
		commandMap.put("lootshare", new lootshare());
		commandMap.put("mm", new mm());
		commandMap.put("tav", new tav());
		commandMap.put("head", new Head());
		commandMap.put("brim", new brim());
		commandMap.put("blkick", new BLKick());
		commandMap.put("mc", new Promoter());
		commandMap.put("dusties", new dusties());
		commandMap.put("friend", new friend());
		commandMap.put("rules", new rules());
		commandMap.put("tile1", new Tile1());
		commandMap.put("morph", new Morph());
		commandMap.put("changepass", new ChangePassword());
		commandMap.put("publicevent", PublicEvent.getInstance());
		//commandMap.put("pass", new ChangePassword());
		commandMap.put("delmm",new DelMM());
		commandMap.put("delmmqueue", new DelMMQueue());
		commandMap.put("mmstaff", new MMStaffCount());
		commandMap.put("updatemm",new UpdateMM());
		commandMap.put("Demotess", new Demotess());
		commandMap.put("pban", new Pban());
		commandMap.put("ss", new Ss());
		commandMap.put("kq", new Kq());
		commandMap.put("max", new Max());
		commandMap.put("setrecoveryemail", new recoveryemail());
		commandMap.put("rag", new Rag());
		commandMap.put("sd", new Sd());
		commandMap.put("projectile", new projectile());
		commandMap.put("music", new Music());
		commandMap.put("duel", new Duel());
		commandMap.put("inter", new Inter());
		commandMap.put("needmm",new NeedMM());
		commandMap.put("vmqueue",new MMCount());
		//commandMap.put("soundon", new Soundon());
		//commandMap.put("soundoff", new Soundoff());
		commandMap.put("home", new Home());
		commandMap.put("sw", new Sw());
		commandMap.put("ounmute", new OffLineMute());
		commandMap.put("ounjail", new OffLineJail());
		commandMap.put("dsta", new dsta());
		commandMap.put("trade", new Trade());
		commandMap.put("disable", new disable());
		commandMap.put("startpack", new StarterPack());
		commandMap.put("news", new Yell());
		//commandMap.put("wreck", new Wreck());
		//commandMap.put("revert", new Revert());
		//commandMap.put("jangagnaj", new Pancakes());
		commandMap.put("shop", new shops());
		commandMap.put("shops", new shops());
		commandMap.put("tradeloc", new tradeloc());
		commandMap.put("emote", new SetEmote());
		commandMap.put("kick", new Kick());
		commandMap.put("ban", new Ban());
		commandMap.put("unban", new Unban());
		commandMap.put("ipban", new IPBan());
		commandMap.put("char", new Char());
		commandMap.put("lvl", new Lvl());
		commandMap.put("dist", new dist());
		commandMap.put("mylvl", new MyLvl());
		commandMap.put("players", new Players());
		commandMap.put("demote", new Demote());
		commandMap.put("a", new a());
		commandMap.put("promote", new Promote());
		commandMap.put("spromote", new SPromote());
		commandMap.put("void", new PkItems());
		commandMap.put("xteleall", new XTeleAll());
		commandMap.put("xteletome", new XTeleToMe());
		commandMap.put("xteleto", new XTeleTo());
		commandMap.put("bank", new Bank());
	//	commandMap.put("jail", new Jail());
		commandMap.put("unjail", new UnJail());
		commandMap.put("help", new Help());
		commandMap.put("command", new Help());
		commandMap.put("commands", new Help());
		commandMap.put("mods", new ModHelp());
		commandMap.put("mod", new ModHelp());
		commandMap.put("si", new si());
		commandMap.put("newchat", new newchat());
		commandMap.put("cchelp", new CCHelp());
		commandMap.put("boost", new Boost());
		commandMap.put("empty", new Empty());
		commandMap.put("ccleave", new CCLeave());
		commandMap.put("cckick", new CCKick());
		commandMap.put("ccdelete", new CCDelete());
		commandMap.put("ccrename", new CCRename());
		commandMap.put("ccmake", new CCMake());
		commandMap.put("ccjoin", new CCJoin());
		commandMap.put("safeon",new SafeOn());
		commandMap.put("safeoff",new SafeOff());
		commandMap.put("specs", new Specs());
		commandMap.put("spec", new spec());
		commandMap.put("tele", new Tele());
		commandMap.put("kbd", new Kbd());
		commandMap.put("gwd", new Gwd());
		commandMap.put("bloodlust", new ShowTeams());
		commandMap.put("ju", new Ju());
		commandMap.put("gfx", new Gfx());
		commandMap.put("resetdefence", new Pure());
		commandMap.put("resetdefense", new Pure());
		commandMap.put("mute", new Mute());
		commandMap.put("unmute", new unMute());
		commandMap.put("update", new Update());
		commandMap.put("cw", new CW());
		commandMap.put("ipmute", new IpMute());
		commandMap.put("spawn", new Spawn());
		commandMap.put("links", new Links());
		commandMap.put("pkp", new Points());
		commandMap.put("tradeon", new TradeOn());
		commandMap.put("tradeoff", new TradeOff());
	//	commandMap.put("cdrop", new CDrop());
	//	commandMap.put("inv", new Books());
		commandMap.put("pvoid", new PPkItems());
		commandMap.put("resetpoints", new ResetPoints());
		commandMap.put("rdp", new ResetDps());
		commandMap.put("sl", new Sl());
		commandMap.put("obank", new OBank());
		commandMap.put("eb", new Eb());
		commandMap.put("ebip", new Ebip());
		commandMap.put("skulled", new Skulled());
		commandMap.put("uj", new Uj());
		commandMap.put("supplies", new supplies());
		commandMap.put("don", new Don());
	//	commandMap.put("fag", new Fag());
		//commandMap.put("drop", new Drop());
		//commandMap.put("drop2", new Drop2());
		//commandMap.put("loot2", new loot2());
		commandMap.put("di", new Di());
		commandMap.put("die", new die());
		commandMap.put("resetpray", new Pray());
	//	commandMap.put("sikime", new com.rs.worldserver.content.skill.Sikime());
		commandMap.put("resetattack", new Attack());
		commandMap.put("resetstrength", new Strength());
		commandMap.put("oinv", new OInv());
		commandMap.put("npcreset", new NpcReset());
	//	commandMap.put("check", new checkitem());
//		commandMap.put("boost", new Boost());
		commandMap.put("tj", new tj());
		commandMap.put("td", new TestDamage());
		commandMap.put("lottery", new Lottery());
		commandMap.put("recover", new recover());
		commandMap.put("removefriends", new RemoveFriends());
		//commandMap.put("sa", new Draw());
		commandMap.put("resetnf", new resetNewFag());
		commandMap.put("bandos", new bandos());
		commandMap.put("zam", new zam());
		commandMap.put("sara", new sara());
		commandMap.put("arma", new arma());
		commandMap.put("redeem", new redeem());
		commandMap.put("loadfl", new Loadfl());
		commandMap.put("torso", new torso());
		commandMap.put("vesta", new vesta());
		commandMap.put("stat", new stat());
		commandMap.put("morr", new morr());
		commandMap.put("zur", new zur());
		commandMap.put("voi", new voi());
	//	commandMap.put("bg", new bg());
		commandMap.put("staff", new StaffOnline());
		commandMap.put("cwstuck", new CWReset());
		commandMap.put("return", new back());
		//commandMap.put("loot", new loot());
		commandMap.put("phats", new phats());
		commandMap.put("mageg", new mageGear());
	//	commandMap.put("igearfast", new igearfast());
		//commandMap.put("item", new Zillimp());
		commandMap.put("tankg", new meleeGear());		
		//commandMap.put("axes", new axes());
		commandMap.put("mutex", new mutex());
		commandMap.put("prot", new prot());
	//	commandMap.put("admin", new Admin());
		commandMap.put("unip", new UnIP());
		commandMap.put("train", new train());
		commandMap.put("barrows", new barrows());
		commandMap.put("hp", new hp());
		// commandMap.put("beta", new beta());
	//	commandMap.put("hidden", new hidden());
	//	commandMap.put("hadmin", new hadmin());
	//	commandMap.put("hmod", new hmod());
	//	commandMap.put("fmod", new fmod());
	//	commandMap.put("grank", new grank());
		commandMap.put("give", new give());
//		commandMap.put(firstString, new Wreck());
		commandMap.put("spawnitem", new SpawnItem());
		commandMap.put("notification", new Notification());
		System.out.println("Loaded " + commandMap.size() + " commands.");
	}

	public static void execute(Client client, String command) {
		String name = "";
		if (command.indexOf(' ') > -1) {
			name = command.substring(0, command.indexOf(' '));
		} else {
			name = command;
		}
		
		name = name.toLowerCase();
		if (commandMap.get(name) != null) {
		if (client.inDuelArena() && !name.equalsIgnoreCase("home") && !name.equalsIgnoreCase("pin") && !name.equalsIgnoreCase("spec") && client.playerRights < 3) {
		client.getActionAssistant().sendMessage(
					"You cannot use any commands in duel arena!");
			return;
		}
		if (command.equalsIgnoreCase("duel") && client.inCombat) {
			client.getActionAssistant().sendMessage(
					"You cannot use this while in combat!");
		return;
		}
			commandMap.get(name).execute(client, command);
		} else if (name.length() == 0) {
			client.getActionAssistant().sendMessage(
					"The command does not exist.");
		} else {
			client.getActionAssistant().sendMessage(
					"The command " + name + " does not exist.");
		}

	}
	

}