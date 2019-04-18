package levelMap;

public class FloorTile
{
	public FloorTile(Sector sector, FloorTileType type)
	{
		this.sector = sector;
		this.type = type;
	}

	public final Sector sector;
	public final FloorTileType type;

	public boolean visible()
	{
		return sector.visible;
	}

	public boolean blocked()
	{
		return type.blocked;
	}

	public boolean canMovementEnd()
	{
		return type.canMovementEnd;
	}

	public int moveCost()
	{
		return type.moveCost;
	}
}