package com.rs.worldserver.model.player;

import java.util.ArrayList;

import com.rs.worldserver.world.PlayerManager;

public class DoorManager
{

	private static ArrayList<Door> doors = new ArrayList<Door>();
	static
	{
		doors.add(new Door(3226, 3214, 1530, -2, -1));
		doors.add(new Door(3106, 3958, 734, -1, -1));
		doors.add(new Door(3105, 3958, 734, -1, -1));
		doors.add(new Door(3158, 3951, 734, -3, -3));
		doors.add(new Door(3093, 3957, 733, -3, -3));
		doors.add(new Door(3095, 3957, 734, 0, 0));
		doors.add(new Door(3095, 3957, 734, 0, 0));
		
		doors.add(new Door(3213, 3470, 1519, -2,-2)); //varrock castle
		doors.add(new Door(2958, 3821, 1519, -2,-2)); //church south of ice plat
	}

	public void door(Client c, int id, int x, int y)
	{
		if (System.currentTimeMillis() - c.getActionAssistant().lastDoor < 800)
			return;
		for (Door i : doors)
		{
			if (i.getID() == id && i.getAbsX() == x && i.getAbsY() == y)
			{
				c.getActionAssistant().lastDoor = System.currentTimeMillis();
				execute(id, i);
				break;
			}
		}
	}

	public void execute(int id, Door i)
	{
		doors.remove(i);
		for (Player p : PlayerManager.getSingleton().getPlayers())
		{
			if (p == null)
				continue;
			if (!p.isActive)
				continue;
			if (p.disconnected)
				continue;
			Client player = (Client) p;
			player.getActionAssistant().sendReplaceObject(i.getAbsX(), i.getAbsY(), id, 
				((i.wasMoved() && i.statusOp()) ? i.getOFace() : i.getNFace()), 0);
		}
		i.setMoved(true);
		i.setStatus((i.statusOp()) ? false : true);
		doors.add(i);
	}

	public static void loadDoors(Client client)
	{
		for (Door i : doors)
		{
			if (i.wasMoved())
			{
				client.getActionAssistant().sendReplaceObject(i.getAbsX(), i.getAbsY(),
					i.getID(), ((i.statusOp()) ? i.getOFace() : i.getNFace()), 0);
			}
		}
	}

	private static class Door
	{

		public Door(int x, int y, int id, int oface, int nface)
		{
			this.absX = x;
			this.absY = y;
			this.id = id;
			this.originalFace = oface;
			this.newFace = nface;
			this.op = false;
			this.moved = false;
		}

		private int absX;
		private int absY;
		private int id;
		private int originalFace;
		private int newFace;
		private boolean op;
		private boolean moved;

		public int getAbsX()
		{
			return absX;
		}

		public int getAbsY()
		{
			return absY;
		}

		public int getTrueX()
		{
			return absX;
		}

		public int getTrueY()
		{
			return absY;
		}

		public int getID()
		{
			return id;
		}

		public int getOFace()
		{
			return originalFace;
		}

		public int getNFace()
		{
			return newFace;
		}

		public boolean statusOp()
		{
			return op;
		}

		public void setStatus(boolean val)
		{
			op = val;
		}

		public boolean wasMoved()
		{
			return moved;
		}

		public void setMoved(boolean val)
		{
			moved = val;
		}
	}
}