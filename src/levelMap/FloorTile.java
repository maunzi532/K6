package levelMap;

public class FloorTile
{
	public FloorTile(int sector, FloorTileType type)
	{
		this.sector = sector;
		this.type = type;
	}

	public final int sector;
	public final FloorTileType type;

	public boolean visible(LevelMap levelMap)
	{
		return levelMap.sectorVisible(sector);
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