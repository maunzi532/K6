package building.blueprint;

import file.*;
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

	public RequiresFloorTiles(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		floorTileType = FloorTileType.valueOf(node.get(0).data);
		amount = node.get(1).dataInt();
		minRange = node.get(2).dataInt();
		maxRange = node.get(3).dataInt();
	}
}