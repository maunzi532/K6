package building.blueprint;

import levelMap.*;

public class RequiresFloorTiles
{
	public final FloorTileType floorTileType;
	public final int amount;
	public final int minRange;
	public final int maxRange;

	public RequiresFloorTiles(FloorTileType floorTileType, int amount, int minRange, int maxRange)
	{
		this.floorTileType = floorTileType;
		this.amount = amount;
		this.minRange = minRange;
		this.maxRange = maxRange;
	}
}