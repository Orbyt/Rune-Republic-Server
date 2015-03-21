package com.rs.worldserver.model.npc;

import java.util.Hashtable;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.model.player.ActionAssistant;
import com.rs.worldserver.model.player.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.util.Map;
import java.util.Set;
import com.rs.worldserver.util.*;
/** 
@author Orbit
*/
public class NPCKiller implements Comparable<Object>{
 
    public int playerId;
    public int hits;
	
	  public NPCKiller(int PlayerId) {
        playerId = PlayerId;
        hits = 0;

    }

	
	public static void addHits(NPC p1, Client p2, int Hits) {
        NPCKiller tempNPCKiller = new NPCKiller(p2.playerId);
        for (NPCKiller pk : p1.killer) {
            if (pk.playerId == tempNPCKiller.playerId) {
                pk.hits += Hits;
                return;
            }
        }
        tempNPCKiller.hits = Hits;
        p1.killer.add(tempNPCKiller);
		
    }
	
	 public static List<Integer> getKillers(NPC p){
		List<NPCKiller> temp = p.killer;
		 Comparator comparator = Collections.reverseOrder();
		Collections.sort(temp,comparator);
		List<Integer> id = new ArrayList<Integer>();
		for( int i = 0; i < p.killer.size(); i++){
			id.add(p.killer.get(i).playerId);
			if(i == 3)
				break;
		}
		return id;
	 }
	 public static int getKiller(NPC p) {
	 //make npckiller lootshare list
		boolean Lootshare = false;
        int PlayerId = 0;
        int maxHits = 0;
		HashMap<Client, Integer> lootShare = new HashMap<Client, Integer>();
		
		for (NPCKiller P : p.killer) {
            if (P.hits > maxHits) {
                maxHits = P.hits;
                PlayerId = P.playerId;
            }
        }
		
        for (NPCKiller P : p.killer) {
			Player d = PlayerManager.getSingleton().getPlayers()[P.playerId];
			if(d != null) 
			{
				Client person = (Client) d;
				if (person.Lootshare && !person.inCWar())  {
					lootShare.put(person, person.playerId);
				}
			}
            if (P.hits > maxHits) {
                maxHits = P.hits;
                PlayerId = P.playerId;
			}
        }
		
		
			Client x = (Client) PlayerManager.getSingleton().getPlayers()[PlayerId];
			if(x == null){
				return PlayerId;
			}
			if(x.Lootshare && !x.inCWar()){
				Iterator iterator = lootShare.keySet().iterator();
				ArrayList<Integer> random = new ArrayList(); 
				while (iterator.hasNext()) {
					Client c = (Client) iterator.next();
					int playerId = lootShare.get(c);
					if (c == null) {
						continue;
					}
					random.add(playerId);
				}
				
				//System.out.println(random.size());
				int randNum = (Misc.random(random.size() - 1));
				//System.out.println(randNum);
				PlayerId = random.get(randNum);
				Client newLoot = (Client) PlayerManager.getSingleton().getPlayers()[PlayerId];
				iterator = lootShare.keySet().iterator();
				while (iterator.hasNext()) {
					Client ce = (Client) iterator.next();
					if (ce == null) {
						continue;
					}
					newLoot.lootShareDistrib.put(ce, ce.playerId);
				}
				random.clear();
			}
		
		lootShare.clear();
        p.killer.clear();
        return PlayerId;

}
   public int compareTo(Object anotherTeam) throws ClassCastException {
		    if (!(anotherTeam instanceof NPCKiller))
		      throw new ClassCastException("A Team object expected.");
		    int anotherTeamsRating = ((NPCKiller) anotherTeam).hits;  
		    return this.hits - anotherTeamsRating;    
	 }
}
