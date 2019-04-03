package levelMap;

import entity.*;

public class FullTile
{
	public final FloorTile floorTile;
	public final MBuilding building;
	public final XEntity entity;
	public final MarkType marked;

	public FullTile()
	{
		floorTile = null; //Out of bounds floor tile
		building = null;
		entity = null;
		marked = MarkType.NOT;
	}

	public FullTile(FloorTile floorTile, MBuilding building, XEntity entity, MarkType marked)
	{
		assert floorTile != null;
		this.floorTile = floorTile;
		this.building = building;
		this.entity = entity;
		this.marked = marked != null ? marked : MarkType.NOT;
	}

	public boolean exists()
	{
		return floorTile != null;
	}

	public boolean visible()
	{
		return floorTile != null && floorTile.visible();
	}
}