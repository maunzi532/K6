package levelMap;

import building.adv.*;
import doubleinv.*;
import entity.*;

public class AdvTile
{
	public static final AdvTile EMPTY = new AdvTile(null);

	private FloorTile floorTile;
	private XBuilding building;
	private XCharacter entity;
	private XBuilding owned;

	public AdvTile(FloorTile floorTile)
	{
		this.floorTile = floorTile;
	}

	public FloorTile getFloorTile()
	{
		return floorTile;
	}

	public XBuilding getBuilding()
	{
		if(building != null && building.active())
			return building;
		return null;
	}

	public XCharacter getEntity()
	{
		return entity;
	}

	public XBuilding getOwned()
	{
		if(owned != null && owned.active())
			return owned;
		return null;
	}

	public void setFloorTile(FloorTile floorTile)
	{
		this.floorTile = floorTile;
	}

	public void setBuilding(XBuilding building)
	{
		this.building = building;
	}

	public void setEntity(XCharacter entity)
	{
		this.entity = entity;
	}

	public void setOwned(XBuilding owned)
	{
		this.owned = owned;
	}

	public boolean visible(LevelMap levelMap)
	{
		return floorTile != null && levelMap.sectorVisible(floorTile.sector);
	}
}