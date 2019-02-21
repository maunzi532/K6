package levelMap;

public class FullTile
{
	public final FloorTile floorTile;
	public final MBuilding building;
	public final MEntity entity;
	public final boolean marked;

	public FullTile()
	{
		floorTile = null; //Out of bounds floor tile
		building = null;
		entity = null;
		marked = false;
	}

	public FullTile(FloorTile floorTile, MBuilding building, MEntity entity, boolean marked)
	{
		assert floorTile != null;
		this.floorTile = floorTile;
		this.building = building;
		this.entity = entity;
		this.marked = marked;
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