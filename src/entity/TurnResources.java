package entity;

import geom.tile.*;

public final class TurnResources
{
	private Tile startLocation;
	private int startMovement;
	private int startDashMovement;
	private int movement;
	private int dashMovement;
	private int actionPoints;
	private boolean moveAction;
	private boolean revertMoveAction;
	private boolean mainAction;

	public TurnResources(Tile startLocation)
	{
		this.startLocation = startLocation;
		startMovement = 0;
		startDashMovement = 0;
		movement = 0;
		dashMovement = 0;
		actionPoints = 0;
		moveAction = false;
		revertMoveAction = false;
		mainAction = false;
	}

	public TurnResources(Tile startLocation, int startMovement, int startDashMovement, int startActionPoints)
	{
		this.startLocation = startLocation;
		this.startMovement = startMovement;
		this.startDashMovement = startDashMovement;
		movement = startMovement;
		dashMovement = startDashMovement;
		actionPoints = startActionPoints;
		moveAction = true;
		revertMoveAction = true;
		mainAction = true;
	}

	private TurnResources(Tile startLocation, int startMovement, int startDashMovement, int movement, int dashMovement,
			int actionPoints, boolean moveAction, boolean revertMoveAction, boolean mainAction)
	{
		this.startLocation = startLocation;
		this.startMovement = startMovement;
		this.startDashMovement = startDashMovement;
		this.movement = movement;
		this.dashMovement = dashMovement;
		this.actionPoints = actionPoints;
		this.moveAction = moveAction;
		this.revertMoveAction = revertMoveAction;
		this.mainAction = mainAction;
	}

	public TurnResources copy(Tile copyLocation)
	{
		return new TurnResources(copyLocation, startMovement, startDashMovement, startMovement,
				startDashMovement, actionPoints, moveAction, moveAction, mainAction);
	}

	public Tile startLocation()
	{
		return startLocation;
	}

	public int movement()
	{
		return movement;
	}

	public int dashMovement()
	{
		return dashMovement;
	}

	public int actionPoints()
	{
		return actionPoints;
	}

	public boolean moveAction()
	{
		return moveAction;
	}

	public boolean revertMoveAction()
	{
		return revertMoveAction;
	}

	public boolean mainAction()
	{
		return mainAction;
	}

	public boolean ready(int apCost)
	{
		return mainAction && actionPoints >= apCost;
	}

	public void move(int cost)
	{
		moveAction = false;
		movement -= cost;
		dashMovement -= cost;
	}

	public void action(boolean main, int ap)
	{
		if(main)
			mainAction = false;
		actionPoints -= ap;
		revertMoveAction = false;
	}

	public void revertMovement()
	{
		movement = startMovement;
		dashMovement = startDashMovement;
		moveAction = true;
	}
}