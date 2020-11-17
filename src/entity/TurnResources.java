package entity;

import geom.tile.*;

public final class TurnResources
{
	private boolean unlimited;
	private Tile startLocation;
	private int startMovement;
	private int leftoverMovement;
	private boolean hasMoveAction;
	private boolean canRevertMoveAction;
	private boolean hasMainAction;

	private TurnResources()
	{
		unlimited = true;
		startLocation = null;
		startMovement = 0;
		leftoverMovement = 0;
		hasMoveAction = false;
		canRevertMoveAction = false;
		hasMainAction = false;
	}

	public static TurnResources unlimited()
	{
		return new TurnResources();
	}

	public TurnResources(Tile startLocation)
	{
		unlimited = false;
		this.startLocation = startLocation;
		startMovement = 0;
		leftoverMovement = 0;
		hasMoveAction = false;
		canRevertMoveAction = false;
		hasMainAction = false;
	}

	public TurnResources(Tile startLocation, int startMovement)
	{
		unlimited = false;
		this.startLocation = startLocation;
		this.startMovement = startMovement;
		leftoverMovement = startMovement;
		hasMoveAction = true;
		canRevertMoveAction = true;
		hasMainAction = true;
	}

	public TurnResources(Tile startLocation, int startMovement, int leftoverMovement, boolean hasMoveAction,
			boolean canRevertMoveAction, boolean hasMainAction)
	{
		unlimited = false;
		this.startLocation = startLocation;
		this.startMovement = startMovement;
		this.leftoverMovement = leftoverMovement;
		this.hasMoveAction = hasMoveAction;
		this.canRevertMoveAction = canRevertMoveAction;
		this.hasMainAction = hasMainAction;
	}

	public Tile startLocation()
	{
		return startLocation;
	}

	public int startMovement()
	{
		return startMovement;
	}

	public int leftoverMovement()
	{
		return leftoverMovement;
	}

	public boolean hasMoveAction()
	{
		return hasMoveAction;
	}

	public boolean canRevertMoveAction()
	{
		return canRevertMoveAction;
	}

	public boolean hasMainAction()
	{
		return hasMainAction;
	}

	public boolean ready()
	{
		return unlimited || hasMainAction;
	}

	public void move(int movementCost)
	{
		hasMoveAction = false;
		leftoverMovement -= movementCost;
	}

	public void action(boolean isMainAction)
	{
		if(!unlimited)
		{
			if(isMainAction)
				hasMainAction = false;
			if(!hasMoveAction)
				canRevertMoveAction = false;
		}
	}

	public void revertMovement()
	{
		leftoverMovement = startMovement;
		hasMoveAction = true;
	}
}