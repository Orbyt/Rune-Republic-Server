package com.rs.worldserver.events;

import java.util.ArrayList;
import java.util.List;

import com.rs.worldserver.Server;
import com.rs.worldserver.model.player.Client;
import com.rs.worldserver.events.FollowingEvent;

/**
 * Manages events which will be run in the future. Has its own thread since some
 * events may need to be ran faster than the cycle time in the main thread.
 * 
 * @author Graham
 * 
 */
public class EventManager implements Runnable {

	/**
	 * A reference to the singleton;
	 */
	private static EventManager singleton = null;

	/**
	 * A list of events that are being executed.
	 */
	private List<EventContainer> events;

	/**
	 * A list of events to add.
	 */
	private List<EventContainer> eventsToAdd;

	/**
	 * Initialise the event manager.
	 */
	private EventManager() {
		events = new ArrayList<EventContainer>();
		eventsToAdd = new ArrayList<EventContainer>();
	}

	/**
	 * The event manager thread. So we can interrupt it and end it nicely on
	 * shutdown.
	 */
	private Thread thread;

	/**
	 * Gets the event manager singleton. If there is no singleton, the singleton
	 * is created.
	 * 
	 * @return The event manager singleton.
	 */
	public static EventManager getSingleton() {
		if (singleton == null) {
			singleton = new EventManager();
			singleton.thread = new Thread(singleton, "eventManager");
			singleton.thread.start();
		}
		return singleton;
	}

	/**
	 * Initialises the event manager (if it needs to be).
	 */
	public static void initialise() {
		getSingleton();
		getSingleton().addGlobalEvents();
	}

	/**
	 * The waitFor variable is multiplied by this before the call to wait() is
	 * made. We do this because other events may be executed after waitFor is
	 * set (and take time). We may need to modify this depending on event count?
	 * Some proper tests need to be done.
	 */
	private static final double WAIT_FOR_FACTOR = 0.5;

	/**
	 * Have we shutdown?
	 */
	private boolean isShutdown = false;

	/**
	 * Toggle shutdown.
	 */
	private boolean toggleShutdown = false;

	@Override
	/*
	 * Processes events. Works kinda like newer versions of cron.
	 */
	public void run() {
		long waitFor = -1;
		List<EventContainer> remove = new ArrayList<EventContainer>();

		while (true) {

			if (toggleShutdown) {
				isShutdown = true;
				break;
			}

			synchronized (eventsToAdd) {
				for (EventContainer c : eventsToAdd) {
					events.add(c);
				}
				eventsToAdd.clear();
			}

			// Log.debug(Log.MAIN, "Executing events");

			// reset wait time
			waitFor = -1;

			// process all events
			for (EventContainer container : events) {
				if (container.isRunning()) {
					if ((System.currentTimeMillis() - container.getLastRun()) >= container
							.getTick()) {
						try {
							synchronized (Server.gameLogicLock) {
								container.execute();
							}
						} catch (Exception e) {
							e.printStackTrace();
							remove.add(container);
						}
					}
					if (container.getTick() < waitFor || waitFor == -1) {
						waitFor = container.getTick();
					}
				} else {
					// add to remove list
					remove.add(container);
				}
			}

			// remove events that have completed
			for (EventContainer container : remove) {
				events.remove(container);
			}
			remove.clear();

			// no events running
			try {
				if (waitFor == -1) {
					// Log.debug(Log.MAIN, "Waiting indefinitely");
					synchronized (this) {
						wait(); // wait with no timeout
					}
				} else {
					// an event is running, wait for that time or until a new
					// event is added
					int decimalWaitFor = (int) (Math.ceil(waitFor
							* WAIT_FOR_FACTOR));
					// Log.debug(Log.MAIN, "Waiting for " + decimalWaitFor);
					synchronized (this) {
						wait(decimalWaitFor);
					}
				}
			} catch (InterruptedException e) {
				isShutdown = true;
				break; // stop running
			}
		}
		System.out.println("Event manager shut down.");
		if (Server.getIoThread().isShutdown()) {
			System.exit(0);
		}
	}

	/**
	 * Adds an event.
	 * 
	 * @param object
	 *            The owner.
	 * @param event
	 *            The event to add.
	 * @param tick
	 *            The tick time.
	 */
	public void addEvent(Object owner,String label, Event event, int tick) {
		synchronized (eventsToAdd) {
			eventsToAdd.add(new EventContainer(owner, event, tick,label));
		}
		synchronized (this) {
			notify();
		}
	}

	/**
	 * Stops events
	 * 
	 * @param object
	 *            The owner.
	 */
	public void stopEvents(Object owner) {
		synchronized (events) {
			for (EventContainer container : events) {
				if (container.getOwner() == owner) {
					container.stop();
					container = null; // <- should be GC'd
				}
			}
		}
		synchronized (this) {
			notify();
		}
	}
	 
	public void writeEvents(){
		synchronized (events) {
			for (EventContainer container : events) {
				Client d = (Client) container.getOwner();
				if(d != null){
					d.eventUsers(d.getPlayerName(), container.getLabel());
				}
			}
		}
	}
	/**
	 * Adds global events
	 */
	public void addGlobalEvents() {
		addEvent(null, "following event", new FollowingEvent(), 100);
	}	
	/**
	 * Shuts the event manager down.
	 */
	public void shutdown() {
		this.thread.interrupt();
		toggleShutdown = true;
	}

	/**
	 * Have we shutdown?
	 * 
	 * @return
	 */
	public boolean isShutdown() {
		return isShutdown;
	}

	public int getEventCount() {
		synchronized (events) {
			return events.size();
		}
	}

}