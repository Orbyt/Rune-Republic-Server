package com.rs.worldserver.model.player;

import com.rs.worldserver.world.PlayerManager;


public class FollowHandler {

	// TODO: Lowercase the current package(util/misc)(Java conventions).

	private final Client client;

	private final int[] followX = new int[64], followY = new int[64];
	private int followLastX = 1, followLastY = 1, followingStepsWalked = 1,
			followIndex = 1;

	/**
	 * Constructs new instance with a provided client(player).
	 * 
	 * @param client
	 *            The <code>client</code> instance.
	 */
	public FollowHandler(final Client client) {
		this.client = client;
	}

	/**
	 * Reset the following.
	 */
	public void resetFollowing() {
		followingStepsWalked = 1;
		client.followId = -1;
		followIndex = 1;
		followLastX = 1;
		followLastY = 1;
	}

	/**
	 * Follows the provided player.
	 * 
	 * @param follow
	 *            The player to follow.
	 */
	public void follow(final int follow) {
		int absX = client.absX;
		int absY = client.absY;
		client.followId = follow;
		Player Players[] = PlayerManager.getSingleton().getPlayers();

		if (absX == followX[followingStepsWalked]
				&& followY[followingStepsWalked] == absY
				&& followX[followingStepsWalked + 1] != 0) {
			followingStepsWalked++;
		}
		if (client.followId != -1 && followIndex > 0
				&& client.freezeTimer > 0 && followIndex > 999999) {
			resetFollowing();
		}
		if (client.withinDistance(Players[client.followId].absX,
				Players[client.followId].absY, absX, absY, 61)) {
			followX[followIndex] = Players[client.followId].absX;
			followY[followIndex] = Players[client.followId].absY;
		}
		if (client.withinDistance(followLastX, followLastY,
				followX[followIndex], followY[followIndex], 1)
				&& followIndex > 0) {
			followIndex--;
		}
		if (client.withinDistance(absX, absY,
				Players[client.followId].absX,
				Players[client.followId].absY, 1)) {
			followX[1] = Players[client.followId].absX;
			followY[1] = Players[client.followId].absY;
			followIndex = 1;
			followingStepsWalked = 1;
		}
		if (followX[followIndex] != 0
				&& followY[followIndex] != 0
				&& !client.withinDistance(absX, absY, followX[followIndex],
						followY[followIndex], 1)
				&& followX[followingStepsWalked] != 0
				&& followY[followingStepsWalked] != 0) {
			client.getActionAssistant().walkTo(client.getActionAssistant().getMove(absX,
					followX[followingStepsWalked]), client.getActionAssistant().getMove(absY,
					followY[followingStepsWalked]));
			//client.faceNPC = 32768 + client.followId;
			client.faceUpdate(client.followId+32768);
			followLastX = followX[followIndex];
			followLastY = followY[followIndex];
			followIndex++;
		} else {
			resetFollowing();
		}
	}

}