package levelMap;

import entity.*;

public class AdvTile
{
	public static final AdvTile EMPTY = new AdvTile(null);

	private FloorTile floorTile;
	private MBuilding building;
	private XEntity entity;
	private MBuilding owned;

	public AdvTile(FloorTile floorTile)
	{
		this.floorTile = floorTile;
	}

	public FloorTile getFloorTile()
	{
		return floorTile;
	}

	public MBuilding getBuilding()
	{
		if(building != null && building.active())
			return building;
		return null;
	}

	public XEntity getEntity()
	{
		return entity;
	}

	public MBuilding getOwned()
	{
		if(owned != null && owned.active())
			return owned;
		return null;
	}

	public void setFloorTile(FloorTile floorTile)
	{
		this.floorTile = floorTile;
	}

	public void setBuilding(MBuilding building)
	{
		this.building = building;
	}

	public void setEntity(XEntity entity)
	{
		this.entity = entity;
	}

	public void setOwned(MBuilding owned)
	{
		this.owned = owned;
	}

	public boolean visible()
	{
		return floorTile != null && floorTile.visible();
	}
}