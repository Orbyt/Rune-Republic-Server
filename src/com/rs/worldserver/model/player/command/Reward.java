//package com.rs.worldserver.model.player.command;
//
////import org.runetoplist.VoteReward;
//import com.rs.worldserver.model.player.Client;
//import com.rs.worldserver.model.player.Command;
//import com.rs.worldserver.Server;
//import com.rs.worldserver.world.PlayerManager;
//import com.rs.worldserver.*;
//import com.rs.worldserver.world.*;
//import com.rs.worldserver.model.player.*;
//import com.rs.worldserver.util.Misc;
//
//public class Reward implements Command {
//
//	public void execute(Client client, String command) {
//        try {
//            VoteReward reward = Server.voteChecker.getReward(client.playerName.replaceAll(" ", "_"));
//            if(reward != null){
//                switch(reward.getReward()){
//                    case 0:
//                        client.getActionAssistant().sendMessage("Thanks for voting!");
//                        client.getActionAssistant().addItem(995, 5000000);
//                        break;
//                    default:
//                        client.getActionAssistant().sendMessage("Reward not found.");
//                        break;
//                }
//                client.getActionAssistant().sendMessage("Thank you for voting.");
//            } else {
//                client.getActionAssistant().sendMessage("You have no items waiting for you.");
//            }
//        } catch (Exception e){
//            client.getActionAssistant().sendMessage("An error occurred please try again later.");
//        }
//	}
//
//}
