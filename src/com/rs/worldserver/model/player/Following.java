package com.rs.worldserver.model.player;

import java.util.HashMap;
import java.util.Map;

import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.Config;
import com.rs.worldserver.content.skill.SkillConstants;
import com.rs.worldserver.events.Event;
import com.rs.worldserver.events.EventContainer;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.FloorItem;
import com.rs.worldserver.model.ItemDefinition;
import com.rs.worldserver.model.npc.NPC;
import com.rs.worldserver.model.npc.NPCDefinition;
import com.rs.worldserver.world.AnimationManager;
import com.rs.worldserver.world.StillGraphicsManager;
import com.rs.worldserver.world.PlayerManager;
import com.rs.worldserver.util.*;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.content.skill.Magic;
import com.rs.worldserver.model.player.packet.Attack;


public class Following {

        /**
         * The ID of the Entity to follow.
         */
        private int followPlayerIndex;

        /**
         * The ID of the Entity to follow.
         */
        private int followId2;

        /**
         * The distance to follow the Entity at.
         */
        private int followDistance;

		private Client client;

		public Following(Client c) {
			this.client = c;
		}
        /**
         * Sets the following player.
         * @param followPlayerIndex The index of the Player to follow.
         */
        public void setFollowPlayerIndex(int followId) {
                this.followPlayerIndex = followId;
        }

        /**
         * Sets the following NPC.
         * @param followPlayerIndex The index of the NPC to follow.
         */
        public void setfollowId2(int followId2) {
                this.followId2 = followId2;
        }

        /**
         * Sets the distance of the following.
         * @param followDistance The distance to follow the Entity at.
         */
        public void setFollowDistance(int followDistance) {
                this.followDistance = followDistance;
        }

        /**
         * Gets the following player's index.
         * @return The followPlayerIndex variable.
         */
        public int getFollowPlayerIndex() {
                return followPlayerIndex;
        }

         /**
         * Gets the follow npc's index.
         * @return The followId2 variable.
         */
        public int getfollowId2() {
                return followId2;
        }

        /**
         * Gets the following distance.
         * @return The followDistance variable.
         */
        public int getFollowDistance() {
                return followDistance;
        }

        /**
         * Gets the player.
         * @return The player object.
         */
        private Player getPlayer() {
                return client;
        }

        /**
         * Resets the following for the player.
         */
        public void resetFollowing() {
            setfollowId2(-1);
            setFollowPlayerIndex(-1);
            setFollowDistance(0);
            getPlayer().faceUpdate(-1);
            client.getActionAssistant().setFollowing(0, -1, 0);
			client.followId = 0;
			client.isFollowingTest = false;
        }

        /**
         * Follows another Entity.
         * @param followPlayerIndex The ID of the Entity to follow.
         * @param followType The type of Entity (NPC: 0, Player: 1).
         * @param followDistance The distance to follow the NPC at.
         */
 public void followEntity(int followId, int followType, int followDistance) {
			client.followId = -1;
			client.followId = followId;
			client.isFollowingTest = true;
			//client.resetWalkingQueue();
	//client.startFollowing();
	
			//client.getActionAssistant().setFollowing(followId, followType, followDistance);
			if (followType == 0) {
					setFollowPlayerIndex(-1);
					setfollowId2(followId);
				   getPlayer().faceUpdate(followId);
					
			} else if (followType == 1) {
					setfollowId2(-1);
					setFollowPlayerIndex(followId);
				   getPlayer().faceUpdate(32768 + followId);
			}
			setFollowDistance(followDistance);
        }

        /**
         * Resets the following if necessary.
         */
    public void checkFollowing() {
		if (getFollowPlayerIndex() > 0) {
                       getPlayer().faceUpdate(32768 + getFollowPlayerIndex());
			Player followPlayer = PlayerManager.getSingleton().getPlayers()[getFollowPlayerIndex()];
			if (followPlayer == null) {
                                resetFollowing();
                                return;
                        }
			if (followPlayer.isDead) {
				resetFollowing();
				return;
			}
			if (!getPlayer().withinDistance(followPlayer.getX(), followPlayer.getY(), getPlayer().getX(), getPlayer().getY(), 12)) {
				resetFollowing();
				return;
			}
		} else if (getfollowId2() > 0) {
                       getPlayer().faceUpdate(getfollowId2());
			NPC npc = Server.getNpcManager().getNPC(getfollowId2());
			if (npc == null) {
                                resetFollowing();
                                return;
                        }
			if (npc.isDead()) {
				resetFollowing();
				return;
			}
			if (!getPlayer().withinDistance(npc.getX(), npc.getX(), getPlayer().getX(), getPlayer().getY(), 12)) {
				resetFollowing();
				return;
			}
		}
	}

}
