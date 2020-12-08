package levelmap;

import doubleinv.*;
import entity.*;

public final class AdvTile
{
	public static final AdvTile EMPTY = new AdvTile(null);

	private FloorTile floorTile;
	private XCharacter entity;
	private StartingLocation4 startingLocation;

	public AdvTile(FloorTile floorTile)
	{
		this.floorTile = floorTile;
	}

	public FloorTile floorTile()
	{
		return floorTile;
	}

	public XCharacter entity()
	{
		return entity;
	}

	public StartingLocation4 startingLocation()
	{
		return startingLocation;
	}

	public void setFloorTile(FloorTile floorTile)
	{
		this.floorTile = floorTile;
	}

	public void setEntity(XCharacter entity)
	{
		this.entity = entity;
	}

	public void setStartingLocation(StartingLocation4 startingLocation)
	{
		this.startingLocation = startingLocation;
	}

	public boolean visible()
	{
		return floorTile != null;
	}
}