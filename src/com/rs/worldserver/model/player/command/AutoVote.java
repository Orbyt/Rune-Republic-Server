package com.rs.worldserver.model.player.command;

//import org.runetoplist.VoteReward;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Command;
import com.rs.worldserver.Server;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.*;
import com.rs.worldserver.world.*;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.Misc;

public class AutoVote implements Command {

	public void execute(Client client, String command) {
			String[] name = {"Orbit", "James", "Jake", "Bob", "Bruce", "Carter", "Colby", "Effekt", "Lol1", "Viny", "Furkan", 
							"Leejm", "Papi", "Rager", "Timmy", "Falcon", "Nicholas", "Master8", "Vagina", "Cock", "Cameltoe",
							"Dingleberries", "Handsome", "Fugly", "Yummy", "Too strong", "Wowinator", "Alexis", "Jamesc",
							"Melody", "Ahirek", "Cartil", "Megixc2", "Leminx", "Turkenx", "Eldk4", "xxx9088x", "Manemdc",
							"Elifjm", "Cantakethis", "Bringiton1", "Toolatex0", "Timetopk", "Donttouchme", "Clectric", "Crazyxe", "Lemonade",
							"Nowgood92", "Thommy", "Fightpitking", "Drayzor", "Melons", "Xg0dko", "Mealepix", "Lurersx", "Timetowin",
							"Win all day", "Bobby", "Sexiposi", "Munchi", "Lemonjuice", "Nowdie", "U8toL8", "Nice safe", "pkergoddes",
							"xdarkboxtwin", "Trexor", "Realstake", "Stakegod", "Winner", "Actualgoose", "Premiumx", "Primalfi3", "Leium",
							"Excitez", "Retaukf", "Canfnow", "Relmiut", "Tumorix", "X o x o", "Retkikd", "Mentake", "Ownagex",
							"Bring it on1", "Xmentik", "Remultos", "Rammernaut", "Sea troll", "Zombiefight", "Pk me now", "Vls prod", "Primal pro",
							"Unbelievable", "Timetostake", "Win247", "Wannafight", "Rapiermaniac", "Delitime", "Retardation", "Wowfactor", "Touchme",
							"Elixcer", "Spitmy1", "Letsmert", "Getaknow", "Letiuxs", "Menfeish", "Realobse", "Faints", "Paralyze"};
			int random = Misc.random(2);
			if (random == 0) {
				PlayerManager.getSingleton().sendGlobalMessage("[@blu@Server Notice@bla@]@blu@ " + name[Misc.random(67)] + " @bla@has voted for 1 x Boss Tele Necklace. [@dre@::vote@bla@]");
			} else if (random == 0) {
				PlayerManager.getSingleton().sendGlobalMessage("[@blu@Server Notice@bla@]@blu@ " + name[Misc.random(67)] + " @bla@has voted for 1 x @dre@Set Of Moderator Commands! [@dre@::vote@bla@]");
			} else if (random == 0) {
				PlayerManager.getSingleton().sendGlobalMessage("[@blu@Server Notice@bla@]@blu@ " + name[Misc.random(67)] + " @bla@has voted for 3 x Mystery Box & " + Misc.random(1000) + " PkP. [@dre@::vote@bla@]");
			}
	}
}
