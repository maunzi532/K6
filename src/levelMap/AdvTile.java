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
	private XBuilding ownedBy;

	public AdvTile(FloorTile floorTile)
	{
		this.floorTile = floorTile;
	}

	public FloorTile floorTile()
	{
		return floorTile;
	}

	public XBuilding building()
	{
		if(building != null && building.active())
			return building;
		return null;
	}

	public XCharacter entity()
	{
		return entity;
	}

	public XBuilding ownedBy()
	{
		if(ownedBy != null && ownedBy.active())
			return ownedBy;
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

	public void setOwnedBy(XBuilding ownedBy)
	{
		this.ownedBy = ownedBy;
	}

	public boolean visible()
	{
		return floorTile != null;
	}
}