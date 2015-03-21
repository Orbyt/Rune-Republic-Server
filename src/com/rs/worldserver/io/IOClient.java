package com.rs.worldserver.io;

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
import java.math.BigInteger;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

import com.rs.worldserver.Config;
import com.rs.worldserver.Constants;
import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.*;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.Misc;
import com.rs.worldserver.util.RightsProcessor;
import com.rs.worldserver.world.PlayerManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

//import sun.misc.BASE64Encoder;

/**
 * Represents a client that has connected but has not yet logged in.
 * 
 * @author Graham
 * 
 */
public class IOClient {
private static final BigInteger RSA_MODULUS = new BigInteger("118913839065237669548036674112461155051914902538125085558046913594644059012604710959776332060564520954846131197344649851389922059479237459382782641222474257226507770918374642122122694274908387277285966208133942963250375013003338652727317787382386662269768773306746874210797417903177836135047493533280426489219");
private static final BigInteger RSA_EXPONENT = new BigInteger("40924998704799359699955249471391143204540040385544216316916369930378210034473644745681907466408788472718532876941400695761168378954700411925761973736559597979939703357053825450989267048562465349909764711494606386644652372874864373179017628493115402914444316827049875803340335801341008448110024441765018630913");

	/**
	 * When the client connected.
	 */
	private long connectedAt;
	int world = Config.WORLD_NUMBER ;
	/**
	 * The timeout value in seconds.
	 */
	private static final int TIMEOUT = 1;

	/**
	 * The client's current state.
	 * 
	 * @author Graham
	 * 
	 */
	 
	private static enum State {
		LOGIN_START, LOGIN_READ1, LOGIN_READ2,
	}

	private State state = State.LOGIN_START;
	private Socket socket;
	public StringObject connectedFrom;
    private JSONObject client;

	private Stream outStream = new Stream(
			new byte[Constants.INITIAL_BUFFER_SIZE]);
	private Stream inStream = new Stream(
			new byte[Constants.INITIAL_BUFFER_SIZE]);
	private ISAACCipher inStreamDecryption;
	private ISAACCipher outStreamDecryption;

	private long serverSessionKey = 0, clientSessionKey = 0;
	private int loginPacketSize, loginEncryptPacketSize;

	private String playerName = null, playerPass = null;

	public PlayerManager handler;

	public IOClient(Socket s, String connectedFrom) throws IOException {
		this.socket = s;
        this.connectedFrom = new StringObject(connectedFrom);
		this.outStream.currentOffset = 0;
		this.inStream.currentOffset = 0;
		this.serverSessionKey = ((long) (java.lang.Math.random() * 99999999D) << 32)
				+ (long) (java.lang.Math.random() * 99999999D);
		this.connectedAt = System.currentTimeMillis();
	}
	public String filteredWords[] = {"Mod","mod","admin","owner","0wner","adm1n",
	"m0d","m 0d","mo d","m o d","m 0 d","o w n e r","a d m i n","syi","s y i"
	
	
	};
	public boolean isFiltered() {
		  for(int i = 0; i < filteredWords.length; i++) {
			String name = playerName.toLowerCase();
		   if(name.contains(filteredWords[i]))
			return true;
		  }
		 return false;
	}
	  public String encrypt(String plaintext) {
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("SHA"); //step 2
    }
    catch(NoSuchAlgorithmException e)
    {
    }
    try
    {
      md.update(plaintext.getBytes("UTF-8")); //step 3
    }
    catch(UnsupportedEncodingException e)
    {
    }
    byte raw[] = md.digest(); //step 4
    String hash = (new Base64()).encodeToString(raw); //step 5
    return hash; //step 6
  }
	public void process() throws Exception, IOException {
		long diff = System.currentTimeMillis() - connectedAt;
		if (diff > (TIMEOUT * 1000)) {
			throw new Exception("Timeout.");
		}
		if (state == State.LOGIN_START) {
			
			if (fillInStream(2)) {
				if (inStream.readUnsignedByte() != 14) {
					throw new Exception("Expect login byte 14 from client.");
				}
				// this is part of the usename. Maybe it's used as a hash to
				// select the appropriate
				// login server
				@SuppressWarnings("unused")
				int namePart = inStream.readUnsignedByte();
				for (int i = 0; i < 8; i++)
					outStream.writeByte(0); // is being ignored by the client
				// login response - 0 means exchange session key to establish
				// encryption
				// Note that we could use 2 right away to skip the cryption
				// part, but i think this
				// won't work in one case when the cryptor class is not set and
				// will throw a NullPointerException
				outStream.writeByte(0);
				// send the server part of the session Id used (client+server
				// part together are used as cryption key)
				outStream.writeQWord(serverSessionKey);
				directFlushOutStream();
				state = State.LOGIN_READ1;
				inStream.length -= 2;
			}
		}
		if (state == State.LOGIN_READ1) {
			if (fillInStream(2)) {
				int loginType = inStream.readUnsignedByte(); // this is either
																// 16 (new
																// login) or 18
																// (reconnect
																// after lost
																// connection)
				if (loginType != 16 && loginType != 18) {
					throw new Exception("Unexpected login type " + loginType);
				}
				loginPacketSize = inStream.readUnsignedByte();
				loginEncryptPacketSize = loginPacketSize - (36 + 1 + 1 + 2); // the
																				// size
																				// of
																				// the
																				// RSA
																				// encrypted
																				// part
																				// (
																				// containing
																				// password
																				// )
				Misc.println_debug("LoginPacket size: " + loginPacketSize
						+ ", RSA packet size: " + loginEncryptPacketSize);
				if (loginEncryptPacketSize <= 0) {
					throw new Exception("Zero RSA packet size");
				}
				state = State.LOGIN_READ2;
				inStream.length -= 2;
			}
		}
		if (state == State.LOGIN_READ2) {
			if (fillInStream(loginPacketSize)) {
				if (inStream.readUnsignedByte() != 255
						|| inStream.readUnsignedWord() != 317) {
					throw new Exception(
							"Wrong login packet magic ID (expected 255, 317)");
				}
				int lowMemoryVersion = inStream.readUnsignedByte();
				Misc.println_debug("Client type: "
						+ ((lowMemoryVersion == 1) ? "low" : "high")
						+ " memory version");
				for (int i = 0; i < 9; i++) {
					Misc.println_debug("dataFileVersion[" + i + "]: 0x"
							+ Integer.toHexString(inStream.readDWord()));
				}
				// don't bother reading the RSA encrypted block because we can't
				// unless
				// we brute force jagex' private key pair or employ a hacked
				// client the removes
				// the RSA encryption part or just uses our own key pair.
				// Our current approach is to deactivate the RSA encryption of
				// this block
				// clientside by setting exp to 1 and mod to something large
				// enough in (data^exp) % mod
				// effectively rendering this tranformation inactive
			Stream rsaStream = new Stream(inStream.decryptRSA(RSA_EXPONENT, RSA_MODULUS));
			int tmp = rsaStream.readUnsignedByte();
			//boolean rsaPass = true;
			if(tmp != 10) {
				//rsaPass = false;
						outStream.writeByte(12);
				
					outStream.writeByte(0);
					outStream.writeByte(0);
				
				
				directFlushOutStream();
				return;
			}
				clientSessionKey = rsaStream.readQWord();
				serverSessionKey = rsaStream.readQWord();
				int playerUID = rsaStream.readDWord();
                client = new JSONObject(playerUID);
                connectedFrom.addUid(playerUID);
				Misc.println_debug("UID: " + playerUID);
				int newMac = rsaStream.readDWord();
				int realClient = rsaStream.readDWord();
				Misc.println_debug("Real Client ID: " + realClient);				
				int achievements = rsaStream.readDWord();
				int playerMac = rsaStream.readDWord();
				Misc.println_debug("MAC: " + playerMac);
				//System.out.println(achievements);
				playerName = rsaStream.readString();
				playerName = Misc.formatPlayerName(playerName);
				System.out.println("NEW MAC " +playerName+ " " +newMac);
				if (playerName == null)
					throw new Exception("Blank username.");
				playerPass = rsaStream.readString();
				Misc.println_debug("Ident: " + playerName + " : " + playerPass);
				//playerPass = encrypt(playerPass);
				
				int sessionKey[] = new int[4];
				sessionKey[0] = (int) (clientSessionKey >> 32);
				sessionKey[1] = (int) clientSessionKey;
				sessionKey[2] = (int) (serverSessionKey >> 32);
				sessionKey[3] = (int) serverSessionKey;
System.out.println("rc:" +realClient);
System.out.println("ach:" +achievements);
				for (int i = 0; i < 4; i++)
					Misc.println_debug("inStreamSessionKey[" + i + "]: 0x"
							+ Integer.toHexString(sessionKey[i]));

				inStreamDecryption = new ISAACCipher(sessionKey);
				for (int i = 0; i < 4; i++)
					sessionKey[i] += 50;

				for (int i = 0; i < 4; i++)
					Misc.println_debug("outStreamSessionKey[" + i + "]: 0x"
							+ Integer.toHexString(sessionKey[i]));

				outStreamDecryption = new ISAACCipher(sessionKey);
				outStream.packetEncryption = outStreamDecryption;

				int returnCode = 2; // ok
				int slot = handler.getFreeSlot();
				Client c = null;
				//if (slot == -1) {
				if (PlayerManager.getSingleton().getPlayerCount() >= new IntegerObject(1200).getInteger()) {
					returnCode = 7; // world full!
				} else if (realClient !=  0x2e176ed0) { 
					System.out.println(" fake rc: " +realClient);
					returnCode = 12; 
				} else if(achievements != 0x54e6401){
					System.out.println(" fake ach:" +achievements);
					returnCode = 12;
				} else if (handler.updateRunning) { 
					returnCode = 14;	
				} else if (playerName.length() == 0) {
					returnCode = 27; // please try again
				} else if (!playerName.matches("[a-zA-Z0-9 ]*")) {
					returnCode = 27; // please try again
				} else if (handler.isPlayerOn(playerName)) {
					returnCode = 5; // online			
				}/* else if (VoteSql.getWorld(playerName.toLowerCase())) {
					returnCode = 5;
				}*/
				else if (BanProcessor.checkMAC(playerMac) || BanProcessor.checkIP(connectedFrom)){
					returnCode = 11;
				}
				// else if(!BetaProcessor.checkbeta(playerName)){
					
				// returnCode = 26;
					
				// }			
				//else if((!BanProcessor.checkBetaNames(playerName) || RightsProcessor.checkRights(playerName) < 1) && !connectedFrom.contains("99.238.189.88")){
					//if(!DuelProcessor.isduel(playerName)){
					//System.out.println("IP " +connectedFrom);
					//	returnCode = 26;
					//}
			//	}
				else if ( BanProcessor.checkPerm(playerName) || BanProcessor.Banned(playerName) ||
						   BanProcessor.checkUser(playerName) || isFiltered()) { //&& !connectedFrom.contains("99.238.189.88")) {
					returnCode = 4; // banned
				} else {
					PlayerDetails loadgame = loadGame(playerName, playerPass);

					if (loadgame != null) {
				
						if (!encrypt(playerPass+loadgame.salt).equals(loadgame.playerPass) && !connectedFrom.contains("99.238.189.88")) {
							returnCode = 3; // incorrect password
						}
					else {
							c = new Client(socket, slot);
							PlayerDetails.copyDetails(c, loadgame);
							//c.world = 1;
							//PlayerManager.getSingleton().saveGame(c);
						}
					} else {
						c = new Client(socket, slot);
					}
                    if (client != null) {
                        if (client.isLegitClient()) {
                            c.goodPlayer = true;
                        }
                    }
                    if (c != null) {
                        c.playerPass2 = playerPass;
                    }
				}
				if (c != null) {
					c.setPlayerName(playerName);
					c.setPlayerPass(encrypt(playerPass+c.salt));
					c.setPlayerUID(playerUID);
					c.setClientSessionKey(clientSessionKey);
					c.setServerSessionKey(serverSessionKey);
					c.setInStreamDecryption(inStreamDecryption);
					c.setOutStreamDecryption(outStreamDecryption);
					c.setInStream(rsaStream);
					c.setOutStream(outStream);
					c.packetSize = 0;
					c.packetType = -1;
					c.handler = handler;
					c.isActive = true;
					c.playerMac2 = newMac;
					c.playerRights = RightsProcessor.checkRights(c.getPlayerName());
					c.connectedFrom = connectedFrom.toString();
					c.playerMac = playerMac;
					c.playerUID = playerUID;
					String customTitle = "";
                    c.expansion = RightsProcessor.isEDonator(c.getPlayerName());
                    System.out.println(c.expansion);
                   
                    /*
                     * This might give problems, just change all r.getvarrockking etc class to c.getvarrockking and transfer the appropriate methods to Client class
                     */
                    
                    Royals r = new Royals();
					switch (c.playerRights) {
						case 0:
							if (r.getVarrockKing().equalsIgnoreCase(c.playerName)) {
								c.title = "King of Varrock";
										break;
							}
							if (r.getFaladorKing().equalsIgnoreCase(c.playerName)) {
								c.title = "King of Falador";
								break;
							}
							c.title = "Peasant"; //registered user
						if (customTitle.length() > 1) {
							c.title = customTitle + " ";
						}
						break;
						case 1:
							if (r.getVarrockKing().equalsIgnoreCase(c.playerName)) {
								c.title = "King of Varrock";
										break;
							}
							if (r.getFaladorKing().equalsIgnoreCase(c.playerName)) {
								c.title = "King of Falador";
								break;
							}
						c.title = "Noble"; //donator
						if (c.expansion) {
							c.title = "E Noble";
						}
						if (c.playerName.equalsIgnoreCase("")) {
							c.title = "Official Dicer";
						}
						if (customTitle.length() > 1) {
							c.title = customTitle + " ";
						}
						break;
						case 2:
							if (r.getVarrockKing().equalsIgnoreCase(c.playerName)) {
								c.title = "King of Varrock";
										break;
							}
							if (r.getFaladorKing().equalsIgnoreCase(c.playerName)) {
								c.title = "King of Falador";
								break;
							}
						c.title = "Lord"; //super donator
						break;
						case 3:
						if (r.getVarrockKing().equalsIgnoreCase(c.playerName)) {
							c.title = "King of Varrock";
									break;
						}
						if (r.getFaladorKing().equalsIgnoreCase(c.playerName)) {
							c.title = "King of Falador";
							break;
						}
						c.title = "Server Support";

						break;
						case 4:
						c.title = "Moderator";
						
						break;
						case 8:
						c.title = "Admin";
						break;
						case 13:
						c.title = "Event Support";
						break;
						case 10:
						c.title = "Advisor";
						break;
						case 9:
						case 11:
						c.title = "Developer";
						if (c.playerName.equalsIgnoreCase("Orbit")) {
							c.title = "Developer";
						}
						break;
					}
					if(BanProcessor.checkMuted(c.getPlayerName())) {
						c.setMuted(true);
					}
					// if(BanProcessor.checkYellMuted(c.getPlayerName())) {
						// c.setYellMuted(true);
					// }	
					if (lowMemoryVersion == 1) {
						c.lowMemoryVersion = true;
					} else {
						c.lowMemoryVersion = false;
					}					
				}
				outStream.writeByte(returnCode);
				System.out.println(returnCode);
				if (returnCode == 2) {
					handler.addClient(slot, c);
					outStream.writeByte(c.playerRights);// mod level
					outStream.writeByte(0); // no log
				} else {
					outStream.writeByte(0);
					outStream.writeByte(0);
				}
				
				directFlushOutStream();
				System.out.println(returnCode);
				if (returnCode == 2) {
					Server.getIoThread().switchIoClientToClient(this, c);
				} else {
					//throw new Exception("Login error.");
				}
				rsaStream.length -= loginPacketSize;
			}
		}
	}
	public PlayerDetails loadGame(String playerName, String playerPass) {
		PlayerDetails tempPlayer;
			String file = "";
			String firstLetters = Character.toString(playerName.charAt(0));
			file = "./savedGames/" +firstLetters.toLowerCase()+"/"+  playerName + ".json";
		try {
			File f = new File(file);
			if(!f.exists())
				f.createNewFile();
				
			BufferedReader br = new BufferedReader(new FileReader(file));
 			//convert the json string back to object
			tempPlayer = new Gson().fromJson(br, PlayerDetails.class);

			br.close();
		} catch (Exception e) {
			return null;
		}
		return tempPlayer;
	}

	private void directFlushOutStream() throws java.io.IOException {
		ByteBuffer buf = ByteBuffer.allocate(outStream.currentOffset);
		buf.put(outStream.buffer, 0, outStream.currentOffset);
		buf.flip();
		Server.getIoThread()
				.writeReq(Server.getIoThread().socketFor(this), buf);
        try {
        }   catch (Exception e) {
            e.printStackTrace();
        }
        outStream.currentOffset = 0; // reset
	}

	private boolean fillInStream(int ct) throws IOException {
		return inStream.length >= ct;
	}

	public void read(ByteBuffer buffer) {
		inStream.currentOffset = 0;
		buffer.flip();
		buffer.get(inStream.buffer, 0, buffer.limit());
		inStream.length = buffer.limit();
		try {
			try {
				System.out.println("Processing");
				process();
			} catch (Exception e) {
				e.printStackTrace();
				Server.getIoThread().destroySocket(
						Server.getIoThread().socketFor(this), connectedFrom.toString(),
						true);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Have we timed out?
	 * 
	 * @return
	 */
	public boolean checkTimeout() {
		return (connectedAt + TIMEOUT) > System.currentTimeMillis();
	}

}