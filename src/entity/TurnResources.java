package entity;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.tile.*;
import java.io.*;
import levelmap.*;
import load.*;

public final class TurnResources implements XSaveableY
{
	private Tile startLocation;
	private boolean hasMoveAction;
	private boolean canRevertMoveAction;
	private boolean hasMainAction;

	private TurnResources()
	{
		startLocation = null;
		hasMoveAction = false;
		canRevertMoveAction = false;
		hasMainAction = false;
	}

	public TurnResources(Tile startLocation)
	{
		this.startLocation = startLocation;
		hasMoveAction = true;
		canRevertMoveAction = true;
		hasMainAction = true;
	}

	public TurnResources(Tile startLocation, boolean hasMoveAction,
			boolean canRevertMoveAction, boolean hasMainAction)
	{
		this.startLocation = startLocation;
		this.hasMoveAction = hasMoveAction;
		this.canRevertMoveAction = canRevertMoveAction;
		this.hasMainAction = hasMainAction;
	}

	public Tile startLocation()
	{
		return startLocation;
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

	public void move()
	{
		hasMoveAction = false;
	}

	public void action(boolean isMainAction)
	{
		if(isMainAction)
			hasMainAction = false;
		if(!hasMoveAction)
			canRevertMoveAction = false;
	}

	public void revertMovement()
	{
		hasMoveAction = true;
	}

	public static TurnResources load(JrsObject data, TileType y1)
	{
		Tile startLocation = XSaveableY.loadLocation(data, y1);
		boolean hasMoveAction = LoadHelper.asBoolean(data.get("HasMoveAction"));
		boolean canRevertMoveAction = LoadHelper.asBoolean(data.get("CanRevertMoveAction"));
		boolean hasMainAction = LoadHelper.asBoolean(data.get("HasMainAction"));
		return new TurnResources(startLocation, hasMoveAction, canRevertMoveAction, hasMainAction);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, TileType y1) throws IOException
	{
		XSaveableY.saveLocation(startLocation, a1, y1);
		a1.put("HasMoveAction", hasMoveAction);
		a1.put("CanRevertMoveAction", canRevertMoveAction);
		a1.put("HasMainAction", hasMainAction);
	}
}