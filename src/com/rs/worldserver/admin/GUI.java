package com.rs.worldserver.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.rs.worldserver.Server;
import com.rs.worldserver.events.EventManager;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.model.player.Player;
import com.rs.worldserver.util.BanProcessor;
import com.rs.worldserver.util.AntiDupe;

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

/**
 * Server gui management tool
 * 
 * @author Graham
 * 
 */
public class GUI implements WindowListener, Runnable {

	/*
	 * The window.
	 */
	private JFrame window;

	/*
	 * The server tab.
	 */
	private JPanel serverPanel;
	private JLabel serverPlayerCount;
	private JLabel processTime;
	private JLabel eventCount;

	/*
	 * The console tab.
	 */
	private JPanel consolePanel;
	private JTextArea console;
	private JScrollPane consolePane;

	/*
	 * The players tab.
	 */
	private JPanel playerListPanel;
	private JPanel playerListButtonPanel;
	private JLabel playerListText;
	private JList playerList;
	private DefaultListModel playerListModel;
	private JScrollPane playerListPane;
	
	/*
	 * 
	 * The AntiDupe tab
	 */
	private JPanel antiDupePanel;
	private JPanel btnPanel;
	private JPanel infoPanel;
	private JLabel limeAmount;
	private JLabel lavaAmount;
	private JLabel ccbAmount;
	private JLabel gpAmount;
	

	/*
	 * Global stuff.
	 */
	private JMenuBar menuBar;

	private JTabbedPane tabs;

	/*
	 * Used to create the menus easily.
	 */
	private static final String[] menus = { "File", "Help" };
	private static final String[][] menuItems = { new String[] { "Exit" },
			new String[] { "About" }, };

	private static final class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getActionCommand().equals("About")) {
				Server.getGui().showAboutDialog();
			} else if (evt.getActionCommand().equals("Exit")) {
				Server.getGui().window.setEnabled(false);
				Server.shutdown();
			}
		}

	}

	private static final class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent evt) {
			
			if (evt.getActionCommand().equals("Run")) {
				System.out.println("Anti Dupe Run button works");
			} else {
			int idx = Server.getGui().playerList.getSelectedIndex();
			if (idx == -1)
				return;
			String name = (String) Server.getGui().playerListModel.get(idx);
			if (evt.getActionCommand().equals("Kick")) {
				  Server.getPlayerManager().kick(name);
				  
			} else if (evt.getActionCommand().equals("Ban name")) {
				try {
					BanProcessor.banUser(name,"Server");
				} catch (IOException e) {
					Server.getGui().handleException(e);
				}
			} else if (evt.getActionCommand().equals("Ban uid")) {
				Player p = Server.getPlayerManager().getPlayerByName(name);
				if (p == null)
					return;
				Client c = (Client) p;
				try {
					BanProcessor.banUID(c.getPlayerUID());
				} catch (IOException e) {
					Server.getGui().handleException(e);
				}
			} else if (evt.getActionCommand().equals("Ban address")) {
				Player p = Server.getPlayerManager().getPlayerByName(name);
				if (p == null)
					return;
				try {
					BanProcessor.banIP(p.connectedFrom, "Server");
				} catch (IOException e) {
					Server.getGui().handleException(e);
				}
			} else if (evt.getActionCommand().equals("Mute")) {
				Player p = Server.getPlayerManager().getPlayerByName(name);
				if (p == null)
					return;
				Client c = (Client) p;
				c.setMuted(true);
				try {
					BanProcessor.muteUser(name,"Server");
				} catch (IOException e) {
					Server.getGui().handleException(e);
				}
			} else if (evt.getActionCommand().equals("Mute (yell)")) {
				Player p = Server.getPlayerManager().getPlayerByName(name);
				if (p == null)
					return;
				Client c = (Client) p;
				c.setYellMuted(true);
				try {
					BanProcessor.yellMuteUser(name);
				} catch (IOException e) {
					Server.getGui().handleException(e);
				}			
			}
		}
		}

	}

	public GUI() {
		// ============================
		// Set the look and feel
		// ============================
		try {
			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceChallengerDeepLookAndFeel");
		} catch (Exception e) {
		} // leave default laf

		// ============================
		// Setup window
		// ============================
		window = new JFrame("Server Console");
		window.setSize(600, 400);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(this);

		// ============================
		// Setup tabs
		// ============================
		tabs = new JTabbedPane();

		// ============================
		// Setup statistics
		// ============================
		serverPanel = new JPanel();
		serverPanel.setLayout(new GridLayout(3, 2));
		serverPanel.add(new JLabel("Process time:"));
		processTime = new JLabel();
		serverPanel.add(processTime);
		serverPanel.add(new JLabel("Players online:"));
		serverPlayerCount = new JLabel("0");
		serverPanel.add(serverPlayerCount);
		serverPanel.add(new JLabel("Events running:"));
		eventCount = new JLabel("0");
		serverPanel.add(eventCount);
		serverPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		tabs.add("Server", serverPanel);

		// ============================
		// Setup system information
		// ============================
		Properties properties = System.getProperties();
		JPanel propertiesPanel = new JPanel();
		propertiesPanel.setLayout(new GridLayout(properties.size(), 1));
		Enumeration<?> props = properties.propertyNames();
		while (props.hasMoreElements()) {
			String name = (String) props.nextElement();
			propertiesPanel.add(new JLabel(name + ": "
					+ System.getProperty(name)));
		}

		tabs.add("System Information", new JScrollPane(propertiesPanel));

		// ============================
		// Setup console
		// ============================
		consolePanel = new JPanel();
		consolePanel.setLayout(new BorderLayout());
		console = new JTextArea();
		console.setEditable(false);
		console.setBackground(Color.BLACK);
		console.setCaretColor(Color.WHITE);
		console.getCaret().setVisible(true);
		console.setDisabledTextColor(Color.WHITE);
		console.setForeground(Color.WHITE);
		consolePane = new JScrollPane(console);
		consolePanel.add(consolePane);
		consolePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		tabs.add("Console", consolePanel);

		// ============================
		// Setup player list
		// ============================
		String[] buttons = new String[] { "Kick", "Ban name", "Ban address",
				"Ban uid", "Mute", "Mute (yell)" };

		playerListPanel = new JPanel();
		playerListPanel.setLayout(new BorderLayout());
		playerListModel = new DefaultListModel();
		playerList = new JList(playerListModel);
		playerListPane = new JScrollPane(playerList);
		playerListPanel.add(playerListPane);
		playerListButtonPanel = new JPanel();

		playerListButtonPanel.setLayout(new GridLayout(buttons.length, 1));

		// ============================
		// Setup player list buttons
		// ============================
		ButtonListener btnListener = new ButtonListener();
		for (String button : buttons) {
			JButton b = new JButton(button);
			b.addActionListener(btnListener);
			playerListButtonPanel.add(b);
		}

		// ============================
		// Finish player list setup
		// ============================
		playerListText = new JLabel("Players online: 0");
		playerListPanel.add(playerListButtonPanel, BorderLayout.EAST);
		playerListPanel.add(playerListText, BorderLayout.NORTH);
		playerListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		tabs.add("Players", playerListPanel);

		
		// =========================
		// Setup AntiDupe tab
		// =========================
		
		//Main Panel for Anti Dupe Tab
		antiDupePanel = new JPanel();
		antiDupePanel.setLayout(new BorderLayout());
		
		// Sets up Anti Dupe Buttons
		String [] antiDupeButtons = {"Run"};
		btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(antiDupeButtons.length, 1));
		ButtonListener listenerB = new ButtonListener();
		for (String btn : antiDupeButtons) {
			JButton b = new JButton(btn);
			b.addActionListener(listenerB);
			btnPanel.add(b);
		}
		
		//Sets up Info Area and calls for item amounts from class AntiDupe
		AntiDupe aD = new AntiDupe();
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(4, 2));
		String tester = "lol";
		infoPanel.add(new JLabel("Amount of Limes = " + aD.getAmountLimes()));
		infoPanel.add(new JLabel("Amount of Lavas ="));
		infoPanel.add(new JLabel("Amount of CCB's ="));
		infoPanel.add(new JLabel("Amount of GP ="));
		
		
		antiDupePanel.add(btnPanel, BorderLayout.EAST);
		antiDupePanel.add(infoPanel, BorderLayout.WEST);
		
		
		
		tabs.add("Anti Dupe", antiDupePanel);
		
		
		
		// ============================
		// Setup menu bar
		// ============================
		menuBar = new JMenuBar();
		int id = 0;
		MenuListener menuListener = new MenuListener();
		for (String menu : menus) {
			JMenu jmenu = new JMenu(menu);
			for (String menuItem : menuItems[id]) {
				JMenuItem jmi = new JMenuItem(menuItem);
				jmi.addActionListener(menuListener);
				jmenu.add(jmi);
			}
			menuBar.add(jmenu);
			id++;
		}

		window.setJMenuBar(menuBar);

		// ============================
		// Setup window layout
		// ============================
		window.setLayout(new BorderLayout());
		window.getContentPane().add(tabs, BorderLayout.CENTER);

		// ============================
		// Display the GUI
		// ============================
		window.setVisible(true);

		// ============================
		// Start statistic collection
		// ============================
		(new Thread(this)).start();
	}

	public synchronized void log(final String s) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				synchronized (console) {
					// ============================
					// Append text
					// ============================
					console.append(s + "\r\n");

					// ============================
					// Force console to scroll down
					// ============================
					console.getCaret().setDot(console.getText().length());
				}
			}
		});
	}

	public void handleException(Exception e) {
		// ============================
		// Build a stack trace
		// ============================
		StringBuilder stack = new StringBuilder();
		for (StackTraceElement stackTraceElement : e.getStackTrace()) {
			stack.append(stackTraceElement.toString() + "\r\n");
		}
		// ============================
		// Show the dialogue
		// ============================
		JOptionPane.showMessageDialog(window, e.getMessage()
				+ "\r\n\r\nSTACK TRACE:\r\n" + stack.toString(), "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	public void showAboutDialog() {
		// ============================
		// Show the dialogue
		// ============================
		JOptionPane.showMessageDialog(window, Server.CREDITS, "About",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public synchronized void addPlayer(final String name) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// ============================
				// Add to list
				// ============================
				playerListModel.addElement(name);
				// ============================
				// Update player count
				// ============================
				playerListText.setText("Players Online: "
						+ playerListModel.size());
				serverPlayerCount.setText("" + playerListModel.size());
			}
		});
	}

	public synchronized void removePlayer(final String name) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// ============================
				// Remove from list
				// ============================
				playerListModel.removeElement(name);
				// ============================
				// Update player count
				// ============================
				playerListText.setText("Players Online: "
						+ playerListModel.size());
				serverPlayerCount.setText("" + playerListModel.size());
			}
		});
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		window.setEnabled(false);
		Server.shutdown();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void run() {
		while (true) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					eventCount.setText(""
							+ EventManager.getSingleton().getEventCount());
					processTime.setText(Server.getLastProcessTime()
							+ "ms (average: " + Server.getAverageProcessTime()
							+ "ms)");
				}
			});
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
