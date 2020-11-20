package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.tile.*;
import java.io.*;
import levelmap.*;
import load.*;

public final class TurnResources implements XSaveableY
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

	public TurnResources(boolean unlimited, Tile startLocation, int startMovement, int leftoverMovement, boolean hasMoveAction,
			boolean canRevertMoveAction, boolean hasMainAction)
	{
		this.unlimited = unlimited;
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

	public static TurnResources load(JrsObject data, TileType y1)
	{
		boolean unlimited = LoadHelper.asBoolean(data.get("Unlimited"));
		Tile startLocation = XSaveableY.loadLocation(data, y1);
		int startMovement = LoadHelper.asInt(data.get("StartMovement"));
		int leftoverMovement = LoadHelper.asInt(data.get("LeftoverMovement"));
		boolean hasMoveAction = LoadHelper.asBoolean(data.get("HasMoveAction"));
		boolean canRevertMoveAction = LoadHelper.asBoolean(data.get("CanRevertMoveAction"));
		boolean hasMainAction = LoadHelper.asBoolean(data.get("HasMainAction"));
		return new TurnResources(unlimited, startLocation, startMovement, leftoverMovement, hasMoveAction, canRevertMoveAction, hasMainAction);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, TileType y1) throws IOException
	{
		a1.put("Unlimited", unlimited);
		XSaveableY.saveLocation(startLocation, a1, y1);
		a1.put("StartMovement", startMovement);
		a1.put("LeftoverMovement", leftoverMovement);
		a1.put("HasMoveAction", hasMoveAction);
		a1.put("CanRevertMoveAction", canRevertMoveAction);
		a1.put("HasMainAction", hasMainAction);
	}
}